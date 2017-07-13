package com.echoii.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.bean.login.LoginResponseData;
import com.echoii.constant.CommonUtil;
import com.echoii.constant.MessageInfo;
import com.echoii.network.http.LoginHttp;

public class LoginActivity extends Activity {
	public static final String TAG = "LoginActivity";
	public static final String USER_INFO = "user_info";
	public static final String USER_ACCOUNT = "user_account";
	public static final String PASSWORD = "password";
	public static boolean LOGIN_FLAG = false;
	private EditText mEditAccount;
	private EditText mEditPassWord;
	private Button mBtnLogin;
	private TextView mTvewRegister;
	private TextView mTvewForgetPassword;
	private TextView mTvewNormalCloud;
	private TextView mTvewDatacenter;
	private ProgressBar mProgress;
	private String username;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		Log.d(TAG,"Login onCreate");

		mEditAccount = (EditText) findViewById(R.id.edit_account);
		mEditPassWord = (EditText) findViewById(R.id.edit_input_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mTvewRegister = (TextView) findViewById(R.id.tvew_noaccount_register);
		mTvewForgetPassword = (TextView) findViewById(R.id.tvew_forget_password);
		mTvewNormalCloud = (TextView) findViewById(R.id.tvew_login_normal);
		mTvewDatacenter = (TextView) findViewById(R.id.tvew_login_datacenter);
		mProgress = (ProgressBar)findViewById(R.id.login_progress);
		mProgress.setVisibility(View.GONE);
		initView();
		
		CommonUtil.largeClickRange(mTvewRegister);
		CommonUtil.largeClickRange(mTvewForgetPassword);
		
		mBtnLogin.setOnClickListener(clickListener);
		mTvewRegister.setOnClickListener(clickListener);
		mTvewForgetPassword.setOnClickListener(clickListener);
		mTvewNormalCloud.setOnClickListener(clickListener);
		mTvewDatacenter.setOnClickListener(clickListener);
		Log.d(TAG,"btn_login");

	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"OnResume");
	}
	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.tvew_login_normal: {
				initTwoLoginWay();
				break;
			}
			case R.id.tvew_login_datacenter: {
//				mTvewDatacenter.setTextColor(getResources().getColor(
//						R.color.fonts_operate_selected));
//				mTvewNormalCloud.setTextColor(Color.BLACK);
				LOGIN_FLAG = true;
				Intent intent = new Intent(LoginActivity.this,MainCloudActivity.class);
				LoginActivity.this.startActivity(intent);
				break;
			}
			case R.id.btn_login: {
//				Intent i = new Intent();
//				i.setClass(getApplicationContext(), MainCloudActivity.class);
//				startActivity(i);
				Log.d(TAG,"btn_login");
				login();
				break;
			}
			case R.id.tvew_forget_password: {
				Intent i = new Intent();
				i.setClass(getApplicationContext(),
						ForgotPassWordActivity.class);
				startActivity(i);
				break;
			}
			case R.id.tvew_noaccount_register: {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
				break;
			}

			default:
				break;
			}
		}

	};

	private void initTwoLoginWay() {
		mTvewNormalCloud.setTextColor(getResources().getColor(
				R.color.fonts_operate_selected));
		mTvewDatacenter.setTextColor(Color.BLACK);
	}

	/**
	 * 从数据库列表中获取数据填入账号密码处
	 */
	private void initView() {
		Log.d(TAG,"initView");
		initTwoLoginWay();
		SharedPreferences userInfo = getSharedPreferences(USER_INFO, 0);
		String username = userInfo.getString(USER_ACCOUNT, "");
		String password = userInfo.getString(PASSWORD, "");
		if (null != username) {
			mEditAccount.setText(username);
		} else if (null != password) {
			mEditPassWord.setText(password);
		}
	}

	/**
	 * login
	 * <p>
	 * Description: 登陆
	 * <p>
	 * 
	 * @date 2013-10-23
	 */
	private void login() {
		String strAccount = mEditAccount.getEditableText().toString().trim();
		String strPassword = mEditPassWord.getEditableText().toString().trim();
		this.username = strAccount;
		this.password = strPassword;
		Log.d(TAG,"Login");

		if (TextUtils.isEmpty(strAccount)) {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.toast_name_null), Toast.LENGTH_SHORT)
					.show();
		} else if (TextUtils.isEmpty(strPassword)) {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.toast_password_null), Toast.LENGTH_SHORT)
					.show();
		} else {
			Log.d(TAG, "next is Http");
			
			HashMap<String,String> hash = new HashMap<String,String>();
			
			Log.d("mating", "strAccount = " + strAccount);
			Log.d("mating", "strPassword = " + strPassword);
			
			hash.put("email", strAccount);
			hash.put("password", strPassword);
			LoginHttp loginHttp = new LoginHttp(handler,hash);
			loginHttp.loginRequest();
			mProgress.setVisibility(View.VISIBLE);
		}
	}

    private List<LoginResponseData> mLoginRspDataList = new ArrayList<LoginResponseData>();
    
    

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MessageInfo.LOGIN_RESPONSE) {
				mProgress.setVisibility(View.GONE);
				switch (msg.arg1) {
				case MessageInfo.MESSAGE_SUCCESS: {
					
					mLoginRspDataList = (List<LoginResponseData>)msg.obj;
					// 用户等待结束
					String token = mLoginRspDataList.get(0).getData().getToken();
					String id = mLoginRspDataList.get(0).getData().getId();
					Log.d("mating","login------------token = " + token + "; id = " + id);
					SharedPreferences idToken = getSharedPreferences(CommonUtil.NEED_TOKENID, 0);
					SharedPreferences.Editor editor = idToken.edit();
					editor.putString(CommonUtil.USER_ID, id);
					editor.putString(CommonUtil.USER_TOKEN, token);
					editor.commit();
					SharedPreferences userInfo = getSharedPreferences(USER_INFO, 0);
					SharedPreferences.Editor editor1 = userInfo.edit();
					editor1.putString(USER_ACCOUNT, username);
					editor1.putString(PASSWORD, password);
					editor1.commit();
					
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainCloudActivity.class);
					startActivity(i);
					break;
				}
				case MessageInfo.RETURN_CODE_PARAMS_WRONG:
				{
					showToast("参数错误！");
					break;
				}
				case MessageInfo.RETURN_CODE_TOKEN_WRONG:
				{
					showToast("认证错误。。请检查用户名或密码是否正确？");
					break;
				}
				case MessageInfo.RETURN_CODE_SYSTEM_WRONG:
				{
					showToast("系统错误！");
					break;
				}
				case MessageInfo.MESSAGE_FAIL:
				{
					showToast("对不起，连接异常，请谅解。。");
					break;
				}
				case MessageInfo.RETURN_RESPONSE_FAIL:
				{
					showToast("对不起，请检查wifi或数据连接是否正常。。");
					break;
				}
				default:
					break;
				}
			}
		}
	};

	private void showToast(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
	

}
