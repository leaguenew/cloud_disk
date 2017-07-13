package com.echoii.cloud.platform.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/*
 * here is the logic data;
 * store the user download task
 * */
@Entity
@Table( name = "torrent_file")
public class TorrentFile extends Base {


	public static final String STATUS_NORMAL = "normal";
	public static final String STATUS_DELETE = "delete";
	public static final String STATUS_STOP = "stop";
	
	
	private String userId; 
	private String torrentMd5;
	private String infoHash;  //core
	private String status;  
	private int percents; //when stop , the percents available
	
	
	@Column(name="user_id")
	@Type(type="string")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="status")
	@Type(type="string")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="torrent_md5")
	@Type(type="string")
	public String getTorrentMd5() {
		return torrentMd5;
	}
	public void setTorrentMd5(String torrentMd5) {
		this.torrentMd5 = torrentMd5;
	}
	
	@Column(name="percents")
	@Type(type="int")
	public int getPercents() {
		return percents;
	}
	public void setPercents(int percents) {
		this.percents = percents;
	}
	
	@Column(name="info_hash")
	@Type(type="string")
	public String getInfoHash() {
		return infoHash;
	}
	public void setInfoHash(String infoHash) {
		this.infoHash = infoHash;
	}
	

}
