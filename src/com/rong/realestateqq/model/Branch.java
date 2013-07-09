package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class Branch implements JSONBean {
	public int mId;
	public int mType;
	public int[] mData;
	
	private boolean mIsPass;
	private boolean mIsPolled;
	private int mSelected = -1;
	
	public int getSelected() {
		return mSelected;
	}
	public void setSelected(int mSelected) {
		this.mSelected = mSelected;
	}
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public int getType() {
		return mType;
	}
	public void setType(int mType) {
		this.mType = mType;
	}
	public int[] getData() {
		return mData;
	}
	public void setData(int[] mData) {
		this.mData = mData;
	}
	public boolean IsPass() {
		for (int option : mData) {
			if (option == mSelected) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPolled() {
		return -1 != mSelected;
	}
}