package com.echoii.tv.fragment.cloud;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.echoii.tv.R;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>云端文件分类界面</p>
 *
 */
public class CloudCategoryFragment extends Fragment {
	
	public static final String TAG = "CloudCategoryFragment";
	public static final int[] icon = {
		R.drawable.cloud_category_all_file,
		R.drawable.cloud_category_image,
		R.drawable.cloud_category_music,
		R.drawable.cloud_category_movie,
		R.drawable.cloud_category_bt,
		R.drawable.cloud_category_doc,
		R.drawable.cloud_category_other,
		R.drawable.cloud_category_mygroup,
		R.drawable.cloud_category_share
	};
	public static String[] title = new String[9];
	
	private GridView mCategoryGrid;
	private OnCloudCategoryClickListener listener;
	
	public static CloudCategoryFragment getInstance() {
		return new CloudCategoryFragment();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		LogUtil.d(TAG, "CloudCategoryFragment onAttach");
		try {
			listener = (OnCloudCategoryClickListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "CloudCategoryFragment onCreate");
		title[0] = getActivity().getResources().getString(R.string.cloud_category_all_file);
		title[1] = getActivity().getResources().getString(R.string.cloud_category_image);
		title[2] = getActivity().getResources().getString(R.string.cloud_category_music);
		title[3] = getActivity().getResources().getString(R.string.cloud_category_movie);
		title[4] = getActivity().getResources().getString(R.string.cloud_category_bt);
		title[5] = getActivity().getResources().getString(R.string.cloud_category_doc);
		title[6] = getActivity().getResources().getString(R.string.cloud_category_other);
		title[7] = getActivity().getResources().getString(R.string.cloud_category_mygroup);
		title[8] = getActivity().getResources().getString(R.string.cloud_category_share);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.d(TAG, "CloudCategoryFragment onCreateView");
		View view = inflater.inflate(R.layout.fragment_cloud_category, null);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		mCategoryGrid = (GridView) view.findViewById(R.id.cloud_category);
		mCategoryGrid.setAdapter(new CategoryGridAdapter());
		mCategoryGrid.setOnItemClickListener(new OnCategoryItemClickListenerImpl());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.d(TAG, "CloudCategoryFragment onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		LogUtil.d(TAG, "CloudCategoryFragment onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		LogUtil.d(TAG, "CloudCategoryFragment onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d(TAG, "CloudCategoryFragment onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.d(TAG, "CloudCategoryFragment onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.d(TAG, "CloudCategoryFragment onStop");
	}
	
	public class CategoryGridAdapter extends BaseAdapter {

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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_cloud_category_gridview, null);
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
	
	public class OnCategoryItemClickListenerImpl implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			listener.onCloudCategoryClick(position);
		}
		
	}
	
	public interface OnCloudCategoryClickListener {
		public void onCloudCategoryClick(int position);
	}

}
