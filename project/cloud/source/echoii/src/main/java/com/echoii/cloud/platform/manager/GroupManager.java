package com.echoii.cloud.platform.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.model.Group;
import com.echoii.cloud.platform.model.GroupDetail;
import com.echoii.cloud.platform.model.GroupFile;
import com.echoii.cloud.platform.model.GroupMember;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.GroupFileService;
import com.echoii.cloud.platform.service.GroupMemberService;
import com.echoii.cloud.platform.service.GroupService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.service.impl.GroupFileServiceImpl;
import com.echoii.cloud.platform.service.impl.GroupMemberServiceImpl;
import com.echoii.cloud.platform.service.impl.GroupServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;
import com.echoii.cloud.platform.util.HashUtil;


public class GroupManager {
	
	private static volatile GroupManager MANAGER;	
	private GroupService groupService;
	private GroupFileService groupfileService;
	private GroupMemberService groupmemberService;
	private UserService userService;
	private FileService fileService;

	public static GroupManager getInstance() {
		if (MANAGER == null) {
			synchronized (GroupManager.class) {
				if (MANAGER == null) {
					MANAGER = new GroupManager();
				}
			}
		}
		return MANAGER;
	}

	GroupManager() {
		groupService = GroupServiceImpl.getInstance();
		groupfileService = GroupFileServiceImpl.getInstance();
		groupmemberService = GroupMemberServiceImpl.getInstance();
		userService = UserServiceImpl.getInstance();
		fileService = FileServiceImpl.getInstance();
	}
	
	public Group create( String creatorId, String name,String desp){
		Group group = groupService.getGroupByName(name);
		if(group != null){
			return null;
		}

		// create a group
		group = new Group();
		group.setId(HashUtil.strHash(HashUtil.getRandomId()));
		group.setCreatorId(creatorId);
		group.setCreateDate(new Date());
		group.setDesp(desp);
		group.setName(name);
		//group.setType(desp);
		groupService.createGroup(group);

		// and also create group - member
		GroupMember member = new GroupMember();
		member.setId(HashUtil.strHash(HashUtil.getRandomId()));
		member.setCreateDate(new Date());
		member.setGroupId(group.getId());
		member.setMemberId(creatorId);
		member.setJoinTime(new Date());
		groupmemberService.createGroupMember(member);

		// create groupdetail
		GroupDetail groupdetail = new GroupDetail();
		groupdetail.setId(HashUtil.strHash(HashUtil.getRandomId()));
		groupdetail.setCreateDate(new Date());
		groupdetail.setGroupId(group.getId());
		groupdetail.setNumber(1);
		groupService.createGroupDetail(groupdetail);

		//GroupEntity groupj = GroupEntityFactory.getGroupEntity(group);
		return group;
	
	}
	
	
	public List<Group> list(String userId, String mode, String order,int begin,int size){
		
		List<Group> list = null;
		
		//three modes
		if(mode.equals("create")){
			list = groupService.listGroupByUserId(userId, order, begin, size);
			return list;
		}
		
		if(mode.equals("join")){
			List<GroupMember> members = groupmemberService.listGroupMemberByMemberId(userId,order,begin,size);
			
			if(members == null || members.isEmpty()){
				return null;
			}
			
			List<String> ids = new ArrayList<String>();
			for(int i = 0 ; i < members.size(); ++i){
				if(!members.get(i).getMemberId().equals(userId)){
					ids.add(members.get(i).getGroupId());
				}
			}
			list = groupService.listGroupByIds(ids, order, begin, size);	
			return list;
		}	
		
		if(mode.equals("all")){
			list = groupService.listAllGroup(order, begin, size);
			return list;
		}
		
		return null;
		
	}
	
	public Group info(String groupId) {
		
		return groupService.getGroupById(groupId);

	}

	public GroupDetail infoDetail(String groupid) {

		return groupService.getGroupDetailByGropId(groupid);

	}
	
	public boolean join(String groupId, String userId) {
		//check the para
		Group group = groupService.getGroupById(groupId);
		User user = userService.getUserById(userId);
		
		if(group == null || user == null){
			return false;
		}
		
		GroupMember gm = groupmemberService.getGroupMember(groupId, userId);
		
		if (gm == null) {
			
			GroupMember ngm = new GroupMember();
			ngm.setCreateDate(new Date());
			ngm.setId(HashUtil.getRandomId());
			ngm.setGroupId(groupId);
			ngm.setMemberId(userId);
			ngm.setNickname(user.getNickName());
			ngm.setJoinTime(new Date());

			groupmemberService.createGroupMember(ngm);
		}

		return true;
	
		
	}
	
