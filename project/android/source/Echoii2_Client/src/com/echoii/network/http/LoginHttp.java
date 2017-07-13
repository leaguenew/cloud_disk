package com.echoii.network.http;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.echoii.bean.login.LoginDetailData;
import com.echoii.bean.login.LoginRequestData;
import com.echoii.bean.login.LoginResponseData;
import com.echoii.bean.login.LoginTimeDetailData;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;


public class LoginHttp
{
    private List<LoginResponseData> mLoginRspDataList = null;
    private HashMap<String,String> hash = null;
    private Handler mHandler;
    public LoginHttp( Handler handler,HashMap<String,String> hash)
    {
        mHandler = handler;
        mLoginRspDataList = new ArrayList<LoginResponseData>();
        this.hash = hash;
    }
    
    public void loginRequest()
    {
        new Thread()
        {
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    super.run();
                    loginRequestThread();
                }
        }.start();
    }
    
    private void loginRequestThread()
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
                returncode = parseLoginReturnCode(responseData);
            }
            else
            {
                Message msg = mHandler.obtainMessage();
                msg.what = MessageInfo.LOGIN_RESPONSE;
                msg.arg1 = MessageInfo.RETURN_RESPONSE_FAIL;
                mHandler.sendMessage(msg);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
            Message msg = mHandler.obtainMessage();
            msg.what = MessageInfo.LOGIN_RESPONSE;
            msg.arg1 = MessageInfo.RETURN_RESPONSE_FAIL;
            mHandler.sendMessage(msg);
        }    
        
        Message msg = mHandler.obtainMessage();
        msg.what = MessageInfo.LOGIN_RESPONSE;
        if (MessageInfo.RETURN_CODE_SUCCESS == returncode)
        {
            Log.d("mating","success");
            msg.arg1 = MessageInfo.MESSAGE_SUCCESS;
            msg.obj = mLoginRspDataList;
        }
        else if (MessageInfo.RETURN_CODE_PARAMS_WRONG == returncode)
        {
            msg.arg1 = MessageInfo.RETURN_CODE_PARAMS_WRONG;
        }
        else if (MessageInfo.RETURN_CODE_TOKEN_WRONG == returncode)
        {
        	 msg.arg1 = MessageInfo.RETURN_CODE_TOKEN_WRONG;
        }
        else if (MessageInfo.RETURN_CODE_SYSTEM_WRONG == returncode)
        {
        	 msg.arg1 = MessageInfo.RETURN_CODE_SYSTEM_WRONG;
        }
        else
        {
        	msg.arg1 = MessageInfo.MESSAGE_FAIL;
        }
            
        mHandler.sendMessage(msg);
        
    }
    
    private int parseLoginReturnCode(String json)
    {
        int returncode = 0;
        JSONObject mJSONObject = null;
        LoginResponseData responseData = new LoginResponseData();
        try
        {
            mJSONObject = new JSONObject(json);
            Log.d("mating", mJSONObject.getString("data"));
           
            returncode = Integer.parseInt(mJSONObject.getString("status"));
            if (200 != returncode)
            {
            	return returncode;
            }
            responseData.setTime(mJSONObject.getString("time"));
            
            LoginDetailData detailData = new LoginDetailData();
            JSONObject jsonDetailData = new JSONObject();
            jsonDetailData = mJSONObject.getJSONObject("data");
            detailData.setCreateDate(jsonDetailData.getString("createDate"));
            detailData.setEmail(jsonDetailData.getString("email"));
            detailData.setId(jsonDetailData.getString("id"));
            detailData.setNickName(jsonDetailData.getString("nickName"));
            detailData.setStatus(jsonDetailData.getString("status"));
            detailData.setToken(jsonDetailData.getString("token"));
            detailData.setUserId(jsonDetailData.getString("userId"));
            detailData.setUsername(jsonDetailData.getString("name"));
            
            JSONObject jsonCurrentLoginData = new JSONObject();                    
            jsonCurrentLoginData = jsonDetailData.getJSONObject("currentlogindate");
            LoginTimeDetailData currentTime = new LoginTimeDetailData();
            currentTime.setDate(jsonCurrentLoginData.getString("date"));
            currentTime.setDay(jsonCurrentLoginData.getString("day"));
            currentTime.setHours(jsonCurrentLoginData.getString("hours"));
            currentTime.setMinutes(jsonCurrentLoginData.getString("minutes"));
            currentTime.setMonth(jsonCurrentLoginData.getString("month"));
            currentTime.setSeconds(jsonCurrentLoginData.getString("seconds"));
            currentTime.setTime(jsonCurrentLoginData.getString("time"));
            currentTime.setTimezoneOffset(jsonCurrentLoginData.getString("timezoneOffset"));
            
//            JSONObject jsonLastLoginData = new JSONObject();
           
//            if ((jsonDetailData.getJSONObject("lastLoginDate")).equals(null))
//            {
//            	jsonLastLoginData = jsonDetailData.getJSONObject("lastLoginDate");    
//	            LoginTimeDetailData lastTime = new LoginTimeDetailData();
//	            lastTime.setDate(jsonLastLoginData.getString("date"));
//	            lastTime.setDay(jsonLastLoginData.getString("day"));
//	            lastTime.setHours(jsonLastLoginData.getString("hours"));
//	            lastTime.setMinutes(jsonLastLoginData.getString("minutes"));
//	            lastTime.setMonth(jsonLastLoginData.getString("month"));
//	            lastTime.setSeconds(jsonLastLoginData.getString("seconds"));
//	            lastTime.setTime(jsonLastLoginData.getString("time"));
//	            lastTime.setTimezoneOffset(jsonLastLoginData.getString("timezoneOffset"));    
//	            detailData.setLastLoginDate(lastTime);
//            }
            detailData.setCurrentlogindate(currentTime);
            responseData.setData(detailData);    
            mLoginRspDataList.add(responseData);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return returncode;
        
    }
    
    /**
     * 
     * <p>
     * Description: 拼接url值
     * <p>
     * @date 2013-10-24 
     * @return
     */
    private String getUrl()
    {
        String url = null;
        String username = hash.get("email");
        String pass = hash.get("password");
//        String username = mLoginRequest.get(0).getEmail();
//        String pass = mLoginRequest.get(0).getPassword();
		Log.d("mataing","request http username = " + username);
		Log.d("mating","request http password = " + pass);
//        SharedPreferences userInfo = mContext.getSharedPreferences(LoginActivity.USER_INFO, 0);  
//        String username = userInfo.getString(LoginActivity.USER_ACCOUNT, "");  
//       String pass = userInfo.getString(LoginActivity.PASSWORD, ""); 
//       Log.d("mating", " mLoginRequest.get(0).getEmail(); = " +  mLoginRequest.get(0).getEmail());
//       Log.d("mating", " mLoginRequest.get(0).getPassword(); = " +  mLoginRequest.get(0).getPassword());
        url = BaseUrl.LOGIN_URL + "?email=" + username + "&password=" + pass;
        return url;
    }
    

}
