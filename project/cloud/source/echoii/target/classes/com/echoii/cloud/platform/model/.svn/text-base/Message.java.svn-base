package com.echoii.cloud.platform.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "message")
public class Message extends Base{

	private String userId;
	private String name;
	private String address;
	private String body;
	private String isCurrentVersion;
	
	
	@Column(name="user_id")
	@Type(type="string")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="name")
	@Type(type="string")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="address")
	@Type(type="string")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="body")
	@Type(type="string")
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	@Column(name="is_current_version")
	@Type(type="string")
	public String getIsCurrentVersion() {
		return isCurrentVersion;
	}
	public void setIsCurrentVersion(String isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}
		
}