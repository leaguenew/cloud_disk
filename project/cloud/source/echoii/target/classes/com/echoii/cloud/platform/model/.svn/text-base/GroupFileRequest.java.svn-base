package com.echoii.cloud.platform.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "group_file_request")
public class GroupFileRequest extends Base{

	private String fileName;
	private int fileSize;
	
	
	@Column(name="file_name")
	@Type(type="string")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name="file_size")
	@Type(type="int")
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
}
