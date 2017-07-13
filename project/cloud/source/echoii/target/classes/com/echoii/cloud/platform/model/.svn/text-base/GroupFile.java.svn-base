package com.echoii.cloud.platform.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "group_file")
public class GroupFile extends Base{
	public static final String TYPE_FOLDER = "folder";
	public static final String TYPE_BINARY = "binary";
	
	public static final String STATUS_PUBLISH = "publish";
	public static final String STATUS_PEND= "pending";
	public static final String STATUS_DENY = "deny";

	private String groupId;
	private String folderId;
	private String userId;
	
	private String type ;//String = file
	private String metaId;
	private String metaFolder;
	private String path;
	private long size;
	private String status;
	
	@Column(name="status")
	@Type(type="string")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="type")
	@Type(type="string")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	@Column(name="path")
	@Type(type="string")
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	@Column(name="size")
	@Type(type="long")
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	@Column(name="folder_id")
	@Type(type="string")
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	
	@Column(name="group_id")
	@Type(type="string")
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
