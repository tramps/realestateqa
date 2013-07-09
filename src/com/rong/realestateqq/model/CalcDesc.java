package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class CalcDesc implements JSONBean{
	public static final String KEY = "calc_desc";
	
	public int mId;
	public int mCity_Id;
	public int mElement_Id;
	public String mTips;
	public String mSample1;
	public String mSample2;
	public String mSample3;
	
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public int getCityId() {
		return mCity_Id;
	}
	public void setCityId(int mCityId) {
		this.mCity_Id = mCityId;
	}
	public int getElementId() {
		return mElement_Id;
	}
	public void setElementId(int mElementId) {
		this.mElement_Id = mElementId;
	}
	public String getTips() {
		return mTips;
	}
	public void setTips(String mTips) {
		this.mTips = mTips;
	}
	public String getSample1() {
		return mSample1;
	}
	public void setSample1(String mSample1) {
		this.mSample1 = mSample1;
	}
	public String getSample2() {
		return mSample2;
	}
	public void setSample2(String mSample2) {
		this.mSample2 = mSample2;
	}
	public String getSample3() {
		return mSample3;
	}
	public void setSample3(String mSample3) {
		this.mSample3 = mSample3;
	}
	
	
}
