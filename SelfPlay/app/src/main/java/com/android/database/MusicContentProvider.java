/**  

* @Title: MusicContentProvider.java 

* @Package com.vanzo.datebase 

* @Description: TODO(用一句话描述该文件做什么) 

* @author A18ccms A18ccms_gmail_com  

* @date Nov 9, 2015 10:14:58 PM 

* @version V1.0  

*/ 
package com.android.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.android.entity.Constant;


public class MusicContentProvider extends ContentProvider {
		private String TAG = Constant.TAG;
	//定义一个URI的匹配器，用于匹配uri，如果路径不满足条件返回-1
		private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		private static final int INSERT = 1;
		private static final int DELETE = 2;
		private static final int UPDATE = 3;
		private static final int QUERY = 4;
		private static final int QUERYONE = 5;
		
		private MyHelper myHelper = null;

		//content://com.feng.database.firstprovider/insert 添加
		//content://com.feng.database.firstprovider/delete 删除
		//content://com.feng.database.firstprovider/update 更新
		//content://com.feng.database.firstprovider/query  查询
		static {
			matcher.addURI("com.vanzo.database.musicprovider", "insert", INSERT);
			matcher.addURI("com.vanzo.database.musicprovider", "delete", DELETE);
			matcher.addURI("com.vanzo.database.musicprovider", "update", UPDATE);
			matcher.addURI("com.vanzo.database.musicprovider", "query", QUERY);
			matcher.addURI("com.vanzo.database.musicprovider", "query/#", QUERYONE);//任意
		}

		@Override
		public boolean onCreate() {
			Log.i(TAG, " music contentprovider ...");
			myHelper = new MyHelper(getContext(),DBInfo.DB.DB_NAME,null,DBInfo.DB.DB_VERSION);
			return (myHelper == null)?false:true;
		}
		
		/*
		 * 添加数据
		 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
		 */
		@Override
		public Uri insert(Uri uri, ContentValues values) {
			// TODO Auto-generated method stub
			try {
				if(matcher.match(uri)==INSERT){
					Log.i(TAG, "insert");
					SQLiteDatabase db = myHelper.getWritableDatabase();
					db.insert(DBInfo.Table.TABLE_MUSIC_NAME, null, values);
					db.close();
				}else{
					throw new IllegalArgumentException("path error,do not insert data");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
		}
		
		/*
		 * 删除数据
		 * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
		 */
		@Override
		public int delete(Uri uri, String selection, String[] selectionStrings) {
			// TODO Auto-generated method stub
			int result = 0;
			try {
				if(matcher.match(uri)==DELETE){
					SQLiteDatabase db = myHelper.getWritableDatabase();
					result = db.delete(DBInfo.Table.TABLE_MUSIC_NAME, selection, selectionStrings);
					
				}else{
					throw new IllegalArgumentException("path error , do not delete data");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		
		/*
		 * 更新数据
		 * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
		 */
		@Override
		public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
			// TODO Auto-generated method stub
			int result = 0;
			try {
				if(matcher.match(uri)==UPDATE){
					SQLiteDatabase db = myHelper.getWritableDatabase();
					result = db.update(DBInfo.Table.TABLE_MUSIC_NAME, values, selection, selectionArgs);
					
				}else{
					throw new  IllegalArgumentException("path error , do not update data");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		/*
		 * 查询数据
		 * 
		 */
		@Override
		public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
				String sortOrder) {
			// TODO Auto-generated method stub
			Cursor cursor = null;
			try {
				if(matcher.match(uri)==QUERY){
					Log.i(TAG, "query");
					SQLiteDatabase db = myHelper.getReadableDatabase();
					cursor = db.query(DBInfo.Table.TABLE_MUSIC_NAME, projection, selection, selectionArgs, null, null, null);
					
					Log.i(TAG, "cursor"+cursor.getCount());
				}else if (matcher.match(uri)==QUERYONE){
					Log.i(TAG, "queryone");
					SQLiteDatabase db = myHelper.getReadableDatabase();
					long id = ContentUris.parseId(uri);
					cursor = db.query(DBInfo.Table.TABLE_MUSIC_NAME, projection, "_id=?", new String[]{id+""}, null, null, null);
				
				}else {
					throw new IllegalAccessException("path error , do not query data");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cursor;
		}

		@Override
		public String getType(Uri uri) {
			// TODO Auto-generated method stub
			if(matcher.match(uri) == QUERY){
				return "com.feng.database.firstprovider";
			}
			return null;
		}
}
