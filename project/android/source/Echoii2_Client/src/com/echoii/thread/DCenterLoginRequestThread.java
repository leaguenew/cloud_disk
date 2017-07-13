package com.echoii.thread;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.echoii.network.socket.DataCenterSocket;

public class DCenterLoginRequestThread extends Thread {
	public static final String TAG = "DCenterLoginRequestThread";
	public static final int SUCCESS = 200;
	public static final int ERROR = 500;
	private Handler mHandler;
	private String serverIp;
	private String password;
	private String clientIp;
	private String requestCode;
	private DataCenterSocket socket;

	public DCenterLoginRequestThread(Handler handler, Activity activity,String serverIp,
			String password, String clientIp, String requestCode) {
		super();
		this.mHandler = handler;
		this.serverIp = serverIp;
		this.password = password;
		this.clientIp = clientIp;
		this.requestCode = requestCode;
		socket = new DataCenterSocket(handler, activity);
	}

	@Override
	public void run() {
		super.run();
		String responseCode = socket.doLoginRequest(serverIp, requestCode, password, clientIp);
		if (responseCode != null) {
			try {
				JSONObject root = new JSONObject(responseCode);
				responseCode = root.getString("param");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (responseCode.equals("200")) {
				Message msg = mHandler.obtainMessage();
				msg.what = SUCCESS;
				mHandler.sendMessage(msg);
			}else{
				Message msg = mHandler.obtainMessage();
				msg.what = ERROR;
				mHandler.sendMessage(msg);
			}
		}else{
			Message msg = mHandler.obtainMessage();
			msg.what = ERROR;
			mHandler.sendMessage(msg);
		}
	}

}
