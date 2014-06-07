package com.example.ifitness.note;

import java.io.Serializable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NoteItem implements Serializable {

	private static final long serialVersionUID = 1L;//SerializableĬ�ϰ汾
	public int Id=0;
	public String NoteDate="";//�ռ�����
	public int Frequency=0;//����
	public int Calorie=0;//��·��
	public int IconIndex=0;//����ͼ��
	public int Weight=0;//����
	public String tString="";
	public String NoteText="";//�ռ�
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
