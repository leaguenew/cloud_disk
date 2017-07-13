package com.echoii.cloud.platform.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "group_detail")
public class GroupDetail extends Base {

	private String groupId;
	private int number;
	
	@Column(name="group_id")
	@Type(type="string")
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	@Column(name="number")
	@Type(type="int")
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	
}
