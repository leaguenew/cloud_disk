package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.GroupFile;
import com.echoii.cloud.platform.model.GroupFileRequest;

public interface GroupFileService {
	
	public void createGroupFile(GroupFile groupfile);

	public void createGroupFileRequest(GroupFileRequest groupfilerequest);

	public void deleteGroupFile(GroupFile groupFile);

	public GroupFile getGroupFile(String groupId, String fileId);

	public GroupFile getGroupFile(String groupId, String userId, String fileId);

	public List<GroupFile> listByGroupId(String groupId, String status, int begin, int size);

	public List<GroupFile> listByUserId(String groupId, String userId, String status,  int begin,int size);
	
	public List<GroupFile> listByFolderId(String userId, String folderId, int begin,int size);

}
