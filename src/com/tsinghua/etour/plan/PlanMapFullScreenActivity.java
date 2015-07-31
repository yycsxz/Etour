package com.tsinghua.etour.plan;

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
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.tsinghua.etour.R;
import com.tsinghua.etour.adapter.FullScreenMapHorizontalListViewAdapter;
import com.tsinghua.etour.base.BaseActivity;
import com.tsinghua.etour.view.HorizontalListView;

import android.R.integer;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PlanMapFullScreenActivity extends BaseActivity implements OnClickListener, LocationSource, OnMarkerDragListener, OnMapLoadedListener, OnMarkerClickListener, AMapLocationListener {
	private static final int BACK_TO_LIST = 1;

	private FullScreenMapHorizontalListViewAdapter mAdapter;
	private HorizontalListView mListView;

	private TextView backToList;

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
		setContentView(R.layout.layout_plan_map_full_screen);
		//map
				mapView = (MapView) findViewById(R.id.map);
				mapView.onCreate(arg0);// 此方法必须重写
				initView();
	}

	private void initActionBar(){
		initPlanActionBar();
		backButton.setText(getResources().getString(R.string.back));
		textView.setText(getResources().getString(R.string.preview));
		nextButton.setText(getResources().getString(R.string.save));
	}

	private void initView(){
		mAdapter = new FullScreenMapHorizontalListViewAdapter(this);
		mAdapter.notifyDataSetChanged();
		mListView = (HorizontalListView) findViewById(R.id.map_full_screen_horizontal_listview);
		mListView.setAdapter(mAdapter);

		backToList = (TextView) findViewById(R.id.list);
		backToList.setOnClickListener(this);
		backToList.setTag(BACK_TO_LIST);
		
		//map
				//startActivity(new Intent(EditPlanActivity.this,BaseMapActivity.class));
				if (aMap == null) {
					aMap = mapView.getMap();
					setUpMap();
				}
	}
	private void setUpMap() {
		aMap.setLocationSource(this);//设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示
		aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.setMyLocationEnabled(true);//设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认false;
		aMap.setMyLocationType(aMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt(v.getTag().toString());
		switch (tag) {
		case BACK_TO_LIST:
			finish();
			break;
		default:
			break;
		}
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
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		
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

}
