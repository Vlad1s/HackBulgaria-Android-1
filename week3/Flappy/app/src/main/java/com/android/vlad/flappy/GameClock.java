package com.android.vlad.flappy;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class GameClock {
    private static final int FRAME_RATE_CONSTANT = 16;

    private List<GameClockListener> mClockListeners = new ArrayList<GameClockListener>();
    private Handler mHandler = new Handler();

    public GameClock() {
        mHandler.post(new ClockRunnable());
    }

    public void subscribe(GameClockListener listener) {
        mClockListeners.add(listener);
    }

    public static interface GameClockListener {
        public void onGameEvent(GameEvent gameEvent);
    }

    private class ClockRunnable implements Runnable {
        @Override
        public void run() {
            onTimerTick();
            mHandler.postDelayed(this, FRAME_RATE_CONSTANT);
        }

        private void onTimerTick() {
            for (GameClockListener listener : mClockListeners) {
                listener.onGameEvent(new GameEvent());
            }
        }
    }
}
