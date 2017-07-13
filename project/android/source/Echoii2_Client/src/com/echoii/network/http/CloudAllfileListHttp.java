package com.echoii.network.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.echoii.bean.cloudfilelist.AllFileDetailData;
import com.echoii.bean.cloudfilelist.AllFileResponseData;
import com.echoii.bean.cloudfilelist.CloudFileListRequestData;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;

public class CloudAllfileListHttp {
	
	private Handler mHandler;
	private List<CloudFileListRequestData> mRequest = null;
	private List<AllFileDetailData>  mResponseListData = null;
	
	public CloudAllfileListHttp(Handler handler,List<CloudFileListRequestData> request,List<AllFileDetailData>  allfileListData)
	{
		mHandler = handler;
		mRequest = request;
		mResponseListData = allfileListData;
	}
	
	  public void getAllfileRequest()   
 	  {
         new Thread()
         {
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    super.run();
                    getAllfileRequestThread();
                }
         }.start();
     }
	  
	  private void getAllfileRequestThread()
	  {
		  int returncode = 0;       
	        
	        try
	        {
	            Log.d("mating","loginRequestThread");
	            //String url = "http://172.21.7.199:81/echoii/service/0/mobile/auth/login?email=456@qq.com&password=1234";          
	            String url = getUrl();
	            Log.d("mating","url = " + url);
	            URL  realUrl = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
	            conn.setConnectTimeout(20000);
	            conn.setRequestMethod("GET");
	            
	            int responseCode = conn.getResponseCode();
	            Log.d("mating","conn.getResponseCode() = " + responseCode);
	            if (responseCode == 200)
	            {
	                byte[] data = BaseUrl.readStream(conn.getInputStream());
	                String responseData = new String(data, "UTF-8");
	                Log.d("mating","responseData=" + responseData );
	                returncode = parseAllFileReturnCode(responseData,mResponseListData);
	                Log.d("mating","returncode=" + returncode );
	            }
	            else
	            {
	            	Message msg = mHandler.obtainMessage();
	      	        msg.what = MessageInfo.ALLFILE;
	      	        msg.arg1 = responseCode;
	      	        mHandler.sendMessage(msg);
	            }
	        }
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        	Message msg = mHandler.obtainMessage();
	        	msg.what = MessageInfo.ALLFILE;
	        	msg.arg1 = MessageInfo.MESSAGE_FAIL;
	        	mHandler.sendMessage(msg);
	        }
	        Message msg = mHandler.obtainMessage();
	        msg.what = MessageInfo.ALLFILE;
	        //解析数据成功
	        if (MessageInfo.RETURN_CODE_SUCCESS == returncode)
	        {
	            msg.arg1 = MessageInfo.MESSAGE_SUCCESS;
	        }
	        else if (0 == returncode)
	        {
	        	 if (0 == Integer.parseInt(mRequest.get(0).getBegin()))
	        	 {        	
	        		 msg.arg1 = MessageInfo.MESSAGE_SUCCESS_NULL;
	        	 }
	        	 else
	        	 {
	        		 msg.arg1 = MessageInfo.MESSAGE_SUCCESS_END;
	        	 }
	        }
	        else
	        {
	            msg.arg1 = MessageInfo.MESSAGE_FAIL;
	            msg.arg2 = returncode;
	        }
	            
	        mHandler.sendMessage(msg);
	  }
	  
	  private int parseAllFileReturnCode(String response,List<AllFileDetailData> list)
	  {
		  int returncode = 0;
		  
		  JSONObject mJSONObject = null;
		  if (null == list)
		  {
			  return -1;
		  }
		  
		  try
		  {
			  AllFileResponseData responseData = new AllFileResponseData();
			  mJSONObject = new JSONObject(response);
			  returncode = Integer.parseInt(mJSONObject.getString("status"));
			  if (200 != returncode)
			  {
				  return returncode;
			  }
			  JSONArray jsonDetailData = mJSONObject.getJSONArray("data");
//			  Log.d("mating","json jsonDetailData = " + jsonDetailData 
//					  + "; jsonDetailData.length()" + (jsonDetailData.length()));
			  if (jsonDetailData.length() == 0)
			  {
				  returncode = 0;
				  return returncode;
			  }
			
			  responseData.setTime(mJSONObject.getString("time"));

			  for (int i = 0; i < jsonDetailData.length(); i++)
			  {
				 
				  JSONObject json = (JSONObject)jsonDetailData.opt(i);
				  AllFileDetailData detail = new AllFileDetailData();
				  
				
				  detail.setId(json.getString("id"));
				  detail.setLastDate(json.getString("lmf_date"));				 
				  detail.setName(json.getString("name"));
				  detail.setMetaFolder(json.getString("metaFolder"));
				  detail.setMetaId(json.getString("metaId"));
				  detail.setParentId(json.getString("folderId"));
				  detail.setPath(json.getString("path"));
				  detail.setSize(json.getString("size"));
				  detail.setStatus(json.getString("status"));
				  detail.setType(json.getString("type"));
				  detail.setUserId(json.getString("userId"));
				  
				  list.add(detail);
			
			  }
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
		  
		  return returncode;
	  }
	  
	  /**
	   * http://{domain}/service/0
	   * /file/list?user_id=123&token=abc&folder_id=def&size=40&begin=0&order=desc&order_by=name
	   * @return
	   */
	  private String getUrl()
	  {
		  String url = "";
		  String reqType = mRequest.get(0).getReqType();
		  
		  String userId = mRequest.get(0).getUserId();
		  String token = mRequest.get(0).getToken();
		  String size = mRequest.get(0).getSize();
		  String begin = mRequest.get(0).getBegin();		  
		  String folderId = mRequest.get(0).getFolder_id();
		  String order = mRequest.get(0).getOrder();
		  String orderBy = mRequest.get(0).getOrder_by();
		  
		  Log.d("mating","begin = " + begin + "; size = " + size + "; token = " 
				  + token + "; userId" + userId + "; folderId" 
				  + folderId + "; order"  + order + "; orderBy" + orderBy);
		  Log.d("mating","reqType = " + reqType);
		  String sub = "?user_id=" +  userId + "&token=" + token 
				  + "&size=" + size + "&begin="
				  + begin ;
		  if (reqType.equals(BaseUrl.LIST_ALLFILE))
		  {			 
			  if (TextUtils.isEmpty(folderId))
			  {
				  url = BaseUrl.ALLFILE_URL + sub;
			  }
			  else
			  {
				  url = BaseUrl.ALLFILE_URL + "?user_id=" +  userId + "&token=" + token 
						  + "&folder_id=" + folderId + "&size=" + size + "&begin="
						  + begin;
			  } 
		  }
		  else if (reqType.equals(BaseUrl.LIST_IMAGE))
		  {
			  url = BaseUrl.IMAGE_URL + sub; 
		  }
		  else if (reqType.equals(BaseUrl.LIST_MUSIC))
		  {
			  url = BaseUrl.MUSIC_URL + sub;
		  }
		  else if (reqType.equals(BaseUrl.LIST_VIDEO))
		  {
			  url = BaseUrl.VIDEO_URL + sub;
		  }
		  else if (reqType.equals(BaseUrl.LIST_BT))
		  {
			  url = BaseUrl.BT_URL + sub;
		  }
		  else if (reqType.equals(BaseUrl.LIST_DOC))
		  {
			  url = BaseUrl.DOC_URL + sub;
		  }
		  else if (reqType.equals(BaseUrl.LIST_OTHER))
		  {
			  url = BaseUrl.OTHER_URL + sub;
		  }
		  else if (reqType.equals(BaseUrl.LIST_GROUP))
		  {
			  url = BaseUrl.GROUP_URL + sub;
		  }
		  else if (reqType.equals(BaseUrl.LIST_SHARE))
		  {
			  url = BaseUrl.SHARE_URL + sub;
		  }
//		  url = BaseUrl.ALLFILE + "?user_id=" +  userId + "&token=" + token 
//				  + "&folder_id=" + folderId + "&size=" + size + "&begin="
//				  + begin + "&order=" + order + "&order_by="+ orderBy;

			 

		  return url;
	  }
}
