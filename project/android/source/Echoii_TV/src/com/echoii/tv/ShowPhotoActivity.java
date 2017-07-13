package com.echoii.tv;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.echoii.tv.util.CommonUtil;

public class ShowPhotoActivity extends Activity {
	public static final String TAG = "ShowPhotoActivity";
	public static final int SEARCH = 0x001;
	public static final int REQUEST_DOWNLOAD = 0x002;
	public static final int DOWNLOAD_RESULT = 0x003;
	public static final int DATA_NULL = 0x004;
	public static final String KEY_DATE = "date";
	
	private ImageView mShowPhotoImage;
	private ProgressBar mShowProgress;
	private Timer mSearchTimer;
	private TimerTask mSearchTask;
	private String mFileName ;
	private String mFileId;
	private SharedPreferences mDatePreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_photo);
		mDatePreferences = this.getSharedPreferences("shared_date", Context.MODE_PRIVATE);
		mShowPhotoImage = (ImageView) this.findViewById(R.id.show_photo);
		mShowProgress = (ProgressBar) this.findViewById(R.id.show_progress);
		mShowProgress.setVisibility(View.GONE);
		
//		ScaleAnimation sa = new ScaleAnimation(0, 0, 100, 100);
//		showPhotoImage.setImageResource(R.drawable.example);
//		showPhotoImage.setAnimation(sa);
		initSearchServer();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
		mSearchTimer.schedule(mSearchTask, 0, 10000);
	}
	
	Handler searchHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SEARCH:
				final String date = mDatePreferences.getString(KEY_DATE, "0");
				mShowProgress.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					@Override
					public void run() {
						getPhotoDataFromServer(date);
					}
				}).start();
				break;
			case DOWNLOAD_RESULT:
				File file = new File(CommonUtil.PHOTO_PATH + mFileName);
				ScaleAnimation animation = new ScaleAnimation(0, 0, 100, 100);
				mShowPhotoImage.setAnimation(animation);
				mShowPhotoImage.setImageURI(Uri.fromFile(file));
				mShowProgress.setVisibility(View.GONE);
				break;
			case DATA_NULL:
				mShowProgress.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			
		}
	};
	
	public void getPhotoDataFromServer(String date){
		String url = CommonUtil.BASE_URL + "user_id=" + CommonUtil.USER_ID
				+ "&token=" + CommonUtil.TOKEN + "&date=" + date
				+ "&begin=0&size=1&type=" + CommonUtil.TYPE;
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
				mDatePreferences.edit().putString(KEY_DATE, requestDate).commit();
				downloadPhoto();
			} else {
				Message msg = new Message();
				msg.what = DATA_NULL;
				searchHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private void downloadPhoto() {
		String path = CommonUtil.PHOTO_PATH + mFileName;
		String url = CommonUtil.DOWNLOAD_BASE_URL + "user_id=" + CommonUtil.USER_ID
				+ "&token=" + CommonUtil.TOKEN + "&file_id=" + mFileId;
		Log.d(TAG, "download url = "+ url);
		Log.d(TAG,"download file id = " + mFileId + "; file name = " + mFileName);
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
				Message msg = new Message();
				msg.what = DOWNLOAD_RESULT;
				searchHandler.sendMessage(msg);
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	private String parsePhotoDateJson(InputStream inputStream) {
		String requestDate = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		try {
			JSONObject root = new JSONObject(br.readLine());
			JSONArray data = root.getJSONArray("data");
			if (data != null || !data.equals("") || !data.equals("null")) {
				JSONObject childData = data.getJSONObject(0);
				this.mFileId = childData.getString("id");
				this.mFileName = childData.getString("name");
				requestDate = getTimeMillions(childData.getString("lmf_date"));
				Log.d(TAG,"parsePhotoDateJson file id = " + mFileId + "; file name = " + mFileName + ";date = " + requestDate);
			}else{
				
			}
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

}
