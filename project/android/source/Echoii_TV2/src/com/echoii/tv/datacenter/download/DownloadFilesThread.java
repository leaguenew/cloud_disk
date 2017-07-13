package com.echoii.tv.datacenter.download;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.echoii.tv.model.Device;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心文件下载线程</p>
 *
 */
public class DownloadFilesThread extends Thread {
	
	public static final String TAG = "DownloadFilesThread";
	
	private String syncPath;
	private List<EchoiiFile> downloadList ;
	
	private Context context ;
	private Handler handler ;
	private DownloadTaskManager downloadManager ;
	private Device device ;
	
	public DownloadFilesThread() {
		super();
	}

	public DownloadFilesThread(Context context, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
		downloadManager = new DownloadTaskManager(context, handler);
	}
	
	public List<EchoiiFile> getDownloadList() {
		return downloadList;
	}

	public void setDownloadList(List<EchoiiFile> downloadList) {
		this.downloadList = downloadList;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getSyncPath() {
		return syncPath;
	}

	public void setSyncPath(String syncPath) {
		this.syncPath = syncPath;
	}

	@Override
	public void run() {
		super.run();
		
		try {
			
			downloadManager.setDownloadFlag(true);
			downloadManager.downloadFile(syncPath, downloadList, device);
			LogUtil.d(TAG, "download starting .. ");
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopDownload() {
		
		downloadManager.setDownloadFlag(false);
		downloadManager.stopDownload();
		
	}

}
