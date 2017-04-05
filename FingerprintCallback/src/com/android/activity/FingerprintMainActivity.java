package com.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.SwitchPreference;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.aidl.IFingerprintManager;
import com.android.fingerprintcallbackdemo.R;
import com.android.service.FingerprintManager;
import com.android.service.KeyguardFingerprintService;

import java.util.ArrayList;
import java.util.List;
import com.android.aidl.*;
import ma.release.FingerprintEntity;

import java.io.File;
import java.io.IOException;

public class FingerprintMainActivity extends Activity {
    
    private String TAG = "fenghaitao";
    
    private List<Finger> mFingers ;
    
    private ListView fingerListView;
    private FingerAdapter mAdapter;
    private TextView addFingerTv;
    private ImageButton resetVolt;
    private Switch mLockSwitch;
    private static final int REQUEST_CODE_SET_KEYGUARD = 5;
    
    int[] dat = {0, 0, 0, 0, 0};
    
    private IFingerprintManager mFingerprintManager;
    
    private List<FingerprintEntity> mFingerprintEntityList = new ArrayList<FingerprintEntity>();
    
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
                try {
                 mFingerprintManager.setOnAuthenticateListen(new AuthenticateListen());
                 mFingerprintManager.authenticate();
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
                updateFingerprintData();
                mAdapter = new FingerAdapter();

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

        fingerListView = (ListView)findViewById(R.id.lv_finger);
        addFingerTv = (TextView)findViewById(R.id.add_finger_tv);
        mLockSwitch = (Switch) findViewById(R.id.fingerlock_switch);
        
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), FingerprintManager.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        boolean hasLock = (Util.readXML(getApplicationContext(), "lock", 0) == 1) ? true : false;
        mLockSwitch.setChecked(hasLock);
        
        mLockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "----------------------------Lock switch----isCheck-"+isChecked);
                onFingerUnlockClick(isChecked);
            }
        });
        
        addFingerTv.setOnClickListener(new View.OnClickListener() {

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
                 Log.i(TAG, "=====select listview position="+position+" === id = "+mFingerprintEntityList.get(position).getId());
                 showRenameDeleteDialog(mFingerprintEntityList.get(position));
            }
        });
        
       
        
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
    
    private void fingersFlush(){
        updateFingerprintData();
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
    
    /**
     * 开启或关闭“指纹解锁功能”，该功能是否开启，记录在sharedpreference的“lock”属性中。
     */
    private void onFingerUnlockClick(boolean isChecked) {
        boolean hasPassword = Util.hasPassword(this);

        if (!hasPassword) {
            Util.writeXML(getApplicationContext(), "lock", 0);
            Intent intent = new Intent();
            intent.setAction("android.app.action.SET_NEW_PASSWORD");
            intent.setPackage("com.android.settings");
            intent.putExtra("fp_show_description", true);
            startActivityForResult(intent, REQUEST_CODE_SET_KEYGUARD);
        } else {
            if(!isChecked) {
                Util.writeXML(getApplicationContext(), "lock", 0);
            } else {
                Util.writeXML(getApplicationContext(), "lock", 1);
            }
        }
    }

    /**
     * show dialog for rename or delete one fingerprint record
     * 
     * @param entity
     *            contains fingerprint record information
     */
    private void showRenameDeleteDialog(final FingerprintEntity fingerprintEntity) {
        AlertDialog.Builder itemsBuilder = new AlertDialog.Builder(this);
        itemsBuilder.setItems(
                new String[] { getResources().getString(R.string.ma_set_finger_dlg_rename),
                        getResources().getString(R.string.ma_set_finger_dlg_delete) },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {

                            showRenameDialog(fingerprintEntity);
                        } else {

                            deleteFingerprintRecord(fingerprintEntity);
                        }
                    }

                });

        itemsBuilder.create().show();
    }

    /**
     * delete one fingerprint record
     * 
     * @param entity
     *            contains fingerprint record information
     */
    private void deleteFingerprintRecord(FingerprintEntity fingerprintEntity) {
        int fid = fingerprintEntity.getId();
        int ret = 0;
        try{
        ret = mFingerprintManager.remove(fid);
        }catch(RemoteException e){
            
        }
        Log.d(TAG,"deleteFingerprintRecord, fid=" + fid + " ,ret=" + ret);
        if (ret == 0) {
            String name = Util.readXML(this, fid+"", "");
            Util.removeXML(this, fid + "");
            if(Util.containsXML(this, name)) {
                Util.writeXML(this, name, false);
            }
        }

        fingersFlush();
    }

    private void showRenameDialog(final FingerprintEntity fingerprintEntity) {
        final EditText nameEt = new EditText(this);
        nameEt.setSingleLine(true);
        nameEt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });

        AlertDialog.Builder renameBuilder = new AlertDialog.Builder(this);
        renameBuilder.setTitle(getResources().getString(R.string.ma_set_finger_dlg_rename));
        renameBuilder.setView(nameEt);
        renameBuilder.setNegativeButton(getResources().getString(R.string.ma_set_finger_dlg_cancel), null);
        renameBuilder.setPositiveButton(getResources().getString(R.string.ma_set_finger_dlg_confirm),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renameFingerprintRecord(fingerprintEntity, nameEt);
                    }

                });

        renameBuilder.create().show();
    }

    private void renameFingerprintRecord(FingerprintEntity entity, final EditText nameEt) {
        String newName = nameEt.getText().toString().trim();
        if (Util.isChinese(newName)) {
            if (newName.length() > Util.maxCLength) {
                newName = newName.substring(0, Util.maxCLength - 1);
            }
        } else {
            if (newName.length() > Util.maxELength) {
                newName = newName.substring(0, Util.maxELength - 1);
            }
        }
        
        if (TextUtils.isEmpty(newName)) {

        } else {
            String fid = entity.getId() + "";
            
            String oldName = Util.readXML(this, fid+"", "");
            if(Util.containsXML(this,  oldName)) {
                Util.writeXML(this, oldName, false);
            }

            Util.writeXML(getApplicationContext(), fid, newName);
            fingersFlush();
        }

    }

    /**
     * 更新指纹数据
     * 
     * @return 更新完后的最新数据
     */
    public List<FingerprintEntity> updateFingerprintData() {
        try {
            mFingerprintManager.enumerate(dat, 5);
        } catch (RemoteException e) {
        }
       
        List<FingerprintEntity> tmp = new ArrayList<FingerprintEntity>();
        for (int i = 0; i < 5; i++) {
            if ((dat[i] & 0xff) == 0) {
                continue;
            }

            tmp.add(generateFingerprintEntity(dat[i]));

        }
        mFingerprintEntityList = tmp;
        return mFingerprintEntityList;
    }


    private FingerprintEntity generateFingerprintEntity(int b) {
        FingerprintEntity entity = new FingerprintEntity();
        int id = b;
        entity.setId(id);
        entity.setImgRes(R.drawable.ic_ma_touch_id);

        String name = Util.readXML(this, id + "", "");
        if (TextUtils.isEmpty(name)) {
            String defaultName = getDefaultName();
            entity.setName(defaultName);
            Util.writeXML(this, id + "", defaultName);
            Util.writeXML(this, defaultName, true); // 名字被使用，标记true
        } else {
            entity.setName(name);
        }

        return entity;
    }

    private String getDefaultName() {
        String defaultName = "";
        for (int i = 1; i <= 5; i++) {
            String key = "指纹" + i;
            boolean used = Util.readXML(this, key, false); // 该名字是否已经被使用
            if (!used) {
                defaultName = key;
                break;
            }
        }
        return defaultName;
    }

    private boolean hasName(int id) {
        String value = Util.readXML(this, id + "", "");
        if (TextUtils.isEmpty(value)) {
            return false;
        } else {
            return true;
        }
    }

    
    public class FingerAdapter extends BaseAdapter {
        
        @Override
        public int getCount() {

            return mFingerprintEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return mFingerprintEntityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holde holde;
            
            if(convertView == null){
                holde = new Holde();
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.finger_item, null);
                holde.fingerItemView = (TextView)convertView.findViewById(R.id.finger_item_tv);
                convertView.setTag(holde);
            }else{
                holde = (Holde)convertView.getTag();
            }
            
            holde.fingerItemView.setText(mFingerprintEntityList.get(position).getName());
            
            return convertView;
        }

       class Holde{
          TextView fingerItemView;
       }
    }

   
}
