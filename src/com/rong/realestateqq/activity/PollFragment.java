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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TextView;

import com.rong.realestateqq.R;
import com.rong.realestateqq.model.CalcDesc;
import com.rong.realestateqq.model.Option;

public class PollFragment extends Fragment {
	private static final String TAG = PollFragment.class.getSimpleName();
	private TextView tvTitle;
	private LinearLayout rgOption;
	private TextView tvTips;
	private LinearLayout lvSample;
	private SampleAdapter mAdapter;

	private ArrayList<Option> mOptions;
	private String mTitle;
	private CalcDesc mDesc;
//	private OnCheckedChangeListener mListener;
	private OnClickListener mListener;
	private int mSeleted = -1;
	
	private static final String PRE_KEY_SELECTED = "pre_key_selected";

	public static PollFragment newInstance(ArrayList<Option> option,
			String title, CalcDesc desc, OnClickListener listener) {
		PollFragment poll = new PollFragment();
		poll.setArgs(option, title, desc, listener, -1);
		return poll;
	}
	
	public static PollFragment newInstance(ArrayList<Option> option,
			String title, CalcDesc desc, OnClickListener listener, int seleted) {
		PollFragment poll = new PollFragment();
		poll.setArgs(option, title, desc, listener, seleted);
		return poll;
	}

	public void setArgs(ArrayList<Option> ops, String title, CalcDesc desc,
			OnClickListener listener, int selected) {
		mOptions = ops;
		mTitle = title;
		mDesc = desc;
		mListener = listener;
		mSeleted = selected;
	}

	public PollFragment() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		if (savedInstanceState != null) {
			mSeleted = savedInstanceState.getInt(PRE_KEY_SELECTED);
		}
		
		Log.i(TAG, "oncreate");
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "selected" + mSeleted);
		initContent();
	}
	
	private void initContent() {
		Activity ac = getActivity();
		tvTitle = (TextView) ac.findViewById(R.id.tv_title);
		rgOption = (LinearLayout) ac.findViewById(R.id.ll_option);
//		rgOption.removeAllViewsInLayout();
		tvTips = (TextView) ac.findViewById(R.id.tv_tip);
		lvSample = (LinearLayout) ac.findViewById(R.id.lv_sample);

		LayoutParams params = new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 0;
		params.rightMargin = 0;

		tvTitle.setText(mTitle);
		
		rgOption.removeAllViewsInLayout();
		OptionAdapter opAdapter = new OptionAdapter(ac, mOptions);
		for (int i = 0; i < opAdapter.getCount(); i++) {
			rgOption.addView(opAdapter.getView(i, null, null), params);
		}
		
//		int i = 0;
//		for (Option op : mOptions) {
//			i++;
//			CustomedRadioButton rb = new CustomedRadioButton(ac);
//			rb.setText(op.getDesc());
//			rb.setId(op.getValue());
//			rgOption.addView(rb, params);
//			if (i != mOptions.size()-1) {
//				View view = new View(ac);
//				view.setBackgroundResource(R.drawable.divider_light);
//				rgOption.addView(view, params);
//			}
//		}
//		rgOption.setOnCheckedChangeListener(mListener);

		if (mDesc != null) {
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

			lvSample.removeAllViewsInLayout();
			mAdapter = new SampleAdapter(ac, samples);
			for (int i = 0; i < mAdapter.getCount(); i++) {
				lvSample.addView(mAdapter.getView(i, null, null));
			}
		} 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mSeleted = savedInstanceState.getInt(PRE_KEY_SELECTED); 
		}
		return inflater.inflate(R.layout.fragment_poll, null);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(PRE_KEY_SELECTED, mSeleted);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	
	private class OptionAdapter extends BaseAdapter {
		private ArrayList<Option> mOps;
		private Context mContext;
		
		public OptionAdapter(Context context, ArrayList<Option> ops) {
			mContext = context;
			mOps = ops;
		}

		@Override
		public int getCount() {
			if (mOps == null) {
				return 0;
			}
			return mOps.size();
		}

		@Override
		public Option getItem(int position) {
			return mOps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_option, null);
			}
			
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv_check);
//			RadioButton iv = (RadioButton) convertView.findViewById(R.id.iv_check);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_desc);
			View divider = convertView.findViewById(R.id.divider);
			final Option o = getItem(position);
			tv.setText(o.getDesc());
			convertView.setTag(o.getValue());
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						mSeleted = o.getValue();
						mListener.onClick(v);
					}
				}
			});
			if (o.getValue() == mSeleted) {
				Log.i(TAG, "setted");
				iv.setSelected(true);
				iv.setPressed(true);
//				iv.setChecked(true);
			}
			
			if (position == mOps.size() - 1) {
				divider.setVisibility(View.GONE);
			} else {
				divider.setVisibility(View.VISIBLE);
			}
			
			return convertView;
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
			if (mSample == null) {
				return 0;
			}
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
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_sample, null);
			}

			TextView tvQuestion = (TextView) convertView
					.findViewById(R.id.tv_ques);
			TextView tvAnswer = (TextView) convertView
					.findViewById(R.id.tv_answer);
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
