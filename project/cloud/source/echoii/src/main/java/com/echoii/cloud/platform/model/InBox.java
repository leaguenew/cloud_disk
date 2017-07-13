package com.echoii.cloud.platform.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "inbox")
public class InBox extends Base{
	
	private String userId;
	private String eventId;
	private String status;
	
	
	@Column(name="user_id")
	@Type(type="string")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="event_id")
	@Type(type="string")
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	
	@Column(name="status")
	@Type(type="string")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
