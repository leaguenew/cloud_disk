package com.echoii.activity.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.echoii.activity.VideoPlayActivity;
import com.echoii.constant.CommonUtil;
import com.echoii.network.socket.DataCenterSocket;
import com.echoii.thread.DCenterDoRequestThread;
import com.echoii.thread.DCenterDoRequestThread.HandleObj;
import com.echoii.thread.DCenterGetDataThread;
import com.echoii.thread.DCenterGetDataThread.DCHandleFileListMsg;
import com.echoii.thread.DCenterLoginRequestThread;
import com.echoii.thread.DCenterRenameRequestThread;
import com.echoii.thread.DCenterRenameRequestThread.RenameHandleObj;
import com.echoii.uploadfile.UploadFile;

public class DataCenterListFragment extends Fragment implements OnClickListener {

	public static final String TAG = "DataCenterListFragment";
	public static final int FILE_ROOT_PATH = 0x001;
	public static final int FILE_CHILD_PATH = 0x002;
	public static final int DO_REQUEST_THREAD_ROOT = 0x003;
	public static final int DO_REQUEST_THREAD_CHILD = 0x004;
	public static final int COPY_FILES_SUCCESS = 0x005;
	public static final int COPY_FILES_ERROR = 0x006;
	public static final int DELETE_FILES_SUCCESS = 0x007;
	public static final int DELETE_FILES_ERROR = 0x008;
	public static final int RENAME_FILE_SUCCESS = 0x009;
	public static final int RENAME_FILE_ERROR = 0x010;
	public static final String CHECK_BOX_ISCHECKED = "isChecked";
	public static final String CHECK_BOX_ISVISIBLE = "isVisible";

	private Activity mActivity;
	private ImageView mImgVideoPlay;
	private ListView mListView;
	private SharedPreferences ipPreference;
	private String dataCenterIp;
	private OnFragmentChangeListener mListener;
	private DataCenterListAdapter mAdapter;
	private List<UploadFile> mFileList = new ArrayList<UploadFile>();
	private DCenterGetDataThread mThread;
	private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等

	private Map<String, Object> checkBoxMap;
	private List<Map<String, Object>> checkBoxList = new ArrayList<Map<String, Object>>();
	private List<String> mFilePathList = new ArrayList<String>();
	public static boolean isScrolling = false;
	public static boolean isEditState = false;
	public static boolean isOperating = false; //半编辑状态标记
	private int itemId;
	private int responsePath;
	
	private RelativeLayout layoutEdit;
	private RelativeLayout layoutTitle;
	private TextView dataCenterTitleText;
	private TextView mTvExit;
	private ImageView settingImage;
	private TextView backText;
	private TextView numberText;
	private TextView uploadOrChooseAll;
	private int chooseCount = 0;

	private View listHeaderView;
	private LinearLayout headerLayout;  //listview top back layout
//	private ProgressBar socketProgress;
	
	private LinearLayout mOperateLayout;
	private LinearLayout mMoreOperateLayout;
	private LinearLayout mCopyLayout;
	private Button mSaleButton;
	private Button mSaveButton;
	private Button mMoveButton;
	private Button mShareButton;
	private Button mCopyButton;
	private Button mDeleteButton;
	private Button mRenameButton;
	private Button mPasteButton;
	private Button mCancleButton;
	private ProgressDialog operateDialog ;
	private ProgressDialog mProgressDialog;
	
	private DataCenterSocket dcenterSocket;
	
	public DataCenterListFragment() {
		Log.d(TAG, "DataCenterListFragment : 构造");
	}
	
	public DataCenterListFragment(OnFragmentChangeListener mListener) {
		super();
		this.mListener = mListener;
	}
	
	public DataCenterListFragment(int itemId,int responsePath, OnFragmentChangeListener mListener) {
		Log.d(TAG, "DataCenterListFragment : 构造");
		this.itemId = itemId;
		this.responsePath = responsePath;
		this.mListener = mListener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(TAG, "DataCenterListFragment : onAttach");
		mActivity = activity;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "DataCenterListFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_datacenter_list, null);
		initViews(view);
		initOperateButton(view);
		return view;
	}
	
