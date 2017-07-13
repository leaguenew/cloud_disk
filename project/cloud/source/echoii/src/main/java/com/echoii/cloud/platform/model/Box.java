package com.echoii.cloud.platform.model;



import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "box")
public class Box extends Base {
//	private String id;
//	private Date createDate;
	private String userId;
	private String eventId;
	private String status;
	
//	@Id
//	@Type(type="String")
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
//	
//	@Column(name="create_date")
//	@Type(type="Date")
//	public Date getCreateDate() {
//		return createDate;
//	}
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
	
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
