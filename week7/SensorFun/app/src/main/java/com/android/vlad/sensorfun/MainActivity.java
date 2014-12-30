package com.android.vlad.sensorfun;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {
    private static final int CONST = 10;

    private SensorManager sensorManager;
    private Sensor sensorAccelerator;
    private Sensor sensorProximity;
    private MediaPlayer mediaPlayerAccelerator;
    private MediaPlayer mediaPlayerProximity;
    private boolean isAcceleratorSensorAvailable = false;
    private boolean isProximitySensorAvailable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerator = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensorAccelerator == null) {
            Toast.makeText(this, "No accelerometer sensor available!", Toast.LENGTH_LONG).show();
        } else {
            isAcceleratorSensorAvailable = true;
            mediaPlayerAccelerator = MediaPlayer.create(this, R.raw.ray_gun);
        }

        if (sensorProximity == null) {
            Toast.makeText(this, "No proximity sensor available!", Toast.LENGTH_LONG).show();
        } else {
            isProximitySensorAvailable = true;
            mediaPlayerProximity = MediaPlayer.create(this, R.raw.roar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isAcceleratorSensorAvailable) {
            sensorManager.registerListener(this, sensorAccelerator, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (isProximitySensorAvailable) {
            sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isAcceleratorSensorAvailable) {
            if (mediaPlayerAccelerator.isPlaying()) {
                mediaPlayerAccelerator.stop();
            }
            mediaPlayerAccelerator.release();
            mediaPlayerAccelerator = null;
        }

        if (isProximitySensorAvailable) {
            if (mediaPlayerProximity.isPlaying()) {
                mediaPlayerProximity.stop();
            }

            mediaPlayerProximity.release();
            mediaPlayerProximity = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isAcceleratorSensorAvailable && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for (int i = 0; i < event.values.length; i++) {
                if (event.values[i] > CONST) {
                    mediaPlayerAccelerator.start();
                    break;
                }
            }
        }

        if (isProximitySensorAvailable && event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] < CONST) {
                mediaPlayerProximity.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
}
