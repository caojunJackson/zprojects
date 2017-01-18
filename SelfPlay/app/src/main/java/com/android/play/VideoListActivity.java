package com.android.play;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.adapter.MediaPlayAdapter;
import com.android.entity.Constant;

public class VideoListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String TAG = Constant.TAG;
    private ListView mVideoListView;
    private String path;
    private TextView mEmptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        Log.i(TAG,"====== 按====actionbar="+actionBar);
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        }

        initView();
        initListen();

        mVideoListView.setAdapter(new MediaPlayAdapter(this));
        Log.i(TAG,"===Constant.mediaList.=="+Constant.mediaList.isEmpty());
        if(Constant.mediaList.isEmpty()){
            mEmptyText.setVisibility(View.VISIBLE);
        }else {
            mEmptyText.setVisibility(View.GONE);
        }

    }

    private void initListen() {

        mVideoListView.setOnItemClickListener(this);
    }

    private void initView() {
        mEmptyText = (TextView) findViewById(R.id.tv_empty);
        mVideoListView = (ListView) findViewById(R.id.lv_video);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                // 当Action Bar的图标被单击时执行下面的finish
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.i(TAG,"===video list===onItemClick="+position);
        path = Constant.mediaList.get(position).getPath();
        Intent videoIntent = new Intent();
        videoIntent.setClass(this, PlayVideoActivity.class);
        videoIntent.putExtra("position", position);
        startActivity(videoIntent);
    }


}
