package com.example.core;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import android.util.Log;

public class CalorieInfo {
	public String Day;
	
	private int mFrequency;
	private int mCalorie;
	private long mWalkingDistance;
	private UserInfo mUserInfo;
	
	private Vector mRepository;
	private OnCalorieEventListener mOnCalorieEventListener;
	
	public CalorieInfo(UserInfo user)
	{
		this.mUserInfo=user;
		TodayInit();
		mRepository=new Vector();
	}

	public void TodayInit()
	{
		this.Day=getNowDate();
		setFrequency(0);
	}
	public boolean IsToday()
	{
		String nowDateString=this.getNowDate();
		if(this.Day.equals(nowDateString))
		{
			return true;
		}
		return false;
	}
	

	public void setOnCalorieEventListener(OnCalorieEventListener listener)
	{
		mRepository.addElement(listener);
	}

	public int getFrequency()
	{
		return this.mFrequency;
	}

	public void setFrequency(int frequency)
	{
		int oldFrequency=this.mFrequency;
		this.mFrequency=frequency;
		if(oldFrequency!=frequency)
		{
			Enumeration enume=mRepository.elements();
			while(enume.hasMoreElements())
			{
				mOnCalorieEventListener=(OnCalorieEventListener)enume.nextElement();
				mOnCalorieEventListener.onFrequencyChange(this);
			}
			
			int calorie=CalcCalorie(frequency);
			this.setCalorie(calorie);
		}
	}
	public int getCalorie()
	{
		return this.mCalorie;
	}
	public void setCalorie(int calorie)
	{
		int oldCalorie=this.mCalorie;
		this.mCalorie=calorie;
		if(oldCalorie!=calorie)
		{
			Enumeration enume=mRepository.elements();
			while(enume.hasMoreElements())
			{
				mOnCalorieEventListener=(OnCalorieEventListener)enume.nextElement();
				mOnCalorieEventListener.onCalorieChange(this);
			}
		}
	}
	
	private int CalcCalorie(int frequency)
	{
		int resault=(int)((float)mUserInfo.Weight *(float)((float)frequency*(float)mUserInfo.Distance/100000)*1.036/1000);
		return resault;
	}
	
	public static String getNowDate()
	{
		String resault="";
		Calendar c = Calendar.getInstance();
		resault = c.get(Calendar.YEAR)+""; 
		resault = resault+"-"+(c.get(Calendar.MONTH)+1);
		resault = resault+"-"+c.get(Calendar.DAY_OF_MONTH);
		return resault;
	}
}
