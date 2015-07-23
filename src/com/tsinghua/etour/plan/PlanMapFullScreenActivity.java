package com.tsinghua.etour.plan;

import com.tsinghua.etour.R;
import com.tsinghua.etour.adapter.FullScreenMapHorizontalListViewAdapter;
import com.tsinghua.etour.base.BaseActivity;
import com.tsinghua.etour.view.HorizontalListView;

import android.R.integer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PlanMapFullScreenActivity extends BaseActivity implements OnClickListener {
	private static final int BACK_TO_LIST = 1;
	
	private FullScreenMapHorizontalListViewAdapter mAdapter;
	private HorizontalListView mListView;
	
	private TextView backToList;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initActionBar();
		setContentView(R.layout.layout_plan_map_full_screen);
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
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt((String)v.getTag());
		switch (tag) {
		case BACK_TO_LIST:
			finish();
			break;
		default:
			break;
		}
	}

}
