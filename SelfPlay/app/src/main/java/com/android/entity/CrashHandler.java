package com.android.entity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.RequiresPermission;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/3.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static CrashHandler INSTANCE = new CrashHandler();

    private Context mContext;

    private Map<String, String> infos = new HashMap<String, String>();

    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private CrashHandler(){
    }

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    public void init(Context context){
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }



    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if(!hadleException(ex) && mDefaultHandler != null){
            mDefaultHandler.uncaughtException(thread,ex);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean hadleException(Throwable ex) {
        if(ex == null){
            return false;
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                Toast.makeText(mContext,"error",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(mContext);
        savaCrashInfo2File(ex);
        return true;
    }

    private String savaCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key+" = "+value+"\n");
        }
        StringWriter write = new StringWriter();
        PrintWriter printWrite = new PrintWriter(write);
        ex.printStackTrace(printWrite);
        Throwable cause = ex.getCause();
        while (cause != null){
            cause.printStackTrace(printWrite);
            cause = cause.getCause();
        }
        printWrite.close();
        String result = write.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = format.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        }catch (Exception e){

        }
        return null;
    }

    private void collectDeviceInfo(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(info != null){
            String versionName  = info.versionName == null ? "null": info.versionName;
            String versionCode = info.versionCode + "";
            infos.put("versionName", versionName);
            infos.put("versionCode", versionCode);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field: fields){
            field.setAccessible(true);
            try {
                infos.put(field.getName(),field.get(null).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }


    
    
    
    
    
    
    
    
    
    
    
    
}
