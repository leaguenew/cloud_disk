package com.echoii.activity.fragment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import android.app.ProgressDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.echoii.activity.UploadPhotoActivity;
import com.echoii.activity.VideoPlayActivity;
import com.echoii.bean.cloudfilelist.AllFileDetailData;
import com.echoii.bean.cloudfilelist.Thumbnails;
import com.echoii.constant.CommonUtil;
import com.echoii.constant.MessageInfo;

public class PhoneFileListFragment  extends Fragment{
	public static final String TAG = "PhoneFileListFragment";
	
	private TextView mTvewNull;
	private ImageView mImgVideoPlay;
	private ListView mListView;
	
	/**文件操作相关---start*/
	private  Button mBtnSale;
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
	/**文件操作相关---end*/
	
	/**标题显示相关---start*/
	private RelativeLayout mrlayoutEdit;
	private TextView mTvewBack;
	private TextView mTvewNumber;
	private TextView mTvewChooseAll;	
	private RelativeLayout mrlayoutNormal;
	private ImageView mTvewSetting;
	private TextView mTvewTitle;
	/**标题显示相关---end*/
	
	/**适配器*/
	private AllFileDataAdapter adapter;
	/**判断是否全选  */
	private boolean isAllChoose = false;
	private List<AllFileDetailData> mList = new ArrayList<AllFileDetailData>();	
	/**存储checkbox是否选中*/
	private HashMap<Integer,Boolean> mCheckedHash = new HashMap<Integer,Boolean>();
	
	/**当前是出于哪个具体文件下的标记  即 音乐 图片等*/
	private int itemId = 0;
	private String currentPath ;
	
	private Activity mActivity;
	private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
//	private ProgressBar scanProgress;
	

	/**待复制文件路径*/
	private List<String> mCopyFrom = new ArrayList<String>();
	/**待复制文件名字*/
	private List<String> mCopyFromName = new ArrayList<String>();
	
	private ImageView mTakePhotoImage;
	private String mFileName;
	private Bitmap bm = null;
	private SharedPreferences userPreferences;
	private ProgressDialog mProgressDialog = null;
	
	public PhoneFileListFragment(){}
	
	public PhoneFileListFragment(int id,String currentPath){
		this.itemId = id;
		this.currentPath = currentPath;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.phone_filelist, null);
		mImgVideoPlay = (ImageView)view.findViewById(R.id.cloud_filelist_play_video);
		mTvewNull = (TextView)view.findViewById(R.id.phone_null);
		mListView = (ListView)view.findViewById(R.id.listview_phone_filelist);
//		rLayoutOperate = (RelativeLayout)view.findViewById(R.id.phonelist_operate);
		mBtnSale = (Button)view.findViewById(R.id.phonelist_sale);
		mBtnSave = (Button)view.findViewById(R.id.phonelist_save);
		mBtnMove = (Button)view.findViewById(R.id.phonelist_move);
		mBtnCopy = (Button)view.findViewById(R.id.phonelist_copy);
		mBtnDel = (Button)view.findViewById(R.id.phonelist_del);
		mBtnRename = (Button)view.findViewById(R.id.phonelist_rename);
		mBtnShare = (Button)view.findViewById(R.id.phonelist_share);
		mBtnPaste = (Button)view.findViewById(R.id.operate_paste);
		mBtnCancel = (Button)view.findViewById(R.id.operate_cancel);
		mLlayoutOperatePaste = (RelativeLayout)view.findViewById(R.id.operate_rlayout_paste);
		mLlayoutOperateFileFolder = (RelativeLayout)view.findViewById(R.id.phonelist_operate_filefolder);
		
		mTvewTitle = (TextView)view.findViewById(R.id.tvew_title);
		mTvewSetting = (ImageView)view.findViewById(R.id.setting);
		mrlayoutNormal = (RelativeLayout)view.findViewById(R.id.layout_title);
		mrlayoutEdit = (RelativeLayout)view.findViewById(R.id.layout_edit);
		mTvewBack = (TextView)view.findViewById(R.id.back);
		mTvewNumber = (TextView)view.findViewById(R.id.number);
		mTvewChooseAll = (TextView)view.findViewById(R.id.upload_or_chooseall);
		
//		scanProgress = (ProgressBar) view.findViewById(R.id.scanProgress);
		
