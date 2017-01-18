package com.android.play;


import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entity.Constant;
import com.android.service.MediaBackService;
import com.android.service.MediaBackService.MyBinder;
import com.android.view.VerticalSeekBar;


/**
 * 视频播放类
 *
 * @author feng
 */
public class PlayVideoActivity extends AppCompatActivity {
    private String TAG = Constant.TAG;

    private ImageButton btn_pre, btn_play, btn_next;

    private SurfaceView surfaceView;
    private TextView tv_curtime, tv_totaltime;
    private SeekBar seekBar;
    private String path;
    private SurfaceHolder holder;
    private MediaBackService mediaService;
    private MediaBackService.MyBinder binder;
    private AudioManager audioManager;
    private ProgressDialog dialog;
    private GestureDetector mGestureDetector = null;
    private BroadcastReceiver receiver;
    private Window window;
    private boolean isRun = false, isLoop = false;
    private boolean isStop = true;
    private boolean isShow = false;
    private VerticalSeekBar verticalSeekBar;


    private Intent intent;
    private int time;
    private int screenWidth, screenHeight;
    private int position, progress;
    private LinearLayout linearLayout, linear_text;
    private FrameLayout ll_playview;

    private TextView mVideoName;
    private ImageButton mBackBt;
    private ImageView mVolumeImage;
    private ImageView mBatteryIamge;
    private Animation showVoicePanelAnimation;
    private Animation hiddenVoicePanelAnimation;
    private View mTopVideoview;

