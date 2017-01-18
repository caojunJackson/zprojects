package com.android.activity;

import android.R.integer;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.fingerprintcallbackdemo.R;
import com.android.service.FingerprintManager;

import com.android.aidl.*;

public class EnrollActivty extends Activity implements OnClickListener{
    private String TAG="fenghaitao";
    
    final public static int ENROLL_SUC = 1;
    final public static int ENROLL_PROGRESS = 2;
    final public static int ENROLL_CAPTURE_FAILD = 3;
    final public static int ENROLL_CAPTURE_SUC = 4;
    final public static int ENROLL_FINGER_REMOVE = 5;
    
    
    private HorizontalProgressBarWithNumber mBar;
    private ImageView enrollImage;
    private TextView enrollToastTextView;
    private Button addOthersFinger, fingerSuc;
    private IFingerprintManager mFingerprintManager;
    private int fid;
    
    private ServiceConnection connection = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
               mFingerprintManager = IFingerprintManager.Stub.asInterface(service);
               Log.i(TAG, "=====FingerprintManager on service Connected===");
               if(mFingerprintManager != null){
                   Log.i(TAG, "======SERVICE CONN ENROLL=====");
                   try {
                    mFingerprintManager.setOnEnrollListen(new MyEnrollListen());
                    mFingerprintManager.enroll();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
               }
        }
    };
    
    private  Handler mHandler = new Handler(){
        
        
        public void handleMessage(android.os.Message msg) {
            long currTime = SystemClock.currentThreadTimeMillis();
            long pressTime ;
            
            switch (msg.what) {
                case ENROLL_SUC:
                    try {
                        mFingerprintManager.stopAuthenticate();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    addOthersFinger.setVisibility(View.VISIBLE);
                    fingerSuc.setVisibility(View.VISIBLE);
                    
                    fid = msg.arg1;
                    enrollToastTextView.setText("指纹已录入完成");
                    Toast.makeText(getApplicationContext(), "指纹录入成功", Toast.LENGTH_SHORT).show();
                    break;
                case ENROLL_PROGRESS:
                    mBar.setProgress(msg.arg1);
                    showEnrollImage(msg.arg1);
                    break;
                    
                 case ENROLL_CAPTURE_FAILD:
                     enrollToastTextView.setText("指纹采图失败请重按手指");
                     break;
                     
                 case ENROLL_CAPTURE_SUC:
                     Log.i(TAG, "====采图成功====");
                     break;
                     
                 case ENROLL_FINGER_REMOVE:
                     Log.i(TAG, "=======手指离开======");
                     enrollToastTextView.setText("请继续按手指");
                     break;
                default:
                    break;
            }
        };
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_finger);
        initView();
        
        mBar.setMax(100);
        mBar.setProgress(0);

        Log.i(TAG, "=====Enroll Activity===");
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), FingerprintManager.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    
    private void initView(){
        mBar = (HorizontalProgressBarWithNumber) findViewById(R.id.progress_enroll);
        enrollImage = (ImageView) findViewById(R.id.enroll_image);
        enrollToastTextView = (TextView) findViewById(R.id.enroll_toast);
        addOthersFinger =(Button) findViewById(R.id.add_others_finger);
        fingerSuc =(Button) findViewById(R.id.suc_finger);
        
        
        addOthersFinger.setOnClickListener(this);
        fingerSuc.setOnClickListener(this);
        
        addOthersFinger.setVisibility(View.INVISIBLE);
        fingerSuc.setVisibility(View.INVISIBLE);
    }
    
    
    private void showEnrollImage(int progress){
        if(progress <1){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_1);
        }else if(progress <7){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_2);
        }else if(progress <14){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_3);
        }else if(progress <21){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_4);
        }else if(progress <28){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_5);
        }else if(progress <35){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_6);
        }else if(progress <42){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_7);
        }else if(progress <49){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_8);
        }else if(progress <56){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_9);
        }else if(progress <64){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_10);
        }else if(progress <72){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_11);
        }else if(progress <79){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_12);
        }else if(progress <86){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_14);
        }else if(progress <93){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_15);
        }else if(progress <=100){
            enrollImage.setImageResource(R.drawable.ic_fingerprint_setting_16);
        }
        
    }
    
    
    class MyEnrollListen extends IEnrollCallback.Stub{

        @Override
        public void onCaptureCompleted() throws RemoteException {
            sendMessage(ENROLL_CAPTURE_SUC, 0);
        }

        @Override
        public void onCaptureFailed(int code) throws RemoteException {
            sendMessage(ENROLL_CAPTURE_FAILD, 0);
        }

        @Override
        public void onProgress(int num) throws RemoteException {
            Log.i(TAG, "===progress="+num);
            sendMessage(ENROLL_PROGRESS, num);
        }

        @Override
        public void onEnrolled(int fid) throws RemoteException {
            Log.i(TAG, "=======on enroll ======");
            sendMessage(ENROLL_SUC, fid);
        }

        @Override
        public void onEnrollmentFailed() throws RemoteException {
            
        }

        @Override
        public void onFingerRemoved() throws RemoteException {
           sendMessage(ENROLL_FINGER_REMOVE, 0);
        }
        
        public void sendMessage(int type, int arg){
            Message message = new Message();
            message.what = type;
            message.arg1 = arg;
            mHandler.sendMessage(message);
        }
        
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_others_finger:
                try {
                    mFingerprintManager.enroll();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mBar.setProgress(0);
                break;
            case R.id.suc_finger:
                try {
                    mFingerprintManager.stopAuthenticate();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.putExtra("fid", fid);
                setResult(1100, intent);
                finish();
                break;
            default:
                break;
        }
        
    }
    
    @Override
    protected void onStop() {
        super.onStop();
       
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "===enroll activity destroy == ");
        
        unbindService(connection);
    }
   
    
}
