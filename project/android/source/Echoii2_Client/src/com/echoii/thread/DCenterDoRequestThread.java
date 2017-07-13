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

public class DCenterDoRequestThread extends Thread{
	public static final String TAG = "DCenterDoRequestThread";
	private String ip = null;
	private String requestCode = null;
	private List<String> filePathList = null;
	private String currentPath = null;
	private Handler handler = null;
	private DataCenterSocket socket;
	
	public DCenterDoRequestThread(Handler handler , Activity activity,String ip , String requestCode , List<String> filesPath , String toPath){
		this.ip = ip;
		this.requestCode = requestCode;
		this.filePathList = filesPath;
		this.currentPath = toPath;
		this.handler = handler;
		socket = new DataCenterSocket(handler, activity);
	}
	
	@Override
	public void run() {
		super.run();
		if (requestCode.equals(DataCenterSocket.REQUEST_CODE_COPY)) {
			String result = socket.doRequest(ip, requestCode, filePathList, currentPath);
			String responseCode = DataCenterJSONUtil.parseCopyJson(result);
			Log.d(TAG, "result code = " + result);
			if (responseCode.equals(DataCenterSocket.SUCCESS)) {
				if (currentPath.equals(CommonUtil.SDCARD_ROOT_PATH)) {
					Message msg = handler.obtainMessage();
					msg.what = DataCenterListFragment.COPY_FILES_SUCCESS;
					HandleObj obj = new HandleObj();
					obj.currentPath = currentPath;
					obj.handlerCase = DataCenterListFragment.FILE_ROOT_PATH;
					msg.obj = obj;
					handler.sendMessage(msg);
				}else{
					Message msg = handler.obtainMessage();
					msg.what = DataCenterListFragment.COPY_FILES_SUCCESS;
					HandleObj obj = new HandleObj();
					obj.currentPath = currentPath;
					obj.handlerCase = DataCenterListFragment.FILE_CHILD_PATH;
					msg.obj = obj;
					handler.sendMessage(msg);
				}
			}else{
				Message msg = handler.obtainMessage();
				msg.what = DataCenterListFragment.COPY_FILES_ERROR;
				handler.sendMessage(msg);
			}
		}else if(requestCode.equals(DataCenterSocket.REQUEST_CODE_DELETE)){
			String result = socket.doRequest(ip, requestCode, filePathList, currentPath);
			String responseCode = DataCenterJSONUtil.parseCopyJson(result);
			Log.d(TAG, "result code = " + result);
			if (responseCode.equals(DataCenterSocket.SUCCESS)) {
				if (currentPath.equals(CommonUtil.SDCARD_ROOT_PATH)) {
					Message msg = handler.obtainMessage();
					msg.what = DataCenterListFragment.DELETE_FILES_SUCCESS;
					HandleObj obj = new HandleObj();
					obj.currentPath = currentPath;
					obj.handlerCase = DataCenterListFragment.FILE_ROOT_PATH;
					msg.obj = obj;
					handler.sendMessage(msg);
				}else{
					Message msg = handler.obtainMessage();
					msg.what = DataCenterListFragment.DELETE_FILES_SUCCESS;
					HandleObj obj = new HandleObj();
					obj.currentPath = currentPath;
					obj.handlerCase = DataCenterListFragment.FILE_CHILD_PATH;
					msg.obj = obj;
					handler.sendMessage(msg);
				}
			}else{
				Message msg = handler.obtainMessage();
				msg.what = DataCenterListFragment.DELETE_FILES_ERROR;
				handler.sendMessage(msg);
			}
		}
		/*if (currentPath.equals(CommonUtil.SDCARD_ROOT_PATH)) {
			Message message = handler.obtainMessage();
			message.what = DataCenterListFragment.DO_REQUEST_THREAD_ROOT;
			HandleObj obj = new HandleObj();
			obj.ip = ip;
			obj.currentPath = currentPath;
			message.obj = obj;
			handler.sendMessage(message);
		}else{
			Message message = handler.obtainMessage();
			message.what = DataCenterListFragment.DO_REQUEST_THREAD_CHILD;
			HandleObj obj = new HandleObj();
			obj.ip = ip;
			obj.currentPath = currentPath;
			message.obj = obj;
			handler.sendMessage(message);
		}*/
	}
	
	public  class HandleObj{
		public String currentPath;
		public int handlerCase; 
	}
}
