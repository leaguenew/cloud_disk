package com.echoii.tv.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class LoginUtil {
	public static final String TAG = "LoginUtil";
	public static final String SUCCESS = "200";
	public static final int MSG_SUCCESS = 0x001;
	public static final String ERROR = "500";
	
	public static String doLogin(Handler handler,String clientPass,String clientIp,String serverPass){
		Log.d(TAG, "clientPass = " + clientPass + " / serverPass = " + serverPass);
		String responseCode = null;
		if (clientPass.equals(serverPass)) {
			responseCode = SUCCESS;
			Message msg = handler.obtainMessage();
			msg.what = MSG_SUCCESS;
			msg.obj = clientIp;
			handler.sendMessage(msg);
		}else{
			responseCode = ERROR;
		}
		return responseCode;
	}

}
