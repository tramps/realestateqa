package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class CityElement implements JSONBean{
	public static final String KEY = "city_element";
	
	public int mId;
	public String mTitle;
	public int mType;
	public int mElement_Id;
	public int mCity_Id;
	public Option[] mData;
	
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public int getType() {
		return mType;
	}
	public void setType(int mType) {
		this.mType = mType;
	}
	public int getElementId() {
		return mElement_Id;
	}
	public void setElementId(int mElementId) {
		this.mElement_Id = mElementId;
	}
	public int getCityId() {
		return mCity_Id;
	}
	public void setCityId(int mCityId) {
		this.mCity_Id = mCityId;
	}
	public Option[] getData() {
		return mData;
	}
	public void setData(Option[] mData) {
		this.mData = mData;
	}
	
}