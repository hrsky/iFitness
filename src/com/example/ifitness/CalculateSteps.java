package com.example.ifitness;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.ifitness.calcCalorie.CalorieInfo;
import com.example.ifitness.calcCalorie.CalorieService;
import com.example.ifitness.calcCalorie.CalorieService.CalorieServiceBinder;
import com.example.ifitness.calcCalorie.OnCalorieChangeListener;

public class CalculateSteps extends Activity{
	private Button calStepStart;
	private TextView calSteps_numOfSteps;
	private TextView calSteps_time;
	private TextView calSteps_calorie;
	private ServiceConnection conn;
	private CalorieService service;
	private OnCalorieChangeListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_calculate_steps);
		
		calStepStart = (Button) findViewById(R.id.calSteps_start);
		calSteps_time  = (TextView) findViewById(R.id.calSteps_time);
		calSteps_calorie=(TextView)this.findViewById(R.id.calSteps_calorie);
		calSteps_numOfSteps=(TextView)this.findViewById(R.id.calSteps_numOfSteps);
		
		calSteps_numOfSteps.setText(0+"");
		calSteps_time.setText(0+"");
		calSteps_calorie.setText(0+"");
		
		listener = new OnCalorieChangeListener() {
			
			@Override
			public void onStepsChange(CalorieInfo calorieInfo) {
				calSteps_numOfSteps.setText(String.valueOf(calorieInfo.getSteps()));
			}
			
			@Override
			public void onCalorieChange(CalorieInfo calorieInfo) {
				calSteps_calorie.setText(String.valueOf(calorieInfo.getCalorie()));
			}

			@Override
			public void onTimeChange(CalorieInfo calorieInfo) {
				calSteps_time.setText(calorieInfo.getTime());
			}
		};
		
		conn = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				service.removeCalorieChangeListener(listener);
				service = null;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder serv) {
				CalorieService.CalorieServiceBinder binder = (CalorieServiceBinder)serv;
				service = binder.getService();
				service.addOnCalorieChangeListener(listener);
				
				if (service.IsRun()){
					calStepStart.setText("暂停");
					calSteps_numOfSteps.setText(String.valueOf(service.getSteps()));
					calSteps_calorie.setText(String.valueOf(service.getCalorie()));
					calSteps_time.setText(service.getTime());
				} else {
					calStepStart.setText("开始");
					service.setSteps(0);
					service.setCalorie(0);
					calSteps_time.setText("00:00");
				}
			}
		};
		
		calStepStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (service != null) {
					if (service.IsRun()) {
						service.StopCalc();
						calStepStart.setText("开始");
					} else {
						service.setSteps(0);
						service.setCalorie(0);
						calSteps_time.setText("00:00");
						service.StartCalc();
						calStepStart.setText("暂停");
					}
				}
			}
		});
		
		Intent bintent = new Intent(this, CalorieService.class);
		bindService(bintent, conn, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		unbindService(conn);
		super.onDestroy();
	}
}
