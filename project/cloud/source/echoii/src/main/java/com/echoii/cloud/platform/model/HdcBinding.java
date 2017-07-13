package com.echoii.cloud.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "hdc_binding")
public class HdcBinding extends Base {
	
	public static final String STATUS_APPROVE = "approve"; 
	public static final String STATUS_REQUEST = "request";
	public static final String STATUS_REFUSE = "refuse";
	
	private String deviceId;
	private String userId;
	private String status;

	@Column(name = "device_id")
	@Type(type = "string")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	@Column(name = "user_id")
	@Type(type = "string")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "status")
	@Type(type = "string")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
