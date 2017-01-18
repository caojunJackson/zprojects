package com.android.play;


import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.android.adapter.MusicListAdapter;
import com.android.aidl.IMediaplayInterface;
import com.android.entity.Constant;

public class MusicListActivity extends ActionBarActivity implements View.OnClickListener,AdapterView.OnItemClickListener,SeekBar.OnSeekBarChangeListener{

    private String TAG = Constant.TAG;

    private ListView mMusicListView;
    private ImageButton mPlayButton;
    private ImageButton mNextButton;
    private TextView mSongName;
    private ImageView mSongImage;
    private SeekBar mSeekBar;
    private MusicListAdapter mAdapter;
    private View mLayoutTools;

    private IMediaplayInterface IMediaplay;

    private int position = 0;
    private int progress = 0;
    private String path;
    private boolean isRun = true;
    private boolean isFirst = false;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG,"=====ServiceConnected===");
            IMediaplay = IMediaplayInterface.Stub.asInterface(iBinder);
            try {
                IMediaplay.preparePlay(Constant.musicList.get(position).getPath());
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
                    mSeekBar.setMax(time);
                    mSongName.setText(Constant.musicList.get(position).getmName());
                    Log.i(TAG, "=====prepare ==isFirst="+isFirst);

                    if(isFirst) {
                        mPlayButton.setBackgroundResource(R.drawable.start_button_video);
                    }else{
                        mPlayButton.setBackgroundResource(R.drawable.pause_button_video);
                    }
                    if (!isFirst) {
                        IMediaplay.start();
                    }else{
                        isFirst = false;
                    }
                    handler.sendEmptyMessage(0x01);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }else if("com.android.media.completion".equals(action)){
                if(isFirst){
                    isFirst = false;
                }
                mSeekBar.setProgress(0);
                mPlayButton.setBackgroundResource(R.drawable.pause_button_video);
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


        setContentView(R.layout.activity_music_list);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Log.i(TAG,"====== 按====actionbar="+actionBar);
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        }
        isFirst = getIntent().getBooleanExtra("isFirst", true);
        initView();

        bindService();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.media.completion");
        filter.addAction("com.android.media.prepare");
        registerReceiver(receiver,filter);

        mSongImage.setImageBitmap(Constant.toRoundCorner((BitmapFactory.decodeResource(getResources(), R.drawable.music_list_image)),2));
        mAdapter = new MusicListAdapter(this);
        mMusicListView.setAdapter(mAdapter);


    }

    private  void initView(){
        mMusicListView = (ListView) findViewById(R.id.music_listview);
        mPlayButton = (ImageButton) findViewById(R.id.music_bt_play);
        mNextButton = (ImageButton) findViewById(R.id.music_bt_next);
        mSongImage = (ImageView) findViewById(R.id.imageview_music_bottom);
        mSongName = (TextView) findViewById(R.id.textview_songname);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar_music_list);

        mLayoutTools = findViewById(R.id.music_bottom_tools);
    }

    private void initListenView(){
        mMusicListView.setOnItemClickListener(this);
        mPlayButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
        mLayoutTools.setOnClickListener(this);
    }

    private void bindService(){
        Log.i(TAG,"====绑定 服务===");
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.android.play","com.android.service.IMediaplayService"));
        bindService(serviceIntent,connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // 当Action Bar的图标被单击时执行下面的finish
                Log.i(TAG,"MusicListActivity :onOptionsItemSelectes");
                try {
                    if (IMediaplay.isplay())
                        IMediaplay.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListenView();
        Log.i(TAG,"MusicListActivity : onResume ");
        SharedPreferences sp = getSharedPreferences("song", 0);
        position = sp.getInt("position", 0);
        mSongName.setText(Constant.musicList.get(position).getmName());
        mMusicListView.setSelection(position);
        Log.i(TAG,"===MusicListActiy==onCreate ==position="+position);
        try {
            if(IMediaplay != null) {
                if (IMediaplay.isplay()) {
                    mPlayButton.setBackgroundResource(R.drawable.pause_button_video);
                } else {
                    mPlayButton.setBackgroundResource(R.drawable.start_button_video);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG,"MusicListActivity : onBackPressed");
        try {
            if(IMediaplay.isplay())
                IMediaplay.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, " MusicListActivity : onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = getSharedPreferences("song",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", position);
        editor.apply();
        Log.i(TAG, " MusicListActivity : onDestroy");
        isRun = false;
        handler.removeMessages(0x01);
        try {
            IMediaplay.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        unbindService(connection);
        unregisterReceiver(receiver);

    }

    @Override
    public void onClick(View view) {
        Log.i(TAG,"====music list activity==onclick==");
        switch (view.getId()){
            case R.id.music_bt_play:
                Log.i(TAG,"====按播放===");
                try {
                    handler.sendEmptyMessage(0x01);
                    if(IMediaplay.isplay()){
                        IMediaplay.pause();
                        isRun = false;
                        mPlayButton.setBackgroundResource(R.drawable.start_button_video);
                    }else{
                        Log.i(TAG,"======= 开始 =");
                        IMediaplay.start();
                        isRun = true;
                        mPlayButton.setBackgroundResource(R.drawable.pause_button_video);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.music_bt_next:
                Log.i(TAG,"======按 下一首==");
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
            case R.id.music_bottom_tools:
                Log.i(TAG,"======按 地步====");
                Intent mMainMusic = new Intent();
                mMainMusic.setClass(this,MusicMainActivity.class);
                mMainMusic.putExtra("postion", position);
                //mMainMusic.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(mMainMusic, 1);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"=========按=====onActivity Result ====");
        if(data != null && requestCode== 1){
            position = data.getIntExtra("position",0);
            Log.i(TAG,"=========按=====onActivity Result ===="+position);
            mSongName.setText(Constant.musicList.get(position).getmName());
            isRun = true;
            handler.sendEmptyMessage(0x01);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i(TAG,"===onItemClick===position="+position);
        this.position = position;
        isFirst = false;
        path = Constant.musicList.get(position).getPath();
        try {
            IMediaplay.preparePlay(path);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
           // Log.i(TAG,"===onProgressChanged====progress=="+progress);
        this.progress = progress;
        try {
            if(b)
                IMediaplay.seekTo(progress);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01:
                    try {
                       // Log.i(TAG,"==============handler===isRun="+isRun+"======"+IMediaplay.isplay());
                        if(isRun && IMediaplay.isplay()){
                            if(mSeekBar.getProgress()<=mSeekBar.getMax()){
                                try {
                                    mSeekBar.setProgress(IMediaplay.getCurrentPosition());
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            handler.sendEmptyMessage(0x01);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
