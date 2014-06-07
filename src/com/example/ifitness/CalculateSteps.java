package com.example.ifitness;

import java.util.Calendar;
import java.util.Date;

import com.example.core.CalorieInfo;
import com.example.core.CalorieManager;
import com.example.core.OnCalorieEventListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CalculateSteps extends Activity{
	Button calStepStart;
	TextView calSteps_numOfSteps;
	TextView calSteps_time;
	TextView calSteps_calorie;
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_calculate_steps);
		
		calStepStart = (Button) findViewById(R.id.calSteps_start);
		calSteps_time  = (TextView) findViewById(R.id.calSteps_time);
		calSteps_calorie=(TextView)this.findViewById(R.id.calSteps_calorie);
		calSteps_numOfSteps=(TextView)this.findViewById(R.id.calSteps_numOfSteps);
		if (CalorieManager.IsRun()) {
			calStepStart.setText("��ͣ");
		}
		else {
			calStepStart.setText("��ʼ");
		}
		
		
		calSteps_numOfSteps.setText(0+"");
		calSteps_time.setText("00:00");
		calSteps_calorie.setText(0+"");
		
//		calStepStart.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//		        
//				if (CalorieManager.IsRun()) {
//					calStepStart.setText("��ʼ");
//					stopService(intent);
//				} else {
//					calStepStart.setText("��ͣ");
//					startService(intent);
//				}
//			}
//		});
		
		if (CalorieManager.userInfo==null)  return;
		CalorieManager.userInfo.calorieInfo.setOnCalorieEventListener(new OnCalorieEventListener()
		{
			public void onFrequencyChange(CalorieInfo calorieInfo)
			{
				calSteps_numOfSteps.setText(calorieInfo.getFrequency()+"");
				calSteps_time.setText(calorieInfo.getTime()+"");
			}
			public void onCalorieChange(CalorieInfo calorieInfo)
			{
				calSteps_calorie.setText(calorieInfo.getCalorie()+"");
				
			}
		});
	}
	
}