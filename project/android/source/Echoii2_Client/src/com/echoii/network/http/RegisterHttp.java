package com.echoii.network.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.echoii.bean.register.RegisterDetailData;
import com.echoii.bean.register.RegisterRequestData;
import com.echoii.bean.register.RegisterResponseData;
import com.echoii.constant.BaseUrl;
import com.echoii.constant.MessageInfo;


public class RegisterHttp
{
    private Handler mHandler;
    private RegisterResponseData mRegisterRspData;
    private HashMap<String,Object>  mRequest = null;
    public RegisterHttp(Handler handler,HashMap<String,Object> hash)
    {
        mHandler = handler;
        mRegisterRspData = new RegisterResponseData();
        mRequest = hash;
    }
    
    public void registerRequest()
    {
        new Thread()
        {
                @Override
                public void run()
                {
                    // TODO Auto-generated method stub
                    super.run();
                    registerRequestThread();
                }
        }.start();
    }
    
    private void registerRequestThread()
    {
        int returncode = 0;   
        
        try
        {
            String url = getUrl();
            Log.d("mating","register url = " + url);
            URL  realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            Log.d("mating","register conn.getResponseCode() = " + responseCode);
            if (responseCode == 200)
            {
                byte[] data = BaseUrl.readStream(conn.getInputStream());
                String responseData = new String(data, "UTF-8");
                Log.d("mating","register responseData=" + responseData );
                returncode = parseRegisterReturnCode(responseData);
            }
            else
            {
                Message msg = mHandler.obtainMessage();
                msg.what = MessageInfo.REGISTER_RESPONSE;
                msg.arg1 = MessageInfo.RETURN_RESPONSE_FAIL;
                mHandler.sendMessage(msg);
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();
            Message msg = mHandler.obtainMessage();
            msg.what = MessageInfo.REGISTER_RESPONSE;
            msg.arg1 = MessageInfo.MESSAGE_FAIL;
            mHandler.sendMessage(msg);
        }    
        
        Message msg = mHandler.obtainMessage();
        msg.what = MessageInfo.REGISTER_RESPONSE;
        if (MessageInfo.RETURN_CODE_SUCCESS == returncode)
        {
            Log.d("mating","success");
            msg.arg1 = MessageInfo.MESSAGE_SUCCESS;
        }
        else if (MessageInfo.RETURN_CODE_PARAMS_WRONG == returncode)
        {
            msg.arg1 = MessageInfo.RETURN_CODE_PARAMS_WRONG;
        }
        else if (MessageInfo.RETURN_CODE_USERNAME_SAME == returncode)
        {
            msg.arg1 = MessageInfo.RETURN_CODE_USERNAME_SAME;
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
    
    private int parseRegisterReturnCode(String json)
    {
        int returncode = 0;
        try
        {
            JSONObject mJsonObject = new JSONObject(json);
            mRegisterRspData.setTime(mJsonObject.getString("time"));
            returncode = Integer.parseInt(mJsonObject.getString("status"));
            if (200 != returncode)
            {
            	return returncode;
            }
            JSONObject registerDetail = new JSONObject();
            registerDetail = mJsonObject.getJSONObject("data");
            RegisterDetailData detail = new RegisterDetailData();
            detail.setCreateDate(registerDetail.getString("createDate"));
            detail.setEmail(registerDetail.getString("email"));
            detail.setId(registerDetail.getString("id"));
            detail.setLastLoginDate(registerDetail.getString("lastLoginDate"));
            detail.setNickName(registerDetail.getString("nickName"));
            detail.setStatus(registerDetail.getString("status"));
            detail.setUserId(registerDetail.getString("userId"));
            detail.setUsername(registerDetail.getString("name"));
            mRegisterRspData.setRegisterDetail(detail);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            returncode = MessageInfo.PARSE_EXCEPTION;
            return returncode;
        }           
        return returncode;
    }
    private String getUrl()
    {
        String url = null;
        String account =  (String) mRequest.get("account");
        String password = (String)mRequest.get("password");
        Log.d("mating","account = " + account + " password = " + password);
    
        url = BaseUrl.REGISTER_URL + "?email=" + account + "&password=" + password;
        
        return url;
    }
}
