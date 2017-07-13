package com.echoii.tv.fragment.cloud;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echoii.tv.R;
import com.echoii.tv.constants.Constants;
import com.echoii.tv.fragment.MessageFragment;
import com.echoii.tv.model.User;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>云端文件浏览器界面</p>
 *
 */
public class CloudFragment extends Fragment {
	
	public static final String TAG = "CloudFragment";
	
	private int position = 0;
	
	private TopCloudActionBarFragment mTopFragment;
	private ListCloudFileFragment mListFragment;
	private BottomCloudActionBarFragment mBottomFragment;
	private MessageFragment mMessageFragment;
	private User user;
	
	public static CloudFragment getInstance() {
		return new CloudFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(TAG, "cloud fragment onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "cloud fragment onCreate");
		Bundle bundle = getArguments();
		if (bundle == null) {
			position = 0;
		} else {
			position = bundle.getInt(Constants.KEY_CLOUD_CATEGORY_POSITION);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG, "cloud fragment onCreateView");
		View view = inflater.inflate(R.layout.fragment_cloud, null);
		initFragments();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "cloud fragment onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG, "cloud fragment onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "cloud fragment onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "cloud fragment onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG, "cloud fragment onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "cloud fragment onStop");
	}
	
	private void initFragments() {
		
		mTopFragment = TopCloudActionBarFragment.getInstance();
		FragmentTransaction topTran = getChildFragmentManager().beginTransaction();
		topTran.replace(R.id.top, mTopFragment, TopCloudActionBarFragment.TAG).commit();
		
		mListFragment = ListCloudFileFragment.getInstance();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.KEY_CLOUD_CATEGORY_POSITION, position);
		mListFragment.setArguments(bundle);
		FragmentTransaction listTran = getChildFragmentManager().beginTransaction();
		listTran.replace(R.id.list, mListFragment, ListCloudFileFragment.TAG).commit();
		
		mBottomFragment = BottomCloudActionBarFragment.getInstance();
		FragmentTransaction bottomTran = getChildFragmentManager().beginTransaction();
		bottomTran.replace(R.id.bottom, mBottomFragment, BottomCloudActionBarFragment.TAG).commit();
		
	}

}
