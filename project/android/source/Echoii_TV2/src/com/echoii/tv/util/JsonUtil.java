package com.echoii.tv.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.constants.HttpConstants;
import com.echoii.tv.model.EchoiiFile;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>Json工具类</p>
 *
 */
public class JsonUtil {
	
	public static List<EchoiiFile> parseDatacenterFileList(String json) throws JSONException{
		
		if (json == null) {
			return null;
		}
		
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		try {
			JSONObject root = new JSONObject(json);
			String status = root.getString("status");
			if (!status.equals("200")) {
				return null;
			}
			String time = root.getString("time");
			JSONArray data = root.getJSONArray("data");
			for (int i = 0; i < data.length(); i++) {
				JSONObject item = data.getJSONObject(i);
				
				EchoiiFile echoiiFile = new EchoiiFile();
				echoiiFile.setCreateDate(item.getString("createDate"));
				echoiiFile.setForderId(item.getString("folderId"));
				echoiiFile.setId(item.getString("id"));
				echoiiFile.setIdx(item.getInt("idx"));
				echoiiFile.setIsCurrentVersion(item.getString("isCurrentVersion"));
				echoiiFile.setLmfDate(item.getString("lmf_date"));
				echoiiFile.setMetaFolder(item.getString("metaFolder"));
				echoiiFile.setMetaId(item.getString("metaId"));
				echoiiFile.setName(item.getString("name"));
				echoiiFile.setPath(item.getString("path"));
				echoiiFile.setSize(item.getString("size"));
				echoiiFile.setStatus(item.getString("status"));
				echoiiFile.setType(item.getString("type"));
				echoiiFile.setUserId(item.getString("deviceId"));
				echoiiFile.setVersion(item.getString("version"));
				
				list.add(echoiiFile);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return list;
		
	}
	
	public static List<EchoiiFile> parseCloudFileList(String json) throws JSONException{
		
		if (json == null) {
			return null;
		}
		
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		try {
			JSONObject root = new JSONObject(json);
			String status = root.getString("status");
			if (!status.equals("200")) {
				return null;
			}
			String time = root.getString("time");
			JSONArray data = root.getJSONArray("data");
			for (int i = 0; i < data.length(); i++) {
				JSONObject item = data.getJSONObject(i);
				
				EchoiiFile echoiiFile = new EchoiiFile();
				echoiiFile.setCreateDate(item.getString("createDate"));
				echoiiFile.setForderId(item.getString("folderId"));
				echoiiFile.setId(item.getString("id"));
				echoiiFile.setIdx(item.getInt("idx"));
				echoiiFile.setIsCurrentVersion(item.getString("isCurrentVersion"));
				echoiiFile.setLmfDate(item.getString("lmf_date"));
				echoiiFile.setMetaFolder(item.getString("metaFolder"));
				echoiiFile.setMetaId(item.getString("metaId"));
				echoiiFile.setName(item.getString("name"));
				echoiiFile.setPath(item.getString("path"));
				echoiiFile.setSize(item.getString("size"));
				echoiiFile.setStatus(item.getString("status"));
				echoiiFile.setType(item.getString("type"));
				echoiiFile.setUserId(item.getString("userId"));
				echoiiFile.setVersion(item.getString("version"));
				
				list.add(echoiiFile);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return list;
		
	}
	
	public static EchoiiFile parseCloudFile(String json) throws JSONException{
		
		EchoiiFile file = new EchoiiFile();
		
		try {
			JSONObject root = new JSONObject(json);
			
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return file;
	}

	public static String parseDevicePassword(String json) throws JSONException{
		
		if (json == null) {
			return null;
		}
		
		try {
			JSONObject root = new JSONObject(json);
			String status = root.getString("status");
			if (!status.equals("200")) {
				return null;
			}
			String time = root.getString("time");
			String data = root.getString("data");
			if (!(data == null || "".equals(data))) {
				return data;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return null;
	}

	public static String parseDatacenterLoginStatus(String json) throws JSONException{
		if (json == null) {
			return null;
		}
		
		try {
			JSONObject root = new JSONObject(json);
			String status = root.getString("status");
			if (status.equals("200")) {
				return Constants.SUCCESS;
			}
			String time = root.getString("time");
			String data = root.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return null;
	}
	
	public static String parseCloudLoginStatus(String json) throws JSONException{
		if (json == null) {
			return null;
		}
		
		try {
			JSONObject root = new JSONObject(json);
			String status = root.getString("status");
			if (status.equals("200")) {
				String time = root.getString("time");
				JSONObject data = root.getJSONObject("data");
				HttpConstants.USER_ID = data.getString("id");
				HttpConstants.PASSWORD = data.getString("token");
				return Constants.SUCCESS;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return null;
	}

	public static String genDeviceInfo(String id, String password) throws JSONException{
		
		JSONObject root = new JSONObject();
		try {
			root.put(Constants.DEVICE_ID, id);
			root.put(Constants.DEVICE_PASSWORD, password);
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		return root.toString();
		
	}
	
	public static String parseDeviceInfo(String json) throws JSONException{
		
		if (json == null || json.equals("")) {
			return null;
		}
		String password = null;
		
		try {
			
			JSONObject root = new JSONObject(json);
			password = root.getString(Constants.DEVICE_PASSWORD);
			
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		
		return password;
	}

}
