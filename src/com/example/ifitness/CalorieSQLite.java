package com.example.ifitness;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ifitness.calcCalorie.CalorieInfo;
import com.example.ifitness.note.NoteAdapter;
import com.example.ifitness.note.NoteItem;

public class CalorieSQLite extends SQLiteOpenHelper {
	public final String TABLE_SYSTEM_CONFIGER="system_configer";
	public final String FIELD_SYSTEM_NAME="configername";//ϵͳ���ñ���
	public final String FIELD_SYSTEM_VALUE="configerdata";//ϵͳ���ñ�����
	public final String FIELD_SYSTEM_USER_WEIGHT="user_weight";//ϵͳ���ñ��û������ֶ�

	public final String TABLE_CALORIE_NOTE="calorie_note1";
	public final String FIELD_NOTE_FREQUENCY="everyday_frequency_count";//note�����ֶ�
	public final String FIELD_NOTE_CALORIE="everyday_caluli_count";//note��·���ֶ�
	public final String FIELD_NOTE_TIME="time_count";//note�����ʱ��
	public final String FIELD_NOTE_UPDATE="updatedate";//note����������ֶ�
	
	public CalorieSQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
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

	public void UpdateFrequency(CalorieInfo calorieInfo)
	{
		//��ȡ���ݿ����
		SQLiteDatabase writdb = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(FIELD_NOTE_FREQUENCY, calorieInfo.getSteps());
		values.put(FIELD_NOTE_CALORIE, calorieInfo.getCalorie());
		values.put(FIELD_NOTE_TIME, calorieInfo.getTime());
		
		int rows=writdb.update(TABLE_CALORIE_NOTE, values, FIELD_NOTE_UPDATE+"='"+Utils.getCurrentDate()+"-v"+calorieInfo.getNum()+"'", null);
		//Log.e("���µ�����", rows+"");
		writdb.close();

		if (rows<=0)	
		{
			InsertFrequency(calorieInfo);
		}
	}
	
	public void InsertFrequency(CalorieInfo calorieInfo)
	{
		if(calorieInfo.getNum()!=0)
		{
			SQLiteDatabase db = this.getWritableDatabase();
			//ʹ��insert��������в�������
			ContentValues values = new ContentValues();
			values.put(FIELD_NOTE_FREQUENCY, calorieInfo.getSteps());
			values.put(FIELD_NOTE_CALORIE, calorieInfo.getCalorie());
			values.put(FIELD_NOTE_TIME, calorieInfo.getTime());
			
			values.put(FIELD_NOTE_UPDATE,Utils.getCurrentDate()+"-v"+calorieInfo.getNum());
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
	
	public CalorieInfo GetUserInfo()
	{
		CalorieInfo calorieInfo=new CalorieInfo();
		//������ݿ����
		SQLiteDatabase db = this.getReadableDatabase();
		//��ѯ���е�����
		Cursor cursor = db.query(TABLE_CALORIE_NOTE, null, 
				FIELD_NOTE_UPDATE+"='"+Utils.getCurrentDate()+"'", null, null, null, "id asc");
		Cursor ct = db.query(TABLE_CALORIE_NOTE,null,
				FIELD_NOTE_UPDATE+" like '%"+Utils.getCurrentDate()+"%'",null,null,null,null);
		
		//��ȡʱ��
		int cTime = cursor.getColumnIndex(FIELD_NOTE_TIME);
		//��ȡ����������
		int frequencyIndex = cursor.getColumnIndex(FIELD_NOTE_FREQUENCY);
		//��ȡlevel�е�����
		int calorieIndex = cursor.getColumnIndex(FIELD_NOTE_CALORIE);
		for (ct.moveToFirst();!(ct.isAfterLast());ct.moveToNext()) {
			calorieInfo.t_calories+=ct.getInt(calorieIndex);
			calorieInfo.t_km+=ct.getInt(frequencyIndex)*calorieInfo.mUserInfo.distance;
			String str = ct.getString(cTime);
			String[] temp = str.split(":");
			calorieInfo.t_mimutes += Integer.parseInt(temp[0])*60 + Integer.parseInt(temp[1]);
		}
		calorieInfo.t_km/=10000;
		calorieInfo.t_mimutes/=60;
		
		for (cursor.moveToFirst();!(cursor.isAfterLast());cursor.moveToNext()) {
			calorieInfo.setSteps(cursor.getInt(frequencyIndex));
			calorieInfo.setCalorie(cursor.getInt(calorieIndex));
		}
		cursor.close();//�رս����
		db.close();//�ر����ݿ����

		String userDistance=this.GetSysData("distance");
		if(userDistance!="")
			calorieInfo.mUserInfo.distance=Integer.parseInt(userDistance);
		String userWeight=this.GetSysData("weight");
		if(userWeight!="")
			calorieInfo.mUserInfo.weight=Integer.parseInt(userWeight);
		return calorieInfo;
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
//	public String getNowDate()
//	{
//		String resault="";
//		Calendar c = Calendar.getInstance();
//		resault = c.get(Calendar.YEAR)+""; //��ȡ��ǰ���
//		resault = resault+"-"+(c.get(Calendar.MONTH)+1);//��ȡ��ǰ�·�
//		resault = resault+"-"+c.get(Calendar.DAY_OF_MONTH);//��ȡ��ǰ�·ݵ����ں���
//		return resault;
//	}
}