	public void initOperateButton(View view){
		mSaleButton = (Button)view.findViewById(R.id.sale);
		mSaveButton = (Button)view.findViewById(R.id.transfer_save);
		mMoveButton = (Button)view.findViewById(R.id.move);
		mShareButton = (Button)view.findViewById(R.id.share);
		
		mCopyButton = (Button) view.findViewById(R.id.copy);
		mDeleteButton = (Button) view.findViewById(R.id.delete);
		mRenameButton = (Button) view.findViewById(R.id.rename);
		mPasteButton = (Button) view.findViewById(R.id.paste);
		mCancleButton = (Button) view.findViewById(R.id.cancle);
		mOperateLayout = (LinearLayout) view.findViewById(R.id.dc_operation);
		mMoreOperateLayout = (LinearLayout) view.findViewById(R.id.dc_operation_more);
		mOperateLayout.setVisibility(View.GONE);
		mMoreOperateLayout.setVisibility(View.GONE);
		mCopyLayout = (LinearLayout) view.findViewById(R.id.copy_layout);
		mRenameButton.setEnabled(false);
		mSaleButton.setOnClickListener(this);
		mSaveButton.setOnClickListener(this);
		mMoveButton.setOnClickListener(this);
		mShareButton.setOnClickListener(this);
		mCopyButton.setOnClickListener(this);
		mDeleteButton.setOnClickListener(this);
		mRenameButton.setOnClickListener(this);
		mPasteButton.setOnClickListener(this);
		mCancleButton.setOnClickListener(this);
	}
	
	public void initViews(View view) {
		mImgVideoPlay = (ImageView)view.findViewById(R.id.cloud_filelist_play_video);
		mListView = (ListView) view.findViewById(R.id.data_center_list);
		mAdapter = new DataCenterListAdapter(mFileList);
		// header需要多套一层layout才能被view.gone
//		listHeaderView = mActivity.getLayoutInflater().inflate(R.layout.header_back, null);
//		headerLayout = (LinearLayout) listHeaderView.findViewById(R.id.header_layout);
//		headerLayout.setVisibility(View.GONE);
//		mListView.addHeaderView(listHeaderView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemLongClickListener(new DCListOnItemLongClickListenerImpl());
		mListView.setOnItemClickListener(new DCFileListOnItemClickListenerImpl());
		layoutEdit = (RelativeLayout) view.findViewById(R.id.layout_edit);
		layoutTitle = (RelativeLayout) view.findViewById(R.id.layout_title);
		dataCenterTitleText = (TextView) view.findViewById(R.id.tvew_title);
		dataCenterTitleText.setText(mActivity.getResources().getString(R.string.main_datacenter));
		mTvExit = (TextView) view.findViewById(R.id.exit);
		mTvExit.setText("断开");
		mTvExit.setVisibility(View.VISIBLE);
		mTvExit.setOnClickListener(this);
		layoutTitle.setVisibility(View.VISIBLE);
		layoutEdit.setVisibility(View.GONE);
		settingImage = (ImageView) view.findViewById(R.id.setting);
		backText = (TextView) view.findViewById(R.id.back);
		numberText = (TextView) view.findViewById(R.id.number);
		uploadOrChooseAll = (TextView) view.findViewById(R.id.upload_or_chooseall);
		mImgVideoPlay.setOnClickListener(this);
		settingImage.setOnClickListener(this);
		backText.setOnClickListener(this);
		numberText.setOnClickListener(this);
		uploadOrChooseAll.setOnClickListener(this);
//		socketProgress = (ProgressBar) view.findViewById(R.id.socketProgress);
	}

