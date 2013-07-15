package com.rong.realestateqq.activity;

import android.os.Bundle;

import com.rong.realestateqq.R;

public class AboutActivity extends BaseActionBar {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("关于");
	}
	
	
	@Override
	protected int getLayout() {
		return R.layout.activity_about;
	}

}
