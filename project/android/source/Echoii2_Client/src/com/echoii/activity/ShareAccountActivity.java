package com.echoii.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShareAccountActivity extends Activity{
	private TextView mTvewBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_account);
		
		mTvewBack = (TextView)findViewById(R.id.back);
		
		mTvewBack.setOnClickListener(shareAccountOnClickListener);
	}
	
	private OnClickListener shareAccountOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View v) {
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
