package com.tsinghua.etour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<Integer> imgList = new ArrayList<Integer>();
	private int xmlView;
	private int imgView;

	public GridViewAdapter(Context context, ArrayList<Integer> imgList, int xmlView, int imgView) {
		this.context = context;
		this.imgList = imgList;
		this.xmlView = xmlView;
		this.imgView = imgView;
		
	}

	@Override
	public int getCount() {
		return imgList.size();
	}

	@Override
	public Object getItem(int position) {
		return imgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		convertView = layoutInflater.inflate(xmlView, null);
		ImageView imageView = (ImageView) convertView.findViewById(imgView);
		imageView.setImageResource(imgList.get(position % imgList.size()));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		return convertView;
	}
}
