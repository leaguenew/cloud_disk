package com.echoii.tv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.echoii.tv.constants.Constants;
import com.echoii.tv.fragment.WelcomeFragment;
import com.echoii.tv.fragment.cloud.CloudCategoryFragment;
import com.echoii.tv.fragment.cloud.CloudCategoryFragment.OnCloudCategoryClickListener;
import com.echoii.tv.fragment.cloud.CloudFragment;
import com.echoii.tv.fragment.cloud.CloudManagerFragment;
import com.echoii.tv.fragment.cloud.LoginFragment.OnCloudLoginListener;
import com.echoii.tv.fragment.cloud.TopCloudActionBarFragment;
import com.echoii.tv.fragment.cloud.TopCloudActionBarFragment.OnBackToParentPathListener;
import com.echoii.tv.fragment.datacenter.DatacenterFragment;
import com.echoii.tv.fragment.datacenter.ListDatacenterFileFragment;
import com.echoii.tv.fragment.datacenter.TopDatacenterActionBarFragment;
import com.echoii.tv.fragment.setting.EchoiiPrefencesFragment;
import com.echoii.tv.model.Device;
import com.echoii.tv.service.DatacenterService;
import com.echoii.tv.service.DatacenterService.LocalBinder;
import com.echoii.tv.util.CommonUtil;
import com.echoii.tv.util.JsonUtil;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>主界面</p>
 *
 */
