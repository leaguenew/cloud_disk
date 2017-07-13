package com.echoii.cloud.platform.entity;

public class FileEntity extends Entity{
	private String userId;
	private String size;
	private String folderId;
	private String name;
	private String status;
	private int version;
	private String type ;//String = file
	//private String fileType;
	private String isCurrentVersion;
	private String metaId;
	private String metaFolder;
	private String lmf_date;
	private String path;
	private int idx;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getFolderId() {
		return folderId;
	}
	public void setFolderId(String parentId) {
		this.folderId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsCurrentVersion() {
		return isCurrentVersion;
	}
	public void setIsCurrentVersion(String isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}
	public String getMetaId() {
		return metaId;
	}
	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}
	
	public String getMetaFolder() {
		return metaFolder;
	}
	public void setMetaFolder(String metaFolder) {
		this.metaFolder = metaFolder;
	}	
	
	
	public String getLmf_date() {
		return lmf_date;
	}
	public void setLmf_date(String lmf_date) {
		this.lmf_date = lmf_date;
	}
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	
}
