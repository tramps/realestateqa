package com.rong.realestateqq.model;

import java.util.HashMap;

import com.rong.realestateqq.json.JSONBean;

public class CityPolicy implements JSONBean{
	public static final String KEY = "city_policy";
	
	public static final String TOKEN_CAN_BUY = "can_buy";
	public static final String TOKEN_NUMBER_HAVE = "num_have";
	public static final String TOKEN_LOAN_INDEX = "loan_index";
	
	public int mId;
	public int mPolicy_Id;
	public int mCity_Id;
	public Case[] mData;
	public double mDefault;
	public String mMeaning;
	public String mToken;

	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public int getPolicyId() {
		return mPolicy_Id;
	}
	public void setPolicyId(int mPolicyId) {
		this.mPolicy_Id = mPolicyId;
	}
	public int getCityId() {
		return mCity_Id;
	}
	public void setCityId(int mCityId) {
		this.mCity_Id = mCityId;
	}
	public Case[] getData() {
		return mData;
	}
	public void setData(Case[] mData) {
		this.mData = mData;
	}
	public double getDefault() {
		return mDefault;
	}
	public void setDefault(double mDefault) {
		this.mDefault = mDefault;
	}
	public String getMeaning() {
		return mMeaning;
	}
	public void setMeaning(String mMeaning) {
		this.mMeaning = mMeaning;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String mToken) {
		this.mToken = mToken;
	}
	
	public int getPolicyResult() {
		for (Case c : mData) {
			if (c.isPass()) {
				return c.getResultVal();
			}
		}
		
		return -1;
	}
	
	public boolean setCustomerAnswer(int elemId, int selected) {
		boolean isSet = false;
		for (Case c : mData) {
			for (Branch b : c.getRequires()) {
				if (b.getId() == elemId) {
					b.setSelected(selected);
					isSet = true;
				}
			}
		}
		
		return isSet;
	}
	
	public void clearAnswer() {
		for (Case c : mData) {
			for (Branch b : c.getRequires()) {
				b.setSelected(-1);
			}
		}
	}
	
	public int getNextQuesElementId(int lastElemId, int seleted) {
		if (lastElemId != -1) {
			setCustomerAnswer(lastElemId, seleted);
		}
		
		HashMap<Integer, Integer> mCountMap = new HashMap<Integer, Integer>();
		
		for (Case c : mData) {
			if (!c.isFail()) {
				for (Branch b: c.getRequires()) {
					if (!b.isPolled()) {
						if (mCountMap.containsKey(b.getId())) {
							int count = mCountMap.get(b.getId());
							mCountMap.put(b.getId(), count+1);
						} else {
							mCountMap.put(b.getId(), 1);
						}
					}
				}
			}
		}
		
		int nextElemId = -1;
		int maxCount = -1;
		int count;
		for (int key : mCountMap.keySet()) {
			if (maxCount == -1) {
				nextElemId = key;
				maxCount = mCountMap.get(key);
			} else {
				count = mCountMap.get(key);
				if (count > maxCount) {
					nextElemId = key;
					maxCount = count;
				}
			}
		}
		
		return nextElemId;
	}
	
}
