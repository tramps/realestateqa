package com.rong.realestateqq.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.rong.realestateqq.exception.JsonParseException;
import com.rong.realestateqq.json.JsonHelper;
import com.rong.realestateqq.model.CalcDesc;
import com.rong.realestateqq.model.CityElement;
import com.rong.realestateqq.model.CityPolicy;
import com.rong.realestateqq.model.HpCity;

public class GlobalValue {
	public static final int ALL_CITY = 10000;

	private static final String TAG = "GlobalValue";

	private static GlobalValue INSTANCE;
	
	private ArrayList<HpCity> mCitys;
	private ArrayList<CityElement> mElements;
	private ArrayList<CityPolicy> mPolicies;
	private ArrayList<CalcDesc> mDescs;
	
	private HashMap<Integer, ArrayList<CityElement>> mCityElemMap;
	private HashMap<Integer, ArrayList<CityPolicy>> mCityPoliMap;
	
	private GlobalValue() {
	}
	
	public void clear() {
		if (mCitys != null) {
			mCitys.clear();
		}
		if (mElements != null) {
			mElements.clear();
		}
		
		if (mPolicies != null) {
			mPolicies.clear();
		}
		
		if (mDescs != null) {
			mDescs.clear();
		}
		
//		mCityElemMap.clear();
//		mCityPoliMap.clear();
	}

	public static GlobalValue getInts() {
		if (INSTANCE == null) {
			return INSTANCE = new GlobalValue();
		}
		
		return INSTANCE;
	}
	
	public static void init(Context context) {
		boolean hasInited = false;
		try {
			JsonHelper.parseJSONFromModel(context);
			Log.i(TAG, "init from model");
			hasInited = true;
		} catch (JsonParseException e) {
			Log.e(TAG, e.getMessage());
		}
		
		if (hasInited) return;
		
		try {
			JsonHelper.parseJSONFromRaw(context);
			Log.i(TAG, "init from raw");
		} catch (JsonParseException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	public ArrayList<HpCity> getCities() {
		if (mCitys == null) {
			mCitys = new ArrayList<HpCity>();
		}
		return mCitys;
	}
	
	public ArrayList<CityElement> getElements() {
		if (mElements == null) {
			mElements = new ArrayList<CityElement>();
		}
		return mElements;
	}
	
	public ArrayList<CityPolicy> getPoliciesById(int cityId) {
		if (null == mCityPoliMap) {
			mCityPoliMap = new HashMap<Integer, ArrayList<CityPolicy>>();
		}
		
		ArrayList<CityPolicy> policies = mCityPoliMap.get(cityId);
		if (null == policies) {
			policies = new ArrayList<CityPolicy>();
			
			for (CityPolicy p : mPolicies) {
				if (p.getCityId() == cityId) {
					policies.add(p);
				}
			}
			
			mCityPoliMap.put(cityId, policies);
		}
		
		return policies;
	}
	
	public CityPolicy getPolicyById(int cityId, String token) {
		for (CityPolicy p: mPolicies) {
			if (p.getCityId() == cityId && p.getToken().equalsIgnoreCase(token)) {
				return p;
			}
		}
		
		for (CityPolicy p: mPolicies) {
			if (p.getCityId() == ALL_CITY && p.getToken().equalsIgnoreCase(token)) {
				return p;
			}
		}
		
		return null;
	}
	
	public CityElement getElementById(int elemId) {
		for (CityElement elem : mElements) {
			if (elem.getId() == elemId) {
				return elem;
			}
		}
		
		return null;
	}
	
	public ArrayList<CityElement> getElementsById(int cityId) {
		if (null == mCityElemMap) {
			mCityElemMap = new HashMap<Integer, ArrayList<CityElement>>();
		}
		
		ArrayList<CityElement> elems = mCityElemMap.get(cityId);
		if (null == elems) {
			elems = new ArrayList<CityElement>();
			for (CityElement e: mElements) {
				if (cityId == e.getCityId()) {
					elems.add(e);
				}
			}
			
			mCityElemMap.put(cityId, elems);
		}
		
		return elems;
	}
	
	public ArrayList<CityPolicy> getPolicies() {
		if (null == mPolicies) {
			mPolicies = new ArrayList<CityPolicy>();
		}
		return mPolicies;
	}
	
	public ArrayList<CalcDesc> getCalcDesc() {
		if (null == mDescs) {
			mDescs = new ArrayList<CalcDesc>();
		}
		return mDescs;
	}
}
