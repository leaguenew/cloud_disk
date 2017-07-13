package com.echoii.activity;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.echoii.constant.CommonUtil;

public class UploadPhotoActivity extends Activity {
	public static final String TAG = "UploadPhotoActivity";
	public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "echoii" + File.separator + "phone" + File.separator ;
	public String url = "http://172.21.7.199:81/echoii/upload";
	
	private String fileName;
	private ImageView photoImageView;
	private Button takePhotoButton;
	private Button uploadButton;
	private TextView backText;
	private ProgressDialog dialog;
	private Bitmap bm = null;
	
	private SharedPreferences userPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		userPreferences = this.getSharedPreferences(CommonUtil.NEED_TOKENID, Context.MODE_PRIVATE);
		initViews();
	}

	private void initViews() {
		photoImageView = (ImageView) this.findViewById(R.id.photo);
		takePhotoButton = (Button) this.findViewById(R.id.take_photo);
		uploadButton = (Button) this.findViewById(R.id.upload_photo);
		backText = (TextView) this.findViewById(R.id.back);
		takePhotoButton.setOnClickListener(new OnClickListenerImpl());
		uploadButton.setOnClickListener(new OnClickListenerImpl());
		backText.setOnClickListener(new OnClickListenerImpl());
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private class OnClickListenerImpl implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.take_photo:
				if (bm!=null) {
					bm.recycle();
					bm = null;
				}
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Toast.makeText(UploadPhotoActivity.this, "请插入SD卡后再试...", Toast.LENGTH_SHORT).show();
					return;
				}
				String fileName = UUID.randomUUID().toString() + ".jpg";
				UploadPhotoActivity.this.fileName = fileName;
				File file = new File(FILE_PATH + fileName);
				if (!file.getParentFile().exists())file.getParentFile().mkdirs();
				try {
					Uri uri = Uri.fromFile(file);
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);	
			        startActivityForResult(intent, 1);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(UploadPhotoActivity.this, "未发现相机应用，请安装相关应用后重试...", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.upload_photo:
//				if (photoImageView.getDrawable() != null) {
//					Toast.makeText(UploadPhotoActivity.this, "已拍照", Toast.LENGTH_SHORT).show();
//				}else{
//					Toast.makeText(UploadPhotoActivity.this, "未拍照", Toast.LENGTH_SHORT).show();
//				}
				dialog = new ProgressDialog(UploadPhotoActivity.this);
				dialog.setMessage("照片上传中，请稍后...");
				dialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						uploadFile();
					}
				}).start();
				break;
			case R.id.back:
				UploadPhotoActivity.this.finish();
				break;
			default:
				break;
			}
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
			URL url = new URL(this.url);
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
			dos.writeBytes("Content-Disposition: form-data; name=\"upload\"; filename=\"" + fileName + "\"" + end);
			dos.writeBytes("Content-Type: image/jpeg"+end);
			dos.writeBytes(end);
			
			String path = FILE_PATH + this.fileName;
			Log.d(TAG, "input steam file name = " + fileName);
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
//			InputStream is = httpURLConnection.getInputStream();
//			InputStreamReader isr = new InputStreamReader(is, "utf-8");
//			BufferedReader br = new BufferedReader(isr);
//			final String result = br.readLine();
			if (responseCode == 200) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(UploadPhotoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
			}else{
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(UploadPhotoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}
				});
			}
			dos.close();
//			is.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			FileOutputStream output = null;
			FileInputStream input = null;
			File file = new File(FILE_PATH + fileName);
			try {
				input = new FileInputStream(file);
				bm = BitmapFactory.decodeStream(input);
				output = new FileOutputStream(file);
				bm.compress(Bitmap.CompressFormat.JPEG, 80, output);// 把数据写入文件
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
			photoImageView.setImageBitmap(bm);
		}
	}

}
