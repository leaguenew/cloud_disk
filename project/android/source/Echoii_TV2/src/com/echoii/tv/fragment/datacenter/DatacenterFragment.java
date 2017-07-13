package com.echoii.tv.fragment.datacenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echoii.tv.DatacenterAuth;
import com.echoii.tv.R;
import com.echoii.tv.constants.Constants;
import com.echoii.tv.fragment.MessageFragment;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心文件浏览器</p>
 *
 */
public class DatacenterFragment extends Fragment {
	
	public static final String TAG = "DatacenterFragment";
	
	private TopDatacenterActionBarFragment mTopFragment;
	private ListDatacenterFileFragment mListFragment;
	private BottomDatacenterActionBarFragment mBottomFragment;
	private MessageFragment mMessageFragment;
	
	public static DatacenterFragment getInstance() {
		return new DatacenterFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(TAG, "DatacenterFragment onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "DatacenterFragment onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG, "DatacenterFragment onCreateView");
		View view = inflater.inflate(R.layout.fragment_datacenter, null);
		initFragments();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "DatacenterFragment onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG, "DatacenterFragment onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "DatacenterFragment onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "DatacenterFragment onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG, "DatacenterFragment onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "DatacenterFragment onStop");
	}
	
	private void initFragments() {
		
		mTopFragment = TopDatacenterActionBarFragment.getInstance();
		FragmentTransaction topTran = getChildFragmentManager().beginTransaction();
		topTran.replace(R.id.top, mTopFragment, TopDatacenterActionBarFragment.TAG).commit();
		
		if (DatacenterAuth.loginStatus == Constants.SUCCESS) {
			mListFragment = ListDatacenterFileFragment.getInstance();
			FragmentTransaction listTran = getChildFragmentManager().beginTransaction();
			listTran.replace(R.id.list, mListFragment, ListDatacenterFileFragment.TAG).commit();
		} else {
			mMessageFragment = MessageFragment.getInstance();
			Bundle bundle = new Bundle();
			bundle.putString("msg", "登录失败，请重试。");
			mMessageFragment.setArguments(bundle);
			FragmentTransaction msgTransaction = getChildFragmentManager().beginTransaction();
			msgTransaction.replace(R.id.list, mMessageFragment, MessageFragment.TAG).commit();
		}
		
		mBottomFragment = BottomDatacenterActionBarFragment.getInstance();
		FragmentTransaction bottomTran = getChildFragmentManager().beginTransaction();
		bottomTran.replace(R.id.bottom, mBottomFragment, BottomDatacenterActionBarFragment.TAG).commit();
		
	}

}
