package com.android.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.provider.MediaStore;

import com.android.entity.MusicMessage;
import com.android.entity.PlayList;
import com.android.entity.PlayMessage;


public class DBInfo {

	public static class DB {
		public static final String DB_NAME = "all.db";
		public static final int DB_VERSION = 1;
	}

	public static class Table {
		//数据库
		public static String  TABLE_NAME ="playlist";
		public static String  TABLE_MUSIC_NAME ="music";
		public static String  TABLE_MEDIA_NAME ="media";
		
		public static String  TABLE_COLUMN_ID="_id";
		public static String  TABLE_COLUMN_NAME = "name";
		public static String  TABLE_COLUMN_PATH="path";
		public static String  TABLE_COLUMN_DURATION = "duration";
		public static String  TABLE_COLUMN_SIZE = "size";
		public static String  TABLE_COLUMN_ARTIST="artist";	
		public static String  TABLE_COLUMN_CURRENT ="current";
		
		
		public static final String DROP_TABLE = "DROP TABLE"+TABLE_NAME;
		
		public static final String CREAT_PLAYLIST_TABLE = "create table "+
				TABLE_NAME+"("+
				TABLE_COLUMN_ID+" integer primary key autoincrement,"+
				TABLE_COLUMN_NAME+" String not null,"+
				TABLE_COLUMN_PATH+" String not null,"+
				TABLE_COLUMN_CURRENT+" String,"+
				TABLE_COLUMN_DURATION+" integer not null,"+
				TABLE_COLUMN_SIZE+" integer not null);";
		
		public static final String CREAT_MUSIC_TABLE = "create table "+
				TABLE_MUSIC_NAME+"("+
				TABLE_COLUMN_ID+" integer primary key autoincrement,"+
				TABLE_COLUMN_NAME+" String not null,"+
				TABLE_COLUMN_PATH+" String not null,"+
				TABLE_COLUMN_CURRENT+" String,"+
				TABLE_COLUMN_ARTIST+" String,"+
				TABLE_COLUMN_DURATION+" integer not null,"+
				TABLE_COLUMN_SIZE+" integer not null);";
		
		public static final String CREAT_VIDEO_TABLE = "create table "+
				TABLE_MEDIA_NAME+"("+
				TABLE_COLUMN_ID+" integer primary key autoincrement,"+
				TABLE_COLUMN_NAME+" String not null,"+
				TABLE_COLUMN_PATH+" String not null,"+
				TABLE_COLUMN_CURRENT+" String,"+
				TABLE_COLUMN_DURATION+" integer not null,"+
				TABLE_COLUMN_SIZE+" integer not null);";
	
	}

	
}
