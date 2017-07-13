package com.echoii.activity.fragment;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.activity.CenterFragment2;
import com.echoii.activity.MainCloudActivity;
import com.echoii.activity.R;

public class DataCenterConnectionFragment extends Fragment {
	public static final String TAG = "DataCenterConnectionFragment";
	public static final String IP_SHARED_NAME = "ip_sharedpreference";  //ip SharedPreferences的名称
	public static final String CURRENT_CONN_IP = "current_datacenter_conn_ip"; //保存当前连接ip的key
	public static final int RESULT_SUCCESS = 0x001;
	public static final int RESULT_FAIL = 0x002;
	public static final int RESULT_CANCEL = 0x003;
	public static final int RESULT_TIME_OUT = 0x004;
	public static final String DATA_CENTER_AUTO_CONN = "auto_conn";
	
	private Activity mActivity;
	private OnFragmentChangeListener mListener;
	private ProgressBar searchProgress;
	private LinearLayout searchResult;
	private ImageView settingImage;
	private CheckBox mCbAutoConn;
	private TextView mTvSearch;

	public boolean flag = true;
	public static final String BROADCAST_IP = "224.0.0.1";
	public static final int BROADCAST_PORT = 9001;
	public String udpServerIp;
	private MulticastSocket udpReceiveSocket;
	private DatagramPacket udpReceivePacket;
	private ReceiveThread mThread;
	private InetAddress udpAddress;
	
	public SharedPreferences ipPreference;
	private SharedPreferences sessionStatusPreference;
	
	public DataCenterConnectionFragment(){
	}
	
