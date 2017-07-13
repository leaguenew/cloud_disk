package com.echoii.cloud.platform.model;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "hdc")
public class Hdc extends Base {

	public static final String STATUS_ACTIVE = "active";	
	private String deviceId;
	private String status;
	private String token;
	private String name;
	
	
	
	@Column(name = "device_id")
	@Type(type="string")
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@Column(name = "status")
	@Type(type="string")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "token")
	@Type(type="string")
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	@Column(name="name")
	@Type(type="string")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
