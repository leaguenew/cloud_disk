package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.GroupMember;

public interface GroupMemberService {
	
	public void createGroupMember(GroupMember groupmember);

	public void deleteGroupMember(GroupMember groupmember);

	public GroupMember getGroupMember(String groupid, String memberid);

	public GroupMember getGroupMemberByGroupid(String groupid);

	public List<GroupMember> listGroupMemberByMemberId(String memberid,String order, int begin, int size);

	public List<GroupMember> listGroupMemberByGroupId(String groupid,int begin, int size);
	
}
