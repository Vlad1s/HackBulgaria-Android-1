package com.android.vlad.flappy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Obstacle extends GameObject {
    private final int OBSTACLE_WIDTH_SCALE = 8;
    private final int HOLE_MIN_SCALE = 5;
    private final int HOLE_MAX_SCALE = 2;

    private int mObstacleWidth;
    private int mHoleMinHeight;
    private int mHoleMaxHeight;
    private int mHoleHeight;
    private int mMoveStep = 4;
    private Point mHolePosition;
    private Paint mPaint;
    private Context mContext;

    public Obstacle(Context context) {
        this.mContext = context;

        mObstacleWidth = DrawingView.getScreenSize().x / OBSTACLE_WIDTH_SCALE;

        mHolePosition = new Point();
        mHoleMinHeight = DrawingView.getScreenSize().y / HOLE_MIN_SCALE;
        mHoleMaxHeight = DrawingView.getScreenSize().y / HOLE_MAX_SCALE;

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);

        init();
    }

    public Point getHolePosition() {
        return mHolePosition;
    }

    public int getHoleWidth() {
        return mObstacleWidth;
    }

    public int getHoleHeight() {
        return mHoleHeight;
    }

    @Override
    public void init() {
        mHolePosition.x = DrawingView.getScreenSize().x;

        Random random = new Random();
        mHoleHeight = mHoleMinHeight + random.nextInt(mHoleMaxHeight - mHoleMinHeight);
        mHolePosition.y = random.nextInt(DrawingView.getScreenSize().y - mHoleMaxHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        mHolePosition.x -= mMoveStep + 2 * sDifficultyLevel;

        if (mHolePosition.x + mObstacleWidth < 0) {
            init();
            return;
        }

        canvas.drawRect(mHolePosition.x, 0, mHolePosition.x + mObstacleWidth, mHolePosition.y, mPaint);
        canvas.drawRect(mHolePosition.x, mHolePosition.y + mHoleHeight, mHolePosition.x + mObstacleWidth, DrawingView.getScreenSize().y, mPaint);
    }
}
