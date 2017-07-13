package com.echoii.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CloudNoLoginFragment extends Fragment {
	public static final String TAG = "CloudNoLoginFragment";

	private Activity mActivity;
	private Button mButLogin;
	private TextView mTvExit;
	private ImageView mImgviewSettings;
	
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cloud_nologin, null);
		mButLogin = (Button) view.findViewById(R.id.login);
		mButLogin.setOnClickListener(new OnClickListenerImpl());
		mImgviewSettings = (ImageView) view.findViewById(R.id.setting);
		mImgviewSettings.setOnClickListener(new OnClickListenerImpl());
		mTvExit = (TextView) view.findViewById(R.id.exit);
		mTvExit.setOnClickListener(new OnClickListenerImpl());
		return view;
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
			switch (v.getId()) {
			case R.id.login:
				LoginActivity.LOGIN_FLAG = false;
				getActivity().finish();
				break;
			case R.id.setting:
				((MainCloudActivity)getActivity()).toggle();
				break;
			case R.id.exit:
				mActivity.finish();
				break;
			default:
				break;
			}
			
		}
	}

}
