package com.echoii.tv.service;

import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.echoii.tv.EchoiiTVMainActivity;
import com.echoii.tv.JLANServerImpl;
import com.echoii.tv.R;
import com.echoii.tv.SyncFiles;
import com.echoii.tv.model.Device;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>后台服务，开启Samba文件共享服务和同步下载家庭数据中心服务</p>
 *
 */
public class DatacenterService extends Service {

	public static final String TAG = "DatacenterService";

	private IBinder binder = new LocalBinder();
	private JLANServerImpl jlanServerImpl;
	private SyncFiles mSyncFiles;
	private Handler handler;
	private Device device;
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public JLANServerImpl getJlanServerImpl() {
		return jlanServerImpl;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public boolean startJLANServer(){
		if (jlanServerImpl == null) {
			return false;
		}
		try {
			InputStream in = this.getResources().openRawResource(R.raw.jlan_config);
			if (!jlanServerImpl.start(new InputStreamReader(in))) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void stopJLANServer(){
		if (jlanServerImpl == null) {
			return;
		}
		jlanServerImpl.stop();
	}

	public class LocalBinder extends Binder {
		public DatacenterService getDatacenterService() {
			return DatacenterService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		LogUtil.d(TAG, "DatacenterService onBind ");
		return binder;
	}

	@Override
	public void onCreate() {
		LogUtil.d(TAG, "DatacenterService onCreate");
		super.onCreate();
		init();
		initForegroundNotification();
	}

	private void init() {
		
		jlanServerImpl = JLANServerImpl.getJLANServerImpl();
		mSyncFiles = new SyncFiles(this.getApplicationContext(), handler);
		
	}

	@SuppressWarnings("deprecation")
	private void initForegroundNotification() {
		
		String title = this.getResources().getString(R.string.service_on);
		Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
		PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this,EchoiiTVMainActivity.class), 0);
		notification.setLatestEventInfo(this, title, title, intent);
		startForeground(0x110, notification);
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d(TAG, "DatacenterService onStartCommand");
		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "DatacenterService onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		LogUtil.d(TAG, "DatacenterService onUnbind");
		return super.onUnbind(intent);
	}
	
	public void startSync(int flag){
		mSyncFiles.startSync(device, flag);
	}
	
	public void stopSync() {
		mSyncFiles.stopSync();
	}

}
