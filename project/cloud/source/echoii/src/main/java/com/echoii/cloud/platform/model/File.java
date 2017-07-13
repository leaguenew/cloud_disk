package com.echoii.cloud.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "file")
public class File extends Base {

	public static final String TYPE_FOLDER = "folder";
	public static final String TYPE_BINARY = "binary";
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_DELETE = "delete";
	public static final String STATUS_SUB_DELETE = "sub_delete";
	public static final String STATUS_PERMANENT_DELETE = "permanent_delete";
	
	private String userId;
	private long size;
	private String folderId;
	private String name;
	private String status;
	private int version;
	private String type ;//String = file
	private String isCurrentVersion = "true";
	private String metaId;
	private String metaFolder;
	private Date lmf_date;
	private String path;
	private int idx = 1;
	
	
		
	@Column(name="user_id")
	@Type(type="string")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	

	
	@Column(name="folder_id")
	@Type(type="string")
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}
	
	@Column(name="name")
	@Type(type="string")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="status")
	@Type(type="string")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="version")
	@Type(type="int")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name="type")
	@Type(type="string")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
//	@Column(name="file_type")
//	@Type(type="String")
//	public String getFileType() {
//		return fileType;
//	}
//	public void setFileType(String fileType) {
//		this.fileType = fileType;
//	}
//	
	@Column(name="is_current_version")
	@Type(type="string")
	public String getIsCurrentVersion() {
		return isCurrentVersion;
	}
	public void setIsCurrentVersion(String isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
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

	@Column(name="lmf_date")
	@Type(type="java.util.Date")
	public Date getLmf_date() {
		return lmf_date;
	}
	public void setLmf_date(Date lmf_date) {
		this.lmf_date = lmf_date;
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
	
	@Column(name="idx")
	@Type(type="int")
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	
	
}
