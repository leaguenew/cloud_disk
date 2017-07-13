package com.echoii.cloud.platform.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * @author 东辉
 * 
 */
@Entity
@Table(name = "groups")
public class Group extends Base {
	private String creatorId;
	private String name;
	private String desp;


	@Column(name = "creator_id")
	@Type(type = "string")
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	@Column(name = "name")
	@Type(type = "string")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Column(name = "desp")
	@Type(type = "string")
	public String getDesp() {
		return desp;
	}

	public void setDesp(String desp) {
		this.desp = desp;
	}

}
