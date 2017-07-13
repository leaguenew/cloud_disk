package com.echoii.tv.fragment.cloud;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.tv.HttpHelper;
import com.echoii.tv.R;
import com.echoii.tv.constants.Constants;
import com.echoii.tv.constants.HttpConstants;
import com.echoii.tv.model.EchoiiFile;
import com.echoii.tv.model.User;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>具体的云端文件列表</p>
 *
 */
public class ListCloudFileFragment extends ListFragment {
	
	public static final String TAG = "ListCloudFileFragment";
	
	private List<EchoiiFile> currentFileList;
	protected Stack<String> pathStack;
	private int position = 0;
	
	private CloudFileItemAdapter adapter;
	private InitFileListAsyncTask mAsyncTask;
	private HttpHelper helper;
	private ListChildFileAsyncTask mTask;
	private User cloudUser;
	
	public static ListCloudFileFragment getInstance() {
		return new ListCloudFileFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cloudUser = new User();
		cloudUser.setId(HttpConstants.USER_ID);
		cloudUser.setPassword(HttpConstants.PASSWORD);
		
		Bundle bundle = getArguments();
		if (bundle == null) {
			position = 0;
		} else {
			position = bundle.getInt(Constants.KEY_CLOUD_CATEGORY_POSITION);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		pathStack = new Stack<String>();
		pathStack.push(Constants.CLOUD_FILE_PATH);
		helper = new HttpHelper();
		mAsyncTask = new InitFileListAsyncTask();
		mAsyncTask.execute(pathStack.peek(), position + "");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		EchoiiFile file = currentFileList.get(position);
		if (file.getIdx() == 0) {
			String path = file.getId();
			mTask = new ListChildFileAsyncTask();
			mTask.execute(path);
			pathStack.push(path);
		} else {
			Toast.makeText(getActivity(), "打开文件。", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<EchoiiFile> getCurrentFiles(String path, int position) {
		List<EchoiiFile> list = null;
		try {
			list = helper.getCloudList(cloudUser, path, position);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (list != null) {
			Collections.sort(list);
		}
		return list;
	}
	
	public class CloudFileItemAdapter extends BaseAdapter{

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
		if (pathStack.size() == 1) {
			return;
		}
		pathStack.pop();
		mTask = new ListChildFileAsyncTask();
		mTask.execute(pathStack.peek());
		
	}
	
	public class InitFileListAsyncTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String path = params[0];
			int position = Integer.parseInt(params[1]);
			if (path == null) {
				return null;
			}
			currentFileList = getCurrentFiles(path, position);
			if (currentFileList == null) {
				currentFileList = new ArrayList<EchoiiFile>();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter = new CloudFileItemAdapter();
			setListAdapter(adapter);
		}
		
	}
	
	public class ListChildFileAsyncTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String path = params[0];
			currentFileList = getCurrentFiles(path, position);
			if (currentFileList == null) {
				currentFileList = new ArrayList<EchoiiFile>();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
		}
		
	}
	
}
