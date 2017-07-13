package com.echoii.bean.login;


/**
 * 
 * 某某某类
 * @ClassName:LoginData 
 * @Description: 登陆信息 数据
 * @author: Acer
 * @date: 2013-10-24
 *
 */
public class LoginDetailData
{
    private String createDate;
    private LoginTimeDetailData currentlogindate;
    private String email;
    private String id;
    private LoginTimeDetailData lastLoginDate;
    private String nickName;
    private String status;
    private String token;
    private String userId;
    private String username;
    
    public String getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }
    
    public LoginTimeDetailData getCurrentlogindate()
    {
        return currentlogindate;
    }
    
    public void setCurrentlogindate(LoginTimeDetailData currentlogindate)
    {
        this.currentlogindate = currentlogindate;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public LoginTimeDetailData getLastLoginDate()
    {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(LoginTimeDetailData lastLoginDate)
    {
        this.lastLoginDate = lastLoginDate;
    }
    
    public String getNickName()
    {
        return nickName;
    }
    
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
    
    public String getUserId()
    {
        return userId;
    }
    
    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
}
