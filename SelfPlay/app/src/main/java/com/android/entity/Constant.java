package com.android.entity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fht on 16-5-9.
 */
public class Constant {
    public static List<MusicMessage> musicList = new ArrayList<MusicMessage>();//

    public static List<PlayMessage> mediaList = new ArrayList<PlayMessage>();//

    public static List<PlayList> playList = new ArrayList<PlayList>();
    //创建Cache 缓存
    private static int maxMemory = (int) Runtime.getRuntime().maxMemory();
    private static int cacheSize = maxMemory/4;
    /*匿名内部类*/
    public static LruCache<String, Bitmap> mCaches = new LruCache<String, Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            //在每次存入缓存的时候调用
            return value.getByteCount();
        }
    };

    //系统音乐查询接口
    public static Uri uri_music = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    //歌名
    public static String mName = MediaStore.Audio.Media.TITLE;
    //路径
    public static String path = MediaStore.Audio.Media.DATA;
    //歌唱者
    public static String artist = MediaStore.Audio.Media.ARTIST;
    //时长
    public static String  duration = MediaStore.Audio.Media.DURATION;
    //歌曲大小
    public static String size = MediaStore.Audio.Media.SIZE;


    //系统视频查询接口
    public static Uri uri_play = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    //视频名
    public static String pName = MediaStore.Video.Media.TITLE;
    //路径
    public static String path_play = MediaStore.Video.Media.DATA;
    //时长
    public static String duration_play = MediaStore.Video.Media.DURATION;
    //视频大小
    public static String size_play = MediaStore.Video.Media.SIZE;


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


    //广播传输
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    //服务ACTION
    public static final String MEDIA_SERVICE = "com.iotek.action.Music";

    //TAG
    public static String TAG = "fenghaitaos";
    /**
     * 时间格式化
     * @param mm--传入总时间数
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String timeFormat(long mm){
        Date date = new Date(mm);
        SimpleDateFormat formater = new SimpleDateFormat("mm:ss");
        return formater.format(date);
    }
    /**
     * 时间格式化
     * @param mm--传入总时间数
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String movieTimeFormat(long mm){
        mm /=1000;
        long min = mm/60;
        long second = mm%60;
        long hour = min/60;
        min %=60;
        return String.format("%02d:%02d:%02d", hour,min,second);
    }

    public static String mediaSize(long size){
        double p_size = size/1024.0/1024.0;
        return String.format("%.2f",p_size);

    }

    //圆形图片
    public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
        if (bitmap == null) {
            return null;
        }
        int round;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            round = bitmap.getHeight();
        } else {
            round = bitmap.getWidth();
        }
        Log.i("fenghaitao", "===========round=="+round);
        Bitmap output = Bitmap.createBitmap(round,
                round, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, round, round);
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, round / ratio,
                round / ratio, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }
}
