package com.echoii.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.echoii.activity.fragment.CloudFileListFragment;
import com.echoii.activity.fragment.DataCenterConnectionFragment;
import com.echoii.activity.fragment.DataCenterListFragment;
import com.echoii.activity.fragment.DataCenterLoginFragment;
import com.echoii.activity.fragment.DataMarketHomeFragment;
import com.echoii.activity.fragment.DataMarketListFragment;
import com.echoii.activity.fragment.OnFragmentChangeListener;
import com.echoii.activity.fragment.PhoneFileListFragment;
import com.echoii.constant.CommonUtil;

public class CenterFragment2 extends Fragment implements OnFragmentChangeListener,OnCheckedChangeListener
{
	public static final String TAG = "CenterFragment2";
	public static final int DATA_CENTER_FRAGMENT = 0x001;
	public static final int DATA_CENTER_LIST_FRAGMENT = 0x010;
	public static final int DATA_CENTER_LOGIN_FRAGMENT = 0x011;
	public static final int DATA_CENTER_CONN_FRAGMENT = 0x012;
	public static final int CLOUD_LIST_ALLFILE_FRAGMENT  = 0x100;
	public static final int CLOUD_FRAGMENT = 0x002;
	public static final int PHONE_FRAGMENT = 0x003;
	public static final int PHONE_LIST_FRAGMENT = 0x004;
	public static int TAB_ID = 0; //表示当前选择是哪一个tab
	/**数据市场*/
	public static final int DATA_MARKET_HOME = 0x005;
	
	public static final int DATA_MARKET_LIST_FRAAGMENT = 0x006;
	
	public static final String SESSION_STATUS = "session_status";  //保存session信息sharepreferences的名称
	public static final String SESSION_TAB_CHANGE_TAG = "tab_change";  //保存tab状态的 key
	
	public static final String SESSION_CLOUD_PATH = "current_cloud_path"; //保存云端当前请求路径的key
	public static final String SESSION_CLOUD_PARENT_PATH = "parent_cloud_path"; //保存云端父请求路径的key
	public static final String SESSION_CLOUD_FILE_TPYE = "file_type_cloud"; //云端当前访问的是哪个路径下的
	
	public static final String SESSION_DATA_CENTER_PATH = "current_data_center_path"; //保存当前数据中心请求路径的key
	public static final String SESSION_DATA_CENTER_PARENT_PATH = "parent_data_center_path"; //保存当前数据中心请求路径的key
	public static final String SESSION_DATA_CENTER_FILE_TYPE = "file_type_data_center";
	
	public static final String SESSION_PHONE_PATH = "current_phone_path"; //保存当前手机请求路径的key
	public static final String SESSION_PHONE_PARENT_PATH = "parent_phone_path"; //保存当前手机请求路径的key
	public static final String SESSION_PHONE_FILE_TYPE = "file_type_phone";
	
    private TabHost mTabHost;
    private Activity mActivity;
    
