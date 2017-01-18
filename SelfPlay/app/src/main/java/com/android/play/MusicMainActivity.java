package com.android.play;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aidl.IMediaplayInterface;
import com.android.entity.Constant;
import com.android.view.MusicPlayerView;


public class MusicMainActivity extends Activity implements View.OnClickListener{
    private String TAG = Constant.TAG;

    private TextView mSongName;
    private TextView mSinger;
    private ImageView mImagePrevious;
    private ImageView mImageNext;

    private IMediaplayInterface IMediaplay;

    MusicPlayerView mpv;
    private int position;
    private int time;
    private String path;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"== ======MusicMainActity===ServiceConnected=======");
            IMediaplay = IMediaplayInterface.Stub.asInterface(iBinder);
            try {
                Log.i(TAG,"=======main MUSIC ========"+IMediaplay);
                if(IMediaplay != null) {
                    time = IMediaplay.getDuration();
                    Log.i(TAG,"===========main music==time=="+time);

                    mpv.setMax(time/1000);
                    mpv.setProgress(IMediaplay.getCurrentPosition()/1000);

                    if(IMediaplay.isplay()){
                        mpv.start();
                    }else{
                        mpv.stop();
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("com.android.media.prepare".equals(action)){
                try {
                    int time = IMediaplay.getDuration();
                    mpv.setMax(time/1000);
                    mSongName.setText(Constant.musicList.get(position).getmName());
                    mSinger.setText(Constant.musicList.get(position).getArtist());
                    IMediaplay.start();
                    mpv.setProgress(IMediaplay.getCurrentPosition()/1000);
                    if(IMediaplay.isplay()){
                        mpv.start();
                    }else{
                        mpv.stop();
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }else if("com.android.media.completion".equals(action)){
                mpv.setProgress(0);

                position++;
                if(position <= Constant.musicList.size()-1){
                    path= Constant.musicList.get(position).getPath();
                    try {
                        IMediaplay.preparePlay(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else {
                    position = 0;
                    path= Constant.musicList.get(position).getPath();
                    try {
                        IMediaplay.preparePlay(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);

        initView();
        initViewListener();

        this.position = getIntent().getIntExtra("postion",0);
        Log.i(TAG,"====MusicMainActity====getIntent ==postion="+position);
        mSinger.setText(Constant.musicList.get(position).getArtist());
        mSongName.setText(Constant.musicList.get(position).getmName());

        mpv.setCoverURL("https://upload.wikimedia.org/wikipedia/en/b/b3/MichaelsNumberOnes.JPG");

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.media.completion");
        filter.addAction("com.android.media.prepare");
        registerReceiver(receiver,filter);

        bindService();
    }

    private void initView(){
        mpv = (MusicPlayerView) findViewById(R.id.mpv);
        mSinger = (TextView) findViewById(R.id.textViewSinger);
        mSongName =(TextView) findViewById(R.id.textViewSong);
        mImagePrevious = (ImageView) findViewById(R.id.previous);
        mImageNext = (ImageView) findViewById(R.id.next);
    }

    private void initViewListener(){
        mpv.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
    }

    private void bindService(){
        Log.i(TAG,"====绑定 服务===");
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.android.play","com.android.service.IMediaplayService"));
        bindService(serviceIntent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {

        Log.i(TAG,"=======按 返回键====Music MAIN ACTIVITY==");
        Intent backIntent = new Intent();
        backIntent.putExtra("position",position);
        setResult(Activity.RESULT_OK,backIntent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("song", 0);
        position = sp.getInt("position", 0);
        mSongName.setText(Constant.musicList.get(position).getmName());
        mSinger.setText(Constant.musicList.get(position).getArtist());
    }


    @Override
    protected void onStop() {
        super.onStop();
        mpv.stop();
        Log.i(TAG,"Music Main Activity : onStop ");
    }

    @Override
    protected void onDestroy() {
        unbindService(connection);
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.mpv:
                try {
                    Log.i(TAG,"=====按 MPV===isRotating="+mpv.isRotating()+" , ====isPlay="+IMediaplay.isplay());
                    if (mpv.isRotating() && IMediaplay.isplay()) {
                        mpv.stop();

                        try {
                            IMediaplay.pause();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            IMediaplay.start();
                            mpv.setProgress(IMediaplay.getCurrentPosition()/1000);
                            mpv.start();

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.previous:
                position--;
                if(position >= 0){
                    path= Constant.musicList.get(position).getPath();
                    try {
                        IMediaplay.preparePlay(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else {
                    position = Constant.musicList.size()-1;
                }
                break;
            case R.id.next:
                position++;
                if(position < Constant.musicList.size()){
                    path= Constant.musicList.get(position).getPath();
                    try {
                        IMediaplay.preparePlay(path);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }else {
                    position = 0;
                }
                break;

        }
    }
}

