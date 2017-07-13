package com.echoii.bean.cloudfilelist;

/**
 * 单条文件内容的详细信息
 * @author Administrator
 *
 */
public class AllFileDetailData{
	
//	private String createdate;
	private String id;
//	private String isCurrentVersion;
	private String lastDate;
	private String metaFolder;
	private String metaId;
	private String name;
	private String parentId;
	private String path;
	private String size;
	private String status;
	/**folder or file*/
	private String type;
	private String userId;

	//	public String getCreatedate() {
//		return createdate;
//	}
//	public void setCreatedate(String createdate) {
//		this.createdate = createdate;
//	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
//	public String getIsCurrentVersion() {
//		return isCurrentVersion;
//	}
//	public void setIsCurrentVersion(String isCurrentVersion) {
//		this.isCurrentVersion = isCurrentVersion;
//	}
	public String getLastDate() {
		return lastDate;
	}
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
