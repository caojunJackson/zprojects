package com.android.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.android.aidl.IMediaplayInterface;
import com.android.entity.Constant;
import com.android.entity.MusicMessage;
import com.android.play.FullscreenActivity;
import com.android.receiver.MusicScreenOffShowReceiver;

import java.io.IOException;
import java.util.List;

public class IMediaplayService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener{
    private String TAG = Constant.TAG;

    public MediaPlayer mMediaPlayer;
    private boolean isRing=false;
    private TelephonyManager tm;
    private String mPath;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
                if(mMediaPlayer.isPlaying()) {
                    Intent mIntent = new Intent();
                    mIntent.setAction("com.android.keygard.activity");
                    mIntent.putExtra("path",mPath);
                    context.sendBroadcast(mIntent);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i(TAG,"===onCreate==IMediaplayService=="+mMediaPlayer);
    }

    PhoneStateListener listener=new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "  挂断  CALL STATE IDLE " + isRing);
                    if (isRing){
                        isRing = false;
                        mMediaPlayer.start();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "  接听  CALL STATE OFFHOOK");
                    System.out.println("接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "响铃:来电号码"+incomingNumber);
                    isRing = true;
                    mMediaPlayer.pause();
                    break;
            }
        }
    };

    private  void addMediaplayListenser(){
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG,"IMediaplayService  onBind ");
        mMediaPlayer = new MediaPlayer();
        tm = (TelephonyManager)getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        addMediaplayListenser();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(receiver,filter);
        Log.i(TAG,"===onBind==IMediaplayService=="+mMediaPlayer);
        return iBinder;
    }

    private IBinder iBinder = new IMediaplayInterface.Stub(){
        @Override
        public void preparePlay(String path) throws RemoteException {
            if(mMediaPlayer == null){return;}
            mPath = path;
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
         public boolean isplay() throws RemoteException {
             if(mMediaPlayer == null){
                 Log.i(TAG,"===isplay==mMediaPlayer="+mMediaPlayer);
                 return false;
             }
             return mMediaPlayer.isPlaying();
         }

         @Override
         public void prepare() throws RemoteException {
             if(mMediaPlayer == null){
                 return;
             }
             try {
                 mMediaPlayer.prepareAsync();
                 //mMediaPlayer.prepare();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         @Override
         public void pause() throws RemoteException {
             if(mMediaPlayer != null){
                 mMediaPlayer.pause();
             }
         }

         @Override
         public void start() throws RemoteException {
             if (mMediaPlayer != null){
                 if(!mMediaPlayer.isPlaying()){
                     mMediaPlayer.start();
                 }

             }
         }

         @Override
         public void stop() throws RemoteException {
             if (mMediaPlayer != null){
                 if (mMediaPlayer.isPlaying()){
                     Log.i(TAG," IMediaplayService  isPlay ");

                     mMediaPlayer.pause();
                     mMediaPlayer.stop();
                     mMediaPlayer.release();
                     mMediaPlayer = null;
                 }else {
                     mMediaPlayer.stop();
                     mMediaPlayer.release();
                     mMediaPlayer = null;
                 }


             }
         }

         @Override
         public int getDuration() throws RemoteException {
             return mMediaPlayer.getDuration();
         }

         @Override
         public void seekTo(int position) throws RemoteException {
                mMediaPlayer.seekTo(position);
         }

         @Override
         public void looper() throws RemoteException {
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        try {
                            start();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
         }

         @Override
         public int getCurrentPosition() throws RemoteException {
             if(mMediaPlayer ==null){
                 return 0;
             }
             return mMediaPlayer.getCurrentPosition();
         }

        @Override
        public int getMusciPostionAtList(String path) throws RemoteException {
            int position = 0;
            if (path == null){return  0;}
            List<MusicMessage> musicList = Constant.musicList;

            for(int i = 0 ;i < musicList.size() ;i++){
                if(musicList.get(i).getPath().equals(path)){
                    position = i;
                    break;
                }
            }

            return position;
        }
    };

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.i(TAG,"'=======onCompletion==");
        Intent intent = new Intent();
        intent.setAction("com.android.media.completion");
        sendBroadcast(intent);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG,"=====onPrepared===");
        Intent intent = new Intent();
        intent.setAction("com.android.media.prepare");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
