package com.echoii.cloud.platform.entity;

public class OfflineFileEntity {
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_COMPLETE = "complete";
	public static final String STATUS_STOP = "stop";
	private String name;
	private String infoHash;
	private String percents;
	private String size;
	private String numberFiles;
	private String status;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfoHash() {
		return infoHash;
	}
	public void setInfoHash(String infoHash) {
		this.infoHash = infoHash;
	}
	public String getPercents() {
		return percents;
	}
	public void setPercents(String percents) {
		this.percents = percents;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getNumberFiles() {
		return numberFiles;
	}
	public void setNumberFiles(String numberFiles) {
		this.numberFiles = numberFiles;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
