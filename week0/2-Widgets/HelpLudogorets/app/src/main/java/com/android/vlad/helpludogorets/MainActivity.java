package com.android.vlad.helpludogorets;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends Activity {
    private final int SEEK_INTERVAL = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VideoView videoView;
        if (isExternalStorageAccessible()) {
            videoView = (VideoView) findViewById(R.id.video);
            // Note: on some devices Environment.getExternalStorageDirectory() does not
            // return the root folder of the mounted SD card as part of the external storage
            File videoFile = new File(Environment.getExternalStorageDirectory(), "video.mp4");

            if (!videoFile.exists()) {
                Toast.makeText(this, "Video file not found!", Toast.LENGTH_LONG).show();
                return;
            }

            videoView.setVideoURI(Uri.fromFile(videoFile));
        } else {
            Toast.makeText(this, "No access to the external storage!", Toast.LENGTH_LONG).show();
            return;
        }

        final ImageView playPauseButton = (ImageView) findViewById(R.id.play_pause);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.play));
                    videoView.pause();
                } else {
                    ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.pause));
                    videoView.start();
                }
            }
        });

        ImageView prevButton = (ImageView) findViewById(R.id.prev);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
                videoView.pause();

                int newPosition = videoView.getCurrentPosition() - SEEK_INTERVAL;
                if (newPosition < 0) {
                    newPosition = 0;
                }
                videoView.seekTo(newPosition);
            }
        });

        ImageView nextButton = (ImageView) findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
                videoView.pause();

                int newPosition = videoView.getCurrentPosition() + SEEK_INTERVAL;
                if (newPosition > videoView.getDuration()) {
                    newPosition = videoView.getDuration();
                }
                videoView.seekTo(newPosition);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playPauseButton.setImageDrawable(getResources().getDrawable(R.drawable.play));
            }
        });
    }

    private boolean isExternalStorageAccessible() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
