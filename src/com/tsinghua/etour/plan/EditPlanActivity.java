package com.tsinghua.etour.plan;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.tsinghua.etour.R;
import com.tsinghua.etour.adapter.EditPlanHorizontalListViewAdapter;
import com.tsinghua.etour.adapter.EditPlanListViewAdapter;
import com.tsinghua.etour.adapter.GridViewAdapter;
import com.tsinghua.etour.base.BaseActivity;
import com.tsinghua.etour.staticc.DragImage;
import com.tsinghua.etour.view.DraggableGridView;
import com.tsinghua.etour.view.DraggableGridView.OnChangeListener;

public class EditPlanActivity extends BaseActivity implements OnClickListener,
OnGestureListener, LocationSource, OnMarkerDragListener, OnMapLoadedListener, OnMarkerClickListener, AMapLocationListener {
	private static final int NEXT = 1;
	private static final int ADD_REMARK = 2;
	private static final int ADD_TAG = 3;


	private Button addRemark;
	private Button addTag;

	//private HorizontalListView horizontalListView;
	private EditPlanHorizontalListViewAdapter mAdapter;

	private ListView listView;
	private EditPlanListViewAdapter planAddDayListViewAdapter;

	//draggable
	private DraggableGridView gridView;
	private ArrayList<Integer> imgList = new ArrayList<Integer>();
	private GridViewAdapter adapter;

	private ArrayList<Integer> imgMorning = new ArrayList<Integer>();
	private ArrayList<Integer> imgAfternoon = new ArrayList<Integer>();
	private ArrayList<Integer> imgEvening = new ArrayList<Integer>();


	//map
	private MapView mapView;
	private AMap aMap;
	private MarkerOptions markerOption;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initActionBar();	
		setContentView(R.layout.layout_edit_plan);

		//map
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(arg0);// 此方法必须重写
		addData();
		initView();

	}
	private void addData(){
		imgList.add(R.drawable.p1);
		imgList.add(R.drawable.p2);
		imgList.add(R.drawable.p3);
		imgList.add(R.drawable.p4);
		imgList.add(R.drawable.p5);
		imgList.add(R.drawable.p6);
		imgList.add(R.drawable.p7);
		imgList.add(R.drawable.p8);
		imgMorning.add(R.drawable.p1);
		imgMorning.add(R.drawable.p2);
		imgAfternoon.add(R.drawable.p3);
		imgEvening.add(R.drawable.p5);
		DragImage.setList(imgList);
		DragImage.setMorning(imgMorning);
	}
	private void initView()
	{
		addRemark = (Button) findViewById(R.id.add_remark);
		addTag = (Button) findViewById(R.id.add_tag);
		gridView = (DraggableGridView) findViewById(R.id.grid);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		//View convertView = layoutInflater.inflate(R.layout.plan_add_day_listview_item, null);
		//gridMorning = (DraggableGridView) convertView.findViewById(R.id.grid_morning);
		adapter = new GridViewAdapter(getApplicationContext(),imgList, R.layout.list_item, R.id.ItemImage);
		//adapterMorning = new GridViewAdapter(getApplicationContext(),imgMorning, R.layout.list_item_morning, R.id.ItemImageMorning); 
		addRemark.setOnClickListener(this);
		addRemark.setTag(ADD_REMARK);
		addTag.setOnClickListener(this);
		addTag.setTag(ADD_TAG);

		listView = (ListView) findViewById(R.id.edit_plan_list_view);


		planAddDayListViewAdapter = new EditPlanListViewAdapter(this, imgMorning, imgAfternoon, imgEvening, R.layout.list_item,R.id.ItemImage);
		planAddDayListViewAdapter.notifyDataSetChanged();
		listView.setAdapter(planAddDayListViewAdapter);
		setGridView(imgList, gridView, adapter);
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg, MotionEvent ev) {
				// TODO Auto-generated method stub
				switch(ev.getAction()){
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					planAddDayListViewAdapter.notifyDataSetChanged();
					break;
				case MotionEvent.ACTION_UP:
					planAddDayListViewAdapter.notifyDataSetChanged();
				}

				return false;
			}
		});
		//	setGridView(imgMorning, olanAddDayListViewAdapter.a);
		//gridMorning.setAdapter(adapterMorning);
		//setGridView(imgMorning, gridMorning, adapterMorning);
		//map
		//startActivity(new Intent(EditPlanActivity.this,BaseMapActivity.class));
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		/*
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontallistview);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(EditPlanActivity.this,SetPositionActivity.class));
			}
		});

		mAdapter = new EditPlanHorizontalListViewAdapter(this);
		mAdapter.notifyDataSetChanged();
		horizontalListView.setAdapter(mAdapter);
		 */


	}
	private void setGridView(final ArrayList<Integer> imgList, DraggableGridView mygridView, final GridViewAdapter adapter) {
		int size = imgList.size();
		int length = 50;
		int h = listView.getHeight();
		int y = (int) listView.getY();
		Log.i("listview h", Integer.toString(h));
		Log.i("listview y", Integer.toString(y));
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
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
			public void onChange(int from, int to, int type, int x ,int y) {
				Log.i("from", Integer.toString(from));
				Log.i("to", Integer.toString(to));
				if(type == 1){
					if(from > to){
						int temp = imgList.get(from);
						imgList.remove(from);
						imgList.add(to, temp);
					}else{
						imgList.add(to+1, imgList.get(from));
						imgList.remove(from);
					}
					adapter.notifyDataSetChanged();
				}else{
					if(from >= 0 && from <imgList.size()){
						int temp = imgList.get(from);
						imgList.remove(from);
						adapter.notifyDataSetChanged();
						if(y > 1050 && y <= 1200)
							planAddDayListViewAdapter.adapterMorning.imgList.add(temp);
						else if(y > 1200 && y <= 1350)
							planAddDayListViewAdapter.adapterAfternoon.imgList.add(temp);
						else if(y > 1350 && y <= 1500)
							planAddDayListViewAdapter.adapterEvening.imgList.add(temp);
						planAddDayListViewAdapter.notifyDataSetChanged();
						//Hori.updateViewLayout(gridView, null);
						Log.i("changed",Integer.toString(temp));
					}
				}

			}
		});

	}
	private void setUpMap() {
		aMap.setLocationSource(this);//设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示
		aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.setMyLocationEnabled(true);//设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认false;
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		//addMarkersToMap();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
	private void initActionBar()
	{
		initPlanActionBar();
		backButton.setText(getResources().getString(R.string.back));
		textView.setText(getResources().getString(R.string.edit_plan));
		nextButton.setOnClickListener(this);
		nextButton.setTag(NEXT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt(v.getTag().toString());
		switch (tag) {
		case NEXT:
			startActivity(new Intent(EditPlanActivity.this,PreviewPlanActivity.class));
			break;
		case ADD_REMARK:
			addRemark();
			break;
		case ADD_TAG:
			addTag();
			break;
		default:
			break;
		}
	}

	private void addTag(){
		PlanAddTagDialog addTagDialog = new PlanAddTagDialog();
		addTagDialog.show(getFragmentManager(), "AddTagDialog");
	}

	private void addRemark(){
		PlanAddRemarkDialog addRemarkDialog = new PlanAddRemarkDialog();
		addRemarkDialog.show(getFragmentManager(), "AddRemarkDialog");
	}


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
		if(mAMapLocationManager == null){
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			//此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			//注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			//在定位结束后，在合适的生命周期调用destroy()方法		
			//其中如果间隔时间为-1，则定位只定一次
			//在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 60*1000, 10, this);
		}
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener = null;
		if(mAMapLocationManager != null){
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
	}

	@Override
	public void onMarkerDrag(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragEnd(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMarkerDragStart(Marker arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub


	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if(mListener != null && amapLocation != null){
			if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
				mListener.onLocationChanged(amapLocation);//显示系统小蓝点
			}
		}
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub


	}

}
