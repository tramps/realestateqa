package com.rong.realestateqq.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.rong.realestateqq.R;
import com.rong.realestateqq.model.CalcDesc;
import com.rong.realestateqq.model.CityElement;
import com.rong.realestateqq.model.CityPolicy;
import com.rong.realestateqq.model.HpCity;
import com.rong.realestateqq.model.Option;
import com.rong.realestateqq.util.GlobalValue;

public class MainActivity extends BaseFragmentActivity {
	private static final String TAG = "MainActivity";
	private FrameLayout flContent;
	private CityPolicy mCanBuyPolicy;
	private CityPolicy mNumHavePolicy;
	private CityPolicy mLoanIndexPolicy;
	private int mCityId = -1;
	private int mStep = -1;
	private ArrayList<CityElement> mElements;
	private ArrayList<Integer> mAnswers;
	private CalcDesc mDesc;
	private GlobalValue mValue;
	private boolean mIsBack = false;
	private int mCanBuy;
	private int mLeftBuy;
	private int mLoanBuy;
	
	private boolean mIsResulted = false;
	
	private static final String TITLE_TEST = "购房资格测试";
	private static final String TITLE_RESULT = "测试结果";

	private static final String SETTING = "关于";
	private static final String SHARE = "分享";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mElements = new ArrayList<CityElement>();
		mAnswers = new ArrayList<Integer>();
		mValue = GlobalValue.getInts();

		getSupportActionBar().setTitle(TITLE_TEST);

		Log.i(TAG, "load");
		loadFragment(mStep);
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, R.id.setting, 0, SETTING);
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.setting) {
			Button btn = (Button)getSupportActionBar().findViewById(R.id.setting);
			if (btn.getText().equals(SETTING)) {
				Intent intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TITLE, "share");
				intent.putExtra(Intent.EXTRA_TEXT, "我的#购房资格测试#结果：“可以在北京贷款买房”。你也来测试一下吧，\n 测试地址：http://www.rong360.com/calculator/xiangou");
				startActivity(Intent.createChooser(intent, "Please choose..."));
			}
			
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected int getLayout() {
		return R.layout.activity_main;
	}

	private OnCheckedChangeListener mCheckChangedListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (!mIsBack) {
				if (checkedId == -1) {
					getSupportActionBar().setDisplayHomeAsUpEnabled(false);
				} else {
					getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				}
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
			title = "您所在的城市";
			ArrayList<HpCity> cities = GlobalValue.getInts().getCities();
			for (HpCity city : cities) {
				Option op = new Option(city.getName(), city.getId());
				ops.add(op);
			}
		} else if (mStep == 0) {
			mCityId = checkedId;
			mCanBuyPolicy = mValue.getPolicyById(mCityId,
					CityPolicy.TOKEN_CAN_BUY);
			if (mCanBuyPolicy == null) {
				Log.e(TAG, "data error, no corresponding city" + mCityId);
				finish();
			}
//			Log.i(TAG, "cityPolicy:" + mCanBuyPolicy.getCityId() + "  "
//					+ mCanBuyPolicy.getMeaning());
		}

		int nextElemId = -1;
		if (mStep > -1) {
			int lastElemId;
			if (mStep == 0) {
				lastElemId = -1;
			} else {
				lastElemId = mElements.get(mStep - 1).getId();
			}

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
//					Toast.makeText(this, "SORRY, BUT YOU CAN'T BUY!",
//							Toast.LENGTH_SHORT).show();
					showResult();
					mAnswers.add(checkedId);
					mStep++;
					Log.i(TAG, "elem size: " + mElements.size() + " answers size: " + mAnswers.size());
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
//						Toast.makeText(this, "SORRY, BUT YOU BUY TOO MUCH!",
//								Toast.LENGTH_SHORT).show();
						showResult();
						mAnswers.add(checkedId);
						mStep++;
						
						Log.i(TAG, "elem size: " + mElements.size() + " answers size: " + mAnswers.size());
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

			mAnswers.add(checkedId);
			
			if (mLoanBuy != 0) {
//				title = "还可以买" + mLeftBuy + "套房;" + "贷款算" + mLoanBuy + "贷款";
				showResult();
				return;
			} else {
				CityElement elem = mValue.getElementById(nextElemId);
				mElements.add(elem);
				for (Option op : elem.getData()) {
					ops.add(op);
				}
				title = elem.getTitle();
			}

		}
		if (mLoanBuy == 0) {
			mStep++;
		}
		
		Log.i(TAG, "elem size: " + mElements.size() + " answers size: " + mAnswers.size());

		mDesc = mValue.getCalcDescByElemId(nextElemId, mCityId);
		PollFragment poll = PollFragment.newInstance(ops, title, mDesc,
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
		if (mIsResulted) {
			if (mLoanBuy != 0) {
				mLoanBuy = 0;
			} else {
				mStep--;
			}
			mAnswers.remove(mStep);
			mIsResulted = false;
			// mLoanIndexPolicy = null;
		} else {
			mLeftBuy = 0;
			try {
				if (mStep > 0) {
					CityElement elem = mElements.remove(mStep - 1);
					clearBranch(elem);
					mAnswers.remove(mStep - 1);
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
				// mStep = mElements.size();
				// CityElement elem = mElements.remove(mStep - 1);
				// clearAnswer(elem);
			}
			mStep--;
			if (mStep == -1) {
				// if (mValue != null)
				// mValue.clear();
				finish();
			}

			if (mCanBuy <= 0)
				mNumHavePolicy = null;
		}
		
		if (getSupportActionBar().getTitleText().getText().equals(TITLE_RESULT)) {
			getSupportActionBar().setTitle(TITLE_TEST);
			((Button)getSupportActionBar().findViewById(R.id.setting)).setText(SETTING);
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

	private void showResult() {
		mIsResulted = true;
		ResultFragment rf = ResultFragment.newInstance(mCityId, mLeftBuy,
				mLoanBuy, mAnswers, mElements, mClearListener);
		
		getSupportActionBar().setTitle(TITLE_RESULT);
		((Button)getSupportActionBar().findViewById(R.id.setting)).setText(SHARE);
		attachFragment(rf);
	}

	private void reStart() {
		FragmentManager fm = getSupportFragmentManager();
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

		for (CityElement e : mElements) {
			clearBranch(e);
		}

		mStep = -1;
		mCityId = -1;
		mElements.clear();
		mAnswers.clear();
		mLoanBuy = 0;

		loadFragment(-1);
	}

	private OnClickListener mClearListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			getSupportActionBar().setTitle(TITLE_TEST);
			reStart();
		}
	};

	private void go2LastStep() {
		onBackPressed();
	}
}
