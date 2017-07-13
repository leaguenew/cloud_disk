package com.echoii.bean.register;

/**
 * 注册时返回的全部数据
 * @ClassName:RegisterResponseData 
 * @Description: 注册时服务器返回的全部数据
 * @author: Acer
 * @date: 2013-10-24
 *
 */
public class RegisterResponseData
{
    private String time = "";
    private RegisterDetailData registerDetail;
    
    public String getTime()
    {
        return time;
    }
    
    public void setTime(String time)
    {
        this.time = time;
    }
    
    public RegisterDetailData getRegisterDetail()
    {
        return registerDetail;
    }
    
    public void setRegisterDetail(RegisterDetailData registerDetail)
    {
        this.registerDetail = registerDetail;
    }
    
    
}
