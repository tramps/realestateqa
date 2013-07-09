package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class Option implements JSONBean{
	public String mDesc;
	public String mValue;
	
	public String getDesc() {
		return mDesc;
	}
	public void setDesc(String mDesc) {
		this.mDesc = mDesc;
	}
	public String getValue() {
		return mValue;
	}
	public void setValue(String mValue) {
		this.mValue = mValue;
	}
	
}
