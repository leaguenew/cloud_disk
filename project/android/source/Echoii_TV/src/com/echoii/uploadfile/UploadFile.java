package com.echoii.uploadfile;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UploadFile implements Serializable,Comparable {

	private String fileName;
	private byte[] fileContent;
	private long fileSize;
	private String date;
	private String parentRoot;
	private String isCurrentVersion;
	private String path;
	private String status;
	private String type;
	private String version;
	private int itemId;
	
	public UploadFile(){}
	
	public UploadFile(int itemId){
		this.itemId = itemId;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getParentRoot() {
		return parentRoot;
	}
	public void setParentRoot(String parentRoot) {
		this.parentRoot = parentRoot;
	}
	public String getIsCurrentVersion() {
		return isCurrentVersion;
	}
	public void setIsCurrentVersion(String isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public byte[] getFileContent() {
		return fileContent;
	}
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	@Override
	public int compareTo(Object another) {
		UploadFile file = (UploadFile)another;
		if (itemId == 1) {
			if (Long.valueOf(this.date) < Long.valueOf(file.getDate())) {
				return 1;
			}else if(Long.valueOf(this.date) > Long.valueOf(file.getDate())) {
				return -1;
			}
			return 0;
		}else{
			if (this.fileName.charAt(0) < file.getFileName().charAt(0)) {
				return -1;
			}else if(this.fileName.charAt(0) > file.getFileName().charAt(0)) {
				return 1;
			}
			return 0;
		}
	}
	
	
}
