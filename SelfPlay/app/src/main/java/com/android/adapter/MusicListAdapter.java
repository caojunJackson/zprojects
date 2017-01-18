package com.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.entity.Constant;
import com.android.entity.MusicMessage;
import com.android.play.R;

public class MusicListAdapter extends BaseAdapter {
	private Context context;
	
	public MusicListAdapter(Context context){
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Constant.musicList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Hold hold;
		if(view == null){
			hold = new Hold();
			view = View.inflate(context, R.layout.musiclist_item,null);
			hold.tv_name=(TextView) view.findViewById(R.id.tv_name);
			hold.tv_dur = (TextView) view.findViewById(R.id.tv_dur);
			hold.tv_artist = (TextView) view.findViewById(R.id.tv_art);
			view.setTag(hold);
		}
		hold = (Hold) view.getTag();
		MusicMessage message = Constant.musicList.get(position);
		hold.tv_name.setText(message.getmName());
		hold.tv_dur.setText(Constant.timeFormat(message.getDuration()));
		if(message.getArtist().equalsIgnoreCase("<unknown>")){
			hold.tv_artist.setText("");
		}else {
			hold.tv_artist.setText(message.getArtist());
		}
		return view;
	}
	class Hold{
		TextView tv_name;
		TextView tv_dur;
		TextView tv_artist;
	}
	
	/**
	 *
	 * @return
	 */
	public String time(int tt){
		double mmtotal = tt/1000.0;
		int min = (int)mmtotal/60;
		int mm = (int)(mmtotal%60);
		return min+":"+mm;
		
	}
	
	
}
