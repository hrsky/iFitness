package com.example.ifitness.note;

import java.io.Serializable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NoteItem implements Serializable {

	private static final long serialVersionUID = 1L;//Serializable默认版本
	public int Id=0;
	public String NoteDate="";//日记日期
	public int Frequency=0;//步数
	public int Calorie=0;//卡路里
	public int IconIndex=0;//心情图标
	public int Weight=0;//体重
	public String tString="";
	public String NoteText="";//日记
	public Bitmap getIcon()
	{
		Bitmap bmp=null;
		switch(IconIndex)
		{
			case 1:
				bmp = BitmapFactory.decodeResource(Resources.getSystem(),1); 
				break;
			case 2:
				bmp = BitmapFactory.decodeResource(Resources.getSystem(),1); 
		}
		return bmp;
	}
}
