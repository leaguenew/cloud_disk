package com.echoii.activity.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.activity.CenterFragment2;
import com.echoii.activity.MainCloudActivity;
import com.echoii.activity.R;
import com.echoii.activity.VideoPlayActivity;
import com.echoii.bean.cloudfilelist.AllFileDetailData;
import com.echoii.bean.cloudfilelist.CloudFileListRequestData;
import com.echoii.bean.cloudfilelist.Thumbnails;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.CommonUtil;
import com.echoii.constant.MessageInfo;
import com.echoii.network.http.CloudAllfileListHttp;
import com.echoii.network.http.CopyFileFolderHttp;
import com.echoii.network.http.DeleteFileFolderHttp;
import com.echoii.network.http.DownAndOpenFileHttp;
import com.echoii.network.http.GetImageThumbnailHttp;
import com.echoii.network.http.RenameFileFolderHttp;

public class CloudFileListFragment extends Fragment{
	public static final String TAG = "CloudFileListFragment";
	public static final int PER_SIZE = 40;
	
	/**文件操作相关---start*/
//	private RelativeLayout rLayoutOperate;
	private Button mBtnSale;
	private Button mBtnSave;
	private Button mBtnMove;
	private Button mBtnCopy;
	private Button mBtnDel;
	private Button mBtnRename;
	private Button mBtnShare;
	private Button mBtnPaste;
	private Button mBtnCancel;
	/**粘贴布局*/
	private RelativeLayout mLlayoutOperatePaste;
	/**复制等操作布局*/
	private RelativeLayout mLlayoutOperateFileFolder;
//	private TextView mTvewShare;
	/**文件操作相关---end*/
	
	/**标题显示相关---start*/
	private RelativeLayout mrlayoutEdit;
	private TextView mTvewBack;
	private TextView mTvewNumber;
	private TextView mTvewChooseAll;	
	private RelativeLayout mrlayoutNormal;
	private ImageView mTvewSetting;
	private TextView mTvewTitle;
	private TextView mTvewExit;	
	/**标题显示相关---end*/
	
	private ImageView mImgPlayVideo;
	private TextView mTvewNull;
	private ListView mListView;
	private ProgressDialog mProgressDialog = null;
	
	/**要展示的数据列表*/	
	private List<AllFileDetailData>  allfileListData = new ArrayList<AllFileDetailData>();
	private AllFileDataAdapter adapter = null;	
	
	/**请求数据参数*/
	private List<CloudFileListRequestData> mRequest = new ArrayList<CloudFileListRequestData>();
	
	/**下拉刷新布局---start*/
//	private ProgressBar progressBar;
//    private LinearLayout loadingLayout;
//	private LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//			LinearLayout.LayoutParams.WRAP_CONTENT);
	/**下拉刷新布局---end*/
    	
	/**判断是否全选  */
	private boolean isAllChoose = false;
	/** 最后可见条目的索引*/
	private int lastVisibleIndex = 0;
	/**当前选择是在哪个root下*/
	private int itemId = 0;	
	/** 保存会话信息，包括tab切换信息，请求路径等*/
	private SharedPreferences sessionStatusPreference; 	
	private SharedPreferences idToken;
	/**存储checkbox是否选中*/
	private HashMap<Integer,Boolean> mCheckedHash = new HashMap<Integer,Boolean>();
	
	private Activity mActivity;
	
	public CloudFileListFragment(){
		Log.d(TAG , "CloudFileListFragment : 构造");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "Cloud File List onAttach");
		mActivity = activity;
		idToken = getActivity().getSharedPreferences(CommonUtil.NEED_TOKENID,0);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d(TAG, "Cloud File List onCreateView");
    	
		View view = inflater.inflate(R.layout.cloud_filelist, null);
		mListView = (ListView)view.findViewById(R.id.listview_cloud_filelist);
		mTvewNull = (TextView)view.findViewById(R.id.tvew_cloud_filelist_null);
		mImgPlayVideo = (ImageView)view.findViewById(R.id.cloud_filelist_play_video);
		
		mTvewTitle = (TextView)view.findViewById(R.id.tvew_title);
		mTvewExit = (TextView)view.findViewById(R.id.exit);
		mTvewExit.setOnClickListener(new ExitOnClickListenerImpl());
		mTvewSetting = (ImageView)view.findViewById(R.id.setting);
		mrlayoutNormal = (RelativeLayout)view.findViewById(R.id.layout_title);
		mrlayoutEdit = (RelativeLayout)view.findViewById(R.id.layout_edit);
		mTvewBack = (TextView)view.findViewById(R.id.back);
		mTvewNumber = (TextView)view.findViewById(R.id.number);
		mTvewChooseAll = (TextView)view.findViewById(R.id.upload_or_chooseall);
		
