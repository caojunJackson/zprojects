package com.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.entity.ImageLoader;
import com.android.entity.NewsBean;
import com.android.play.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7.
 */
public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    private ImageLoader mIamgeLoader;
    private int mStart, mEnd;
    public static  String[] URLS;
    private boolean mFirstIn;

    public NewsAdapter(Context context, List<NewsBean> data, ListView listView){
        mList = data;
        mInflater = LayoutInflater.from(context);
        mIamgeLoader= new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i = 0; i<data.size();i++){
            URLS[i] = data.get(i).newsIconUrl;
        }
        mFirstIn = true;
        //注册对应得事件
        listView.setOnScrollListener(this);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.internal_item_layout,null);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);
        String url = mList.get(position).newsIconUrl;
        viewHolder.ivIcon.setTag(url); //解决缓存图片对正确图片的影响
        //new ImageLoader().showImageByThread(viewHolder.ivIcon, mList.get(position).newsIconUrl);
        //new ImageLoader().showImageByAsyncTask(viewHolder.ivIcon, mList.get(position).newsIconUrl);
        mIamgeLoader.showImageByAsyncTask(viewHolder.ivIcon, mList.get(position).newsIconUrl);//防止每次都会创建对象
        viewHolder.tvTitle.setText(mList.get(position).newsTitle);
        viewHolder.tvContent.setText(mList.get(position).newsContent);

        return convertView;
    }
    class ViewHolder{
        public TextView tvTitle,tvContent;
        public ImageView ivIcon;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            //加载图片
            mIamgeLoader.loadImages(mStart, mEnd);
        }else{
            //停止任务
            mIamgeLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem+visibleItemCount;
        //第一次显示的时候调用
        if(mFirstIn && visibleItemCount > 0){
            mIamgeLoader.loadImages(mStart, mEnd);
            mFirstIn = false;
        }
    }


}
