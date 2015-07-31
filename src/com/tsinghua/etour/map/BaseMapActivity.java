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
 * AMapV2��ͼ�н��������ʾһ��������ͼ
 */
public class BaseMapActivity extends Activity implements LocationSource, AMapLocationListener, OnCheckedChangeListener, OnMarkerDragListener, OnMapLoadedListener, OnMarkerClickListener{
	private MapView mapView;
	private AMap aMap;
	private MarkerOptions markerOption;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;

	private Marker marker2;// ������Ч����marker����
	public static final LatLng XIAN = new LatLng(34.341568, 108.940174);// �����о�γ��
	public static final LatLng BEIJING = new LatLng(39.90403, 116.407525);// �����о�γ��
	
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_plan);
	    /*
         * �������ߵ�ͼ�洢Ŀ¼�����������ߵ�ͼ���ʼ����ͼ����;
         * ʹ�ù����п���������, ���������������ߵ�ͼ�洢��·����
         * ����Ҫ�����ߵ�ͼ���غ�ʹ�õ�ͼҳ�涼����·������
         * */
	    //Demo��Ϊ�������������ʹ�����ص����ߵ�ͼ��ʹ��Ĭ��λ�ô洢���������Զ�������
      //  MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// �˷���������д
	 
		init();
	}

	/**
	 * ��ʼ��AMap����
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
	 * ����һЩamap������
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);//���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);//����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);//����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ��false;
		aMap.setMyLocationType(aMap.LOCATION_TYPE_LOCATE);// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������
		aMap.setOnMapLoadedListener(this);// ����amap���سɹ��¼�������
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		addMarkersToMap();
	}
	/**
	 * ����������д
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}
	/**
	 * ����������д
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}
	/**
	 * ����������д
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	/**
	 * ����������д
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
			//�˷���Ϊÿ���̶�ʱ��ᷢ��һ�ζ�λ����Ϊ�˼��ٵ������Ļ������������ģ�
			//ע�����ú��ʵĶ�λʱ��ļ������С���֧��Ϊ2000ms���������ں���ʱ�����removeUpdates()������ȡ����λ����
			//�ڶ�λ�������ں��ʵ��������ڵ���destroy()����		
			//����������ʱ��Ϊ-1����λֻ��һ��
			//�ڵ��ζ�λ����£���λ���۳ɹ���񣬶��������removeUpdates()�����Ƴ����󣬶�λsdk�ڲ����Ƴ�
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 60*1000, 10, this);
		}
			
	}
	/**
	 * ֹͣ��λ
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
			// ���ö�λ������Ϊ��λģʽ
			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
			break;
		case R.id.gps_follow_button:
			// ���ö�λ������Ϊ ����ģʽ
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
			break;
		case R.id.gps_rotate_button:
			// ���ö�λ������Ϊ���ݵ�ͼ��������ת
			aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
			break;
		case R.id.gps_hole_button:
			// ���ö�λ������Ϊ���ݵ�ͼ��������ת
			onMapLoaded();
			break;
		}

		*��
		
	}
	/**
	 * ��λ�ɹ�����룬�ص�����
	 * ���Ͷ�λ����󣬻����λ�ص�������λ����Ϣ���� AMapLocation��
	 */

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if(mListener != null && amapLocation != null){
			if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
				mListener.onLocationChanged(amapLocation);//��ʾϵͳС����
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
				.position(XIAN).title("������")
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
		//����һ������������������
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
				//markerText.setText("��������" + marker.getTitle());
				//markerText.showContextMenu();
			}
		}
		
		return false;
	}
	/**
	 * marker���ʱ����һ��
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
