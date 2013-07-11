package com.rong.realestateqq.util;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.rong.realestateqq.task.HandleMessageTask;
import com.rong.realestateqq.task.HandleMessageTask.Callback;
import com.rong.realestateqq.task.UpdateTask;

public class UpdataHelper {
	private static final String TAG = "UpdataHelper";
	
	private static final String API = "http://www.rong360.com/api/hpdata?lasttime=";
	public static final String DEFAULT_FILE = "jsondata";
	public static final String DEFAULT_FOLDER = "model";
	private static final String NO_UPDATE = "0";
	
	public static File getJsonModelFile (Context context) {
		return FileUtil.getFile(context, DEFAULT_FOLDER, DEFAULT_FILE);
	}
	
	public static void updateModel(final Context context) {
		long time = System.currentTimeMillis() / 1000;
		String url = API + 0;
		UpdateTask task = new UpdateTask(context, url);
		task.setCallback(new Callback() {
			
			@Override
			public void onSuccess(HandleMessageTask task, Object t) {
				String res = ((UpdateTask)task).getResult();
				if (res.equalsIgnoreCase(NO_UPDATE)) {
					
					Log.i(TAG, "NO UPDATE");
					return;
				}
				
				File model = FileUtil.getFile(context, DEFAULT_FOLDER, DEFAULT_FILE);
				FileUtil.writeFile(model, res);
				
				Log.i(TAG, "UPDATE SUCCESS");
			}
			
			@Override
			public void onFail(HandleMessageTask task, Object t) {
				
			}
		});
		
		task.execute();
	}
}
