package com.rong.realestateqq.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.rong.realestateqq.R;

public abstract class BaseFragmentActivity extends FragmentActivity{
	private TitleBar mTitleBar;
	private RelativeLayout mContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContainer = new RelativeLayout(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(getLayout(), mContainer);
		setContentView(mContainer);
		initElement();
	}

	/** get the layout resource id. */
	protected int getLayout() {
		return 0;
	}

	/** find all elements and set listeners */
	protected void initElement() {
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
	
	public ViewGroup getMContainer(){
		return mContainer;
	}
}
