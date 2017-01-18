package com.android.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.entity.Constant;
import com.android.play.FullscreenActivity;
import com.android.service.UtilService;

public class MusicScreenOffShowReceiver extends BroadcastReceiver {
    private String TAG = Constant.TAG;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "==Music screen on  receiver ==");
        Intent restart = new Intent();
        ComponentName componentName = new ComponentName("com.android.play","com.android.play.FullscreenActivity");
        restart.setComponent(componentName);
      //  restart.setClass(context, FullscreenActivity.class);
        restart.putExtra("path",intent.getStringExtra("path"));
        restart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(restart);

    }
}
