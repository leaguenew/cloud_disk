package com.echoii.cloud.platform.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table( name = "group_member")
public class GroupMember extends Base {

	private String groupId;
	private String memberId;
	private String nickname;
	private Date joinTime;
	
	
	@Column(name="group_id")
	@Type(type="string")
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
	
	@Column(name="member_id")
	@Type(type="string")
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	@Column(name="join_time")
	@Type(type="java.util.Date")
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	
	@Column(name="nick_name")
	@Type(type="string")
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
