package com.echoii.network.socket;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.echoii.constant.CommonUtil;
import com.echoii.constant.DataCenterJSONUtil;
import com.echoii.uploadfile.UploadFile;

public class DataCenterSocket {
	
	public static final String TAG = "DataCenterSocket";
	public static final String REQUEST_CODE_GET_FILES = "getFiles";
	public static final String REQUEST_CODE_MOVE = "moveFiles";
	public static final String REQUEST_CODE_DOWNLOAD_FILE = "downloadFile";
	public static final String REQUEST_CODE_COPY = "copyFiles";
	public static final String REQUEST_CODE_DELETE = "deleteFiles";
	public static final String REQUEST_CODE_RENAME = "renameFile";
	public static final String REQUEST_CODE_LOGIN = "login";
	public static final String SUCCESS = "200";
	public static final String ERROR = "503";
	public static final int REQUEST_PORT = 8888;
	
	private Handler mHandler;
	private Activity mActivity;
	
	private Socket downloadSocket = null;
	private String downloadPath = null;
	
	public DataCenterSocket(Handler mHandler, Activity mActivity) {
		super();
		this.mHandler = mHandler;
		this.mActivity = mActivity;
	}

	public List<UploadFile> getFileList(String ip, String path , int itemId) {
		List<UploadFile> list = null;
		Socket socket = null;
		try {
			socket = new Socket(ip, REQUEST_PORT);
			socket.setSoTimeout(20000);
		} catch (Exception e) {
			e.printStackTrace();
			handleException();
			return new ArrayList<UploadFile>();
		} 
		DataInputStream input = null;
		try {
			input = new DataInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			handleException();
			return new ArrayList<UploadFile>();
		}
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String requestJson = DataCenterJSONUtil.requestFileListJson(REQUEST_CODE_GET_FILES,path,itemId);
		Log.d(TAG, "cliet requestJson = " + requestJson);
		try {
			output.writeUTF(requestJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buffer = new byte[4096];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		try {
			while ((len = input.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		String json = null;
		try {
			json = new String(bos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			e3.printStackTrace();
		}
		if (json.equals("null") || TextUtils.isEmpty(json)) {
			list = new ArrayList<UploadFile>();
		} else {
			if (itemId == 1) {
				list = DataCenterJSONUtil.parseJsonForImage(json);
			}else{
				list = DataCenterJSONUtil.parseJson(json);
			}
		}
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private void handleException() {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mActivity, "连接家庭数据中心失败，请检查文件共享是否开启,或断开重新连接...", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public String doRequest(String ip , String requestCode , List<String> filesPath , String toPath) {
		String responseJson = "";
		Socket socket = null;
		try {
			socket = new Socket(ip, REQUEST_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataInputStream input = null;
		try {
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String requestJson = DataCenterJSONUtil.requestOperateJson(requestCode,filesPath , toPath);
		Log.d(TAG, "cliet requestJson = " + requestJson);
		try {
			output.writeUTF(requestJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		try {
			while ((len = input.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			responseJson = new String(bos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			e3.printStackTrace();
		}
		Log.d(TAG, requestCode + " response json = " + responseJson);
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseJson;
	}
	
	public String doLoginRequest(String serverIp , String requestCode , String password , String clientIp) {
		String responseJson = "";
		Socket socket = null;
		try {
			socket = new Socket(serverIp, REQUEST_PORT);
			socket.setSoTimeout(10000);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		DataInputStream input = null;
		try {
			input = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String requestJson = DataCenterJSONUtil.requestLoginJson(requestCode,password,clientIp);
		Log.d(TAG, "cliet requestJson = " + requestJson);
		try {
			output.writeUTF(requestJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		try {
			while ((len = input.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		try {
			responseJson = new String(bos.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e3) {
			e3.printStackTrace();
		}
		Log.d(TAG, requestCode + " response json = " + responseJson);
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseJson;
	}
	
	public String doDownload(String ip , String requestCode , String requestPath) {
		Socket socket = null;
		UploadFile downloadFile = null;
		try {
			socket = new Socket(ip, 8891);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String requestJson = DataCenterJSONUtil.getDownloadJson(requestCode,requestPath);
		try {
			output.writeUTF(requestJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ObjectInputStream input = null;
		try {
			input = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			downloadFile = (UploadFile) input.readObject();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String path = CommonUtil.getFilePathByType(downloadFile);
		Log.d(TAG, "downloadFile getFilePathByType = " + path);
		if (path == null) {
			return null;
		}
		File file = new File(path);
		Log.d(TAG, "downloadFile new File path = " + file.getPath());
		Log.d(TAG, "downloadFile new File parent path = " + file.getParentFile().getPath());
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		FileOutputStream fileOutput = null;
		try {
			fileOutput = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fileOutput.write(downloadFile.getFileContent());
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mActivity, "下载出错，请重试...", Toast.LENGTH_SHORT).show();
			file.delete();
			return null;
		}
		try {
			fileOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public String doDownload2(String ip , String requestCode , String requestPath) {
			UploadFile downloadFile = new UploadFile();
			try {
				downloadSocket = new Socket(ip, 8891);
				downloadSocket.setSoTimeout(20000);
			} catch (Exception e) {
				e.printStackTrace();
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mActivity, "数据中心连接异常，请检查网络或是否已开启文件共享...", Toast.LENGTH_SHORT).show();
					}
				});
			}
			DataOutputStream output = null;
			DataInputStream input = null;
			FileOutputStream fileOutput = null;
			try {
				output = new DataOutputStream(downloadSocket.getOutputStream());
				String requestJson = DataCenterJSONUtil.getDownloadJson(requestCode,requestPath);
				output.writeUTF(requestJson);
				output.flush();
				input = new DataInputStream(downloadSocket.getInputStream());
				
//				output.writeUTF(file.getName());
//				output.flush();
//				output.writeUTF(file.getAbsolutePath());
//				output.flush();
//				output.writeUTF(file.getParentFile().getAbsolutePath());
//				output.flush();
//				output.writeUTF(String.valueOf(file.lastModified()));
//				output.flush();
//				output.writeLong(file.length());
//				output.flush();
//				output.writeUTF("file");
//				output.flush();
//				output.writeUTF("0");
//				output.flush();
//				output.writeUTF("0");
//				output.flush();
//				output.writeUTF("available");
//				output.flush();
				
				downloadFile.setFileName(input.readUTF());
				downloadFile.setPath(input.readUTF());
				downloadFile.setParentRoot(input.readUTF());
				downloadFile.setDate(input.readUTF());
				downloadFile.setFileSize(input.readLong());
				downloadFile.setType(input.readUTF());
				downloadFile.setVersion(input.readUTF());
				downloadFile.setIsCurrentVersion(input.readUTF());
				downloadFile.setStatus(input.readUTF());
				downloadPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "echoii" + File.separator + "data_center" + File.separator  + CommonUtil.getFileType(downloadFile.getFileName()) + File.separator + downloadFile.getFileName();
				Log.d(TAG, "download input path = " + downloadPath);
				File file = new File(downloadPath);
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				fileOutput = new FileOutputStream(file);
				int len = 0;
				byte[] buffer = new byte[4*1024];
				while ((len = input.read(buffer)) != -1) {
					fileOutput.write(buffer, 0, len);
					fileOutput.flush();
				}
				
				fileOutput.close();
				output.close();
				input.close();
				downloadSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		return downloadPath;
	}
	
	public void cancleDownloadSocket(){
		if (downloadSocket != null) {
			if (!downloadSocket.isClosed()) {
				try {
					downloadSocket.shutdownInput();
					downloadSocket.close();
					Toast.makeText(mActivity, "下载操作已取消。", Toast.LENGTH_SHORT).show();
					downloadPath = "cancle";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
