package com.android.vlad.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentGame extends Fragment implements DrawingView.OnCollisionListener {
    private DrawingView mFlappyGame;
    private MediaPlayer mGameOverTheme;
    private OnGameOverListener mOnGameOverListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnGameOverListener = (OnGameOverListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGameOverListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGameOverTheme = MediaPlayer.create(getActivity(), R.raw.game_over);
        mGameOverTheme.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mOnGameOverListener.onStartMainTheme();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DrawingView.setScreenSize(getScreenSize());
        mFlappyGame = (DrawingView) inflater.inflate(R.layout.fragment_game, container, false);
        mFlappyGame.setOnCollisionListener(this);

        Background background = new Background(getActivity());
        mFlappyGame.setBackground(background);

        Obstacle obstacle = new Obstacle(getActivity());
        mFlappyGame.setObstacle(obstacle);

        Bird bird = new Bird(getActivity());
        mFlappyGame.setBird(bird);

        GameClock gameClock = new GameClock();
        gameClock.subscribe(mFlappyGame);

        return mFlappyGame;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFlappyGame.resumeGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        mFlappyGame.pauseGame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGameOverTheme.release();
        mGameOverTheme = null;

        if (!mOnGameOverListener.isMainThemePlaying()) {
            mOnGameOverListener.onStartMainTheme();
        }
    }

    @Override
    public void onCollision(int score) {
        mOnGameOverListener.onGameOver(score);
        mOnGameOverListener.onPauseMainTheme();
        mGameOverTheme.start();
    }

    private Point getScreenSize() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        return size;
    }

    public interface OnGameOverListener {
        public void onGameOver(int score);

        public void onPauseMainTheme();

        public void onStartMainTheme();

        public boolean isMainThemePlaying();

    }
}
