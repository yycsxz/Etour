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
import com.tsinghua.etour.adapter.PreviewPlanListViewAdapter;
import com.tsinghua.etour.base.BaseActivity;

import android.R.integer;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PreviewPlanActivity extends BaseActivity implements
OnClickListener, LocationSource, OnMarkerDragListener, OnMapLoadedListener, OnMarkerClickListener, AMapLocationListener {
	private static final int NEXT = 1;
	private static final int FULL_SCREEN = 2;

	private PreviewPlanListViewAdapter mAdapter;
	private ListView listView;
	private TextView fullScreen;

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
		setContentView(R.layout.layout_preview_plan);
		//map
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(arg0);// �˷���������д
		initView();
	}

	private void initActionBar()
	{
		initPlanActionBar();
		backButton.setText(getResources().getString(R.string.back));
		textView.setText(getResources().getString(R.string.preview));
		nextButton.setText(getResources().getString(R.string.save));

		nextButton.setOnClickListener(this);
		nextButton.setTag(NEXT);
	}

	private void initView(){
		mAdapter = new PreviewPlanListViewAdapter(this);
		mAdapter.notifyDataSetChanged();
		listView = (ListView) findViewById(R.id.preview_plan_list_view);
		listView.setAdapter(mAdapter);
		fullScreen = (TextView) findViewById(R.id.full_screen);

		fullScreen.setOnClickListener(this);
		fullScreen.setTag(FULL_SCREEN);
		//map
		//startActivity(new Intent(EditPlanActivity.this,BaseMapActivity.class));
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		aMap.setLocationSource(this);//���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(false);//����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.getUiSettings().setZoomControlsEnabled(false);
		aMap.setMyLocationEnabled(true);//����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ��false;
		aMap.setMyLocationType(aMap.LOCATION_TYPE_LOCATE);// ���ö�λ������Ϊ��λģʽ �������ɶ�λ��������ͼ������������ת����
		aMap.setOnMarkerDragListener(this);// ����marker����ק�¼�������
		aMap.setOnMapLoadedListener(this);// ����amap���سɹ��¼�������
		aMap.setOnMarkerClickListener(this);// ���õ��marker�¼�������
		//addMarkersToMap();
	}
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt(v.getTag().toString());
		//int tag = v.getTag().hashCode();
		switch (tag) {
		case NEXT:
			break;
		case FULL_SCREEN:
			startActivity(new Intent(PreviewPlanActivity.this,PlanMapFullScreenActivity.class));
			break;
		default:
			break;
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

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if(mListener != null && amapLocation != null){
			if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
				mListener.onLocationChanged(amapLocation);//��ʾϵͳС����
			}
		}
	}

}
