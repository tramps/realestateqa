package com.rong.realestateqq.model;

import com.rong.realestateqq.json.JSONBean;

public class Case implements JSONBean{
	public Branch[] mRequire;
	public int mResult_Val;
	
	public Branch[] getRequires() {
		return mRequire;
	}
	public void setRequires(Branch[] mRequires) {
		this.mRequire = mRequires;
	}
	public int getResultVal() {
		return mResult_Val;
	}
	public void setResultVal(int mResultVal) {
		this.mResult_Val = mResultVal;
	}
	
	public boolean isPass() {
		for (Branch b: mRequire) {
			if (!b.IsPass()) return false;
		}
		return true;
	}
	
	public boolean isFail() {
		for (Branch b: mRequire) {
			if (b.isPolled() && !b.IsPass()) {
				return true;
			}
		}
		return false;
	}
}
