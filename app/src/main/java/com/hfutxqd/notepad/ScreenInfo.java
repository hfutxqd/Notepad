package com.hfutxqd.notepad;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenInfo{
	public static DisplayMetrics dm;
	public static float xdpi;
	public static float ydpi;
	public static int heightPixels;
	public static int widthPixels;
	
	public ScreenInfo(Context context)
	{
		dm = context.getResources().getDisplayMetrics();
		xdpi = dm.xdpi;
		ydpi = dm.ydpi;
		heightPixels = dm.heightPixels;
		widthPixels = dm.widthPixels;
	}
	
	public static int dp_to_px(float dpi)
	{
		if (dm != null)
			return (int)(dpi * dm.density);
		else
			return 0;
	}
}
