package com.echoii.network.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;

public class DownAndOpenFileHttp {
 
	private Handler mHandler;
	private HashMap<String,String>  mRequest;
	public DownAndOpenFileHttp(Handler handler,HashMap<String,String> request)
	{
		mHandler = handler;
		mRequest = request;
	}
	
	public void downFile()
	{
		new Thread()
		{
			public void run() 
			{
				super.run();
				getDownFileThread();
			}
		}.start();
	}
	
	private void getDownFileThread()
	{
		 String name = mRequest.get("name").toString();
		 String url =  getUrl(name);
		 Log.d("mating","down url = " + url);
		
		try {
			 URL realUrl = new URL(url);
	         HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
	         conn.setConnectTimeout(20000);
	         conn.setRequestMethod("GET");
	         
	         int responseCode = conn.getResponseCode();
	         Log.d("mating","responseCode = " + responseCode);
	         if (200 == conn.getResponseCode())
	         {
	        	 String filePath = writeFile(conn.getInputStream(),name);     
	           	 Message msg = mHandler.obtainMessage();
         		 msg.what = MessageInfo.DOWN_OPEN_FILE;

            	msg.obj = filePath;            	
        		mHandler.sendMessage(msg);
	         }
	         else
	         {
	        	 Message msg = mHandler.obtainMessage();
	        	 msg.what = MessageInfo.DOWN_OPEN_FILE_ERROE;
	        	 mHandler.sendMessage(msg);
	         }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
	}
	
	private String writeFile(InputStream is,String name) 
	{
		 String dir = Environment.getExternalStorageDirectory() +"/echoii/cloud";
		 String type = mRequest.get("type").toString();
		 String dirName = "";
		 if (type.equals(BaseUrl.LIST_IMAGE))
		 {
			 dirName = "/image/";
		 }
		 else if (type.equals(BaseUrl.LIST_MUSIC))
		 {
			 dirName = "/music/";
		 }
		 else if(type.equals(BaseUrl.LIST_VIDEO))
		 {
			 dirName = "/video/";
		 }
		 else if(type.equals(BaseUrl.LIST_DOC))
		 {
			 dirName = "/doc/";
		 }
		 else
		 {
			 dirName = "/other/";
		 }
		 
    	 File f = new File(dir + dirName);
    	 if(!f.exists())
    	 {
    	     f.mkdirs();
    	 }
    	String filePath = dir + dirName + name;
    	Log.d("mating","writeFile filePath = " + filePath);
    	File file = new File(filePath);
    	if (file.exists())
    	{
    		return filePath;
    	}
    	try
    	{
		    byte[] buffer = new byte[1024];
		    OutputStream fout = new FileOutputStream(filePath);
		    int len; 
		    while ((len = is.read(buffer)) != -1) 
		    {   
		    	fout.write(buffer, 0, len);   
	        }  
		    fout.flush();
		    is.close();
		    fout.close();
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	 
    	return filePath;
    	
	}
	private String getUrl(String name)
	{
		String url = "";
		String userId = mRequest.get("id").toString();
		String token = mRequest.get("token").toString();
		String fileId = mRequest.get("fileid").toString();

		
		Log.d("mating","getDownFileThread userId = " + userId 
				+ " ; token = " + token + ";fileId = " + fileId + "; name = " + name);
		
		url = BaseUrl.BASE_URL + "/download"  + "?user_id=" +  userId 
				+ "&token=" + token + "&file_id=" + fileId;
		return url;
	}
}
