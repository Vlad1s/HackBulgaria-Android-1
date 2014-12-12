package com.android.vlad.flappy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class DrawingView extends ImageView implements View.OnClickListener, GameClock.GameClockListener {
    private static Point sScreenSize;

    private final int TEXT_SIZE_SCALE = 8;

    private int mTextSize;
    private int mScore;
    private int mFrame;
    private int mDifficultyLevel;
    private boolean mGameOver;
    private Paint mTextPaint;
    private Background mBackground;
    private Obstacle mObstacle;
    private Bird mBird;
    private CollisionListener mCollisionListener;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);

        mTextSize = getScreenSize().x / TEXT_SIZE_SCALE;
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(mTextSize);

        init();
    }

    public static Point getScreenSize() {
        return sScreenSize;
    }

    public static void setScreenSize(Point screenSize) {
        DrawingView.sScreenSize = screenSize;
    }

    public void setOnCollisionListener(CollisionListener collisionListener) {
        this.mCollisionListener = collisionListener;
    }

    public void setBird(Bird bird) {
        this.mBird = bird;
    }

    public void setBackground(Background background) {
        this.mBackground = background;
    }

    public void setObstacle(Obstacle obstacle) {
        this.mObstacle = obstacle;
    }

    private boolean hasCollisions() {
        if (mBird.getPosition().y < 0 || mBird.getPosition().y + mBird.getHeight() > DrawingView.getScreenSize().y) {
            mCollisionListener.onCollision();
            return true;
        }

        if (mBird.getPosition().x + mBird.getWidth() < mObstacle.getHolePosition().x
                || mBird.getPosition().x > mObstacle.getHolePosition().x + mObstacle.getHoleWidth()) {
            return false;
        }

        if (mBird.getPosition().y < mObstacle.getHolePosition().y
                || mBird.getPosition().y + mBird.getHeight() > mObstacle.getHolePosition().y + mObstacle.getHoleHeight()) {
            mCollisionListener.onCollision();
            return true;
        }

        return false;
    }

    private void init() {
        mGameOver = false;
        mFrame = 0;
        mScore = 0;
        mDifficultyLevel = 0;
        GameObject.setDifficultyLevel(mDifficultyLevel);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        mBackground.draw(canvas);
        mObstacle.draw(canvas);
        mBird.draw(canvas);

        canvas.drawText("" + mScore, 32, 8 + mTextSize, mTextPaint);
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (mGameOver) {
            return;
        }

        mFrame++;

        // Every 60 frames (around 1 second) the score gets updated
        if (mFrame % 60 == 0) {
            mScore += 1 + mDifficultyLevel;
        }

        // Every 600 frames (around 10 seconds) game gets harder
        if (mFrame == 600) {
            mFrame = 0;
            mDifficultyLevel++;
            GameObject.setDifficultyLevel(mDifficultyLevel);
        }

        if (hasCollisions()) {
            Toast.makeText(getContext(), "Game Over! Your score is " + mScore + "!", Toast.LENGTH_LONG).show();
            mGameOver = true;
            return;
        }

        this.invalidate();
    }

    @Override
    public void onClick(View v) {
        if (mGameOver) {
            init();
            mBackground.init();
            mObstacle.init();
            mBird.init();
            return;
        }

        mBird.flyUp();
    }

    public interface CollisionListener {
        public void onCollision();
    }
}
