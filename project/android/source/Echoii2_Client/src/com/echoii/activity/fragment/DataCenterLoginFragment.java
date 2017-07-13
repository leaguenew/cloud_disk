package com.echoii.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.echoii.activity.CenterFragment2;
import com.echoii.activity.DataCenterFragment;
import com.echoii.activity.R;
import com.echoii.constant.CommonUtil;
import com.echoii.network.socket.DataCenterSocket;
import com.echoii.thread.DCenterLoginRequestThread;

public class DataCenterLoginFragment extends Fragment {
	public static final String TAG = "DataCenterLoginFragment";
	public static final String DATA_CENTER_LOGIN_SUCCESS = "login_success";

	private Activity mActivity;
	private OnFragmentChangeListener mListener;
	private EditText mEtPassword;
	private Button mButLogin;
	private ProgressBar mPbLogin;
	private SharedPreferences mSharedPrefIpAddress;
	private SharedPreferences sessionStatusPreference;
	
	
	public DataCenterLoginFragment(OnFragmentChangeListener mListener) {
		super();
		this.mListener = mListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_datacenter_login, null);
		mEtPassword = (EditText) view.findViewById(R.id.et_password);
		mButLogin = (Button) view.findViewById(R.id.but_dc_login);
		mPbLogin = (ProgressBar) view.findViewById(R.id.pb_dc_login_progress);
		mPbLogin.setVisibility(View.GONE);
		mButLogin.setOnClickListener(new OnClickListenerImpl());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mSharedPrefIpAddress = mActivity.getSharedPreferences(DataCenterConnectionFragment.IP_SHARED_NAME, Context.MODE_PRIVATE);
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}
	
	public class OnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			String serverIp = mSharedPrefIpAddress.getString(DataCenterConnectionFragment.CURRENT_CONN_IP, "null");
			String password = mEtPassword.getText().toString();
			mPbLogin.setVisibility(View.VISIBLE);
			new DCenterLoginRequestThread(handler, mActivity,serverIp, password, CommonUtil.getClientIpAddress(mActivity), DataCenterSocket.REQUEST_CODE_LOGIN).start();
		}
	}
	
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Log.d(TAG, "login fragment msg.what = " + msg.what);
			switch (msg.what) {
			case DCenterLoginRequestThread.SUCCESS:
				mPbLogin.setVisibility(View.GONE);
				InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
				Fragment dataFragment = new DataCenterFragment(mListener);
    	        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
    	        tran.replace(android.R.id.tabcontent, dataFragment, "dataFragment");
    	        tran.commit();
    	        sessionStatusPreference.edit().putBoolean(DATA_CENTER_LOGIN_SUCCESS, true).commit();
				break;
			case DCenterLoginRequestThread.ERROR :
				mPbLogin.setVisibility(View.GONE);
				Toast.makeText(mActivity, "数据中心登录失败，请重试...", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
		
	};

}
