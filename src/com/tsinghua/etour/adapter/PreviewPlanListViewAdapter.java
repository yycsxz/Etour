package com.tsinghua.etour.adapter;

import com.tsinghua.etour.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PreviewPlanListViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;
	
	//test
	private int arr[] = {1,2,3,4,5};
	
	public PreviewPlanListViewAdapter(Context context){
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
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.plan_preview_day_listview_item, null);
			mViewHolder.day = (TextView) convertView.findViewById(R.id.day);
			convertView.setTag(mViewHolder);			
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.day.setText("Day"+arr[position]);
		return convertView;
	}
	
	private static class ViewHolder{
		private TextView day;
	}
	
	


}
