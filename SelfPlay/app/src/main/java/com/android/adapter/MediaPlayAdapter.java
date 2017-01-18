package com.android.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entity.Constant;
import com.android.entity.PlayMessage;
import com.android.play.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MediaPlayAdapter extends BaseAdapter {
	private Context context;
	boolean isChecked;
	//创建Cache 缓存
	private LruCache<String, Bitmap> mCaches;

	public MediaPlayAdapter(Context context){
		this.context = context;
		Log.i("fenghaitao"," mediaplay adapter new ");
		mCaches = Constant.mCaches;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Constant.mediaList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Hold hold;
		if(view == null){
			hold = new Hold();
			view = View.inflate(context, R.layout.playlist_item,null);
			hold.tv_name=(TextView) view.findViewById(R.id.tv_pname);
			hold.tv_dur = (TextView) view.findViewById(R.id.tv_pduration);
			hold.tv_size = (TextView) view.findViewById(R.id.tv_pszie);
			hold.imageView= (ImageView) view.findViewById(R.id.im_suo);
			hold.deleteButton = view.findViewById(R.id.delete_button);
			view.setTag(hold);
		}
		hold = (Hold) view.getTag();
	 	PlayMessage message = Constant.mediaList.get(position);
		String url = message.getPath();

		Bitmap bitmap = getBitmapFromCache(url);
		Log.i("fenghaitao"," mediaplay bitmap = " + bitmap);
		if(bitmap == null){
			bitmap = getVideoThumbnail(url,60,60,MediaStore.Images.Thumbnails.MICRO_KIND);
			if(bitmap != null){
				addBitmapToCache(url,bitmap);
				hold.imageView.setImageBitmap(bitmap);
			}
		}else {
			hold.imageView.setImageBitmap(bitmap);
		}
		//hold.imageView.setImageBitmap(getVideoThumbnail(message.getPath(), 60, 60, MediaStore.Images.Thumbnails.MICRO_KIND));
		hold.tv_name.setText(message.getName());
		hold.tv_dur.setText(Constant.movieTimeFormat(message.getDuration()));
		
		hold.tv_size.setText(Constant.mediaSize(message.getSize())+"Mb");
		hold.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(context, "delete item", Toast.LENGTH_SHORT).show();
				showDeleteDialog(context, position);

			}
		});
		return view;
	}
	class Hold{
		TextView tv_name;
		TextView tv_dur;
		TextView tv_size;
		ImageView imageView;
		View deleteButton;
	}

	//增加到缓存，增加的同时判断是否已经存在
	public void addBitmapToCache(String url, Bitmap bitmap){
		if (getBitmapFromCache(url) == null){
			mCaches.put(url,bitmap);
		}
	}


	//从缓存中获取数据
	public Bitmap getBitmapFromCache(String url){
		return mCaches.get(url);
	}

	private Bitmap getVideoThumbnail(String videoPath, int width, int height,
			int kind) {
			Bitmap bitmap = null;
			//获得系统缩略图
			bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
			
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
			ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			return bitmap;
		}

/*
	自定义Dialog注意加载顺序
 */
	private void showDeleteDialog(final Context contextdialog, final int position){
		String [] items = new String[]{"是否删除原文件"};


		AlertDialog.Builder builder = new AlertDialog.Builder(contextdialog);

		View view = View.inflate(contextdialog, R.layout.delete_dialog_item_video, null);
		Button dialogCancel = (Button) view.findViewById(R.id.dialog_cancel);
		Button dialogOk = (Button) view.findViewById(R.id.dialog_ok);

		builder.setView(view);  //1，先加载布局
		final AlertDialog dialog = builder.create(); //2，获得实例

		dialogCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (dialog != null)
					dialog.dismiss();
			}
		});

		dialogOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String name = Constant.mediaList.get(position).getName();
				String path = Constant.mediaList.get(position).getPath();


				Constant.mediaList.remove(position);
				Uri uri = Uri.parse("content://com.vanzo.database.videoprovider/delete");
				context.getContentResolver().delete(uri, "name = ?", new String[]{name});
				Log.i("fenghaitao", "===isChecked="+isChecked+", ===path="+path);
				if(isChecked){
					File file = new File(path);
					if(file.exists()){
						Log.i("fenghaitao", "===delete file="+path);
						file.delete();
					}
					Uri fileUri = Uri.fromFile(file);
					context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,fileUri));
				}
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});




		CheckBox mCheck = (CheckBox) view.findViewById(R.id.check_dialog_sum);
		if(Constant.mediaList.get(position).getPath().contains("DCIM")){
			mCheck.setVisibility(View.GONE);
		}
		mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				isChecked = b;
			}
		});


		dialog.show();
	}
}
