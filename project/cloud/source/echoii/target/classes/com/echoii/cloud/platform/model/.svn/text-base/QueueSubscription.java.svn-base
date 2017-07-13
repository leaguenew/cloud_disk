package com.echoii.cloud.platform.model;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "queue_subscription")
public class QueueSubscription extends Base{
	
	private String userId;
	private String queueId;
	private String subId;
	

	@Column(name="user_id")
	@Type(type="string")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="queue_id")
	@Type(type="string")
	public String getQueueId() {
		return queueId;
	}
	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}

	@Column(name="sub_id")
	@Type(type="string")
	public String getSubId() {
		return subId;
	}
	public void setSubId(String subId) {
		this.subId = subId;
	}
	
}
