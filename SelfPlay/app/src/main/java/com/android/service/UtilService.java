package com.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.entity.Constant;
import com.android.play.FullscreenActivity;

public class UtilService extends Service {
    private String TAG = Constant.TAG;


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG," UtilService : onBind ");
     return new UtillBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG," UtilService : onCreate ");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG," UtilService : onDestory ");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG," UtilService : onStartCommand ");

        return START_REDELIVER_INTENT;
    }

    public class UtillBinder extends Binder {
        public  UtilService getResult(){
            return UtilService.this;
        }
    }


}
