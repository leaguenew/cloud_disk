package com.echoii.tv.datacenter.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import android.content.Context;
import android.os.Handler;

import com.echoii.tv.BaseContext;
import com.echoii.tv.constants.HttpConstants;
import com.echoii.tv.model.Device;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.model.DownloadObj;
import com.echoii.tv.util.FileUtil;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心下载任务代理类</p>
 *
 */
public class DownloadTask extends BaseContext{
	
	public static final String TAG = "DownloadTask";
	
	private long fileLength = 0;
	
	private HttpURLConnection conn = null;
	private EchoiiFile echoiiFile = null;
	private FileUtil fileUtil = null;
	
	public DownloadTask() {
		super();
	}

	public DownloadTask(Context context, Handler handler) {
		super(context, handler);
	}

	public void downloadFile(EchoiiFile file, Device device, DownloadObj obj, 
			String url) throws SocketTimeoutException,
			IOException {
		LogUtil.d(TAG, "-----------------------------------------------------");
		LogUtil.d(TAG, "downloadFile : download file path = " + file.getPath());
		LogUtil.d(TAG, "download url = " + url);
		try {
			URL realUrl = new URL(url);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			LogUtil.d(TAG, "downloadFile : responseCode = " + responseCode); 
			if (responseCode == HttpURLConnection.HTTP_OK) {
				file.setSize(String.valueOf(conn.getContentLength()));
				echoiiFile = file;
				FileUtil.writeFile(getHandler(), conn.getInputStream(), file, obj);
			} else {
				deleteCloudFile(file, device);
			}
		}  catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally { 
			if (conn != null) {
				conn.disconnect();
			}
		}
		
	}
	
	public void deleteCloudFile(EchoiiFile file, Device device) {
		
		String delUrl = HttpConstants.DELETE_BASE_URL + "user_id=" + HttpConstants.USER_ID + "&token=" + HttpConstants.PASSWORD + "&file_id=" + file.getId();
		
		HttpURLConnection conn = null;
		try {
			URL realUrl = new URL(delUrl);
			conn = (HttpURLConnection) realUrl.openConnection();
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			LogUtil.d(TAG, "deleteCloudFile : responseCode = " + responseCode); 
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				LogUtil.d(TAG, "deleteCloudFile : response = " + reader.readLine());
			} 
		}  catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally { 
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	public void stopDownload() {
		
		if (conn != null) {
			conn.disconnect();
			conn = null;
			
			if (!fileUtil.isDownloadFinish()) {
				String delPath = echoiiFile.getPath();
				File file = new File(delPath);
				if (!file.exists()) {
					return ;
				}
				
				String str = file.delete() ? "delete success !" : "delete fail !" ;
				LogUtil.d(TAG, "delete info = " + str);
			}
			
		}
		
	}

}
