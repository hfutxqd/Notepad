package com.hfutxqd.notepad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyManagerAdpter extends BaseAdapter{

	Context context;
	Map<String, Object> map;
	List <Map<String, Object>> list;
	LayoutInflater layoutInflater;
	View v;
	boolean[] checks;
	public MyManagerAdpter(Context context, List <Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
		checks = new boolean[list.size()];
		for (int i =0; i < list.size(); i++)
			checks[i] = false;
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
		v = layoutInflater.inflate(R.layout.manager_item, null);
		TextView title = (TextView) v.findViewById(R.id.item_title);
		TextView content = (TextView) v.findViewById(R.id.item_content);
		CheckBox checkBox = (CheckBox) v.findViewById(R.id.item_check);
		checkBox.setChecked(checks[arg0]);
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
	
	public String[] getCheckedDBId()
	{
		ArrayList< String> arrayList = new ArrayList<String>();
		for (int i = 0; i < checks.length; i++)
		{
			if (checks[i])
			{
				Map<String, Object> map = list.get(i);
				arrayList.add((String) map.get("id"));
			}
		}
		return ((String[]) arrayList.toArray(new String[0]));
	}
	
	public boolean getCheckBoxCheck(int pos)
	{
		return checks[pos];
	}
	
	public void setChecked(int pos)
	{
		checks[pos] = true;
		System.out.println("Manger-------------------->setChecked, pos = " + pos);
	}
	public void setAllChecked()
	{
		for (int i = 0; i < checks.length; i++)
		{
			checks[i] = true;;
		}
		notifyDataSetChanged();
	}
	
	public void setUnChecked(int pos)
	{
		checks[pos] = false;
	}
	
	public void setAllUnChecked()
	{
		for (int i = 0; i < checks.length; i++)
		{
			checks[i] = false;
		}
		notifyDataSetChanged();
	}
	
}
