package com.android.vlad.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements FragmentLogin.OnLoginButtonClickListener, FragmentGame.OnGameOverListener {
    private FragmentManager mFragmentManager;
    private FragmentLogin mFragmentLogin;
    private MediaPlayer mMainTheme;
    private int mScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();

        Fragment fragmentGame = new FragmentGame();
        mFragmentManager.beginTransaction().add(R.id.container, fragmentGame).commit();

        mFragmentLogin = new FragmentLogin();

        mMainTheme = MediaPlayer.create(this, R.raw.flappy_soundtrack);
        mMainTheme.setLooping(true);
    }

    @Override
    public void onLoginButtonClick(Player player) {
        if (isNetworkAvailable()) {
            new UploadResults(this).execute(player.getName(), player.getEmail(), player.getUniversity(), "" + mScore);
        } else {
            Toast.makeText(this, "Score upload failed! No active internet connection!", Toast.LENGTH_LONG).show();
        }
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
    public void onGameOver(int score) {
        mScore = score;

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_in_top, R.animator.fade_out, R.animator.fade_in, R.animator.slide_out_top);
        transaction.replace(R.id.container, mFragmentLogin);
        transaction.addToBackStack(null);
        transaction.commit();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
