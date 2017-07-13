package com.echoii.bean.register;

/**
 * 
 * 注册时返回的详细数据
 * @ClassName:RegisterDetailData 
 * @Description: 注册时返回的详细数据，如 创建时间，用户名、昵称之类
 * @author: Acer
 * @date: 2013-10-24
 *
 */
public class RegisterDetailData
{
    private String createDate;
    private String email;
    private String id;
    private String lastLoginDate;
    private String nickName;
    private String status;
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
    
    public String getLastLoginDate()
    {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(String lastLoginDate)
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
