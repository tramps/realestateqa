package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class Option implements JSONBean{
	public String mDesc;
	public int mValue;
	
	public Option(String desc, int value) {
		mDesc = desc;
		mValue = value;
	}
	
	public Option() {
	}
	
	public String getDesc() {
		return mDesc;
	}
	public void setDesc(String mDesc) {
		this.mDesc = mDesc;
	}
	public int getValue() {
		return mValue;
	}
	public void setValue(int mValue) {
		this.mValue = mValue;
	}
	
}
