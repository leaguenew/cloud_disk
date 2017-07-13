package com.echoii.tv.fragment.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echoii.tv.R;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>设置默认同步路径dialog</p>
 *
 */
public class DialogSyncPathSelectorFragment extends DialogFragment {
	
	public static final String TAG = "DialogSyncPathSelectorFragment";
	
	private TopSyncPathSelectorFragment mTopFragment;
	private ListSyncPathSelectorFragment mListFragment;
	private BottomSyncPathSelectorFragment mBottomFragment;

	public static DialogSyncPathSelectorFragment getInstance() {
		return new DialogSyncPathSelectorFragment();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogSyncPathSelectorFragment.STYLE_NO_TITLE, getTheme());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_syncpath, null);
		initFragments();
		return view;
	}

	private void initFragments() {
		
		mTopFragment = new TopSyncPathSelectorFragment();
		FragmentTransaction topTran = getChildFragmentManager().beginTransaction();
		topTran.replace(R.id.top, mTopFragment, TopSyncPathSelectorFragment.TAG).commit();
		
		mListFragment = new ListSyncPathSelectorFragment();
		FragmentTransaction listTran = getChildFragmentManager().beginTransaction();
		listTran.replace(R.id.list, mListFragment, ListSyncPathSelectorFragment.TAG).commit();
		
		mBottomFragment = new BottomSyncPathSelectorFragment();
		FragmentTransaction bottomTran = getChildFragmentManager().beginTransaction();
		bottomTran.replace(R.id.bottom, mBottomFragment, BottomSyncPathSelectorFragment.TAG).commit();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().finish();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
