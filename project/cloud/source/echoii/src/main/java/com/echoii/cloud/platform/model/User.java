package com.echoii.cloud.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.annotations.Type;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table( name = "user")
public class User extends Base{
	
	public static final String STATUS_ACTIVE = "active";
	public static final String STATUS_BLOCKED = "blocked";
	public static final String STATUS_REGISTER = "register";
	
	private String idcard;
	private String name;
	private String nickName;
	private String email;
	private String password;
	private String pwValidCode;
	private String status;
	private Date lastLoginDate;
	private Date ValidCodePeriod;
	
	
	@Column(name = "idcard")
	@Type(type="string")
	public String getIdCard() {
		return idcard;
	}
	public void setIdCard(String userId) {
		this.idcard = userId;
	}
	
	@Column(name = "name")
	@Type(type="string")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "nick_name")
	@Type(type="string")
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	@Column(name = "email")
	@Type(type="string")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "password")
	@Type(type="string")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "pw_valid_code")
	@Type(type="string")
	public String getPwValidCode() {
		return pwValidCode;
	}
	public void setPwValidCode(String pwValidCode) {
		this.pwValidCode = pwValidCode;
	}
	
	@Column(name = "status")
	@Type(type="string")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "last_login_date")
	@Type(type="java.util.Date")
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	@Column(name = "valid_code_period")
	@Type(type = "java.util.Date")
	public Date getValidCodePeriod() {
		return ValidCodePeriod;
	}
	public void setValidCodePeriod(Date ValidCodePeriod) {
		this.ValidCodePeriod = ValidCodePeriod;
	}
	
}
