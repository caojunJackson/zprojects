package com.android.play;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.entity.Constant;
import com.android.entity.MusicMessage;
import com.android.entity.PlayMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private String TAG = Constant.TAG;
    private int REQUEST_CODE = 1000;
    private TextView mName;
    private TextView mEmail;
    private ImageView mHeadImage;
    private View mHeadLayout;
    private NavigationView navigationView;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<android.support.v4.app.Fragment> mFragments;

    private LinearLayout mTabHome;
    private LinearLayout mTabHot;
    private LinearLayout mTabFind;

    private ImageButton mImageHome;
    private ImageButton mImageHot;
    private ImageButton mImageFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    //getVideoListFiles(new File("/sdcard"));


                }
            });
        }
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
        initListener();

        setSelect(1);

        if(mHeadLayout != null) {
            mHeadLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent authIntent = new Intent();
                    authIntent.setClass(getApplicationContext(), AuthQQActivity.class);
                    startActivityForResult(authIntent, REQUEST_CODE);

                }
            });
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                Log.i(TAG, "====load resources about  video music ==");
                clearAll();
                getMusicResoureFromDB();
                getVideoResoureFromDB();
            }
        }.start();




    }


    private void initView(){
        mHeadLayout = navigationView.getHeaderView(0);
        mName = (TextView) mHeadLayout.findViewById(R.id.tv_main_name);
        mEmail = (TextView) mHeadLayout.findViewById(R.id.tv_main_email);
        mHeadImage = (ImageView) mHeadLayout.findViewById(R.id.im_main_head);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_home);

        mTabFind = (LinearLayout) findViewById(R.id.tab_find_layout);
        mTabHome = (LinearLayout) findViewById(R.id.tab_home_layout);
        mTabHot = (LinearLayout) findViewById(R.id.tab_hot_layout);

        mImageFind = (ImageButton) findViewById(R.id.imb_tab_find);
        mImageHome = (ImageButton) findViewById(R.id.imb_tab_home);
        mImageHot = (ImageButton) findViewById(R.id.imb_tab_hot);

        mFragments = new ArrayList<Fragment>();
        Fragment mTab1 = new HotPagerFragment();
        Fragment mTab2 = new HomePagerFragment();
        Fragment mTab3 = new FindPagerFragment();

        mFragments.add(mTab1);
        mFragments.add(mTab2);
        mFragments.add(mTab3);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        mViewPager.setAdapter(mAdapter);
    }

    private void initListener(){
        mTabHot.setOnClickListener(this);
        mTabHome.setOnClickListener(this);
        mTabFind.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                resetImages();
                switch (currentItem){
                    case 0:
                        mImageHot.setImageResource(R.drawable.tab_hot_select);
                        break;
                    case 1:
                        mImageHome.setImageResource(R.drawable.tab_home_selected);
                        break;
                    case 2:
                        mImageFind.setImageResource(R.drawable.tab_find_selected);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Log.i(TAG,"Main Activity : 更新 资源");
                    Looper.prepare();
                    deleteAllResource();
                    clearAll();
                    getMusicResoureFromDB();
                    getVideoResoureFromDB();
                    Looper.loop();
                }
            }.start();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent();
            intent.putExtra("isFirst", true);
            intent.setClass(this,MusicListActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent  video = new Intent();
            video.setClass(this, VideoListActivity.class);
            video.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(video);

        } else if (id == R.id.nav_manage) {
            Intent settings = new Intent();
            settings.setClass(this, SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //////////////////////////////////////fenghaitao add codes////////////////////////



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"onActivityResult  data : "+ data.toString());
        if(requestCode == REQUEST_CODE){

            String name = data.getStringExtra("name");
            String imageUrl = data.getStringExtra("image");
            Log.i(TAG,"onActivityResult  name : "+ name +" , image url : "+imageUrl );
            mName.setText(name);
            Picasso.with(getApplicationContext()).load(imageUrl).into(target);

        }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmap = Constant.toRoundCorner(bitmap,2);
            mHeadImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    public void clearAll() {
        Constant.musicList.clear();
        Constant.mediaList.clear();
        Constant.playList.clear();
    }

    public void getMusicResoureFromDB() {
        Uri uri = Uri.parse("content://com.vanzo.database.musicprovider/query");
        String[] columnnew = new String []{
            Constant.TABLE_COLUMN_NAME,
                    Constant.TABLE_COLUMN_ARTIST,
                    Constant.TABLE_COLUMN_PATH,
                    Constant.TABLE_COLUMN_DURATION,
                    Constant.TABLE_COLUMN_SIZE};
        Cursor mMusicCursor = getContentResolver().query(uri, columnnew, null, null, null);
        Log.i(TAG,"=======do getMusic==mMusicCursor="+mMusicCursor);

        if (mMusicCursor != null && mMusicCursor.getCount() == 0 ) {
            Log.i(TAG, "==getMusicResource==");
            getAllMusic();
        }
        if (mMusicCursor != null && mMusicCursor.getCount() > 0) {
            for (mMusicCursor.moveToFirst(); !mMusicCursor.isAfterLast(); mMusicCursor.moveToNext()) {
                Log.i(TAG, "===add music message ====name=" + mMusicCursor.getString(0) + " ,====path=" + mMusicCursor.getString(2));
                MusicMessage musicMessage = new MusicMessage();

                musicMessage.setmName(mMusicCursor.getString(0));
                musicMessage.setArtist(mMusicCursor.getString(1));
                musicMessage.setPath(mMusicCursor.getString(2));
                musicMessage.setDuration(mMusicCursor.getInt(3));
                musicMessage.setSize(mMusicCursor.getLong(4));

                Constant.musicList.add(musicMessage);
            }
        }
        if(mMusicCursor !=null)
            mMusicCursor.close();
    }

    public void getVideoResoureFromDB() {
        Uri videoUri = Uri.parse("content://com.vanzo.database.videoprovider/query");
        String[] videoCloumn = new String[]{Constant.TABLE_COLUMN_NAME,
                Constant.TABLE_COLUMN_PATH,
                Constant.TABLE_COLUMN_DURATION,
                Constant.TABLE_COLUMN_SIZE};
        Cursor mVieoCursor = getContentResolver().query(videoUri, videoCloumn, null, null, null);
        Log.i(TAG, "=========do get videos==mVieoCursor="+mVieoCursor);

        if (mVieoCursor != null && mVieoCursor.getCount() == 0 ) {
            Log.i(TAG, "====getVideoResource===");
            getAllVideoResources();
        }
        if (mVieoCursor != null && mVieoCursor.getCount() > 0) {
            for (mVieoCursor.moveToFirst(); !mVieoCursor.isAfterLast(); mVieoCursor.moveToNext()) {
                Log.i(TAG, "======add video message==name=" + mVieoCursor.getString(0) + " ,===path=" + mVieoCursor.getString(1));

                PlayMessage videoMessage = new PlayMessage();
                videoMessage.setName(mVieoCursor.getString(0));
                videoMessage.setPath(mVieoCursor.getString(1));
                videoMessage.setDuration(mVieoCursor.getInt(2));
                videoMessage.setSize(mVieoCursor.getLong(3));

                Constant.mediaList.add(videoMessage);
            }
        }
        if(mVieoCursor!=null)
            mVieoCursor.close();
    }

    public void getAllMusic() {

        Cursor cursor = getContentResolver().query(Constant.uri_music,
                new String[]{Constant.mName, Constant.artist, Constant.path, Constant.duration, Constant.size}, null, null, null);

         if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                //小于30秒 不插入音乐列表
                if(Integer.valueOf(cursor.getString(3)) < 30*1000){
                    continue;
                }
                Log.i(TAG, "========getALLMusic ===name==" + cursor.getString(0));
                ContentValues values = new ContentValues();
                values.put(Constant.TABLE_COLUMN_NAME, cursor.getString(0));
                values.put(Constant.TABLE_COLUMN_ARTIST, cursor.getString(1));
                values.put(Constant.TABLE_COLUMN_PATH, cursor.getString(2));
                values.put(Constant.TABLE_COLUMN_DURATION, cursor.getInt(3));
                values.put(Constant.TABLE_COLUMN_SIZE, cursor.getLong(4));

                Uri uri = Uri.parse("content://com.vanzo.database.musicprovider/insert");
                Log.i(TAG,"=======music provider class===");
                getContentResolver().insert(uri, values);

                //add music list array
                MusicMessage musicMessage = new MusicMessage();
                musicMessage.setmName(cursor.getString(0));
                musicMessage.setArtist(cursor.getString(1));
                musicMessage.setPath(cursor.getString(2));
                musicMessage.setDuration(cursor.getInt(3));
                musicMessage.setSize(cursor.getLong(4));
                Constant.musicList.add(musicMessage);
            }
        }
        if(cursor!=null)
            cursor.close();

    }

    public void getAllVideoResources() {

        Cursor cursor = getContentResolver().query(Constant.uri_play,
                new String[]{Constant.pName, Constant.path_play, Constant.duration_play, Constant.size_play}, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                String path = cursor.getString(1);
                Log.i(TAG, "==getAllVideoRes===name=" + path);
                if(path.substring(path.length()-".mp4".length()).equals(".mp4")
                        || path.substring(path.length()-".3gp".length()).equals(".3gp")
                        || path.substring(path.length()-".mkv".length()).equals(".mkv")
                        || path.substring(path.length()-".avi".length()).equals(".avi")){

                        ContentValues values = new ContentValues();
                        values.put(Constant.TABLE_COLUMN_NAME, cursor.getString(0));
                        values.put(Constant.TABLE_COLUMN_PATH, cursor.getString(1));
                        values.put(Constant.TABLE_COLUMN_DURATION, cursor.getInt(2));
                        values.put(Constant.TABLE_COLUMN_SIZE, cursor.getLong(3));
                        Uri uri = Uri.parse("content://com.vanzo.database.videoprovider/insert");
                        Log.i(TAG,"=======video provider  class ==");
                        getContentResolver().insert(uri, values);

                        //add video to list array
                        PlayMessage videoMessage = new PlayMessage();
                        videoMessage.setName(cursor.getString(0));
                        videoMessage.setPath(cursor.getString(1));
                        videoMessage.setDuration(cursor.getInt(2));
                        videoMessage.setSize(cursor.getLong(3));
                        Constant.mediaList.add(videoMessage);
                }
            }
        }
        if(cursor!=null)
            cursor.close();

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void getVideoListFiles(File file){
        File [] files = file.listFiles();
        if(files == null){
            return ;
        }
        for (File mFile : files){
            if(mFile.isDirectory()){
                getVideoListFiles(mFile);
            }else if(mFile.isFile()) {
                if(mFile.getPath().substring(mFile.getPath().length()-".mp4".length()).equals(".mp4")
                        || mFile.getPath().substring(mFile.getPath().length()-".avi".length()).equals(".avi")){
                    Log.i(TAG, " MainActivity : getVideoListFiles , " + mFile.getAbsolutePath());
                    Log.i(TAG, " MainActivity : getVideoListFiles , " + mFile.getPath());
                    Log.i(TAG, " MainActivity : getVideoListFiles , " + mFile.getName());
                    Log.i(TAG, " MainActivity : getVideoListFiles , " + mFile.length());
                    Log.i(TAG, " MainActivity : getVideoListFiles , " + mFile.getFreeSpace());
                    Log.i(TAG, " MainActivity : getVideoListFiles , " + mFile.toURI());
                }
            }
        }
    }

    private void deleteAllResource(){
        Uri videoUri = Uri.parse("content://com.vanzo.database.videoprovider/delete");
        int result  = getContentResolver().delete(videoUri, null, null);
        Log.i(TAG, "MainActivity; deleteAllResource : result = "+result);
        if(result != -1){
            Toast.makeText(getApplicationContext(), "视频删除成功", Toast.LENGTH_SHORT).show();
        }

        Uri uri = Uri.parse("content://com.vanzo.database.musicprovider/delete");
        int resultMusic = getContentResolver().delete(uri, null, null);

        if(resultMusic != -1){
            Toast.makeText(getApplicationContext(), "音乐删除成功" , Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_hot_layout:
                setSelect(0);
                break;
            case R.id.tab_home_layout:
                setSelect(1);
                break;
            case R.id.tab_find_layout:
                setSelect(2);
                break;
        }
    }

    private void resetImages() {
        mImageFind.setImageResource(R.drawable.tab_find_normal);
        mImageHot.setImageResource(R.drawable.tab_hot_normal);
        mImageHome.setImageResource(R.drawable.tab_home_normal);
    }

    private void setSelect(int i){
        resetImages();
        switch (i){
            case 0:
                mImageHot.setImageResource(R.drawable.tab_hot_select);
                break;
            case 1:
                mImageHome.setImageResource(R.drawable.tab_home_selected);
                break;
            case 2:
                mImageFind.setImageResource(R.drawable.tab_find_selected);
                break;

        }
        mViewPager.setCurrentItem(i);
    }
}
