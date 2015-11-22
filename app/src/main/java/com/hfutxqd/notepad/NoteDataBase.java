package com.hfutxqd.notepad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteDataBase implements NoteService {
	private static DataBaseService dbs;

	public NoteDataBase(Context context) {
		dbs = new DataBaseService(context);
	}

	@Override
	public boolean add(String title, String content) {
		SQLiteDatabase db = dbs.getWritableDatabase();
		String sql = "insert into note (title, content) values (?, ?)";
		String[] bindArgs = new String[] { title, content };
		db.execSQL(sql, bindArgs);
		db.close();
		return true;
	}

	@Override
	public boolean update(String id, String title, String content) {
		SQLiteDatabase db = dbs.getWritableDatabase();
		String sql = "update note set title = ?, content = ? where id = ?";
		String[] bindArgs = new String[] { title , content, id };
		db.execSQL(sql, bindArgs);
		db.close();
		return true;
	}

	@Override
	public boolean delete(String id) {
		SQLiteDatabase db = dbs.getWritableDatabase();
		String sql = "delete from note where id = ?";
		String[] bindArgs = new String[] { id };
		db.execSQL(sql, bindArgs);
		db.close();
		return true;
	}

	@Override
	public List<Map<String, Object>> getList() {
		SQLiteDatabase db = dbs.getWritableDatabase();
		Cursor cursor = db.query("note", null, null, null, null, null, null);
		int column = cursor.getColumnCount();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < column; i++)
			{
				String columnName = cursor.getColumnName(i);
				String columnValue = cursor.getString(i);
				if (columnValue == null)
					columnValue = "";
				map.put(columnName, columnValue);
			}
			list.add(map);
		}
		db.close();
		return list;
	}

	@Override
	public Map<String, Object> getItem(String id) {
		SQLiteDatabase db = dbs.getWritableDatabase();
		Cursor cursor = db.query("note", null, "id = ?", new String[] { id },
				null, null, null);
		int column = cursor.getColumnCount();
		Map<String, Object> map = new HashMap<String, Object>();
		cursor.moveToNext();
		for (int i = 0; i < column; i++) {
			String columnName = cursor.getColumnName(i);
			String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
			if (columnValue == null)
				columnValue = "";
			map.put(columnName, columnValue);
		}
		db.close();
		return map;
	}

}
