package com.example.ifitness;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MakePlans extends Activity {
	private LinearLayout layout;
	private TextView tv_date;
	private EditText et_minutes;
	private TextView et_minutes_done;
	private TextView et_kilometers_done;
	private Button btn_new;
	private Button btn_save;
	private Button btn_edit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_make_plans);
		
		layout = (LinearLayout) findViewById(R.id.makePlans_plan);
		tv_date = (TextView) findViewById(R.id.makePlans_date);
		et_minutes = (EditText) findViewById(R.id.makePlans_minutes);
		et_kilometers_done = (TextView) findViewById(R.id.makePlans_kilometer_ran);
		et_minutes_done = (TextView) findViewById(R.id.makePlans_minutes_ran);
		btn_new = (Button) findViewById(R.id.makePlans_new);
		btn_save = (Button) findViewById(R.id.makePlans_save);
		btn_edit = (Button) findViewById(R.id.makePlans_edit);
		
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
				if (et_minutes.getText().toString().matches("[0-9]+")){
					//TODO
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

}
