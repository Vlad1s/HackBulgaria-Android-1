package com.android.vlad.flappy;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;

public class MainActivity extends Activity implements DrawingView.CollisionListener {
    private MediaPlayer mMainTheme;
    private MediaPlayer mGameOverTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawingView.setScreenSize(getScreenSize());
        DrawingView flappy = (DrawingView) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        flappy.setOnCollisionListener(this);
        setContentView(flappy);

        mMainTheme = MediaPlayer.create(this, R.raw.flappy_soundtrack);
        mMainTheme.setLooping(true);

        mGameOverTheme = MediaPlayer.create(this, R.raw.game_over);
        mGameOverTheme.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMainTheme.start();
            }
        });

        Background background = new Background(this);
        flappy.setBackground(background);

        Obstacle obstacle = new Obstacle(this);
        flappy.setObstacle(obstacle);

        Bird bird = new Bird(this);
        flappy.setBird(bird);

        GameClock gameClock = new GameClock();
        gameClock.subscribe(flappy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainTheme.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMainTheme.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainTheme.release();
        mMainTheme = null;
        mGameOverTheme.release();
        mGameOverTheme = null;
    }

    private Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    @Override
    public void onCollision() {
        mMainTheme.pause();
        mGameOverTheme.start();
    }
}
