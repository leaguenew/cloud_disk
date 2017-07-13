package com.echoii.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LeftFragment extends Fragment{

	/**软件密码锁*/
	private TextView mTvewSoftLock;
	/**分享账号*/
	private TextView mTvewShareAccount; 
	/**退出登录*/
	private TextView mTvewExit;
	/**仅在wifi上传/下载*/
	private ToggleButton mWifiToggleBtn;
	/**相册自动备份*/
	private ToggleButton mBackupToggleBtn;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		View view = inflater.inflate(R.layout.main_left, null);
		mTvewSoftLock = (TextView)view.findViewById(R.id.left_tvew_softlock);
		mTvewShareAccount = (TextView)view.findViewById(R.id.left_tvew_share_accoutbinding);
		mTvewExit = (TextView)view.findViewById(R.id.left_tvew_exit);
		
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		mTvewSoftLock.setOnClickListener(leftTvewOnClickListener);
		mTvewShareAccount.setOnClickListener(leftTvewOnClickListener);
		mTvewExit.setOnClickListener(leftTvewOnClickListener);
	}
	
	private OnClickListener leftTvewOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			// TODO Auto-generated method stub
			switch (v.getId())
			{
				
				case R.id.left_tvew_softlock:
				{
					intent.setClass(getActivity(), LockSettingActivity.class);
					startActivity(intent);
					break;
				}
				case R.id.left_tvew_share_accoutbinding:
				{
					intent.setClass(getActivity(), ShareAccountActivity.class);	
					startActivity(intent);
					break;
				}
				case R.id.left_tvew_exit:
				{
					getActivity().finish();
				}
				default:
					break;
			}			
		}		
	};
}
