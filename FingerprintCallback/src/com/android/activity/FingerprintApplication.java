package com.android.activity;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.android.service.FingerprintManager;


public class FingerprintApplication extends Application {

    private String TAG = "fenghaitao";
    Intent fingerprintManager;

    @Override
    public void onCreate() {
        //程序创建的时候执行
        super.onCreate();
        Log.d(TAG,"-------------------Fingerprint Application--------");
        fingerprintManager = new Intent();
        fingerprintManager.setClass(getApplicationContext(), FingerprintManager.class);
        startFingerprintService();
    }
    
    @Override
    public void onTrimMemory(int level) {
        //程序在内存清理的时候执行
        
        super.onTrimMemory(level);
    }
    
    @Override
    public void onTerminate() {
        //程序终止的时候执行
        super.onTerminate();
        stopFingerprintService();
    }
    
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }
    
    public void startFingerprintService(){
        Log.i(TAG, "---------start Fingerprint service");
        startService(fingerprintManager);
    }
    
    public void stopFingerprintService(){
        Log.i(TAG, "--------stop Fingerprint service");
        stopService(fingerprintManager);
    }

}
