package com.echoii.tv.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.DatagramPacket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.echoii.tv.MainActivity;
import com.echoii.tv.R;
import com.echoii.tv.util.CommonUtil;
import com.echoii.tv.util.JsonMakeUtil;
import com.echoii.tv.util.JsonParseUtil;
import com.echoii.uploadfile.UploadFile;

public class TVService extends Service {

	public static final String TAG = "TVService";
	public static final int REQUEST = 1;
	public static final int UPLOAD = 2;
	public static final int DOWNLOAD = 3;
	public static final int SHOW_SERVER_IP = 0x010;
	public static final String REQUEST_IMAGES_PARAM = "get_all_images";
	public static final String REQUEST_MUSICS_PARAM = "get_all_musics";
	public static final String REQUEST_VIDEOS_PARAM = "get_all_videos";
	public static final String REQUEST_DOCUMENTS_PARAM = "get_all_documents";
	public static final String REQUEST_OTHERS_PARAM = "get_others";
	public static final String SHARED_PREF_NAME = "tvservice_shared_preferences";

	private IBinder mBinder = new LocalBinder();
	private ServerSocket requestServerSocket;
	private ServerSocket uploadServerSocket;
	private ServerSocket downloadServerSocket;
	private UploadFile myUploadFile;

	public static boolean flag = true;

	private MulticastSocket udpSenderSocket;
	private DatagramPacket udpSenderPacket;
	private InetAddress udpSendAddress;
	public static final String BROADCAST_IP = "224.0.0.1";  //IP地址范围是224.0.0.0---239.255.255.255,其中224.0.0.0为系统自用
	public static final int BROADCAST_PORT = 9001;
	public String udpReceiverIp;
	public UdpSendThread mSendThread;
	public boolean udpSenderFlag = true;
	
	//照片同步
	private Timer mSearchTimer;
	private TimerTask mSearchTask;
	private SharedPreferences mSharedPrefService;
	public static final int SEARCH = 0x001;
	public static final int REQUEST_DOWNLOAD = 0x002;
	public static final int DOWNLOAD_RESULT = 0x003;
	public static final int DATA_NULL = 0x004;
	public static final String KEY_DATE = "date";
	
	private JsonParseUtil mJsonParseUtil;
	private Handler mHandler;

	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public class LocalBinder extends Binder {
		public TVService getTVService() {
			return TVService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "TV service onBind ");
		return mBinder;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		Log.d(TAG, "TV service onCreate");
		super.onCreate();
		Notification notification = new Notification(R.drawable.ic_launcher, "Echoii文件共享已开启!",
				System.currentTimeMillis());
		PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this,MainActivity.class), 0);
		notification.setLatestEventInfo(this, "Echoii服务已开启", "", intent);
		startForeground(0x110, notification);
		mSharedPrefService = this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		initUploadServerSocket();
		initRequestServerSocket();
		initDownloadServerSocket();
		initUdpBroadcastSender();
		initSearchServer();
