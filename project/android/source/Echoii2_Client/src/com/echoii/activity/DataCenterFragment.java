package com.echoii.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.echoii.activity.fragment.DataCenterConnectionFragment;
import com.echoii.activity.fragment.DataCenterLoginFragment;
import com.echoii.activity.fragment.OnFragmentChangeListener;


public class DataCenterFragment extends Fragment
{
	private Activity mActivity;
    private GridView mGridData;
    private AllfileGridAdapter cloudAllFileAdapter = null;
    private String[] itemNames = {"全部文件","图片","音乐","视频","bt种子","文档","数据市场","我的分享","其他"};
	private int[] dataCenterImages = { R.drawable.grid_item_allfile,
			R.drawable.grid_item_image, R.drawable.grid_item_music,
			R.drawable.grid_item_video, R.drawable.grid_item_bt,R.drawable.grid_item_document,
			R.drawable.grid_data_market,
			R.drawable.grid_item_myshare,R.drawable.grid_item_other };
    private OnFragmentChangeListener mListener;
    private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
    private SharedPreferences ipPreference;
    private ImageView settingImage;
	private TextView disconnText;
    
    public DataCenterFragment(){}
    
    public DataCenterFragment(OnFragmentChangeListener listener){
    	this.mListener = listener;
    }
    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_datacenter_main, null);
        mGridData = (GridView) view.findViewById(R.id.center_gridview);
        cloudAllFileAdapter = new AllfileGridAdapter(getActivity(), dataCenterImages , itemNames);
        mGridData.setAdapter(cloudAllFileAdapter);
        mGridData.setOnItemClickListener(new DataCenterOnItemClickListenerImple());
        settingImage = (ImageView) view.findViewById(R.id.setting);
		settingImage.setOnClickListener(new OnClickListenerImpl());
		disconnText = (TextView) view.findViewById(R.id.disconnection);
		disconnText.setVisibility(View.VISIBLE);
		disconnText.setOnClickListener(new DisconnOnClickListenerImpl());
        return view;
    }
    
	@Override
	public void onResume() {
		super.onResume();
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		ipPreference = mActivity.getSharedPreferences(DataCenterConnectionFragment.IP_SHARED_NAME, Context.MODE_PRIVATE);
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PATH, "DataCenterFragment").commit();
    	sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH, "DataCenterConnectionFragment").commit();
	}

	private class DisconnOnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			if (ipPreference != null) {
				ipPreference.edit().remove(DataCenterConnectionFragment.CURRENT_CONN_IP).commit();
			}
			if (sessionStatusPreference != null) {
				sessionStatusPreference.edit().remove(CenterFragment2.SESSION_DATA_CENTER_PATH).commit();
				sessionStatusPreference.edit().remove(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH).commit();
				sessionStatusPreference.edit().remove(DataCenterConnectionFragment.DATA_CENTER_AUTO_CONN).commit();
				sessionStatusPreference.edit().remove(DataCenterLoginFragment.DATA_CENTER_LOGIN_SUCCESS).commit();
			}
			Fragment dataConnFragment = new DataCenterConnectionFragment(mListener);
            FragmentTransaction tran = getActivity().getSupportFragmentManager().beginTransaction();
            tran.replace(android.R.id.tabcontent, dataConnFragment, "dataConnFragment");
            tran.commit();
		}
	}
	
    private class AllfileGridAdapter extends BaseAdapter
    {
        LayoutInflater layoutInflater = null;
        Context mContext = null;
        int[] images = null;
        String[] itemNames = null;
        public AllfileGridAdapter(Context context , int[] images , String[] itemNames)
        {
            mContext = context;
            this.images = images;
            this.itemNames = itemNames;
            layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount()
        {
            return images.length;
        }

        @Override
        public Object getItem(int arg0)
        {
            return arg0;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            convertView = layoutInflater.inflate(R.layout.cloud_allfile_item, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.img_cloud_allfile_item);
            TextView tvew = (TextView)convertView.findViewById(R.id.tvew_cloud_allfile_item);
            tvew.setText(this.itemNames[position]);
            img.setImageResource(this.images[position]);
            return convertView;
        }
        
    }
    
    public class DataCenterOnItemClickListenerImple implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			if (position == 6) {
				mListener.onFragmentChange(CenterFragment2.DATA_MARKET_HOME , position);
			}else{
				mListener.onFragmentChange(CenterFragment2.DATA_CENTER_LIST_FRAGMENT , position);	
			}
			
		}
    }
    
    private class OnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			((MainCloudActivity)mActivity).toggle();
		}
	}
    
}
