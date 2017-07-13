package com.echoii.tv.fragment.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.tv.R;
import com.echoii.tv.constants.Constants;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.util.CommonUtil;
import com.echoii.tv.util.FileUtil;
import com.echoii.tv.util.LogUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>设置默认同步文件具体文件列表</p>
 *
 */
public class ListSyncPathSelectorFragment extends ListFragment {
	
	public static final String TAG = "ListSyncPathSelectorFragment";
	
	private String currentPath = Constants.SYNC_BASE_PATH;
	private String parentPath = "/";
	private List<EchoiiFile> currentFileList ;
	
	private FileItemAdapter adapter;
	private OnListDataChangeListener listener;
	private ListAsyncTask mAsyncTask;
	
	public static ListSyncPathSelectorFragment getInstance() {
		return new ListSyncPathSelectorFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mAsyncTask = new ListAsyncTask();
		mAsyncTask.execute(currentPath);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnListDataChangeListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		listener.OnShowCurrentPath(currentPath);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		EchoiiFile file = currentFileList.get(position);
		if (file.getIdx() == 0) {
			currentFileList = getCurrentFiles(file.getPath());
			if (currentFileList == null) {
				currentFileList = new ArrayList<EchoiiFile>();
			} else if (currentFileList.size() == 0) {
				Toast.makeText(getActivity(), "此路径下没有可选子路径。", Toast.LENGTH_LONG).show();
			} 
			currentPath = file.getPath();
			parentPath = file.getForderId();
			adapter.notifyDataSetChanged();
			listener.OnShowCurrentPath(currentPath);
		} else {
			Toast.makeText(getActivity(), "无法选择此项。", Toast.LENGTH_SHORT).show();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EchoiiFile> getCurrentFiles(String path) {
		List<EchoiiFile> list = FileUtil.getFileList(path);
		if (list != null) {
			Collections.sort(list);
		}
		return list;
	}
	
	public class FileItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return currentFileList.size();
		}

		@Override
		public Object getItem(int position) {
			return currentFileList.get(position);
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_fileselector_listview, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag(); 
			}
			
			EchoiiFile file = currentFileList.get(position);
			if (file.getIdx() == 0) {
				holder.icon.setImageResource(R.drawable.directory);
			} else {
				holder.icon.setImageResource(R.drawable.file);
			}
			holder.fileName.setText(file.getName());
			
			return convertView;
		}
		
	}
	
	private final class ViewHolder {
		ImageView icon;
		TextView fileName;
	}
	
	public void back(){
		if (parentPath.equals("/")) {
			return;
		}
		currentFileList = getCurrentFiles(parentPath);
		adapter.notifyDataSetChanged();
		currentPath = parentPath;
		parentPath = FileUtil.getParentPath(currentPath);
		listener.OnShowCurrentPath(currentPath);
	}
	
	public void saveSyncDefaultPath() {
		Constants.SYNC_DEFAULT_PATH = CommonUtil.setSyncPath(getActivity(), currentPath);
		LogUtil.d(TAG, "current sync path = " + Constants.SYNC_DEFAULT_PATH);
		SharedPreferences defaultPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());
		defaultPreference.edit().putString(EchoiiPrefencesFragment.KEY_DEFAULT_SYNC_PATH, Constants.SYNC_DEFAULT_PATH).commit();
	}
	
	public interface OnListDataChangeListener {
		public void OnShowCurrentPath(String currentPath);
	}
	
	public class ListAsyncTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String path = params[0];
			if (path == null) {
				return null;
			}
			currentFileList = getCurrentFiles(currentPath);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter = new FileItemAdapter();
			setListAdapter(adapter);
		}
		
	}

}
