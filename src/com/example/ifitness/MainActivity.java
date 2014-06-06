package com.example.ifitness;

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
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HealthIndex.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
