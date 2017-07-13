package com.echoii.tv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.model.Device;
import com.echoii.tv.util.CommonUtil;
import com.echoii.tv.util.JsonUtil;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心注册与登录封装类</p>
 *
 */
public class DatacenterAuth extends BaseContext{

	public static final String TAG = "DatacenterAuth";
	
	public static String loginStatus = Constants.AUTH_ERROR;
	
	private HttpHelper helper;
	
	public DatacenterAuth() {
		super();
		helper = new HttpHelper();
	}
	
	public DatacenterAuth(Context context, Handler handler) {
		super(context, handler);
		helper = new HttpHelper();
	}
	
	public void login(Device device) 
			throws SocketTimeoutException, MalformedURLException, IOException, JSONException{
		
		try {
			
			if (device.getDeviceToken() == null) {
				
				String deviceToken = httpRegister(device);
				if (deviceToken == null) {
					LogUtil.d(TAG, "register failed.");
					return ;
				}
				device.setDeviceToken(deviceToken);
				httpLogin(device);
			} else {
				httpLogin(device);
			}
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
	}

	private void httpLogin(Device device) 
			throws SocketTimeoutException, MalformedURLException, IOException, JSONException {
		
		String loginResponse = null;
		try {
			
			loginResponse = helper.datacenterLogin(device);
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		if (loginResponse.equals(Constants.SUCCESS)) {
			loginStatus = Constants.SUCCESS;
		} else {
			loginStatus = Constants.AUTH_ERROR;
		}
	}

	private String httpRegister(Device deivce) 
			throws SocketTimeoutException, MalformedURLException, IOException, JSONException{
		
		String deviceToken = null;
		
		try {
			
			deviceToken = helper.datacenterRegister(deivce);
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		saveDeviceInfo(deivce.getDeviceId(), deviceToken);
		return deviceToken;
	}

	private void saveDeviceInfo(String deviceId, String deviceToken) 
		throws FileNotFoundException, IOException, JSONException{
		
		try {
			String json = JsonUtil.genDeviceInfo(deviceId, deviceToken);
			LogUtil.d(TAG, "gen device info = " + json);
			CommonUtil.writeFile(json);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
}