		mLlayoutOperatePaste = (RelativeLayout)view.findViewById(R.id.operate_rlayout_paste);
		mLlayoutOperateFileFolder = (RelativeLayout)view.findViewById(R.id.phonelist_operate_filefolder);
		mBtnPaste = (Button)view.findViewById(R.id.operate_paste);
		mBtnCancel = (Button)view.findViewById(R.id.operate_cancel);
		mBtnSale = (Button)view.findViewById(R.id.phonelist_sale);
		mBtnSave = (Button)view.findViewById(R.id.phonelist_save);
		mBtnMove = (Button)view.findViewById(R.id.phonelist_move);
		mBtnCopy = (Button)view.findViewById(R.id.phonelist_copy);
		mBtnDel = (Button)view.findViewById(R.id.phonelist_del);
		mBtnRename = (Button)view.findViewById(R.id.phonelist_rename);
		mBtnShare = (Button)view.findViewById(R.id.phonelist_share);
		
		mrlayoutNormal.setVisibility(View.VISIBLE);
		mrlayoutEdit.setVisibility(View.GONE);
		mLlayoutOperateFileFolder.setVisibility(View.GONE);
		mLlayoutOperatePaste.setVisibility(View.GONE);
		
		setMiddleTitle();		
		initRootData();		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "Cloud File List onActivityCreated");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Cloud File List onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Cloud File List onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}
		Log.d(TAG, "Cloud File List onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "Cloud File List onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Cloud File List onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "Cloud File List onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "Cloud File List onStop");
	}
	
	private void setMiddleTitle()
	{
		switch (this.itemId)
		{
		case 0:
			mTvewTitle.setText(getResources().getString(R.string.allfile_fold));
			break;
		case 1:
			mTvewTitle.setText(getResources().getString(R.string.photo));
			break;
		case 2:
			mTvewTitle.setText(getResources().getString(R.string.audio));
			break;
		case 3:
			mTvewTitle.setText(getResources().getString(R.string.video));
			break;
		case 4:
			mTvewTitle.setText(getResources().getString(R.string.bt) );
			break;
		case 5:
			mTvewTitle.setText(getResources().getString(R.string.doc));
			break;
		case 7:
			mTvewTitle.setText(getResources().getString(R.string.group));
			break;
		case 8:
			mTvewTitle.setText(getResources().getString(R.string.my_share));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化 
	 */
	private void initRootData()
	{		
		initLoadingView();		
		adapter = new AllFileDataAdapter(mActivity,allfileListData,handler);
//		adapter = new AllFileDataAdapter(mActivity);
//		mListView.addFooterView(loadingLayout);
		mListView.setAdapter(adapter);
	    mListView.setOnScrollListener(new mOnScrollListener());
	    mListView.setOnItemClickListener(new ItemClickListener());
	    mListView.setOnItemLongClickListener(new ListViewOnLongItemClickListener());
	    mBtnSale.setOnClickListener(operateOnClickListener);
	    mBtnSave.setOnClickListener(operateOnClickListener);
	    mBtnMove.setOnClickListener(operateOnClickListener);
	    mTvewSetting.setOnClickListener(operateOnClickListener);
	    mTvewBack.setOnClickListener(operateOnClickListener);
	    mTvewChooseAll.setOnClickListener(operateOnClickListener);
	    mBtnCopy.setOnClickListener(operateOnClickListener);
	    mBtnRename.setOnClickListener(operateOnClickListener);
	    mBtnDel.setOnClickListener(operateOnClickListener);
	    mBtnShare.setOnClickListener(operateOnClickListener);
	    mBtnPaste.setOnClickListener(operateOnClickListener);
	    mBtnCancel.setOnClickListener(operateOnClickListener);
	    
	    mImgPlayVideo.setOnClickListener(operateOnClickListener);
	}
	
	
	/**
	 * 下拉刷新布局
	 */
	private void initLoadingView()
	{
//		LinearLayout layout = new LinearLayout(mActivity);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//		progressBar = new ProgressBar(mActivity);
//		progressBar.setPadding(0, 0, 15, 0);
//		layout.addView(progressBar,mLayoutParams);
//		
//		loadingLayout = new LinearLayout(mActivity);
//		loadingLayout.addView(layout,mLayoutParams);
//		loadingLayout.setGravity(Gravity.CENTER);
		
		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("正在请求，请稍后...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
	}
	

	String folderId;
	String parentFolderId;
	/**
	 * 发送请求 获得数据
	 * @param offset 起始数据位置
	 * @param maxsize 数据最大个数
	 */
	public void getAllFileData(String id,String token,int itemId,int offset,int maxsize, String folderId,String parentFolderId)
	{
		if (TextUtils.isEmpty(folderId))
		{
			mHashRequestId.put("root", "CloudFragment");
		}
		if (isFirstShown)
		{
			Log.d("mating","getFileData first");
			allfileListData.clear();
		}
	
		parentFolderId = mHashRequestId.get(folderId);
		
		CloudFileListRequestData data = new CloudFileListRequestData();
		this.folderId = folderId;	
		this.parentFolderId = parentFolderId;	
		this.itemId = itemId;
		Log.d(TAG,"request folderId = " + folderId + "; parentFolderId = " + parentFolderId);

		mRequest.clear();
		//请求服务器数据，用户等待
		data.setBegin(String.valueOf(offset));
		data.setSize(String.valueOf(maxsize));
		data.setFolder_id(folderId);
//		data.setOrder("");
//		data.setOrder_by("");
		Log.d("mating","getReqType = " + getReqType());
		data.setReqType(getReqType());
		data.setToken(token);
		
		data.setUserId(id);
		mRequest.add(data);
		
		CloudAllfileListHttp allFileHttp = new CloudAllfileListHttp(handler,mRequest,allfileListData);
		allFileHttp.getAllfileRequest();

		
	}
	
	/**
	 * 保存 folderId等信息
	 * @param currentFolderId
	 * @param parentFolderId
	 * @param type
	 */
	public void savePathAndParentPath(String currentFolderId,String parentFolderId) {
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PATH, currentFolderId).commit();
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_CLOUD_PARENT_PATH, parentFolderId).commit();
	}
	
	/**
	 * 获取请求类型，如 全部文件，图片等
	 * @return
	 */
	private String getReqType()
	{
		String reqType = "";
		switch (itemId)
		{
		case 0:
			reqType = BaseUrl.LIST_ALLFILE;
			
			break;
		case 1:
			reqType = BaseUrl.LIST_IMAGE;
			break;
		case 2:
			reqType = BaseUrl.LIST_MUSIC;
			break;
		case 3:
			reqType = BaseUrl.LIST_VIDEO;
			break;
		case 4:
			reqType = BaseUrl.LIST_BT;
			break;
		case 5:
			reqType = BaseUrl.LIST_DOC;
			break;
		case 7:
			reqType = BaseUrl.LIST_GROUP;
			break;
		case 8:
			reqType = BaseUrl.LIST_SHARE;
			break;
		default:
				break;
		}
		return reqType;
	}

	/**
	 * listview滚动事件
	 * @author Administrator
	 *
	 */
	private class mOnScrollListener implements OnScrollListener
	{
		@Override
		public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			 Log.d("mating","onScroll--->totalItemCount = " + totalItemCount);
			 Log.d("mating","onScroll--->firstVisibleItem = " + firstVisibleItem);
			 Log.d("mating","onScroll--->visibleItemCount = " + visibleItemCount);

			 lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			 Log.d("mating","onScroll--->lastVisibleIndex = " + lastVisibleIndex);
			}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int scrollState) {
			// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
			 Log.d("mating","onScrollStateChanged--->adapter.getCount() = " + adapter.getCount());
			  if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
		                && lastVisibleIndex == adapter.getCount() - 1) 
			  {
				  Log.d("mating","onScrollStateChanged--->mList.size() = " + allfileListData.size());
//				  mListView.addFooterView(loadingLayout);		
				  //获取当前的folderId 
				  mProgressDialog.show();
				  String id = idToken.getString(CommonUtil.USER_ID,"");
				  String token = idToken.getString(CommonUtil.USER_TOKEN, "");
				  isFirstShown = false;
				  getAllFileData(id,token,itemId,allfileListData.size(),PER_SIZE,null,null);
			  }			
		}		
	}

	
	private int clickOrNot()
	{
		int count = 0;
		for(int i = 0; i < mCheckedHash.size(); i++)
		{
			if (mCheckedHash.get(i))
			{
				count++;
			}
			
		}
		return count;
	}
	
	private OnClickListener operateOnClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{			
			switch (v.getId())
			{
				case R.id.back:
				{
					mrlayoutNormal.setVisibility(View.VISIBLE);
					mrlayoutEdit.setVisibility(View.GONE);
					mLlayoutOperateFileFolder.setVisibility(View.GONE);	
					mLlayoutOperatePaste.setVisibility(View.GONE);
					if (!adapter.checkIsShown)
					{
						adapter.checkIsShown = true;
					} 				    
					adapter.notifyDataSetChanged();
					setCheckboxCheckedOrNot(allfileListData);
					mTvewNumber.setText(String.valueOf(0));
					break;
				}
				case R.id.upload_or_chooseall:
				{
					if (!isAllChoose)
					{
						for (int i = 0; i < allfileListData.size(); i++)
						{
							if (!mCheckedHash.get(i))
							{
								mCheckedHash.put(i, true);
							}						
						}
						isAllChoose = true;
						mTvewNumber.setText(String.valueOf(allfileListData.size()));
					}
					else
					{
						for (int i = 0; i < allfileListData.size(); i++)
						{
							if (mCheckedHash.get(i))
							{
								mCheckedHash.put(i, false);
							}
						}
						isAllChoose = false;
						mTvewNumber.setText(String.valueOf(0));
					}
					adapter.notifyDataSetChanged();
					break;
				}
				case R.id.setting:
				{
					((MainCloudActivity)mActivity).toggle();
					break;
				}
				case R.id.exit:
				{
					mActivity.finish();
					break;
				}
				case R.id.cloud_filelist_play_video:
				{
					Intent intent = new Intent();
					intent.setClass(mActivity, VideoPlayActivity.class);
					startActivity(intent);
					break;
				}
				
				case R.id.phonelist_save:
				case R.id.phonelist_move:
				case R.id.phonelist_share:
				case R.id.phonelist_sale:
				{
					showToast("该功能还未开放，敬请期待！");
					break;
				}
				case R.id.phonelist_copy:
				{
					if (0 != itemId)
					{
						showToast("对不起，此处不能进行该操作，请回到“全部文件”处进行操作，谢谢！");
						return;
					}
					if (0 == clickOrNot())
					{
						showToast("您尚未选择需要复制的文件(夹)。。请选择");
						return;
					}
					if (!adapter.checkIsShown)
					{
						adapter.checkIsShown = true;
					}	
				    adapter.notifyDataSetChanged();
					mLlayoutOperatePaste.setVisibility(View.VISIBLE);
					mLlayoutOperateFileFolder.setVisibility(View.GONE);
					copyFileOrFolder();
					
					break;
				}
				case R.id.phonelist_del:
				{
					if (0 == clickOrNot())
					{
						showToast("您尚未选择需要删除的文件(夹)。。请选择");
						return;
					}					
					deleteFileOrFolder();
					completeOperateFile();
					break;
				}
				case R.id.phonelist_rename:
				{
					if (0 == clickOrNot() )
					{
						showToast("您尚未选择需要重命名的文件(夹)。。请选择");
						return;
					}
					if (1 != clickOrNot())					{
						
						showToast("对不起，不能对多个文件(夹)同时进行重命名操作。。");
						return;
					}
					renameFileOrFolder();	
					completeOperateFile();
					break;
				}

				case R.id.operate_paste:
				{
					if (TextUtils.isEmpty(mStrDestCopyFolderId))
					{
						showToast("您尚未选择将文件放在哪里");
						return;
					}
					pasteFileFolder();
					completeOperateFile();
					break;
				}
				case R.id.operate_cancel:
				{
					mrlayoutNormal.setVisibility(View.VISIBLE);
					mrlayoutEdit.setVisibility(View.GONE);
					mLlayoutOperateFileFolder.setVisibility(View.GONE);
					mLlayoutOperatePaste.setVisibility(View.GONE);
					if (adapter.checkIsShown)
					{
						adapter.checkIsShown = true;
					}	
					adapter.notifyDataSetChanged();
					setCheckboxCheckedOrNot(allfileListData);
					mTvewNumber.setText(String.valueOf(0));
					break;
				}
				default:
					break;			
			}
		}
		
	};
	private void setCheckboxCheckedOrNot(List<AllFileDetailData> list)
	{
	    for (int i = 0; i < list.size(); i++)
	    {	    	
	    	mCheckedHash.put(i,false);
	    }
		adapter.refreshFileList(list);
	}
	
	private void completeOperateFile()
	{
		setCheckboxCheckedOrNot(allfileListData);
		if (!adapter.checkIsShown)
		{
			adapter.checkIsShown = true;
		}
		adapter.notifyDataSetChanged();
		mrlayoutNormal.setVisibility(View.VISIBLE);
		mrlayoutEdit.setVisibility(View.GONE);
		mLlayoutOperateFileFolder.setVisibility(View.GONE);
		mLlayoutOperatePaste.setVisibility(View.GONE);
	}
	
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
	
	private String mStrDestCopyFolderId = null;
	private String mStrDestCopyFolderParentId = null;
	private HashMap<String,Object> mCopyRequest = new HashMap<String,Object>(); 
	private void copyFileOrFolder()
	{
	    String id = idToken.getString(CommonUtil.USER_ID,"");
	    String token = idToken.getString(CommonUtil.USER_TOKEN, "");
		mCopyRequest.put("user_id", id);
		mCopyRequest.put("token", token);
		List<String> filefolderids = new ArrayList<String>();
		for (int i = 0; i < mCheckedHash.size(); i++)
		{
			if (mCheckedHash.get(i))
			{
				String copyFileId = allfileListData.get(i).getId();
				filefolderids.add(copyFileId);
			}
		}
		mCopyRequest.put("originalIds", filefolderids);
		
		showToast("请选择目的文件夹");
	}

	private void pasteFileFolder()
	{
		mCopyRequest.put("destIds", mStrDestCopyFolderId);
		
		CopyFileFolderHttp copyHttp = new CopyFileFolderHttp(handler,mCopyRequest);
		copyHttp.copyFileFolderThread();
	}
	
	
	private void deleteFileOrFolder()
	{
	    String id = idToken.getString(CommonUtil.USER_ID,"");
	    String token = idToken.getString(CommonUtil.USER_TOKEN, "");
		HashMap<String,Object> request = new HashMap<String,Object>();
		request.put("user_id", id);
		request.put("token", token);
		List<String> listFileFolderIds = new ArrayList<String>();
		for (int i = 0; i < mCheckedHash.size(); i++)
		{
			if (mCheckedHash.get(i))
			{
				String delFileId = allfileListData.get(i).getId();
				mStrFolderId = allfileListData.get(i).getParentId();
				listFileFolderIds.add(delFileId);			
			}
		}		
		request.put("filefolderId", listFileFolderIds);
		
		DeleteFileFolderHttp deleteHttp = new DeleteFileFolderHttp(handler, request);
		deleteHttp.deleteThread();
	}
	
	
	/**重命名之后的新名字*/
	private String mStrRenameNewName;
	/**待重命名的文件或文件夹所在文件夹的id*/
	private String mStrFolderId;
	private void renameFileOrFolder()
	{
	    final String id = idToken.getString(CommonUtil.USER_ID,"");
	    final String token = idToken.getString(CommonUtil.USER_TOKEN, "");

		for (int i = 0; i < mCheckedHash.size(); i++)
		{
			if (mCheckedHash.get(i))
			{
				final String OriginalFileId = allfileListData.get(i).getId();
				mStrFolderId = allfileListData.get(i).getParentId();
				String name = allfileListData.get(i).getName();
				
				LinearLayout layout = (LinearLayout)mActivity.getLayoutInflater().inflate(R.layout.rename,null);
				final EditText editRename = (EditText)layout.findViewById(R.id.operate_edit_rename);
				editRename.setText(name);
				
				new AlertDialog.Builder(mActivity).setTitle(getResources().getString(R.string.rename))
				.setView(layout)
				.setPositiveButton(getResources().getString(R.string.sure_ok), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						mStrRenameNewName = editRename.getText().toString();
						HashMap<String,String> request = new HashMap<String,String>();
						request.put("user_id", id);
						request.put("token", token);
						request.put("fileId", OriginalFileId);
						request.put("newname",mStrRenameNewName);

						RenameFileFolderHttp renameHttp = new RenameFileFolderHttp(handler, request);
						renameHttp.renameFileFolderThread();
						
					}					
				})
				.setNegativeButton(getResources().getString(R.string.list_cancle), new DialogInterface.OnClickListener()
				{					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				}).show();						
				
			}
		}
	}
	/**
	 * listview item 项点击事件
	 * @author Administrator
	 *
	 */
	private class ItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (mLlayoutOperateFileFolder.isShown())
			{
				if (mCheckedHash.get(arg2))
				{
					mCheckedHash.put(arg2,false);
				}
				else
				{
					mCheckedHash.put(arg2,true);
				}
				adapter.notifyDataSetChanged();
				
				Message msg = new Message();
				msg.what = MessageInfo.FILE_OPERATE;
				msg.obj = mCheckedHash;
				handler.sendMessage(msg);
			}
			else
			{
				itemClick(arg2);
			}
		}		
	}
	/**打开文件时的等待框*/
	private ProgressDialog waitDialog = null;
	/**用于返回上一层 ---保存当前id和parentId*/
	private HashMap<String,String> mHashRequestId = new HashMap<String,String>();
	/**判断是否是滚动获取的数据：true为刚进入文件夹，false为滚动显示的数据*/
	private boolean isFirstShown = false;
	/**
	 * 打开下一级文件夹
	 * @param arg2
	 */
	private void itemClick(int arg2)
	{
		String type = allfileListData.get(arg2).getType();
		String path = allfileListData.get(arg2).getPath();//网络存储路径
		Log.d("mating","item click type = " + type + "; path = " + path);
		String fId = allfileListData.get(arg2).getId();
		Log.d("mating","fileId = " + fId);
		if (type.equals("folder"))
		{			
			allfileListData.get(arg2).getPath();
			String parentFolderId = allfileListData.get(arg2).getParentId();
			if (mLlayoutOperatePaste.isShown())
			{
				mStrDestCopyFolderId = fId;
				mStrDestCopyFolderParentId = parentFolderId;
			}
			mHashRequestId.put(fId, parentFolderId);
			
			Log.d("mating","item click folderId = " + fId + "; mHashRequestId = " + mHashRequestId);
			allfileListData.clear();			
			String id = idToken.getString(CommonUtil.USER_ID,"");
			String token = idToken.getString(CommonUtil.USER_TOKEN, "");
			isFirstShown = true;
			getAllFileData(id,token,itemId,0,PER_SIZE,fId,parentFolderId);
		}
		else
		{	
			waitDialog = new ProgressDialog(mActivity); 
			waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waitDialog.setMessage("正在请求，请稍后...");
			waitDialog.setCancelable(true);
			waitDialog.setIndeterminate(false);
			waitDialog.show();
			
			String fileBeyond = CommonUtil.getOpenFileType(type);
			SharedPreferences idToken = mActivity.getSharedPreferences(CommonUtil.NEED_TOKENID,0);
			String id = idToken.getString(CommonUtil.USER_ID,"");
			String token = idToken.getString(CommonUtil.USER_TOKEN, "");
			HashMap<String,String> fileOpenRequest = new HashMap<String,String>();
			
			fileOpenRequest.put("id", id);
			fileOpenRequest.put("token", token);
			fileOpenRequest.put("fileid", fId);
			fileOpenRequest.put("name", allfileListData.get(arg2).getName());
			
			Log.d("mating","click id = " + id 
					+ " ; token = " + token + ";fileId = " + fId + "; name = " 
					+ allfileListData.get(arg2).getName());
			 if (fileBeyond.equals(BaseUrl.LIST_IMAGE))
			 {
				 fileOpenRequest.put("type", BaseUrl.LIST_IMAGE);
			 }
			// 要么就直接下载后打开 要么就直接打开网络上的数据
			 else if (fileBeyond.equals(BaseUrl.LIST_MUSIC))
			 {
				 fileOpenRequest.put("type", BaseUrl.LIST_MUSIC);

			 }
			 else if(fileBeyond.equals(BaseUrl.LIST_VIDEO))
			 {
				 fileOpenRequest.put("type", BaseUrl.LIST_VIDEO);
			 }
			 else if(fileBeyond.equals(BaseUrl.LIST_DOC))
			 {
				 fileOpenRequest.put("type", BaseUrl.LIST_DOC);
			 }
			 else
			 {
				 fileOpenRequest.put("type", BaseUrl.LIST_OTHER);
			 }
			DownAndOpenFileHttp  down = new DownAndOpenFileHttp(handler,fileOpenRequest);
			down.downFile();				 
		}
	}
	
		
	/**
	 * listview 长按事件
	 * @author Administrator
	 *
	 */
	private class ListViewOnLongItemClickListener implements OnItemLongClickListener
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3)
		{
			if (mLlayoutOperateFileFolder.isShown())
			{
				mrlayoutNormal.setVisibility(View.VISIBLE);
				mrlayoutEdit.setVisibility(View.GONE);
				mLlayoutOperateFileFolder.setVisibility(View.GONE);
				mLlayoutOperatePaste.setVisibility(View.GONE);
				setCheckboxCheckedOrNot(allfileListData);
				mTvewNumber.setText(String.valueOf(0));
			}
			else
			{
				mrlayoutNormal.setVisibility(View.GONE);
				mrlayoutEdit.setVisibility(View.VISIBLE);
				mLlayoutOperateFileFolder.setVisibility(View.VISIBLE);
				mLlayoutOperatePaste.setVisibility(View.GONE);
			}						
			if (adapter.checkIsShown)
			{
				adapter.checkIsShown = false;
			}
			else
			{
				adapter.checkIsShown = true;
			}	 
		    adapter.notifyDataSetChanged();
			return true;
		}
		
	}
	
	private Handler handler = new Handler()
	{		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what)
			{
				case MessageInfo.ALLFILE:
				{
					if (3 == itemId)
					{
						mImgPlayVideo.setVisibility(View.VISIBLE);
					}
					else
					{
						mImgPlayVideo.setVisibility(View.GONE);
					}
					switch (msg.arg1)
					{						
						case MessageInfo.MESSAGE_SUCCESS:
						{
							//用户等待结束，展示数据
							mListView.setVisibility(View.VISIBLE);
							mTvewNull.setVisibility(View.GONE);
							
//							mListView.removeFooterView(loadingLayout);
							
							/**初始化 checkbox是否被选中*/
							setCheckboxCheckedOrNot(allfileListData);
									
							//保存 folderid与 parentId
							if (TextUtils.isEmpty(folderId) || folderId.equals("root"))
							{								
								savePathAndParentPath("nullcontent","CloudFragment");
								sessionStatusPreference.edit().putInt(CenterFragment2.SESSION_CLOUD_FILE_TPYE, CloudFileListFragment.this.itemId).commit();
								Log.d(TAG, "MESSAGE_SUCCESS，且当前显示为根目录下文件显示");
							}
							else
							{
								savePathAndParentPath(folderId,parentFolderId);
								Log.d(TAG, "MESSAGE_SUCCESS，且当前显示为子目录文件显示");
							}
							mProgressDialog.dismiss();	//先刷新界面，后取消dialog
							break;
						}
						case MessageInfo.MESSAGE_FAIL:
						{
							mProgressDialog.dismiss();
//							mListView.removeFooterView(loadingLayout);
							if (0 != msg.arg2)
							{
								mTvewNull.setVisibility(View.VISIBLE);
								mListView.setVisibility(View.GONE);
								mTvewNull.setText("此功能暂未开放，敬请期待~~");
							}
							
							if (TextUtils.isEmpty(folderId) || folderId.equals("root"))
							{								
								savePathAndParentPath("nullcontent","CloudFragment");
								sessionStatusPreference.edit().putInt(CenterFragment2.SESSION_CLOUD_FILE_TPYE, CloudFileListFragment.this.itemId).commit();
								Log.d(TAG, "MESSAGE_FAIL，且当前显示为根目录下文件显示");
							}
							else
							{
								savePathAndParentPath(folderId,parentFolderId);
								Log.d(TAG, "MESSAGE_FAIL，且当前显示为子目录文件显示");
							}
							break;
						}
						case MessageInfo.MESSAGE_SUCCESS_NULL:
						{
//							mListView.removeFooterView(loadingLayout);
							mTvewNull.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
							if (TextUtils.isEmpty(folderId) || folderId.equals("root"))
							{								
								savePathAndParentPath("nullcontent","CloudFragment");
								sessionStatusPreference.edit().putInt(CenterFragment2.SESSION_CLOUD_FILE_TPYE, CloudFileListFragment.this.itemId).commit();
								Log.d(TAG, "MESSAGE_SUCCESS_NULL，且当前显示根目录数据为空");
							}
							else
							{
								savePathAndParentPath(folderId,parentFolderId);
								Log.d(TAG, "MESSAGE_SUCCESS_NULL，且当前显示为子目录数据为空");
							}
							mProgressDialog.dismiss();
							break;
						}
						case MessageInfo.MESSAGE_SUCCESS_END:
						{
							mListView.setOnScrollListener(null);
							mProgressDialog.dismiss();
//							mListView.removeFooterView(loadingLayout);
//							Toast.makeText(getActivity(), "there is none data", Toast.LENGTH_SHORT).show();
							break;
						}
//						case MessageInfo.ALLFILE_TOKEN_WRONG:
//						{
////							mListView.removeFooterView(loadingLayout);
//							Toast.makeText(mActivity, "认证错误，请重新登录。。", Toast.LENGTH_SHORT).show();
//							break;
//						}
						default:
//							mListView.removeFooterView(loadingLayout);
							mTvewNull.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
							mTvewNull.setText("此功能暂未开放，敬请期待~~");
							mProgressDialog.dismiss();
							break;
					}
					break;
				}
				case MessageInfo.FILE_OPERATE:
				{
					int iChecked = 0;
//					Log.d("mating","handler mCjeckedHash = " + mCheckedHash);
					for (int i = 0; i < mCheckedHash.size(); i++)
					{
						if (mCheckedHash.get(i))
						{
							iChecked++;
						}
					}
					mTvewNumber.setText(String.valueOf(iChecked));
						
//					if (1 != iChecked)
//					{
//						mBtnRename.setEnabled(false);
//						mBtnRename.setTextColor(getResources().getColor(R.color.fonts_operate_unused));
//					}					
					break;
				}
				case MessageInfo.DOWN_OPEN_FILE:
				{
					waitDialog.setMessage("下载结束！正在打开文件。。");
					waitDialog.show();
					String path = (String)msg.obj;
					Log.d("mating","CloudFileList open file path = " + path);
//					Toast.makeText(mActivity, "OK", Toast.LENGTH_SHORT).show();
					Intent i = CommonUtil.openFile(path);
					waitDialog.dismiss();
					try {
						if (i != null) {
							mActivity.startActivity(i);
						}else{
							showToast("打开文件失败，请重试...");
							Log.d(TAG, "cloud open file intent is null");
						}
					} catch (Exception e) {
						e.printStackTrace();
						showToast("打开文件失败，请重试...");
						Log.d(TAG, "cloud open file intent is null");
					}
					break;
				}
				case MessageInfo.DOWN_OPEN_FILE_ERROE:
				{
					waitDialog.dismiss();
					Toast.makeText(mActivity, "下载失败，请检查是否网络问题。。", Toast.LENGTH_SHORT).show();					
					break;
				}
				case MessageInfo.RENAME_RESPONSE:
				{
					switch (msg.arg1)
					{
						case MessageInfo.MESSAGE_SUCCESS:
						{
						    String id = idToken.getString(CommonUtil.USER_ID,"");
						    String token = idToken.getString(CommonUtil.USER_TOKEN, "");
							AllFileDetailData fileData = (AllFileDetailData)msg.obj;
							if (mStrRenameNewName.equals(fileData.getName()))
							{
								Toast.makeText(mActivity, "重命名成功", Toast.LENGTH_SHORT).show();
								String parent = fileData.getParentId();
								isFirstShown = true;
								getAllFileData(id, token, itemId, 0, PER_SIZE, mStrFolderId, parent);
							}
							break;
						}
						case MessageInfo.MESSAGE_FAIL:
						{
							Toast.makeText(mActivity, "重命名失败", Toast.LENGTH_SHORT).show();
							break;
						}
						default:
							break;
					}			
					break;
				}
				case MessageInfo.DELETE_RESPONSE:
				{
					switch(msg.arg1)
					{
						case MessageInfo.MESSAGE_SUCCESS:
						{
							showToast("删除成功");
						    String id = idToken.getString(CommonUtil.USER_ID,"");
						    String token = idToken.getString(CommonUtil.USER_TOKEN, "");
							AllFileDetailData fileData = (AllFileDetailData)msg.obj;
							String parent = fileData.getParentId();
							isFirstShown = true;
							
							getAllFileData(id, token, itemId, 0, PER_SIZE, mStrFolderId, parent);
							mTvewNumber.setText(String.valueOf(0));
							break;
						}
						case MessageInfo.MESSAGE_FAIL:
						{
							showToast("删除失败");
							break;
						}
						default:
							break;					
					}
					break;
				}
				case MessageInfo.COPY_RESPONSE:
				{
					Log.d("mating copy", "msg.arg1 = " + msg.arg1);
					switch (msg.arg1)
					{					
						case MessageInfo.MESSAGE_SUCCESS:
						{
							showToast("复制成功");
						    String id = idToken.getString(CommonUtil.USER_ID,"");
						    String token = idToken.getString(CommonUtil.USER_TOKEN, "");
							isFirstShown = true;
							getAllFileData(id, token, itemId, 0, PER_SIZE, mStrDestCopyFolderId, mStrDestCopyFolderParentId);
							mTvewNumber.setText(String.valueOf(0));
							break;
						}
						case MessageInfo.MESSAGE_FAIL_UNDO:
						{
							showToast("对不起，该功能暂时还未开通，敬请期待！");
							break;
						}
						case MessageInfo.MESSAGE_FAIL:
						{
							showToast("复制失败");
							break;
						}
						default:
							break;
					}
					mCopyRequest.clear();
					break;
				}
				case MessageInfo.IMAGE_THUMBNAILS:
				{
					Log.d(TAG,"handler thumbnails");
			         Thumbnails thumb = (Thumbnails) msg.obj;
	                 ImageView img = thumb.getImageView();
                     if (thumb.getUrl().equals(img.getTag()))
                     {
                    	 Bitmap bitmap = thumb.getBitmap();
                    	 if (null != bitmap)
                    	 {
                    		 img.setImageBitmap(bitmap);
                    	 }
                    	 else
                    	 {
                    		 img.setBackgroundResource(R.drawable.photo_icon);
                    	 }
                     }
					break;
				}
				default:
					break;
			}
		}
	};
	
	private class AllFileDataAdapter extends BaseAdapter
	{
		/** 用来响应获取缩略图成功的消息 */
		private Handler mHandler;
		
		LayoutInflater layoutInflater = null;
	    List<AllFileDetailData> mList = null;
		ViewHolder holder = null; 
	    /**判断 checkbox是否显示 默认为不显示*/
	    public boolean checkIsShown = true;
	    
	    public AllFileDataAdapter(Context context,List<AllFileDetailData> list,Handler handler)
	    {
	        layoutInflater = LayoutInflater.from(context);
	        mHandler = handler;
	    	mList = list;
	    }
		@Override
		public int getCount() {
			return mList.size();
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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
		
			if (arg1 == null)
			{
				arg1 = layoutInflater.inflate(R.layout.allfile_item, null);
				holder = new ViewHolder(); 
				holder.img = (ImageView)arg1.findViewById(R.id.img_allfile);
				holder.tvewName = (TextView)arg1.findViewById(R.id.tvew_allfile_title);
				holder.tvewTime = (TextView)arg1.findViewById(R.id.tvew_all_file_time);
				holder.tvewSize = (TextView)arg1.findViewById(R.id.tvew_all_file_size);
				holder.check = (CheckBox)arg1.findViewById(R.id.check_allfile_choose);
				
				holder.listener = new CheckBoxOnCheckChangedListener();				
				holder.check.setOnCheckedChangeListener(holder.listener);
				arg1.setTag(holder);  
			}
			else
			{
				holder = (ViewHolder) arg1.getTag();  
			}
			if(checkIsShown)
			{
				holder.check.setVisibility(View.GONE);
			}
			else
			{
				holder.check.setVisibility(View.VISIBLE);
			}
			
			holder.listener.mPosition = arg0;
			
			holder.check.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View v) {
					CheckBox checkbox = (CheckBox)v;
					mCheckedHash.put(arg0, checkbox.isChecked());
					Message msg = new Message();
					msg.what = MessageInfo.FILE_OPERATE;
					handler.sendMessage(msg);
				}
			});
			
			if (mCheckedHash.size() > 0)
			{
				holder.check.setChecked(mCheckedHash.get(arg0));
			}
			String type = mList.get(arg0).getType();
			String fileBeyond = CommonUtil.getOpenFileType(type);
			 if ("folder".equals(type))
			 {
				 holder.img.setImageResource(R.drawable.folder);
			 }
			 else if (fileBeyond.equals(BaseUrl.LIST_IMAGE))
			 {
				 String metaid = mList.get(arg0).getMetaId();
				 String metaFolder = mList.get(arg0).getMetaFolder();
				 String url = BaseUrl.IMAGE_BASE_URL + "id=" + metaFolder + "/" + metaid + "&type=img&size=thumb";
				 holder.img.setTag(url);
				 
				 GetImageThumbnailHttp getImageHttp = new GetImageThumbnailHttp(mHandler,holder.img,url);
				 getImageHttp.getImageBitmap();
			 }
			 else if (fileBeyond.equals(BaseUrl.LIST_MUSIC))
			 {
				 holder.img.setImageResource(R.drawable.music_icon);
			 }
			 else if (fileBeyond.equals(BaseUrl.LIST_VIDEO))
			 {
				 holder.img.setImageResource(R.drawable.video_icon);
			 }
			 else if (fileBeyond.equals(BaseUrl.LIST_DOC))
			 {
				 holder.img.setImageResource(R.drawable.doc_icon);
			 }
			 else
			 {
				 holder.img.setImageResource(R.drawable.other_icon);
			 }
			 holder.tvewName.setText(mList.get(arg0).getName());
			 holder.tvewTime.setText(mList.get(arg0).getLastDate());
			 holder.tvewSize.setText(mList.get(arg0).getSize());
			 
			 
			return arg1;
		}
		
		class ViewHolder
		{
			ImageView img;
			TextView tvewName;
			TextView tvewTime;
			TextView tvewSize;
			CheckBox check;
			
			CheckBoxOnCheckChangedListener listener;
			
		}
		private class CheckBoxOnCheckChangedListener implements OnCheckedChangeListener
		{
			private int mPosition = 0;
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) 
			{
				if (arg1)
				{
					mCheckedHash.put(mPosition, true);
					
				}
				else
				{
					mCheckedHash.put(mPosition, false);
				}						
			}
		}
		
        public void refreshFileList(List<AllFileDetailData> list)
        {
            Log.d("mating","refreshList list = null is "  + list.equals(null));
            if(null != list)
            {
                Log.d("mating","data changed" );
                mList = list;
                notifyDataSetChanged();
            }
        }		
	}
	
	public class ExitOnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			mActivity.finish();
		}
		
	}
}
