package com.echoii.activity.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.echoii.activity.CenterFragment2;
import com.echoii.activity.MainCloudActivity;
import com.echoii.activity.R;

public class DataMarketHomeFragment extends Fragment
{
	private ViewPager mViewPager;
	private ViewPagerAdapter viewpagerAdapter;	
	 // 记录当前选中位置  
    private int currentIndex; 
    private List<View> views;      
    // 底部小点图片  
    private ImageView[] dots;  
	private LinearLayout llayout_dots;
	private TextView mTvewPagerTitle;
	private String[] strPagerTitles = {"稻草人","我的最爱","渴望的家，大家一起来住吧？","高品质生活，从钢琴开始。。"};
	
	private TextView mTvewExit;
	private ImageView mImgSetting;
	private GridView mGridView;
    private String[] itemNames = {"数据买卖","热门视频","旅游攻略","电子书","我的拍卖","我的交易"};
	private int[] images = { R.drawable.market_business,
			R.drawable.market_hot_video, R.drawable.market_travel,
			R.drawable.market_ebook, R.drawable.market_sale,
			R.drawable.market_transaction};
	private DataMarketHomeAdapter adapter = null;
	private OnFragmentChangeListener mListener;
	
	private MainCloudActivity mActivity;
	private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
	
	
	public DataMarketHomeFragment(){}
	
	public DataMarketHomeFragment(OnFragmentChangeListener listener)
	{
		mListener = listener;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = (MainCloudActivity)activity;
	}
	