	public boolean quit(String groupId, String userId) {
		
		// check the para
		Group group = groupService.getGroupById(groupId);
		User user = userService.getUserById(userId);
		
		if(group == null || user == null){
			return false;
		}
		
		// if the user is the admin of the group,delete the group and group  detail
		if (group.getCreatorId().equals(userId)) {
			groupService.deleteGroup(group);
			GroupDetail gpdetail = groupService.getGroupDetailByGropId(groupId);
			groupService.deleteGroupDetial(gpdetail);
		}

		GroupMember gm = groupmemberService.getGroupMember(groupId, userId);
		
		if (gm != null) {
			groupmemberService.deleteGroupMember(gm);
		}

		return true;		
		
	}
	
	public List<GroupMember> listMembers(String groupId,int begin,int size){
		
		return groupmemberService.listGroupMemberByGroupId(groupId, begin, size);
		
			
	}
	
	
	
	public List<GroupFile> listFiles(String groupId, String userId, String mode, int begin, int size){
        
		List<GroupFile> list;
		
		//three modes
		if(mode.equals("mine")){
			list = groupfileService.listByUserId(groupId, userId,GroupFile.STATUS_PUBLISH, begin, size);
			
			return list;
		}
		
		if(mode.equals("publish")){
			list = groupfileService.listByGroupId(groupId,GroupFile.STATUS_PUBLISH, begin, size);
			
			return list;
			
		}		
		
		if(mode.equals("pending")){		
			Group gp = groupService.getGroup(groupId, userId);
			if(gp != null){//admin
				list = groupfileService.listByGroupId(groupId, GroupFile.STATUS_PEND,begin, size);
				
				return list;
			}else{//normal user
				list = groupfileService.listByUserId(groupId, userId,GroupFile.STATUS_PEND, begin, size);
				
				return list;
			}
		}
				
		if(mode.equals("deny")){	
			Group gp = groupService.getGroup(groupId, userId);	
			
			if(gp != null){//admin
				list = groupfileService.listByGroupId(groupId,GroupFile.STATUS_DENY, begin, size);
				
				return list;
			}
			
			list = groupfileService.listByUserId(groupId,userId,GroupFile.STATUS_DENY, begin, size);
			
			return list;
			
		}
				
		return null;
		
	}
	
	public boolean deleteGroupFile(String groupId,String userId,String fileId){
		
		Group gp = groupService.getGroup(groupId, userId);
		GroupFile gpfile = null;
		
		if(gp != null){ //admin
			gpfile =  groupfileService.getGroupFile(groupId, fileId);
			if(gpfile == null){
				return false;
			}
			groupfileService.deleteGroupFile(gpfile);
			return true;
			
		}
		
		//normal user
		gpfile = groupfileService.getGroupFile(groupId, userId, fileId);
		
		if(gpfile == null){
			return false;
		}
		groupfileService.deleteGroupFile(gpfile);	
		return true;
		
	}
	
    public boolean copy(String groupId, String[] list, String userId,String folderId){
		
		if(list == null){
			return false;
		}
		
		for(int i = 0; i < list.length; ++i){
			GroupFile gfile = groupfileService.getGroupFile(groupId, list[i]);
			File file = new File();
			file.setId(HashUtil.getRandomId());
			file.setUserId(userId);
			file.setFolderId(folderId);
			file.setMetaFolder(gfile.getMetaFolder());
			file.setMetaId(gfile.getMetaId());
			file.setSize(gfile.getSize());
			file.setType(gfile.getType());
			file.setStatus(File.STATUS_NORMAL);
			file.setCreateDate(new Date());
			fileService.createFile(file);
			
			if(gfile.getType().equals("folder")){
				copyFolderGroupService(userId,groupId,file.getId(),list[i]);
			}
			
		}
		

		return true;
		
	}	
	
	private void copyFolderGroupService(String userId, String groupId, String folderId, String groupFolderId){
		
		List<GroupFile> files = groupfileService.listByFolderId(userId, groupFolderId, 0, 100);
		
		if(files.isEmpty() ){
			return;
		}
		
		for(int i = 0 ; i < files.size(); ++i){
			
			File file = new File();
			file.setId(HashUtil.getRandomId());
			file.setUserId(userId);
			file.setFolderId(folderId);
			file.setMetaFolder(files.get(i).getMetaFolder());
			file.setMetaId(files.get(i).getMetaId());
			file.setSize(files.get(i).getSize());
			file.setType(files.get(i).getType());
			file.setStatus(File.STATUS_NORMAL);
			file.setCreateDate(new Date());
			fileService.createFile(file);
			
			if(files.get(i).getType().equals("folder")){
				//recursion here
				copyFolderGroupService(userId,groupId,file.getId(),files.get(i).getId());
			}
			
		}
	
	}

}
