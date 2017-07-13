package com.echoii.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.activity.CenterFragment2;
import com.echoii.activity.MainCloudActivity;
import com.echoii.activity.R;
import com.echoii.constant.MessageInfo;

public class DataMarketListFragment extends Fragment
{
	private int itemId ;
	private TextView mTvewExit;
	private ImageView mImgSetting;
	private TextView mTvewTitle;
	private ListView mListView;
	private ListViewAdapter adapter;
	private MySaleAdapter saleadapter;
	private String[] strPagerTitles = {"重生之官道","三国演义","召唤美女军团","异世之风流大法师","重生之官道"};
	private Double[] doublePrice = {100.00,10.00,90.00,98.99,89.09};
	private Integer[] intNum = {45678,98768,77876,66655,322};
	private int[] images = { R.drawable.market_list_a1,
			R.drawable.market_list_a2, R.drawable.market_list_a3,
			R.drawable.market_list_a4, R.drawable.market_list_a5};
	private MainCloudActivity mActivity;
	private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
	public DataMarketListFragment(){}
	
	public DataMarketListFragment(int itemId)
	{
		this.itemId = itemId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Log.d("zhangzhang","onCreateView");
		View view = inflater.inflate(R.layout.datamarket_list, null);
		mTvewTitle = (TextView)view.findViewById(R.id.tvew_title);
		mListView = (ListView)view.findViewById(R.id.market_list);
		mImgSetting = (ImageView)view.findViewById(R.id.setting);
		mTvewExit = (TextView)view.findViewById(R.id.exit);
		setMiddleTitle();
		
		mImgSetting.setOnClickListener(new viewOnClickListener());
		mTvewExit.setOnClickListener(new viewOnClickListener());
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(4 == this.itemId)
		{
			saleadapter = new MySaleAdapter(mActivity,strPagerTitles,doublePrice,intNum,images);
			mListView.setAdapter(saleadapter);
		}
		else
		{
			adapter = new ListViewAdapter(mActivity,strPagerTitles,doublePrice,intNum,images);
			mListView.setAdapter(adapter);
		}
		
		savePathAndParentPath();
	}
	
