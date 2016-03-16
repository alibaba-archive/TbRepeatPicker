package com.teambition.recurrencerule.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class MonthButton extends ToggleButton {

    private static int mDefaultTextColor, mCheckedTextColor;

    private CheckableDrawable mDrawable;

    private boolean noAnimate = false;

    public MonthButton(Context context) {
        super(context);
        init();
    }

    public MonthButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setTextSize(12);
        setPadding(0, 0, 0, 0);
    }

    private CheckableDrawable.OnAnimationDone mCallback = new CheckableDrawable.OnAnimationDone() {
        @Override
        public void animationIsDone() {

        }

        @Override
        public void animationHasBeenCancelled() {

        }
    };

    public void setCheckedNoAnimate(boolean checked) {
        final boolean tempAnimate = noAnimate;
        noAnimate = true;
        setChecked(checked);
        noAnimate = tempAnimate;
    }

    @Override
    public void setChecked(final boolean checked) {
        super.setChecked(checked);

        if (mDrawable != null) {
            if (noAnimate) {
                mDrawable.setChecked(checked);
                setTextColor(isChecked() ? mCheckedTextColor : mDefaultTextColor);
            } else {
                setTextColor(isChecked() ? mCheckedTextColor : mDefaultTextColor);
                mDrawable.setCheckedOnClick(isChecked(), mCallback);
            }
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
        if (d instanceof CheckableDrawable) {
            mDrawable = (CheckableDrawable) d;
        } else {
            mDrawable = null;
        }
    }

    public static void setStateColors(int defaultColor, int checkedColor) {
        mDefaultTextColor = defaultColor;
        mCheckedTextColor = checkedColor;
    }

}
