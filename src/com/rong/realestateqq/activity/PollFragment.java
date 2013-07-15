package com.rong.realestateqq.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.rong.realestateqq.R;
import com.rong.realestateqq.model.CalcDesc;
import com.rong.realestateqq.model.Option;

public class PollFragment extends Fragment {
	private static final String TAG = PollFragment.class.getSimpleName();
	private TextView tvTitle;
	private RadioGroup rgOption;
	private TextView tvTips;
	private ListView lvSample;
	private SampleAdapter mAdapter;
	
	private ArrayList<Option> mOptions;
	private String mTitle;
	private CalcDesc mDesc;
	private OnCheckedChangeListener mListener;

	public static PollFragment newInstance(ArrayList<Option> option,
			String title, CalcDesc desc, OnCheckedChangeListener listener) {
		PollFragment poll = new PollFragment();
		poll.setArgs(option, title, desc, listener);
		return poll;
	}

	public void setArgs(ArrayList<Option> ops, String title, CalcDesc desc,
			OnCheckedChangeListener listener) {
		mOptions = ops;
		mTitle = title;
		mDesc = desc;
		mListener = listener;
	}

	public PollFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_poll, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Activity ac = getActivity();
		tvTitle = (TextView) ac.findViewById(R.id.tv_title);
		rgOption = (RadioGroup) ac.findViewById(R.id.rg_option);
		rgOption.removeAllViewsInLayout();
		tvTips = (TextView) ac.findViewById(R.id.tv_tip);
		lvSample = (ListView) ac.findViewById(R.id.lv_sample);

		tvTitle.setText(mTitle);
		for (Option op : mOptions) {
			RadioButton rb = new RadioButton(ac);
			rb.setText(op.getDesc());
			rb.setId(op.getValue());
			rgOption.addView(rb);
			rgOption.setOnCheckedChangeListener(mListener);
		}
		
		if(mDesc != null) {
			tvTips.setText(mDesc.getTips());
			ArrayList<String> samples = new ArrayList<String>();
			if (mDesc.getSample1() != null && mDesc.getSample1().length() > 0) {
				samples.add(mDesc.getSample1());
			}
			if (mDesc.getSample2() != null && mDesc.getSample2().length() > 0) {
				samples.add(mDesc.getSample2());
			} 
			if (mDesc.getSample3() != null && mDesc.getSample3().length() > 0) {
				samples.add(mDesc.getSample3());
			}
			
			mAdapter = new SampleAdapter(ac, samples);
			lvSample.setAdapter(mAdapter);
		}
	}
	
	private class SampleAdapter extends BaseAdapter {
		private ArrayList<String> mSample;
		private Context mContext;
		
		public SampleAdapter(Context context, ArrayList<String> samples) {
			mContext = context;
			mSample = samples;
		}

		@Override
		public int getCount() {
			return mSample.size();
		}

		@Override
		public String getItem(int position) {
			return mSample.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_sample, null);
			}
			
			TextView tvQuestion = (TextView) convertView.findViewById(R.id.tv_ques);
			TextView tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
			String sample = getItem(position);
			String[] example = sample.split("\r\n");
			if (example.length == 2) {
				tvQuestion.setText(example[0]);
				tvAnswer.setText(example[1]);
			} else {
				Log.e(TAG, "sample format error: " + sample);
			}
			
			return convertView;
		}
		
	}
	
}
