package com.echoii.tv.datacenter.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.echoii.tv.BaseContext;
import com.echoii.tv.constants.HttpConstants;
import com.echoii.tv.model.Device;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.model.DownloadObj;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心下载任务管理类</p>
 *
 */
public class DownloadTaskManager extends BaseContext{
	
	public static final String TAG = "DownloadTaskManager";
	
	private boolean downloadFlag = true;
	private DownloadTask downloadTask = null;
	private DownloadObj obj = null;
	
	public DownloadTaskManager() {
		super();
	}

	public DownloadTaskManager(Context context, Handler handler) {
		super(context, handler);
		downloadTask = new DownloadTask(getContext(), getHandler());
	}

	public boolean isDownloadFlag() {
		return downloadFlag;
	}

	public void setDownloadFlag(boolean downloadFlag) {
		this.downloadFlag = downloadFlag;
	}

	public void downloadFile(EchoiiFile file, Device device, DownloadObj obj, String url) throws SocketTimeoutException, IOException, MalformedURLException{
		
		try {
			
			if (file.getIdx() == 0) {
				File directory = new File(file.getPath());
				if (!directory.exists()) {
					directory.mkdirs();
				}
			}else {
				downloadTask.downloadFile(file, device, obj, url);
			}
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} 
			
	}
	
	public void downloadFile(String path, List<EchoiiFile> list, Device device) throws SocketTimeoutException, IOException{
		LogUtil.d(TAG, "downloadFlag = " + downloadFlag);
		for (int i = 0 ; i < list.size() ; i++) {
			if (downloadFlag) {
				String url = HttpConstants.DOWNLOAD_BASE_URL + "device_id=" + device.getDeviceId() + "&token=" + device.getDeviceToken() + "&file_id=" + list.get(i).getId();
				String echoiiFilePath = list.get(i).getPath();
				String localFilePath = path + echoiiFilePath.substring(5, echoiiFilePath.length());
				list.get(i).setPath(localFilePath);
				try {
					obj = new DownloadObj();
					obj.setCurrentIndex(i + 1);
					obj.setFileCount(list.size());
					downloadFile(list.get(i), device, obj, url);
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
					throw e;
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				} 
			}
		}

	}
	
	public void stopDownload() {
		downloadTask.stopDownload();
	}

}