	@Override
	public void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		savePathAndParentPath();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_datamarket_home, null);
		mGridView = (GridView)view.findViewById(R.id.market_gridview);
		mViewPager = (ViewPager)view.findViewById(R.id.market_viewPager);
		llayout_dots = (LinearLayout) view.findViewById(R.id.market_dots); 
		mTvewPagerTitle = (TextView)view.findViewById(R.id.market_recommend_title);
		mImgSetting = (ImageView)view.findViewById(R.id.setting);
		mTvewExit = (TextView)view.findViewById(R.id.exit);
		
	       // 初始化页面  
        initViews();  
        initDots();
        
		adapter = new DataMarketHomeAdapter(getActivity(),images,itemNames);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new ItemClickListener());
		mImgSetting.setOnClickListener(new itemOnClickListener());
		mTvewExit.setOnClickListener(new itemOnClickListener());
		return view;
	}
	
	 /**
	    * GridView Item CLick
	    */
	private class ItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) 
		{
			// TODO Auto-generated method stub
			Log.d("zhangzhang","mListener = " + mListener);
			mListener.onFragmentChange(CenterFragment2.DATA_MARKET_LIST_FRAAGMENT , arg2);		
		}		
	}
	
	private class  itemOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			// TODO Auto-generated method stub
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
		
	}
	
	public void savePathAndParentPath()
	{
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		switch (CenterFragment2.TAB_ID) {
		case 0:
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PATH, "DataMarketHomeFragment").commit();
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PARENT_PATH, "CloudFragment").commit();
			break;
		case 1:
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PATH, "DataMarketHomeFragment").commit();
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH, "DataCenterFragment").commit();
			break;
		case 2:
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PATH, "DataMarketHomeFragment").commit();
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PARENT_PATH, "PhoneFragment").commit();
			break;
		default:
			break;
		}
		
	}
	
	
	/**
	 * 初始化 Viewpagers 
	 */
	private void initViews()
	{
		 LayoutInflater inflater = LayoutInflater.from(getActivity());  
		views = new ArrayList<View>();  
        // 初始化引导图片列表  
	     views.add(inflater.inflate(R.layout.what_new_one, null));  
	     views.add(inflater.inflate(R.layout.what_new_two, null));  
	     views.add(inflater.inflate(R.layout.what_new_three, null));  
	     views.add(inflater.inflate(R.layout.what_new_four, null));  
        // 初始化Adapter  
        viewpagerAdapter = new ViewPagerAdapter(views); 
        
        mViewPager.setAdapter(viewpagerAdapter);  
        mViewPager.setOnPageChangeListener(new viewPagerChangeListener());
        
        //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动  
//        mViewPager.setCurrentItem((views.size() * 100));  
	}

   /**
    * 初始化底部小圆点
    */
   private void initDots() 
   {    
        dots = new ImageView[views.size()];  
  
        // 循环取得小点图片  
        for (int i = 0; i < views.size(); i++) 
        {  
            dots[i] = (ImageView) llayout_dots.getChildAt(i);  
            dots[i].setBackgroundResource(R.drawable.dot_normal);
           
        }  
  
        currentIndex = 0;  
        dots[currentIndex].setBackgroundResource(R.drawable.dot_selected);// 设置为白色，即选中状态  
        mTvewPagerTitle.setText(strPagerTitles[currentIndex]);
    }  
   
   /**
    *设置底部小圆点
    */
   private void setCurrentDot(int position) 
   {  
       if (position < 0 || position > views.size() - 1  
               || currentIndex == position)
       {  
           return;  
       }  
 
       dots[position].setBackgroundResource(R.drawable.dot_selected);
       dots[currentIndex].setBackgroundResource(R.drawable.dot_normal);
       mTvewPagerTitle.setText(strPagerTitles[position]);
 
       currentIndex = position;  
   }  
   
   /**
    * Viewpager 监听
    */
	private class viewPagerChangeListener implements OnPageChangeListener 
	{

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0)
		{
			// TODO Auto-generated method stub
//			 setCurrentDot(arg0 % (views.size()- 1)); 
			setCurrentDot(arg0);
		}
		
	}
	
	
	/***
	 * ViewPager 适配器
	 * @author Administrator
	 *
	 */
	private  class ViewPagerAdapter extends PagerAdapter
	{
	    // 界面列表  
	    private List<View> views;  
	    public ViewPagerAdapter(List<View> views)
	    {  
	        this.views = views;  
	    } 
	    // 销毁arg1位置的界面  
	    @Override  
	    public void destroyItem(View arg0, int arg1, Object arg2)
	    {  
	        ((ViewPager) arg0).removeView(views.get(arg1));  
	    }  
		@Override
		public int getCount() 
		{
	        if (views != null) 
	        {  
	            return views.size();  
	        }  
	        return 0; 
		}

		 // 判断是否由对象生成界面  
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			 return (arg0 == arg1);  
		}
		  // 初始化arg1位置的界面  用当前的position 除以 图片数组长度取余数是关键 
	    @Override  
	    public Object instantiateItem(View arg0, int arg1) 
	    {  
	    	//不能循环的
	        ((ViewPager) arg0).addView(views.get(arg1), 0);  
	        return views.get(arg1); 
	    	
//	        if (arg1 == views.size() - 1) 
//	        {  
//	            ImageView mStartWeiboImageButton = (ImageView) arg0  
//	                    .findViewById(R.id.iv_start_weibo);  
//	            mStartWeiboImageButton.setOnClickListener(new OnClickListener()
//	            {  
//	  
//	                @Override  
//	                public void onClick(View v) {  
//	                    // 设置已经引导  
//	                    setGuided();  
//	                    goHome();  
//	  
//	                }  
//	  
//	            });  
//	        }  
	
	        //循环显示
//            ((ViewPager)arg0).addView(views.get((arg1 % views.size())), 0);  
//            return views.get(arg1 % (views.size()));  
	    }  

	}
	
   /**
    * GridView适配器
    */
	private class DataMarketHomeAdapter extends BaseAdapter
	{
        LayoutInflater layoutInflater = null;
        Context mContext = null;
        int[] images = null;
        String[] itemNames = null;
        
		public DataMarketHomeAdapter(Context context,int[] images , String[] itemNames)
		{
            mContext = context;
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
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			convertView = layoutInflater.inflate(R.layout.datamarket_item, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.img_datamarket_item);
            TextView tvew = (TextView)convertView.findViewById(R.id.tvew_datamarket_item);
            tvew.setText(this.itemNames[position]);
            img.setImageResource(this.images[position]);
			return convertView;
		}		
	}
}
