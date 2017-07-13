package com.echoii.cloud.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Table( name = "share_link_file")
public class ShareLinkFile extends Base{
	
	public static final String TYPE_PUBLIC = "public";
	public static final String TYPE_PRIVATE = "private";
	
	private String type;
	private String userId;
	private String validCode;
	private String metaId;
	private String metaFolder;
	private String name;
	private long size;
	
	
	@Column(name="type")
	@Type(type="string")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="valid_code")
	@Type(type="string")
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	
	@Column(name="meta_id")
	@Type(type="string")
	public String getMetaId() {
		return metaId;
	}
	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}
	
	@Column(name="meta_folder")
	@Type(type="string")
	public String getMetaFolder() {
		return metaFolder;
	}
	public void setMetaFolder(String metaFolder) {
		this.metaFolder = metaFolder;
	}
	
	@Column(name="size")
	@Type(type="long")
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	@Column(name="name")
	@Type(type="string")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="user_id")
	@Type(type="string")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
