package com.seven.textedit;

import android.app.Application;

public class BaseApplication extends Application {
	private static BaseApplication bApplication;
	private static final Object obj = new Object(); 
	
	public static BaseApplication getInstance()
	{
		return bApplication;
	}
	public void onCreate()
    {
		super.onCreate();
		bApplication = this;
    }
}
