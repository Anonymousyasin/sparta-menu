package com.android.support.components;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

/**
 * Sparta Menu animation helpers.
 * All durations live here so tuning is one-place.
 */
public class SpartanAnim {

    public static final int DUR_OPEN   = 220;  // menu appear
    public static final int DUR_CLOSE  = 160;
    public static final int DUR_EXPAND = 180;  // category expand
    public static final int DUR_PRESS  = 120;  // widget press bounce

    /** Rounded rectangle background drawable (glass panels, inputs). */
    public static android.graphics.drawable.GradientDrawable roundBg(
            int color, float radiusDp, int strokeColor) {
        android.graphics.drawable.GradientDrawable d =
                new android.graphics.drawable.GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(radiusDp * 2f); // approx dp→px at common densities
        if (strokeColor != 0) d.setStroke(1, strokeColor);
        return d;
    }

    /** Scale+fade entrance for the menu panel. */
    public static void popIn(View v) {
        if (v == null) return;
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(
                0.85f, 1f, 0.85f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(DUR_OPEN);
        scale.setInterpolator(new OvershootInterpolator(1.1f));
        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(DUR_OPEN);
        fade.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(scale);
        set.addAnimation(fade);
        v.startAnimation(set);
    }

    /** Shrink+fade exit, then optionally hide the view. */
    public static void popOut(final View v) {
        if (v == null) return;
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.85f, 1f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(DUR_CLOSE);
        AlphaAnimation fade = new AlphaAnimation(1f, 0f);
        fade.setDuration(DUR_CLOSE);
        set.addAnimation(scale);
        set.addAnimation(fade);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation a) {}
            @Override public void onAnimationRepeat(Animation a) {}
            @Override public void onAnimationEnd(Animation a) {
                v.setVisibility(View.GONE);
            }
        });
        v.startAnimation(set);
    }

    /** Press bounce — quick grow-and-settle on touch feedback. */
    public static void bounce(View v) {
        if (v == null) return;
        ScaleAnimation s = new ScaleAnimation(
                1f, 1.06f, 1f, 1.06f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        s.setDuration(DUR_PRESS / 2);
        s.setRepeatCount(1);
        s.setRepeatMode(Animation.REVERSE);
        s.setInterpolator(new OvershootInterpolator());
        v.startAnimation(s);
    }

    /** Smooth height expand/collapse wrapper for category containers. */
    public static void toggleExpand(final View target, boolean expand) {
        if (target == null) return;
        target.measure(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetH = target.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float t, android.view.animation.Transformation tr) {
                int h = expand ? (int) (targetH * t) : (int) (targetH * (1 - t));
                target.getLayoutParams().height = h;
                target.requestLayout();
            }
            @Override
            public boolean willChangeBounds() { return true; }
        };
        a.setDuration(DUR_EXPAND);
        a.setInterpolator(new DecelerateInterpolator());
        target.startAnimation(a);
    }
}
