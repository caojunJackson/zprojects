package com.android.fingerprintcallbackdemo;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

public class ScreenObserver {
    private Context mContext;
    private ScreenBroadcastReceiver mScreenReceiver;
    private ScreenStateListener mScreenStateListener;
    
    public ScreenObserver(Context context){
        mContext = context;
        mScreenReceiver = new ScreenBroadcastReceiver();
    }
    
    public void startObserver(ScreenStateListener listener){
        mScreenStateListener = listener;
        registerListener();
        getScreenState();
    }
    
    public void shutdownObserver(){
        unregisterListener();
    }
    
    @SuppressLint("NewApi")
    private void getScreenState(){
        if(mContext == null){
            return;
        }
        
        PowerManager manager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
        if(manager.isInteractive()){
            if(mScreenStateListener != null){
                mScreenStateListener.onScreenOn();
            }
        }else{
            if(mScreenStateListener != null){
                mScreenStateListener.onScreenOff();
            }
        }
    }
    
    private void registerListener(){
        if(mContext != null){
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_USER_PRESENT);
            mContext.registerReceiver(mScreenReceiver, filter);
        }
    }
    
    private void unregisterListener(){
        if(mContext != null){
            mContext.unregisterReceiver(mScreenReceiver);
        }
    }
    
    private class ScreenBroadcastReceiver extends BroadcastReceiver{
        private String action = null;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            android.util.Log.i("fenghaitao", "===========ScreenObserver onreceive=======action="+action);
            if(Intent.ACTION_SCREEN_ON.equals(action)){
                mScreenStateListener.onScreenOn();
            }else if(Intent.ACTION_SCREEN_OFF.equals(action)){
                mScreenStateListener.onScreenOff();
            }else if(Intent.ACTION_USER_PRESENT.equals(action)){
                mScreenStateListener.onUserPresent();
            }
        }
    }
    
    public interface ScreenStateListener{
        public void onScreenOn();
        public void onScreenOff();
        public void onUserPresent();
    }
    
    
}
