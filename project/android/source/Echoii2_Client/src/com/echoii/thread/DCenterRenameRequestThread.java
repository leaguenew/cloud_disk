package com.echoii.thread;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.echoii.activity.fragment.DataCenterListFragment;
import com.echoii.constant.CommonUtil;
import com.echoii.constant.DataCenterJSONUtil;
import com.echoii.network.socket.DataCenterSocket;

public class DCenterRenameRequestThread extends Thread{
	public static final String TAG = "DCenterRenameRequestThread";
	
	private String ip = null;
	private String requestCode = null;
	private List<String> filePathList = null;
	private String currentPath = null;
	private Handler handler = null;
	private String newFileName = null;
	private DataCenterSocket socket;
	
	public DCenterRenameRequestThread(Handler handler , Activity activity,String ip , String requestCode , List<String> filesPath , String currentPath ,String newName){
		this.ip = ip;
		this.requestCode = requestCode;
		this.filePathList = filesPath;
		this.currentPath = currentPath;
		this.handler = handler;
		this.newFileName = newName;
		socket = new DataCenterSocket(handler, activity);
	}
	
	@Override
	public void run() {
		super.run();
		if (requestCode.equals(DataCenterSocket.REQUEST_CODE_RENAME)) {
			String result = socket.doRequest(ip, requestCode, filePathList, newFileName);
			String responseCode = DataCenterJSONUtil.parseCopyJson(result);
			Log.d(TAG, "result code = " + result);
			if (responseCode.equals(DataCenterSocket.SUCCESS)) {
				if (currentPath.equals(CommonUtil.SDCARD_ROOT_PATH)) {
					Message msg = handler.obtainMessage();
					msg.what = DataCenterListFragment.RENAME_FILE_SUCCESS;
					RenameHandleObj obj = new RenameHandleObj();
					obj.currentPath = currentPath;
					obj.handlerCase = DataCenterListFragment.FILE_ROOT_PATH;
					msg.obj = obj;
					handler.sendMessage(msg);
				}else{
					Message msg = handler.obtainMessage();
					msg.what = DataCenterListFragment.RENAME_FILE_SUCCESS;
					RenameHandleObj obj = new RenameHandleObj();
					obj.currentPath = currentPath;
					obj.handlerCase = DataCenterListFragment.FILE_CHILD_PATH;
					msg.obj = obj;
					handler.sendMessage(msg);
				}
			}else{
				Message msg = handler.obtainMessage();
				msg.what = DataCenterListFragment.RENAME_FILE_ERROR;
				handler.sendMessage(msg);
			}
		}
	}
	
	public  class RenameHandleObj{
		public String currentPath;
		public int handlerCase; 
	}
}
