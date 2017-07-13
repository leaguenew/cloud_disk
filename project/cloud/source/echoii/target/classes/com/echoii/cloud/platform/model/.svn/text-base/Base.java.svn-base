package com.echoii.cloud.platform.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;


@MappedSuperclass

public class Base {
	private String id;
	private Date createDate;
	@Id
	@Type(type="string")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="create_date")
	@Type(type="java.util.Date")
	@GeneratedValue
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
