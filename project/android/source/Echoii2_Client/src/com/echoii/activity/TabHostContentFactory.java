package com.echoii.activity;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;


public class TabHostContentFactory implements TabContentFactory{
	
	Context mContext;
	public TabHostContentFactory(Context context){
		this.mContext = context;
	}

	@Override
	public View createTabContent(String tag) {
		View v = new View(mContext);
		return v;
	}

}
