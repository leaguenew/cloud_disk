package com.echoii.tv;

import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.echoii.tv.service.TVService;
import com.echoii.tv.service.TVService.LocalBinder;
import com.echoii.tv.util.CommonUtil;
import com.echoii.tv.util.LoginUtil;

public class MainActivity extends Activity implements OnClickListener{
	
	public static final String TAG = "TVMainActivity";

	public static final String PASSWORD = "password";
	
	private TVService mTVService;
	private Intent serviceIntent;
	private static ComponentName serviceName;
	private Button mShowPhoto;
	
	private CheckBox  mFileShareCb;
	private TextView mFileShareTv;
	private TextView mPasswordTv;
	private Button mPasswordBut;
	private LinearLayout mLtConnPhone;
	private LinearLayout isFileShareOnLayout;
	private SharedPreferences mSharedPrefService;
	
	
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			mTVService = binder.getTVService();
			isFileShareOnLayout.setVisibility(View.VISIBLE);
			mFileShareTv.setText("文件共享已开启，IP地址：" + CommonUtil.getServerIpAddress(mTVService));
			mTVService.setHandler(handler);
			Log.d(TAG, "service connection mTVService = " + mTVService);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mTVService = null;
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSharedPrefService = this.getSharedPreferences(TVService.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		mShowPhoto = (Button) findViewById(R.id.show_photo);
		mShowPhoto.setOnClickListener(this);
		isFileShareOnLayout = (LinearLayout) this.findViewById(R.id.ll_file_share_ison);
		mFileShareCb = (CheckBox) this.findViewById(R.id.cb_file_share);
		mFileShareTv = (TextView) this.findViewById(R.id.tv_file_share);
		mPasswordTv = (TextView) this.findViewById(R.id.tv_password);
		mPasswordBut = (Button) this.findViewById(R.id.but_reset_password);
		mPasswordBut.setFocusable(true);
		mLtConnPhone = (LinearLayout) this.findViewById(R.id.lt_conn_phone);
		mFileShareCb.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		mFileShareCb.setChecked(true);
		mPasswordBut.setOnClickListener(new PasswordOnClickListenerImpl());
		genRandomNumber();
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LoginUtil.MSG_SUCCESS:
//				mLtConnPhone.setVisibility(View.VISIBLE);
//				TextView tvPhone = new TextView(MainActivity.this);
//				tvPhone.setText((String)msg.obj);
//				mLtConnPhone.addView(tvPhone);
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
//                + Environment.getExternalStorageDirectory())));
	}
	
	public void scanSdCard(){
        IntentFilter intentfilter = new IntentFilter( Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentfilter.addDataScheme("file");
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
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
//		unBindService();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.open_echoii:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);          
			ComponentName cn = new ComponentName("com.echoii.activity", "com.echoii.activity.LoginActivity");          
			intent.setComponent(cn);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void unBindService() {
		if (serviceName != null) {
			this.stopService(serviceIntent);
		}
		if (mTVService != null) {
			mTVService.stopForeground(false);
			this.unbindService(conn);
		}
	}
	
	

	private void bindService() {
		serviceIntent = new Intent(this, TVService.class);
		serviceName = startService(serviceIntent);
		bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
	}
	
	public static class AutoBootReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
				intent = new Intent(context, TVService.class);
				serviceName = context.startService(intent);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_photo:
			Intent intent = new Intent(this,ShowPhotoActivity.class);
			this.startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	public class OnCheckedChangeListenerImpl implements OnCheckedChangeListener{
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				bindService();
				mFileShareCb.setText(MainActivity.this.getResources().getString(R.string.file_share_on));
			}else{
				isFileShareOnLayout.setVisibility(View.GONE);
				unBindService();
				mFileShareCb.setText(MainActivity.this.getResources().getString(R.string.file_share_off));
			}
		}
	}
	
	public class PasswordOnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			genRandomNumber();
		}
	}

	private void genRandomNumber(){
		Random random = new Random();
		int randomNum = random.nextInt(1000000); 
		if ( randomNum >= 100000 && randomNum < 1000000) {
			mPasswordTv.setText(String.valueOf(randomNum));
			mSharedPrefService.edit().putString(PASSWORD, String.valueOf(randomNum)).commit();
		}else{
			genRandomNumber();
		}
	}
}
