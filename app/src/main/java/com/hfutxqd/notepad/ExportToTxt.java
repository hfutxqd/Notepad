package com.hfutxqd.notepad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.os.Environment;

public class ExportToTxt {
	
	File file;
	NoteDataBase ndb;
	public ExportToTxt(Context context) {
		file  = Environment.getExternalStorageDirectory();
		ndb = new NoteDataBase(context);
		File savePath = new File(file, "Notepad");
		if (!savePath.exists() || !savePath.isDirectory())
		{
			savePath.mkdir();
			System.out.println("Notepad文件夹创建成功！");
			System.out.println(savePath);
		}
		file = savePath;
	}
	
	public boolean save(String id) throws IOException
	{
		boolean state = false;
		String title = "题目：";
		String time = "时间：";
		String content = "内容：";
		Map<String, Object> map = ndb.getItem(id);
		title += map.get("title");
		time += map.get("createTime");
		content += map.get("content");
		String filename = map.get("title") + "_" + id + ".txt";
		File save_file = new File(file, filename);
		if (!save_file.exists())
			save_file.createNewFile();
		FileOutputStream out = new FileOutputStream(save_file);
		String save_content = title + "\n" + time + "\n" + content;
		byte[] buf = save_content.getBytes();
		out.write(buf);
		out.close();
		state = true;
		return state;
	}
	
	public boolean save(String[] id) throws IOException
	{
		boolean state = true;
		for (String i: id)
		{
			if (!save(i))
				state = false;
		}
		return state;
	}
}
