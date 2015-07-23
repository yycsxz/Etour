package com.tsinghua.etour.plan;

import javax.security.auth.PrivateCredentialPermission;

import com.tsinghua.etour.R;

import android.R.integer;
import android.app.DialogFragment;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class PlanAddTagDialog extends DialogFragment implements OnClickListener {
	private static final int VIEW = 1;
	private static final int RESTAURANT = 2;
	private static final int HOTEL = 3;
	private static final int ENTERTAINMENT = 4;
	private static final int SHOPPING = 5;
	private static final int OTHER = 6;
	
	private static final int SELECTED = 1;
	private static final int UNSELECTED = 2;
	
	private Button viewButton;
	private Button restaurantButton;
	private Button hotelButton;
	private Button entertainmentButton;
	private Button shoppingButton;
	private Button otherButton;
	
	private int viewState = UNSELECTED;
	private int restaurantState = UNSELECTED;
	private int hotelState = UNSELECTED;
	private int entertainmentState = UNSELECTED;
	private int shoppingState = UNSELECTED;
	private int otherState = UNSELECTED;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_add_tag, container);
		
		viewButton = (Button) view.findViewById(R.id.view);
		restaurantButton = (Button) view.findViewById(R.id.restaurant);
		hotelButton = (Button) view.findViewById(R.id.hotel);
		entertainmentButton = (Button) view.findViewById(R.id.entertainment);
		shoppingButton = (Button) view.findViewById(R.id.shopping);
		otherButton = (Button) view.findViewById(R.id.other);

		viewButton.setOnClickListener(this);
		viewButton.setTag(VIEW);
		restaurantButton.setOnClickListener(this);
		restaurantButton.setTag(RESTAURANT);
		hotelButton.setOnClickListener(this);
		hotelButton.setTag(HOTEL);
		entertainmentButton.setOnClickListener(this);
		entertainmentButton.setTag(ENTERTAINMENT);
		shoppingButton.setOnClickListener(this);
		shoppingButton.setTag(SHOPPING);
		otherButton.setOnClickListener(this);
		otherButton.setTag(OTHER);
		
		return view;
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int tag = (int) v.getTag();
		switch (tag) {
		case VIEW:
			if (viewState == UNSELECTED) {
				viewButton.setBackgroundColor(getResources().getColor(R.color.orange));
				viewState = SELECTED;
			}else{
				viewButton.setBackgroundColor(getResources().getColor(R.color.bg));
				viewState = UNSELECTED;
			}
			break;
		case RESTAURANT:
			if (restaurantState == UNSELECTED) {
				restaurantButton.setBackgroundColor(getResources().getColor(R.color.orange));
				restaurantState = SELECTED;
			}else{
				restaurantButton.setBackgroundColor(getResources().getColor(R.color.bg));
				restaurantState = UNSELECTED;
			}
			break;
		case HOTEL:
			if (hotelState == UNSELECTED) {
				hotelButton.setBackgroundColor(getResources().getColor(R.color.orange));
				hotelState = SELECTED;
			}else{
				hotelButton.setBackgroundColor(getResources().getColor(R.color.bg));
				hotelState = UNSELECTED;
			}
			break;
		case ENTERTAINMENT:
			if (entertainmentState == UNSELECTED) {
				entertainmentButton.setBackgroundColor(getResources().getColor(R.color.orange));
				entertainmentState = SELECTED;
			}else{
				entertainmentButton.setBackgroundColor(getResources().getColor(R.color.bg));
				entertainmentState = UNSELECTED;
			}
			break;
		case SHOPPING:
			if (shoppingState == UNSELECTED) {
				shoppingButton.setBackgroundColor(getResources().getColor(R.color.orange));
				shoppingState = SELECTED;
			}else{
				shoppingButton.setBackgroundColor(getResources().getColor(R.color.bg));
				shoppingState = UNSELECTED;
			}
			break;
		case OTHER:
			if (otherState == UNSELECTED) {
				otherButton.setBackgroundColor(getResources().getColor(R.color.orange));
				otherState = SELECTED;
			}else{
				otherButton.setBackgroundColor(getResources().getColor(R.color.bg));
				otherState = UNSELECTED;
			}
			break;
		default:
			break;
		}
	}
	



}
