package com.teambition.recurrencerule.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

public class CheckableDrawable extends Drawable {
    private final int ANIMATION_DURATION_EXPAND = 500, ANIMATION_DURATION_COLLAPSE = 400;
    private int mMinAlpha, mMaxAlpha;
    private Paint mPaint;

    private AnimatorSet asTransition;
    private final OvershootInterpolator mExpandInterpolator = new OvershootInterpolator();
    private final AnticipateInterpolator mCollapseInterpolator = new AnticipateInterpolator();
    private final LinearInterpolator mLinearInterpolator = new LinearInterpolator();
    private final CRectFEvaluator mRectEvaluator = new CRectFEvaluator();

    private RectF mRectToDraw, mCollapsedRect, mExpandedRect;
    private int mExpandedWidthHeight;

    private boolean mChecked, mReady;

    private boolean isLinearExpand = false;

    public CheckableDrawable(int color, boolean checked, int expandedWithHeight) {
        this(color, checked, expandedWithHeight, false);
    }

    public CheckableDrawable(int color, boolean checked, int expandedWithHeight, boolean isLinearExpand) {
        mChecked = checked;
        mExpandedWidthHeight = expandedWithHeight;
        this.isLinearExpand = isLinearExpand;

        mMaxAlpha = Color.alpha(color);
        mMinAlpha = 0;

        mRectToDraw = new RectF();
        mExpandedRect = new RectF();
        mCollapsedRect = new RectF();
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAlpha(mMaxAlpha);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    // initialize dimensions
    private void setDimens(int width, int height) {
        mReady = true;

        float expandedLeft = (width - mExpandedWidthHeight) / 2f;
        float expandedTop = (height - mExpandedWidthHeight) / 2f;
        float expandedRight = (width + mExpandedWidthHeight) / 2f;
        float expandedBottom = (height + mExpandedWidthHeight) / 2f;

        float collapsedLeft = width / 2f;
        float collapsedTop = height / 2f;
        float collapsedRight = width / 2f;
        float collapsedBottom = height / 2f;

        mCollapsedRect = new RectF(collapsedLeft, collapsedTop,
                collapsedRight, collapsedBottom);
        mExpandedRect = new RectF(expandedLeft, expandedTop,
                expandedRight, expandedBottom);

        reset();
    }


    // Called when 'WeekButton' checked state changes
    public void setCheckedOnClick(boolean checked, final OnAnimationDone callback) {
        mChecked = checked;
        if (!mReady) {
            invalidateSelf();
            return;
        }
        reset();
        onClick(callback);
    }

    private void onClick(final OnAnimationDone callback) {
        animate(mChecked, callback);
    }

    private void cancelAnimationInTracks() {
        if (asTransition != null && asTransition.isRunning()) {
            asTransition.cancel();
        }
    }

    // Set state without animation
    public void setChecked(boolean checked) {
        if (mChecked == checked)
            return;

        mChecked = checked;
        reset();
    }

    private void reset() {
        cancelAnimationInTracks();

        if (mChecked) {
            mRectToDraw.set(mExpandedRect);
        } else {
            mRectToDraw.set(mCollapsedRect);
        }

        invalidateSelf();
    }

    // Animate between 'on' & 'off' state
    private void animate(boolean expand, final OnAnimationDone callback) {
        RectF from = expand ? mCollapsedRect : mExpandedRect;
        RectF to = expand ? mExpandedRect : mCollapsedRect;

        mRectToDraw.set(from);

        ObjectAnimator oaTransition = ObjectAnimator.ofObject(this,
                "newRectBounds",
                mRectEvaluator, from, to);

        int duration = expand ?
                ANIMATION_DURATION_EXPAND :
                ANIMATION_DURATION_COLLAPSE;

        oaTransition.setDuration(duration);
        oaTransition.setInterpolator(expand ?
                (isLinearExpand ? mLinearInterpolator : mExpandInterpolator) :
                (isLinearExpand ? mLinearInterpolator : mCollapseInterpolator));

        ObjectAnimator oaAlpha = ObjectAnimator.ofInt(this,
                "alpha",
                expand ? mMinAlpha : mMaxAlpha,
                expand ? mMaxAlpha : mMinAlpha);
        oaAlpha.setDuration(duration);

        asTransition = new AnimatorSet();
        asTransition.playTogether(oaTransition, oaAlpha);

        asTransition.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (callback != null) {
                    callback.animationIsDone();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);

                if (callback != null) {
                    callback.animationHasBeenCancelled();
                }
            }
        });

        asTransition.start();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mReady) {
            setDimens(getBounds().width(), getBounds().height());
            return;
        }

        canvas.drawOval(mRectToDraw, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    // ObjectAnimator property
    @SuppressWarnings("unused")
    public void setNewRectBounds(RectF newRectBounds) {
        mRectToDraw = newRectBounds;
        invalidateSelf();
    }

    // Callback
    public interface OnAnimationDone {
        void animationIsDone();

        void animationHasBeenCancelled();
    }

    static class CRectFEvaluator implements TypeEvaluator<RectF> {
        /**
         * When null, a new Rect is returned on every evaluate call. When non-null,
         * mRect will be modified and returned on every evaluate.
         */
        private RectF mRectF;

        /**
         * Construct a RectEvaluator that returns a new Rect on every evaluate call.
         * To avoid creating an object for each evaluate call,
         * {@link CRectFEvaluator#CRectFEvaluator(RectF)} should be used
         * whenever possible.
         */
        public CRectFEvaluator() {
        }

        /**
         * Constructs a RectEvaluator that modifies and returns <code>reuseRect</code>
         * in #evaluate(float, android.graphics.RectF, android.graphics.Rect) calls.
         * The value returned from
         * #evaluate(float, android.graphics.RectF, android.graphics.Rect) should
         * not be cached because it will change over time as the object is reused on each
         * call.
         *
         * @param reuseRect A Rect to be modified and returned by evaluate.
         */
        public CRectFEvaluator(RectF reuseRect) {
            mRectF = reuseRect;
        }

        /**
         * This function returns the result of linearly interpolating the start and
         * end Rect values, with <code>fraction</code> representing the proportion
         * between the start and end values. The calculation is a simple parametric
         * calculation on each of the separate components in the Rect objects
         * (left, top, right, and bottom).
         * <p>If #CRectFEvaluator(android.graphics.Rect) was used to construct
         * this RectEvaluator, the object returned will be the <code>reuseRect</code>
         * passed into the constructor.</p>
         *
         * @param fraction   The fraction from the starting to the ending values
         * @param startValue The start Rect
         * @param endValue   The end Rect
         * @return A linear interpolation between the start and end values, given the
         * <code>fraction</code> parameter.
         */
        @Override
        public RectF evaluate(float fraction, RectF startValue, RectF endValue) {
            float left = startValue.left + (endValue.left - startValue.left) * fraction;
            float top = startValue.top + (endValue.top - startValue.top) * fraction;
            float right = startValue.right + (endValue.right - startValue.right) * fraction;
            float bottom = startValue.bottom + (endValue.bottom - startValue.bottom) * fraction;
            if (mRectF == null) {
                return new RectF(left, top, right, bottom);
            } else {
                mRectF.set(left, top, right, bottom);
                return mRectF;
            }
        }
    }

}
