package com.echoii.network.http;


import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.echoii.bean.cloudfilelist.Thumbnails;
import com.echoii.constant.MessageInfo;

public class GetImageThumbnailHttp 
{
	private String url = "";
	private Handler mHandler;
	private ImageView imageView;
	
	public GetImageThumbnailHttp(Handler handler,ImageView img ,String url)
	{
		mHandler = handler;
		imageView = img;
		this.url = url;
	}
	
	public void getImageBitmap()
	{
		new Thread()
		{
			public void run() 
			{
				super.run();
				getBitmap();
			}
		}.start();
	}
	
	private void getBitmap()
	{
		Bitmap bitmap = null;
        try
        {
        	Log.d("mating","http url = " + url);
//            URL url = new URL(this.url);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(20000);
//            
//            int responseCode = connection.getResponseCode();
//            Log.d("mating","responseCode = " + responseCode);
//            
//            InputStream input = connection.getInputStream();
//           
//            bitmap = BitmapFactory.decodeStream(input);
//            input.close();
        	HttpGet httpRequest = new HttpGet(url);
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
        	 Log.d("mating","response = " + response);
        	HttpEntity entity = response.getEntity();
        	BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
        	InputStream is = bufferedHttpEntity.getContent();
        	bitmap = BitmapFactory.decodeStream(is);
        	is.close();
        }
        catch (ClientProtocolException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Message msg = mHandler.obtainMessage();
        
        msg.what = MessageInfo.IMAGE_THUMBNAILS;
        msg.obj = new Thumbnails(imageView, url, bitmap);
        mHandler.sendMessage(msg);
	}
}
