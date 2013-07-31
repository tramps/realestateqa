package com.rong.realestateqq.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.parse.PushService;
import com.rong.realestateqq.activity.PushReceiverActivity;
import com.rong.realestateqq.exception.JsonParseException;
import com.rong.realestateqq.json.JsonHelper;
import com.rong.realestateqq.model.CityLocation;
import com.rong.realestateqq.model.IPCity;
import com.rong.realestateqq.task.HandleMessageTask;
import com.rong.realestateqq.task.HandleMessageTask.Callback;
import com.rong.realestateqq.task.UpdateTask;

public class NetHelper {
	private static final String TAG = "UpdataHelper";

	private static final String API_DOMAIN = "http://www.rong360.com/api/";
	private static final String SUFFIX_UPDATE = "hpdata?lasttime=";
	private static final String SUFFIX_LOCATION = "iplocation.html";
	// private static final String
	public static final String DEFAULT_FILE = "jsondata";
	public static final String DEFAULT_FOLDER = "model";
	private static final String NO_UPDATE = "0";
	
	public static final String PREFERENCE_NET = "preference_net";
	private static final String PRE_KEY_IS_SUBSCRIBED = "pre_key_is_subscribed";
	private static final String PRE_KEY_DOMAIN = "pre_key_domain";
	public static final String	PRE_KEY_UPDATE_TIME = "pre_key_update_time";
	
	private static final String SUFFIX_APK = "apk";

	public static File getJsonModelFile(Context context) {
		return FileUtil.getFile(context, DEFAULT_FOLDER, DEFAULT_FILE);
	}

	public static void updateModel(final Context context) {
		final SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NET, Context.MODE_PRIVATE);
		long lastUpdateTime = sp.getLong(PRE_KEY_UPDATE_TIME, 0);
		String url = API_DOMAIN + SUFFIX_UPDATE + lastUpdateTime;
		
		Log.i(TAG, "url: " + url);
		UpdateTask task = new UpdateTask(context, url);
		task.setCallback(new Callback() {

			@Override
			public void onSuccess(HandleMessageTask task, Object t) {
				String res = ((UpdateTask) task).getResult();
				if (res.equalsIgnoreCase(NO_UPDATE)) {

					Log.i(TAG, "NO UPDATE");
					return;
				}

				File model = FileUtil.getFile(context, DEFAULT_FOLDER,
						DEFAULT_FILE);
				FileUtil.writeFile(model, res);

				Editor e = sp.edit();
				e.putLong(PRE_KEY_UPDATE_TIME, System.currentTimeMillis() / 1000);
				e.commit();
				Log.i(TAG, "UPDATE SUCCESS");
			}

			@Override
			public void onFail(HandleMessageTask task, Object t) {

			}
		});

		task.execute();
	}

	public static void locateCity(final Context context) {
		String url = API_DOMAIN + SUFFIX_LOCATION;
		UpdateTask task = new UpdateTask(context, url);
		task.setCallback(new Callback() {

			@Override
			public void onSuccess(HandleMessageTask task, Object t) {
				String res = ((UpdateTask) task).getResult();
				if (res == null || res.length() == 0) {
					Log.i(TAG, "No City Location");
					return;
				}	
				
				Log.i(TAG, res);
				try {
					CityLocation city = JsonHelper.parseJSONToObject(
							CityLocation.class, res);
					IPCity l = city.mData;
					if (l == null) {
						return;
					} else {
						String domain = l.mDomain;  
						Log.i(TAG, domain);
						if (domain != null && domain.length() > 0) {
							subscribeCity(context, domain);
						}
					}
				} catch (JsonParseException e) {
					Log.e(TAG, e.toString());
				}
			}

			@Override
			public void onFail(HandleMessageTask task, Object t) {
			}
		});
		
		task.execute();
	}
	
	public static void subscribeCity(Context context, String city) {
		SharedPreferences p = context.getSharedPreferences(PREFERENCE_NET, Context.MODE_PRIVATE);
		PushService.subscribe(context, city, PushReceiverActivity.class);
		Editor e = p.edit();
		e.putBoolean(PRE_KEY_IS_SUBSCRIBED, true);
		e.putString(PRE_KEY_DOMAIN, city);
		e.commit();
	}
	
	public static void subscribe2Parse(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NET, context.MODE_PRIVATE);
		boolean isSubed = sp.getBoolean(PRE_KEY_IS_SUBSCRIBED, false);
		if (!isSubed) {
			Log.i(TAG, "subscribe");
			locateCity(context);
		}
	}
}
