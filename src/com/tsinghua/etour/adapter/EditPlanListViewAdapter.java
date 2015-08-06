package com.tsinghua.etour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsinghua.etour.R;
import com.tsinghua.etour.view.DraggableGridView;
import com.tsinghua.etour.view.DraggableGridView.OnChangeListener;

public class EditPlanListViewAdapter extends BaseAdapter {
	
	private ViewHolder mViewHolder;
	private LayoutInflater mInflater;
	
	private ArrayList<Integer> imgMorning;
	private ArrayList<Integer> imgAfternoon;
	private ArrayList<Integer> imgEvening;
	private int xmlView;
	private int imgView;
	
	public GridViewAdapter adapterMorning;
	public GridViewAdapter adapterAfternoon;
	public GridViewAdapter adapterEvening;
	
	private Context context;

	//test
	private int arr[] = {1};

	public EditPlanListViewAdapter(Context context, ArrayList<Integer> imgMorning, ArrayList<Integer> imgAfternoon, ArrayList<Integer> imgEvening, int xmlView, int imgView){
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.imgMorning = new ArrayList<Integer>(imgMorning);
		this.imgAfternoon = new ArrayList<Integer>(imgAfternoon);
		this.imgEvening = new ArrayList<Integer>(imgEvening);
		this.xmlView = xmlView;
		this.imgView =imgView;
		//初始化，context, 图片数组， itemview id， imageview id
		adapterMorning = new GridViewAdapter(context, imgMorning,xmlView, imgView);
		adapterAfternoon = new GridViewAdapter(context, imgAfternoon,xmlView, imgView);
		adapterEvening = new GridViewAdapter(context, imgEvening,xmlView, imgView);
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

	public void notifyDataSetChanged(){
		adapterMorning.notifyDataSetChanged();
		super.notifyDataSetChanged();
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			mViewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.plan_add_day_listview_item, null);
			mViewHolder.day = (TextView) convertView.findViewById(R.id.day);
			mViewHolder.morning = (DraggableGridView) convertView.findViewById(R.id.grid_morning);
		    mViewHolder.afternoon = (DraggableGridView) convertView.findViewById(R.id.grid_afternoon);
			mViewHolder.evening = (DraggableGridView) convertView.findViewById(R.id.grid_evening);
			convertView.setTag(mViewHolder);
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.day.setText("Day" + arr[position]);
		//嵌套gridview adapter
		setGridView(mViewHolder.morning, adapterMorning);
		setGridView(mViewHolder.afternoon, adapterAfternoon);
		setGridView(mViewHolder.evening, adapterEvening);
		
		return convertView;
	}

	private void setGridView(DraggableGridView mygridView, final GridViewAdapter adapter) {
		int size = adapter.imgList.size();
		int length = 50;
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float density = dm.density;
		int gridviewWidth = (int) (size * (length+2) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, itemWidth);
		Log.i("params.width",Integer.toString(params.width));
		Log.i("params.height",Integer.toString(params.height));

		mygridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		mygridView.setColumnWidth(itemWidth); // 设置列表项宽
		mygridView.setHorizontalSpacing(5); // 设置列表项水平间距
		mygridView.setStretchMode(GridView.NO_STRETCH);
		mygridView.setNumColumns(size); // 设置列数量=列表集合数

		mygridView.setAdapter(adapter);
		
		mygridView.setOnChangeListener(new OnChangeListener() {

			@Override
			public void onChange(int from, int to, int type, int x, int y) {
				Log.i("from", Integer.toString(from));
				Log.i("to", Integer.toString(to));
				if(adapter.imgList.get(0)!= null)
				Log.i("p1", Integer.toString(adapter.imgList.get(0)));
				if(adapter.imgList.get(0)!= null)
				Log.i("p2", Integer.toString(adapter.imgList.get(1)));
				if(from>=0 && from<adapter.imgList.size() && to>=0 && to<adapter.imgList.size()){
					
					if(type == 1){
						if(from > to){
							int temp = adapter.imgList.get(from);
							adapter.imgList.remove(from);
							adapter.imgList.add(to, temp);
						}else{
							adapter.imgList.add(to+1, adapter.imgList.get(from));
							adapter.imgList.remove(from);
						}
						adapter.notifyDataSetChanged();
						Log.i("p1", Integer.toString(adapter.imgList.get(0)));
						Log.i("p2", Integer.toString(adapter.imgList.get(1)));
						//this.notifyDataSetChanged();
					}else{
						if(from >= 0 && from <adapter.imgList.size()){
							int temp = adapter.imgList.get(from);
							adapter.imgList.remove(from);
							adapter.notifyDataSetChanged();
							Log.i("changed",Integer.toString(temp));
						}
					}
				}
			}
		});

	}
	//视图缓存
	private static class ViewHolder {
		private TextView day;
		private DraggableGridView morning;
		private DraggableGridView afternoon;
		private DraggableGridView evening;
	}

}
