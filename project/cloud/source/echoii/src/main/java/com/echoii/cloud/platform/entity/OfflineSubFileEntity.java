package com.echoii.cloud.platform.entity;

public class OfflineSubFileEntity {
	private String name;
	private String size;
	private String percents;
	private String parentInfoId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getParentInfoId() {
		return parentInfoId;
	}
	public void setParentInfoId(String parentInfoId) {
		this.parentInfoId = parentInfoId;
	}
	public String getPercents() {
		return percents;
	}
	public void setPercents(String percents) {
		this.percents = percents;
	}

}
