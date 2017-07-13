package com.echoii.network.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.echoii.bean.cloudfilelist.AllFileDetailData;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;

public class DeleteFileFolderHttp 
{
	private Handler mHandler;
	private HashMap<String,Object> mRequest;
	private List<Integer> mReturnCode;
	private AllFileDetailData fileData;
	public DeleteFileFolderHttp(Handler handler,HashMap<String,Object> request)
	{
		mHandler = handler;
		mRequest = request;
		fileData = new AllFileDetailData();
		mReturnCode = new ArrayList<Integer>();
	}
	
	public void deleteThread()
	{
		new Thread()
		{
			public void run()
			{
				super.run();
				deleteFileFolderRequest();
			}
		}.start();
	}
	
	private void deleteFileFolderRequest()
	{
		int returncode = 0;
		String url = "";
		String fileId = "";
		String userId = (String)mRequest.get("user_id");
		String token = (String)mRequest.get("token");
		List<String> listfileId = (List<String>)mRequest.get("filefolderId");
		
	
		for (int i = 0; i < listfileId.size(); i++)
		{
			fileId = listfileId.get(i);
			url = BaseUrl.DEL_URL + url + "user_id=" + userId + "&token=" + token + "&file_id=" + fileId;
			
			Log.d("mating","del url = " + url);
			try
			{
				URL  realUrl = new URL(url);
		        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
		        conn.setConnectTimeout(20000);
		        conn.setRequestMethod("GET");
		        
		        int responseCode = conn.getResponseCode();
		        Log.d("mating","del conn.getResponseCode() = " + responseCode);
		        if (200 == responseCode)
		        {
	                byte[] data = BaseUrl.readStream(conn.getInputStream());
	                String responseData = new String(data, "UTF-8");
	                Log.d("mating","register responseData=" + responseData );   
	    			returncode = parseDeleteReturnCode(responseData);	    	         
	    			mReturnCode.add(returncode);
		        }		        
			}
			catch(Exception e)
			{
				e.printStackTrace();
				Message msg = mHandler.obtainMessage();
				msg.what = MessageInfo.DELETE_RESPONSE;
				msg.arg1 = returncode;
				mHandler.sendMessage(msg);
			}
			url = "";
		}
		
		Message msg = mHandler.obtainMessage();
		msg.what = MessageInfo.DELETE_RESPONSE;
		for (int i = 0; i < mReturnCode.size(); i++)
		{
			if (200 != mReturnCode.get(i))
			{
				returncode = 2;
			}
			else
			{
				returncode = 200;
			}
		}
        if (MessageInfo.RETURN_CODE_SUCCESS == returncode)
        {
        	 msg.arg1 = MessageInfo.MESSAGE_SUCCESS;
        	 msg.obj = fileData;
        }
        else
        {
        	 msg.arg1 = MessageInfo.MESSAGE_FAIL;
        }
        
        mHandler.sendMessage(msg);
		
	}
	
	
	private int parseDeleteReturnCode(String json)
	{
		int returncode = 0;
		try
		{
			 JSONObject mJsonObject = new JSONObject(json);
			 returncode = Integer.parseInt(mJsonObject.getString("status"));
			 JSONObject detail = new JSONObject();
	         detail = mJsonObject.getJSONObject("data");
	         fileData.setId(detail.getString("id"));
	         fileData.setLastDate(detail.getString("lmf_date"));
	         fileData.setName(detail.getString("name"));
	         fileData.setParentId(detail.getString("folderId"));
	         fileData.setPath(detail.getString("path"));
	         fileData.setSize(detail.getString("size"));
	         fileData.setStatus(detail.getString("status"));
	         fileData.setType(detail.getString("type"));
	         fileData.setUserId(detail.getString("userId"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		  
		return returncode;
	}
	
}
