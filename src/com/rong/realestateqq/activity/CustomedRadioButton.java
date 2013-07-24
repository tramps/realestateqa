package com.rong.realestateqq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RadioButton;

import com.rong.realestateqq.R;
import com.rong.realestateqq.util.DisplayUtils;

public class CustomedRadioButton extends RadioButton {

	@SuppressLint("NewApi")
	public CustomedRadioButton(Context context) {
		super(context);
		setButtonDrawable(R.drawable.ic_radiobutton);
//		setCompoundDrawablePadding(DisplayUtils.getPixel(context, 15));
		setPadding(DisplayUtils.getPixel(context, 20),
				DisplayUtils.getPixel(context, 12), 0,
				DisplayUtils.getPixel(context, 12));
//		setPaddingRelative(DisplayUtils.getPixel(context, 12), 0, 0, 0);
//		setBackgroundResource(R.drawable.bkg_rg);
	}

	@Override
	public void toggle() {
		super.toggle();

		if (isChecked() || isFocused()) {

		}
	}

}
