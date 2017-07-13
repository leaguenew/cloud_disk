package com.echoii.tv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.echoii.tv.R;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>欢迎界面</p>
 *
 */
public class WelcomeFragment extends Fragment {
	
	public static WelcomeFragment getInstance() {
		return new WelcomeFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ImageView image = new ImageView(getActivity());
		image.setImageResource(R.drawable.welcome_bg);
		image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		image.setScaleType(ScaleType.CENTER);
		return image;
	}

}
