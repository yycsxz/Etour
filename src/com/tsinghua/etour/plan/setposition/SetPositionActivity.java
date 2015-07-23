package com.tsinghua.etour.plan.setposition;

import com.tsinghua.etour.R;
import com.tsinghua.etour.adapter.SetPositionAdapter;
import com.tsinghua.etour.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class SetPositionActivity extends BaseActivity implements
		OnClickListener {
	private SetPositionAdapter mAdapter;
	private ListView mListView;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initActionBar();
		setContentView(R.layout.layout_set_position);
		initView();
	}
	
	private void initActionBar(){
		initPlanActionBar();
		backButton.setText(getResources().getString(R.string.back));
		textView.setText(getResources().getString(R.string.set_position));
		nextButton.setVisibility(View.GONE);
	}
	
	private void initView(){
		mAdapter = new SetPositionAdapter(this);
		mAdapter.notifyDataSetChanged();
		mListView = (ListView) findViewById(R.id.history_position_list);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
