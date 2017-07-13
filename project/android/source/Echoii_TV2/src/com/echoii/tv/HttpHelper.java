package com.echoii.tv;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.constants.HttpConstants;
import com.echoii.tv.model.Device;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.model.User;
import com.echoii.tv.util.HttpUtil;
import com.echoii.tv.util.JsonUtil;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>所有Http请求的代理类</p>
 *
 */
public class HttpHelper extends BaseContext{

	public static final String TAG = "HttpHelper";

	public HttpHelper() {
		super();
	}

	public HttpHelper(Context context, Handler handler) {
		super(context, handler);
	}

	public String cloudLogin(User user) 
			throws MalformedURLException, SocketTimeoutException, IOException,
			JSONException {
		
		String url = HttpConstants.CLOUD_LOGIN_URL + "email=" + user.getUsername() + "&password=" + user.getPassword();
		
		String json = null;
		String loginStatus = null;
		try {
			
			json = HttpUtil.cloudLogin(url);
			LogUtil.d(TAG, "cloudLogin : json = " + json);
			loginStatus = JsonUtil.parseCloudLoginStatus(json);
			if (loginStatus == null) {
				loginStatus = Constants.AUTH_ERROR;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 
		return loginStatus;
	}
	
	public String datacenterLogin(Device device) 
			throws MalformedURLException, SocketTimeoutException, IOException,
			JSONException {
		
		String url = HttpConstants.DEVICE_LOGIN_BASE_URL + "device_id=" + device.getDeviceId() + "&token=" + device.getDeviceToken();
		
		String json = null;
		String loginStatus = null;
		try {
			
			json = HttpUtil.datacenterLogin(url);
			LogUtil.d(TAG, "datacenterLogin : json = " + json);
			loginStatus = JsonUtil.parseDatacenterLoginStatus(json);
			if (loginStatus == null) {
				loginStatus = Constants.AUTH_ERROR;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 
		return loginStatus;
	}
	
	public String datacenterRegister(Device device) 
			throws MalformedURLException, SocketTimeoutException, IOException,
			JSONException {
		String url = HttpConstants.DEVICE_REG_BASE_URL + "device_id=" + device.getDeviceId();
		
		String json = null;
		String deviceToken = null;
		try {
			
			json = HttpUtil.datacenterRegister(url);
			LogUtil.d(TAG, "datacenterRegister : json = " + json);
			deviceToken = JsonUtil.parseDevicePassword(json);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 
		
		return deviceToken;
	}
	
	public boolean uploadFile(EchoiiFile file, String url) {

		return false;

	}
	
	public boolean uploadFile(List<EchoiiFile> list, String url) {

		return false;

	}

	public List<EchoiiFile> getAllDatacenterList(Device device)
			throws MalformedURLException, SocketTimeoutException, IOException,
			JSONException {
		
		String url = HttpConstants.DEVICE_LIST_ALL_BASE_URL + "device_id=" + device.getDeviceId() + "&token=" + device.getDeviceToken();
		
		String json = null;
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		
		try {
			
			json = HttpUtil.getAllCloudList(url);
			LogUtil.d(TAG, "getAllDatacenterList : json = " + json);
			list = JsonUtil.parseDatacenterFileList(json);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 
		
		return list;

	}
	
	public List<EchoiiFile> getDatacenterList(Device device, String folderId)
			throws MalformedURLException, SocketTimeoutException, IOException,
			JSONException {
		
		String url = HttpConstants.DEVICE_LIST_FILE_BASE_URL + "device_id=" + device.getDeviceId() + "&token=" + device.getDeviceToken() + "&folder_id=" + folderId;
		
		String json = null;
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		
		try {
			
			json = HttpUtil.getCloudList(url);
			LogUtil.d(TAG, "getDatacenterList : json = " + json);
			list = JsonUtil.parseDatacenterFileList(json);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 
		
		return list;

	}

	public List<EchoiiFile> getCloudList(User user, String folderId, int position) 
			throws MalformedURLException, SocketTimeoutException, IOException, JSONException{
		
		String url = null;
		switch (position) {
		case 0:
			url = HttpConstants.ALLFILE_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&folder_id=" + folderId + "&size=1000&begin=0";
			break;
		case 1:
			url = HttpConstants.IMAGE_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 2:
			url = HttpConstants.MUSIC_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 3:
			url = HttpConstants.VIDEO_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 4:
			url = HttpConstants.BT_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 5:
			url = HttpConstants.DOC_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 6:
			url = HttpConstants.OTHER_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 7:
			url = HttpConstants.GROUP_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;
		case 8:
			url = HttpConstants.SHARE_URL + "user_id=" + user.getId() + "&token=" + user.getPassword() + "&size=1000&begin=0";
			break;

		default:
			break;
		}
		
		String json = null;
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		
		try {
			
			json = HttpUtil.getCloudList(url);
			LogUtil.d(TAG, "getCloudList : json = " + json);
			list = JsonUtil.parseCloudFileList(json);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw e;
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		} 
		
		return list;
	}
	
}
