package com.tsinghua.etour.staticc;

import java.util.ArrayList;

public class DragImage {
	static private ArrayList<Integer> imgList;
	static private ArrayList<Integer> imgMorning;
	static private ArrayList<Integer> imgAfternoon;
	static private ArrayList<Integer> imgEvening;
	
	public static void setList(ArrayList<Integer> List){
		imgList = new ArrayList<Integer>(List);
	}
	public static void setMorning(ArrayList<Integer> List){
		imgMorning = new ArrayList<Integer>(List);
	}
	public static void setAfternoon(ArrayList<Integer> List){
		imgAfternoon = new ArrayList<Integer>(List);
	}
	public static void setEvening(ArrayList<Integer> List){
		imgEvening = new ArrayList<Integer>(List);
	}
	public static ArrayList<Integer> getList(){
		return imgList;
	}
	public static ArrayList<Integer> getMorning(){
		return imgMorning;
	}
	public static ArrayList<Integer> getAfternonn(){
		return imgAfternoon;
	}
	public static ArrayList<Integer> getEvening(){
		return imgEvening;
	}
}
