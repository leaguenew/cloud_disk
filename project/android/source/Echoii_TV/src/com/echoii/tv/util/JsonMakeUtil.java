package com.echoii.tv.util;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.echoii.tv.service.TVService;

public class JsonMakeUtil {
	public static final String TAG = "JSONUtil";

	public static String getAllFiles(String param) throws Exception{
		File[] files = CommonUtil.getFileList(param);
		JSONObject root = new JSONObject();
		JSONArray array = new JSONArray();
		if (files!=null&&files.length>0) {
			for (File file : files) {
				Log.d(TAG, "get all files -- file name = " + file.getName());
				if (!file.getName().startsWith(".")) {
					JSONObject uploadFileObject = new JSONObject();
					uploadFileObject.put("date", file.lastModified());
					uploadFileObject.put("isCurrentVersion", "0");
					uploadFileObject.put("name", file.getName());
					uploadFileObject.put("parentId", file.getParentFile().getAbsolutePath());
					uploadFileObject.put("path", file.getAbsolutePath());
					uploadFileObject.put("size", CommonUtil.getDirSize(file));
					uploadFileObject.put("status", "aviliable");
					uploadFileObject.put("type", file.isDirectory()?"folder":"file");
					uploadFileObject.put("version", "0");
					array.put(uploadFileObject);
				}
			}
			root.put("data", array);
		}
		return root.toString();
	}