    private int downX,downY,tempX,tempY,moveX,moveY;
    private boolean isShowVolumeBar = false;


    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyBinder) service;
            mediaService = binder.getResult();
            if (mediaService != null) {
                mediaService.activityNotify();
                Log.i("TAG", "Conn");
            }
        }
    };

    private BroadcastReceiver mReceiverBattery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
                int level = intent.getIntExtra("level",0);

                int status = intent.getIntExtra("status", 0);

                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                Log.i(TAG, " Battery changed level : "+level+" , isCharging = "+isCharging);
                if(isCharging){
                    if(level == 100){
                        mBatteryIamge.setImageResource(R.drawable.battery_full);
                    }else {
                        mBatteryIamge.setImageResource(R.drawable.battery_charging);
                    }
                }else {
                    if(level == 10){
                        mBatteryIamge.setImageResource(R.drawable.battery_10);
                    }else if(level == 20){
                        mBatteryIamge.setImageResource(R.drawable.battery_20);
                    }else if(level == 50){
                        mBatteryIamge.setImageResource(R.drawable.battery_50);
                    }else if(level == 80) {
                        mBatteryIamge.setImageResource(R.drawable.battery_80);
                    }else if(level == 100){
                        mBatteryIamge.setImageResource(R.drawable.battery_100);
                    }
                }

            }
        }
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_video_suface);
        fullscreen(true);

        initFindView();
        setListener();

        position = getIntent().getIntExtra("position", 0);
        path = Constant.mediaList.get(position).getPath();
        Log.i(TAG, "=======oncreat==path====" + path);
        tv_curtime.setText("00:00:00");
        window = getWindow();


        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new MyCallback());
        bindService();

        receiver = new ServiceBroadcast();
        IntentFilter filter = new IntentFilter("mediaplay");
        registerReceiver(receiver, filter);

        IntentFilter batteryFiltet = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mReceiverBattery,batteryFiltet);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        verticalSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        verticalSeekBar.setVisibility(View.GONE);
        verticalSeekBar.setClickable(false);
        mVolumeImage.setVisibility(View.GONE);
        mVolumeImage.setClickable(false);

        showVoicePanelAnimation = AnimationUtils.loadAnimation(PlayVideoActivity.this, R.anim.puch_up_in);
        hiddenVoicePanelAnimation = AnimationUtils.loadAnimation(PlayVideoActivity.this, R.anim.push_up_out);



        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    mediaService.seekTo(progress);
                }

            }
        });


    }

    private void bindService() {
        Log.i(TAG, "====绑定 服务===");
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.android.play", "com.android.service.MediaBackService"));
        bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
    }

    public void showView() {
        linear_text.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        mTopVideoview.setVisibility(View.VISIBLE);
    }

    public void hideView() {

        linear_text.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        mTopVideoview.setVisibility(View.GONE);
    }

    private void fullscreen(boolean enable) {
        if (enable) { //显示状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else { //隐藏状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public void getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }


    @SuppressWarnings("deprecation")
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        File file = new File(getFilesDir().getParentFile().getPath() + "/shared_prefs/backsetting.xml");
        if (file.exists()) {
            SharedPreferences sp = getSharedPreferences("backsetting", 0);
            int res = sp.getInt("background", -1);
            ll_playview.setBackgroundResource(res);
        }

        Log.i("TAG", "start");

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.i(TAG, "restart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        Log.i(TAG, "SaveInstanceState play ");
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mediaService.isPlay()) {
            mediaService.pause();
        }
        time = mediaService.getCurrentPosition();
        btn_play.setBackgroundResource(R.drawable.start_button_video);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG, "PlayVideoActivity : onBackPressed");
        if (mediaService.isPlay()) {
            mediaService.pause();
            isRun = false;
            btn_play.setBackgroundResource(R.drawable.start_button_video);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (time != 0) {
            mediaService.seekTo(time);
            mediaService.play();
            btn_play.setBackgroundResource(R.drawable.pause_button_video);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, " PlayVideo : Destroy");
        if (mediaService.isPlay()) {
            mediaService.pause();
            mediaService.stop();
            isRun = false;
            handler.removeMessages(0x01);
        } else {
            mediaService.stop();
            isRun = false;
            handler.removeMessages(0x01);
        }

        unregisterReceiver(receiver);
        unregisterReceiver(mReceiverBattery);
        unbindService(conn);
    }

    //
    class ServiceBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra("ok", -1);
            switch (status) {
                case Constant.PLAY:
                    Log.i("TAG", "broadcast");
                    Log.i(TAG, "========start==path====" + path);
                    mediaService.preparePlay(holder, path);
                    mediaService.mediaPlayer.setScreenOnWhilePlaying(true);
                    mediaService.mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            time = mp.getDuration();
                            seekBar.setMax(time);
                            tv_totaltime.setText(Constant.movieTimeFormat(time));
                            mVideoName.setText(Constant.mediaList.get(position).getName());
                            mediaService.play();

                            if (progress != 0) {
                                mediaService.seekTo(progress);
                            }
                            isRun = true;
                            handler.sendEmptyMessage(0x01);
                            btn_play.setBackgroundResource(R.drawable.pause_button_video);
                            Log.i("TAG", time + "=time");
                        }
                    });
                    //
                    mediaService.mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub

                            if (isLoop) {
                                path = Constant.mediaList.get(position).getPath();
                                mediaService.activityNotify();
                            } else {
                                tv_curtime.setText("00:00:00");
                                seekBar.setProgress(0);
                                btn_play.setBackgroundResource(R.drawable.start_button_video);
                                position++;
                                if (position < Constant.mediaList.size()) {
                                    path = Constant.mediaList.get(position).getPath();
                                    mediaService.activityNotify();
                                } else {
                                    position = 0;
                                    path = Constant.mediaList.get(position).getPath();
                                    mediaService.activityNotify();
                                }
                            }
                        }
                    });
                    Log.i("TAG", "broadcast");
                    break;

                default:
                    break;
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x01://
                    if (isRun) {
                        if (seekBar.getProgress() <= seekBar.getMax()) {
                            seekBar.setProgress(mediaService.getCurrentPosition());
                            tv_curtime.setText(Constant.movieTimeFormat(mediaService.getCurrentPosition()));
                        }
                        handler.sendEmptyMessage(0x01);
                    }
                    break;
                case 0x02:
                    hideView();
                    isShow = false;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int)event.getRawX();
                downY = tempY = (int)event.getRawY();
                Log.i(TAG," PLAYVIDEO Activity ACTION DOWN : downX : "+downX+" , downY : "+downY);
                break;
            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getRawX();
                moveY = (int) event.getRawY();
                Log.i(TAG,"PLAYVIDEO ontuch ACTION MOVE  movex : "+moveX+" , moveY : "+moveY);
                if(Math.abs(moveX- downX)>100 && Math.abs(moveY - downY) <50){
                    if(moveX > downX){
                        fastForward(moveX-downX);
                    }else {
                        fastBack(downX-moveX);
                    }
                }

                if(Math.abs(moveY-downY)> 20 && Math.abs(moveX - downX)<20){
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    Log.i(TAG, " PLAYVIDEO maxvolume : "+ maxVolume + " isShowVolumeBar : "+isShowVolumeBar);
                    if(!isShowVolumeBar) {
                        isShowVolumeBar = true;
                        showVolumeBar();
                    }
                    if(moveY > downY){
                        mSeekHandler.removeMessages(0x22);
                        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        Log.i(TAG," play VIDEO volume : "+volume);
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0);

                        verticalSeekBar.setProgress(volume);
                        mSeekHandler.sendEmptyMessageDelayed(0x22,4000);
                    }else {
                        mSeekHandler.removeMessages(0x22);
                        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        Log.i(TAG," play VIDEO volume : "+volume);
                        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0);
                        verticalSeekBar.setProgress(volume);
                        mSeekHandler.sendEmptyMessageDelayed(0x22,4000);
                    }
                }
                break;
        }

        if(!isShow) {
            isShow = true;
            showView();
            handler.sendEmptyMessageDelayed(0x02, 4000);
        }
        return false;
    }

    private void showVolumeBar(){
        verticalSeekBar.startAnimation(showVoicePanelAnimation);
        verticalSeekBar.setVisibility(View.VISIBLE);

        mVolumeImage.startAnimation(showVoicePanelAnimation);
        mVolumeImage.setVisibility(View.VISIBLE);
    }

    private Handler mSeekHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x22) {
                isShowVolumeBar = false;
                verticalSeekBar.startAnimation(hiddenVoicePanelAnimation);
                verticalSeekBar.setVisibility(View.GONE);
                mVolumeImage.startAnimation(hiddenVoicePanelAnimation);
                mVolumeImage.setVisibility(View.GONE);
            }
        }
    };

    //快进
    public void fastForward(int moveLong) {
        int currentPosition = 1000*(moveLong/100);
        Log.i(TAG," PLAYVIDEO  fastForward , currentPositon = "+currentPosition);
        if (mediaService.isPlay()) {
            mediaService.pause();
            isRun = false;
            if(mediaService.getDuration() > currentPosition) {
                mediaService.seekTo(mediaService.getCurrentPosition() + currentPosition);
            }
            isRun = true;
            handler.sendEmptyMessage(0x01);
            mediaService.play();
        } else {
            if(mediaService.getDuration() > currentPosition) {
                mediaService.seekTo(mediaService.getCurrentPosition() + currentPosition);
            }
            seekBar.setProgress(mediaService.getCurrentPosition());
            tv_curtime.setText(Constant.movieTimeFormat(mediaService.getCurrentPosition()));
        }

    }

    //快退
    public void fastBack(int moveLong) {
        int currentPosition = 1000*(moveLong/100);
        Log.i(TAG," PLAYVIDEO  fastback , currentPositon = "+currentPosition);
        if (mediaService.isPlay()) {
            mediaService.pause();
            isRun = false;
            if(mediaService.getCurrentPosition() != 0) {
                mediaService.seekTo(mediaService.getCurrentPosition() - currentPosition);
            }
            isRun = true;
            handler.sendEmptyMessage(0x01);
            mediaService.play();
        } else {
            if(mediaService.getCurrentPosition() != 0) {
                mediaService.seekTo(mediaService.getCurrentPosition() - currentPosition);
            }
            seekBar.setProgress(mediaService.getCurrentPosition());
            tv_curtime.setText(Constant.movieTimeFormat(mediaService.getCurrentPosition()));
        }
    }


    class MyCallback implements Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "holder");


        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            Log.i(TAG, "===surfaceDestroyed");

        }

    }

    public void initFindView() {
        btn_pre = (ImageButton) findViewById(R.id.ibtn_prev);
        btn_play = (ImageButton) findViewById(R.id.ibtn_play);
        btn_next = (ImageButton) findViewById(R.id.ibtn_next);

        tv_curtime = (TextView) findViewById(R.id.tv_hasPlayed);
        tv_totaltime = (TextView) findViewById(R.id.tv_duration);


        seekBar = (SeekBar) findViewById(R.id.sbar_progress);
        surfaceView = (SurfaceView) findViewById(R.id.sfv_image);

        ll_playview = (FrameLayout) findViewById(R.id.fr_playview);

        linearLayout = (LinearLayout) findViewById(R.id.layout_control);//

        linear_text = (LinearLayout) findViewById(R.id.layout_progress);//

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.vertical_seekbar);

        mBackBt = (ImageButton) findViewById(R.id.video_top_back);
        mVolumeImage = (ImageView) findViewById(R.id.iv_volume_tag);
        mBatteryIamge = (ImageView) findViewById(R.id.iv_video_battery);
        mVideoName = (TextView) findViewById(R.id.tv_video_name);
        mTopVideoview = findViewById(R.id.video_top_layout);

    }

    public void setListener() {
        btn_pre.setOnClickListener(new MyOnClickListener());
        btn_play.setOnClickListener(new MyOnClickListener());
        btn_next.setOnClickListener(new MyOnClickListener());
        mBackBt.setOnClickListener(new MyOnClickListener());
    }

    class MyOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ibtn_prev:
                    position--;
                    if (position >= 0) {
                        path = Constant.mediaList.get(position).getPath();
                        mediaService.activityNotify();
                    } else {
                        position = Constant.mediaList.size() - 1;
                        path = Constant.mediaList.get(position).getPath();
                        mediaService.activityNotify();
                    }
                    break;
                case R.id.ibtn_play:
                    handler.sendEmptyMessage(0x01);
                    if (mediaService.isPlay()) {
                        mediaService.pause();
                        isRun = false;
                        btn_play.setBackgroundResource(R.drawable.start_button_video);
                    } else {
                        mediaService.play();
                        isRun = true;
                        btn_play.setBackgroundResource(R.drawable.pause_button_video);
                    }

                    break;

                case R.id.ibtn_next:
                    position++;
                    if (position < Constant.mediaList.size()) {
                        path = Constant.mediaList.get(position).getPath();
                        mediaService.activityNotify();
                    } else {
                        position = 0;
                        path = Constant.mediaList.get(position).getPath();
                        mediaService.activityNotify();
                    }
                    break;
                case R.id.video_top_back:
                    if (mediaService.isPlay()) {
                        mediaService.pause();
                        isRun = false;
                        btn_play.setBackgroundResource(R.drawable.start_button_video);
                    }
                    finish();
                    break;
                default:
                    break;
            }
        }

    }


}
