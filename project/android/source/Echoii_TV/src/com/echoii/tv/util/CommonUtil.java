package com.echoii.tv.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

import com.echoii.uploadfile.UploadFile;

public class CommonUtil {
	public static final String TAG = "CommonUtil";
	
	public static final String SERVER_BASE_URL = "192.168.0.103:8080";
//	public static final String SERVER_BASE_URL = "210.75.252.180:7080";
	public static final String USER_ID = "12345678";
	public static final String TOKEN = "test";
	public static final String TYPE = "image";
//	public static final String BASE_URL = "http://172.21.7.199:81/echoii/service/0/file/latest?";
//	public static final String DOWNLOAD_BASE_URL = "http://172.21.7.199:81/echoii/service/0/download?";
	public static final String BASE_URL = "http://"+ SERVER_BASE_URL +"/echoii/service/0/file/latest?";
	public static final String DOWNLOAD_BASE_URL = "http://"+ SERVER_BASE_URL +"/echoii/service/0/download/mobile?";
	public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "echoii" + File.separator + "photo" + File.separator;
	public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath();
	
	public static long getDirSize(File file) {     
        if (file.exists()) {     
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                long size = 0; 
                if (children != null && children.length > 0) {
                	for (File childFile : children){
                        size += getDirSize(childFile);
                    }
				}
                return size;     
            } else {
                long size = file.length();        
                return size;     
            }     
        } else {     
            return 0;     
        }     
    }     

	public static File[] getFileList(String path){
		File[] files = null;
		File file = new File(path);
		if (file.isDirectory()) {
			files = file.listFiles();
		}
		return files;
	}

	public static String moveFiles(List<String> fromPath, String toPath) {
		FileOperateImpl operator = new FileOperateImpl();
//		for (int i = 0; i < fromPath.size(); i++) {
//			String response = operator.moveFolder(fromPath.get(i), toPath);
//			if (response.equals(FileOperate.ERROR)) {
//				return FileOperate.ERROR;
//			}
//		}
		return FileOperateImpl.SUCCESS;
	}

	public static String makeResponseJson(String requestCode, String responseCode) {
		String responseJson = "";
		try {
			JSONObject root = new JSONObject();
			root.put("response", requestCode);
			root.put("param", responseCode);
			responseJson = root.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	public static UploadFile getDownloadFile(String request) {
		UploadFile uploadFile = new UploadFile();
		try {
			JSONObject root = new JSONObject(request);
			String requestCode = root.getString("request");
			if (requestCode.equals("downloadFile")) {
				String path = root.getString("param");
				Log.d(TAG, "download path = " + path);
				File file = new File(path);
				FileInputStream input = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(input);
//				byte[] buffer = new byte[bis.available()];
				ByteArrayBuffer baf  = new ByteArrayBuffer(bis.available());
				byte[] buffer = new byte[3*1024];
				int len = 0;
				while ((len = bis.read(buffer))!=-1) {
					baf.append(buffer, 0, len);
//					Log.d(TAG, "buffer = " + buffer.length);
//					output.write(buffer);
				}
				
				uploadFile.setFileContent(baf.toByteArray());
				uploadFile.setFileName(file.getName());
				uploadFile.setPath(file.getAbsolutePath());
				uploadFile.setParentRoot(file.getParentFile().getPath());
				uploadFile.setDate(String.valueOf(file.lastModified()));
				uploadFile.setFileSize(file.length());
				uploadFile.setType("file");
				uploadFile.setVersion("0");
				uploadFile.setIsCurrentVersion("0");
				uploadFile.setStatus("available");
				
//				output.close();
				baf.clear();
				baf = null;
				input.close();
				bis.close();
				System.gc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uploadFile;
	}
	
	public static String getParentPath(String path) {
		String parentPath = path.substring(0, path.lastIndexOf("/"));
		return parentPath;
	}
	
	public static String getFileType(String name) {
		String exp = name.substring(name.lastIndexOf(".") + 1, name.length());
		exp = exp.toLowerCase();
		if (exp.equals("mp3") || exp.equals("ogg") || exp.equals("wav")
				|| exp.equals("wma")) {
			return "music";
		} else if (exp.equals("3gp") || exp.equals("rmvb") || exp.equals("mp4")
				|| exp.equals("mpg4") || exp.equals("wmv") || exp.equals("flv")) {
			return "video";
		} else if (exp.equals("png") || exp.equals("jpg") || exp.equals("jpeg")
				|| exp.equals("bmp") || exp.equals("gif")) {
			return "image";
		} else if (exp.equals("doc") || exp.equals("ppt") || exp.equals("xls")
				|| exp.equals("xlsx") || exp.equals("docx")
				|| exp.equals("txt")) {
			return "document";
		} else {
			return "others";
		}
	}
	
	public static String getServerIpAddress(Service service){
		WifiManager wm = (WifiManager) service.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wi = wm.getConnectionInfo();
		int ip = wi.getIpAddress();
		Log.d(TAG, "wifi info ip = " + ip);
		//(i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		String ipAddress = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
		return ipAddress;
	}
}
