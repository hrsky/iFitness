package com.example.ifitness;

import java.math.BigDecimal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class HealthIndex extends Activity{
	EditText body_weight;
	EditText body_high;
	EditText body_age;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.health_index);
		Button btn_bmi = (Button)findViewById(R.id.cal_bmi);
		body_weight = (EditText)findViewById(R.id.tool_bim_bodyweight);
		body_high = (EditText)findViewById(R.id.tool_bim_bodyhigh);
		body_high.setText("175");
		body_weight.setText("75");
		btn_bmi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout linearLayout=(LinearLayout)findViewById(R.id.tool_bmi_resultplan);
				linearLayout.setVisibility(View.VISIBLE);
				LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.tool_heart_resultplan);
				linearLayout2.setVisibility(View.GONE);
				
				double height=Double.parseDouble(body_high.getText().toString())/100;
				double weight=Double.parseDouble(body_weight.getText().toString());
				double result= weight/ (height*height);
				BigDecimal bg = new BigDecimal(result); 
				result = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
				String Bmi_result = "";
				if(result < 18.5)
					Bmi_result = "体重偏轻";
				else if(result < 24)
					Bmi_result = "健康体重";
				else if(result < 28)
					Bmi_result = "体重偏胖";
				else 
					Bmi_result = "过于肥胖";
				TextView resultTextView=(TextView)findViewById(R.id.tool_bmi_result);
				resultTextView.setText(result+"("+Bmi_result+")");
			}
		});
		super.onCreate(savedInstanceState);
		
		Button btn_heart = (Button)findViewById(R.id.cal_heart);
		body_age = (EditText)findViewById(R.id.tool_bim_age);
		body_age.setText("20");
		btn_heart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout linearLayout2 = (LinearLayout)findViewById(R.id.tool_heart_resultplan);
				linearLayout2.setVisibility(View.VISIBLE);
				LinearLayout linearLayout=(LinearLayout)findViewById(R.id.tool_bmi_resultplan);
				linearLayout.setVisibility(View.GONE);
				try {
					int age=Integer.parseInt(body_age.getText().toString());
					
					int result1=(int)((220-age)*0.6);
					int result2=(int)((220-age)*0.8);
					
					TextView result1TextView=(TextView)findViewById(R.id.tool_heart_result1);
					result1TextView.setText(result1+"");
					TextView result2TextView=(TextView)findViewById(R.id.tool_heart_result2);
					result2TextView.setText(result2+"");
				} catch (Exception e) {
					Toast.makeText(HealthIndex.this, "请正确输入年龄", 2000);
				}
			}
		});
		
		Button btn_health_explain = (Button)findViewById(R.id.health_explain);
		btn_health_explain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HealthIndex.this, Health_explain.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
