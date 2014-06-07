package com.example.ifitness;

import java.util.Calendar;

import com.example.ifitness.calcCalorie.CalorieService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView personInfo;
	private Button calculateSteps;
	private Button makePlans;
	private Button record;
	private Button healthIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent intent = new Intent(this, CalorieService.class);
		startService(intent);
		
		personInfo = (TextView) findViewById(R.id.personInfo);
		calculateSteps = (Button) findViewById(R.id.calculateSteps);
		makePlans = (Button) findViewById(R.id.makePlans);
		record = (Button) findViewById(R.id.record);
		healthIndex = (Button) findViewById(R.id.health_index);
		
		calculateSteps.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CalculateSteps.class);
				startActivity(intent);
			}
		});
		
		makePlans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MakePlans.class);
				startActivity(intent);
			}
		});
		
		healthIndex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HealthIndex.class);
				startActivity(intent);
			}
		});

		record.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, NoteListActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String str = Utils.getCurrentDate();
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 12 && hour >= 8){
			str += "，早上好！";
		}else if (hour >=12 && hour < 18){
			str += "，中午好！";
		}else{
			str += "，晚上好！";
		}
		personInfo.setText(str);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
