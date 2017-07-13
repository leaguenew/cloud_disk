package com.echoii.cloud.platform.model;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table( name = "torrent_table")
public class TorrentTable extends Base {
	
	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_COMPLETE = "complete";
	
	private String torrentMd5;
	private String infoHash;
	private String status;
	private String fileId;
	
	public String getTorrentMd5() {
		return torrentMd5;
	}
	public void setTorrentMd5(String torrentMd5) {
		this.torrentMd5 = torrentMd5;
	}
	public String getInfoHash() {
		return infoHash;
	}
	public void setInfoHash(String infoHash) {
		this.infoHash = infoHash;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	

}
