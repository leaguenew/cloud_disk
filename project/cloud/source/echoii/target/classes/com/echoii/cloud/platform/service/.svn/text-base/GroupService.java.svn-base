package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.Group;
import com.echoii.cloud.platform.model.GroupDetail;

public interface GroupService {

	public void createGroup(Group group);

	public void createGroupDetail(GroupDetail groupDetail);

	public void deleteGroup(Group group);

	public void deleteGroupDetial(GroupDetail groupDetail);

	public Group getGroupByName(String name);

	public Group getGroup(String groupId, String userId);

	public GroupDetail getGroupDetailByGropId(String groupId);

	public Group getGroupById(String id);

	public List<Group> listGroupByUserId(String userId, String order,int begin, int size);

	public List<Group> listAllGroup(String order, int begin, int size);

	public List<Group> listGroupByIds(List<String> ids, String order, int begin,int size);

}
