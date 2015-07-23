package com.tsinghua.etour.adapter;

import java.util.zip.Inflater;

import com.tsinghua.etour.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FullScreenMapHorizontalListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	//test
	private int arr[] = {1,2,3,4,5};
	
	public FullScreenMapHorizontalListViewAdapter(Context context){
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
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
		// TODO Auto-generated method stub
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.full_screen_map_horizontalviewlistview_item, null);
			mViewHolder.day = (TextView) convertView.findViewById(R.id.day);
			convertView.setTag(mViewHolder);
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.day.setText("Day" + arr[position]);
		return convertView;
	}
	
	private static class ViewHolder{
		private TextView day;
	}

}
