package com.tsinghua.etour.plan;

import com.tsinghua.etour.R;
import com.tsinghua.etour.adapter.EditPlanHorizontalListViewAdapter;
import com.tsinghua.etour.adapter.EditPlanListViewAdapter;
import com.tsinghua.etour.base.BaseActivity;
import com.tsinghua.etour.plan.setposition.SetPositionActivity;
import com.tsinghua.etour.view.HorizontalListView;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class EditPlanActivity extends BaseActivity implements OnClickListener,
		OnGestureListener {
	private static final int NEXT = 1;
	private static final int ADD_REMARK = 2;
	private static final int ADD_TAG = 3;
	
	
	private Button addRemark;
	private Button addTag;
	
	private HorizontalListView horizontalListView;
	private EditPlanHorizontalListViewAdapter mAdapter;
	
	private ListView listView;
	private EditPlanListViewAdapter planAddDayListViewAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initActionBar();	
		setContentView(R.layout.layout_edit_plan);
		initView();
	}
	
	private void initView()
	{
		addRemark = (Button) findViewById(R.id.add_remark);
		addTag = (Button) findViewById(R.id.add_tag);
		
		addRemark.setOnClickListener(this);
		addRemark.setTag(ADD_REMARK);
		addTag.setOnClickListener(this);
		addTag.setTag(ADD_TAG);
		
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
		
		listView = (ListView) findViewById(R.id.edit_plan_list_view);
		planAddDayListViewAdapter = new EditPlanListViewAdapter(this);
		planAddDayListViewAdapter.notifyDataSetChanged();
		listView.setAdapter(planAddDayListViewAdapter);
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
		int tag = (int) v.getTag();
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

}
