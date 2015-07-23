package com.tsinghua.etour.plan;

import com.tsinghua.etour.R;
import com.tsinghua.etour.adapter.PreviewPlanListViewAdapter;
import com.tsinghua.etour.base.BaseActivity;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class PreviewPlanActivity extends BaseActivity implements
		OnClickListener {
	private static final int NEXT = 1;
	private static final int FULL_SCREEN = 2;
	
	private PreviewPlanListViewAdapter mAdapter;
	private ListView listView;
	private TextView fullScreen;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initActionBar();
		setContentView(R.layout.layout_preview_plan);
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
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = Integer.parseInt((String)v.getTag());
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

}