//		handleMainActivityOrderListener.onHandleOrder(SHOW_SERVER_IP, getServerIpAddress());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "TV service onStartCommand");
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "TV service onDestroy");
		super.onDestroy();
		try {
			closeServerSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "TV service onUnbind");
		return super.onUnbind(intent);
	}

	public void initSearchServer() {
		mSearchTimer = new Timer();
		mSearchTask = new TimerTask(){
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = SEARCH;
				searchHandler.sendMessage(msg);
			}
		};
		mSearchTimer.schedule(mSearchTask, 0, 30*1000);
	}

	public void initUploadServerSocket() {
		if (uploadServerSocket == null) {
			try {
				uploadServerSocket = new ServerSocket(8889);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				doUpload(true, uploadServerSocket);
			}
		}).start();

	}

	public void initRequestServerSocket() {
		if (requestServerSocket == null) {
			try {
				requestServerSocket = new ServerSocket(8888);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				doRequest(true, requestServerSocket);
			}
		}).start();

	}

	public void initDownloadServerSocket() {
		if (downloadServerSocket == null) {
			try {
				downloadServerSocket = new ServerSocket(8891);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				doDownload2(true, downloadServerSocket);
			}
		}).start();
	}

	public void doRequest(boolean whileFlag, ServerSocket serverSocket) {
		while (whileFlag) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (Exception e) {
				e.printStackTrace();
				return;
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
			String content = null;
			try {
				content = input.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d(TAG, "server json = " + content);
			String json = handleRequest(content);
			Log.d(TAG, "handled json = " + json);
			if (TextUtils.isEmpty(json)) {
				try {
					output.write("null".getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else{
				try {
					output.write(json.getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
//			File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "Download" + File.separator +"json.txt");
//			FileOutputStream out = null;
//			try {
//				out= new FileOutputStream(file);
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			}
//			try {
//				out.write(json.getBytes());
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			//handleMainActivityOrderListener.onHandleOrder(REQUEST, json);
			//Log.d(TAG, "json = " + json);
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
		}
	}

	public void doUpload(boolean whileFlag, ServerSocket serverSocket) {
		while (whileFlag) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			ObjectInputStream input = null;
			try {
				input = new ObjectInputStream(socket.getInputStream());
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				myUploadFile = (UploadFile) input.readObject();
			} catch (OptionalDataException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "Download" + File.separator
					+ myUploadFile.getFileName());
			FileOutputStream output = null;
			try {
				output = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				output.write(myUploadFile.getFileContent());
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
		}
	}

	public void doDownload(boolean whileFlag, ServerSocket serverSocket) {
		while (whileFlag) {
			Socket socket = null;
			String request = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			DataInputStream input = null;
			ObjectOutputStream output = null;
			try {
				output = new ObjectOutputStream(socket.getOutputStream());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				input = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				request = input.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			UploadFile responseFile = CommonUtil.getDownloadFile(request);
			Log.d(TAG, "do download responseFile = " + responseFile);
			try {
				output.writeObject(responseFile);
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
		}
	}
	
	public void doDownload2(boolean whileFlag, ServerSocket serverSocket) {
		while (whileFlag) {
			Socket socket = null;
			String request = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			DataInputStream input = null;
			DataOutputStream output = null;
			FileInputStream fis = null;
			try {
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
				request = input.readUTF();
//				UploadFile responseFile = CommonUtil.getDownloadFile(request);
//				uploadFile.setFileName(file.getName());
//				uploadFile.setPath(file.getAbsolutePath());
//				uploadFile.setParentRoot(file.getParentFile().getPath());
//				uploadFile.setDate(String.valueOf(file.lastModified()));
//				uploadFile.setFileSize(file.length());
//				uploadFile.setType("file");
//				uploadFile.setVersion("0");
//				uploadFile.setIsCurrentVersion("0");
//				uploadFile.setStatus("available");
				JSONObject root = new JSONObject(request);
				String requestCode = root.getString("request");
				if (requestCode.equals("downloadFile")) {
					String path = root.getString("param");
					Log.d(TAG, "download path = " + path);
					File file = new File(path);
					fis = new FileInputStream(file);
					
					output.writeUTF(file.getName());
					output.flush();
					output.writeUTF(file.getAbsolutePath());
					output.flush();
					output.writeUTF(file.getParentFile().getAbsolutePath());
					output.flush();
					output.writeUTF(String.valueOf(file.lastModified()));
					output.flush();
					output.writeLong(file.length());
					output.flush();
					output.writeUTF("file");
					output.flush();
					output.writeUTF("0");
					output.flush();
					output.writeUTF("0");
					output.flush();
					output.writeUTF("available");
					output.flush();
					
					int len = 0;
					byte[] buffer = new byte[4*1024];
					while ((len = fis.read(buffer, 0, buffer.length) )!= -1) {
						output.write(buffer, 0, len);
						output.flush();
					}
					
				}
				fis.close();
				input.close();
				output.close();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	public void initUdpBroadcastSender() {
		try {
			udpSenderSocket = new MulticastSocket(BROADCAST_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			udpSendAddress = InetAddress.getByName(BROADCAST_IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			udpSenderSocket.setTimeToLive(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			udpSenderSocket.joinGroup(udpSendAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSendThread = new UdpSendThread();
		mSendThread.start();
	}
	
	public class UdpSendThread extends Thread{
		@Override
		public void run() {
			super.run();
			String ip = CommonUtil.getServerIpAddress(TVService.this);
			byte[] data = ip.getBytes();
			Log.d(TAG, "ip = " + ip);
			udpSenderPacket = new DatagramPacket(data, data.length, udpSendAddress, BROADCAST_PORT);
			while (udpSenderFlag) {
				try {
					udpSenderSocket.send(udpSenderPacket);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String handleRequest(String content){
		String responseJson = "";
		mJsonParseUtil = new JsonParseUtil(mHandler, this);
		try {
			JSONObject requestRoot = new JSONObject(content);
			String requestCode = requestRoot.getString("request");
			if (requestCode.equals("getFiles")) {
				responseJson = this.getFiles(requestRoot);
			}else if (requestCode.equals("moveFiles")) {
				responseJson = mJsonParseUtil.moveFiles(requestCode,requestRoot);
			}else if (requestCode.equals("copyFiles")) {
				responseJson = mJsonParseUtil.copyFiles(requestCode,requestRoot);
			}else if(requestCode.equals("deleteFiles")){
				responseJson = mJsonParseUtil.deleteFiles(requestCode,requestRoot);
			}else if (requestCode.equals("renameFile")) {
				responseJson = mJsonParseUtil.renameFile(requestCode,requestRoot);
			}else if (requestCode.equals("login")) {
				String password = mSharedPrefService.getString(MainActivity.PASSWORD, "null");
				responseJson = mJsonParseUtil.login(requestCode,requestRoot,password);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	private String getFiles(JSONObject requestRoot) {
		String responseJson = "null";
		try {
			String param = requestRoot.getString("param");
			if (param.equals(REQUEST_IMAGES_PARAM)) {
				responseJson = JsonMakeUtil.getAllImages(CommonUtil.ROOT_PATH);
			}else if(param.equals(REQUEST_MUSICS_PARAM)){
				responseJson = JsonMakeUtil.getAllMusics(CommonUtil.ROOT_PATH);
			}else if(param.equals(REQUEST_VIDEOS_PARAM)){
				responseJson = JsonMakeUtil.getAllVideos(CommonUtil.ROOT_PATH);
			}else if(param.equals(REQUEST_DOCUMENTS_PARAM)){
				responseJson = JsonMakeUtil.getAllDocuments(CommonUtil.ROOT_PATH);
			}else if(param.equals(REQUEST_OTHERS_PARAM)){
				responseJson = JsonMakeUtil.getOthers(CommonUtil.ROOT_PATH);
			}else{
				responseJson = JsonMakeUtil.getAllFiles(param);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseJson;
	}
	
	Handler searchHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SEARCH:
				final String date = mSharedPrefService.getString(KEY_DATE, "0");
				new Thread(new Runnable() {
					@Override
					public void run() {
						getPhotoDataFromServer(date);
					}
				}).start();
				break;
			case DOWNLOAD_RESULT:
				break;
			case DATA_NULL:
				break;
			default:
				break;
			}
			
		}
	};
	
	public void getPhotoDataFromServer(String date){
		String url = CommonUtil.BASE_URL + "user_id=" + CommonUtil.USER_ID
				+ "&token=" + CommonUtil.TOKEN + "&date=" + date
				+ "&begin=0&size=40&type=" + CommonUtil.TYPE;
		try {
			URL realUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			conn.setConnectTimeout(20000);
			conn.setRequestMethod("GET");

			int responseCode = conn.getResponseCode();
			Log.d(TAG, "getPhotoDataFromServer : responseCode = " + responseCode);
			if (200 == conn.getResponseCode()) {
				String requestDate = this.parsePhotoDateJson(conn.getInputStream());
			} else {
				Log.d(TAG, "get photo data response code = " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void downloadPhoto(String fileId , String fileName , String requestDate) {
		String path = CommonUtil.PHOTO_PATH + fileName;
		String url = CommonUtil.DOWNLOAD_BASE_URL + "user_id=" + CommonUtil.USER_ID
				+ "&file_id=" + fileId;
		Log.d(TAG, "download url = "+ url);
		synchronized (TVService.this) {
			try {
				URL realUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) realUrl
						.openConnection();
				conn.setConnectTimeout(20000);
				conn.setRequestMethod("GET");

				int responseCode = conn.getResponseCode();
				Log.d(TAG, "downloadPhoto : responseCode = " + responseCode);
				if (200 == conn.getResponseCode()) {
					File file = new File(path);
					file.setLastModified(Long.valueOf(requestDate));
					Log.d(TAG, "current download photo path = " + path);
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					FileOutputStream output = new FileOutputStream(file);
					InputStream input = conn.getInputStream();
					byte[] buffer = new byte[2048];
					int len = 0;
					while ((len = input.read(buffer))!=-1) {
						output.write(buffer, 0, len);
					}
					output.close();
					input.close();
					mSharedPrefService.edit().putString(KEY_DATE, requestDate).commit();
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
				mSharedPrefService.edit().putString(KEY_DATE, requestDate).commit();
			} 
		}
	}

	private String parsePhotoDateJson(InputStream inputStream) {
		String requestDate = null;
		String fileId = null;
		String fileName = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		try {
			String json = br.readLine();
			Log.d(TAG, "request photo json = " + json);
			JSONObject root = new JSONObject(json);
			JSONArray data = root.getJSONArray("data");
			if (data != null || !data.equals("") || !data.equals("null")) {
				for (int i = 0; i < data.length(); i++) {
					JSONObject childData = data.getJSONObject(i);
					fileId = childData.getString("id");
					fileName = childData.getString("name");
					requestDate = getTimeMillions(childData.getString("lmf_date"));
					downloadPhoto(fileId, fileName ,requestDate);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestDate;
	}

	private String getTimeMillions(String date) {
		String result = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d = format.parse(date);
			result = String.valueOf(d.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void closeServerSocket() throws Exception {
		if (requestServerSocket != null) {
			requestServerSocket.close();
			requestServerSocket = null;
		}
		if (uploadServerSocket != null) {
			uploadServerSocket.close();
			uploadServerSocket = null;
		}
		if (downloadServerSocket != null) {
			downloadServerSocket.close();
			downloadServerSocket = null;
		}
		if (udpSenderSocket != null) {
			udpSenderSocket.close();
			udpSenderSocket = null;
		}
	}


}
