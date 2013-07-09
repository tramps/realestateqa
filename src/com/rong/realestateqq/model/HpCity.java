package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class HpCity implements JSONBean{
	public static final String KEY = "hpcity";
	public int mId;
	public String mName;
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
}
