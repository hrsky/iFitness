package com.example.ifitness.calcCalorie;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import com.example.ifitness.Utils;

public class CalorieInfo {
	private  int num;
	private int mSteps;
	private int mCalorie;
	// private long mWalkingDistance;
	public UserInfo mUserInfo;
	private StringBuffer mTime;
	public Calendar oldCal;
	public String mDate;
	private Vector<OnCalorieChangeListener> mListeners;
	private OnCalorieChangeListener mOnCaloriechangeListener;
	public double t_km;
	public double t_mimutes;
	public int t_calories;
	
	public CalorieInfo() {
		mUserInfo = new UserInfo();
		mTime=new StringBuffer("");
		t_km=0.0;
		t_mimutes=0.0;
		t_calories=0;
		mSteps = 0;
		mCalorie = 0;

		mListeners = new Vector<OnCalorieChangeListener>();
		DateInit();
	}
	
	public CalorieInfo(UserInfo user) {
		mUserInfo = user;
		mTime=new StringBuffer("");
		mSteps = 0;
		mCalorie = 0;
		t_km=0.0;
		t_mimutes=0.0;
		t_calories=0;
		mListeners = new Vector<OnCalorieChangeListener>();
		DateInit();
	}

	public void DateInit() {
		mDate = Utils.getCurrentDate();
		num = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		setTime(calendar);
		setSteps(0);
		setCalorie(0);
	}

	public boolean IsToday() {
		String curDateStr = Utils.getCurrentDate();
		if (mDate.equals(curDateStr)) {
			return true;
		}
		return false;
	}

	public void addOnCalorieChangeListener(OnCalorieChangeListener listener) {
		mListeners.addElement(listener);
	}

	public void removeCalorieChangeListener(OnCalorieChangeListener listener) {
		mListeners.remove(listener);
	}
	
	public String getTime() {
		return this.mTime.toString();
	}

	public void setTime(Calendar c) {
		if (c != null){
			oldCal = c;
			mTime = new StringBuffer("00:00");
			return;
		}
		mTime = new StringBuffer("");
		Calendar newCal = Calendar.getInstance();
		newCal.setTime(new Date());
		int m = newCal.get(Calendar.MINUTE) - oldCal.get(Calendar.MINUTE);
		int s = newCal.get(Calendar.SECOND) - oldCal.get(Calendar.SECOND);
		int t = m * 60 + s;

		mTime.append(String.valueOf(t / 60) + ":" + String.valueOf(t % 60));
		
		Enumeration<OnCalorieChangeListener> enume = mListeners.elements();
		while (enume.hasMoreElements()) {
			mOnCaloriechangeListener = (OnCalorieChangeListener) enume
					.nextElement();
			mOnCaloriechangeListener.onTimeChange(this);
		}
	}

	public int getSteps() {
		return mSteps;
	}

	public void setSteps(int steps) {
		int old = mSteps;
		mSteps = steps;
		if (old != steps) {
			Enumeration<OnCalorieChangeListener> enume = mListeners.elements();
			while (enume.hasMoreElements()) {
				mOnCaloriechangeListener = (OnCalorieChangeListener) enume
						.nextElement();
				mOnCaloriechangeListener.onStepsChange(this);
			}

			int calorie = CalcCalorie(steps);
			this.setCalorie(calorie);
		}
	}

	public int getCalorie() {
		return mCalorie;
	}

	public void setCalorie(int calorie) {
		int oldCalorie = mCalorie;
		mCalorie = calorie;
		if (oldCalorie != calorie) {
			Enumeration<OnCalorieChangeListener> enume = mListeners.elements();
			while (enume.hasMoreElements()) {
				mOnCaloriechangeListener = (OnCalorieChangeListener) enume
						.nextElement();
				mOnCaloriechangeListener.onCalorieChange(this);
			}
		}
	}
	
	public int getNum(){
		return num;
	}
	
	public void setNum(int n){
		num = n;
	}
	
	public void addNum(){
		num += 1;
	}

	private int CalcCalorie(int frequency) {
		int result = (int) ((float) mUserInfo.weight
				* (float) ((float) frequency * (float) mUserInfo.distance / 100000)
				* 1.036 / 1000);
		return result;
	}

	public class UserInfo {
		public int age;
		public int weight;
		public int distance;

		public UserInfo() {
			this.age = 20;
			this.weight = 75000;
			this.distance = 50;
		}
	}
}
