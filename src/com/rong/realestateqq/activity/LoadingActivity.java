package com.rong.realestateqq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.rong.realestateqq.R;

public class LoadingActivity extends BaseActionBar implements Runnable{
	
	private ImageView ivLoading;
	private Handler mHandler = new Handler();
	
	@Override
	protected int getLayout() {
		return R.layout.activity_loading;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLoadingView();
		mHandler.postDelayed(this, 2000);
	}
	
	@Override
	protected void initElements() {
		ivLoading = (ImageView) findViewById(R.id.loading);
	}

	private void setLoadingView() {
		ivLoading.setImageResource(R.drawable.ic_loading);
	}

	@Override
	public void run() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
