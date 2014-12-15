package com.android.vlad.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

public class MainActivity extends Activity implements FragmentLogin.OnLoginButtonClickListener, FragmentGame.OnGameOverListener {
    private FragmentManager mFragmentManager;
    private Fragment mFragmentGame;
    private MediaPlayer mMainTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Needed to avoid android.os.NetworkOnMainThreadException
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mFragmentManager = getFragmentManager();

        Fragment fragmentLogin = new FragmentLogin();
        mFragmentManager.beginTransaction().add(R.id.container, fragmentLogin).commit();

        mFragmentGame = new FragmentGame();

        mMainTheme = MediaPlayer.create(this, R.raw.flappy_soundtrack);
        mMainTheme.setLooping(true);
    }

    @Override
    public void onLoginButtonClick() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.slide_out_top, R.animator.slide_in_top, R.animator.fade_out);
        transaction.replace(R.id.container, mFragmentGame);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onPauseMainTheme() {
        mMainTheme.pause();
    }

    @Override
    public void onStartMainTheme() {
        mMainTheme.start();
    }

    @Override
    public boolean isMainThemePlaying() {
        return mMainTheme.isPlaying();
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
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            findViewById(R.id.container).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
