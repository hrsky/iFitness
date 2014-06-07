package com.example.ifitness.calcCalorie;

import java.util.Calendar;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.ifitness.CalorieSQLite;

public class CalorieService extends Service {
	public static int UpdateSQLiteMillisecond;
	public static String SHAREDPREFERENCES_NAME = "CALORIE_SERVICE";
	public static CalorieSQLite SQLite;
	
	private IBinder mBinder = new CalorieServiceBinder();
	public CalorieInfo mCalorieInfo;
	private SensorManager mSensorMgr;
	private SensorListener mSensorListener;
	private boolean mIsRunning;
	private Thread mUpdateSQLiteThread;
	
	@Override
	public void onCreate() {
		SQLInit();
		UserInit();
		RegCalorieListenerInit();
		ManagerInit();
		super.onCreate();
	}

	private void ManagerInit() {
		UpdateSQLiteMillisecond = 1000;

		mUpdateSQLiteThread = new Thread(mUpdateSQLRunnable);
		mUpdateSQLiteThread.start();
	}

	private void UserInit() {
		if (mCalorieInfo == null)
			mCalorieInfo = new CalorieInfo();
	}

	private void RegCalorieListenerInit() {
		mSensorListener = new SensorListener(mCalorieInfo);
		mSensorMgr = (SensorManager) this
				.getSystemService(android.content.Context.SENSOR_SERVICE);
	}

	private void SQLInit() {
		SQLite = new CalorieSQLite(this, "ifitness.db", null, 1);
		mCalorieInfo = SQLite.GetUserInfo();
		mIsRunning = false;
		SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		mCalorieInfo.setNum(sp.getInt("num", 0));
	}

	public boolean IsRun() {
		return mIsRunning;
	}

	public void StopCalc() {
		
		mIsRunning = false;
		if (mSensorMgr != null && mSensorListener != null) {
			Sensor sensor = mSensorMgr
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorMgr.unregisterListener(mSensorListener, sensor);
		}
//		 SQLite.SetCalorieRunState(false);
		 
	}

	public void StartCalc() {
		addNum();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		setTime(calendar);
		mIsRunning = true;
		if (mSensorMgr != null && mSensorListener != null) {
			Sensor sensor = mSensorMgr
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorMgr.registerListener(mSensorListener, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}
//		 SQLite.SetCalorieRunState(true);
	}

	private Handler mUpdateSQLHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mCalorieInfo.DateInit();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private Runnable mUpdateSQLRunnable = new Runnable() {
		public void run() {
			while (true) {
				SQLite.UpdateFrequency(mCalorieInfo);
				if (!mCalorieInfo.IsToday()) {
					Message message = new Message();
					message.what = 1;
					mUpdateSQLHandler.sendMessage(message);
				}
				try {
					Thread.sleep(CalorieService.UpdateSQLiteMillisecond);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	};

	
	public int getSteps(){
		return mCalorieInfo.getSteps();
	}
	
	public void setSteps(int s){
		mCalorieInfo.setSteps(s);
	}
	
	public int getCalorie(){
		return mCalorieInfo.getCalorie();
	}
	
	public void setCalorie(int ca){
		mCalorieInfo.setCalorie(ca);
	}
	
	public String getTime(){
		return mCalorieInfo.getTime();
	}
	
	public void setTime(Calendar c){
		mCalorieInfo.setTime(c);
	}
	
	public void addNum(){
		mCalorieInfo.addNum();
	}
	
	public void addOnCalorieChangeListener(OnCalorieChangeListener listener) {
		mCalorieInfo.addOnCalorieChangeListener(listener);
	}

	public void removeCalorieChangeListener(OnCalorieChangeListener listener) {
		mCalorieInfo.removeCalorieChangeListener(listener);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onDestroy() {
		SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("num", mCalorieInfo.getNum());
		editor.commit();
		super.onDestroy();
	}

	public class CalorieServiceBinder extends Binder {
		public CalorieService getService() {
			return CalorieService.this;
		}
	}
}
