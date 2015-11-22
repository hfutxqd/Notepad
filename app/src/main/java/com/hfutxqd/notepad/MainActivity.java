package com.hfutxqd.notepad;

import java.io.IOException;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	public static boolean change = false;
	public static ScreenInfo screenInfo;
	NoteDataBase ndb;
	ListView listView;
	ItemClick ItemListener;
	ItemLongClick ItemLong;
	ExportToTxt toTxt;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			listView.setAdapter(new Myadpter(getApplicationContext(), ndb
					.getList()));
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ndb = new NoteDataBase(getApplicationContext());
		toTxt = new ExportToTxt(getApplicationContext());
		setContentView(R.layout.activity_main);
		screenInfo = new ScreenInfo(getApplicationContext());
		listView = getListView();
		ItemListener = new ItemClick();
		ItemLong = new ItemLongClick();
		listView.setOnItemClickListener(ItemListener);
		listView.setOnItemLongClickListener(ItemLong);
		listView.setAdapter(new Myadpter(getApplicationContext(), ndb.getList()));

	}

	@Override
	protected void onResume() {
		if (change) {
			listView.setAdapter(new Myadpter(getApplicationContext(), ndb
					.getList()));
			change = false;
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			Intent intentToAdd = new Intent(getApplicationContext(),
					AddActivity.class);
			startActivity(intentToAdd);
			break;
		case R.id.action_manager:
			Intent intentToManager = new Intent(getApplicationContext(),
					ManagerActivity.class);
			startActivity(intentToManager);
			break;
		case R.id.action_about:
			Intent intentToAbout = new Intent(getApplicationContext(),
					AboutActivity.class);
			startActivity(intentToAbout);
		default:
		}
		return super.onOptionsItemSelected(item);
	}

	class ItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) listView
					.getItemAtPosition(arg2);
			Intent intent = new Intent(getApplicationContext(),
					ViewActivity.class);
			intent.putExtra("note", new String[] { (String) map.get("title"),
					(String) map.get("createTime"),
					(String) map.get("content"), (String) map.get("id") });
			startActivity(intent);
		}

	}

	class ItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) listView
					.getItemAtPosition(arg2);
			showListDia((String) map.get("title"), (String) map.get("id"));
			return true;
		}

	}

	private void showListDia(String title, String id) {
		final String[] mList = { "删除此条目", "编辑此条目", "以此为模板新增", "导出到文本",
				"管理所有条目", "取消" };
		AlertDialog.Builder listDia = new AlertDialog.Builder(this);
		listDia.setTitle(title);
		listDia.setItems(mList, new MyDiaListener(id));
		listDia.create().show();
	}

	class MyDiaListener implements DialogInterface.OnClickListener {
		String id;

		public MyDiaListener(String id) {
			this.id = id;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			 switch (which) {
				case 0:
					new MyDelThread(new String[]{id}).start();
					Toast.makeText(getApplicationContext(), "已删除", Toast.LENGTH_SHORT).show();
					change = true;
					break;
				case 1:
					Map<String, Object> map = ndb.getItem(id);
					AddActivity.title = (String) map.get("title");
					AddActivity.content = (String) map.get("content");
					Intent intent = new Intent(getApplicationContext(), AddActivity.class);
					intent.putExtra("update", true);
					intent.putExtra("id", id);
					startActivity(intent);
					break;
				case 2:
					Map<String, Object> map1 = ndb.getItem(id);
					AddActivity.title = (String) map1.get("title");
					AddActivity.content = (String) map1.get("content");
					Intent intent1 = new Intent(getApplicationContext(), AddActivity.class);
					startActivity(intent1);
					break;
				case 3:
					boolean state = false;
					try {
						state = toTxt.save(id);
					} catch (IOException e) {
						e.printStackTrace();
					}finally{
						if (state)
							Toast.makeText(getApplicationContext(), "导出成功！", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(getApplicationContext(), "导出失败！", Toast.LENGTH_SHORT).show();
					}
					break;
				case 4:
					Intent intentToManager = new Intent(getApplicationContext(), ManagerActivity.class);
					startActivity(intentToManager);
					break;
				case 5:
					break;
				default:
				}
		}
	}

	class MyDelThread extends Thread {
		String[] id;

		public MyDelThread(String[] id) {
			this.id = id;
		}

		@Override
		public void run() {
			for (String i : id) {
				ndb.delete(i);
			}
			super.run();
			handler.sendEmptyMessage(0);
		}
	}
}
