package com.rong.realestateqq.net;

import com.rong.realestateqq.json.JSONBean;

public class ServerApi {
	protected String mHost;
	protected String mApi;
	protected Class<? extends JSONBean> mResponse;

	public ServerApi(String host, String api,
			Class<? extends JSONBean> response) {
		mHost = host;
		mApi = api;
		mResponse = response;
	}

	public String getHost() {
		return mHost;
	}

	public String getApi() {
		return mApi;
	}

	public String getUrl() {
		return mHost + mApi;
	}

	public Class<? extends JSONBean> getResponse() {
		if (mResponse == null)
			throw new RuntimeException("Response for " + mApi
					+ " is not implements");
		return mResponse;
	}

}