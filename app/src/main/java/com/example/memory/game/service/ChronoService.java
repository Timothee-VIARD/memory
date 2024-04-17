package com.example.memory.game.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.example.memory.R;

import java.util.Timer;
import java.util.TimerTask;

public class ChronoService extends Service {

    private Timer timer;
    private int seconds;
    private Handler handler = new Handler();
    private OnSecondChangeListener onSecondChangeListener;
    private OnTimerFinishedListener onTimerFinishedListener;
    private String mode;
    private boolean isPaused = false;

    /**
     * Instantiates a new Chrono service.
     */
    public ChronoService() {
    }

    /**
     * Reset timer.
     */
    public void resetTimer() {
        this.seconds = -1;
    }

    /**
     * The interface on second change listener.
     */
    public interface OnSecondChangeListener {
        void onSecondChange(int seconds);
    }

    /**
     * The interface on timer finished listener.
     */
    public interface OnTimerFinishedListener {
        void onTimerFinished();
    }

    /**
     * The type My binder.
     */
    public class MyBinder extends Binder {
        public ChronoService getService() {
            return ChronoService.this;
        }
    }
    private final MyBinder myBinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    /**
     * Starts the timer.
     *
     * @param intent  the intent
     * @param flags   the flags
     * @param startId the start id
     * @return the start command
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.timer = new Timer();
        this.mode = intent.getStringExtra("mode");
        int difficulty = intent.getIntExtra("difficulty", 1);

        if (mode.equals(getString(R.string.normal))) {
            this.seconds = -1;
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                seconds++;
                                if (onSecondChangeListener != null) {
                                    onSecondChangeListener.onSecondChange(seconds);
                                }
                            }
                        });
                    }
                }
            }, 0, 1000);
        } else if (mode.equals(getString(R.string.contre_la_montre))) {
            setChronoTimer(difficulty);
            this.timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isPaused) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                seconds--;
                                if (onSecondChangeListener != null) {
                                    onSecondChangeListener.onSecondChange(seconds);
                                }
                                if (seconds == 0) {
                                    if (onTimerFinishedListener != null) {
                                        onTimerFinishedListener.onTimerFinished();
                                    }
                                }
                            }
                        });
                    }
                }
            }, 0, 1000);
        }
        return START_STICKY;
    }

    /**
     * Sets chrono timer.
     * @param difficulty the difficulty
     */
    public void setChronoTimer(int difficulty) {
        switch (difficulty) {
            case 1:
                seconds = 51;
                break;
            case 2:
                seconds = 151;
                break;
            case 3:
                seconds = 201;
                break;
        }
    }

    /**
     * Pause timer.
     */
    public void pauseTimer() {
        isPaused = true;
    }

    /**
     * Resume timer.
     */
    public void resumeTimer() {
        isPaused = false;
    }

    /**
     * Sets on second change listener.
     * @param onSecondChangeListener the on second change listener
     */
    public void setOnSecondChangeListener(OnSecondChangeListener onSecondChangeListener) {
        this.onSecondChangeListener = onSecondChangeListener;
    }

    /**
     * Sets on timer finished listener.
     * @param listener the listener
     */
    public void setOnTimerFinishedListener(OnTimerFinishedListener listener) {
        this.onTimerFinishedListener = listener;
    }

    /**
     * Cancel timer.
     */
    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Gets seconds.
     * @return the seconds
     */
    public int getSeconds() {
        return seconds;
    }
}