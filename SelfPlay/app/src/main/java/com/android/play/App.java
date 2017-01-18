package com.android.play;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.android.entity.Constant;
import com.android.entity.CrashHandler;
import com.android.service.UtilService;
import com.umeng.socialize.PlatformConfig;

/**
 * Created by ntop on 15/7/8.
 */
public class App extends Application {
    private String TAG = Constant.TAG;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG," APP : onCreate ");
        //CrashHandler crashHandler = CrashHandler.getInstance();
        //crashHandler.init(getApplicationContext());
        Intent service = new Intent();
        service.setClass(getApplicationContext(), UtilService.class);
        startService(service);
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {

        PlatformConfig.setQQZone("1105333821", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setAlipay("2015111700822536");

    }
}