    public SharedPreferences sessionStatusPreference; //保存会话信息，包括tab切换信息，请求路径等
    public SharedPreferences ipPreference;
    public SharedPreferences idToken;
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tabhost, null);
        
        mTabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        mTabHost.setup();
        initTabHost();
        initDefaultTabView();
        Log.d(TAG, "Center onCreateView");
        return view;
    }
    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		sessionStatusPreference = activity.getSharedPreferences(SESSION_STATUS, Context.MODE_PRIVATE);
    	ipPreference = activity.getSharedPreferences(DataCenterConnectionFragment.IP_SHARED_NAME, Context.MODE_PRIVATE);
    	idToken = getActivity().getSharedPreferences(CommonUtil.NEED_TOKENID,0);
		Log.d(TAG, "Center onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Center onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Center onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "Center onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "Center onPause");
	}

	@Override
	public void onResume() {
		super.onResume();

		Log.d(TAG, "Center onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "Center onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "Center onStop");
	}

	private void initDefaultTabView() {
		if (LoginActivity.LOGIN_FLAG) {
			mTabHost.setCurrentTab(1);
			Fragment dataConnFragment = new DataCenterConnectionFragment(CenterFragment2.this);
            FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
            tran.replace(android.R.id.tabcontent, dataConnFragment, "dataConnFragment");
            tran.commit();
	        sessionStatusPreference.edit().putString(SESSION_TAB_CHANGE_TAG, "data").commit();
		}else{
			mTabHost.setCurrentTab(0);
	        Fragment cloudFragment = new CloudFragment(this);
	        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
	        tran.replace(android.R.id.tabcontent, cloudFragment, "cloudFragment");
	        tran.commit();
	        sessionStatusPreference.edit().putString(SESSION_TAB_CHANGE_TAG, "cloud").commit();
		}
    }
    
    
    private void initTabHost()
    {
        RelativeLayout cloudIndicator = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator,null);
        ImageView cloudLineImage = (ImageView) cloudIndicator.findViewById(R.id.tab_indicator_line);
        CheckBox cloudImage = (CheckBox) cloudIndicator.findViewById(R.id.tab_indicator_icon);
        
        cloudIndicator.setBackgroundResource(R.drawable.cloud_back_style);
        cloudLineImage.setImageResource(R.drawable.cloud_line_style);
        cloudImage.setButtonDrawable(R.drawable.tab_indicator_cloud);
        cloudImage.setText(mActivity.getResources().getString(R.string.main_cloud));
        cloudImage.setOnCheckedChangeListener(this);
        cloudImage.setTag("0");
        
        RelativeLayout dataIndicator = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator,null);
        ImageView dataLineImage = (ImageView) dataIndicator.findViewById(R.id.tab_indicator_line);
        CheckBox dataImage = (CheckBox) dataIndicator.findViewById(R.id.tab_indicator_icon);
        
        dataIndicator.setBackgroundResource(R.drawable.cloud_back_style);
        dataLineImage.setImageResource(R.drawable.cloud_line_style);
        dataImage.setButtonDrawable(R.drawable.tab_indicator_data_center);
        dataImage.setText(mActivity.getResources().getString(R.string.main_datacenter));
        dataImage.setOnCheckedChangeListener(this);
        dataImage.setTag("1");
        
        RelativeLayout phoneIndicator = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.tab_indicator,null);
        ImageView phoneLineImage = (ImageView) phoneIndicator.findViewById(R.id.tab_indicator_line);
        CheckBox phoneImage = (CheckBox) phoneIndicator.findViewById(R.id.tab_indicator_icon);
        
        phoneIndicator.setBackgroundResource(R.drawable.cloud_back_style);
        phoneLineImage.setImageResource(R.drawable.cloud_line_style);
        phoneImage.setButtonDrawable(R.drawable.tab_indicator_phone);
        phoneImage.setText(mActivity.getResources().getString(R.string.main_phone));
        phoneImage.setOnCheckedChangeListener(this);
        phoneImage.setTag("2");
        
        TabSpec cloudTab = mTabHost.newTabSpec("cloud");
        cloudTab.setIndicator(cloudIndicator);
        cloudTab.setContent(new TabHostContentFactory(getActivity()));
        mTabHost.addTab(cloudTab);
        
        TabSpec dataTab = mTabHost.newTabSpec("data");
        dataTab.setIndicator(dataIndicator);
        dataTab.setContent(new TabHostContentFactory(getActivity()));
        mTabHost.addTab(dataTab);
        
        TabSpec phoneTab = mTabHost.newTabSpec("phone");
        phoneTab.setIndicator(phoneIndicator);
        phoneTab.setContent(new TabHostContentFactory(getActivity()));
        mTabHost.addTab(phoneTab);
        
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId)
            {
                if (tabId.equals("cloud")) {
                	if (LoginActivity.LOGIN_FLAG) {
                		Fragment cloudNoLoginFragment = new CloudNoLoginFragment();
                        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
                        tran.replace(android.R.id.tabcontent, cloudNoLoginFragment, "cloudNoLoginFragment");
                        tran.commit();
					}else{
						String cloudPath = sessionStatusPreference.getString(SESSION_CLOUD_PATH, "");
	                	int itemId = sessionStatusPreference.getInt(CenterFragment2.SESSION_CLOUD_FILE_TPYE, 0);
	                	Log.d("mating","changeTab   cloud cloudPath = " + cloudPath);
	                	
	                	if (cloudPath.equals("CloudFragment"))
	                	{
	                        Fragment cloudFragment = new CloudFragment(CenterFragment2.this);
	                        FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
	                        tran.replace(android.R.id.tabcontent, cloudFragment, "cloudFragment");
	                        tran.commit();
	                	}else if (cloudPath.equals("DataMarketHomeFragment")){
	            			startDataMarketHomeFragment();
	                	}else if (cloudPath.equals("DataMarketListFragment")){
	            			startDataMarketListFragment(itemId);
	                	}
	                	else 
	                	{
	                		CloudFileListFragment cloudListFragment = new CloudFileListFragment();
	                		FragmentTransaction tran1 = getActivity().getSupportFragmentManager().beginTransaction();
	                		tran1.replace(android.R.id.tabcontent, cloudListFragment, "cloudListFragment");
	                		tran1.commit();
	                		String id = idToken.getString(CommonUtil.USER_ID,"");
	            			String token = idToken.getString(CommonUtil.USER_TOKEN, "");
	            			
		                   	if ("nullcontent".equals((cloudPath)))                		 
		                   	{
		                   		cloudListFragment.getAllFileData(id,token,itemId,0,40,"",null);
		                   	}
		                   	else
		                   	{
		                   		cloudListFragment.getAllFileData(id,token,itemId,0,40,cloudPath,null);
		                   	}	                   		
	                	} 
					}
                	TAB_ID = 0;
                	 sessionStatusPreference.edit().putString(SESSION_TAB_CHANGE_TAG, "cloud").commit();
                }else if (tabId.equals("data")) {
                	String dataCenterPath = sessionStatusPreference.getString(SESSION_DATA_CENTER_PATH, "null");
                	String ip = ipPreference.getString(DataCenterConnectionFragment.CURRENT_CONN_IP, "null");
                	int itemId = sessionStatusPreference.getInt(SESSION_DATA_CENTER_FILE_TYPE, 0);
                	boolean conn = sessionStatusPreference.getBoolean(DataCenterConnectionFragment.DATA_CENTER_AUTO_CONN, false);
                	boolean loginStatus = sessionStatusPreference.getBoolean(DataCenterLoginFragment.DATA_CENTER_LOGIN_SUCCESS, false);
                	Log.d(TAG, "conn = " + conn + " / login status = " + loginStatus );
                	if (conn && loginStatus) {
                		if (dataCenterPath.equals("DataCenterFragment")) {
                    		startDcFragment();
    					}else if (dataCenterPath.startsWith("/")) {
    				        if (dataCenterPath.equals(CommonUtil.SDCARD_ROOT_PATH)) {
    				        	startDcListFragment(itemId, DataCenterListFragment.FILE_ROOT_PATH);
    						}else{
    							startDcListFragment(itemId, DataCenterListFragment.FILE_CHILD_PATH);
    						}
    					}else if (dataCenterPath.equals("DataMarketHomeFragment")){
                			startDataMarketHomeFragment();
                    	}else if (dataCenterPath.equals("DataMarketListFragment")){
                			startDataMarketListFragment(itemId);
                    	}else{
                    		startDcFragment();
    					}
					}else{
						if (dataCenterPath.equals("DataCenterFragment")) {
	                		startDcFragment();
						}else if (dataCenterPath.startsWith("/")) {
					        if (dataCenterPath.equals(CommonUtil.SDCARD_ROOT_PATH)) {
					        	startDcListFragment(itemId, DataCenterListFragment.FILE_ROOT_PATH);
							}else{
								startDcListFragment(itemId, DataCenterListFragment.FILE_CHILD_PATH);
							}
						}else if (dataCenterPath.equals("DataMarketHomeFragment")){
	            			startDataMarketHomeFragment();
	                	}else if (dataCenterPath.equals("DataMarketListFragment")){
	            			startDataMarketListFragment(itemId);
	                	}else if (dataCenterPath.equals("DataCenterConnectionFragment")) {
	                		startDcConnFragment();
						}else{
							startDcConnFragment();
						}
					}
                	TAB_ID = 1;
                	sessionStatusPreference.edit().putString(SESSION_TAB_CHANGE_TAG, "data").commit();
                }else if (tabId.equals("phone")) {
                	String phonePath = sessionStatusPreference.getString(SESSION_PHONE_PATH, "null");
                	int itemId = sessionStatusPreference.getInt(SESSION_PHONE_FILE_TYPE, 0);
                	Log.d("mating","changeTab   phone phonePath = " + phonePath);
                	if (phonePath.equals("PhoneFragment"))
                	{
	                    Fragment phoneFragment = new PhoneFragment(CenterFragment2.this);
	                    FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
	                    tran.replace(android.R.id.tabcontent, phoneFragment, "phoneFragment");
	                    tran.commit();
                	}
                	else if (phonePath.equals("DataMarketHomeFragment"))
                	{
            			startDataMarketHomeFragment();
                	}
                	else if (phonePath.equals("DataMarketListFragment"))
                	{
            			startDataMarketListFragment(itemId);
                	}
                	else if(phonePath.startsWith("/"))
                	{
                		PhoneFileListFragment phoneListFragment = new PhoneFileListFragment(itemId,phonePath);
                		FragmentTransaction tran1 = getActivity().getSupportFragmentManager().beginTransaction();
                		tran1.replace(android.R.id.tabcontent, phoneListFragment, "phoneListFragment");
                		tran1.commit();
                		//phoneListFragment.getFileListData(phonePath,itemId,mActivity);
                	}else if(phonePath.equals("null")){
                		Fragment phoneFragment = new PhoneFragment(CenterFragment2.this);
	                    FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
	                    tran.replace(android.R.id.tabcontent, phoneFragment, "phoneFragment");
	                    tran.commit();
                	}
                	TAB_ID = 2;
                    sessionStatusPreference.edit().putString(SESSION_TAB_CHANGE_TAG, "phone").commit();
                }
            }
        });
    }

	@Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Center onActivityCreated");
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

	@Override
	public void onFragmentChange(int page , int itemId) {
		switch (page) {
		case DATA_CENTER_FRAGMENT:
			startDcFragment();
			break;
		case DATA_CENTER_LIST_FRAGMENT:
			startDcListFragment(itemId, DataCenterListFragment.FILE_ROOT_PATH);
			break;
		case DATA_CENTER_CONN_FRAGMENT:
			startDcConnFragment();
			break;
		case DATA_CENTER_LOGIN_FRAGMENT:
			startDcLoginFragment();
			break;
		case CLOUD_FRAGMENT:
			Fragment cFragment = new CloudFragment(this);
	        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
	        tr.replace(android.R.id.tabcontent, cFragment, "cloudFragment");
	        tr.commit();
	        break;
		case CLOUD_LIST_ALLFILE_FRAGMENT:
			CloudFileListFragment cloudListFragment = new CloudFileListFragment();
			FragmentTransaction tran2 = getActivity().getSupportFragmentManager().beginTransaction();
			tran2.replace(android.R.id.tabcontent, cloudListFragment, "cloudListFragment");
			tran2.commit();
			String id = idToken.getString(CommonUtil.USER_ID,"");
			String token = idToken.getString(CommonUtil.USER_TOKEN, "");
			cloudListFragment.getAllFileData(id,token,itemId,0,CloudFileListFragment.PER_SIZE,"","CloudFragment");
			break;
		case PHONE_FRAGMENT:
            Fragment phoneFragment = new PhoneFragment(CenterFragment2.this);
            FragmentTransaction trn = getActivity().getSupportFragmentManager().beginTransaction();
            trn.replace(android.R.id.tabcontent, phoneFragment, "phoneFragment");
            trn.commit();
			 break;
		case PHONE_LIST_FRAGMENT:
			PhoneFileListFragment phoneListFragment = new PhoneFileListFragment(itemId,"/mnt/sdcard");
			FragmentTransaction tran3 = getActivity().getSupportFragmentManager().beginTransaction();
			tran3.replace(android.R.id.tabcontent, phoneListFragment, "phoneListFragment");
			tran3.commit();
//			phoneListFragment.getFileListData("/mnt/sdcard",itemId,mActivity);
			break;
		case DATA_MARKET_HOME:
			startDataMarketHomeFragment();
			break;
		case DATA_MARKET_LIST_FRAAGMENT:
			Log.d("zhangzhang","itemId = " + itemId);
			startDataMarketListFragment(itemId);
			break;
		default:
			break;
		}
	}

	private void startDataMarketListFragment(int itemId) {
		Fragment listmarketFragment = new DataMarketListFragment(itemId);
		FragmentTransaction listMarket = getActivity().getSupportFragmentManager().beginTransaction();
		listMarket.replace(android.R.id.tabcontent, listmarketFragment, "listmarketFragment");
		listMarket.commit();
	}

    private void startDcListFragment(int itemId, int resultPath) {
		DataCenterListFragment listFragment = new DataCenterListFragment(itemId,resultPath,this);
		FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
		tran.replace(android.R.id.tabcontent, listFragment, "datalistFragment");
		tran.commit();
	}
    
    private void startDcFragment() {
		Fragment dataFragment = new DataCenterFragment(CenterFragment2.this);
		FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
		tran.replace(android.R.id.tabcontent, dataFragment, "dataFragment");
		tran.commit();
	}
	
	private void startDcConnFragment() {
		DataCenterConnectionFragment dcConnFragment = new DataCenterConnectionFragment(this);
		FragmentTransaction dcConnTran = getActivity().getSupportFragmentManager().beginTransaction();
		dcConnTran.replace(android.R.id.tabcontent, dcConnFragment, "dcConnFragment");
		dcConnTran.commit();
	}

	private void startDataMarketHomeFragment() {
		Fragment marketFragment = new DataMarketHomeFragment(CenterFragment2.this);
		FragmentTransaction tranmarket = getActivity().getSupportFragmentManager().beginTransaction();
		tranmarket.replace(android.R.id.tabcontent, marketFragment, "marketFragment");
		tranmarket.commit();
	}
	
    private void startDcLoginFragment() {
    	DataCenterLoginFragment dcLoginFragment = new DataCenterLoginFragment(this);
        FragmentTransaction dcLoginTran = getActivity().getSupportFragmentManager().beginTransaction();
        dcLoginTran.replace(android.R.id.tabcontent, dcLoginFragment, "dcLoginFragment");
        dcLoginTran.commit();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (Integer.parseInt((String) buttonView.getTag())) {
		case 0:
				mTabHost.setCurrentTab(0);
			break;
		case 1:
				mTabHost.setCurrentTab(1);
			break;
		case 2:
				mTabHost.setCurrentTab(2);
			break;

		default:
			break;
		}
	}

}

