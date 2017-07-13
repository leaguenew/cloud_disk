package com.echoii.tv.model;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>家庭数据中心封装类</p>
 *
 */
public class Device {
	
	private String deviceId;
	private String deviceToken;
	
	public Device() {
		super();
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	

}
