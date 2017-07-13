package com.echoii.tv.fragment.datacenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.echoii.tv.R;
import com.echoii.tv.fragment.cloud.TopCloudActionBarFragment.OnBackToParentPathListener;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心顶部操作栏，如返回，切换文件显示风格等</p>
 *
 */
public class TopDatacenterActionBarFragment extends Fragment implements OnClickListener{
	
	public static final String TAG = "TopDatacenterActionBarFragment";
	
	private ImageButton mButBack;
	private OnBackToParentPathListener listener;
	
	public static TopDatacenterActionBarFragment getInstance() {
		return new TopDatacenterActionBarFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnBackToParentPathListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_top_cloud, null);
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
		listener.onBackToParentPath(TAG);
	}

}
