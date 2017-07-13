package com.echoii.tv.fragment.datacenter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.echoii.tv.R;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心文件底部具体操作栏，如复制，粘贴，删除，移动，分享等</p>
 *
 */
public class BottomDatacenterActionBarFragment extends Fragment{
	
	public static final String TAG = "BottomDatacenterActionBarFragment";
	
	public static final int[] icon = {
		R.drawable.copy,
		R.drawable.delete,
		R.drawable.rename,
		R.drawable.move,
		R.drawable.share
	};
	public static final String[] title = new String[5];
	
	private GridView actionGrid;
	private ActionGridAdapter adapter;
	
	
	public static BottomDatacenterActionBarFragment getInstance() {
		return new BottomDatacenterActionBarFragment();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		title[0] = getActivity().getResources().getString(R.string.bottom_cloud_copy);
		title[1] = getActivity().getResources().getString(R.string.bottom_cloud_delete);
		title[2] = getActivity().getResources().getString(R.string.bottom_cloud_rename);
		title[3] = getActivity().getResources().getString(R.string.bottom_cloud_move);
		title[4] = getActivity().getResources().getString(R.string.bottom_cloud_share);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bottom_cloud, null);
		initViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initViews(View view) {
		actionGrid = (GridView) view.findViewById(R.id.bottom_cloud_gridview);
		adapter = new ActionGridAdapter();
		actionGrid.setAdapter(adapter);
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

	public void showCurrentPath(String currentPath) {
		
	}
	
	public class ActionGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return icon.length;
		}

		@Override
		public Object getItem(int position) {
			return icon[position];
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_bottom_cloud_gridview, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag(); 
			}
			
			holder.icon.setImageResource(icon[position]);
			holder.title.setText(title[position]);
			
			return convertView;
		}
		
	}

	private final class ViewHolder {
		ImageView icon;
		TextView title;
	}
	
}
