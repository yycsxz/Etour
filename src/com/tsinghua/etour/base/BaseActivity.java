package com.tsinghua.etour.base;

import com.tsinghua.etour.R;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity {
	protected ActionBar mActionBar;
	protected Button backButton;
	protected Button nextButton;
	protected TextView textView;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//setContentView(R.layout.activity_main);
		mActionBar = getActionBar();
	}
	
	protected void initPlanActionBar(){
		mActionBar.setCustomView(R.layout.actionbar_plan);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setDisplayShowCustomEnabled(true);
		
		backButton = (Button) mActionBar.getCustomView().findViewById(R.id.back);
		nextButton = (Button) mActionBar.getCustomView().findViewById(R.id.next);
		textView = (TextView) mActionBar.getCustomView().findViewById(R.id.title);
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
