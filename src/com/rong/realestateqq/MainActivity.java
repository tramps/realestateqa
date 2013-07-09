package com.rong.realestateqq;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

import com.rong.realestateqq.exception.JsonParseException;
import com.rong.realestateqq.json.JsonHelper;
import com.rong.realestateqq.util.GlobalValue;

public class MainActivity extends FragmentActivity {
	private TextView tvContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvContent = (TextView) findViewById(R.id.tv_content);
		
		StringBuilder sb = new StringBuilder();
		try {
			JsonHelper.parseJSONFromRaw(getApplicationContext());
		} catch (JsonParseException e) {
			e.printStackTrace();
		}
		
		GlobalValue global = GlobalValue.getInts();
		sb.append(JsonHelper.parseListToJsonArray(global.getCities()).toString());
		tvContent.setText(sb.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
