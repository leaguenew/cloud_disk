package com.echoii.tv.fragment.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.echoii.tv.R;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>设置默认同步路径顶部操作栏</p>
 *
 */
public class TopSyncPathSelectorFragment extends Fragment implements OnClickListener{
	
	public static final String TAG = "TopSyncPathSelectorFragment";
	
	private ImageButton mButBack;
	private OnBackListener listener;

	public static TopSyncPathSelectorFragment getInstance() {
		return new TopSyncPathSelectorFragment();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnBackListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_top_syncpath, null);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		mButBack = (ImageButton) view.findViewById(R.id.back);
		mButBack.setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			back();
			break;

		default:
			break;
		}
	}

	private void back() {
		listener.onBack();
	}
	
	public interface OnBackListener{
		public void onBack();
	}

}
