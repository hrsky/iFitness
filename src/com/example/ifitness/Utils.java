package com.example.ifitness;

import java.text.SimpleDateFormat;

public class Utils {
	public static String getCurrentDate(){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String str = formatter.format(new java.util.Date());
		return str;
	}
}
