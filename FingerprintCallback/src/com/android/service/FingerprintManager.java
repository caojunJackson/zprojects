package com.android.service;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.activity.Util;
import com.android.aidl.IAuthenticateCallback;
import com.android.aidl.IEnrollCallback;
import com.android.aidl.IFingerprintManager;

import ma.release.Fingerprint;
import ma.release.Jnifp;

import android.os.PowerManager;
import android.content.Context;
import android.app.KeyguardManager;


public class FingerprintManager extends Service {

    private String TAG = "fenghaitao";
    
    IAuthenticateCallback mIAuthenticateCallback;
    IEnrollCallback mIEnrollCallback;
    
    Fingerprint mFinger;
    private boolean isAlreadyOpen= false;
    private int ret;
    
    
    @Override
    public IBinder onBind(Intent intent) {
        
        return mStub;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.i(TAG, "=====FingerprintManager onCreate===");
        mFinger = Fingerprint.getInstance();
        
        if(mFinger == null){
            mFinger = Fingerprint.getInstance();
        }
        
        if(mFinger != null && !isAlreadyOpen){
            Log.d(TAG,"----------------FingerprintManager ---onAuthenticate Listen---");
            mFinger.setOnAuthenticateListen(mAuthenticateCallback);
            mFinger.setOnEnrollListen(mEnrollCallback);
            new Jnifp().setNotify();
            if(!isAlreadyOpen){
                ret = Jnifp.open(getApplicationContext().getFilesDir().toString());
                if(ret == 0){
                    isAlreadyOpen = true;
                    
                }
            }
        }
        
        startKeyguardService();
    }
    
    private void startKeyguardService() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), KeyguardFingerprintService.class);
        startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i(TAG, "=====FingerprintManager startCommand===");
        if(mFinger == null){
            mFinger = Fingerprint.getInstance();
        }
        
        if(mFinger != null){
            Log.d(TAG, "-----------onStartCommand --- set onAuthenticate---");
            mFinger.setOnAuthenticateListen(mAuthenticateCallback);
            mFinger.setOnEnrollListen(mEnrollCallback);
        }
        
        
        return super.onStartCommand(intent, flags, startId);
    }
   

    
     IFingerprintManager.Stub mStub = new IFingerprintManager.Stub() {
        
        @Override
        public int stopAuthenticate() throws RemoteException {
            // TODO Auto-generated method stub
            Jnifp.stopAuthenticate();
            return 0;
        }
        
        @Override
        public void setOnEnrollListen(IEnrollCallback callback) throws RemoteException {
            // TODO Auto-generated method stub
            mIEnrollCallback = callback;
        }
        
        @Override
        public void setOnAuthenticateListen(IAuthenticateCallback callback) throws RemoteException {
            // TODO Auto-generated method stub
            mIAuthenticateCallback = callback;
        }
        
        @Override
        public int remove(int fid) throws RemoteException {
            
            Jnifp.remove(fid);
            return 0;
        }
        
        @Override
        public int enumerate(int[] dat, int N) throws RemoteException {
            // TODO Auto-generated method stub
            Jnifp.enumerate(dat, N);
            return 0;
        }
        
        @Override
        public int preEnroll() throws RemoteException {
            Log.i(TAG, "=============FingerprintManager preEnroll ======");
            Jnifp.preEnroll();
            return 0;
        }

        @Override
        public int enroll() throws RemoteException {
            // TODO Auto-generated method stub
            Log.i(TAG, "=====FingerprintManager enroll===");
            Jnifp.enroll();
            return 0;
        }
        @Override
        public int postEnroll() throws RemoteException {
            Log.i(TAG, "=============FingerprintManager postEnroll ======");
            Jnifp.postEnroll();
            return 0;
        }

        @Override
        public int authenticate() throws RemoteException {
            // TODO Auto-generated method stub
                Jnifp.authenticate();
            return 0;
        }

        @Override
        public int resetVolt() throws RemoteException {
            return Jnifp.resetVolt();
        }
    };
    
    
    
    Fingerprint.IAuthenticateCallback mAuthenticateCallback = new Fingerprint.IAuthenticateCallback() {

        @Override
        public void onNoMatch() {
            super.onNoMatch();
            try {
                mIAuthenticateCallback.onNoMatch();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onIdentified(int fid) {
            super.onIdentified(fid);
            Log.i(TAG,"----------FingerprintManager onIdentified----");
            try {
                mIAuthenticateCallback.onIdentified(fid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCaptureFailed(int reason) {
            super.onCaptureFailed(reason);
            try {
                mIAuthenticateCallback.onCaptureFailed(reason);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFingerRemoved() {
            super.onFingerRemoved();
            try {
                mIAuthenticateCallback.onFingerRemoved();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    
    };

    Fingerprint.IEnrollCallback mEnrollCallback = new Fingerprint.IEnrollCallback() {

        @Override
        public void onCaptureCompleted() {
            super.onCaptureCompleted();
            try {
                mIEnrollCallback.onCaptureCompleted();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCaptureFailed(int code) {
            super.onCaptureFailed(code);
            try {
                mIEnrollCallback.onCaptureFailed(code);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProgress(int num) {
            super.onProgress(num);
            try {
                mIEnrollCallback.onProgress(num);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEnrolled(int fid) {
            super.onEnrolled(fid);
            try {
                mIEnrollCallback.onEnrolled(fid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEnrollmentFailed() {
            super.onEnrollmentFailed();
            try {
                mIEnrollCallback.onEnrollmentFailed();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFingerRemoved() {
            super.onFingerRemoved();
            try {
                mIEnrollCallback.onFingerRemoved();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    };
    
}
