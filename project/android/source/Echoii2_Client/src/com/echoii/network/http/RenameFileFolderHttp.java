package com.echoii.network.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import com.echoii.bean.cloudfilelist.AllFileDetailData;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RenameFileFolderHttp 
{
	private Handler mHandler;
	private HashMap<String,String> mRequest;	
	private AllFileDetailData fileData;
	
	public RenameFileFolderHttp(Handler handler,HashMap<String,String> request)
	{
		mHandler = handler;
		fileData = new AllFileDetailData();
		mRequest = request;
	}
	
	public void renameFileFolderThread()
	{
		new Thread()
		{
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				super.run();
				renameRequest();
			}
		}.start();
	}
	
	private void renameRequest()
	{
		int returncode = 0;
        String url = getUrl();
        Log.d("mating","register url = " + url);
        try
        {
	        URL  realUrl = new URL(url);
	        HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
	        conn.setConnectTimeout(20000);
	        conn.setRequestMethod("GET");
	        
	        int responseCode = conn.getResponseCode();
	        Log.d("mating","rename conn.getResponseCode() = " + responseCode);
	        if (200 == responseCode)
	        {
                byte[] data = BaseUrl.readStream(conn.getInputStream());
                String responseData = new String(data, "UTF-8");
                Log.d("mating","register responseData=" + responseData );                
                returncode = parseRenameReturnCode(responseData);
	        }
	        
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            Message msg = mHandler.obtainMessage();
            msg.what = MessageInfo.RENAME_RESPONSE;
            msg.arg1 = MessageInfo.MESSAGE_FAIL;
            mHandler.sendMessage(msg);
        }
        
        Message msg = mHandler.obtainMessage();
        msg.what = MessageInfo.RENAME_RESPONSE;
        
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
	

	private int parseRenameReturnCode(String json)
	{
		int returncode = 0;
		try
		{
			JSONObject mJsonObject = new JSONObject(json);
			returncode = Integer.parseInt(mJsonObject.getString("status"));
			 JSONObject renameDetail = new JSONObject();
	         renameDetail = mJsonObject.getJSONObject("data");
	         fileData.setId(renameDetail.getString("id"));
	         fileData.setLastDate(renameDetail.getString("lmf_date"));
	         fileData.setName(renameDetail.getString("name"));
	         fileData.setParentId(renameDetail.getString("folderId"));
	         fileData.setPath(renameDetail.getString("path"));
	         fileData.setSize(renameDetail.getString("size"));
	         fileData.setStatus(renameDetail.getString("status"));
	         fileData.setType(renameDetail.getString("type"));
	         fileData.setUserId(renameDetail.getString("userId"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		  
		return returncode;
	}
	
	
	private String getUrl()
	{
		String url = "";
		String userId = mRequest.get("user_id");
		String token = mRequest.get("token");
		String fileId = mRequest.get("fileId");
		String newname = mRequest.get("newname");
		url = BaseUrl.RENAME_URL + "user_id=" + userId + "&token=" + token + "&file_id=" 
				+ fileId + "&new_name=" + newname;
		return url;
	}
}
