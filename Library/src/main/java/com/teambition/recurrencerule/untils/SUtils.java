package com.teambition.recurrencerule.untils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.view.View;

import java.util.Arrays;


public class SUtils {

//    private static final int CORNER_RADIUS = DensityUtil.dip2px(MainApp.CONTEXT, 2);


    public static boolean isApi_18_OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isApi_21_OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isApi_22_OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setViewBackground(View view, Drawable bg) {
        int paddingL = view.getPaddingLeft();
        int paddingT = view.getPaddingTop();
        int paddingR = view.getPaddingRight();
        int paddingB = view.getPaddingBottom();

        view.setBackground(bg);
        view.setPadding(paddingL, paddingT, paddingR, paddingB);
    }

    // Returns material styled button bg
    public static Drawable createButtonBg(Context context,
                                          int colorButtonNormal,
                                          int colorControlHighlight) {
        if (isApi_21_OrHigher()) {
            return createButtonRippleBg(context, colorButtonNormal,
                    colorControlHighlight);
        }

        return createButtonNormalBg(context, colorControlHighlight);
    }

    // Button bg for API versions >= Lollipop
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable createButtonRippleBg(Context context,
                                                 int colorButtonNormal,
                                                 int colorControlHighlight) {
        return new RippleDrawable(ColorStateList.valueOf(colorControlHighlight),
                null, createButtonShape(context, colorButtonNormal));
    }

    // Button bg for API version < Lollipop
    private static Drawable createButtonNormalBg(Context context, int colorControlHighlight) {
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed},
                createButtonShape(context, colorControlHighlight));
        sld.addState(new int[]{},
                new ColorDrawable(Color.TRANSPARENT));
        return sld;
    }

    // Base button shape
    private static Drawable createButtonShape(Context context, int color) {
        // Translation of Lollipop's xml button-bg definition to Java
        int paddingH = DensityUtil.dip2px(context, 8);
        int paddingV = DensityUtil.dip2px(context, 4);
        int insetH = DensityUtil.dip2px(context, 4);
        int insetV = DensityUtil.dip2px(context, 6);
        int corner_radius = DensityUtil.dip2px(context, 2);
        float[] outerRadii = new float[8];
        Arrays.fill(outerRadii, corner_radius);

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);

        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.setPadding(paddingH, paddingV, paddingH, paddingV);

        return new InsetDrawable(shapeDrawable,
                insetH, insetV, insetH, insetV);
    }

    // Drawable for icons in 'ButtonLayout'
    public static Drawable createImageViewBg(int colorButtonNormal, int colorControlHighlight) {
        if (isApi_21_OrHigher()) {
            return createImageViewRippleBg(colorButtonNormal, colorControlHighlight);
        }

        return createImageViewNormalBg(colorControlHighlight);
    }

    // Icon bg for API versions >= Lollipop
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable createImageViewRippleBg(int colorButtonNormal, int colorControlHighlight) {
        return new RippleDrawable(ColorStateList.valueOf(colorControlHighlight),
                null, createImageViewShape(colorButtonNormal));
    }

    // Icon bg for API versions < Lollipop
    private static Drawable createImageViewNormalBg(int colorControlHighlight) {
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed},
                createImageViewShape(colorControlHighlight));
        sld.addState(new int[]{},
                new ColorDrawable(Color.TRANSPARENT));
        return sld;
    }

    // Base icon bg shape
    private static Drawable createImageViewShape(int color) {
        OvalShape ovalShape = new OvalShape();

        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        return shapeDrawable;
    }


    // Creates a drawable with the supplied color and corner radii
    public static Drawable createBgDrawable(int color, int rTopLeft,
                                            int rTopRight, int rBottomRight,
                                            int rBottomLeft) {
        float[] outerRadii = new float[8];
        outerRadii[0] = rTopLeft;
        outerRadii[1] = rTopLeft;
        outerRadii[2] = rTopRight;
        outerRadii[3] = rTopRight;
        outerRadii[4] = rBottomRight;
        outerRadii[5] = rBottomRight;
        outerRadii[6] = rBottomLeft;
        outerRadii[7] = rBottomLeft;

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);

        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);

        return shapeDrawable;
    }

    public static Drawable createOverflowButtonBg(Context context, int pressedStateColor) {
        if (SUtils.isApi_21_OrHigher()) {
            return createOverflowButtonBgLollipop(pressedStateColor);
        }

        return createOverflowButtonBgBC(context, pressedStateColor);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Drawable createOverflowButtonBgLollipop(int pressedStateColor) {
        return new RippleDrawable(
                ColorStateList.valueOf(pressedStateColor),
                null, null);
    }

    private static Drawable createOverflowButtonBgBC(Context context, int pressedStateColor) {
        int corner_radius = DensityUtil.dip2px(context, 2);
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{android.R.attr.state_pressed},
                createBgDrawable(pressedStateColor,
                        0, corner_radius, 0, 0));
        sld.addState(new int[]{}, new ColorDrawable(Color.TRANSPARENT));
        return sld;
    }


}
