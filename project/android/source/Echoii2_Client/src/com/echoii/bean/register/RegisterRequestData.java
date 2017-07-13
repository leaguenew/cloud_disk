package com.echoii.bean.register;

/**
 * 
 * 注册请求数据类
 * @ClassName:RegisterRequestData 
 * @Description: 注册请求时需要的字段，（目前还缺少头像）
 * @author: Acer
 * @date: 2013-10-24
 *
 */
public class RegisterRequestData
{
    private String account = "";
    private String password = "";
    private String nick ="";
    private String sex;
    private String birthday;
    private String qq;
    private String phone;
    private String mail;
    private String microblog;
    private String signature;
    

    
    
    public String getAccount()
    {
        return account;
    }

    
    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getNick()
    {
        return nick;
    }
    
    public void setNick(String nick)
    {
        this.nick = nick;
    }
    
    public String getSex()
    {
        return sex;
    }
    
    public void setSex(String sex)
    {
        this.sex = sex;
    }
    
    public String getBirthday()
    {
        return birthday;
    }
    
    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }
    
    public String getQq()
    {
        return qq;
    }
    
    public void setQq(String qq)
    {
        this.qq = qq;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public String getMail()
    {
        return mail;
    }
    
    public void setMail(String mail)
    {
        this.mail = mail;
    }
    
    public String getMicroblog()
    {
        return microblog;
    }
    
    public void setMicroblog(String microblog)
    {
        this.microblog = microblog;
    }
    
    public String getSignature()
    {
        return signature;
    }
    
    public void setSignature(String signature)
    {
        this.signature = signature;
    }
    
    
}
