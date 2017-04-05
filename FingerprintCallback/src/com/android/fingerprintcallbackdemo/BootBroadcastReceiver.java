package com.android.fingerprintcallbackdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
            Log.d("fenghaitao", "----------boot completed fingerprint start------");
            Intent bootIntent = new Intent();
            bootIntent.setClassName("com.android.fingerprintcallbackdemo", "com.android.service.FingerprintManager");
            context.startService(bootIntent);
        }
    }

}