	private void savePathAndParentPath()
	{
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		switch (CenterFragment2.TAB_ID) {
		case 0:
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PATH, "DataMarketListFragment").commit();
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PARENT_PATH, "DataMarketHomeFragment").commit();
			break;
		case 1:
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PATH, "DataMarketListFragment").commit();
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH, "DataMarketHomeFragment").commit();
			break;
		case 2:
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PATH, "DataMarketListFragment").commit();
			sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PARENT_PATH, "DataMarketHomeFragment").commit();
			break;
		default:
			break;
		}
	}
	
	private void setMiddleTitle()
	{
		switch (this.itemId)
		{
			case 0:
			{
				mTvewTitle.setText(getResources().getString(R.string.data_shopping));
				break;
			}
			case 1:
			{
				mTvewTitle.setText(getResources().getString(R.string.data_hot_video));
				break;
			}
			case 2:
			{
				mTvewTitle.setText(getResources().getString(R.string.data_travel));
				break;
			}
			case 3:
			{
				mTvewTitle.setText(getResources().getString(R.string.data_book));
				break;
			}
			case 4:
			{
				mTvewTitle.setText(getResources().getString(R.string.data_myauction));
				break;
			}
			case 5:
			{
				mTvewTitle.setText(getResources().getString(R.string.data_mydeal));
				break;
			}
			default:
				break;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = (MainCloudActivity)activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private class viewOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			// TODO Auto-generated method stub
			switch (arg0.getId())
			{
				case  R.id.setting:
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
	
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
	
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what)
			{
				case MessageInfo.MARKET_PURCHASE:
				{
					showToast("对不起，此功能还未实现");
					break;
				}
				case MessageInfo.MARKET_SALE:
				{
					showToast("对不起，该商品还不能进行此操作");
					break;
				}
				default:
					break;
			}
		}
	};
	
	
	private class MySaleAdapter extends BaseAdapter
	{
		private String[] titles;
		private Double[] price;
		private Integer[] num;
		private int[] images;
		private LayoutInflater inflater;
		ViewHolder holder;
		public MySaleAdapter(Context context,String[] titles,Double[] price,Integer[] num,int[] images)
		{
			this.titles = titles;
			this.price = price;
			this.num = num;
			this.images = images;
			inflater  = LayoutInflater.from(context);
		}
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return titles.length;
		}

		@Override
		public Object getItem(int arg0)
		{
			return arg0;
		}

		@Override
		public long getItemId(int arg0) 
		{
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) 
		{
			if (null == arg1)
			{
				arg1 = inflater.inflate(R.layout.datamarket_mysale_list_item, null);
				holder = new ViewHolder(); 
				holder.img = (ImageView)arg1.findViewById(R.id.img_marketlist_item);
				holder.title = (TextView)arg1.findViewById(R.id.tvew_item_marketlist);
				holder.prices = (TextView)arg1.findViewById(R.id.tvew_item_marketlist_money);
				holder.tvewNum = (TextView)arg1.findViewById(R.id.tvew_item_marketlist_num);
				holder.btnOperate = (Button)arg1.findViewById(R.id.btn_item_marketlist_shopping);
				
				arg1.setTag(holder);  
			}
			else
			{
				holder = (ViewHolder) arg1.getTag();  
			}
			
			holder.img.setImageResource(images[arg0]);
			holder.title.setText(titles[arg0]);
			holder.prices.setText(String.valueOf(price[arg0]));
			holder.tvewNum.setText(String.valueOf(num[arg0]));		
			
			holder.btnOperate.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = MessageInfo.MARKET_PURCHASE;
					handler.sendMessage(msg);
				}
			});
			
			return arg1;
		}
		
		private class ViewHolder
		{
			ImageView img;
			TextView title;
			TextView prices;
			TextView tvewNum;
			Button btnOperate;
		}
		
	}
	private class ListViewAdapter extends BaseAdapter
	{
		private String[] titles;
		private Double[] price;
		private Integer[] num;
		private int[] images;
		private LayoutInflater inflater;
		ViewHolder holder;
		public ListViewAdapter(Context context,String[] titles,Double[] price,Integer[] num,int[] images)
		{
			this.titles = titles;
			this.price = price;
			this.num = num;
			this.images = images;
			inflater  = LayoutInflater.from(context);
		}
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return titles.length;
		}

		@Override
		public Object getItem(int arg0)
		{
			return arg0;
		}

		@Override
		public long getItemId(int arg0) 
		{
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) 
		{
			if (null == arg1)
			{
				arg1 = inflater.inflate(R.layout.datamarket_list_item, null);
				holder = new ViewHolder(); 
				holder.img = (ImageView)arg1.findViewById(R.id.img_marketlist_item);
				holder.title = (TextView)arg1.findViewById(R.id.tvew_item_marketlist);
				holder.prices = (TextView)arg1.findViewById(R.id.tvew_item_marketlist_money);
				holder.tvewNum = (TextView)arg1.findViewById(R.id.tvew_item_marketlist_num);
				holder.btnOperate = (Button)arg1.findViewById(R.id.btn_item_marketlist_shopping);
				
				arg1.setTag(holder);  
			}
			else
			{
				holder = (ViewHolder) arg1.getTag();  
			}
			
			holder.img.setImageResource(images[arg0]);
			holder.title.setText(titles[arg0]);
			holder.prices.setText(String.valueOf(price[arg0]));
			holder.tvewNum.setText(String.valueOf(num[arg0]));
			
			holder.btnOperate.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = MessageInfo.MARKET_SALE;
					handler.sendMessage(msg);
				}
			});
			return arg1;
		}
		
		private class ViewHolder
		{
			ImageView img;
			TextView title;
			TextView prices;
			TextView tvewNum;
			Button btnOperate;
		}
		
	}
}