		mrlayoutNormal.setVisibility(View.VISIBLE);
		mrlayoutEdit.setVisibility(View.GONE);
		mLlayoutOperateFileFolder.setVisibility(View.GONE);
		mLlayoutOperatePaste.setVisibility(View.GONE);
		mTakePhotoImage = (ImageView) view.findViewById(R.id.take_photo);
		mTakePhotoImage.setOnClickListener(viewOnClickListener);
		setMiddleTitle();
		initListener();
		return view;
	}
	
	
	private void initListener()
	{
		adapter = new AllFileDataAdapter(mActivity,mList,handler);
		Log.d("mating", "mListView = " + mListView);
		mListView.setAdapter(adapter);		
		
		mListView.setOnItemClickListener(new ListViewItemClickListener());		
		mListView.setOnItemLongClickListener(new ListViewLongItemClickListener());		
		mBtnSave.setOnClickListener(viewOnClickListener);
		mBtnMove.setOnClickListener(viewOnClickListener);
		mBtnCopy.setOnClickListener(viewOnClickListener);
		mBtnDel.setOnClickListener(viewOnClickListener);
		mBtnRename.setOnClickListener(viewOnClickListener);
		mBtnShare.setOnClickListener(viewOnClickListener);
		mBtnSale.setOnClickListener(viewOnClickListener);
		mTvewSetting.setOnClickListener(viewOnClickListener);
		mBtnPaste.setOnClickListener(viewOnClickListener);
		mBtnCancel.setOnClickListener(viewOnClickListener);
		mTvewBack.setOnClickListener(viewOnClickListener);
		mImgVideoPlay.setOnClickListener(viewOnClickListener);
		mTvewChooseAll.setOnClickListener(viewOnClickListener);
	}
	
	/**
	 * 获得手机文件线程
	 * @author Administrator
	 *
	 */
	public class GetFileListDataThread extends Thread{
		List<AllFileDetailData> mFileList = new ArrayList<AllFileDetailData>();	
		private String path ;
		private int itemId;
		public GetFileListDataThread(Context context,String path,int itemId){
			this.path = path;
			this.itemId = itemId;
		}		
		@Override
		public void run()
		{
			super.run();
			File  dir = new File(path);
		    File[] files = dir.listFiles();
		    switch (this.itemId)
		    {
			    case 0:
			    {
			    	for (File f : files)
			    	{
			    		AllFileDetailData data = new AllFileDetailData();
			    		String name = f.getName();
			    		if (!name.startsWith(".",0))
			    		{
			    			data.setName(name); //名字
			    			data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
//			    			data.setSize(CommonUtil.formatFileSize(getFileSize(f))); //大小
//			    			data.setSize(String.valueOf(0));
			    			data.setPath(f.getAbsolutePath());
			    			mFileList.add(data);				    	
			    		}
			    	}
			    	phoneSendMessage();
			    	break;
			    }
			    case 1:
			    {
			    	File file = new File(path);
					File[] otherFiles = file.listFiles();
			    	getImageFiles(otherFiles,mFileList);
			    	phoneSendMessage();
			    	break;
			    }
			    case 2:
			    {
			    	File file = new File(path);
					File[] otherFiles = file.listFiles();
			    	getMusicFiles(otherFiles,mFileList);
			    	phoneSendMessage();
			    	break;
			    }
			    case 3:
			    {
			    	File file = new File(path);
					File[] otherFiles = file.listFiles();
			    	getVideoFiles(otherFiles,mFileList);
			    	phoneSendMessage();
			    	break;
			    }
			    case 4: //BT种子
			    {
			    	File file = new File(path);
					File[] btFiles = file.listFiles();
		    	    getBtFiles(btFiles,mFileList);
		    	    phoneSendMessage();
			    	break;
			    }
			    case 5:
			    {
			    	File file = new File(path);
					File[] docFiles = file.listFiles();
			    	getDocumentFiles(docFiles,mFileList);
			    	phoneSendMessage();
			    	break;
			    }
			    case 7:
			    {
			    	File file = new File(path);
					File[] otherFiles = file.listFiles();
			    	getOtherFils(otherFiles,mFileList);
			    	
			    	phoneSendMessage();
			    	break;
			    }
			    case 8:
			    {
					Message msg = new Message();
		    	    msg.what = MessageInfo.PHONE_LIST;
		    	    msg.arg1 = MessageInfo.PHONE_LIST_SUCCESS_NULL;
		    	    handler.sendMessage(msg);
			    	break;
			    }
			    default:
			    	break;
		    }	
		}
		
		private void phoneSendMessage()
		{
			Message msg = new Message();
    	    msg.what = MessageInfo.PHONE_LIST;
    	    if (mFileList.isEmpty())
    	    {
    	    	msg.arg1 = MessageInfo.PHONE_LIST_SUCCESS_NULL;
    	    }
    	    else
    	    {
    	    	msg.arg1 = MessageInfo.PHONE_LIST_SUCCESS;
    	    	msg.obj = mFileList;
    	    }
    	   
    	    handler.sendMessage(msg);
		}
//		 private long getFileSize(File f) 
//		 {
//		  long size = 0;
//		  if (f.isDirectory())
//		  {
//			  File flist[] = f.listFiles();
//			  for(File flength : flist)
//			  {
//				  if (flength.isDirectory())
//				  {
//					  size += getFileSize(flength);
//				  }
//				  else
//				  {
//					  size += flength.length();
//				  }
//			  }
//		  }
//		  else
//		  {
//			  size += f.length();
//		  }
//		  return size;
//		 } 
	}
	
	private void getImageFiles(File[] files,List<AllFileDetailData> mFileList)
	{
		for (File f : files)
    	{
    		if (f.isFile())
    		{
    			String name = f.getName();
    			if ("image".equals(CommonUtil.getFileType(name)))
    			{
    				AllFileDetailData data = new AllFileDetailData();
    				data.setName(name); //名字
    				data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
			    	data.setSize(CommonUtil.formatFileSize(f.length())); //大小
			    	data.setPath(f.getAbsolutePath());
			    	mFileList.add(data);		    				
    			}
    		}
    		else if (f.isDirectory())
    		{
    			File[] childFiles = f.listFiles();
    			if (childFiles!=null&&childFiles.length>0) {
    				getImageFiles(childFiles,mFileList);
				}
    			
    		}
    	}
	}
	
	
	private void getMusicFiles(File[] files,List<AllFileDetailData> mFileList)
	{
		for (File f : files)
    	{
    		if (f.isFile())
    		{
    			String name = f.getName();
    			if ("music".equals(CommonUtil.getFileType(name)))
    			{
    				AllFileDetailData data = new AllFileDetailData();
    				data.setName(name); //名字
    				data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
			    	data.setSize(CommonUtil.formatFileSize(f.length())); //大小
			    	data.setPath(f.getAbsolutePath());
			    	Log.d("mating","doc f.getAbsolutePath() = " + f.getAbsolutePath());
			    	mFileList.add(data);		    				
    			}
    		}
    		else if (f.isDirectory())
    		{
    			File[] childFiles = f.listFiles();
    			if (childFiles!=null&&childFiles.length>0) {
    				getMusicFiles(childFiles,mFileList);
				}
    			
    		}
    	}
	}
	
	private void getVideoFiles(File[] files,List<AllFileDetailData> mFileList)
	{
		for (File f : files)
    	{
    		if (f.isFile())
    		{
    			String name = f.getName();
    			if ("video".equals(CommonUtil.getFileType(name)))
    			{
    				AllFileDetailData data = new AllFileDetailData();
    				data.setName(name); //名字
    				data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
			    	data.setSize(CommonUtil.formatFileSize(f.length())); //大小
			    	data.setPath(f.getAbsolutePath());
			    	Log.d("mating","doc f.getAbsolutePath() = " + f.getAbsolutePath());
			    	mFileList.add(data);		    				
    			}
    		}
    		else if (f.isDirectory())
    		{
    			File[] childFiles = f.listFiles();
    			if (childFiles!=null&&childFiles.length>0) {
    				getVideoFiles(childFiles,mFileList);
				}
    			
    		}
    	}
	}
	
	private void getBtFiles(File[] files,List<AllFileDetailData> mFileList)
	{
		for (File f: files)
		{
			if (f.isFile())
			{
				String name = f.getName();
				if ("torrent".equals(CommonUtil.getFileType(name)))
				{
    				AllFileDetailData data = new AllFileDetailData();
    				data.setName(name); //名字
    				data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
			    	data.setSize(CommonUtil.formatFileSize(f.length())); //大小
			    	data.setPath(f.getAbsolutePath());
			    	Log.d("mating","bt f.getAbsolutePath() = " + f.getAbsolutePath());
			    	mFileList.add(data);	
				}
			}
    		else if (f.isDirectory())
    		{
    			File[] childFiles = f.listFiles();
    			if (childFiles!=null&&childFiles.length>0) {
    				getBtFiles(childFiles,mFileList);
				}
    			
    		}
		}
		
	}
	/**
	 * 获取“文档”下的文件
	 * @param path
	 * @param mFileList
	 */
	private void getDocumentFiles(File[] files,List<AllFileDetailData> mFileList)
	{
		for (File f : files)
    	{
    		if (f.isFile())
    		{
    			String name = f.getName();
    			if ("document".equals(CommonUtil.getFileType(name)))
    			{
    				AllFileDetailData data = new AllFileDetailData();
    				data.setName(name); //名字
    				data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
			    	data.setSize(CommonUtil.formatFileSize(f.length())); //大小
			    	data.setPath(f.getAbsolutePath());
			    	Log.d("mating","doc f.getAbsolutePath() = " + f.getAbsolutePath());
			    	mFileList.add(data);		    				
    			}
    		}
    		else if (f.isDirectory())
    		{
    			File[] childFiles = f.listFiles();
    			if (childFiles!=null&&childFiles.length>0) {
    				getDocumentFiles(childFiles,mFileList);
				}
    			
    		}
    	}
	}
	
	/**
	 * 获取其他文件
	 * @param files
	 * @param mFileList
	 */
	private void getOtherFils(File[] files,List<AllFileDetailData> mFileList)
	{
    	for (File f : files)
    	{
    		if (f.isFile())
    		{
    			String name = f.getName();
    			if ("others".equals(CommonUtil.getFileType(name)))
    			{
    				AllFileDetailData data = new AllFileDetailData();
    				data.setName(name); //名字
    				data.setLastDate(CommonUtil.transformTime(f.lastModified())); //最后修改时间
			    	data.setSize(CommonUtil.formatFileSize(f.length())); //大小
			    	data.setPath(f.getAbsolutePath());
			    	mFileList.add(data);		    				
    			}
    		}
    		else if (f.isDirectory())
    		{
    			File[] childFiles = f.listFiles();
    			if (childFiles != null && childFiles.length > 0) {
    				getOtherFils(childFiles,mFileList);
				}
    		}
    	}
	}
	
	/**
	 * 获取某个文件夹下的文件
	 * @param path
	 * @param itemId 当前类型下的标识
	 */
	public void getFileListData(final String path,int itemId,Activity activity)
	{		
		currentPath = path;
//		scanProgress.setVisibility(View.VISIBLE);
		mProgressDialog= new ProgressDialog(mActivity);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("正在请求，请稍后...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
		new GetFileListDataThread(activity,path,itemId).start();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Phone list onResume");
		userPreferences = mActivity.getSharedPreferences(CommonUtil.NEED_TOKENID, Context.MODE_PRIVATE);
		getFileListData(currentPath, itemId, mActivity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}
	}

	/**
	 * 初始化checkbox
	 */
	private void setCheckboxCheckedOrNot(List<AllFileDetailData> list)
	{
		Log.d("mating"," setCheckboxCheckedOrNot list.size() = " + list.size());
		mCheckedHash.clear();
	    for (int i = 0; i < list.size(); i++)
	    {	    	
	    	mCheckedHash.put(i,false);
	    }
		Log.d("mating"," setCheckboxCheckedOrNot mCheckedHash.size() = " + mCheckedHash.size());
		adapter.refreshFileList(list);
	}
	
	Handler handler = new Handler()
	{
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what)
			{
				case MessageInfo.PHONE_LIST:
				{
					if (3 == itemId)
					{
						mImgVideoPlay.setVisibility(View.VISIBLE);
					}
					else
					{
						mImgVideoPlay.setVisibility(View.GONE);
					}
					switch (msg.arg1)
					{
						case MessageInfo.PHONE_LIST_SUCCESS_NULL:
						{
							mTvewNull.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
							if (itemId == 8)
							{
								mTvewNull.setText("该功能还未开放，敬请期待！");
							}
							else
							{
								mTvewNull.setText("数据为空");
							}
							break;
						}
						case MessageInfo.PHONE_LIST_SUCCESS:
						{
							mListView.setVisibility(View.VISIBLE);
							mTvewNull.setVisibility(View.GONE);
							mList.clear();
							mList = (ArrayList<AllFileDetailData>)msg.obj;
							setCheckboxCheckedOrNot(mList);
							break;
						}
					}
					if (currentPath.equals("/mnt/sdcard"))
					{
						savePathAndParentPath(currentPath,"PhoneFragment");
					}
					else
					{
						String parentPath = currentPath.substring(0,currentPath.lastIndexOf("/"));
						Log.d("mating","phone-------request path = " + currentPath 
								+ "; parentPath = " + parentPath + "; mCheckedHash = " + mCheckedHash.size());
						savePathAndParentPath(currentPath,parentPath);
					}
					if (mProgressDialog.isShowing())
					{
						mProgressDialog.dismiss();
					}
					break;
				}
				case MessageInfo.FILE_OPERATE:
				{
					int iChecked = 0;
					Log.d("mating","handler mCjeckedHash = " + mCheckedHash);
					for (int i = 0; i < mCheckedHash.size(); i++)
					{
						if (mCheckedHash.get(i))
						{
							iChecked = iChecked + 1;
						}
					}
					mTvewNumber.setText(String.valueOf(iChecked));
					
					break;
				}
				case MessageInfo.PHONE_IMAGE_THUMBNAILS:
				{
			         Thumbnails thumb = (Thumbnails) msg.obj;
	                 ImageView img = thumb.getImageView();
                     if (thumb.getUrl().equals(img.getTag()))
                     {
                        img.setImageBitmap(thumb.getBitmap());
                     }
                     else
                     {
                    	 img.setImageResource(R.drawable.photo_icon);
                     }
                     mProgressDialog.dismiss();
					break;
				}
				case MessageInfo.PHONE_VIDEO_THUMBNAILS:
				{
			         Thumbnails thumb = (Thumbnails) msg.obj;
	                 ImageView img = thumb.getImageView();
	                 if (thumb.getUrl().equals(img.getTag()))
                     {
	                	 Bitmap bitmap = thumb.getBitmap();
	                	 if (null !=  bitmap)
	                	 {
	                		 img.setImageBitmap(bitmap);
	                	 }
	                	 else
	                	 {
	                		 img.setImageResource(R.drawable.video_icon);
	                	 }                	                      
                     }
					break;
				}
				case MessageInfo.COPY_RESPONSE:
				{
					Log.d("mating","MessageInfo.COPY_RESPONSE mProgressDialog.isShowing() = " + mProgressDialog.isShowing());	
					mProgressDialog.dismiss();
					getFileListData(currentPath,itemId, mActivity);
					completeOperateFile();
					mCopyFrom.clear();
					mCopyFromName.clear();
					showToast("复制成功");
					break;
				}
				case MessageInfo.DELETE_RESPONSE:
				{
					
					String oldPath = (String)msg.obj;
					if (0 == itemId)
					{
						getFileListData(oldPath,itemId,mActivity);
					}
					else
					{
						getFileListData("/mnt/sdcard",itemId,mActivity);
					}
					mTvewNumber.setText(String.valueOf(0));	
					completeOperateFile();
//					mProgressDialog.dismiss();
					showToast("删除成功");
					
					break;
				}
				default:
					break;
			}
			sessionStatusPreference.edit().putInt(CenterFragment2.SESSION_PHONE_FILE_TYPE, itemId).commit();
		}
	};
	
	/**
	 * 保存当前路径和其父路径
	 * @param currentFolderId
	 * @param parentFolderId
	 */
	public void savePathAndParentPath(String currentFolderId,String parentFolderId)
	{
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PATH, currentFolderId).commit();
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PARENT_PATH, parentFolderId).commit();
	}
	
	/**
	 * listview单击事件
	 * @author Administrator
	 *
	 */
	private class ListViewItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (mLlayoutOperateFileFolder.isShown())
			{
				Log.d("mating"," itemClick choose item");
				if (mCheckedHash.get(arg2))
				{
					mCheckedHash.put(arg2,false);
				}
				else
				{
					mCheckedHash.put(arg2,true);
				}
				
				Message msg = new Message();
				msg.what = MessageInfo.FILE_OPERATE;
				msg.obj = mCheckedHash;
				handler.sendMessage(msg);
				
				adapter.notifyDataSetChanged();
			}
			else 
			{
				Log.d("mating"," itemClick open file or folder");
				String path = mList.get(arg2).getPath();
				Log.d("mating","phone--- click path = " + path);
				File f = new File(path);
				if (f.isDirectory())
				{
//					if (mLlayoutOperatePaste.isShown())
//					{
//						Log.d("mating","f.getPath = " + f.getPath());
//						mDestPath = f.getPath();
//					}
					mList.clear();
					getFileListData(path,0,mActivity);
				}
				else
				{
					Log.d("mating","f.getParent = " + f.getParent() + "; f.getpath() = " + f.getPath());
					try {
						Intent intent = CommonUtil.openFile(path);
					    startActivityForResult(intent, 1);
					} catch (Exception e) {
						e.printStackTrace();
						showToast("未发现可以打开该文件的工具，请安装后重试。");
					}
				}		
			}
		}		
	}
	
	/**
	 * listview 长按事件
	 * @author Administrator
	 *
	 */
	private class ListViewLongItemClickListener implements OnItemLongClickListener
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			if (mLlayoutOperateFileFolder.isShown())
			{
				mrlayoutNormal.setVisibility(View.VISIBLE);
				mrlayoutEdit.setVisibility(View.GONE);
				mLlayoutOperateFileFolder.setVisibility(View.GONE);
				mLlayoutOperatePaste.setVisibility(View.GONE);
				setCheckboxCheckedOrNot(mList);
				mTvewNumber.setText(String.valueOf(0));
			}
			else
			{
				Log.d("mating"," longClick  eidt show");
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
		 
			return true; //return false 表示
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
	
	
	private void showToast(String message)
	{
		Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 文件操作部分 单击事件
	 */
	private OnClickListener viewOnClickListener  = new OnClickListener()
	{
		@Override
		public void onClick(View arg0) 
		{
			switch (arg0.getId())
			{
				case R.id.setting:
				{
					((MainCloudActivity)mActivity).toggle();
					break;
				}
				case R.id.cloud_filelist_play_video:
				{
					Intent intent = new Intent();
					intent.setClass(mActivity, VideoPlayActivity.class);
					startActivity(intent);
					break;
				}
				case R.id.back:
				{
					mrlayoutNormal.setVisibility(View.VISIBLE);
					mrlayoutEdit.setVisibility(View.GONE);
					mLlayoutOperatePaste.setVisibility(View.GONE);
					mLlayoutOperateFileFolder.setVisibility(View.GONE);
					mTvewChooseAll.setText(getResources().getString(R.string.upload));
					
					if (!adapter.checkIsShown)
					{
						adapter.checkIsShown = true;
					} 				    
					adapter.notifyDataSetChanged();
					setCheckboxCheckedOrNot(mList);
					mTvewNumber.setText(String.valueOf(0));
					break;
				}
				case R.id.upload_or_chooseall:
				{
					if (!isAllChoose)
					{
						for (int i = 0; i < mList.size(); i++)
						{
							if (!mCheckedHash.get(i))
							{
								mCheckedHash.put(i, true);
							}						
						}
						isAllChoose = true;
						mTvewNumber.setText(String.valueOf(mList.size()));
					}
					else
					{
						for (int i = 0; i < mList.size(); i++)
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
				case R.id.phonelist_sale:
				case R.id.phonelist_save:
				case R.id.phonelist_move:
				case R.id.phonelist_share:
				{
					showToast("该功能还未开放，敬请期待！");
					break;
				}
				case R.id.take_photo:
				{
					String sdStatus = Environment.getExternalStorageState();
					if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
						showToast("请插入SD卡后再试...");
						return;
					}
					String fileName = UUID.randomUUID().toString() + ".jpg";
					PhoneFileListFragment.this.mFileName = fileName;
					File file = new File(CommonUtil.FILE_PATH + fileName);
					if (!file.getParentFile().exists())file.getParentFile().mkdirs();
					try {
						Uri uri = Uri.fromFile(file);
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);	
				        startActivityForResult(intent, 1);
					} catch (Exception e) {
						e.printStackTrace();
						showToast("未发现相机应用，请安装相关应用后重试...");
					}
					break;
				}
				case R.id.phonelist_copy:
				{
					if (0 != itemId)
					{
						showToast("对不起，此处不能进行该操作，请回到“全部文件处进行操作”，谢谢！");
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
					Log.d("mating"," del");
					if (0 == clickOrNot())
					{
						showToast("您尚未选择需要删除的文件(夹)。。请选择");
						return;
					}
					new deleteFileFolderThread(handler,mCheckedHash,mList).start();
					
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
					int size = mCopyFrom.size();
					if (0 == size)
					{
						return;
					}
					mProgressDialog.show();
					
//					if (TextUtils.isEmpty(mDestPath))
//					{
//						showToast("您尚未选择将文件放在哪里");
//						return;
//					}
					
					Log.d("mating","mProgressDialog.isShowing() = " + mProgressDialog.isShowing());	
					new copyFileThread(handler,mCopyFrom,mCopyFromName,currentPath).start();
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
					setCheckboxCheckedOrNot(mList);
					mTvewNumber.setText(String.valueOf(0));
				}
				default:
					break;
			}
		}
		
	};
	
	/**
	 * 文件复制完删除完之后刷新界面
	 */
	private void completeOperateFile()
	{
		setCheckboxCheckedOrNot(mList);
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
	
	/**
	 * 选择要复制的文件或文件夹
	 */
	private void copyFileOrFolder()
	{
		for (int i = 0; i < mCheckedHash.size(); i++)
		{
			if (mCheckedHash.get(i))
			{
				String oldAbsolutePath = mList.get(i).getPath();
				String name = mList.get(i).getName();
				mCopyFrom.add(oldAbsolutePath);
				mCopyFromName.add(name);
			}
		}
	}
	
	/**
	 * 复制文件或文件夹 线程
	 * @author Administrator
	 *
	 */
	private class copyFileThread extends Thread
	{
		private Handler hand; 
		/**源文件路劲*/
		private List<String> mpath;
		/**源文件名称*/
		private List<String> mname;
		/**目的文件路径*/
		private String mdest;
		
		public copyFileThread(Handler handler,List<String> mPath,List<String> mName,String mDestPath)
		{
			hand = handler;
			mpath = mPath;
			mname = mName;
			mdest = mDestPath;
		}
		
		@Override
		public void run() 
		{
			super.run();
			doPasteFileFolder();
		}
		
		private void doPasteFileFolder()
		{
			Log.d("mating"," doPasteFileFolder mpath.size() = " + mpath.size());
			for (int i = 0; i <  mpath.size(); i++)
			{
				String str = mpath.get(i);
				String name = mname.get(i);
				Log.d("mating","operate_paste....str = " + str + "; currentPath = " + currentPath);
				File f = new File(str);
				if (f.isDirectory())
				{
					copyFolder(str,mdest + "/" + name);
				}
				else
				{
					copyFile(str,mdest + "/" + name);
				}
			}					
			
			Message msg = hand.obtainMessage();
			msg.what = MessageInfo.COPY_RESPONSE;
			hand.sendMessage(msg);
		}
		
		/**
		 * 文件夹复制
		 * @param from
		 * @param to
		 */
		private void copyFolder(String from,String to)
		{
			Log.d("mating","copyFolder....from = " + from + "; to = " + to);
			File root = new File(from);
			if(!root.exists())
			{
				return;
			}
			File[] currentFiles = root.listFiles();
			
			File targetDir = new File(to);
			if (!targetDir.exists())
			{
				targetDir.mkdirs();
			}
			
			for (int i = 0; i < currentFiles.length; i++)
			{
				if (currentFiles[i].isDirectory())
				{
					copyFolder(currentFiles[i].getPath() + "/",to + "/" + currentFiles[i].getName() + "/");
					
				}
				else
				{
					copyFile(currentFiles[i].getPath(), to + "/" + currentFiles[i].getName());
				}
			}
		}
		
		/**
		 * 文件复制
		 * @param from
		 * @param to
		 */
		private void copyFile(String from,String to)
		{
			Log.d("mating","CopyFile....from = " + from + "; to = " + to);
			try 
			{
				InputStream fosfrom = new FileInputStream(from);
				OutputStream fosto = new FileOutputStream(to);
				byte buffer[] = new byte[1024];
				int c;
				while((c = fosfrom.read(buffer)) > 0)
				{
					fosto.write(buffer, 0, c);
				}
				fosfrom.close();
				fosto.close();
			} 
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 删除文件或文件夹 线程
	 * @author Administrator
	 *
	 */
	private class deleteFileFolderThread extends Thread
	{
		private Handler hand;
		private HashMap<Integer,Boolean> mHash;
		private List<AllFileDetailData> mlist;
		
		public deleteFileFolderThread(Handler handler,HashMap<Integer,Boolean> checkedHash,List<AllFileDetailData> list)
		{
			hand = handler;
			mHash = checkedHash;
			mlist = list;
		}
		
		@Override
		public void run() 
		{
			super.run();
			
			String oldpath = "";
			int size = mHash.size();
			Log.d("mating","deleteFileOrFolder size = " + size);
			for (int i = 0; i < size; i++)
			{
				if (mHash.get(i))
				{
					String oldAbsolutePath = mlist.get(i).getPath();
					int end = oldAbsolutePath.lastIndexOf("/"); 
				    oldpath = oldAbsolutePath.substring(0, end);
					File file = new File(oldAbsolutePath);
					deleteFile(file);
				}
			}
			
			Message msg = hand.obtainMessage();
			msg.what = MessageInfo.DELETE_RESPONSE;
			msg.obj = oldpath;
			hand.sendMessage(msg);
		}
		
		/**
		 * 删除文件
		 * @param file
		 */
		private void deleteFile(File file)
		{
			if (file.isFile())
			{
				file.delete();
				return;
			}
			if (file.isDirectory())
			{
				File[] childFiles = file.listFiles();
				if (childFiles == null || childFiles.length == 0)
				{
					file.delete();
					return;
				}
				for (File f : childFiles)
				{
					deleteFile(f);
				}
				file.delete();
			}
		}
	}
	
	/**
	 * 重命名文件或文件夹  响应
	 */
	private void renameFileOrFolder()
	{
		
		for (int i = 0; i < mCheckedHash.size(); i++)
		{
			if (mCheckedHash.get(i))
			{
				final String oldAbsolutepath = mList.get(i).getPath();
				String oldname = mList.get(i).getName();							
				int end = oldAbsolutepath.lastIndexOf("/"); 
				 final String oldpath = oldAbsolutepath.substring(0, end);
				 Log.d("mating","rename oldpath = " + oldpath);
				LinearLayout layout = (LinearLayout)mActivity.getLayoutInflater().inflate(R.layout.rename,null);
				final EditText editRename = (EditText)layout.findViewById(R.id.operate_edit_rename);
				Log.d("mating", "editRename = " + editRename );
				editRename.setText(oldname);
				new AlertDialog.Builder(mActivity).setTitle(getResources().getString(R.string.rename))
								.setView(layout)
								.setPositiveButton(getResources().getString(R.string.sure_ok), new DialogInterface.OnClickListener()
								{
									String newname = "";
									@Override
									public void onClick(DialogInterface dialog, int which) 
									{
										newname = editRename.getText().toString();
										Log.d("mating","newname = " + newname);
										String newAbsolutepath = oldpath + "/" + newname;
										
										Log.d("mating","oldAbsolutePath = " + oldAbsolutepath + "; newAbsolutepath = " + newAbsolutepath);
										File file = new File(oldAbsolutepath);
										file.renameTo(new File(newAbsolutepath));	
										
										if (0 == itemId)
										{
											Log.d("mating"," next flush oldpath = " + oldpath);
											getFileListData(oldpath,itemId,mActivity);
										}
										else
										{
											getFileListData("/mnt/sdcard",itemId,mActivity);
										}
										showToast("重命名成功");
									}
								})
								.setNegativeButton(getResources().getString(R.string.list_cancle), new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog,	int which) 
									{
										dialog.dismiss();
									}
								
								}).show();						
			}
		}
		
	}
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			FileOutputStream output = null;
			FileInputStream input = null;
			File file = new File(CommonUtil.FILE_PATH + this.mFileName);
			try {
				input = new FileInputStream(file);
				bm = BitmapFactory.decodeStream(input);
				output = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, CommonUtil.COMPRESS_FORMAT, output);// 把数据写入文件
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					output.flush();
					output.close();
					input.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
//			scanProgress.setVisibility(View.VISIBLE);
//			mProgressDialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					uploadFile();
				}
			}).start();
			
		}
	}
	
	private void uploadFile() {
		String userId = userPreferences.getString(CommonUtil.USER_ID, "");
		String userToken = userPreferences.getString(CommonUtil.USER_TOKEN, "");
		Log.d(TAG, "userId = " + userId + ";userToken = " + userToken);
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "----" + UUID.randomUUID().toString();
		try {
			URL url = new URL(CommonUtil.UPLOAD_PHOTO_URL);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"upload\"; filename=\"" + mFileName + "\"" + end);
			dos.writeBytes("Content-Type: image/jpeg"+end);
			dos.writeBytes(end);
			
			String path = CommonUtil.FILE_PATH + this.mFileName;
			Log.d(TAG, "input steam file name = " + mFileName);
			FileInputStream fis = new FileInputStream(path);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();
			
			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"user_id\""+end);
			dos.writeBytes(end);
			dos.writeBytes(userId+end);
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"token\""+end);
			dos.writeBytes(end);
			dos.writeBytes(userToken+end);
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"folder_id\""+end);
			dos.writeBytes(end);
			dos.writeBytes("root"+end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			int responseCode = httpURLConnection.getResponseCode();
			Log.d(TAG, "responseCode" + responseCode);
			if (responseCode == 200) {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToast("上传成功,文件名称：" + mFileName);
//						scanProgress.setVisibility(View.GONE);
						mProgressDialog.dismiss();
						if (bm!=null&&!bm.isRecycled()) {
							bm.recycle();
						}
					}
				});
			}else{
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToast( "上传失败");
//						scanProgress.setVisibility(View.GONE);
						mProgressDialog.dismiss();
						if (bm!=null&&!bm.isRecycled()) {
							bm.recycle();
						}
					}
				});
			}
			dos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private class AllFileDataAdapter extends BaseAdapter
	{
		/** 用来响应获取缩略图成功的消息 */
		private Handler mHandler;
		
		LayoutInflater layoutInflater = null;
		/**文件列表*/
	    List<AllFileDetailData> madapterList = null;
	    ViewHolder holder = null; 
	    
	    /**判断 checkbox是否显示 默认为不显示*/
	    public boolean checkIsShown = true;
	    
	    public AllFileDataAdapter(Context context,List<AllFileDetailData> list,Handler handler)
	    {	    	
	        layoutInflater = LayoutInflater.from(context);
	        madapterList = list;
	        mHandler = handler;
	    }
		@Override
		public int getCount() 
		{
			return madapterList.size();
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
		public View getView(final int position, View arg1, ViewGroup arg2)
		{
			
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
			holder.listener.mPosition = position;
			
//			if (mCheckedHash.get(position))
//			{
//				holder.check.setButtonDrawable(R.drawable.checkbox_selected);			
//			}
//			else
//			{
//				holder.check.setButtonDrawable(R.drawable.checkbox_normal);	
//			}
//   			
			holder.check.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View arg0) {
					CheckBox checkbox = (CheckBox)arg0;
					mCheckedHash.put(position, checkbox.isChecked());
					Message msg = new Message();
					msg.what = MessageInfo.FILE_OPERATE;
					handler.sendMessage(msg);
				}
			});
			
			holder.check.setChecked(mCheckedHash.get(position));
			String path = madapterList.get(position).getPath();
			File f = new File(path);			
			String name = madapterList.get(position).getName();
			if (f.isDirectory())
			{
				 holder.img.setImageResource(R.drawable.folder);
			}
			else if ("image".equals(CommonUtil.getFileType(name)))
			{
				holder.img.setTag(path);
				new ImageViewThread(mHandler,holder.img,path,"image").start();
			}
			else if ("video".equals(CommonUtil.getFileType(name)))
			{
				holder.img.setTag(path);
				new ImageViewThread(mHandler,holder.img,path,"video").start();

			}
			else if ("music".equals(CommonUtil.getFileType(name)))
			{
				holder.img.setImageResource(R.drawable.music_icon);
			}
			else if ("document".equals(CommonUtil.getFileType(name)))
			{
				holder.img.setImageResource(R.drawable.doc_icon);
			}
			else if ("others".equals(CommonUtil.getFileType(name)))
			{
				holder.img.setImageResource(R.drawable.other_icon);
			}
			
			 holder.tvewSize.setVisibility(View.GONE);
			 holder.tvewName.setText(madapterList.get(position).getName());
			 holder.tvewTime.setText(madapterList.get(position).getLastDate());
