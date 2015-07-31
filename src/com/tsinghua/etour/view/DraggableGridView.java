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
	//默认不可拖拽
	private boolean isDrag = false;
	private long dragResponseMS = 1000;  
	//正在拖拽的position 
	private int mDragPosition; 

	private int mDownX;
	private int mDownY;

	private int lMoveX;
	private int lMoveY;

	private float lDownX = -1;
	private float lDownY = -1;
	private long lDownTime = -1;

	private GestureDetector detector;

	//振动器
	private Vibrator mVibrator;
	//

	private WindowManager mWindowManager;
	//  状态栏的高度

	private int mStatusHeight; 
	//用于拖拽的镜像，这里直接用一个ImageView

	private ImageView mDragImageView;
	private View mStartDragItemView;
	private WindowManager.LayoutParams mWindowLayoutParams;

	//我们拖拽的item对应的Bitmap
	private Bitmap mDragBitmap;

	private Handler mHandler = new Handler();  

	//按下的点到所在item的上边缘的距离 a
	private int mPoint2ItemTop ;     
	// 按下的点到所在item的左边缘的距离 
	private int mPoint2ItemLeft;  
	// DragGridView距离屏幕顶部的偏移量 
	private int mOffset2Top;  
	//DragGridView距离屏幕左边的偏移量 
	private int mOffset2Left;  

	private OnChangeListener onChangeListener; 
	/**
	 * DragGridView自动向下滚动的边界值
	 */
	private int mDownScrollBorder;

	/**
	 * DragGridView自动向上滚动的边界值
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
		mStatusHeight = getStatusHeight(context); //获取状态栏的高度
	}

	private void createDragImage(Bitmap bitmap, int downX , int downY){
		if(downX< mPoint2ItemLeft) downX = mPoint2ItemLeft;
		if(downY< mPoint2ItemTop) downY = mPoint2ItemTop;
		mWindowLayoutParams = new WindowManager.LayoutParams();
		mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
		mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowLayoutParams.alpha = 0.55f; //透明度
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

			//根据position获取该item所对应的View
			mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
			//下面这几个距离大家可以参考我的博客上面的图来理解下
			mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
			mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
			mOffset2Top = (int) (ev.getRawY() - mDownY);
			mOffset2Left = (int) (ev.getRawX() - mDownX);

			//获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
			//mDownScrollBorder = getHeight() /4;
			//获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
			//	mUpScrollBorder = getHeight() * 3/4;
			//开启mDragItemView绘图缓存
			mStartDragItemView.setDrawingCacheEnabled(true);
			//获取mDragItemView在缓存中的Bitmap对象
			mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
			//这一步很关键，释放绘图缓存，避免出现重复的镜像
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
				isDrag = true; //设置可以拖拽
				//mVibrator.vibrate(50); //震动一下
				if(mStartDragItemView != null)
					mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item
				//根据我们按下的点显示item镜像
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
		//获取我们手指移动到的那个item的position
		int tempPosition = pointToPosition(moveX, moveY);
		
		//假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
		if(tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION){
			getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);//拖动到了新的item,新的item隐藏掉
			getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);//之前的item显示出来
			
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
		mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //更新镜像的位置   
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
	 * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除 
	 */  
	private void onStopDrag(){  
		//int pos = mDragPosition -getFirstVisible
		getChildAt(mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
		removeDragImage();

	} 
	/** 
	 * 从界面上面移动拖动镜像 
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

