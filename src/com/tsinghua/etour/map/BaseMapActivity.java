package com.tsinghua.etour.map;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.tsinghua.etour.R;







/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class BaseMapActivity extends Activity implements LocationSource, AMapLocationListener, OnCheckedChangeListener, OnMarkerDragListener, OnMapLoadedListener, OnMarkerClickListener{
	private MapView mapView;
	private AMap aMap;
	private MarkerOptions markerOption;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	private Marker marker2;// 有跳动效果的marker对象
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// 西安市经纬度
	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// 北京市经纬度
	
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_plan);
	    /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
	    //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
      //  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
	 
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		//markerText = (TextView) findViewById(R.id.mark);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		//mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
		//mGPSModeGroup.setOnCheckedChangeListener(this);
	}
	
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);//设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);//设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认false;
		aMap.setMyLocationType(aMap.LOCATION_TYPE_LOCATE);// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		addMarkersToMap();
	}
	/**
	 * 方法必须重写
	 */
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
	/**
	 * 停止定位
	 */

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
/*
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.gps_locate_button:
			// 设置定位的类型为定位模式
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
		case R.id.gps_follow_button:
			// 设置定位的类型为 跟随模式
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
			break;
		case R.id.gps_rotate_button:
			// 设置定位的类型为根据地图面向方向旋转
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
			break;
		case R.id.gps_hole_button:
			// 设置定位的类型为根据地图面向方向旋转
			onMapLoaded();
			break;
		}

		*、
		
	}
	/**
	 * 定位成功后代码，回调函数
	 * 发送定位请求后，会进定位回调，返回位置信息对象 AMapLocation。
	 */

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if(mListener != null && amapLocation != null){
			if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
				mListener.onLocationChanged(amapLocation);//显示系统小蓝点
			}
		}
		
	}
	private void addMarkersToMap(){

		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		giflist.add(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

		MarkerOptions markerOption1 = new MarkerOptions().anchor(0.5f, 0.5f)
				.position(XIAN).title("西安市")
				.snippet("34.341568, 108.940174").icons(giflist)
			 .draggable(true).period(50);
		ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
		markerOptionlst.add(markerOption);
		markerOptionlst.add(markerOption1);
		List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
		marker2 = markerlst.get(0);
		
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
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		LatLngBounds bounds = new LatLngBounds.Builder().include(XIAN).include(BEIJING).build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
		//绘制一个北京到西安的虚线
		aMap.addPolyline((new PolylineOptions())
				.add(new LatLng(34.341568, 108.940174), new LatLng(39.90403, 116.407525))
				.setCustomTexture(BitmapDescriptorFactory.defaultMarker()));
		
	}

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// TODO Auto-generated method stub
		if (marker.equals(marker2)) {
			
			if (aMap != null) {
			 jumpPoint(marker);
				//markerText.setText("你点击的是" + marker.getTitle());
				//markerText.showContextMenu();
			}
		}
		
		return false;
	}
	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(XIAN);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;

		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * XIAN.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * XIAN.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


}
