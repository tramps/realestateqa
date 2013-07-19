package com.rong.realestateqq.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.rong.realestateqq.R;
import com.rong.realestateqq.util.IntentUtil;
import com.umeng.fb.FeedbackAgent;

public class AboutActivity extends BaseActionBar implements OnClickListener {
	private View rlFeedback;
	private View rlDownload;

	private static final String RONG_LINK = 
			"http://www.rong360.com/static/dl/com.rong360.loans.apk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setTitle("关于");
	}

	private static final String FINISHED = "完成";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(0, R.id.setting, 0, FINISHED);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.setting) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_about;
	}

	@Override
	protected void initElements() {
		rlFeedback = findViewById(R.id.rl_feedback);
		rlDownload = findViewById(R.id.rl_download);
		rlFeedback.setOnClickListener(this);
		rlDownload.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == rlFeedback) {
			FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
			agent.startFeedbackActivity();
		} else if (v == rlDownload) {
			IntentUtil.startWeb(AboutActivity.this, RONG_LINK);
		}
	}
}