//			 holder.tvewSize.setText(mList.get(position).getSize());	
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
            if(null != list)
            {
                Log.d("mating","data changed" );
                madapterList = list;
                notifyDataSetChanged();
            }
        }
		
	}
	
	private void setMiddleTitle()
	{
		switch (this.itemId)
		{
		case 0:
			mTvewTitle.setText(getResources().getString(R.string.allfile_fold));
			break;
		case 1:
			mTvewTitle.setText(getResources().getString(R.string.photo) );
			break;
		case 2:
			mTvewTitle.setText(getResources().getString(R.string.audio) );
			break;
		case 3:
			mTvewTitle.setText(getResources().getString(R.string.video) );
			break;
		case 4:
			mTvewTitle.setText(getResources().getString(R.string.bt));
			break;
		case 5:
			mTvewTitle.setText(getResources().getString(R.string.doc) );
			break;
		case 6:
			mTvewTitle.setText(getResources().getString(R.string.others) );
			break;
		case 8:
		    mTvewTitle.setText(getResources().getString(R.string.my_share));
			break;
			default:
				break;
		}
	}
	
	private class UploadPhotoOnClickListenerImpl implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mActivity,UploadPhotoActivity.class);
			startActivity(intent);
		}
		
	}
	
	private class ImageViewThread extends Thread
	{
		private Handler mHandler;
		private ImageView imageView;
		private String mPath;
		private String type;
		public ImageViewThread(Handler handler,ImageView img,String path,String type)
		{
			mHandler = handler;
			imageView = img;
			mPath = path;
			this.type = type;
		}
		
		@Override
		public void run()
		{
			Bitmap bitmap = null;
			super.run();
			Message msg = mHandler.obtainMessage();
			if ("image".equals(this.type))
			{
				bitmap = getImageThumbnails(mPath,50,50);
				msg.what = MessageInfo.PHONE_IMAGE_THUMBNAILS;
			}
			else if ("video".equals(this.type))
			{
				bitmap = getVideoThumbnails(mPath,50,50,MediaStore.Images.Thumbnails.MICRO_KIND);
				msg.what = MessageInfo.PHONE_VIDEO_THUMBNAILS;
			}               
	        msg.obj = new Thumbnails(imageView, mPath, bitmap);
	        mHandler.sendMessage(msg);
		}
		
		/**
		 * 根据指定的图像路径和大小来获取缩略图 
		 * 
		 * @param imagePath 图像的路径 
		 * @param width 指定输出图像的宽度 
		 * @param height 指定输出图像的高度 
		 * @return  生成的缩略图 
		 */
		private Bitmap getImageThumbnails(String imagePath, int width, int height)
		{
			Bitmap bitmap = null;
		    BitmapFactory.Options options = new BitmapFactory.Options();  
		    options.inJustDecodeBounds = true;  
		    // 获取这个图片的宽和高，注意此处的bitmap为null  
		     bitmap = BitmapFactory.decodeFile(imagePath, options);  
		     options.inJustDecodeBounds = false; // 设为 false  
		     // 计算缩放比  
		     int h = options.outHeight;  
		     int w = options.outWidth;  
		     int beWidth = w / width;  
		     int beHeight = h / height;  
		     int be = 1;  
		     if (beWidth < beHeight) 
		     {  
		            be = beWidth;  

		     } 
		     else
		     {  
		            be = beHeight;  
		     }  
		     if (be <= 0) 
		     {  
		            be = 1;  
		     }  
		     options.inSampleSize = be;  
		     // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false  
		     bitmap = BitmapFactory.decodeFile(imagePath, options);  
		     // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象  
		     bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
		                ThumbnailUtils.OPTIONS_RECYCLE_INPUT); 
			return bitmap;
		}
		
		/**
		 * 获取视频的缩略图 
		 * @param videoPath 视频的路径 
		 * @param width 指定输出视频缩略图的宽度 
		 * @param height 指定输出视频缩略图的高度度 
		 * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
	     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
		 * @return 指定大小的视频缩略图 
		 */
		private Bitmap getVideoThumbnails(String videoPath, int width, int height, int kind)
		{
			Bitmap bitmap = null;
	        // 获取视频的缩略图  
	        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
	        if (null == bitmap)
	        {
	        	return null;
	        }
	        Log.d(TAG,"width w = " + bitmap.getWidth());  
	        Log.d(TAG,"height h = " + bitmap.getHeight());  
	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
	                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
			return bitmap;
		}
	}
	
	
}
