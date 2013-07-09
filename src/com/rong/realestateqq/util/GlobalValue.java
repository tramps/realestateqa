package com.rong.realestateqq.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.rong.realestateqq.model.CalcDesc;
import com.rong.realestateqq.model.CityElement;
import com.rong.realestateqq.model.CityPolicy;
import com.rong.realestateqq.model.HpCity;

public class GlobalValue {
	private static GlobalValue INSTANCE;
	
	private ArrayList<HpCity> mCitys;
	private ArrayList<CityElement> mElements;
	private ArrayList<CityPolicy> mPolicies;
	private ArrayList<CalcDesc> mDescs;
	
	private HashMap<Integer, ArrayList<CityElement>> mCityElemMap;
	private HashMap<Integer, ArrayList<CityPolicy>> mCityPoliMap;
	
	private GlobalValue() {
	}

	public static GlobalValue getInts() {
		if (INSTANCE == null) {
			return INSTANCE = new GlobalValue();
		}
		
		return INSTANCE;
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