	public DataCenterConnectionFragment(OnFragmentChangeListener listener){
		this.mListener = listener;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		ipPreference = mActivity.getSharedPreferences(DataCenterConnectionFragment.IP_SHARED_NAME, Context.MODE_PRIVATE);
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PATH, "DataCenterConnectionFragment").commit();
    	sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH, "null").commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_datacenter_conn, null);
		mTvSearch = (TextView) view.findViewById(R.id.disconnection);
		mTvSearch.setText("搜索");
		mTvSearch.setVisibility(View.VISIBLE);
		searchProgress = (ProgressBar) view.findViewById(R.id.data_center_search_progress);
		searchResult = (LinearLayout) view
				.findViewById(R.id.data_center_search_result);
		mTvSearch.setOnClickListener(new SearchButOnClickListenerImpl());
		settingImage = (ImageView) view.findViewById(R.id.setting);
		settingImage.setOnClickListener(new OnClickListenerImpl());
		mCbAutoConn = (CheckBox) view.findViewById(R.id.cb_dcenter_auto_conn);
		mCbAutoConn.setVisibility(View.GONE);
		mCbAutoConn.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		return view;
	}

	private class SearchButOnClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			mTvSearch.setVisibility(View.INVISIBLE);
			searchProgress.setVisibility(View.VISIBLE);
			mCbAutoConn.setVisibility(View.GONE);
			searchResult.removeAllViews();
			flag = true;
			startSearch();
		}
	}

	int count = 1;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case RESULT_SUCCESS:
				String ip = new String(msg.obj.toString());
				try {
					ImageView image = new ImageView(mActivity);
					image.setImageResource(R.drawable.datacenter_icon);
					image.setScaleType(ScaleType.FIT_CENTER);
					LinearLayout.LayoutParams imageLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					imageLp.gravity = Gravity.CENTER_HORIZONTAL;
					image.setLayoutParams(imageLp);
					TextView text = new TextView(mActivity);
					text.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
					text.setGravity(Gravity.CENTER_HORIZONTAL);
					text.setText(ip);
					Button but = new Button(mActivity);
					LinearLayout.LayoutParams buttonLp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
					int marginLeft = (int) (32 * mActivity.getResources().getDisplayMetrics().density);
					int marginTop = (int) (8 * mActivity.getResources().getDisplayMetrics().density);
					buttonLp.setMargins(marginLeft, marginTop, marginLeft, marginTop);
					but.setLayoutParams(buttonLp);
					but.setBackgroundResource(R.drawable.datacenter_button_bg);
					but.setText("家庭数据中心盒子 " + count++);
					but.setTextColor(Color.WHITE);
					but.setTag(ip);
					but.setOnClickListener(new SearchResultButtonOnClickListenerImpl());
					searchResult.addView(image);
					searchResult.addView(text);
					searchResult.addView(but);
					mCbAutoConn.setVisibility(View.VISIBLE);
					mCbAutoConn.setChecked(false);
					searchResult.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case RESULT_FAIL:
				flag = false;
				searchProgress.setVisibility(View.INVISIBLE);
				mTvSearch.setVisibility(View.VISIBLE);
				stopSearch();
				break;
			case RESULT_CANCEL:
				flag = false;
				searchProgress.setVisibility(View.INVISIBLE);
				mTvSearch.setVisibility(View.VISIBLE);
				stopSearch();
				break;
			case RESULT_TIME_OUT:
				flag = false;
				searchProgress.setVisibility(View.INVISIBLE);
				mTvSearch.setVisibility(View.VISIBLE);
				stopSearch();
				Toast.makeText(mActivity, "网络连接异常，请检查网络...", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	public class ReceiveThread extends Thread {
		@Override
		public void run() {
			super.run();
			byte[] data = new byte[50];
			udpReceivePacket = new DatagramPacket(data, data.length,
					udpAddress, BROADCAST_PORT);
			ArrayList<String> ipAddress = new ArrayList<String>();
			long lastTime = System.currentTimeMillis();
			Log.d(TAG, "flag = " + flag + "/last time = " + lastTime);
			while (flag) {
					
				try {
					udpReceiveSocket.setSoTimeout(10000);
				} catch (SocketException e) {
					e.printStackTrace();
				}
				try {
					udpReceiveSocket.receive(udpReceivePacket);
				} catch (IOException e) {
					e.printStackTrace();
					Message message = new Message();
					message.what = RESULT_FAIL;
					mHandler.sendMessageDelayed(message, 100);
					return;
				}
					
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				udpServerIp = new String(udpReceivePacket.getData(),udpReceivePacket.getOffset(),udpReceivePacket.getLength());
				if (TextUtils.isEmpty(udpServerIp)) {
					Message message = new Message();
					message.obj = udpServerIp;
					message.what = RESULT_FAIL;
					mHandler.sendMessageDelayed(message, 100);
				}
				Log.d(TAG, "ip = " + udpServerIp);
				if (ipAddress.size() > 0) {
					if (!ipAddress.contains(udpServerIp)) {
						Message message = new Message();
						message.obj = udpServerIp;
						message.what = RESULT_SUCCESS;
						mHandler.sendMessageDelayed(message, 100);
						ipAddress.add(udpServerIp);
					}
				}else{
					Message message = new Message();
					message.obj = udpServerIp;
					message.what = RESULT_SUCCESS;
					mHandler.sendMessageDelayed(message, 100);
					ipAddress.add(udpServerIp);
				}
				long currentTime = System.currentTimeMillis();
				Log.d(TAG, "currentTime - lastTime = " + (currentTime - lastTime));
				if (currentTime - lastTime > 10000) {
					Message message2 = new Message();
					message2.what = RESULT_CANCEL;
					mHandler.sendMessageDelayed(message2, 100);
					break;
				}
			}
		}
	}

	public void startSearch() {
		try {
			udpReceiveSocket = new MulticastSocket(BROADCAST_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			udpAddress = InetAddress.getByName(BROADCAST_IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			udpReceiveSocket.joinGroup(udpAddress);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mActivity, "没有发现家庭数据中心，请检查是否已开启文件共享", Toast.LENGTH_SHORT).show();
			flag = false;
			searchProgress.setVisibility(View.INVISIBLE);
			mTvSearch.setVisibility(View.VISIBLE);
			stopSearch();
			return ;
		}
		mThread = new ReceiveThread();
		mThread.start();
	}
	
	private class SearchResultButtonOnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			ipPreference.edit().putString(CURRENT_CONN_IP, (String)v.getTag()).commit();
			flag = false;
			stopSearch();
//			DataCenterConnectionFragment.this.mListener.onFragmentChange(CenterFragment2.DATA_CENTER_FRAGMENT , -1);
			DataCenterConnectionFragment.this.mListener.onFragmentChange(CenterFragment2.DATA_CENTER_LOGIN_FRAGMENT , -1);
		}
		
	}
	
	public void stopSearch(){
		try {
			if (!udpReceiveSocket.isClosed()) {
				udpReceiveSocket.leaveGroup(udpAddress);
				udpReceiveSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class OnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			((MainCloudActivity)mActivity).toggle();
		}
	}
	
	private class OnCheckedChangeListenerImpl implements OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				sessionStatusPreference.edit().putBoolean(DATA_CENTER_AUTO_CONN, true).commit();
			}else{
				sessionStatusPreference.edit().remove(DATA_CENTER_AUTO_CONN).commit();
			}
		}
	}

}
