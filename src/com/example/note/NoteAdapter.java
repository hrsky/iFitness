package com.example.note;

import java.util.ArrayList;
import java.util.List;

import com.example.ifitness.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
	List<NoteItem> mItems;
	private LayoutInflater mInflater;
	public NoteAdapter(Context context)
	{
		mItems=new ArrayList<NoteItem>();
		this.mInflater = LayoutInflater.from(context);
		//JiBuServiceManager.jiBuSQLite.LoadNote(this);//从数据库读取数据日记
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1==null)
			arg1=this.mInflater.inflate(R.layout.note_item, null);
		
		TextView todayTextView=(TextView)arg1.findViewById(R.id.noteitem_day);
		todayTextView.setText(this.mItems.get(arg0).NoteDate);
		
		TextView buTextView=(TextView)arg1.findViewById(R.id.noteitem_bufunumber);
		buTextView.setText(this.mItems.get(arg0).Frequency+"");
		
		TextView calorieTextView=(TextView)arg1.findViewById(R.id.noteitem_calorienumber);
		calorieTextView.setText(this.mItems.get(arg0).Calorie+"");
		
		TextView TimeTextView=(TextView)arg1.findViewById(R.id.noteitem_time);
		TimeTextView.setText(this.mItems.get(arg0).tString+"");
		
		TextView textTextView=(TextView)arg1.findViewById(R.id.noteitem_text);
		textTextView.setText(this.mItems.get(arg0).NoteText+"");
		if (this.mItems.get(arg0).NoteText=="" && this.mItems.get(arg0).NoteText==null)
			textTextView.setVisibility(View.INVISIBLE);
		else {
			textTextView.setVisibility(View.VISIBLE);
		}
		return arg1;
	}
	
	public void AddNewItem(NoteItem note)
	{
		this.mItems.add(note);
	}
}
