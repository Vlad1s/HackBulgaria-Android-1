package com.android.vlad.flappy;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class SlidingLinearLayout extends LinearLayout {

    public SlidingLinearLayout(Context context) {
        super(context);
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public float getYFraction() {
        if (getHeight() == 0) {
            return 0;
        }
        return getTranslationY() / getHeight();
    }

    public void setYFraction(final float fraction) {
        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }
}
