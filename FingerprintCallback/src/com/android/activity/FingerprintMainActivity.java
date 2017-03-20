package com.android.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.adapter.FingerAdapter;
import com.android.aidl.IFingerprintManager;
import com.android.fingerprintcallbackdemo.R;
import com.android.service.FingerprintManager;
import com.android.service.KeyguardFingerprintService;

import java.util.ArrayList;
import java.util.List;
import com.android.aidl.*;
import java.io.File;
import java.io.IOException;

public class FingerprintMainActivity extends Activity {
    
    private String TAG = "fenghaitao";
    
    private List<Finger> mFingers ;
    
    private ListView fingerListView;
    private FingerAdapter mAdapter;
    private ImageButton addFingerBt;
    
    int[] dat = {0, 0, 0, 0, 0};
    
    private IFingerprintManager mFingerprintManager;
    
    private ServiceConnection connection = new ServiceConnection() {
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mFingerprintManager = IFingerprintManager.Stub.asInterface(service);
            Log.i(TAG, "=====FingerprintManager on service Connected===");
            if(mFingerprintManager != null){
                Log.i(TAG, "======SERVICE CONN ENROLL=====");
                try {
                 mFingerprintManager.setOnAuthenticateListen(new AuthenticateListen());
                 mFingerprintManager.authenticate();
                 mFingerprintManager.enumerate(dat, 5);
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
                getIds();// 读取当前所有指纹
                for(Finger finger :mFingers){
                    Log.i(TAG, "=========mainActivity======"+finger.getName());
                }
                mAdapter = new FingerAdapter(getApplicationContext(), mFingers);

                fingerListView.setAdapter(mAdapter);
            }            
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.finger_main);
        
        creatFile();

        mFingers = new ArrayList<Finger>();
        fingerListView = (ListView)findViewById(R.id.lv_finger);
        addFingerBt = (ImageButton)findViewById(R.id.image_add_finger);
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), FingerprintManager.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
       
        addFingerBt.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), EnrollActivty.class);
                startActivityForResult(intent, 0);
            }
        });
        
       
        fingerListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 Log.i(TAG, "=====select listview position="+position+" === id = "+mFingers.get(position).getFingerID());
                try {
                    mFingerprintManager.remove(mFingers.get(position).getFingerID());
                    mAdapter.notifyDataSetChanged();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                fingersFlush();
            }
        });
        
        startKeyguardService();
    }
    
    
    private void startKeyguardService() {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), KeyguardFingerprintService.class);
        startService(intent);
    }

    public void creatFile(){
        File file = new File("/data/system/ma_fingerprint");
        if(!file.exists()){
            file.mkdirs();
        }
        Process p;
        int status;

        try {
            p=Runtime.getRuntime().exec("chmod 777 "+file);
            status = p.waitFor();
            if(status == 0){
                Log.d("fht"," 创建成功");
            }else{
                Log.d("fht","创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getIds() {
        int count = 0;
        int[] temp = new int[5];
        int[] fingerid = new int[5];

        mFingers.removeAll(mFingers);
        
        for (int i = 0; i < 5; i++) {
            Log.i(TAG, "===data=" + dat[i]);
            if (dat[i] > 0) {
                temp[count] = i + 1;
                fingerid[count] = dat[i];
                count++;
                Log.i(TAG, "===data===count=" + count);
            }
        }
        StringBuffer sb = new StringBuffer();
        int[] fingers = new int[count];
        for (int j = 0; j < count; j++) {
            fingers[j] = temp[j];
            Log.i(TAG, "===temp ===" + temp[j]);
            Finger finger = new Finger();
            finger.setId(temp[j]);
            finger.setName("指纹 "+temp[j] + "");
            finger.setFingerID(fingerid[j]);
            mFingers.add(finger);
        }

    }
    
    private void fingersFlush(){
        try {
            mFingerprintManager.enumerate(dat, 5);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        getIds();// 读取当前所有指纹
        mAdapter.notifyDataSetChanged();
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "=======onActivity Result=========");
        fingersFlush();
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if (mFingerprintManager != null) {
            try {
                mFingerprintManager.authenticate();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    class AuthenticateListen extends IAuthenticateCallback.Stub{

        @Override
        public void onNoMatch() throws RemoteException {
            
        }

        @Override
        public void onIdentified(int fid) throws RemoteException {
            
        }

        @Override
        public void onCaptureFailed(int reason) throws RemoteException {
            
        }

        @Override
        public void onFingerRemoved() throws RemoteException {
            
        }
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "=======finger main activity ===destroy=");
        try {
            mFingerprintManager.stopAuthenticate();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(connection);
    }
}
