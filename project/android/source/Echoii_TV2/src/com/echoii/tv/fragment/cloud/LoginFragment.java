package com.echoii.tv.fragment.cloud;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.echoii.tv.HttpHelper;
import com.echoii.tv.R;
import com.echoii.tv.constants.Constants;
import com.echoii.tv.model.User;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>云端登录界面</p>
 *
 */
public class LoginFragment extends Fragment {
	
	public static final String TAG = "LoginFragment";
	
	private String username;
	private String password;
	
	private HttpHelper helper;
	private User cloudUser;
	private OnCloudLoginListener listener;
	
	private EditText mEdtxtUsername;
	private EditText mEdtxtPassword;
	private Button mButLogin;
	
	public static LoginFragment getInstance() {
		return new LoginFragment();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(TAG, "LoginFragment onAttach");
		try {
			listener = (OnCloudLoginListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "LoginFragment onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG, "LoginFragment onCreateView");
		View view = inflater.inflate(R.layout.fragment_cloud_login, null);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		helper = new HttpHelper();
		cloudUser = new User();
		
		mEdtxtUsername = (EditText) view.findViewById(R.id.cloud_username);
		mEdtxtPassword = (EditText) view.findViewById(R.id.cloud_password);
		
		mButLogin = (Button) view.findViewById(R.id.cloud_login);
		mButLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				username = mEdtxtUsername.getText().toString();
				password = mEdtxtPassword.getText().toString();
				
				cloudUser.setUsername(username);
				cloudUser.setPassword(password);
				new CloudLoginAsyncTask().execute();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "LoginFragment onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG, "LoginFragment onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "LoginFragment onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "LoginFragment onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG, "LoginFragment onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "LoginFragment onStop");
	}
	
	public class CloudLoginAsyncTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			String loginResponse = null;
			try {
				
				loginResponse = helper.cloudLogin(cloudUser);
				
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return loginResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result.equals(Constants.AUTH_ERROR)) {
				listener.onCloudLoginError();
			} else {
				listener.onCloudLoginSuccess();
			}
		}
		
	}
	
	public interface OnCloudLoginListener {
		public void onCloudLoginSuccess();
		public void onCloudLoginError();
	}

}
