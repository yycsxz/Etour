package com.tsinghua.etour.plan;

import com.tsinghua.etour.R;
import com.tsinghua.etour.base.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PlanMainActivity extends BaseActivity {
	private ImageView cover;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initActionBar();
		setContentView(R.layout.layout_create_plan);
		setCover();
	}
	
	private void initActionBar()
	{
		initPlanActionBar();
		textView.setText(getResources().getString(R.string.basic_infor));
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(PlanMainActivity.this,EditPlanActivity.class));
			}
		});
	}
	
    private void setCover(){
    	cover = (ImageView) findViewById(R.id.coverImage);
    	cover.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转至图片选择页面
//				Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//				startActivityForResult(intent, Gallery);
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
			    intent.addCategory(Intent.CATEGORY_OPENABLE);
			    intent.setType("image/*");
			    intent.putExtra("crop", "true");
			    intent.putExtra("aspectX", 1);
			    intent.putExtra("aspectY", 1);
			    intent.putExtra("outputX", 80);
			    intent.putExtra("outputY", 80);
			    intent.putExtra("return-data", true);

			    startActivityForResult(intent, 0);
			}
			
		});
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0&&resultCode == Activity.RESULT_OK&&data != null) {
			Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
			cover.setImageBitmap(cameraBitmap);
		}
	}
}
