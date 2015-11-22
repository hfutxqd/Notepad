package com.hfutxqd.notepad;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;


public class NotepadAppWidgetProvider extends AppWidgetProvider {
	public NotepadAppWidgetProvider() {
		super();
	}
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.println("NotepadAppWidget----------------->onUpdate is called!");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	@Override
	public void onEnabled(Context context) {
		System.out.println("NotepadAppWidget----------------->onEnabled is called!");
		super.onEnabled(context);
	}
	
	@Override
	public void onDisabled(Context context) {
		System.out.println("NotepadAppWidget----------------->onDisabled is called!");
		super.onDisabled(context);
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		System.out.println("NotepadAppWidget----------------->onDeleted is called!");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("NotepadAppWidget----------------->onReceive is called!");
		super.onReceive(context, intent);
	}
}
