package com.example.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.note.NoteAdapter;
import com.example.note.NoteItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CalorieSQLite extends SQLiteOpenHelper {
	public final String TABLE_CALORIE_NOTE="calorie_note1";
	public final String TABLE_SYSTEM_CONFIGER="system_configer";
	
	
	public final String FIELD_SYSTEM_NAME="configername";//ϵͳ���ñ���
	public final String FIELD_SYSTEM_VALUE="configerdata";//ϵͳ���ñ�����
	public final String FIELD_NOTE_FREQUENCY="everyday_frequency_count";//note�����ֶ�
	public final String FIELD_NOTE_CALORIE="everyday_caluli_count";//note��·���ֶ�
	public final String FIELD_NOTE_TIME="time_count";//note�����ʱ��
	public final String FIELD_NOTE_UPDATE="updatedate";//note����������ֶ�
	public final String FIELD_SYSTEM_USER_WEIGHT="user_weight";//ϵͳ���ñ��û������ֶ�
	
	public CalorieSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//�����ռǱ�
		String sql="create table if not exists "+TABLE_CALORIE_NOTE+" ("
			+ "id integer primary key autoincrement,"
			+ "iconindex integer default 0,"
			+ FIELD_NOTE_FREQUENCY + " integer,"
			+ FIELD_NOTE_CALORIE + " integer,"
			+ FIELD_NOTE_TIME + " integer,"
			+ "note text,"
			+ FIELD_NOTE_UPDATE + " date default (datetime('now','localtime')))";
		db.execSQL(sql);
		
		sql="create table if not exists "+TABLE_SYSTEM_CONFIGER+" ("
			+ "id integer primary key autoincrement,"
			+ "configername nvarchar(64),"
			+ "configerdata nvarchar(64),"
			+ "updatedate date default (datetime('now','localtime')))";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public void UpdateFrequency(UserInfo userinfo)
	{
		//��ȡ���ݿ����
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_NOTE_FREQUENCY, userinfo.calorieInfo.getFrequency());
		values.put(FIELD_NOTE_CALORIE, userinfo.calorieInfo.getCalorie());
		values.put(FIELD_NOTE_TIME, userinfo.calorieInfo.getTime());
		
		int rows=writdb.update(TABLE_CALORIE_NOTE, values, FIELD_NOTE_UPDATE+"='"+getNowDate()+"-v"+userinfo.calorieInfo.num+"'", null);
		//Log.e("���µ�����", rows+"");
		writdb.close();

		if (rows<=0)	
		{
			InsertFrequency(userinfo);
		}
	}
	
	public void InsertFrequency(UserInfo userInfo)
	{
		if(userInfo.calorieInfo.num!=0)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			//ʹ��insert��������в�������
			ContentValues values = new ContentValues();
			values.put(FIELD_NOTE_FREQUENCY, userInfo.calorieInfo.getFrequency());
			values.put(FIELD_NOTE_CALORIE, userInfo.calorieInfo.getCalorie());
			values.put(FIELD_NOTE_TIME, userInfo.calorieInfo.getTime());
			
			values.put(FIELD_NOTE_UPDATE,getNowDate()+"-v"+userInfo.calorieInfo.num);
			db.insert(TABLE_CALORIE_NOTE, null, values);
			
			db.close();
		}
	}
	
	//��ϵͳ�����и�������
	public void UpdateSysData(String name,String value)
	{
		//��ȡ���ݿ����
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_SYSTEM_NAME, name);
		values.put(FIELD_SYSTEM_VALUE, value);
		int rows=writdb.update(TABLE_SYSTEM_CONFIGER, values, FIELD_SYSTEM_NAME+"='"+name+"'", null);
		writdb.close();

		if (rows<=0)	
		{
			InsertSysData(name,value);
		}
	}
	//��ϵͳ���ñ����������
	public void InsertSysData(String name,String value)
	{
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_SYSTEM_NAME, name);
		values.put(FIELD_SYSTEM_VALUE, value);
		writdb.insert(TABLE_SYSTEM_CONFIGER, null, values);
		writdb.close();
	}
	//���ϵͳ���ñ��е�����
	public String GetSysData(String name)
	{
		String resault="";
		//������ݿ����
		SQLiteDatabase db = this.getReadableDatabase();
		//��ѯ���е�����
		Cursor cursor = db.query(TABLE_SYSTEM_CONFIGER, null, 
				FIELD_SYSTEM_NAME+"='"+name+"'", null, null, null, null);
		//��ȡname�е�����
		int valueIndex = cursor.getColumnIndex(FIELD_SYSTEM_VALUE);
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			resault=cursor.getString(valueIndex);
		}
		cursor.close();//�رս����
		db.close();//�ر����ݿ����
		
		return resault;
	}
	//�������״̬
	public Boolean GetCalorieRunState()
	{
		String resault=this.GetSysData("calore_run");
		if(resault=="0")
			return false;
		return true;
	}
	//����ϴμ�¼������״̬
	public void SetCalorieRunState(Boolean isRun)
	{
		if(isRun)
			this.UpdateSysData("calore_run", "1");
		else {
			this.UpdateSysData("calore_run", "0");
		}
	}
	
	public UserInfo GetUserInfo()
	{
		UserInfo userInfo=new UserInfo();
		//������ݿ����
		SQLiteDatabase db = this.getReadableDatabase();
		//��ѯ���е�����
		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, 
				FIELD_NOTE_UPDATE+"='"+getNowDate()+"'", null, null, null, "id asc");
		//��ȡname�е�����
		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);
		//��ȡlevel�е�����
		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			userInfo.calorieInfo.setFrequency(cursor.getInt(frequencyIndex));
			userInfo.calorieInfo.setCalorie(cursor.getInt(calorieIndex));
		}
		cursor.close();//�رս����
		db.close();//�ر����ݿ����

		String userDistance=this.GetSysData("distance");
		if(userDistance!="")
			userInfo.Distance=Integer.parseInt(userDistance);
		String userWeight=this.GetSysData("weight");
		if(userWeight!="")
			userInfo.Weight=Integer.parseInt(userWeight);
		return userInfo;
	}
	
	//������־�б�
	public void LoadNoteList(NoteAdapter adapter)
	{
		//������ݿ����
		SQLiteDatabase db = this.getReadableDatabase();
		//��ѯ���е�����
		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, null, null, null, null, FIELD_NOTE_UPDATE+" desc");
		//��ȡʱ��
		int cTime = cursor.getColumnIndex(FIELD_NOTE_TIME);
		//��ȡname�е�����
		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);
		//��ȡlevel�е�����
		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
		//��ȡlevel�е�����
		int dayIndex = cursor.getColumnIndex(FIELD_NOTE_UPDATE);
		
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			NoteItem item =new NoteItem();
			item.Frequency=cursor.getInt(frequencyIndex);
			item.Calorie=cursor.getInt(calorieIndex);
			item.NoteDate=cursor.getString(dayIndex);
			item.tString=cursor.getString(cTime);
			adapter.AddNewItem(item);
		}
		cursor.close();//�رս����
		db.close();//�ر����ݿ����
	}
	//��õ�ǰ����
	public String getNowDate()
	{
		String resault="";
		Calendar c = Calendar.getInstance();
		resault = c.get(Calendar.YEAR)+""; //��ȡ��ǰ���
		resault = resault+"-"+(c.get(Calendar.MONTH)+1);//��ȡ��ǰ�·�
		resault = resault+"-"+c.get(Calendar.DAY_OF_MONTH);//��ȡ��ǰ�·ݵ����ں���
		return resault;
	}
}
