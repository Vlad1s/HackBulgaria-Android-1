package com.android.vlad.flappy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

public class Bird extends GameObject {
    private final int BIRD_SCALE = 10;
    private final int FLY_UP_DISTANCE = 200;

    private int mInitX;
    private int mInitY;
    private int mWidth;
    private int mHeight;
    private int mYOffset;
    private int mMoveStep = 8;
    private Bitmap mBitmapBird;
    private Point mPosition;
    private Context mContext;

    public Bird(Context context) {
        this.mContext = context;

        Bitmap unscaledBitmapBird = BitmapFactory.decodeResource(context.getResources(), R.drawable.bird);
        mWidth = DrawingView.getScreenSize().x / BIRD_SCALE;
        mHeight = unscaledBitmapBird.getHeight() * mWidth / unscaledBitmapBird.getWidth();
        mBitmapBird = Bitmap.createScaledBitmap(unscaledBitmapBird, mWidth, mHeight, false);
        mInitX = (DrawingView.getScreenSize().x - mWidth) / 4;
        mInitY = (DrawingView.getScreenSize().y - mHeight) / 2;
        mPosition = new Point();
        init();
    }

    public Point getPosition() {
        return mPosition;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void flyUp() {
        mYOffset = FLY_UP_DISTANCE;
    }

    @Override
    public void init() {
        mPosition.set(mInitX, mInitY);
        mYOffset = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        if (mYOffset > 0) {
            mYOffset -= mMoveStep + 2 * sDifficultyLevel;
            mPosition.y -= mMoveStep + 2 * sDifficultyLevel;
        } else {
            mPosition.y += mMoveStep + 2 * sDifficultyLevel;
        }

        canvas.drawBitmap(mBitmapBird, mPosition.x, mPosition.y, null);
    }
}
