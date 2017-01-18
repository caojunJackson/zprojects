package com.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.activity.Finger;
import com.android.fingerprintcallbackdemo.R;

import ma.release.Fingerprint;

import java.util.List;
import java.util.zip.Inflater;

public class FingerAdapter extends BaseAdapter {
    List<Finger> mFingerLists;
    Context mContext;
    
    
    public FingerAdapter(Context context, List<Finger> fingerprints) {
        mFingerLists = fingerprints;
        mContext = context;
    }
    
    @Override
    public int getCount() {

        return mFingerLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mFingerLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holde holde;
        
        if(convertView == null){
            holde = new Holde();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.finger_item, null);
            holde.fingerItemView = (TextView)convertView.findViewById(R.id.finger_item_tv);
            convertView.setTag(holde);
        }else{
            holde = (Holde)convertView.getTag();
        }
        for(Finger finger:mFingerLists){
            Log.i("fenghaitao", "====adapter==finger name ="+finger.getName());
        }
        
        holde.fingerItemView.setText(mFingerLists.get(position).getName());
        
        return convertView;
    }

   class Holde{
      TextView fingerItemView;
   }
}
