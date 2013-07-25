package com.rong.realestateqq.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rong.realestateqq.R;
import com.rong.realestateqq.model.CityElement;
import com.rong.realestateqq.util.GlobalValue;

public class ResultFragment extends Fragment {
	private static final String TAG = "ResultFragment";

	private int[] mHouseLayout = { R.layout.layout_house_two,
			R.layout.layout_house_two_one, R.layout.layout_house_two_none,
			R.layout.layout_hourse_one_one, R.layout.layout_hourse_one_two,
			R.layout.layout_house_one_none, R.layout.layout_house_zero };

	private int mCityId;
	private int mLeftBuy;
	private int mLoanBuy;
	private ArrayList<Integer> mAnswers;
	private ArrayList<CityElement> mElements;

	private OnClickListener mListener;
	
	private static final String TITLE_RESULT_NONE = "您目前没有买房资格";
	private static final String TITLE_RESULT_ONE = "您可以购买一套房";
	private static final String TITLE_RESULT_TWO = "您可以购买两套房";

	public ResultFragment() {
		super();
	}

	public static ResultFragment newInstance(int cityId, int leftBuy,
			int loanBuy, ArrayList<Integer> answers,
			ArrayList<CityElement> elements, OnClickListener listener) {
		ResultFragment rf = new ResultFragment();
		rf.setArgs(cityId, leftBuy, loanBuy, answers, elements, listener);
		return rf;
	}

	public void setArgs(int cityId, int leftBuy, int loanBuy,
			ArrayList<Integer> answers, ArrayList<CityElement> elements,
			OnClickListener listener) {
		mCityId = cityId;
		mLeftBuy = leftBuy;
		mLoanBuy = loanBuy;
		mAnswers = answers;
		mElements = elements;
		mListener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_result, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Activity ac = getActivity();
		TextView tvResult = (TextView) ac.findViewById(R.id.tv_result);
		FrameLayout flHouse = (FrameLayout) ac.findViewById(R.id.fl_house);
		LinearLayout llPoll = (LinearLayout) ac.findViewById(R.id.ll_poll);
		Button btnRestart = (Button) ac.findViewById(R.id.btn_restart);
		btnRestart.setOnClickListener(mListener);
		
		String title = TITLE_RESULT_NONE;
		if (mLeftBuy == 2) {
			title = TITLE_RESULT_TWO;
		} else if (mLeftBuy == 1) {
			title = TITLE_RESULT_ONE;
		}
		tvResult.setText(title);

		int index = 6;
		if (mLeftBuy == 2) {
			if (mLoanBuy == 2) {
				index = 1;
			} else if (mLoanBuy == 1) {
				index = 0;
			} else {
				index = 2;
			}
		} else if (mLeftBuy == 1) {
			if (mLoanBuy == 1) {
				index = 3;
			} else if (mLoanBuy == 2){
				index = 4;
			} else {
				index = 5;
			}
		}
		flHouse.addView(LayoutInflater.from(ac).inflate(mHouseLayout[index],
				null));

		ArrayList<String> items = new ArrayList<String>();
		GlobalValue gl = GlobalValue.getInts();
		if (mAnswers != null && mElements != null)
		for (int i = 0; i < mAnswers.size(); i++) {
			if (i == 0) {
				items.add("您所在的城市" + "#" + gl.getCityNameById(mCityId));
			} else {
				CityElement elem;
				try {
					elem = mElements.get(i - 1);
					items.add(elem.getTitle() + "#"
							+ elem.getOptionDescByValue(mAnswers.get(i)));
				} catch (Exception e) {
					Log.e(TAG, e.toString());
					// elem = mElements.get(i-2);
					// i++;
				}
			}
		}

		AnswerAdaper adapter = new AnswerAdaper(ac, items);
		for (int i = 0; i < adapter.getCount(); i++) {
			llPoll.addView(adapter.getView(i, null, null));
		}
	}

	private class AnswerAdaper extends BaseAdapter {
		private Context mContext;
		private ArrayList<String> mItems;

		public AnswerAdaper(Context context, ArrayList<String> items) {
			mContext = context;
			mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public String getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_answer, null);
			}

			TextView tvQues = (TextView) convertView.findViewById(R.id.tv_ques);
			TextView tvAns = (TextView) convertView.findViewById(R.id.tv_ans);
			String item = mItems.get(position);
			String[] spilts = item.split("#");
			tvQues.setText(spilts[0] + ":");
			tvAns.setText(spilts[1]);
			return convertView;
		}

	}

}
