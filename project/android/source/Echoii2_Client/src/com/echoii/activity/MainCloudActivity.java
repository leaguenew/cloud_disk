package com.echoii.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

import com.echoii.activity.fragment.CloudFileListFragment;
import com.echoii.activity.fragment.DataCenterConnectionFragment;
import com.echoii.activity.fragment.DataCenterListFragment;
import com.echoii.activity.fragment.DataCenterLoginFragment;
import com.echoii.activity.fragment.PhoneFileListFragment;
import com.echoii.constant.CommonUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class MainCloudActivity extends SlidingActivity{
	public static final String TAG = "MainCloudActivity";
	public  boolean IS_OPEN_FILE = false;
	
	public SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
	public SharedPreferences ipPreference;
	public SharedPreferences idToken;
	
	private FragmentManager fm;
	private CenterFragment2 centerFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_main);

		sessionStatusPreference = getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		ipPreference = getSharedPreferences(DataCenterConnectionFragment.IP_SHARED_NAME, Context.MODE_PRIVATE);
		idToken = getSharedPreferences(CommonUtil.NEED_TOKENID,0);
		// set the Behind View
		setBehindContentView(R.layout.frame_menu);
		
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		Fragment menuFragment = new LeftFragment();
		centerFragment =  new CenterFragment2();
		fragmentTransaction.replace(R.id.sliding_menu, menuFragment);
		fragmentTransaction.replace(R.id.main,centerFragment);
		fragmentTransaction.commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(50);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(60);
		sm.setFadeDegree(0.35f);
		int behindWidth = (int) (260 * this.getResources().getDisplayMetrics().density);
		sm.setBehindWidth(behindWidth);
		// 设置slding menu的几种手势模式
		// TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
		// TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding
		// menu
		// TOUCHMODE_NONE 自然是不能通过手势打开啦
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		Log.d(TAG, "Main OnCreate");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "Main onPause");
		IS_OPEN_FILE = true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "Main onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "Main onStop");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "Main onResume");
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				IS_OPEN_FILE  = false;
			}
		},1000);
	}
	
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyUp : IS_OPEN_FILE = " + IS_OPEN_FILE);
		if (IS_OPEN_FILE) {
			return true;
		}
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			String tabTag = sessionStatusPreference.getString(
					CenterFragment2.SESSION_TAB_CHANGE_TAG, "null");
			fm = this.getSupportFragmentManager();
			if (tabTag.equals("cloud")) {
				if (LoginActivity.LOGIN_FLAG) {
				}else{
					CloudFileListFragment cloudFileListFragment = (CloudFileListFragment)fm.findFragmentByTag("cloudListFragment");
					String path = sessionStatusPreference.getString(CenterFragment2.SESSION_CLOUD_PARENT_PATH, "");
					int itemId = sessionStatusPreference.getInt(CenterFragment2.SESSION_CLOUD_FILE_TPYE, 0);
					String id = idToken.getString(CommonUtil.USER_ID,"");
	    			String token = idToken.getString(CommonUtil.USER_TOKEN, "");
					Log.d(TAG," cloud path = " + path);
//					if (path.equals("nullcontent"))
//					{
//						f.getAllFileData(id,token,itemId,0, 40, "",null);
//					}
//					else 
					if(path.equals("CloudFragment"))
					{
						int sessionId = sessionStatusPreference.getInt(CenterFragment2.SESSION_CLOUD_FILE_TPYE, 0);
						centerFragment.onFragmentChange(CenterFragment2.CLOUD_FRAGMENT, sessionId);
					} else if (path.equals("DataMarketHomeFragment"))
					{
						centerFragment.onFragmentChange(CenterFragment2.DATA_MARKET_HOME, -1);
					}
					else if (path.equals("null"))
					{
//						finish();
					}
					else
					{
						//请求 该路径，刷新界面
						cloudFileListFragment.getAllFileData(id,token,itemId,0, 40, path,null);
					}
				}
			} else if (tabTag.equals("data")) {
				DataCenterListFragment dCenterListFragment = (DataCenterListFragment) fm.findFragmentByTag("datalistFragment");
				String ip = ipPreference.getString(DataCenterConnectionFragment.CURRENT_CONN_IP, "null");
				int itemId = sessionStatusPreference.getInt(CenterFragment2.SESSION_DATA_CENTER_FILE_TYPE, 0);
				String path = sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH, "null");
				Log.d(TAG, "path = " + path);
				if (path.equals("DataCenterFragment")) {
					if (!DataCenterListFragment.isEditState) {
						centerFragment.onFragmentChange(CenterFragment2.DATA_CENTER_FRAGMENT, -1);
					}
				}else if(path.startsWith("/") || DataCenterListFragment.isOperating){
					if (path.equals(CommonUtil.SDCARD_ROOT_PATH)) {
						dCenterListFragment.getDataCenterData(ip, itemId,path, DataCenterListFragment.FILE_ROOT_PATH);
					}else{
						dCenterListFragment.getDataCenterData(ip,itemId, path, DataCenterListFragment.FILE_CHILD_PATH);
					}
				}else if (path.equals("DataMarketHomeFragment")){
					centerFragment.onFragmentChange(CenterFragment2.DATA_MARKET_HOME, -1);
				}else if (path.equals("DataCenterLoginFragment")) {
					centerFragment.onFragmentChange(CenterFragment2.DATA_CENTER_LOGIN_FRAGMENT, -1);
				}else if(path.equals("DataCenterConnectionFragment")){
					centerFragment.onFragmentChange(CenterFragment2.DATA_CENTER_CONN_FRAGMENT, -1);
				}
				else {
//					this.finish();
					Log.d(TAG, "finish");
				}
			} else if (tabTag.equals("phone")) {
				
				PhoneFileListFragment phone = (PhoneFileListFragment)fm.findFragmentByTag("phoneListFragment");
				String path = sessionStatusPreference.getString(CenterFragment2.SESSION_PHONE_PARENT_PATH, "null");
				Log.d("mating","main--phone next path = " + path);
				if (path.equals("PhoneFragment"))
				{
					centerFragment.onFragmentChange(CenterFragment2.PHONE_FRAGMENT, -1);
				}
				else if (path.equals("null"))
				{
//					finish();
				}
				else if (path.equals("DataMarketHomeFragment"))
				{
					centerFragment.onFragmentChange(CenterFragment2.DATA_MARKET_HOME, -1);
				}
				else
				{
					phone.getFileListData(path,0,this);
				}

			}
			else {

			}
			break;

		default:
			break;
		}
		return true;
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "Main onRestart");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Main onDestroy");
//		boolean conn = sessionStatusPreference.getBoolean(DataCenterConnectionFragment.DATA_CENTER_AUTO_CONN, false);
//    	boolean loginStatus = sessionStatusPreference.getBoolean(DataCenterLoginFragment.DATA_CENTER_LOGIN_SUCCESS, false);
//    	Log.d(TAG, "conn = " + conn + " / login status = " + loginStatus );
//		if (sessionStatusPreference != null) {
//			sessionStatusPreference.getAll().clear();
//			sessionStatusPreference.edit().clear().commit();
//			sessionStatusPreference = null;
//		}
//		if (ipPreference != null) {
//			ipPreference.getAll().clear();
//			ipPreference.edit().clear().commit();
//			ipPreference = null;
//		}
		LoginActivity.LOGIN_FLAG = false;
	}

}
