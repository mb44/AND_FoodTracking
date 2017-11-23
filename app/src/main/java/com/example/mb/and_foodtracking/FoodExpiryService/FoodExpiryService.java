package com.example.mb.and_foodtracking.FoodExpiryService;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FoodExpiryService extends Service {
    private Timer timer;

    TimerTask timerTask = new TimerTask() {
        public void run() {
            Log.d("FoodExpiryService", "run: running");
            //Toast.makeText(FoodExpiryService.this, "Working...", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(FoodExpiryService.this, "Service created", Toast.LENGTH_SHORT).show();
        timer = new Timer();
        timer.schedule(timerTask, 2000, 2*1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(FoodExpiryService.this, "onDestroy", Toast.LENGTH_SHORT).show();
        timer.cancel();
        timerTask.cancel();
        super.onDestroy();
    }
}
