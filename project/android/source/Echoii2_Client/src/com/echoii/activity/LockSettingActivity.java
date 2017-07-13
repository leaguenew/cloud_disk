package com.echoii.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LockSettingActivity extends Activity{
	
	private TextView mTvewBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locksetting);
		
		mTvewBack = (TextView)findViewById(R.id.back);
		
		mTvewBack.setOnClickListener(lockSettingOnClickListener);
	}
	
	private OnClickListener lockSettingOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId())
			{
			case R.id.back:
			{
				finish();
			}
			}
		}
		
	};
}
