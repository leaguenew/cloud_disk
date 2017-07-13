package com.echoii.tv.fragment.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.echoii.tv.R;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>设置同步默认路径界面底部操作栏</p>
 *
 */
public class BottomSyncPathSelectorFragment extends Fragment implements OnClickListener{
	
	public static final String TAG = "BottomSyncPathSelectorFragment";
	
	private TextView mTxvDefaultPath;
	private Button mButSubmit;
	private Button mButCancel;
	private OnSelectorActionListener listener;

	
	public static BottomSyncPathSelectorFragment getInstance() {
		return new BottomSyncPathSelectorFragment();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnSelectorActionListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bottom_syncpath, null);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		mTxvDefaultPath = (TextView) view.findViewById(R.id.default_path);
		mButSubmit = (Button) view.findViewById(R.id.submit);
		mButSubmit.setOnClickListener(this);
		mButCancel = (Button) view.findViewById(R.id.cancel);
		mButCancel.setOnClickListener(this);
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
		case R.id.submit:
			submit();
			break;
		case R.id.cancel:
			cancel();
			break;

		default:
			break;
		}
	}

	private void cancel() {
		listener.cancel();
	}

	private void submit() {
		listener.submit();
	}

	public void showCurrentPath(String currentPath) {
		mTxvDefaultPath.setText(currentPath);
	}
	
	public interface OnSelectorActionListener{
		public void submit();
		public void cancel();
	}
	
}