	public static String getAllImages(Service service) throws Exception{
		synchronized (service) {
			Cursor cursor = service.getContentResolver().query(  
	    			MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,  
	    			MediaStore.Images.Media.DEFAULT_SORT_ORDER); 
			JSONObject root = new JSONObject();
			JSONArray array = new JSONArray();
			for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));   
	    		String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));   
	    		long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)); 
				long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
				Log.d(TAG, "image date = " + time + "column index = " + cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
				JSONObject uploadFileObject = new JSONObject();
				uploadFileObject.put("date", time * 1000);
				uploadFileObject.put("isCurrentVersion", "0");
				uploadFileObject.put("name", tilte);
				uploadFileObject.put("parentId", CommonUtil.getParentPath(url));
				uploadFileObject.put("path", url);
				uploadFileObject.put("size", size);
				uploadFileObject.put("status", "aviliable");
				uploadFileObject.put("type", "file");
				uploadFileObject.put("version", "0");
				array.put(uploadFileObject);
			}
			root.put("data", array);
			return root.toString();
		}
    }
	
	public static String getAllImages(String path) throws Exception{
		JSONArray othersArray = new JSONArray();
		JSONObject root = new JSONObject();
		File file = new File(path);
		File[] files = file.listFiles();
		getAllImagesJson(files,othersArray);
		root.put("data", othersArray);
		return root.toString();
    }
	
	public static void getAllImagesJson(File[] files,JSONArray othersArray) throws Exception{
		for (File f : files) {
			if (f.isFile()) {
				String name = f.getName();
				if ("image".equals(CommonUtil.getFileType(name))) {
					JSONObject uploadFileObject = new JSONObject();
					uploadFileObject.put("date", f.lastModified());
					uploadFileObject.put("isCurrentVersion", "0");
					uploadFileObject.put("name", name);
					uploadFileObject.put("parentId",f.getParentFile().getAbsolutePath());
					uploadFileObject.put("path", f.getAbsolutePath());
					uploadFileObject.put("size", f.length());
					uploadFileObject.put("status", "aviliable");
					uploadFileObject.put("type", "file");
					uploadFileObject.put("version", "0");
					othersArray.put(uploadFileObject);
				}
			}else{
				File[] f1 = f.listFiles();
				if (f1 != null && f1.length > 0) {
					getAllImagesJson(f1,othersArray);
				}
			}
		}
	}

	public static String getAllMusics(TVService tvService) throws Exception{
		synchronized (tvService) {
			Cursor cursor = tvService.getContentResolver().query(  
	                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,  
	                MediaStore.Audio.Media.DEFAULT_SORT_ORDER); 
			JSONObject root = new JSONObject();
			JSONArray array = new JSONArray();
			for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));   
	    		String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));   
	    		long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)); 
				long time = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED));
				JSONObject uploadFileObject = new JSONObject();
				uploadFileObject.put("date", time);
				uploadFileObject.put("isCurrentVersion", "0");
				uploadFileObject.put("name", tilte);
				uploadFileObject.put("parentId", CommonUtil.getParentPath(url));
				uploadFileObject.put("path", url);
				uploadFileObject.put("size", size);
				uploadFileObject.put("status", "aviliable");
				uploadFileObject.put("type", "file");
				uploadFileObject.put("version", "0");
				array.put(uploadFileObject);
			}
			root.put("data", array);
			return root.toString();
		}
	}
	
	public static String getAllMusics(String path) throws Exception{
		JSONArray othersArray = new JSONArray();
		JSONObject root = new JSONObject();
		File file = new File(path);
		File[] files = file.listFiles();
		getAllMusicsJson(files,othersArray);
		root.put("data", othersArray);
		return root.toString();
	}
	
	public static void getAllMusicsJson(File[] files,JSONArray othersArray) throws Exception{
		for (File f : files) {
			if (f.isFile()) {
				String name = f.getName();
				if ("music".equals(CommonUtil.getFileType(name))) {
					JSONObject uploadFileObject = new JSONObject();
					uploadFileObject.put("date", f.lastModified());
					uploadFileObject.put("isCurrentVersion", "0");
					uploadFileObject.put("name", name);
					uploadFileObject.put("parentId",f.getParentFile().getAbsolutePath());
					uploadFileObject.put("path", f.getAbsolutePath());
					uploadFileObject.put("size", f.length());
					uploadFileObject.put("status", "aviliable");
					uploadFileObject.put("type", "file");
					uploadFileObject.put("version", "0");
					othersArray.put(uploadFileObject);
				}
			}else{
				File[] f1 = f.listFiles();
				if (f1 != null && f1.length > 0) {
					getAllMusicsJson(f1,othersArray);
				}
			}
		}
	}

	public static String getAllVideos(TVService tvService) throws Exception{
		synchronized (tvService) {
			Cursor cursor = tvService.getContentResolver().query(  
	    			MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,  
	    			MediaStore.Video.Media.DEFAULT_SORT_ORDER); 
			JSONObject root = new JSONObject();
			JSONArray array = new JSONArray();
			for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
				String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));   
	    		String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));   
	    		long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)); 
				long time = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED));
				JSONObject uploadFileObject = new JSONObject();
				uploadFileObject.put("date", time);
				uploadFileObject.put("isCurrentVersion", "0");
				uploadFileObject.put("name", tilte);
				uploadFileObject.put("parentId", CommonUtil.getParentPath(url));
				uploadFileObject.put("path", url);
				uploadFileObject.put("size", size);
				uploadFileObject.put("status", "aviliable");
				uploadFileObject.put("type", "file");
				uploadFileObject.put("version", "0");
				array.put(uploadFileObject);
			}
			root.put("data", array);
			return root.toString();
		}
	}
	
	public static String getAllVideos(String path) throws Exception{
		JSONArray othersArray = new JSONArray();
		JSONObject root = new JSONObject();
		File file = new File(path);
		File[] files = file.listFiles();
		getAllVideosJson(files,othersArray);
		root.put("data", othersArray);
		return root.toString();
	}
	
	public static void getAllVideosJson(File[] files,JSONArray othersArray) throws Exception{
		for (File f : files) {
			if (f.isFile()) {
				String name = f.getName();
				if ("video".equals(CommonUtil.getFileType(name))) {
					JSONObject uploadFileObject = new JSONObject();
					uploadFileObject.put("date", f.lastModified());
					uploadFileObject.put("isCurrentVersion", "0");
					uploadFileObject.put("name", name);
					uploadFileObject.put("parentId",f.getParentFile().getAbsolutePath());
					uploadFileObject.put("path", f.getAbsolutePath());
					uploadFileObject.put("size", f.length());
					uploadFileObject.put("status", "aviliable");
					uploadFileObject.put("type", "file");
					uploadFileObject.put("version", "0");
					othersArray.put(uploadFileObject);
				}
			}else{
				File[] f1 = f.listFiles();
				if (f1 != null && f1.length > 0) {
					getAllVideosJson(f1,othersArray);
				}
			}
		}
	}

	public static String getAllDocuments(String path) throws Exception{
		JSONArray documentArray = new JSONArray();
		JSONObject root = new JSONObject();
		File file = new File(path);
		File[] files = file.listFiles();
		getDocumentJson(files,documentArray);
		root.put("data", documentArray);
		return root.toString();
	}

	
	public static void getDocumentJson(File[] files,JSONArray documentArray) throws Exception{
		for (File f : files) {
			if (f.isFile()) {
				String name = f.getName();
				if ("document".equals(CommonUtil.getFileType(name))) {
					Log.d(TAG, "document file name = " + name);
					JSONObject uploadFileObject = new JSONObject();
					uploadFileObject.put("date", f.lastModified());
					uploadFileObject.put("isCurrentVersion", "0");
					uploadFileObject.put("name", name);
					uploadFileObject.put("parentId",f.getParentFile().getAbsolutePath());
					uploadFileObject.put("path", f.getAbsolutePath());
					uploadFileObject.put("size", f.length());
					uploadFileObject.put("status", "aviliable");
					uploadFileObject.put("type", "file");
					uploadFileObject.put("version", "0");
					documentArray.put(uploadFileObject);
				}
			}else{
				File[] f1 = f.listFiles();
				if (f1 != null && f1.length > 0) {
					getDocumentJson(f1,documentArray);
				}
			}
		}
	}

	public static String getOthers(String path) throws Exception{
		JSONArray othersArray = new JSONArray();
		JSONObject root = new JSONObject();
		File file = new File(path);
		File[] files = file.listFiles();
		getOthersJson(files,othersArray);
		root.put("data", othersArray);
		return root.toString();
	}
	
	public static void getOthersJson(File[] files,JSONArray othersArray) throws Exception{
		for (File f : files) {
			if (f.isFile()) {
				String name = f.getName();
				if ("others".equals(CommonUtil.getFileType(name))) {
					JSONObject uploadFileObject = new JSONObject();
					uploadFileObject.put("date", f.lastModified());
					uploadFileObject.put("isCurrentVersion", "0");
					uploadFileObject.put("name", name);
					uploadFileObject.put("parentId",f.getParentFile().getAbsolutePath());
					uploadFileObject.put("path", f.getAbsolutePath());
					uploadFileObject.put("size", f.length());
					uploadFileObject.put("status", "aviliable");
					uploadFileObject.put("type", "file");
					uploadFileObject.put("version", "0");
					othersArray.put(uploadFileObject);
				}
			}else{
				File[] f1 = f.listFiles();
				if (f1 != null && f1.length > 0) {
					getOthersJson(f1,othersArray);
				}
			}
		}
	}
}