	private void initCheckBoxMap() {
		if (checkBoxList != null) {
			checkBoxList.clear();
		}
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxMap = new HashMap<String, Object>();
			checkBoxMap.put(CHECK_BOX_ISCHECKED, false);
			checkBoxMap.put(CHECK_BOX_ISVISIBLE, View.GONE);
			checkBoxList.add(checkBoxMap);
		}
		refreshListView();
	}
	
	private void initEditCheckBoxMap() {
		if (checkBoxList != null) {
			checkBoxList.clear();
		}
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxMap = new HashMap<String, Object>();
			checkBoxMap.put(CHECK_BOX_ISCHECKED, false);
			checkBoxMap.put(CHECK_BOX_ISVISIBLE, View.VISIBLE);
			checkBoxList.add(checkBoxMap);
		}
		refreshListView();
	}

	public void savePathAndParentPath(String path, String parentPath) {
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PATH, path).commit();
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH,parentPath).commit();
	}

	public class DataCenterListAdapter extends BaseAdapter {

		List<UploadFile> fileList;

		public DataCenterListAdapter() {
		}

		public DataCenterListAdapter(List<UploadFile> list) {
			this.fileList = list;
		}

		@Override
		public int getCount() {
			return fileList.size();
		}

		@Override
		public Object getItem(int position) {
			return fileList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup root) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.allfile_item, null);
			}
			final int index = position;
			ImageView fileIcon = (ImageView) convertView.findViewById(R.id.img_allfile);
			TextView fileName = (TextView) convertView.findViewById(R.id.tvew_allfile_title);
			TextView fileDate = (TextView) convertView.findViewById(R.id.tvew_all_file_time);
			TextView fileSize = (TextView) convertView.findViewById(R.id.tvew_all_file_size);
			CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_allfile_choose);
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						checkBoxList.get(index).put(CHECK_BOX_ISCHECKED, true);
						int count = 0;
						for (int i = 0; i < checkBoxList.size(); i++) {
							if ((Boolean) checkBoxList.get(i).get(CHECK_BOX_ISCHECKED)) {
								count += 1;
							}
						}
						if (count == 1) {
							mRenameButton.setEnabled(true);
						}else{
							mRenameButton.setEnabled(false);
						}
						numberText.setText(String.valueOf(count));
					}else {
						checkBoxList.get(index).put(CHECK_BOX_ISCHECKED, false);
						int count = 0;
						for (int i = 0; i < checkBoxList.size(); i++) {
							if ((Boolean) checkBoxList.get(i).get(CHECK_BOX_ISCHECKED)) {
								count += 1;
							}
						}
						if (count == 1) {
							mRenameButton.setEnabled(true);
						}else{
							mRenameButton.setEnabled(false);
						}
						numberText.setText(String.valueOf(count));
					}
					
				}
			});
			if (checkBoxList.size() > 0) {
				if (!(fileList.get(index).getFileSize() < 0)) {
					checkBox.setChecked((Boolean) checkBoxList.get(index).get(CHECK_BOX_ISCHECKED));
					checkBox.setVisibility((Integer) checkBoxList.get(index).get(CHECK_BOX_ISVISIBLE));
				}
			}
			String name = fileList.get(index).getFileName();
			if (fileList.get(index).getType().equals("folder")) {
				fileIcon.setImageResource(R.drawable.folder);
			} else {
				String type = CommonUtil.getFileType(name);
				if (type.equals("music")) {
					fileIcon.setImageResource(R.drawable.music_icon);
				} else if (type.equals("video")) {
					fileIcon.setImageResource(R.drawable.video_icon);
				} else if (type.equals("image")) {
					fileIcon.setImageResource(R.drawable.photo_icon);
				} else if (type.equals("document")) {
					fileIcon.setImageResource(R.drawable.doc_icon);
				} else if (type.equals("others")) {
					fileIcon.setImageResource(R.drawable.other_icon);
				}
			}
			fileName.setText(name);
			fileDate.setText(CommonUtil.formatTime(Long.parseLong(fileList.get(index).getDate())));
			fileSize.setText(CommonUtil.formatFileSize(fileList.get(index).getFileSize()));
			return convertView;
		}

		public void refreshDataChanged(List list) {
			this.fileList = list;
			notifyDataSetChanged();
		}
	}

	public void getDataCenterData(String ip, int itemId,String path, int handlerCase) {
		Log.d(TAG, "DataCenterListFragment : getDataCenterData");
//		socketProgress.setVisibility(View.VISIBLE);
		mProgressDialog= new ProgressDialog(mActivity);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("正在请求，请稍后...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.show();
		this.itemId = itemId;
		mThread = new DCenterGetDataThread(handler,mActivity,ip, itemId,path, handlerCase);
		mThread.start();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(TAG, "DataCenterListFragment : onActivityCreated");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "DataCenterListFragment : onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "DataCenterListFragment : onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (isEditState) {
			cancleEditState();
		}
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss()	;
			}
		}
		Log.d(TAG, "DataCenterListFragment : onDetach");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "DataCenterListFragment : onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		operateDialog = new ProgressDialog(mActivity);
		dcenterSocket = new DataCenterSocket(handler, mActivity);
		ipPreference = mActivity.getSharedPreferences(DataCenterConnectionFragment.IP_SHARED_NAME,Context.MODE_PRIVATE);
		dataCenterIp = ipPreference.getString(DataCenterConnectionFragment.CURRENT_CONN_IP, "");
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		String path = sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PATH, "null");
		if (responsePath == DataCenterListFragment.FILE_ROOT_PATH) {
			getDataCenterData(dataCenterIp,itemId,CommonUtil.SDCARD_ROOT_PATH,DataCenterListFragment.FILE_ROOT_PATH);
		}else{
			getDataCenterData(dataCenterIp,itemId,path,DataCenterListFragment.FILE_CHILD_PATH);
		}
		switch (itemId) {
		case 0:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.allfile));
			break;
		case 1:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.photo));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 2:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.audio));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 3:
			mImgVideoPlay.setVisibility(View.VISIBLE);
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.video));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 4:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.bt));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 5:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.doc));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 6:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.data_market));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 7:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.share));
			mCopyLayout.setVisibility(View.GONE);
			break;
		case 8:
			dataCenterTitleText.setText(mActivity.getResources().getString(R.string.others));
			mCopyLayout.setVisibility(View.GONE);
			break;

		default:
			break;
		}
		Log.d(TAG, "DataCenterListFragment : onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "DataCenterListFragment : onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG, "DataCenterListFragment : onStop");
	}

	public static String getParentPath(String path) {
		String parentPath;
		if (path.equals(CommonUtil.SDCARD_ROOT_PATH)) {
			parentPath = "DataCenterFragment";
		} else {
			parentPath = path.substring(0, path.lastIndexOf("/"));
		}
		return parentPath;
	}

	public void cancleEditState() {
		layoutTitle.setVisibility(View.VISIBLE);
		layoutEdit.setVisibility(View.GONE);
		mOperateLayout.setVisibility(View.GONE);
		mMoreOperateLayout.setVisibility(View.GONE);
//		if (!sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PATH, "null").equals(CommonUtil.SDCARD_ROOT_PATH)) {
//			headerLayout.setVisibility(View.VISIBLE);
//		}
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxList.get(i).put(CHECK_BOX_ISCHECKED, false);
			checkBoxList.get(i).put(CHECK_BOX_ISVISIBLE, View.GONE);
		}
		mFilePathList.clear();
		chooseCount = 0;
		mAdapter.notifyDataSetChanged();
		isEditState = false;
		isOperating = false;
	}

	/**
	 * @param isOperating  表示是否正在操作文件，true：正在操作文件，如等待移动，等待复制，此时应该进入半编辑状态，使可以选择文件路径
	 * 																	false：others；
	 */
	public void enterEditState() {
		chooseCount = 0;
		layoutTitle.setVisibility(View.GONE);
		layoutEdit.setVisibility(View.VISIBLE);
//		headerLayout.setVisibility(View.GONE);
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxList.get(i).put(CHECK_BOX_ISVISIBLE, View.VISIBLE);
		}
		uploadOrChooseAll.setVisibility(View.VISIBLE);
		mOperateLayout.setVisibility(View.VISIBLE);
		mMoreOperateLayout.setVisibility(View.GONE);
		isEditState = true;
		mAdapter.notifyDataSetChanged();
	}
	
	public void enterEditCopyState() {
		chooseCount = 0;
		isOperating = true;
		layoutTitle.setVisibility(View.GONE);
		layoutEdit.setVisibility(View.VISIBLE);
//		headerLayout.setVisibility(View.GONE);
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxList.get(i).put(CHECK_BOX_ISVISIBLE, View.GONE);
		}
		uploadOrChooseAll.setVisibility(View.VISIBLE);
		mOperateLayout.setVisibility(View.GONE);
		mMoreOperateLayout.setVisibility(View.VISIBLE);
		isEditState = true;
		mAdapter.notifyDataSetChanged();
	}
	
	public void refreshListView(){
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(0);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FILE_ROOT_PATH:
				DCHandleFileListMsg msgObj = (DCHandleFileListMsg) msg.obj;
				savePathAndParentPath(msgObj.path, getParentPath(msgObj.path));
//				headerLayout.setVisibility(View.GONE);
				mFileList = msgObj.fileList;
				mAdapter.refreshDataChanged(mFileList);
				mListView.setSelection(0);
				if (isEditState&&!isOperating) {
					initEditCheckBoxMap();
				}else{
					initCheckBoxMap();
				}
				responsePath = FILE_ROOT_PATH;
//				socketProgress.setVisibility(View.GONE);
				mProgressDialog.dismiss();
				Log.d(TAG, "handleMessage file root path.");
				break;
			case FILE_CHILD_PATH:
				DCHandleFileListMsg msgObj1= (DCHandleFileListMsg) msg.obj;
				savePathAndParentPath(msgObj1.path, getParentPath(msgObj1.path));
//				headerLayout.setVisibility(View.VISIBLE);
				mFileList = msgObj1.fileList;
				mAdapter.refreshDataChanged(mFileList);
				mListView.setSelection(0);
				if (isEditState&&!isOperating) {
					initEditCheckBoxMap();
				}else{
					initCheckBoxMap();
				}
//				socketProgress.setVisibility(View.GONE);
				responsePath = FILE_CHILD_PATH;
				mProgressDialog.dismiss();
				Log.d(TAG, "handleMessage file child path .");
				break;
			case DO_REQUEST_THREAD_ROOT :
				HandleObj obj = (HandleObj)msg.obj;
				getDataCenterData(dataCenterIp,itemId, obj.currentPath, DataCenterListFragment.FILE_ROOT_PATH);
				break;
			case DO_REQUEST_THREAD_CHILD :
				HandleObj obj1 = (HandleObj)msg.obj;
				getDataCenterData(dataCenterIp,itemId, obj1.currentPath, DataCenterListFragment.FILE_CHILD_PATH);
				break;
			case COPY_FILES_SUCCESS:
				HandleObj obj2 = (HandleObj)msg.obj;
				Log.d(TAG, "ip = " + dataCenterIp + "/item id = " + itemId + "/current path = " + obj2.currentPath);
				getDataCenterData(dataCenterIp, itemId, obj2.currentPath, obj2.handlerCase);
				Toast.makeText(mActivity, "复制成功。", Toast.LENGTH_SHORT).show();
				cancleEditState();
				cancleProgressDialog(operateDialog);
				break;
			case COPY_FILES_ERROR : 
				Toast.makeText(mActivity, "复制失败，请重试...", Toast.LENGTH_SHORT).show();
				cancleEditState();
				cancleProgressDialog(operateDialog);
				break;
			case DELETE_FILES_SUCCESS:
				HandleObj obj3 = (HandleObj)msg.obj;
				Log.d(TAG, "ip = " + dataCenterIp + "/item id = " + itemId + "/current path = " + obj3.currentPath);
				getDataCenterData(dataCenterIp, itemId, obj3.currentPath, obj3.handlerCase);
				Toast.makeText(mActivity, "删除成功。", Toast.LENGTH_SHORT).show();
				cancleEditState();
				cancleProgressDialog(operateDialog);
				break;
			case DELETE_FILES_ERROR : 
				Toast.makeText(mActivity, "删除失败，请重试...", Toast.LENGTH_SHORT).show();
				cancleEditState();
				cancleProgressDialog(operateDialog);
				break;
			case RENAME_FILE_SUCCESS:
				RenameHandleObj obj4 = (RenameHandleObj)msg.obj;
				Log.d(TAG, "ip = " + dataCenterIp + "/item id = " + itemId + "/current path = " + obj4.currentPath);
				getDataCenterData(dataCenterIp, itemId, obj4.currentPath, obj4.handlerCase);
				Toast.makeText(mActivity, "重命名成功。", Toast.LENGTH_SHORT).show();
				cancleEditState();
				cancleProgressDialog(operateDialog);
				break;
			case RENAME_FILE_ERROR : 
				Toast.makeText(mActivity, "重命名失败，请重试...", Toast.LENGTH_SHORT).show();
				cancleEditState();
				cancleProgressDialog(operateDialog);
				break;
			default:
				break;
			}
			sessionStatusPreference.edit().putInt(CenterFragment2.SESSION_DATA_CENTER_FILE_TYPE, DataCenterListFragment.this.itemId).commit();
		};
	};

	public class DCFileListOnItemClickListenerImpl implements
			OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			final int index = position ;
			if (isEditState&&!isOperating) {
				CheckBox cb = (CheckBox) view
						.findViewById(R.id.check_allfile_choose);
				if (cb.isChecked()) {
					cb.setChecked(false);
				} else {
					cb.setChecked(true);
				}
			} else {
//				if (index == -1) {
//					String path = sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PARENT_PATH, "null");
//					if (path.equals(CommonUtil.SDCARD_ROOT_PATH)) {
//						getDataCenterData(dataCenterIp,itemId,CommonUtil.SDCARD_ROOT_PATH, FILE_ROOT_PATH);
//					} else {
//						getDataCenterData(dataCenterIp, itemId,path, FILE_CHILD_PATH);
//					}
//				} else {
					Log.d(TAG, "request path = " + mFileList.get(index).getPath());
					if (mFileList.get(index).getType().equals("folder")) {
						getDataCenterData(dataCenterIp, itemId,mFileList.get(index).getPath(), FILE_CHILD_PATH);
					} else {
						//Toast.makeText(getActivity(), "打开文件",Toast.LENGTH_SHORT).show();
						final ProgressDialog progress = new ProgressDialog(getActivity());
						progress.setMessage("正在从数据中心下载文件，请稍候...");
//						progress.setOnCancelListener(new OnCancelListenerImpl());
						progress.setCancelable(false);
						progress.setButton("隐藏", new downloadIgnoreOnClickListenerImpl());
						showDownloadDialog(progress);
						String openFilePath = sessionStatusPreference.getString(mFileList.get(index).getPath(),"null");
						Log.d(TAG, "dc download file path = " + openFilePath);
						if (!openFilePath.equals("null")) {
							Intent  intent = CommonUtil.openFile(openFilePath);
							Log.d(TAG, "dc file list open file intent = " + intent);
							cancleProgressDialog(progress);
							if (intent == null) {
								sessionStatusPreference.edit().remove(mFileList.get(index).getPath()).commit();
								Toast.makeText(mActivity, "下载出错，请重试...", Toast.LENGTH_SHORT).show();
								return;
							}else{
								getActivity().startActivity(intent);
							}
						}else{
							new Thread(new Runnable() {
								@Override
								public void run() {
									String path = dcenterSocket.doDownload2(dataCenterIp, DataCenterSocket.REQUEST_CODE_DOWNLOAD_FILE, mFileList.get(index).getPath());
									Log.d(TAG, "downloaded path = " + path);
									if (path == null) {
										sessionStatusPreference.edit().remove(mFileList.get(index).getPath()).commit();
										showToast("下载出错，请重试...");
										cancleProgressDialog(progress);
										return;
									}else{
										if (path.equals("cancle")) {
											sessionStatusPreference.edit().remove(mFileList.get(index).getPath()).commit();
											File file = new File(path);
											if (file.exists()) {
												file.delete();
											}
											cancleProgressDialog(progress);
											return;
										} else {
											sessionStatusPreference.edit().putString(mFileList.get(index).getPath(), path).commit();
											showProgressDialog(progress,"下载成功，等待打开文件...");
											Intent  intent = CommonUtil.openFile(path);
											cancleProgressDialog(progress);
											mActivity.startActivity(intent);
										}
									}
								}
							}).start();
						}
//					}
				}
			}
		}
	}
	
	public void showToast(final String message){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void showDownloadDialog(final ProgressDialog pd){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pd.show();
			}
		});
	}
	
	public void showProgressDialog(final ProgressDialog pd,final String message){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pd.setMessage(message);
				pd.show();
			}
		});
	}
	
	public void cancleProgressDialog(final ProgressDialog pd){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}
		});
	}

	public class DCListOnItemLongClickListenerImpl implements
			OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			if (isEditState) {
				cancleEditState();
			} else {
				enterEditState();
			}
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.sale:
		case R.id.transfer_save:
		case R.id.move:
		case R.id.share:
			Toast.makeText(mActivity, "该功能还未开放，敬请期待！", Toast.LENGTH_SHORT).show();
			break;
		case R.id.setting:
			((MainCloudActivity)mActivity).toggle();
			break;
		case R.id.cloud_filelist_play_video:
			Intent intent = new Intent();
			intent.setClass(mActivity, VideoPlayActivity.class);
			startActivity(intent);
			break;
		case R.id.back:
			cancleEditState();
			break;
		case R.id.exit:
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
			break;
		case R.id.upload_or_chooseall:
			Log.d(TAG, "upload_or_chooseall button is pressed.");
			if (isEditState) {
				if (chooseCount >= 0 && chooseCount < mFileList.size()) {
					chooseAllCheckBox();
					chooseCount = mFileList.size();
					numberText.setText(String.valueOf(chooseCount));
				}else if(chooseCount == mFileList.size()){
					cancleChooseAllCheckBox();
					chooseCount = 0;
					numberText.setText(String.valueOf(chooseCount));
				}
			}
			break;
		case R.id.copy :
			mFilePathList.clear();
			for (int i = 0; i < checkBoxList.size(); i++) {
				if ((Boolean) checkBoxList.get(i).get(CHECK_BOX_ISCHECKED)) {
					mFilePathList.add(mFileList.get(i).getPath());
				}
			}
			if (mFilePathList.size() > 0) {
				Toast.makeText(mActivity, mActivity.getResources().getString(R.string.choose_files_path), Toast.LENGTH_SHORT).show();
				enterEditCopyState();
			}else{
				Toast.makeText(mActivity, mActivity.getResources().getString(R.string.choose_files_first), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.delete:
			mFilePathList.clear();
			for (int i = 0; i < checkBoxList.size(); i++) {
				if ((Boolean) checkBoxList.get(i).get(CHECK_BOX_ISCHECKED)) {
					mFilePathList.add(mFileList.get(i).getPath());
				}
			}
			if (mFilePathList.size() > 0) {
				showProgressDialog(operateDialog, "正在删除，请稍候...");
				String currentPath = sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PATH, "null");
				new DCenterDoRequestThread(handler, mActivity,dataCenterIp,DataCenterSocket.REQUEST_CODE_DELETE, mFilePathList, currentPath).start();
			}else{
				Toast.makeText(mActivity, mActivity.getResources().getString(R.string.choose_files_first), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.rename:
			Log.d(TAG, "rename *****");
			mFilePathList.clear();
			for (int i = 0; i < checkBoxList.size(); i++) {
				if ((Boolean) checkBoxList.get(i).get(CHECK_BOX_ISCHECKED)) {
					mFilePathList.add(mFileList.get(i).getPath());
				}
			}
			showRenameDialog(mFilePathList);
			break;
		case R.id.paste:
			showProgressDialog(operateDialog,"正在复制文件请稍候");
			String currentPath = sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PATH, "null");
			new DCenterDoRequestThread(handler, mActivity,dataCenterIp,DataCenterSocket.REQUEST_CODE_COPY, mFilePathList, currentPath).start();
			break;
		case R.id.cancle:
			cancleEditState();
			break;
		default:
			break;
		}
	}

	private void showRenameDialog(final List<String> mFilePathList) {
		final EditText edit = new EditText(mActivity);
		edit.setId(R.id.rename_edittext);
		edit.setSingleLine(true);
		edit.setText(new File(mFilePathList.get(0)).getName());
		new AlertDialog.Builder(mActivity).setTitle(R.string.rename).setView(edit).setPositiveButton(R.string.sure_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(TAG, "rename dialog positive button clicked");
				String currentPath = sessionStatusPreference.getString(CenterFragment2.SESSION_DATA_CENTER_PATH, "null");
				new DCenterRenameRequestThread(handler, mActivity,dataCenterIp, DataCenterSocket.REQUEST_CODE_RENAME, mFilePathList, currentPath, edit.getText().toString()).start();
			}
		}).setNegativeButton(R.string.list_cancle, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(TAG, "rename dialog negative button clicked");
				dialog.dismiss();
			}
		}).create().show();
	}

	public void chooseAllCheckBox() {
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxList.get(i).put(CHECK_BOX_ISCHECKED, true);
		}
		refreshListView();
	}

	public void cancleChooseAllCheckBox() {
		for (int i = 0; i < mFileList.size(); i++) {
			checkBoxList.get(i).put(CHECK_BOX_ISCHECKED, false);
		}
		mFilePathList.clear();
		refreshListView();
	}
	
	public class OnCancelListenerImpl implements OnCancelListener{
		@Override
		public void onCancel(DialogInterface dialog) {
			dcenterSocket.cancleDownloadSocket();
		}
	}
	
	public class downloadIgnoreOnClickListenerImpl implements android.content.DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
		
	}

}
