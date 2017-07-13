package com.echoii.tv.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.echoii.tv.EchoiiTVMainActivity;
import com.echoii.tv.R;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>主界面左侧列表菜单，可继续添加其他项目</p>
 *
 */
public class LeftListFragment extends ListFragment {
	
	public static final String TAG = "LeftListFragment";
	
	public static final String[] leftTitle = new String[3];
	
	public static LeftListFragment getInstance() {
		return new LeftListFragment();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.d(TAG, "LeftListFragment onActivityCreated");
		leftTitle[0] = getActivity().getResources().getString(R.string.open_echoii);
		leftTitle[1] = getActivity().getResources().getString(R.string.data_center_title);
		leftTitle[2] = getActivity().getResources().getString(R.string.settings_title);
		
		setListAdapter(new LeftItemAdapter());
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setHeaderDividersEnabled(true);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG, "LeftListFragment onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(TAG, "LeftListFragment onAttach");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "LeftListFragment onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG, "LeftListFragment onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "LeftListFragment onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "LeftListFragment onResume");
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ViewPager pager = (ViewPager) getActivity().findViewById(R.id.pager);
		if (position == 2) {
			((EchoiiTVMainActivity)getActivity()).showPreferenceFragment();
		} else {
			((EchoiiTVMainActivity)getActivity()).showViewPager();
			pager.setCurrentItem(position + 1, false);
		}
		
	}
	
	public class LeftItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return leftTitle.length;
		}

		@Override
		public Object getItem(int position) {
			return leftTitle[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_left_listview, null);
				holder.itemText = (TextView) convertView.findViewById(R.id.item_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag(); 
			}
			
			holder.itemText.setText(leftTitle[position]);
			
			return convertView;
		}
		
	}
	
	private final class ViewHolder {
		TextView itemText;
	}

}
