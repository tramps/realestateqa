package com.rong.realestateqq;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.rong.realestateqq.exception.JsonParseException;
import com.rong.realestateqq.json.JsonHelper;
import com.rong.realestateqq.model.CityElement;
import com.rong.realestateqq.model.CityPolicy;
import com.rong.realestateqq.model.HpCity;
import com.rong.realestateqq.model.Option;
import com.rong.realestateqq.util.GlobalValue;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private FrameLayout flContent;
	private CityPolicy mCanBuyPolicy;
	private CityPolicy mNumHavePolicy;
	private CityPolicy mLoanIndexPolicy;
	private int mCityId;
	private int mStep = -1;
	private ArrayList<CityElement> mElements;
	private ArrayList<Integer> mAnswers;
	private GlobalValue mValue;
	private boolean mIsBack = false;
	private int mCanBuy;
	private int mLeftBuy;
	private int mLoanBuy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// flContent = (FrameLayout) findViewById(R.id.content);

		try {
			JsonHelper.parseJSONFromRaw(getApplicationContext());
		} catch (JsonParseException e) {
			e.printStackTrace();
		}

		mElements = new ArrayList<CityElement>();
		mAnswers = new ArrayList<Integer>();
		Log.i(TAG, "load");
		loadFragment(mStep);
		// StringBuilder sb = new StringBuilder();
		// try {
		// JsonHelper.parseJSONFromRaw(getApplicationContext());
		// } catch (JsonParseException e) {
		// e.printStackTrace();
		// }
		//
		// GlobalValue global = GlobalValue.getInts();
		// sb.append(JsonHelper.parseListToJsonArray(global.getCities()).toString());
		// tvContent.setText(sb.toString());
	}

	private OnCheckedChangeListener mCheckChangedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (!mIsBack) {
				loadFragment(checkedId);
			}
			mIsBack = false;
		}
	};

	protected void loadFragment(int checkedId) {
		Log.i(TAG, "step: " + mStep + " checkedId:" + checkedId);

		String title = "";
		ArrayList<Option> ops = new ArrayList<Option>();
		if (mStep == -1) {// first step
			title = "请选择所在城市";
			ArrayList<HpCity> cities = GlobalValue.getInts().getCities();
			for (HpCity city : cities) {
				Option op = new Option(city.getName(), city.getId());
				ops.add(op);
			}
		} else if (mStep == 0) {
			mValue = GlobalValue.getInts();
			mCityId = checkedId;
			mCanBuyPolicy = mValue.getPolicyById(mCityId,
					CityPolicy.TOKEN_CAN_BUY);
			if (mCanBuyPolicy == null) {
				Log.e(TAG, "data error, no corresponding city" + mCityId);
				finish();
			}
			Log.i(TAG, "cityPolicy:" + mCanBuyPolicy.getCityId() + "  "
					+ mCanBuyPolicy.getMeaning());
		}

		if (mStep > -1) {
			int lastElemId;
			if (mStep == 0) {
				lastElemId = -1;
			} else {
				lastElemId = mElements.get(mStep - 1).getId();
			}

			int nextElemId = -1;
			if (mNumHavePolicy == null) {
				nextElemId = mCanBuyPolicy.getNextQuesElementId(lastElemId,
						checkedId);
			}
			if (nextElemId == -1) {
				// Toast.makeText(this, "first step period",
				// Toast.LENGTH_SHORT).show();
				// return;
				mCanBuy = mCanBuyPolicy.getPolicyResult();
				if (mCanBuy <= 0) {
					Toast.makeText(this, "SORRY, BUT YOU CAN'T BUY!",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (mNumHavePolicy == null) {
					mNumHavePolicy = mValue.getPolicyById(mCityId,
							CityPolicy.TOKEN_NUMBER_HAVE);
					nextElemId = mNumHavePolicy.getNextQuesElementId(-1, -1);
				} else {
					nextElemId = mNumHavePolicy.getNextQuesElementId(
							lastElemId, checkedId);
				}

				if (nextElemId == -1) {
					mLeftBuy = mCanBuy - mNumHavePolicy.getPolicyResult();
					if (mLeftBuy <= 0) {
						Toast.makeText(this, "SORRY, BUT YOU BUY TOO MUCH!",
								Toast.LENGTH_SHORT).show();
						return;
					}

					if (mLoanIndexPolicy == null) {
						mLoanIndexPolicy = mValue.getPolicyById(mCityId,
								CityPolicy.TOKEN_LOAN_INDEX);
					}
					nextElemId = mLoanIndexPolicy.getNextQuesElementId(
							lastElemId, checkedId);

					if (nextElemId == -1
							|| nextElemId == mElements
									.get(mElements.size() - 1).getId()) {
						mLoanBuy = mLoanIndexPolicy.getPolicyResult();
					}
				}

			}

			if (mLoanBuy != 0) {
				title = "还可以买" + mLeftBuy + "套房;" + "贷款算" + mLoanBuy + "贷款";
			} else {
				CityElement elem = mValue.getElementById(nextElemId);
				mElements.add(elem);
				Log.i(TAG,
						"next elem Id " + nextElemId + " size: "
								+ mElements.size());

				for (Option op : elem.getData()) {
					ops.add(op);
				}
				title = elem.getTitle();
			}

			mAnswers.add(checkedId);

		}
		if (mLoanBuy == 0) {
			mStep++;
		}

		PollFragment poll = PollFragment.newInstance(ops, title,
				mCheckChangedListener);
		attachFragment(poll);
	}

	private void attachFragment(Fragment f) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction t = fm.beginTransaction();
		t.replace(R.id.content, f);
		t.addToBackStack(null);
		t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		// t.setCustomAnimations(arg0, arg1);
		t.commit();
	}

	@Override
	public void onBackPressed() {
		Log.i(TAG, "elems:" + mElements.size() + " mstep:" + mStep);
		if (mLoanBuy != 0) {
			mLoanBuy = 0;
			mAnswers.remove(mStep);
			// mLoanIndexPolicy = null;
		} else {
			try {
				if (mStep > 0) {
					CityElement elem = mElements.remove(mStep - 1);
					clearBranch(elem);
					mAnswers.remove(mStep - 1);
				}
			} catch (Exception e) {
				Log.e(TAG, e.getCause().toString());
				// mStep = mElements.size();
				// CityElement elem = mElements.remove(mStep - 1);
				// clearAnswer(elem);
			}
			mStep--;
			if (mStep == -1) {
				mValue.clear();
				finish();
			}

			if (mCanBuy <= 0)
				mNumHavePolicy = null;
		}

		mIsBack = true;
		super.onBackPressed();
	}

	private void clearBranch(CityElement elem) {
		if (elem == null) {
			return;
		}
		if (mLoanIndexPolicy != null) {
			mLoanIndexPolicy.setCustomerAnswer(elem.getId(), -1);
			mLoanIndexPolicy = null;
		} else if (mNumHavePolicy != null) {
			mNumHavePolicy.setCustomerAnswer(elem.getId(), -1);
			mNumHavePolicy = null;
		} else {
			mCanBuyPolicy.setCustomerAnswer(elem.getId(), -1);
		}
	}

	private void showResult(int canBuyNum, int loanIndex) {
		if (canBuyNum > 0) {
			// TODO
		}
	}

	private void reStart() {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

		for (CityElement e : mElements) {
			clearBranch(e);
		}

		mStep = -1;
		mElements.clear();
		mAnswers.clear();
		mLoanBuy = 0;

		loadFragment(-1);
	}

	private void go2LastStep() {
		onBackPressed();
	}
}
