package com.android.vlad.flappy;

import android.graphics.Canvas;

public abstract class GameObject {
    protected static int sDifficultyLevel;

    public abstract void init();

    public abstract void draw(Canvas canvas);

    public static void setDifficultyLevel(int difficultyLevel) {
        GameObject.sDifficultyLevel = difficultyLevel;
    }
}
