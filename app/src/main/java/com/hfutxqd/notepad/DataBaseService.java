package com.hfutxqd.notepad;




import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseService extends SQLiteOpenHelper {
	private static String name = "Notepad.db";
	private static int version = 1;
	private Context context;
	
	
	public DataBaseService(Context context) {
		super(context, name, null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		boolean flag = false;
		String sql = "create table note (id integer primary key autoincrement, createTime datetime not null default (datetime('now','localtime')), title text, content text)";
		arg0.execSQL(sql);
		sql = "insert into note (title, content) values (?, ?)";
		String[] bindArgs = new String[] { context.getString(R.string.default_1_title), context.getString(R.string.default_1_content)};
		arg0.execSQL(sql, bindArgs);
		bindArgs = new String[] { context.getString(R.string.default_2_title), context.getString(R.string.default_2_content)};
		arg0.execSQL(sql, bindArgs);
		bindArgs = new String[] { context.getString(R.string.default_3_title), context.getString(R.string.default_3_content)};
		arg0.execSQL(sql, bindArgs);
		flag = true;
		Log.i("SQL create", "-->" + flag);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
