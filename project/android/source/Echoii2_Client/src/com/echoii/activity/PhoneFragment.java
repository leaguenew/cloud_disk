package com.echoii.activity;


import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.activity.fragment.OnFragmentChangeListener;
import com.echoii.constant.CommonUtil;


public class PhoneFragment extends Fragment
{
	public static final String TAG = "PhoneFragment";
	private ImageView mTvewSetting;
	
	private GridView mGridData;
    private AllfileGridAdapter phoneAllFileAdapter = null;
    private String[] itemNames = {"全部文件","图片","音乐","视频","BT种子","文档","数据市场","其他","我的分享"};
	private int[] phoneImages = { R.drawable.grid_item_allfile,
			R.drawable.grid_item_image, R.drawable.grid_item_music,
			R.drawable.grid_item_video, R.drawable.grid_item_bt,
			R.drawable.grid_item_document,
			 R.drawable.grid_data_market,R.drawable.grid_item_other,
			R.drawable.grid_item_myshare};
	private OnFragmentChangeListener mListener;
	private SharedPreferences sessionStatusPreference; // 保存会话信息，包括tab切换信息，请求路径等
	private MainCloudActivity mActivity;
	private ImageView mTakePhotoImage;
	private String mFileName;
	private Bitmap bm = null;
	private SharedPreferences userPreferences;
	private ProgressDialog dialog;
	
	public PhoneFragment(){}
	
	public PhoneFragment(OnFragmentChangeListener listener )
	{
		this.mListener = listener;
	
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainCloudActivity)activity;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_phone, null);
        mGridData = (GridView) view.findViewById(R.id.phone_gridview);
		mTvewSetting = (ImageView)view.findViewById(R.id.setting);
        phoneAllFileAdapter = new AllfileGridAdapter(getActivity(), phoneImages , itemNames);
        mGridData.setAdapter(phoneAllFileAdapter);
        mGridData.setOnItemClickListener(new ItemClickListener());
        
		mTvewSetting.setOnClickListener(titleOnClickListener);
		mTakePhotoImage = (ImageView) view.findViewById(R.id.take_photo);
		mTakePhotoImage.setOnClickListener(titleOnClickListener);
		
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
			case R.id.take_photo:
			{
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Toast.makeText(mActivity, "请插入SD卡后再试...", Toast.LENGTH_SHORT).show();
					return;
				}
				String fileName = UUID.randomUUID().toString() + ".jpg";
				PhoneFragment.this.mFileName = fileName;
				File file = new File(CommonUtil.FILE_PATH + fileName);
				if (!file.getParentFile().exists())file.getParentFile().mkdirs();
				try {
					Uri uri = Uri.fromFile(file);
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);	
			        startActivityForResult(intent, 1);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(mActivity, "未发现相机应用，请安装相关应用后重试...", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			default:
				break;
			}			
		}		
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "****** phone activity result / resultCode = " + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			FileOutputStream output = null;
			FileInputStream input = null;
			BufferedOutputStream bos = null;
			File file = new File(CommonUtil.FILE_PATH + this.mFileName);
			try {
				input = new FileInputStream(file);
				bm = BitmapFactory.decodeStream(input);
				output = new FileOutputStream(file);
				bos = new BufferedOutputStream(output);
				bm.compress(Bitmap.CompressFormat.JPEG,CommonUtil.COMPRESS_FORMAT, bos);// 把数据写入文件
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					output.close();
					input.close();
					bos.flush();
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			dialog = new ProgressDialog(mActivity);
			dialog.setMessage("照片上传中，请稍后...");
			dialog.show();
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
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
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
//			InputStream is = httpURLConnection.getInputStream();
//			InputStreamReader isr = new InputStreamReader(is, "utf-8");
//			BufferedReader br = new BufferedReader(isr);
//			final String result = br.readLine();
			if (responseCode == 200) {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mActivity, "上传成功,文件名称：" + mFileName, Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						if (bm!=null&&!bm.isRecycled()) {
							bm.recycle();
						}
					}
				});
			}else{
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mActivity, "上传失败", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						if (bm!=null&&!bm.isRecycled()) {
							bm.recycle();
						}
					}
				});
			}
			dos.close();
//			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
    @Override
	public void onResume() {
		super.onResume();
		sessionStatusPreference = mActivity.getSharedPreferences(CenterFragment2.SESSION_STATUS, Context.MODE_PRIVATE);
		userPreferences = mActivity.getSharedPreferences(CommonUtil.NEED_TOKENID, Context.MODE_PRIVATE);
		sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PATH, "PhoneFragment").commit();
    	sessionStatusPreference.edit().putString(CenterFragment2.SESSION_PHONE_PARENT_PATH, "null").commit();
	}

	/**
     * gridview项点击事件
     * @author Administrator
     *
     */
    private class ItemClickListener implements OnItemClickListener
    {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			if (6 == arg2)
			{
				mListener.onFragmentChange(CenterFragment2.DATA_MARKET_HOME, arg2);
			}
			else
			{
				mListener.onFragmentChange(CenterFragment2.PHONE_LIST_FRAGMENT, arg2);
			}			
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
}
