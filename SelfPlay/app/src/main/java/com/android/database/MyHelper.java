package com.android.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHelper extends SQLiteOpenHelper {


	public MyHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		Log.i("fenghaitao", "create db ...");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DBInfo.Table.CREAT_PLAYLIST_TABLE);
		db.execSQL(DBInfo.Table.CREAT_MUSIC_TABLE);
		db.execSQL(DBInfo.Table.CREAT_VIDEO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL(DBInfo.Table.DROP_TABLE);
		onCreate(db);
	}

}
