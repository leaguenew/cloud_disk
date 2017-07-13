package com.echoii.tv.model;

import java.io.Serializable;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>文件封装类，并按照文件夹在前排序</p>
 *
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class EchoiiFile implements Serializable,Comparable {

	private String id;
	private String name;
	private byte[] content;
	private String size;
	private String lmfDate;
	private String isCurrentVersion;
	private String path;
	private String status;
	private String type;
	/**
	 * idx 值为0时代表是文件夹，为1时代表是文件
	 */
	private int idx;
	private String version;
	private String createDate;
	private String forderId;
	private String metaFolder;
	private String metaId;
	private String userId;
	
	public EchoiiFile(){}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLmfDate() {
		return lmfDate;
	}

	public void setLmfDate(String lmfDate) {
		this.lmfDate = lmfDate;
	}

	public String getForderId() {
		return forderId;
	}

	public void setForderId(String forderId) {
		this.forderId = forderId;
	}

	public String getMetaFolder() {
		return metaFolder;
	}

	public void setMetaFolder(String metaFolder) {
		this.metaFolder = metaFolder;
	}

	public String getMetaId() {
		return metaId;
	}

	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
	
	public int getIdx() {
		return idx;
	}
	
	public void setIdx(int idx) {
		this.idx = idx;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public int compareTo(Object another) {
		EchoiiFile file = (EchoiiFile)another;
		if (this.idx < file.getIdx()) {
			return -1;
		}else if (this.idx > file.getIdx()) {
			return 1;
		}else if (this.name.charAt(0) < file.getName().charAt(0)) {
			return -1;
		}else if (this.name.charAt(0) > file.getName().charAt(0)) {
			return 1;
		}
		return 0;
	}
	
	
}
