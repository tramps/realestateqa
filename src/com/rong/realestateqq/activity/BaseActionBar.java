package com.rong.realestateqq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.rong.realestateqq.R;

public abstract class BaseActionBar extends Activity{
	private static final String TAG = BaseActionBar.class.getSimpleName();
	
	private TitleBar mTitleBar;
	private RelativeLayout mContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContainer = new RelativeLayout(this);
		View view = LayoutInflater.from(this).inflate(getLayout(), null);
		mContainer.addView(view, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(mContainer);
		
		initElements();
	}

	protected void initElements() {
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onResume(this);
	}

	protected int getLayout() {
		return 0;
	}
	
	protected RelativeLayout getMContainer() {
		return mContainer;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.back) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public TitleBar getSupportActionBar() {
		if (mTitleBar == null) {
			mTitleBar = new TitleBar(this);
			mContainer.addView(mTitleBar);
		}
		return mTitleBar;
	}
}
