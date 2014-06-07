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
	
	
	public final String FIELD_SYSTEM_NAME="configername";//系统配置表名
	public final String FIELD_SYSTEM_VALUE="configerdata";//系统配置表数据
	public final String FIELD_NOTE_FREQUENCY="everyday_frequency_count";//note表步数字段
	public final String FIELD_NOTE_CALORIE="everyday_caluli_count";//note表卡路里字段
	public final String FIELD_NOTE_TIME="time_count";//note表更新时长
	public final String FIELD_NOTE_UPDATE="updatedate";//note表更新日期字段
	public final String FIELD_SYSTEM_USER_WEIGHT="user_weight";//系统配置表用户体重字段
	
	public CalorieSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建日记表
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
		//获取数据库对象
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_NOTE_FREQUENCY, userinfo.calorieInfo.getFrequency());
		values.put(FIELD_NOTE_CALORIE, userinfo.calorieInfo.getCalorie());
		values.put(FIELD_NOTE_TIME, userinfo.calorieInfo.getTime());
		
		int rows=writdb.update(TABLE_CALORIE_NOTE, values, FIELD_NOTE_UPDATE+"='"+getNowDate()+"-v"+userinfo.calorieInfo.num+"'", null);
		//Log.e("更新的行数", rows+"");
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
			//使用insert方法向表中插入数据
			ContentValues values = new ContentValues();
			values.put(FIELD_NOTE_FREQUENCY, userInfo.calorieInfo.getFrequency());
			values.put(FIELD_NOTE_CALORIE, userInfo.calorieInfo.getCalorie());
			values.put(FIELD_NOTE_TIME, userInfo.calorieInfo.getTime());
			
			values.put(FIELD_NOTE_UPDATE,getNowDate()+"-v"+userInfo.calorieInfo.num);
			db.insert(TABLE_CALORIE_NOTE, null, values);
			
			db.close();
		}
	}
	
	//向系统配置中更新数据
	public void UpdateSysData(String name,String value)
	{
		//获取数据库对象
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
	//向系统配置表中添加数据
	public void InsertSysData(String name,String value)
	{
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_SYSTEM_NAME, name);
		values.put(FIELD_SYSTEM_VALUE, value);
		writdb.insert(TABLE_SYSTEM_CONFIGER, null, values);
		writdb.close();
	}
	//获得系统配置表中的数据
	public String GetSysData(String name)
	{
		String resault="";
		//获得数据库对象
		SQLiteDatabase db = this.getReadableDatabase();
		//查询表中的数据
		Cursor cursor = db.query(TABLE_SYSTEM_CONFIGER, null, 
				FIELD_SYSTEM_NAME+"='"+name+"'", null, null, null, null);
		//获取name列的索引
		int valueIndex = cursor.getColumnIndex(FIELD_SYSTEM_VALUE);
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			resault=cursor.getString(valueIndex);
		}
		cursor.close();//关闭结果集
		db.close();//关闭数据库对象
		
		return resault;
	}
	//获得运行状态
	public Boolean GetCalorieRunState()
	{
		String resault=this.GetSysData("calore_run");
		if(resault=="0")
			return false;
		return true;
	}
	//获得上次记录的运行状态
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
		//获得数据库对象
		SQLiteDatabase db = this.getReadableDatabase();
		//查询表中的数据
		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, 
				FIELD_NOTE_UPDATE+"='"+getNowDate()+"'", null, null, null, "id asc");
		//获取name列的索引
		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);
		//获取level列的索引
		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			userInfo.calorieInfo.setFrequency(cursor.getInt(frequencyIndex));
			userInfo.calorieInfo.setCalorie(cursor.getInt(calorieIndex));
		}
		cursor.close();//关闭结果集
		db.close();//关闭数据库对象

		String userDistance=this.GetSysData("distance");
		if(userDistance!="")
			userInfo.Distance=Integer.parseInt(userDistance);
		String userWeight=this.GetSysData("weight");
		if(userWeight!="")
			userInfo.Weight=Integer.parseInt(userWeight);
		return userInfo;
	}
	
	//加载日志列表
	public void LoadNoteList(NoteAdapter adapter)
	{
		//获得数据库对象
		SQLiteDatabase db = this.getReadableDatabase();
		//查询表中的数据
		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, null, null, null, null, FIELD_NOTE_UPDATE+" desc");
		//获取时长
		int cTime = cursor.getColumnIndex(FIELD_NOTE_TIME);
		//获取name列的索引
		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);
		//获取level列的索引
		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
		//获取level列的索引
		int dayIndex = cursor.getColumnIndex(FIELD_NOTE_UPDATE);
		
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			NoteItem item =new NoteItem();
			item.Frequency=cursor.getInt(frequencyIndex);
			item.Calorie=cursor.getInt(calorieIndex);
			item.NoteDate=cursor.getString(dayIndex);
			item.tString=cursor.getString(cTime);
			adapter.AddNewItem(item);
		}
		cursor.close();//关闭结果集
		db.close();//关闭数据库对象
	}
	//获得当前日期
	public String getNowDate()
	{
		String resault="";
		Calendar c = Calendar.getInstance();
		resault = c.get(Calendar.YEAR)+""; //获取当前年份
		resault = resault+"-"+(c.get(Calendar.MONTH)+1);//获取当前月份
		resault = resault+"-"+c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
		return resault;
	}
}
