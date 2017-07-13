package com.echoii.bean.login;

/**
 * 
 * 登陆请求信息类
 * @ClassName:LoginRequestData 
 * @Description: 这里用一句话描述这个类的作用 
 * @author: Acer
 * @date: 2013-10-24
 *
 */
public class LoginRequestData
{
    private String email;
    private String password;
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
}
