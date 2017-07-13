package com.echoii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.echoii.activity.fragment.OnFragmentChangeListener;

public class CloudFragment extends Fragment {
	
	private ImageView mTvewSetting;
	private TextView mTvewExit;
	
	private GridView mGridData;
	private AllfileGridAdapter cloudAllFileAdapter = null;
	private String[] itemNames = { "全部文件", "图片", "音乐", "视频", "BT种子", "文档",
			"数据市场", "我的群", "我的分享" };
	private int[] cloudImages = { R.drawable.grid_item_allfile,
			R.drawable.grid_item_image, R.drawable.grid_item_music,
			R.drawable.grid_item_video, R.drawable.grid_item_bt,
			R.drawable.grid_item_document, R.drawable.grid_data_market,
			R.drawable.grid_item_mygroup, R.drawable.grid_item_myshare };

	private GridView mGridHotResource = null;
	private HotResourceAdapter mHotResourceAdapter = null;
	private int[] hotResourceImages = { 
			R.drawable.cloud_hot_01,
			R.drawable.cloud_hot_02,
			R.drawable.cloud_hot_03,
			R.drawable.cloud_hot_04,
			R.drawable.cloud_hot_05,
			R.drawable.cloud_hot_06 };
	private OnFragmentChangeListener mListener;
    private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
    private MainCloudActivity mActivity;
    
    public CloudFragment(){}
    
	public CloudFragment(OnFragmentChangeListener listener){
		this.mListener = listener;	
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		sessionStatusPreference = activity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		mActivity =  (MainCloudActivity)activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PATH, "CloudFragment").commit();
    	sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PARENT_PATH, "null").commit();
		View view = inflater.inflate(R.layout.fragment_cloud, null);
		mGridData = (GridView) view.findViewById(R.id.cloud_gridview);
		mTvewSetting = (ImageView)view.findViewById(R.id.setting);
		mTvewExit = (TextView)view.findViewById(R.id.exit);
		cloudAllFileAdapter = new AllfileGridAdapter(getActivity(),
				cloudImages, itemNames);
		mGridData.setAdapter(cloudAllFileAdapter);

		mGridHotResource = (GridView) view
				.findViewById(R.id.cloud_hot_resource);
		mHotResourceAdapter = new HotResourceAdapter(hotResourceImages);
		mGridHotResource.setAdapter(mHotResourceAdapter);
		
		mGridData.setOnItemClickListener(gridDatalistener);
		mTvewSetting.setOnClickListener(titleOnClickListener);
		mTvewExit.setOnClickListener(titleOnClickListener);
		return view;
	}

	private OnClickListener titleOnClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0) 
		{
			switch (arg0.getId())
			{
			case R.id.setting:
			{
				mActivity.toggle();
				break;
			}
			case R.id.exit:
			{
				mActivity.finish();
				break;
			}
			default:
				break;
			}
			
		}
		
	};
	
	private OnItemClickListener gridDatalistener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id)
		{
			if (position == 6) {
				mListener.onFragmentChange(CenterFragment2.DATA_MARKET_HOME , position);
				
			}else{
				mListener.onFragmentChange(CenterFragment2.CLOUD_LIST_ALLFILE_FRAGMENT , position);	
			}
		}		
	};
	
	private class AllfileGridAdapter extends BaseAdapter {
		LayoutInflater layoutInflater = null;
		int[] images = null;
		String[] itemNames = null;

		public AllfileGridAdapter(Context context, int[] images,
				String[] itemNames) {
			this.images = images;
			this.itemNames = itemNames;
			layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.cloud_allfile_item, null);
			}
			ImageView img = (ImageView) convertView
					.findViewById(R.id.img_cloud_allfile_item);
			TextView tvew = (TextView) convertView
					.findViewById(R.id.tvew_cloud_allfile_item);
			tvew.setText(this.itemNames[position]);
			img.setImageResource(this.images[position]);
			return convertView;
		}

	}

	public class HotResourceAdapter extends BaseAdapter {

		private int[] images ;
		
		public HotResourceAdapter(int[] images) {
			this.images = images;
		}

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.cloud_hot_resource, null);
			}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.item_hot_resource);
			imageView.setImageResource(hotResourceImages[position]);
			return convertView;
		}

	}
}
