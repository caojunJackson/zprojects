package com.android.play;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.adapter.LoopPagerAdapter;
import com.android.adapter.NewsAdapter;
import com.android.entity.Constant;
import com.android.entity.NewsBean;
import com.android.hintview.ColorPointHintView;
import com.android.hintview.RollPagerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/11.
 */
public class HomePagerFragment extends Fragment {
    private String TAG = Constant.TAG;
    private ListView mListView;
    private TextView mEmptyText;

    private RollPagerView mRollViewPager;
    private TestLoopAdapter mLoopAdapter;
    private MoocLoopAdapter mMoocAdapter;

    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();

    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG," HomepagerFragement : onCreateView");
        return inflater.inflate(R.layout.home_fragementlayout,container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG," HomepagerFragement : onActivityCreated");
        mListView = (ListView) getActivity().findViewById(R.id.lv_main);
        mEmptyText = (TextView) getActivity().findViewById(R.id.empty_tab);

        mRollViewPager= (RollPagerView)getActivity().findViewById(R.id.roll_view_pager);
        mRollViewPager.setPlayDelay(2000);
     //   mRollViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mRollViewPager));
//        mRollViewPager.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(getActivity(), Color.YELLOW, Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
        new NewsAsyncTask().execute(URL);
        new RollPagerAsyncTask().execute("");
    }

    //实现网络的异步访问
    class NewsAsyncTask extends AsyncTask<String,Void,List<NewsBean>>/*网址，过程，结果*/{

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJsonData(params[0]);
        }

        @Override
        protected void onPostExecute(final List<NewsBean> newsBeen) {
            super.onPostExecute(newsBeen);
            Log.i(TAG," HomePagerFragment : onPostExecute  = "+newsBeen.size());
            if (newsBeen.size() > 1){
                Log.i(TAG," HomePagerFragment : onPostExecute   if= ");
                mEmptyText.setVisibility(View.GONE);
            }
            Log.i(TAG, " 1 : "+newsBeen.get(2).newsIconUrl);
            Log.i(TAG, " 2 : "+newsBeen.get(5).newsIconUrl);
            Log.i(TAG, " 3 : "+newsBeen.get(8).newsIconUrl);
            Log.i(TAG, " 4 : "+newsBeen.get(9).newsIconUrl);
            Log.i(TAG, " 5 : "+newsBeen.get(11).newsIconUrl);
            NewsAdapter adapter = new NewsAdapter(getActivity(), newsBeen, mListView);
            mListView.setAdapter(adapter);
        }
    }

    /*
     * 将url对应得json格式数据转化为我们所封装的对象
     */
    private List<NewsBean> getJsonData(String url) {
        List<NewsBean> newsBeanList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(URL).openStream());
            Log.i(TAG,"jsonString : "+jsonString);
            JSONObject jsonObject;
            NewsBean newsBean;
            jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0 ; i< jsonArray.length();i++){
                jsonObject = jsonArray.getJSONObject(i);
                newsBean = new NewsBean();
                newsBean.newsIconUrl = jsonObject.getString("picSmall");
                newsBean.newsTitle = jsonObject.getString("name");
                newsBean.newsContent= jsonObject.getString("description");
                newsBeanList.add(newsBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  newsBeanList;
    }

    /*
     * 通过is解析网页返回的数据
     */
    private String readStream(InputStream is){
        InputStreamReader isr;
        String result = "";
        try {
            String line = "";
            isr = new InputStreamReader(is,"utf-8");
            BufferedReader br = new BufferedReader(isr);
            while((line = br.readLine()) != null){
                result += line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    class RollPagerAsyncTask extends AsyncTask<String,Void,List<Bitmap>>{


        @Override
        protected List<Bitmap> doInBackground(String... params) {
            bitmaps.add(getBitmapFromURL("http://img.mukewang.com/5523711700016d1606000338-300-170.jpg"));
            bitmaps.add(getBitmapFromURL("http://img.mukewang.com/552640c300018a9606000338-300-170.jpg"));
            bitmaps.add(getBitmapFromURL("http://img.mukewang.com/551b98ae0001e57906000338-300-170.jpg"));
            bitmaps.add(getBitmapFromURL("http://img.mukewang.com/550b86560001009406000338-300-170.jpg"));
            bitmaps.add(getBitmapFromURL("http://img.mukewang.com/5518ecf20001cb4e06000338-300-170.jpg"));

            return bitmaps;
        }

        @Override
        protected void onPostExecute(List<Bitmap> bitmaps) {
            super.onPostExecute(bitmaps);
            Log.i(TAG, "RollPaper size = "+ bitmaps.size());
            if(bitmaps.size() > 1){
                mRollViewPager.setAdapter(mMoocAdapter = new MoocLoopAdapter(mRollViewPager));
            }else{
                mRollViewPager.setAdapter(mLoopAdapter = new TestLoopAdapter(mRollViewPager));
            }
            mMoocAdapter.notifyDataSetChanged();
        }
    }

    //  通过一个网络上的url来转换成图片
    public Bitmap getBitmapFromURL(String urlString){
        Bitmap bitmap;
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private class MoocLoopAdapter extends LoopPagerAdapter {

        private int count = bitmaps.size();


        public MoocLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageBitmap(bitmaps.get(position));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.i("fenghaitao","===============getView==position="+position);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return count;
        }

    }


    private class TestLoopAdapter extends LoopPagerAdapter {
        private int[] imgs = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
        };
        private int count = imgs.length;

        public void add(){
            Log.i("RollViewPager","Add");
            count++;
            if (count>imgs.length)count = imgs.length;
            notifyDataSetChanged();
        }
        public void minus(){
            Log.i("RollViewPager","Minus");
            count--;
            if (count<1)count=1;
            notifyDataSetChanged();
        }

        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.i("fenghaitao","===============getView==position="+position);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return count;
        }

    }
}
