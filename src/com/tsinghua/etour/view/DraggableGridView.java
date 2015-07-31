package com.tsinghua.etour.view;


import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class DraggableGridView extends GridView {
	//Ĭ�ϲ�����ק
	private boolean isDrag = false;
	private long dragResponseMS = 1000;  
	//������ק��position 
	private int mDragPosition; 

	private int mDownX;
	private int mDownY;

	private int lMoveX;
	private int lMoveY;

	private float lDownX = -1;
	private float lDownY = -1;
	private long lDownTime = -1;

	private GestureDetector detector;

	//����
	private Vibrator mVibrator;
	//

	private WindowManager mWindowManager;
	//  ״̬���ĸ߶�

	private int mStatusHeight; 
	//������ק�ľ�������ֱ����һ��ImageView

	private ImageView mDragImageView;
	private View mStartDragItemView;
	private WindowManager.LayoutParams mWindowLayoutParams;

	//������ק��item��Ӧ��Bitmap
	private Bitmap mDragBitmap;

	private Handler mHandler = new Handler();  

	//���µĵ㵽����item���ϱ�Ե�ľ��� a
	private int mPoint2ItemTop ;     
	// ���µĵ㵽����item�����Ե�ľ��� 
	private int mPoint2ItemLeft;  
	// DragGridView������Ļ������ƫ���� 
	private int mOffset2Top;  
	//DragGridView������Ļ��ߵ�ƫ���� 
	private int mOffset2Left;  

	private OnChangeListener onChangeListener; 
	/**
	 * DragGridView�Զ����¹����ı߽�ֵ
	 */
	private int mDownScrollBorder;

	/**
	 * DragGridView�Զ����Ϲ����ı߽�ֵ
	 */
	private int mUpScrollBorder;

	/*
	public void setLayoutParams(LayoutParams params){
		super.setLayoutParams(params);
	}; 
	 */
	public DraggableGridView(Context context) {
		this(context, null);
	}

	public DraggableGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}


	public DraggableGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(context); //��ȡ״̬���ĸ߶�
	}

	private void createDragImage(Bitmap bitmap, int downX , int downY){
		if(downX< mPoint2ItemLeft) downX = mPoint2ItemLeft;
		if(downY< mPoint2ItemTop) downY = mPoint2ItemTop;
		mWindowLayoutParams = new WindowManager.LayoutParams();
		mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //ͼƬ֮��������ط�͸��
		mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowLayoutParams.alpha = 0.55f; //͸����
		mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
		mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
		mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

		mDragImageView = new ImageView(getContext());  
		mDragImageView.setImageBitmap(bitmap);  
		mWindowManager.addView(mDragImageView, mWindowLayoutParams);  
	}

	public boolean isLongPressed(float lastX,float lastY,float thisX, float thisY, long lastDownTime,long thisEventTime,long longPressTime){
		float offsetX = Math.abs(thisX-lastX);
		float offsetY =Math.abs(thisY-lastY);
		long intervalTime = thisEventTime - lastDownTime;
		Log.i("time", Long.toString(intervalTime));
		if(offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime){
			return true;
		}else{
			return false;
		}
	}


	public boolean onTouchEvent(MotionEvent ev){
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:

			mDownX = (int)ev.getX();
			mDownY = (int)ev.getY();
			lDownTime = Calendar.getInstance().get(Calendar.SECOND)*1000+Calendar.getInstance().get(Calendar.MILLISECOND);
			Log.i("start time", Long.toString(lDownTime));
			mDragPosition = pointToPosition(mDownX, mDownY);

			if(mDragPosition == AdapterView.INVALID_POSITION){  
				return super.onTouchEvent(ev);  
			}

			//����position��ȡ��item����Ӧ��View
			mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
			//�����⼸�������ҿ��Բο��ҵĲ��������ͼ�������
			mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
			mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
			mOffset2Top = (int) (ev.getRawY() - mDownY);
			mOffset2Left = (int) (ev.getRawX() - mDownX);

			//��ȡDragGridView�Զ����Ϲ�����ƫ������С�����ֵ��DragGridView���¹���
			//mDownScrollBorder = getHeight() /4;
			//��ȡDragGridView�Զ����¹�����ƫ�������������ֵ��DragGridView���Ϲ���
			//	mUpScrollBorder = getHeight() * 3/4;
			//����mDragItemView��ͼ����
			mStartDragItemView.setDrawingCacheEnabled(true);
			//��ȡmDragItemView�ڻ����е�Bitmap����
			mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
			//��һ���ܹؼ����ͷŻ�ͼ���棬��������ظ��ľ���
			mStartDragItemView.destroyDrawingCache();

			break;  

		case MotionEvent.ACTION_MOVE: 
			this.requestDisallowInterceptTouchEvent(true);

			int moveX = (int)ev.getX();  
			int moveY = (int)ev.getY();
			long thisTime = Calendar.getInstance().get(Calendar.SECOND)*1000+Calendar.getInstance().get(Calendar.MILLISECOND);
			Log.i("end time", Long.toString(thisTime));

			if(lDownX < 0){
				lDownX = moveX;
			}
			if(lDownY < 0){
				lDownY = moveY;
			}
			if(lMoveX < 0){
				lMoveX = moveX;
			}
			if(lMoveY < 0){
				lMoveY = moveY;
			}

			if(isDrag == false && isLongPressed(mDownX, mDownY, moveX, moveY, lDownTime, thisTime, 500)){//not lon press
				isDrag = true; //���ÿ�����ק
				//mVibrator.vibrate(50); //��һ��
				if(mStartDragItemView != null)
					mStartDragItemView.setVisibility(View.INVISIBLE);//���ظ�item
				//�������ǰ��µĵ���ʾitem����
				createDragImage(mDragBitmap, mDownX, mDownY);
				//if(moveX)

			}else if(isDrag == true){
				if((moveY < 1.5 * mPoint2ItemTop) && ((moveY-lMoveY<-10)||(Math.abs(moveX-lMoveX) > Math.abs(moveY-lMoveY)))){
					//isDrag = false;
					//onStopDrag();
					//mStartDragItemView.setVisibility(View.VISIBLE);
					onDragItem(moveX,moveY);
				}else{
					onDragItem(moveX,moveY);
				}
			}
			lMoveX = moveX;
			lMoveY = moveY;

			break;  

		case MotionEvent.ACTION_UP: 
			int upX = (int)ev.getX();  
			int upY = (int)ev.getY();
			if(isDrag == true){ 
				onStopDrag(); 
				if(upY > 3*mPoint2ItemTop)
					onDeleteItem(); 
				isDrag = false;
			}
			break;  
		}  
		return super.onTouchEvent(ev);  

	}

	private void onUpdateItem(int moveX, int moveY){
		//��ȡ������ָ�ƶ������Ǹ�item��position
		int tempPosition = pointToPosition(moveX, moveY);
		
		//����tempPosition �ı��˲���tempPosition������-1,����н���
		if(tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION){
			getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//�϶������µ�item,�µ�item���ص�
			getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);//֮ǰ��item��ʾ����
			
			if(onChangeListener != null){
				onChangeListener.onChange(mDragPosition, tempPosition, 1);
			}
			
			mDragPosition = tempPosition;
		}
		
	}
	private void onDragItem(int moveX, int moveY){  
		if(moveX< mPoint2ItemLeft) moveX = mPoint2ItemLeft;
		if(moveY< mPoint2ItemTop) moveY = mPoint2ItemTop;
		if(moveX > getWidth()-mPoint2ItemLeft) moveX = getWidth()-mPoint2ItemLeft;
		mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;  
		mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight; 
		mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //���¾����λ��   
		onUpdateItem(moveX, moveY);
	}

	/** 
	 *  
	 * @param moveX 
	 * @param moveY 
	 */  
	private void onDeleteItem(){  
		if(onChangeListener != null){  
			onChangeListener.onChange(mDragPosition,0,0);  
		}  
	}  
	/** 
	 * ֹͣ��ק���ǽ�֮ǰ���ص�item��ʾ���������������Ƴ� 
	 */  
	private void onStopDrag(){  
		//int pos = mDragPosition -getFirstVisible
		getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
		removeDragImage();

	} 
	/** 
	 * �ӽ��������ƶ��϶����� 
	 */  
	private void removeDragImage(){  
		if(mDragImageView != null){  
			mWindowManager.removeView(mDragImageView);  
			mDragImageView = null; 
			//mStartDragItemView = null;
		}  
	}  

	private static int getStatusHeight(Context context){
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight){
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return statusHeight;
	}

	public interface OnChangeListener{  

		public void onChange(int from, int to, int type);  

	}

	public void setOnChangeListener(OnChangeListener onChangeListener) {
		// TODO Auto-generated method stub
		this.onChangeListener = onChangeListener; 
	}

}

