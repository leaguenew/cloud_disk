package com.echoii.cloud.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "hdc_detail")
public class HdcDetail extends Base{
	
	public static final String IS_Activate = "is_activate";
	public static final String NOT_Activate = "not_activate";
	
	private String deviceId;
	private String password;
	private Date activationTime;
	
	
	@Column(name="password")
	@Type(type="string")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	@Column(name="activation_time")
	@Type(type="java.util.Date")
	public Date getActivationTime() {
		return activationTime;
	}
	public void setActivationTime(Date activationTime) {
		this.activationTime = activationTime;
	}
	@Column(name="device_id")
	@Type(type="string")
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	
}
