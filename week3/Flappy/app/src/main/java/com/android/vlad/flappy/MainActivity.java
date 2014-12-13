package com.android.vlad.flappy;

import android.app.Activity;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;

public class MainActivity extends Activity implements DrawingView.CollisionListener {
    private DrawingView mFlappyGame;
    private MediaPlayer mMainTheme;
    private MediaPlayer mGameOverTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DrawingView.setScreenSize(getScreenSize());
        mFlappyGame = (DrawingView) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        mFlappyGame.setOnCollisionListener(this);
        setContentView(mFlappyGame);

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
        mFlappyGame.setBackground(background);

        Obstacle obstacle = new Obstacle(this);
        mFlappyGame.setObstacle(obstacle);

        Bird bird = new Bird(this);
        mFlappyGame.setBird(bird);

        GameClock gameClock = new GameClock();
        gameClock.subscribe(mFlappyGame);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFlappyGame.resumeGame();
        mMainTheme.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFlappyGame.pauseGame();
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
