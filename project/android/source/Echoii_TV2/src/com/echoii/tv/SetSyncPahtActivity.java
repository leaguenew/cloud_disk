package com.echoii.tv;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.echoii.tv.fragment.setting.BottomSyncPathSelectorFragment;
import com.echoii.tv.fragment.setting.BottomSyncPathSelectorFragment.OnSelectorActionListener;
import com.echoii.tv.fragment.setting.DialogSyncPathSelectorFragment;
import com.echoii.tv.fragment.setting.ListSyncPathSelectorFragment;
import com.echoii.tv.fragment.setting.ListSyncPathSelectorFragment.OnListDataChangeListener;
import com.echoii.tv.fragment.setting.TopSyncPathSelectorFragment.OnBackListener;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>设置本地默认同步路径类</p>
 *
 */
public class SetSyncPahtActivity extends FragmentActivity implements 
			OnBackListener, 
			OnListDataChangeListener,
			OnSelectorActionListener {
	
	private DialogSyncPathSelectorFragment dialogFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		dialogFragment = DialogSyncPathSelectorFragment.getInstance();
		dialogFragment.show(getSupportFragmentManager(), DialogSyncPathSelectorFragment.TAG);
	}

	@Override
	public void OnShowCurrentPath(String currentPath) {
		BottomSyncPathSelectorFragment bottomFragment = (BottomSyncPathSelectorFragment) dialogFragment.getChildFragmentManager().findFragmentByTag(BottomSyncPathSelectorFragment.TAG);
		bottomFragment.showCurrentPath(currentPath);
	}
	
	private void closeDialogFragment() {
		if (dialogFragment != null) {
			dialogFragment.dismiss();
		}
	}

	@Override
	public void submit() {
		ListSyncPathSelectorFragment listFragment = (ListSyncPathSelectorFragment) dialogFragment.getChildFragmentManager().findFragmentByTag(ListSyncPathSelectorFragment.TAG);
		listFragment.saveSyncDefaultPath();
		closeDialogFragment();
	}

	@Override
	public void cancel() {
		closeDialogFragment();
	}

	@Override
	public void onBack() {
		ListSyncPathSelectorFragment listFragment = (ListSyncPathSelectorFragment) dialogFragment.getChildFragmentManager().findFragmentByTag(ListSyncPathSelectorFragment.TAG);
		listFragment.back();
	}
	
}
