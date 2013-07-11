package com.rong.realestateqq.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.rong.realestateqq.R;
import com.rong.realestateqq.model.Option;

public class PollFragment extends Fragment {
	private TextView tvTitle;
	private RadioGroup rgOption;
	private ArrayList<Option> mOptions;
	private String mTitle;
	private OnCheckedChangeListener mListener;

	public static PollFragment newInstance(ArrayList<Option> option,
			String title, OnCheckedChangeListener listener) {
		PollFragment poll = new PollFragment();
		poll.setArgs(option, title, listener);
		return poll;
	}

	public void setArgs(ArrayList<Option> ops, String title,
			OnCheckedChangeListener listener) {
		mOptions = ops;
		mTitle = title;
		mListener = listener;
	}

	public PollFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.poll, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Activity ac = getActivity();
		tvTitle = (TextView) ac.findViewById(R.id.tv_title);
		rgOption = (RadioGroup) ac.findViewById(R.id.rg_option);
		rgOption.removeAllViewsInLayout();

		tvTitle.setText(mTitle);
		for (Option op : mOptions) {
			RadioButton rb = new RadioButton(ac);
			rb.setText(op.getDesc());
			rb.setId(op.getValue());
			rgOption.addView(rb);
			rgOption.setOnCheckedChangeListener(mListener);
		}
	}
}
