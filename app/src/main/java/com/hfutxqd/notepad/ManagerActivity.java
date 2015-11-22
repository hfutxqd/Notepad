package com.hfutxqd.notepad;

import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ManagerActivity extends ListActivity {

	public static boolean change = false;
	NoteDataBase ndb;
	MyManagerAdpter managerAdpter;
	ListView listView;
	ItemClickListener itemClickListener;
	ExportToTxt toTxt;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				MainActivity.change = true;
				Toast.makeText(getApplicationContext(), "已删除完成",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "已导出完成",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "导出异常！",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ndb = new NoteDataBase(getApplicationContext());
		toTxt = new ExportToTxt(getApplicationContext());
		setContentView(R.layout.activity_manager);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		listView = getListView();
		managerAdpter = new MyManagerAdpter(getApplicationContext(),
				ndb.getList());
		itemClickListener = new ItemClickListener();
		listView.setAdapter(managerAdpter);
		listView.setClickable(true);
		listView.setOnItemClickListener(itemClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String[] ids = managerAdpter.getCheckedDBId();
		switch (item.getItemId()) {
		case R.id.action_m_del:
			if (ids.length > 0) {
				new MyDelThread(ids).start();
				Toast.makeText(getApplicationContext(), "正在删除...",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "未选中任何条目！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.action_m_all:
			managerAdpter.setAllChecked();
			break;
		case R.id.action_m_opp:
			int size = managerAdpter.getCount();
			for (int i = 0; i < size; i++) {
				if (managerAdpter.getCheckBoxCheck(i))
					managerAdpter.setUnChecked(i);
				else
					managerAdpter.setChecked(i);
				managerAdpter.notifyDataSetChanged();
			}
			break;
		case R.id.action_m_export:
			if (ids.length > 0) {
				new MyExportThread(ids).start();
				Toast.makeText(getApplicationContext(), "正在导出...",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "未选中任何条目！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case android.R.id.home:
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (!managerAdpter.getCheckBoxCheck(arg2)) {
				managerAdpter.setChecked(arg2);
			} else {
				managerAdpter.setUnChecked(arg2);
			}
			managerAdpter.notifyDataSetChanged();
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
			handler.sendEmptyMessage(0);
		}
	}

	class MyExportThread extends Thread {
		String[] ids;

		public MyExportThread(String[] ids) {
			this.ids = ids;
		}

		@Override
		public void run() {
			boolean state = false;
			try {
				state = toTxt.save(ids);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (state)
					handler.sendEmptyMessage(1);
				else
					handler.sendEmptyMessage(2);
			}

		}
	}

}
