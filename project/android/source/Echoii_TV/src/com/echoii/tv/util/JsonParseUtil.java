package com.echoii.tv.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.os.Handler;
import android.util.Log;

public class JsonParseUtil {
	public static final String TAG = "JsonParseUtil";
	private Handler mHandler;
	private Service mService;

	public JsonParseUtil(Handler mHandler, Service mService) {
		super();
		this.mHandler = mHandler;
		this.mService = mService;
	}

	public String login(String requestCode, JSONObject requestRoot,String serverPass) {
		String responseJson = "";
		try {
			JSONObject paramObj = requestRoot.getJSONObject("param");
			String clientPass = paramObj.getString("password");
			String clientIp = paramObj.getString("clientIp");
			String responseCode = LoginUtil.doLogin(mHandler,clientPass, clientIp , serverPass);
			responseJson = CommonUtil.makeResponseJson(requestCode,
					responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	public String renameFile(String requestCode, JSONObject requestRoot) {
		String responseJson = "";
		try {
			JSONObject paramObj = requestRoot.getJSONObject("param");
			String fromPath = paramObj.getString("fromPath");
			String newFileName = paramObj.getString("newName");
			String responseCode = FileOperateFactory.renameFile(fromPath, newFileName);
			responseJson = CommonUtil.makeResponseJson(requestCode,
					responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	public String deleteFiles(String requestCode, JSONObject requestRoot) {
		String responseJson = "";
		List<String> fromPath = new ArrayList<String>();
		try {
			JSONObject paramObj = requestRoot.getJSONObject("param");
			JSONArray fromPathArray = paramObj.getJSONArray("fromPath");
			for (int i = 0; i < fromPathArray.length(); i++) {
				fromPath.add(fromPathArray.getString(i));
			}
			String responseCode = FileOperateFactory.deleteFiles(fromPath);
			responseJson = CommonUtil.makeResponseJson(requestCode,
					responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	public String copyFiles(String requestCode, JSONObject requestRoot) {
		String responseJson = "";
		List<String> fromPath = new ArrayList<String>();
		String toPath = "";
		try {
			JSONObject paramObj = requestRoot.getJSONObject("param");
			JSONArray fromPathArray = paramObj.getJSONArray("fromPath");
			for (int i = 0; i < fromPathArray.length(); i++) {
				fromPath.add(fromPathArray.getString(i));
			}
			toPath = paramObj.getString("toPath");
			String responseCode = FileOperateFactory.copyFiles(fromPath, toPath);
			Log.d(TAG, "copyFile response code = " + responseCode);
			responseJson = CommonUtil.makeResponseJson(requestCode,
					responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	public String moveFiles(String requestCode, JSONObject requestRoot) {
		String responseJson = "";
		List<String> fromPath = new ArrayList<String>();
		String toPath = "";
		try {
			JSONObject paramObj = requestRoot.getJSONObject("param");
			JSONArray fromPathArray = paramObj.getJSONArray("fromPath");
			for (int i = 0; i < fromPathArray.length(); i++) {
				fromPath.add(fromPathArray.getString(i));
			}
			toPath = paramObj.getString("toPath");
			String responseCode = CommonUtil.moveFiles(fromPath, toPath);
			responseJson = CommonUtil.makeResponseJson(requestCode,responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

}
