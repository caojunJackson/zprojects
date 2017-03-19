package com.android.recyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.android.recyclerviewdemo.R.layout.item_text;

/**
 * Created by root on 17-2-5.
 */
public class NormalReyclerViewAdapter extends RecyclerView.Adapter<NormalReyclerViewAdapter.NormalTextViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] mTitles;


    public NormalReyclerViewAdapter(Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NormalReyclerViewAdapter.NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(item_text, false));
    }

    @Override
    public void onBindViewHolder(NormalReyclerViewAdapter.NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
    }

    @Override
    public int getItemCount() {
        return mTitles == null?0:mTitles.length;
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;

        NormalTextViewHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.d("fht","onclic-->position = "+getPosition());
                }
            });
        }
    }
}
