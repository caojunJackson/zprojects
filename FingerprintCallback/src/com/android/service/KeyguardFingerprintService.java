package com.android.service;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.android.fingerprintcallbackdemo.ScreenObserver;
import com.android.fingerprintcallbackdemo.ScreenObserver.ScreenStateListener;
import com.android.internal.policy.IKeyguardService;

import com.android.activity.Util;
import com.android.aidl.*;

public class KeyguardFingerprintService extends Service {
    private String TAG = "fenghaitao";
    private ScreenObserver mScreenObserver;
    IKeyguardService mService = null;
    private KeyguardManager keyguardMan;
    private PowerManager mPowerManager;
    private IFingerprintManager mFingerprintManager;
    private static boolean isAlreadSetAuthticate = false;
    
    private ServiceConnection mIFingerConn = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
           Log.d(TAG, "----------KeyguardFingerprintService onServiceDisconnected----");
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFingerprintManager = IFingerprintManager.Stub.asInterface(service);
            if(mFingerprintManager != null){
                try {
                    Log.d(TAG,"---------onServiceConnected------setOnAuthenticateListen----");
                    mFingerprintManager.setOnAuthenticateListen(mCallback);
                    //mFingerprintManager.authenticate();
                    new ThreadRun().start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                
            }
        }
    };
    
    
    private ServiceConnection connection = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            Log.d(TAG,"------------------KeyguardFingerprintService onServiceDisconnected -- keyguard--");
            mService = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mService = IKeyguardService.Stub.asInterface(service);
            Log.i("fenghaitao", "======onServiceConected==="+mService);
        }
    };
    
    Handler mHandler = new Handler(){
      @SuppressLint("NewApi")
    public void handleMessage(Message msg) {
          switch (msg.what) {
            case 3:
                Log.i(TAG, "------------KeyguardFingerprintService ----SERVICE 解锁==");
                    
                    if (keyguardMan.isKeyguardLocked()) {
                        if(!mPowerManager.isInteractive()){
                            mPowerManager.wakeUp(SystemClock.uptimeMillis());
                        }
                        try {
                            mService.keyguardDone(true, true);
                        } catch (RemoteException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                break;

            default:
                break;
        }
      };  
    };
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i(TAG, "========onBind=======");
        return null;
    }
    
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "====-------------KeyguardFingerprintService  ---OnCreate====");
       
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG, "========= KeyguardFingerprintService ---onStartCommand=======");
        mScreenObserver = new ScreenObserver(getApplicationContext());
        mScreenObserver.startObserver(screenListener);
        mybindService();
        
        Intent mFingerIntent = new Intent();
        mFingerIntent.setClass(getApplicationContext(), FingerprintManager.class);
        bindService(mFingerIntent, mIFingerConn, Context.BIND_AUTO_CREATE);
        
        keyguardMan = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        
        return super.onStartCommand(intent, flags, startId);
    }

    
    ScreenObserver.ScreenStateListener screenListener = new ScreenStateListener() {
        
        @Override
        public void onUserPresent() {
            Log.i(TAG, "===onUserPresent==");
            stopAuthenticate();
        }
        
        @Override
        public void onScreenOn() {
            Log.i(TAG, "==onScreenOn===");
            
        }
        
        @Override
        public void onScreenOff() {
            Log.i(TAG, "===onScreenOff==");
        }
    };
    
    private IAuthenticateCallback mCallback = new IAuthenticateCallback.Stub() {
        
        @Override
        public void onNoMatch() throws RemoteException {

        }

        @Override
        public void onIdentified(int fid) throws RemoteException {
            Log.i(TAG, "-------KeyguardFingerprintService--------test----");
            Log.i(TAG, "-------------------------KeyguardFingerprintService -Authenticate call back onIdentified----");
            Message message = new Message();
            message.arg1 = fid;
            message.what = 3;
            mHandler.sendMessage(message);
        }

        @Override
        public void onFingerRemoved() throws RemoteException {

        }

        @Override
        public void onCaptureFailed(int reason) throws RemoteException {
            Util.vibrate(getApplicationContext(), 200);

        }
    };



    public void mybindService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.systemui", "com.android.systemui.keyguard.KeyguardService"));
        bindService(intent, connection, BIND_AUTO_CREATE);
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unbindService(connection);
        unbindService(mIFingerConn);
        
    }
    
    class ThreadRun extends Thread{
        
        @Override
        public void run() {
            super.run();
            boolean hasLock;
            boolean isScreenOn;
            boolean isSetOnAuthListen = false;
            while(true){
                try {
                    sleep(500);
                } catch (Exception e) {

                }
                
                hasLock = (Util.readXML(getApplicationContext(), "lock", 0) == 1) ? true : false;
                isScreenOn = mPowerManager.isScreenOn();

                if(hasLock && !isSetOnAuthListen){
                    Log.d(TAG,"-----------KeyguardFingerprintService ---thread isSetOnAuthListen = true ");
                    isSetOnAuthListen = true;
                    try{
                        mFingerprintManager.setOnAuthenticateListen(mCallback);
                    }catch (RemoteException e){
                    }
                }else if(!hasLock && isSetOnAuthListen){
                    Log.d(TAG,"-----------KeyguardFingerprintService ---thread isSetOnAuthListen = false ");
                    isSetOnAuthListen = false;
                }
                //Log.d(TAG,"---------Thread ----isScreenOn = "+isScreenOn);
                //Log.d(TAG,"-----------------Locked ----"+keyguardMan.isKeyguardLocked() +" ,  hasLock="+hasLock);
                if (!hasLock) {
                    continue;
                }

                if (keyguardMan.isKeyguardLocked()) {
                 //   Log.d(TAG, "-----start authenticate-----");
                    authenticate();
                } else {
                   // Log.d(TAG, "-----stop authenticate-----");
                    stopAuthenticate();
                }
            }

        }
    }
    
    public void authenticate() {
        if (!isAlreadSetAuthticate) {
            isAlreadSetAuthticate = true;
            try {
                mFingerprintManager.authenticate();
            } catch (RemoteException e) {

            }
        }
    }
    
    public void stopAuthenticate(){
        if(isAlreadSetAuthticate){
            isAlreadSetAuthticate = false;
            try{
                mFingerprintManager.stopAuthenticate();
            }catch(RemoteException e){
                
            }
        }
    }
}

