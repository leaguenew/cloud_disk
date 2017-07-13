package com.echoii.tv.util;

import com.echoii.tv.constants.Constants;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>自定义SharedPreference存储工具类</p>
 *
 */
public class PreferencesUtil{
	
	public static void putString(Context context, String key, String value) {
		SharedPreferences preference = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		preference.edit().putString(key, value).commit();
	}
	
	public static String getString(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		return preference.getString(key, null);
	}
	
	public static void putInt (Context context, String key, int value) {
		SharedPreferences preference = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		preference.edit().putInt(key, value).commit();
	}
	
	public static int getInt(Context context, String key) {
		SharedPreferences preference = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		return preference.getInt(key, -1);
	}
	
}
