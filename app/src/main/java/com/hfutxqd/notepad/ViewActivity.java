package com.hfutxqd.notepad;

import java.io.IOException;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ViewActivity extends Activity {

	NoteDataBase ndb;
	Intent intent;
	String[] note;
	TextView title, datetime, content;
	ExportToTxt toTxt;
	public static boolean change = false;
	@SuppressLint("HandlerLeak") Handler handler = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "已保存", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "已更新", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			finish();
		}
	};
	@Override
	protected void onResume()
	{
		if (change)
		{
			Map<String, Object> map = ndb.getItem(note[3]);
			title.setText((String)map.get("title"));
			datetime.setText((String)map.get("createTime"));
			content.setText((String)map.get("content"));
			change = false;
		}
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ndb = new NoteDataBase(getApplicationContext());
		toTxt = new ExportToTxt(getApplicationContext());
		title = (TextView) findViewById(R.id.view_title);
		datetime = (TextView) findViewById(R.id.view_time);
		content = (TextView) findViewById(R.id.view_content);
		intent = getIntent();
		note = intent.getStringArrayExtra("note");
		if (note[0].equals("") || note[0] == null)
			title.setVisibility(8);
		else
			title.setText(note[0]);
		datetime.setText(note[1]);
		if (note[2].equals("") || note[2] == null)
			content.setVisibility(8);
		else
			content.setText(note[2]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_edit:
			Map<String, Object> map = ndb.getItem(note[3]);
			AddActivity.title = (String) map.get("title");
			AddActivity.content = (String) map.get("content");
			Intent intent = new Intent(getApplicationContext(), AddActivity.class);
			intent.putExtra("update", true);
			intent.putExtra("id", note[3]);
			startActivity(intent);
			break;
		case R.id.action_del:
			new MyDelThread(new String[]{note[3]}).start();
			MainActivity.change = true;
			break;
		case R.id.action_export:
			boolean state = false;
			try {
				state = toTxt.save(note[3]);
			} catch (IOException e) {
				e.printStackTrace();
			}finally
			{
				if (state)
					Toast.makeText(getApplicationContext(), "导出成功！", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(), "导出失败！", Toast.LENGTH_SHORT).show();
			}
			break;
		case android.R.id.home:
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MyDelThread extends Thread
	{
		String[] id;
		public MyDelThread(String[] id)
		{
			this.id = id;
		}
		@Override
		public void run() {
			for (String i: id)
			{
				ndb.delete(i);
			}
			super.run();
			handler.sendEmptyMessage(2);
		}
	}

}
