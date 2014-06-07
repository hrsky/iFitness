package com.example.core;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class CalorieManager extends Service{
	
	public static String ACTION_DESKTOP_CLICK_PLAY="action_desktop_click_play";
	
	public static UserInfo userInfo;
	public static int UpdateWidgetMillisecond;
	public static int UpdateSQLiteMillisecond;
	public static CalorieSQLite SQLite;
	private SensorManager mSensorMgr;
	private CalorieListener mCalorieListener;
	private static boolean mIsPlayCalorieListener;
	
	private Thread mUpdateSQLiteThread;
	@Override
	public void onCreate() {
		
		SQLInit();
		UserInit();
		RegCalorieListenerInit();
		BroadcastReceiverInit();
		ManagerInit();
		super.onCreate();
	}
	
	private BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(ACTION_DESKTOP_CLICK_PLAY))
			{
			//	RemoteViews remoteViews= new RemoteViews(context.getPackageName(),R.layout.desktop1_2);
				if(IsRun())
				{
					StopCalc();
				}
				else {
					StartCalc();
				}
			}
		}
		
	};
	
	/*public static void AddDesktop(DesktopInfo desktopInfo)
	{
		AddDesktop(desktopInfo);
	}*/
	
	private void BroadcastReceiverInit()
	{
  		IntentFilter intentFilter=new IntentFilter();
  		intentFilter.addAction(ACTION_DESKTOP_CLICK_PLAY);
  		registerReceiver(mBroadcastReceiver, intentFilter);
	}
	private void ManagerInit()
	{
		UpdateSQLiteMillisecond=60000;
		UpdateWidgetMillisecond=10000;
		
		mUpdateSQLiteThread=new Thread(mUpdateSQLRunnable);
		mUpdateSQLiteThread.start();
	}
	private void UserInit()
	{
		if(userInfo==null)
			userInfo = new UserInfo();
	}
	private void RegCalorieListenerInit()
	{
		mCalorieListener=new CalorieListener(userInfo.calorieInfo);
		mSensorMgr = (SensorManager)this.getSystemService(android.content.Context.SENSOR_SERVICE );
		this.StartCalc();
	}
	private void SQLInit()
	{
		SQLite=new CalorieSQLite(this, "yucalorie.db", null, 1);
		CalorieManager.userInfo=SQLite.GetUserInfo();
		mIsPlayCalorieListener=SQLite.GetCalorieRunState();
	}
	
	public static boolean IsRun()
	{
		return mIsPlayCalorieListener;
	}
	
	public void StopCalc()
	{
		mIsPlayCalorieListener=false;
		if(mSensorMgr!=null && mCalorieListener!=null)
		{
			Sensor sensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorMgr.unregisterListener(mCalorieListener,sensor);
		}
		SQLite.SetCalorieRunState(false);
		/*try {
			mUpdateSQLiteThread.stop();
			mUpdateDesktopThread.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}*/
	}
	public void StartCalc()
	{
		mIsPlayCalorieListener=true;
		if(mSensorMgr!=null && mCalorieListener!=null)
		{
			Sensor sensor = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			mSensorMgr.registerListener(mCalorieListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		}
		SQLite.SetCalorieRunState(true);
	}
	
	private	Handler mUpdateSQLHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case 1:   
                	  userInfo.calorieInfo.TodayInit();
                      break;   
             }   
             super.handleMessage(msg);   
        }   
    }; 

    private  Runnable  mUpdateSQLRunnable = new Runnable() {   
       public void run() {
            while (true) { 
            	SQLite.UpdateFrequency(CalorieManager.userInfo);
            	if(!userInfo.calorieInfo.IsToday())
            	{
            		Message message = new Message();   
                    message.what = 1;
                    mUpdateSQLHandler.sendMessage(message);  
            	}
            	try {   
                    Thread.sleep(CalorieManager.UpdateSQLiteMillisecond);
               } catch (InterruptedException e) {   
                    Thread.currentThread().interrupt();   
               }  
            }   
       }   
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
