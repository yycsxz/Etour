package com.tsinghua.etour.plan;

import com.tsinghua.etour.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;

public class PlanAddRemarkDialog extends DialogFragment implements
		OnClickListener {

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_add_remark, container);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
