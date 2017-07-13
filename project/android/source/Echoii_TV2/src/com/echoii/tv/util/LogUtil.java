package com.echoii.tv.util;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>Log日志输出工具类</p>
 *
 */
public class LogUtil {
	
	public static final boolean debug = true;
	
	public static void d(String TAG, String msg) {
		if (debug) {
			android.util.Log.d(TAG, msg);
		}
	}
	
	public static void i(String TAG, String msg) {
		android.util.Log.i(TAG, msg);
	}

}
