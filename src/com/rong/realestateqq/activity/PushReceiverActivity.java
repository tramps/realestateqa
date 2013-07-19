package com.rong.realestateqq.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.rong.realestateqq.R;
import com.rong.realestateqq.exception.JsonParseException;
import com.rong.realestateqq.json.JsonHelper;
import com.rong.realestateqq.model.PushContent;
import com.rong.realestateqq.model.PushInfo;
import com.rong.realestateqq.util.IntentUtil;

public class PushReceiverActivity extends BaseActionBar {
	private static final String PRE_KEY_CHANNEL = "com.parse.Channel";
	private static final String PRE_KEY_DATA = "com.parse.Data";
	private static final String PRE_KEY_ACTION = "com.parse.actioin";
	
	private static final String TAG = "PushReceiverActivity";
	
	private final String XG_LINK = "http://www.rong360.com/calculator/xiangou";
	private String mLink = XG_LINK;
	
	private WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getIntent().getExtras();
//		String channel = args.getString(PRE_KEY_CHANNEL);
		String data = args.getString(PRE_KEY_DATA);
		try {
			PushInfo push = JsonHelper.parseJSONToObject(PushInfo.class, data);
			if (push != null) {
				PushContent content = push.mData;
				if (content != null) {
					mLink = content.mUrl;
				}
			}
		} catch (JsonParseException e) {
			Log.e(TAG, e.toString());
		}
		
		initContent();
	}
	
	private void initContent() {
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);

		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setSupportZoom(true);
//		mWebView.loadUrl(mLink);
		Log.i(TAG, mLink);
		IntentUtil.startWeb(this, mLink);
		finish();
	}

	@Override
	protected void initElements() {
		mWebView = (WebView) findViewById(R.id.push_view);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_push_receiver;
	}
}
