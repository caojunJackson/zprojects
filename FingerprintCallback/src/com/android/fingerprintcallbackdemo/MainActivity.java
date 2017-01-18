
package com.android.fingerprintcallbackdemo;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.policy.IKeyguardService;

import ma.release.Fingerprint;
import ma.release.Jnifp;

public class MainActivity extends Activity {
    String TAG = "fenghaitao";
    Button mButton;
    Button mMatch;
    Button mRemove;
    EditText mEditText;
    ProgressBar mBar;

    String path;
    Fingerprint mFingerprint;
    byte[] dat = {
            0, 0, 0, 0, 0
    };
    TextView tv;

    PowerManager pm;
    IKeyguardService mService = null;
    private ServiceConnection mConnection = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
           mService = null;
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            
            mService = IKeyguardService.Stub.asInterface(service);
            Log.i("fenghaitao", "======onServiceConected==="+mService);
        }
    };
    
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Log.i("fenghaitao", "====handleMessage=====");

            switch (msg.what) {
                case 1:
                    Jnifp.enumerate(dat, 5);
                    StringBuffer sb = getIds();
                    tv.setText(sb);
                    mBar.setProgress(0);
                    break;
                case 2:
                    mBar.setProgress(msg.arg1);
                    break;
                case 3:
                   /* try {
                        mService.keyguardDone(true, true);
                    } catch (RemoteException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }*/
                    Toast.makeText(getApplicationContext(), "匹配成功:指纹 " + msg.arg1,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Jnifp.stopAuthenticate();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "匹配失败！！", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFingerprint = Fingerprint.getInstance();

        mButton = (Button) findViewById(R.id.enroll);
        mMatch = (Button) findViewById(R.id.match);
        mRemove = (Button) findViewById(R.id.remove);
        mEditText = (EditText) findViewById(R.id.et_remove);
        mBar = (ProgressBar) findViewById(R.id.progressbar);

        path = getApplicationContext().getFilesDir().toString();
        tv = (TextView) findViewById(R.id.textView1);

        mBar.setMax(100);

        new Jnifp().setNotify();
        Jnifp.open(path);
        Log.i(TAG, "===========Activity====path=" + path);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i(TAG, "=============Activity enroll click====");
                /*
                 * Jnifp.stopAuthenticate(); Jnifp.setKeyMode(1);
                 */
                Jnifp.enroll();
                // Jnifp.stopAuthenticate();
            }
        });

        mMatch.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Jnifp.setDetectMode(1);
                Log.i(TAG, "=============Activity match click====");
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Jnifp.authenticate();
                    }
                }).start();

            }
        });

        mRemove.setOnClickListener(new OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i(TAG, "=============Activity remove click====");
                Log.i(TAG, "======Activity=====remove start======");
                if(mEditText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "请先输入ID，再按remove", Toast.LENGTH_SHORT).show();
                    return;
                }
                int finger = Integer.valueOf(mEditText.getText().toString());
                Jnifp.remove(finger);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Jnifp.enumerate(dat, 5);
                StringBuffer sb = getIds();

                tv.setText(sb);
                mEditText.setText("");
                mEditText.setHint("请输入指纹ID，然后按remove键");
                Log.i(TAG, "======Activity=====remove end======");

            }
        });

        mFingerprint.setOnEnrollListen(mEnrollCallback);
        mFingerprint.setOnAuthenticateListen(mAuthenticateCallback);

        Jnifp.enumerate(dat, 5);
        StringBuffer sb = getIds();

        tv.setText(sb);
        mybindService();
        Intent intent = new Intent();
        intent.setClassName("com.android.fingerprintcallbackdemo", 
                "com.android.fingerprintcallbackdemo.FingerprintService");
        startService(intent);
    }

    public void mybindService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.systemui", "com.android.systemui.keyguard.KeyguardService"));
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
    
    private StringBuffer getIds() {
        int count = 0;
        int[] temp = new int[5];

        for (int i = 0; i < 5; i++) {
            Log.i(TAG, "===data=" + dat[i]);
            if (dat[i] > 0) {
                temp[count] = i + 1;
                count++;
                Log.i(TAG, "===data===count=" + count);
            }
        }

        StringBuffer sb = new StringBuffer();
        int[] fingers = new int[count];
        for (int j = 0; j < count; j++) {
            fingers[j] = temp[j];
            Log.i(TAG, "===temp ===" + temp[j]);
            sb.append("指纹 ");
            sb.append(temp[j] + "");
            sb.append(" ");
        }
        return sb;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Jnifp.stopAuthenticate();
        finish();
    }

    Fingerprint.IAuthenticateCallback mAuthenticateCallback = new Fingerprint.IAuthenticateCallback() {
        public void onCaptureFailed(int reason) {
            Log.i(TAG, "======Activity===onCaptureFailed=======");

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = 4;
            mHandler.sendMessage(message);

        };

        public void onFingerRemoved() {
            Log.i(TAG, "====Activity====onFingerRemoved== Authenticate=======");
        };

        public void onIdentified(int fid) {
            Log.i(TAG, "=====Activity====onIdentified======" + fid);
            // mHandler.sendEmptyMessage(0);
            Message message = new Message();
            message.arg1 = fid;
            message.what = 3;
            mHandler.sendMessage(message);
        };

        public void onNoMatch() {
            Log.i(TAG, "====Activity===onNoMatch========");
            Message message = new Message();
            message.what = 5;
            mHandler.sendMessage(message);
        };

    };

    Fingerprint.IEnrollCallback mEnrollCallback = new Fingerprint.IEnrollCallback() {
        public void onCaptureCompleted() {
            Log.i(TAG, "=====Activity====onCaptureCompleted=========");
        };

        public void onCaptureFailed(int code) {

            switch (code) {
                case Fingerprint.FP_CHK_PART:
                    Log.i(TAG, "=====Activity======onCaptureFailed===触摸面积小==");
                    break;
                case Fingerprint.FP_DUPLI_AREA:
                    Log.i(TAG, "=====Activity======onCaptureFailed===重复区域==");
                    break;
                case Fingerprint.FP_DUPLI_FINGER:
                    Log.i(TAG, "=====Activity======onCaptureFailed====重复手指=");
                    break;
                default:
                    break;
            }
        };

        public void onEnrolled(int fid) {
            Log.i(TAG, "======Activity======onEnrolled===fid=" + fid);
            Message message = new Message();
            message.arg1 = fid;
            message.what = 1;
            mHandler.sendMessage(message);

        };

        public void onEnrollmentFailed() {
            Log.i(TAG, "======Activity======onEnrollmentFailed===");
        };

        public void onFingerRemoved() {
            Log.i(TAG, "======Activity======onFingerRemoved== Enroll=");
        };

        public void onProgress(int num) {
            Log.i(TAG, "======Activity========onProgress==num=" + num);
            Message message = new Message();
            message.arg1 = num;
            message.what = 2;
            mHandler.sendMessage(message);

        };
    };

}
