package com.echoii.cloud.platform.model;



import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "message_queue")
public class MessageQueue extends Base{
	
	private String queueId;
	
	

	@Column(name="queue_id")
	@Type(type="string")
	public String getQueueId() {
		return queueId;
	}
	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}	
	
}
