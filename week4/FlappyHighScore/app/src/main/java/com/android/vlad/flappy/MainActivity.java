package com.android.vlad.flappy;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity implements FragmentLogin.OnLoginButtonClickListener, FragmentGame.OnGameOverListener {
    private FragmentManager mFragmentManager;
    private Fragment mFragmentGame;
    private MediaPlayer mMainTheme;
    private Player mPlayer;

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
    public void onLoginButtonClick(Player player) {
        mPlayer = player;

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
    public void onGameOver(int score) {
        uploadResults(score);
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

    private void uploadResults(int score) {
        try {
            URL url = new URL("http://95.111.103.224:8080/Flappy/scores");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            httpCon.setRequestProperty("Content-type", "application/json");
            OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
            out.write("{ \"name\" : \"" + mPlayer.getName()
                    + "\" , \"mail\" : \"" + mPlayer.getEmail()
                    + "\" , \"whereFrom\" : \"" + mPlayer.getUniversity() + "\" , \"score\" : "
                    + score + "}");
            out.flush();
            out.close();

            Toast.makeText(this, "Your score has been uploaded!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Score upload failed! Unable to connect to server!", Toast.LENGTH_LONG).show();
        }
    }
}
