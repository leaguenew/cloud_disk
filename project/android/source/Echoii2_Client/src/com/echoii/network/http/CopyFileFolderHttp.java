package com.echoii.network.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.echoii.bean.cloudfilelist.AllFileDetailData;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;

public class CopyFileFolderHttp 
{
	private Handler mHandler;
	private HashMap<String,Object> mRequest;
	private List<Integer> mReturnCode;
//	private AllFileDetailData fileData;
	
	public CopyFileFolderHttp(Handler handler,HashMap<String,Object> request)
	{
		mHandler = handler;
		mRequest = request;
//		fileData = new AllFileDetailData();
		mReturnCode = new ArrayList<Integer>();
	}
	
	public void copyFileFolderThread()
	{
		new Thread()
		{
			public void run() 
			{
				super.run();
				
				copyFileFolderRequest();
			}
		}.start();
	}
	
	private void copyFileFolderRequest()
	{
		int returncode = 0;
		String url = "";
		String fileId = "";
		String userId = (String)mRequest.get("user_id");
		String token = (String)mRequest.get("token");
		List<String> listfileId = (List<String>)mRequest.get("originalIds");
		
		
		for (int i = 0; i < listfileId.size(); i++)
		{
			String destFolderId = (String)mRequest.get("destIds");
			fileId = listfileId.get(i);
			url = BaseUrl.COPY_URL + url + "user_id=" + userId + "&token=" + token + "&file_id=" + fileId + "&folder_id=" + destFolderId;
			Log.d("mating","COPY_URL url = " + url);
			try
			{
				URL  realUrl = new URL(url);
		        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
		        conn.setConnectTimeout(20000);
		        conn.setRequestMethod("GET");
		        
		        int responseCode = conn.getResponseCode();
		        Log.d("mating","COPY_URL conn.getResponseCode() = " + responseCode);
		        if (200 == responseCode)
		        {
	                byte[] data = BaseUrl.readStream(conn.getInputStream());
	                String responseData = new String(data, "UTF-8");
	                Log.d("mating","COPY_URL responseData=" + responseData );   
		   		    JSONObject mJsonObject = new JSONObject(responseData);
					returncode = Integer.parseInt(mJsonObject.getString("status"));
					
					if (mJsonObject.isNull("data"))
					{ 
						 returncode = 0; //该功能暂时还未实现，用此代替，后服务器端开通后，即可去除
					}
					mReturnCode.add(returncode);
		        }		        
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = MessageInfo.COPY_RESPONSE;
				Log.d("mating copy catch", "returncode = " + returncode);
				msg.arg1 = returncode;
				mHandler.sendMessage(msg);
			}
			url = "";
		}
		
		int rtnCode = -1;
		Message msg = mHandler.obtainMessage();
		msg.what = MessageInfo.COPY_RESPONSE;
		for (int i = 0; i < mReturnCode.size(); i++)
		{
			if (0 == mReturnCode.get(i)) 
			{
				rtnCode = MessageInfo.MESSAGE_FAIL_UNDO;
			}
			else if (200 == mReturnCode.get(i))
			{
				rtnCode = MessageInfo.RETURN_CODE_SUCCESS;
			}
			else
			{
				rtnCode = MessageInfo.MESSAGE_FAIL;
			}
		}
		
		Log.d("mating copy", "rtnCode = " + rtnCode);
        if (MessageInfo.RETURN_CODE_SUCCESS == rtnCode)
        {
        	 msg.arg1 = MessageInfo.MESSAGE_SUCCESS;
        }
        else if (MessageInfo.MESSAGE_FAIL_UNDO == rtnCode)
        {
        	msg.arg1 = MessageInfo.MESSAGE_FAIL_UNDO;
        }
        else
        {
        	 msg.arg1 = MessageInfo.MESSAGE_FAIL;
        }
        
        mHandler.sendMessage(msg);
	}
}
