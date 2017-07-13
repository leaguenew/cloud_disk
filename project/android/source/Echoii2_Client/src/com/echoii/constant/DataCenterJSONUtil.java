package com.echoii.constant;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.echoii.network.socket.DataCenterSocket;
import com.echoii.uploadfile.UploadFile;

public class DataCenterJSONUtil {
	public static final String TAG = "DataCenterJSONUtil";
	public static final String REQUEST_IMAGES_PARAM = "get_all_images";
	public static final String REQUEST_MUSICS_PARAM = "get_all_musics";
	public static final String REQUEST_VIDEOS_PARAM = "get_all_videos";
	public static final String REQUEST_DOCUMENTS_PARAM = "get_all_documents";
	public static final String REQUEST_OTHERS_PARAM = "get_others";
	
	public static String requestFileListJson(String requestCode,String path,int itemId) {
		Log.d(TAG, "item id = " + itemId);
		String json = "";
		try {
			switch (itemId) {
			case 0:
				JSONObject root = new JSONObject();
				root.put("request", requestCode);
				root.put("param", path);
				json = root.toString();
				break;
			case 1:
				JSONObject root1 = new JSONObject();
				root1.put("request", requestCode);
				root1.put("param", REQUEST_IMAGES_PARAM);
				json = root1.toString();
				break;
			case 2:
				JSONObject root2 = new JSONObject();
				root2.put("request", requestCode);
				root2.put("param", REQUEST_MUSICS_PARAM);
				json = root2.toString();
				break;
			case 3:
				JSONObject root3 = new JSONObject();
				root3.put("request", requestCode);
				root3.put("param", REQUEST_VIDEOS_PARAM);
				json = root3.toString();
				break;
			case 5:
				JSONObject root4 = new JSONObject();
				root4.put("request", requestCode);
				root4.put("param", REQUEST_DOCUMENTS_PARAM);
				json = root4.toString();
				break;
			case 8:
				JSONObject root5 = new JSONObject();
				root5.put("request", requestCode);
				root5.put("param", REQUEST_OTHERS_PARAM);
				json = root5.toString();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String requestOperateJson(String requestCode,List<String> filesPath , String toPath) {
		String responseJson = "";
		try {
			if (requestCode.equals(DataCenterSocket.REQUEST_CODE_MOVE)) {
				JSONObject root = new JSONObject();
				root.put("request", requestCode);
				JSONObject paramObj = new JSONObject();
				JSONArray fromPathArray = new JSONArray();
				for (int i = 0; i < filesPath.size(); i++) {
					fromPathArray.put(filesPath.get(i));
				}
				paramObj.put("fromPath", fromPathArray);
				paramObj.put("toPath", toPath);
				root.put("param", paramObj);
				responseJson = root.toString();
			}else if (requestCode.equals(DataCenterSocket.REQUEST_CODE_COPY)) {
				JSONObject root = new JSONObject();
				root.put("request", requestCode);
				JSONObject paramObj = new JSONObject();
				JSONArray fromPathArray = new JSONArray();
				for (int i = 0; i < filesPath.size(); i++) {
					fromPathArray.put(filesPath.get(i));
				}
				paramObj.put("fromPath", fromPathArray);
				paramObj.put("toPath", toPath);
				root.put("param", paramObj);
				responseJson = root.toString();
			}else if(requestCode.equals(DataCenterSocket.REQUEST_CODE_DELETE)){
				JSONObject root = new JSONObject();
				root.put("request", requestCode);
				JSONObject paramObj = new JSONObject();
				JSONArray fromPathArray = new JSONArray();
				for (int i = 0; i < filesPath.size(); i++) {
					fromPathArray.put(filesPath.get(i));
				}
				paramObj.put("fromPath", fromPathArray);
//				paramObj.put("toPath", toPath);
				root.put("param", paramObj);
				responseJson = root.toString();
			}else if (requestCode.equals(DataCenterSocket.REQUEST_CODE_RENAME)) {
				JSONObject root = new JSONObject();
				root.put("request", requestCode);
				JSONObject paramObj = new JSONObject();
				paramObj.put("fromPath", filesPath.get(0));
				paramObj.put("newName", toPath);
				root.put("param", paramObj);
				responseJson = root.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}
	
	public static String requestLoginJson(String requestCode,String password,String clientIp) {
		String responseJson = "";
		try {
			JSONObject root = new JSONObject();
			root.put("request", requestCode);
			JSONObject paramObj = new JSONObject();
			paramObj.put("password", password);
			paramObj.put("clientIp", clientIp);
			root.put("param", paramObj);
			responseJson = root.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}
	
	public static List<UploadFile> parseJson(String json) {
		UploadFile file;
		List<UploadFile> list = new ArrayList<UploadFile>();
		Log.d(TAG, "fileJson = " + json);
		try {
			JSONObject root = new JSONObject(json);
			JSONArray fileArray = root.getJSONArray("data");
			for (int i = 0; i < fileArray.length(); i++) {
				file = new UploadFile();
				file.setFileName(fileArray.getJSONObject(i).getString("name"));
				file.setDate(fileArray.getJSONObject(i).getString("date"));
				file.setFileSize(fileArray.getJSONObject(i).getLong("size"));
				file.setIsCurrentVersion(fileArray.getJSONObject(i).getString(
						"isCurrentVersion"));
				file.setParentRoot(fileArray.getJSONObject(i).getString(
						"parentId"));
				file.setPath(fileArray.getJSONObject(i).getString("path"));
				file.setStatus(fileArray.getJSONObject(i).getString("status"));
				file.setType(fileArray.getJSONObject(i).getString("type"));
				file.setVersion(fileArray.getJSONObject(i).getString("version"));
				list.add(file);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<UploadFile> parseJsonForImage(String json) {
		UploadFile file;
		List<UploadFile> list = new ArrayList<UploadFile>();
		Log.d(TAG, "fileJson = " + json);
		try {
			JSONObject root = new JSONObject(json);
			JSONArray fileArray = root.getJSONArray("data");
			for (int i = 0; i < fileArray.length(); i++) {
				file = new UploadFile(1);
				file.setFileName(fileArray.getJSONObject(i).getString("name"));
				file.setDate(fileArray.getJSONObject(i).getString("date"));
				file.setFileSize(fileArray.getJSONObject(i).getLong("size"));
				file.setIsCurrentVersion(fileArray.getJSONObject(i).getString(
						"isCurrentVersion"));
				file.setParentRoot(fileArray.getJSONObject(i).getString(
						"parentId"));
				file.setPath(fileArray.getJSONObject(i).getString("path"));
				file.setStatus(fileArray.getJSONObject(i).getString("status"));
				file.setType(fileArray.getJSONObject(i).getString("type"));
				file.setVersion(fileArray.getJSONObject(i).getString("version"));
				list.add(file);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String getDownloadJson(String requestCode, String requestPath) {
		JSONObject root = null;
		try {
			root = new JSONObject();
			root.put("request", requestCode);
			root.put("param", requestPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root.toString();
	}

	public static String parseCopyJson(String result) {
		String response = null;
		try {
			JSONObject root = new JSONObject(result);
			response = root.getString("param");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
