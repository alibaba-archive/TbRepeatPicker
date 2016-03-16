package com.teambition.recurrencerule.views;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class WeekButton extends ToggleButton {

    private static int mDefaultTextColor, mCheckedTextColor;

    private CheckableDrawable mDrawable;

    private boolean noAnimate = false;

    public WeekButton(Context context) {
        super(context);
    }

    public WeekButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private CheckableDrawable.OnAnimationDone mCallback = new CheckableDrawable.OnAnimationDone() {

        @Override
        public void animationIsDone() {
            setTextColor(isChecked() ? mCheckedTextColor : mDefaultTextColor);
            mDrawable.setChecked(isChecked());
        }

        @Override
        public void animationHasBeenCancelled() {
            setTextColor(isChecked() ? mCheckedTextColor : mDefaultTextColor);
            mDrawable.setChecked(isChecked());
        }
    };

    public void setCheckedNoAnimate(boolean checked) {
        noAnimate = true;
        setChecked(checked);
        noAnimate = false;
    }

    @Override
    public void setChecked(final boolean checked) {
        super.setChecked(checked);

        if (mDrawable != null) {
            if (noAnimate) {
                mDrawable.setChecked(checked);
                setTextColor(isChecked() ? mCheckedTextColor : mDefaultTextColor);
            } else {
                setTextColor(mCheckedTextColor);
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

    public void setStateColors(int defaultColor, int checkedColor) {
        mDefaultTextColor = defaultColor;
        mCheckedTextColor = checkedColor;
    }


}
