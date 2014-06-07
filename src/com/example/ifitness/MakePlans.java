package com.example.ifitness;

import java.text.DecimalFormat;

import com.example.ifitness.calcCalorie.CalorieInfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MakePlans extends Activity {
	public static final String SHAREDPREFERENCES_NAME = "plans";
	private LinearLayout layout;
	private TextView tv_date;
	private EditText et_minutes;
	private TextView et_minutes_done;
	private TextView et_kilometers_done;
	private TextView makePlans_calorie_consumed;
	private Button btn_new;
	private Button btn_save;
	private Button btn_edit;
	public static CalorieSQLite SQLite;
	public CalorieInfo calorieInfo;
	DecimalFormat df;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_make_plans);
		
		df = new DecimalFormat("#.000");
		
		layout = (LinearLayout) findViewById(R.id.makePlans_plan);
		tv_date = (TextView) findViewById(R.id.makePlans_date);
		et_minutes = (EditText) findViewById(R.id.makePlans_minutes);
		et_kilometers_done = (TextView) findViewById(R.id.makePlans_kilometer_ran);
		et_minutes_done = (TextView) findViewById(R.id.makePlans_minutes_ran);
		makePlans_calorie_consumed = (TextView) findViewById(R.id.makePlans_calorie_consumed);
		btn_new = (Button) findViewById(R.id.makePlans_new);
		btn_save = (Button) findViewById(R.id.makePlans_save);
		btn_edit = (Button) findViewById(R.id.makePlans_edit);
		SQLite = new CalorieSQLite(this, "ifitness.db", null, 1);
		calorieInfo=SQLite.GetUserInfo();
		et_kilometers_done.setText(String.valueOf(calorieInfo.t_km));
		et_minutes_done.setText(df.format(calorieInfo.t_mimutes));
		makePlans_calorie_consumed.setText(String.valueOf(calorieInfo.t_calories));
		
		
		btn_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!layout.isShown()){
					layout.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btn_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String minutes = et_minutes.getText().toString();
				if (minutes.matches("[0-9]+")){
					SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("currentDate", Utils.getCurrentDate());
					editor.putString("minutes", minutes);
					editor.commit();
					et_minutes.setEnabled(false);
				}
			}
		});
		
		btn_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!et_minutes.isEnabled()){
					et_minutes.setEnabled(true);
				}
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String str = Utils.getCurrentDate();
		tv_date.setText(str);
		
		SharedPreferences sp = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
		String currentDate = sp.getString("currentDate", "");
		if (!currentDate.equals(Utils.getCurrentDate())){
			layout.setVisibility(View.GONE);
		}else{
			et_minutes.setText(sp.getString("minutes", ""));
			et_minutes.setEnabled(false);
			layout.setVisibility(View.VISIBLE);
		}
	}

}
