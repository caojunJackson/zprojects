package com.android.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.aidl.IMediaplayInterface;
import com.android.entity.Constant;
import com.android.view.SildingFinishFramLayout;

import java.util.Calendar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends Activity implements View.OnClickListener,SildingFinishFramLayout.OnSildingFinishListener {
    private String TAG = Constant.TAG;

    private ImageButton mImageNext;
    private ImageButton mImagePre;
    private ImageButton mImagePlayorPause;
    private TextView mSongName;
    private TextView mSongAuth;
    private TextView mTimeSec;
    private TextView mTimeMon;
    private SildingFinishFramLayout mSildLayout;
    private IMediaplayInterface IMediaplay;

    private TextView tv1,tv2,tv3,tv4;
    private int count = 1;
    private int position;
    private int time;
    private String path;
    private static int ONE = 1;
    private static int TWO = 2;
    private static int THREE = 3;
    private static int FOUR = 4;
    private static int ZERO = 0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == ZERO) {
                getMonthAndDay();
                getHourAndMinutes();
                Log.i(TAG,"FullScreen : Handel ZERO");
                mHandler.sendEmptyMessageDelayed(ZERO,500);
            }else if (msg.what == ONE){
                flashText(count);
                Log.i(TAG,"FullScreen : Handel ONE");
                count++;
                if(count > 4){
                    count = 1;
                }
                mHandler.sendEmptyMessageDelayed(ONE,500);
            }

        }
    };


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "== ======FullScreenACTIVIY===ServiceConnected=======");
            IMediaplay = IMediaplayInterface.Stub.asInterface(iBinder);
            try {
                position = IMediaplay.getMusciPostionAtList(path);
                mSongName.setText(Constant.musicList.get(position).getmName());
                mSongAuth.setText(Constant.musicList.get(position).getArtist());
                if(IMediaplay.isplay()){
                    mImagePlayorPause.setImageResource(R.drawable.keyguard_button_pause);
                }else {
                    mImagePlayorPause.setImageResource(R.drawable.keyguard_button_play);
                }
            }catch (RemoteException e){
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
                    mSongName.setText(Constant.musicList.get(position).getmName());
                    mSongAuth.setText(Constant.musicList.get(position).getArtist());
                    IMediaplay.start();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }else if("com.android.media.completion".equals(action)){
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

            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.activity_fullscreen);

        path = getIntent().getStringExtra("path");

        initView();
        initViewListener();
        mSildLayout.setEnableLeftSildeEvent(true);
        mSildLayout.setEnableRightSildeEvent(false);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.media.completion");
        filter.addAction("com.android.media.prepare");
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);

        bindService();

        mHandler.sendEmptyMessage(ZERO);
        mHandler.sendEmptyMessage(ONE);
    }

    private void bindService(){
        Log.i(TAG,"==FullScreenActivity==绑定 服务===");
        Intent serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.android.play","com.android.service.IMediaplayService"));
        bindService(serviceIntent,connection, Context.BIND_AUTO_CREATE);
    }

    private void initView(){
        mImageNext = (ImageButton) findViewById(R.id.screen_bt_next);
        mImagePre = (ImageButton) findViewById(R.id.screen_bt_pres);
        mImagePlayorPause = (ImageButton) findViewById(R.id.screen_bt_playorpause);

        mSongName = (TextView) findViewById(R.id.screen_tv_songname);
        mSongAuth = (TextView) findViewById(R.id.screen_tv_songauth);
        mTimeMon = (TextView) findViewById(R.id.screen_tv_month);
        mTimeSec = (TextView) findViewById(R.id.screen_tv_sec);

        mSildLayout = (SildingFinishFramLayout) findViewById(R.id.sildinglayout);

        tv1 = (TextView)findViewById(R.id.tv_scoll_1);
        tv2 = (TextView)findViewById(R.id.tv_scoll_2);
        tv3 = (TextView)findViewById(R.id.tv_scoll_3);
        tv4 = (TextView)findViewById(R.id.tv_scoll_4);
    }

    private  void initViewListener(){
        mImageNext.setOnClickListener(this);
        mImagePre.setOnClickListener(this);
        mImagePlayorPause.setOnClickListener(this);

        mSildLayout.setOnSildingFinishListener(this);
    }

    private void getMonthAndDay(){
        Calendar c = Calendar.getInstance();
        String [] weekArray = new String[]{"周日","周一","周二","周三","周四","周五","周六"};
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int week = c.get(Calendar.DAY_OF_WEEK)-1;

        Log.i(TAG,"FullscreenActivity : month : "+month +" , day : "+day +", week : "+week);

        StringBuilder sb = new StringBuilder();
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month);
        sb.append("月");
        if(day <10){
            sb.append("0");
        }
        sb.append(day);
        sb.append("日");
        sb.append(" ");
        sb.append(weekArray[week]);
        mTimeMon.setText(sb.toString());
    }

    private void getHourAndMinutes(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Log.i(TAG,"FullscreenActivity : hour : "+hour +", minute : "+minute);
        StringBuffer stringBuffer = new StringBuffer();
        if(hour<10){
            stringBuffer.append("0");
        }
        stringBuffer.append(hour);
        stringBuffer.append(":");
        if(minute<10){
            stringBuffer.append("0");
        }
        stringBuffer.append(minute);
        mTimeSec.setText(stringBuffer.toString());
    }

    private void flashText(int index){
        switch (index){
            case 1:
                tv4.setTextColor(Color.WHITE);
                tv4.setTextSize(10);
                tv1.setTextColor(Color.YELLOW);
                tv1.setTextSize(12);
                break;
            case 2:
                tv1.setTextColor(Color.WHITE);
                tv1.setTextSize(10);
                tv2.setTextColor(Color.YELLOW);
                tv2.setTextSize(12);

                break;
            case 3:
                tv2.setTextColor(Color.WHITE);
                tv2.setTextSize(10);
                tv3.setTextColor(Color.YELLOW);
                tv3.setTextSize(12);
                break;
            case 4:
                tv3.setTextColor(Color.WHITE);
                tv3.setTextSize(10);
                tv4.setTextColor(Color.YELLOW);
                tv4.setTextSize(12);
                break;
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences("song",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", position);
        editor.apply();
        Log.i(TAG, "FULL Screen : onStop");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        unregisterReceiver(receiver);
        mHandler.removeMessages(ZERO);
        mHandler.removeMessages(ONE);
        Log.i(TAG, "FULL Screen : onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.screen_bt_next:
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
            case R.id.screen_bt_pres:
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

            case R.id.screen_bt_playorpause:
                try {
                    if (IMediaplay.isplay()) {
                        try {
                            IMediaplay.pause();
                            mImagePlayorPause.setImageResource(R.drawable.keyguard_button_play);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            IMediaplay.start();
                            mImagePlayorPause.setImageResource(R.drawable.keyguard_button_pause);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSildingBack() {
        SharedPreferences sp = getSharedPreferences("song",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("position", position);
        editor.apply();
        mHandler.removeMessages(ZERO);
        mHandler.removeMessages(ONE);
        finish();
    }

    @Override
    public void onSildingForward() {

        finish();
    }
}
