package com.echoii.cloud.platform.entity;

import java.util.Date;

public class UserLoginEntity extends UserEntity {
	private Date currentlogindate;
	private String token;
	public Date getCurrentlogindate() {
		return currentlogindate;
	}
	public void setCurrentlogindate(Date currentlogindate) {
		this.currentlogindate = currentlogindate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

}
