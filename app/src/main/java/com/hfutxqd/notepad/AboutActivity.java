package com.hfutxqd.notepad;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends Activity {
	
	int count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		count = 0;
		super.onResume();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
			finish();
		else if (item.getItemId() == R.id.action_announce)
		{
			new AlertDialog.Builder(this)  
			                .setTitle("免责声明")
			                .setMessage(R.string.announce)
			                .setPositiveButton("确定", null)
			                .show();
		}
		else if (item.getItemId() == R.id.action_update)
		{
			new AlertDialog.Builder(this)  
			                .setTitle("更新说明")
			                .setMessage(R.string.update)
			                .setPositiveButton("确定", null)
			                .show();
		}
		else if (item.getItemId() == R.id.action_suport)
		{
			new AlertDialog.Builder(this)  
			                .setTitle("支持我")
			                .setMessage(R.string.suport)
			                .setPositiveButton("确定", null)
			                .show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean auther(View v)
	{
		count++;
		if (count == 5)
		{
			int x = (int) (3 * Math.random());
			switch (x) {
			case 0:
				Toast.makeText(getApplicationContext(), "不要对我这么暧昧嘛！", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "喜欢我的话加我QQ把！", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "不要再摸我了。。。", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			count = 0;
		}
		return true;
	}
}
