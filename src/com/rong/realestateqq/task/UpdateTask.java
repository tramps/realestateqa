package com.rong.realestateqq.task;

import android.content.Context;
import android.util.Log;

import com.rong.realestateqq.exception.CustomException;
import com.rong.realestateqq.exception.ECode;
import com.rong.realestateqq.json.JSONBean;
import com.rong.realestateqq.net.BaseHttpsManager;
import com.rong.realestateqq.net.BaseHttpsManager.RequestParam;

public class UpdateTask extends HandleMessageTask {
	private static final String TAG = "HttpTask";
	private String mUrl;
	private String mResult;

	public UpdateTask(Context context, String url) {
		super(context);
		setShowProgressDialog(false);
		mUrl = url;
	}

	@Override
	protected Object doInBackground() {
		if (mUrl != null) {
			try {
				mResult = BaseHttpsManager.executeGetRequest(mUrl);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				return ECode.FAIL;
			}
		} 
		
		return ECode.SUCCESS;
	}
	
	public String getResult() {
		return mResult;
	}

}
