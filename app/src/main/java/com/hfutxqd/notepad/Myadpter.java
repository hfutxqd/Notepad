package com.hfutxqd.notepad;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Myadpter extends BaseAdapter{

	Context context;
	Map<String, Object> map;
	List <Map<String, Object>> list;
	public Myadpter(Context context, List <Map<String, Object>> list) {
		this.context = context;
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View v = layoutInflater.inflate(R.layout.list_item, null);
		TextView title = (TextView) v.findViewById(R.id.item_title);
		TextView content = (TextView) v.findViewById(R.id.item_content);
		String titleStr = (String)list.get(arg0).get("title");
		if (titleStr.equals("") || titleStr == null)
		{
			title.setVisibility(8);
			content.setLines(4);
			content.setMaxLines(4);
		}
		else
			title.setText(titleStr);
		content.setText((String)list.get(arg0).get("content"));
		return v;
	}
}
