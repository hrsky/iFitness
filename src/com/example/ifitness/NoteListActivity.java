package com.example.ifitness;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ifitness.calcCalorie.CalorieService;
import com.example.ifitness.note.NoteAdapter;

public class NoteListActivity extends Activity{

	ListView mListView;
	NoteAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.note_list);
		
		mListView=(ListView)this.findViewById(R.id.note_listview);
		mAdapter=new NoteAdapter(this);
		CalorieService.SQLite.LoadNoteList(mAdapter);
		this.mListView.setAdapter(mAdapter);

		super.onCreate(savedInstanceState);
	}

}
