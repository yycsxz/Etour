package com.tsinghua.etour.adapter;


import com.tsinghua.etour.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EditPlanHorizontalListViewAdapter extends BaseAdapter {
	private ViewHolder vh = new ViewHolder();
	private LayoutInflater mInflater;
	
	public EditPlanHorizontalListViewAdapter(Context con){
		mInflater=LayoutInflater.from(con);
	}
	
	@Override
	public int getCount() {
		return 5;
	}
	

	@Override
	public Object getItem(int position) {
		return position;
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.horizontallistview_item, null);
			vh.im=(ImageView)convertView.findViewById(R.id.iv_pic);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder)convertView.getTag();
		}
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView im;
	}
}
