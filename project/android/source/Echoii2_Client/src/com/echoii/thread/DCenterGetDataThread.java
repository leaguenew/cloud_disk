package com.echoii.thread;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.echoii.network.socket.DataCenterSocket;
import com.echoii.uploadfile.UploadFile;

public class DCenterGetDataThread extends Thread {
	public static final String TAG = "DCenterGetDataThread";
	
	private String ip = null;
	private String path = null;
	private Handler handler;
	private int msg = 0;
	private int itemId = 0;
	private DataCenterSocket socket;

	public DCenterGetDataThread(Handler handler,Activity activity,String ip,int itemId, String path, int msg) {
		this.ip = ip;
		this.path = path;
		this.handler = handler;
		this.msg = msg;
		this.itemId = itemId;
		socket = new DataCenterSocket(handler, activity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		super.run();
		List mFileList = socket.getFileList(ip, path , itemId);
		if (mFileList.size() > 0) {
			try {
				System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
				Collections.sort(mFileList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Message message = handler.obtainMessage();
		DCHandleFileListMsg msgObj = new DCHandleFileListMsg();
		msgObj.fileList = mFileList;
		msgObj.path = path;
		message.what = msg;
		message.obj = msgObj;
		handler.sendMessage(message);
	}
	
	public class DCHandleFileListMsg{
		public List<UploadFile> fileList;
		public String path;
	}
}
