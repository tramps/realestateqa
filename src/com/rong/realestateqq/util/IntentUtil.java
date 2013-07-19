package com.rong.realestateqq.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

/**
 * utils for intent operation.
 */
public class IntentUtil {
	private static final String TAG = "IntentUtil";

	private static void startActivity(Context context, Intent intent) {
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "", e);
		}
	}

	/**
	 * start a browser to view a web site.
	 */
	public static void startWeb(Context context, String url) {
		if (url == null)
			return;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(context, intent);
	}

}
