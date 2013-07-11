package com.rong.realestateqq.application;

import com.rong.realestateqq.net.BaseHttpsManager;
import com.rong.realestateqq.util.GlobalValue;
import com.rong.realestateqq.util.UpdataHelper;

import android.app.Application;

public class MyApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		onAppStart();
	}

	private void onAppStart() {
		BaseHttpsManager.init(getApplicationContext());
		GlobalValue.init(getApplicationContext());
		UpdataHelper.updateModel(getApplicationContext());
	}
}
