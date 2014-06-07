package com.example.core;

public class UserInfo {
	public int Age;
	public int Weight;
	public int Distance;
	public CalorieInfo calorieInfo;
	public UserInfo()
	{
		this.calorieInfo=new CalorieInfo(this);
		this.Weight=75000;
		this.Distance=50;
	}
}
