package com.rong.realestateqq.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.rong.realestateqq.activity.PushReceiverActivity;
import com.rong.realestateqq.net.BaseHttpsManager;
import com.rong.realestateqq.util.GlobalValue;
import com.rong.realestateqq.util.NetHelper;

public class MyApplication extends Application {
	private static final String APPLICATION_ID = "aISEcULQ9hkzQF1SiBcoPmnQHheUtkPAcIAjTJNu";
	private static final String CLIENT_ID = "mFpfAJ8u4KYGZmY0hCQjSbjPqQ12mv0jacBy9Hip";

	@Override
	public void onCreate() {
		super.onCreate();
		onAppStart();
	}

	private void onAppStart() {
		BaseHttpsManager.init(getApplicationContext());
		GlobalValue.init(getApplicationContext());
		NetHelper.updateModel(getApplicationContext());
		
		initParse();
	}

	private void initParse() {
		Parse.initialize(this, APPLICATION_ID, CLIENT_ID);
		PushService.setDefaultPushCallback(this, PushReceiverActivity.class);
		NetHelper.subscribe2Parse(this);
		ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
}