public class EchoiiTVMainActivity extends FragmentActivity 
	implements OnBackToParentPathListener,
			   OnCloudLoginListener,
			   OnCloudCategoryClickListener{
	
	public static final String TAG = "EchoiiTVMainActivity2";
	
	public Handler handler = new MyHandler(); 
	public Device device;
	
	private FrameLayout preferenceLayout;
	
	private Context context;
	private ViewPager pager;
	private MyAdapter adapter;
	private DatacenterAuth auth;
	private DatacenterService service;
	private Intent serviceIntent;
	private SharedPreferences defaultPreference;
	private ProgressDialog serviceDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		
		context = this;
		defaultPreference = PreferenceManager.getDefaultSharedPreferences(this);
		
		datacenterLogin();
		initViews();
		bindService();
		initSyncDefaultPath();
	}


	private void initSync() {
		if (defaultPreference.getBoolean("sync_swich", false)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					service.startSync(Constants.SYNC_ALL_FILES);
				}
			}).start();
		}
	}


	private void initViews() {
		pager = (ViewPager) this.findViewById(R.id.pager);
		adapter = new MyAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				pager.requestDisallowInterceptTouchEvent(true);
				return true;
			}
		});
		pager.setCurrentItem(0);
	}


	private void initPreference() {
		preferenceLayout = (FrameLayout) this.findViewById(R.id.preference);
		EchoiiPrefencesFragment fragment = EchoiiPrefencesFragment.getInstance();
		android.app.FragmentTransaction prefTransaction = this.getFragmentManager().beginTransaction();
		prefTransaction.replace(R.id.preference, fragment, EchoiiPrefencesFragment.TAG).commit();
		preferenceLayout.setVisibility(View.GONE);
	}
	
	private void datacenterLogin() {
		
		auth = new DatacenterAuth(context, handler);
		final String deviceId = CommonUtil.getLocalMacAddress(context);
		final String deviceToken = getDeviceToken();
		LogUtil.d(TAG, "deviceId = " + deviceId + " / devicePassword = " + deviceToken);
		device = new Device();
		device.setDeviceId(deviceId);
		device.setDeviceToken(deviceToken);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if (auth == null) {
						LogUtil.d(TAG, "auth is null");
						return ;
					}
					auth.login(device);
				} catch (SocketTimeoutException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private String getDeviceToken() {
		
		String password = null;
		try {
			
			password = JsonUtil.parseDeviceInfo(CommonUtil.readFile());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return password;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
        	switch (position) {
			case 0:
				Fragment welcomeFragment = WelcomeFragment.getInstance();
				return welcomeFragment;
			case 1:
				Fragment cloudManagerFragment = CloudManagerFragment.getInstance();
				return cloudManagerFragment;
			case 2:
				Fragment datacenterFragment = DatacenterFragment.getInstance();
				return datacenterFragment;
			default:
				return WelcomeFragment.getInstance();
			}
		}
    }
	
	public class MyHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				
			default:
				break;
			}
		}
		
	}
	
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder b) {
			LocalBinder binder = (LocalBinder) b;
			service = binder.getDatacenterService();
			service.setHandler(handler);
			service.setDevice(device);
			initPreference();
			initSync();
			initSambaServer();
			serviceDialog.dismiss();
			LogUtil.d(TAG, "DatacenterService = " + service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			service = null;
		}

	};
	
	public DatacenterService getService() {
		return service;
	}

	private void bindService() {
		if (service == null) {
			try {
				serviceDialog = new ProgressDialog(this);
				serviceDialog.setMessage(this.getResources().getString(R.string.service_opening));
				serviceDialog.show();
				serviceIntent = new Intent(this, DatacenterService.class);
				bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
			} catch (Exception e) {
				e.printStackTrace();
				serviceDialog.dismiss();
//				this.unbindService(conn);
			}
		}
	}
	
	public void unBindService() {
		
		if (service == null) {
			return;
		}
		service.stopJLANServer();
		service.stopForeground(true);
		this.unbindService(conn);
		service = null;
		
	}
	
	public void showPreferenceFragment() {
		pager.setVisibility(View.GONE);
		preferenceLayout.setVisibility(View.VISIBLE);
	}
	
	public void showViewPager() {
		pager.setVisibility(View.VISIBLE);
		preferenceLayout.setVisibility(View.GONE);
	}
	
	private void initSyncDefaultPath() {
		String syncPath = PreferenceManager.getDefaultSharedPreferences(this).getString(EchoiiPrefencesFragment.KEY_DEFAULT_SYNC_PATH, null);
		if (syncPath == null) {
			syncPath = Constants.SYNC_SDCARD_BASE_PATH + File.separator + "echoii"  + File.separator + CommonUtil.getLocalMacAddress(context) + File.separator + "data" ;
		} 
		Constants.SYNC_DEFAULT_PATH = syncPath;
		File file = new File(Constants.SYNC_DEFAULT_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		LogUtil.d(TAG, "sync default path = " + Constants.SYNC_DEFAULT_PATH);
	}

	@Override
	public void onBackToParentPath(String tag) {
		if (tag.equals(TopCloudActionBarFragment.TAG)) {
			CloudManagerFragment cloudManagerFragment = (CloudManagerFragment) ((FragmentPagerAdapter)pager.getAdapter()).instantiateItem(pager, 1);
			cloudManagerFragment.backToParentCloudList();
		} else if (tag.equals(TopDatacenterActionBarFragment.TAG)) {
			DatacenterFragment dcFragment = (DatacenterFragment) ((FragmentPagerAdapter)pager.getAdapter()).instantiateItem(pager, 2);
			ListDatacenterFileFragment fragment = (ListDatacenterFileFragment) dcFragment.getChildFragmentManager().findFragmentByTag(ListDatacenterFileFragment.TAG);
			fragment.back();
		}
		
	}

	@Override
	public void onCloudLoginSuccess() {
		CloudManagerFragment cloudFragment = (CloudManagerFragment) ((FragmentPagerAdapter)pager.getAdapter()).instantiateItem(pager, 1);
		cloudFragment.manageCloudFragment(CloudCategoryFragment.TAG);
	}

	@Override
	public void onCloudLoginError() {
		Toast.makeText(this, getResources().getString(R.string.cloud_login_error), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCloudCategoryClick(int position) {
		CloudManagerFragment cloudManagerFragment = (CloudManagerFragment) ((FragmentPagerAdapter)pager.getAdapter()).instantiateItem(pager, 1);
		cloudManagerFragment.setPosition(position);
		cloudManagerFragment.manageCloudFragment(CloudFragment.TAG);
	}


	private void initSambaServer() {
		if (defaultPreference.getBoolean("samba_share", false)) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					if (service.startJLANServer()) { 
						LogUtil.d(TAG, "samba server start success");
					}else{
						LogUtil.d(TAG, "samba server start error");
					}
					
				}
				
			}).start();
		}
	}

}
