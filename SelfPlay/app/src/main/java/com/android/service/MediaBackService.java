package com.android.service;

import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;

import com.android.entity.Constant;


public class MediaBackService extends Service {
	private String TAG = Constant.TAG;
	public MediaPlayer mediaPlayer;
	boolean isStop = true;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onbind");

		mediaPlayer = new MediaPlayer();
		return new MyBinder();
	}
	 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		Log.i(TAG,"oncreate");

	}
	
	public class MyBinder extends Binder{
		public  MediaBackService getResult(){
			return MediaBackService.this;
		}
	}


	
	
	/**
	 *
	 * @author Administrator
	 *
	 */
	public void activityNotify(){
		Intent server = new Intent("mediaplay");
		server.putExtra("ok", Constant.PLAY);
		sendBroadcast(server);
		Log.i(TAG, "onstartCommand");
	}
	
	/**
	 * 预加载
	 */
	public void preparePlay(SurfaceHolder holder,String path){
		//
		//String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/weileyujianni.3gp";
		
		try {
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDisplay(holder);
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
		public boolean isPlay(){
			if (mediaPlayer == null){
				return false;
			}
			return mediaPlayer.isPlaying();
		}
		
		//缓存
		public void prepare(){
			try {
				mediaPlayer.prepareAsync();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//暂停
		public void pause(){
			mediaPlayer.pause();
		}
		//播放
		public void play(){
			if(mediaPlayer.isPlaying()){
				mediaPlayer.stop();
			}else{
				mediaPlayer.start();
			}
			
		} 
		
		//ͣ停止
		public void stop(){
			mediaPlayer.stop();
			mediaPlayer.release();

		}
		//获得资源长度
		public int getDuration(){
			return mediaPlayer.getDuration();
		}
		//跳转到指定位置
		public void seekTo(int position){
			mediaPlayer.seekTo(position);
		}
		//获得当前播放位置
		public int getCurrentPosition(){
			return mediaPlayer.getCurrentPosition();
		}
		
		/**
		 * 循环
		 */
		public void looper(){
			
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					play();
				}
			});
			
		}

	@Override
	public boolean onUnbind(Intent intent) {
		mediaPlayer = null;
		return super.onUnbind(intent);
	}
}
