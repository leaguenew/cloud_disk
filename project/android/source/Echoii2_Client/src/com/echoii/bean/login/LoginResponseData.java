package com.echoii.bean.login;

import java.util.List;

/**
 * 
 * 登陆返回数据 信息类 
 * @ClassName:LoginResponseData 
 * @Description: 登陆操作之后返回的数据，是否登陆成功等
 * @author: Acer
 * @date: 2013-10-24
 *
 */
public class LoginResponseData
{
    /**登陆状态 */
//    private String status ;
    /**登陆时间*/
    private String time;
    /**登陆信息*/
    private LoginDetailData data;
    
    private List<LoginResponseData> mLoginRspData;
    
    
      
    public List<LoginResponseData> getmLoginRspData() {
		return mLoginRspData;
	}


	public void setmLoginRspData(List<LoginResponseData> mLoginRspData) {
		this.mLoginRspData = mLoginRspData;
	}


	public String getTime()
    {
        return time;
    }

    
    public void setTime(String time)
    {
        this.time = time;
    }

    
    public LoginDetailData getData()
    {
        return data;
    }

    
    public void setData(LoginDetailData data)
    {
        this.data = data;
    }


}
