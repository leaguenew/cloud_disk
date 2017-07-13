package com.echoii.tv;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.os.Handler;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.datacenter.download.DownloadFilesThread;
import com.echoii.tv.model.Device;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.util.CommonUtil;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>同步家庭数据中心文件封装类</p>
 *
 */
public class SyncFiles extends BaseContext{
	
	public static final String TAG = "SyncFiles";

	private DownloadFilesThread downloadThread;
	private HttpHelper mHelper;
	
	private List<EchoiiFile> localFileList;
	private List<EchoiiFile> datacenterFileList;
	private List<EchoiiFile> downloadFileList;
	
	public SyncFiles() {
		super();
	}

	public SyncFiles(Context context, Handler handler) {
		super(context, handler);
		downloadThread = new DownloadFilesThread(getContext(), getHandler());
	}

	public List<EchoiiFile> getLocalFileList(String path) {
		
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();

		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			return null;
		}
		getAllLocalFile(files, list);
		
		return list;
		
	}
	
	private void getAllLocalFile(File[] files, List<EchoiiFile> list) {
		
		for (int i = 0; i < files.length; i++) {
			EchoiiFile file = new EchoiiFile();
			if (files[i].isFile()) {
				file.setPath(files[i].getAbsolutePath());
			}
			if (files[i].isDirectory()) {
				file.setPath(files[i].getAbsolutePath());
				File[] f = files[i].listFiles();
				if (f == null) {
					return;
				}
				getAllLocalFile(f, list);
			}
			list.add(file);
		}
		
	}

	public List<EchoiiFile> getAllDatacenterFileList(Device device) {
		
		List<EchoiiFile> list = null;
		
		try {
			
			mHelper = new HttpHelper(getContext(), getHandler());
			list = mHelper.getAllDatacenterList(device);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			CommonUtil.handleMessage(getHandler(), Constants.MSG_CONNECTION_TIMEOUT);
			
		} catch (IOException e) {
			e.printStackTrace();
			CommonUtil.handleMessage(getHandler(), Constants.MSG_NETWORK_ERROR);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} 
				
		return list;
		
	}
	
	public List<EchoiiFile> compareAndGetDownloadList(List<EchoiiFile> local, List<EchoiiFile> cloud) {
		
		List<EchoiiFile> list = new ArrayList<EchoiiFile>();
		for (int i = 0; i < cloud.size(); i++) {
			String cloudPath = cloud.get(i).getPath();
			LogUtil.d(TAG, "cloudName = " + cloud.get(i).getName() + " -- cloudPath = " + cloudPath);
			String cloudSubPath = cloudPath.substring(5, cloudPath.length());
			boolean flag = false;
			for (int j = 0; j < local.size(); j++) {
				String localPath = local.get(j).getPath();
				if (localPath.contains(cloudSubPath)) {
					//处理冲突文件
					flag = true;
					continue;
				}
			}
			if (!flag) {
				list.add(cloud.get(i));
			}
		}
		
		return list;
		
	}
	
	public void startSync(Device device, int flag){
		switch (flag) {
		case Constants.SYNC_FILE:
			syncFile();
			break;
		case Constants.SYNC_ALL_FILES:
			syncAllFiles(device);
			break;
		default:
			break;
		}
	}
	
	public void syncFile(){
		
	}
	
	public void syncAllFiles(Device device) {
		
		String path = Constants.SYNC_DEFAULT_PATH ;
		
		if (!isStorageMounted(path)) {
			CommonUtil.handleMessage(getHandler(), Constants.MSG_STORAGE_UNMOUNTED);
			return;
		}
		
		localFileList = getLocalFileList(path);
		datacenterFileList = getAllDatacenterFileList(device);
		
		if (datacenterFileList == null || datacenterFileList.size() == 0) {
			CommonUtil.handleMessage(getHandler(), Constants.MSG_CLOUD_LIST_NULL);
			return;
		}
		
		if (localFileList == null || localFileList.size() == 0) {
			downloadFileList = datacenterFileList;
		}else{
			downloadFileList = compareAndGetDownloadList(localFileList, datacenterFileList);
		}
		
		if (downloadFileList == null || downloadFileList.size() == 0) {
			CommonUtil.handleMessage(getHandler(), Constants.MSG_DOWNLOAD_LIST_NULL);
			return;
		}
		
		for (int i = 0; i < downloadFileList.size(); i++) {
			LogUtil.d(TAG, "syncAllFiles : downloadFileList - " + i + " --> " + downloadFileList.get(i).getPath());
		}
		
		downloadFiles(path, device);
		
	}

	private void downloadFiles(String path, Device device) {
		downloadThread.setDevice(device);
		downloadThread.setSyncPath(path);
		downloadThread.setDownloadList(downloadFileList);
		downloadThread.start();
	}
	
	public void stopSync() {
		downloadThread.stopDownload();
	}
	
	public boolean isStorageMounted(String path){
		
		if (path == null) {
			return false;
		}
		
		if (path.contains(Constants.SYNC_USB_BASE_PATH)) {
			if (CommonUtil.isUsbMounted()) {
				return true;
			}
		} else {
			if (CommonUtil.isSDcardMounted()) {
				return true;
			}
		}
		
		return false;
	}

}
