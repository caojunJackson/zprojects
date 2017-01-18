package com.android.play;
/**
 * Created by wangfei on 15/9/14.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;

import java.util.Map;

public class AuthQQActivity extends Activity{

        private UMShareAPI mShareAPI = null;




    public void onDeletAuth() {
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;

        /**begin invoke umeng api**/
        mShareAPI.deleteOauth(AuthQQActivity.this, platform, umdelAuthListener);
    }

    private  void AuthQQ(){
        SHARE_MEDIA platform = SHARE_MEDIA.QQ;
        mShareAPI.doOauthVerify(AuthQQActivity.this, platform, umAuthListener);

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_auth);
        /** init auth api**/
        mShareAPI = UMShareAPI.get( this );
        AuthQQ();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /** auth callback interface**/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();

            mShareAPI.getPlatformInfo(AuthQQActivity.this, platform, umAuthListenerUserInfo);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    private void doCompete(Map<String, String> data){
        String name = data.get("screen_name");
        String imageUrl = data.get("profile_image_url");
        android.util.Log.i("fenghaitao" ," onComplete   name : "+name +" ,  imageUrl : "+imageUrl);
        Intent intent = getIntent();
        intent.putExtra("name", name);
        intent.putExtra("image", imageUrl);
        setResult(1001,intent);
        finish();
    }

    private UMAuthListener umAuthListenerUserInfo = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            if (data!=null){
                Log.d("auth callbacl","getting data");
               // Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
               doCompete(data);

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "get fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "get cancel", Toast.LENGTH_SHORT).show();
        }
    };
    /** delauth callback interface**/
    private UMAuthListener umdelAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "delete Authorize succeed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "delete Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "delete Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("auth", "on activity re 2");
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        Log.d("auth", "on activity re 3");
    }
}
