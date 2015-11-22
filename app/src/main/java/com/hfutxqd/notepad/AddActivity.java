package com.hfutxqd.notepad;

import com.hfutxqd.notepad.JsonParser;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends Activity implements OnClickListener, RecognizerListener{
	public static String title = "";
	public static String content = "";
	EditText et_title, et_content;
	Button voice1, voice2;
	SpeechRecognizer mIat;
	int state = 0;
	NoteDataBase ndb;
	Intent intent;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(getApplicationContext(), "已保存",
						Toast.LENGTH_SHORT).show();
				title = "";
				content = "";
				et_title.setText(title);
				et_content.setText(content);
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "已更新",
						Toast.LENGTH_SHORT).show();
				title = "";
				content = "";
				et_title.setText(title);
				et_content.setText(content);
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "已删除",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			MainActivity.change = true;
			ViewActivity.change = true;
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		ndb = new NoteDataBase(getApplicationContext());
		intent = getIntent();
		voiceInit();
		if (intent.getBooleanExtra("update", false))
			setTitle("编辑");

		et_title = (EditText) findViewById(R.id.editTitle);
		et_content = (EditText) findViewById(R.id.editContent);
		voice1 = (Button) findViewById(R.id.bt_voice1);
		voice2 = (Button) findViewById(R.id.bt_voice2);
		voice1.setOnClickListener(this);
		voice2.setOnClickListener(this);
	}
	
	public void voiceInit()
	{
		SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=53aa69a6");
        mIat= SpeechRecognizer.createRecognizer(getApplicationContext(), null);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
	}

	@Override
	protected void onResume() {
		et_title.setText(title);
		et_content.setText(content);
		super.onResume();
	}

	@Override
	protected void onPause() {
		title = et_title.getText().toString();
		content = et_content.getText().toString();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save:
			title = et_title.getText().toString();
			content = et_content.getText().toString();
			if (!intent.getBooleanExtra("update", false)) {
				if (title.equals("") && content.equals("")) {
					Toast.makeText(getApplicationContext(), "未保存，请输入内容！",
							Toast.LENGTH_SHORT).show();
				} else {
					new MySaveThread(title, content).start();
				}
			} else {
				new MyUpdateThread(title, content, intent.getStringExtra("id"))
						.start();
			}
			break;
		case R.id.action_clear:
			if (title.equals("") && content.equals("")) {
					finish();
			} else {
				title = "";
				content = "";
				et_title.setText(title);
				et_content.setText(content);
			}
			break;
		case android.R.id.home:
			finish();
		default:
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MySaveThread extends Thread {
		String title, content;
		
		public MySaveThread(String title, String content) {
			this.title = title;
			this.content = content;
		}

		@Override
		public void run() {
			ndb.add(title, content);
			super.run();
			handler.sendEmptyMessage(0);
		}
	}

	class MyUpdateThread extends Thread {
		String title, content, id;

		public MyUpdateThread(String title, String content, String id) {
			this.title = title;
			this.content = content;
			this.id = id;
		}

		@Override
		public void run() {
			ndb.update(id, title, content);
			super.run();
			handler.sendEmptyMessage(1);
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
			handler.sendEmptyMessage(2);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.bt_voice1)
		{
			state = 1;
			if (mIat.isListening())
				mIat.stopListening();
			mIat.startListening(this);
		}
		else
		{
			state = 2;
			if (mIat.isListening())
				mIat.stopListening();
			mIat.startListening(this);
		}
	}

	@Override
	public void onBeginOfSpeech() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "请说话。。。", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(SpeechError arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "识别出错！错误码：" + arg0.getErrorCode() , Toast.LENGTH_SHORT).show();
		System.out.println("SpeechError" + arg0);
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		// TODO Auto-generated method stub
		System.out.println("------------------------->onResult");
		if (state == 1)
		{
			et_title.append(JsonParser.parseIatResult(results.getResultString()));
		}
		else if(state == 2)
		{
			et_content.append(JsonParser.parseIatResult(results.getResultString()));
		}
		else
			Toast.makeText(getApplicationContext(), "未知错误！", Toast.LENGTH_SHORT).show();
		if(isLast)
		{
			System.out.println("识别结束！");
			Toast.makeText(getApplicationContext(), "识别完成！", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onVolumeChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
