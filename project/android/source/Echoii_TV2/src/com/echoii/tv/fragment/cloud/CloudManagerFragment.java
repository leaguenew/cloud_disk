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
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>关于各种云端操作的管理者，负责切换按需切换不同的界面显示</p>
 *
 */
public class CloudManagerFragment extends Fragment {
	
	public static final String TAG = "CloudManagerFragment";
	
	private int position = 0;
	
	private LoginFragment loginFragment;
	private CloudFragment cloudFragment;
	private CloudCategoryFragment cloudCategoryFragment;
	
	public static CloudManagerFragment getInstance() {
		return new CloudManagerFragment();
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(TAG, "CloudManagerFragment onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "CloudManagerFragment onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG, "CloudManagerFragment onCreateView");
		View view = inflater.inflate(R.layout.fragment_cloud_manager, null);
		manageCloudFragment(LoginFragment.TAG);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "CloudManagerFragment onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG, "CloudManagerFragment onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "CloudManagerFragment onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "CloudManagerFragment onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG, "CloudManagerFragment onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "CloudManagerFragment onStop");
	}
	
	public void manageCloudFragment(String tag) {
		if (tag.equals(LoginFragment.TAG)) {
			
			loginFragment = LoginFragment.getInstance();
			FragmentTransaction loginTransaction = getChildFragmentManager().beginTransaction();
			loginTransaction.replace(R.id.cloud_manager, loginFragment, LoginFragment.TAG).commit();
			
		} else if (tag.equals(CloudFragment.TAG)) {
			
			cloudFragment = CloudFragment.getInstance();
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.KEY_CLOUD_CATEGORY_POSITION, position);
			cloudFragment.setArguments(bundle);
			FragmentTransaction cloudTransaction = getChildFragmentManager().beginTransaction();
			cloudTransaction.replace(R.id.cloud_manager, cloudFragment, CloudFragment.TAG).commit();
			
		} else if (tag.equals(CloudCategoryFragment.TAG)) {
			
			cloudCategoryFragment = CloudCategoryFragment.getInstance();
			FragmentTransaction categoryTransaction = getChildFragmentManager().beginTransaction();
			categoryTransaction.replace(R.id.cloud_manager, cloudCategoryFragment, CloudCategoryFragment.TAG).commit();
			
		}
		
	}

	public void backToParentCloudList() {
		ListCloudFileFragment fragment = (ListCloudFileFragment) cloudFragment.getChildFragmentManager().findFragmentByTag(ListCloudFileFragment.TAG);
		if (fragment.pathStack.size() == 1) {
			manageCloudFragment(CloudCategoryFragment.TAG);
		} else {
			fragment.back();
		}
	}
}
