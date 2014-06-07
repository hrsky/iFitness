package com.example.core;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CalorieSQLite extends SQLiteOpenHelper {
	public final String TABLE_CALORIE_NOTE="calorie_note";
	public final String TABLE_SYSTEM_CONFIGER="system_configer";
	
	
	public final String FIELD_SYSTEM_NAME="configername";
	public final String FIELD_SYSTEM_VALUE="configerdata";
	public final String FIELD_NOTE_FREQUENCY="everyday_frequency_count";
	public final String FIELD_NOTE_CALORIE="everyday_caluli_count";
	public final String FIELD_NOTE_UPDATE="updatedate";
	public final String FIELD_SYSTEM_USER_WEIGHT="user_weight";
	
	public CalorieSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql="create table if not exists "+TABLE_CALORIE_NOTE+" ("
			+ "id integer primary key autoincrement,"
			+ "iconindex integer default 0,"
			+ FIELD_NOTE_FREQUENCY + " integer,"
			+ FIELD_NOTE_CALORIE + " integer,"
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
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_NOTE_FREQUENCY, userinfo.calorieInfo.getFrequency());
		values.put(FIELD_NOTE_CALORIE, userinfo.calorieInfo.getCalorie());
		
		int rows=writdb.update(TABLE_CALORIE_NOTE, values, FIELD_NOTE_UPDATE+"='"+getNowDate()+"'", null);
		writdb.close();

		if (rows<=0)	
		{
			InsertFrequency(userinfo);
		}
	}
	
	public void InsertFrequency(UserInfo userInfo)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_NOTE_FREQUENCY, userInfo.calorieInfo.getFrequency());
		values.put(FIELD_NOTE_CALORIE, userInfo.calorieInfo.getCalorie());
		
		values.put(FIELD_NOTE_UPDATE,getNowDate());
		db.insert(TABLE_CALORIE_NOTE, null, values);
		db.close();
	}
	
	public void UpdateSysData(String name,String value)
	{
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

	public void InsertSysData(String name,String value)
	{
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_SYSTEM_NAME, name);
		values.put(FIELD_SYSTEM_VALUE, value);
		writdb.insert(TABLE_SYSTEM_CONFIGER, null, values);
		writdb.close();
	}

	public String GetSysData(String name)
	{
		String resault="";
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SYSTEM_CONFIGER, null, 
				FIELD_SYSTEM_NAME+"='"+name+"'", null, null, null, null);

		int valueIndex = cursor.getColumnIndex(FIELD_SYSTEM_VALUE);
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			resault=cursor.getString(valueIndex);
		}
		cursor.close();
		db.close();
		
		return resault;
	}

	public Boolean GetCalorieRunState()
	{
		String resault=this.GetSysData("calore_run");
		if(resault=="0")
			return false;
		return true;
	}

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

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, 
				FIELD_NOTE_UPDATE+"='"+getNowDate()+"'", null, null, null, "id asc");

		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);

		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			userInfo.calorieInfo.setFrequency(cursor.getInt(frequencyIndex));
			userInfo.calorieInfo.setCalorie(cursor.getInt(calorieIndex));
		}
		cursor.close();
		db.close();

		String userDistance=this.GetSysData("distance");
		if(userDistance!="")
			userInfo.Distance=Integer.parseInt(userDistance);
		String userWeight=this.GetSysData("weight");
		if(userWeight!="")
			userInfo.Weight=Integer.parseInt(userWeight);
		return userInfo;
	}
	

//	public void LoadNoteList(NoteAdapter adapter)
//	{
//
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, null, null, null, null, FIELD_NOTE_UPDATE+" desc");
//
//		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);
//
//		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
//
//		int dayIndex = cursor.getColumnIndex(FIELD_NOTE_UPDATE);
//		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
//			NoteItem item =new NoteItem();
//			item.Frequency=cursor.getInt(frequencyIndex);
//			item.Calorie=cursor.getInt(calorieIndex);
//			item.NoteDate=cursor.getString(dayIndex);
//			adapter.AddNewItem(item);
//		}
//		cursor.close();
//		db.close();
//	}

	public String getNowDate()
	{
		String resault="";
		Calendar c = Calendar.getInstance();
		resault = c.get(Calendar.YEAR)+""; 
		resault = resault+"-"+(c.get(Calendar.MONTH)+1);
		resault = resault+"-"+c.get(Calendar.DAY_OF_MONTH);
		return resault;
	}
}
