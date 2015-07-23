package com.tsinghua.etour.adapter;

import com.tsinghua.etour.R;
import com.tsinghua.etour.R.string;

import android.R.array;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SetPositionAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ViewHolder mViewHolder;

	//test
	private int arr1[] = {1,2,3,4,5};
	private String arr2[] = {"海淀区","房山区","昌平区","通州","朝阳区"};
	
	public SetPositionAdapter(Context context){
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
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.set_position_listview_item, null);
			mViewHolder.historyPosition = (TextView) convertView.findViewById(R.id.history_position);
			mViewHolder.position = (TextView) convertView.findViewById(R.id.position);
			convertView.setTag(mViewHolder);
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.historyPosition.setText("历史记录" + arr1[position]);
		mViewHolder.position.setText(arr2[position]);
		return convertView;
	}
	
	private static class ViewHolder{
		private TextView historyPosition;
		private TextView position;
	}

}
