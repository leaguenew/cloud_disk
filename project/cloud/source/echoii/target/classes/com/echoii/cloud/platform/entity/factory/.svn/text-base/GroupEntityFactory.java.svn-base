package com.echoii.cloud.platform.entity.factory;

import java.util.ArrayList;
import java.util.List;

import com.echoii.cloud.platform.entity.GroupDetailEntity;
import com.echoii.cloud.platform.entity.GroupEntity;
import com.echoii.cloud.platform.entity.GroupFileEntity;
import com.echoii.cloud.platform.entity.GroupMemberEntity;
import com.echoii.cloud.platform.model.Group;
import com.echoii.cloud.platform.model.GroupDetail;
import com.echoii.cloud.platform.model.GroupFile;
import com.echoii.cloud.platform.model.GroupMember;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.DateUtil;

public class GroupEntityFactory {
	
	public final static GroupEntity getGroupEntity( Group group ){
		if( group == null ){
			return null;
		}
		
		GroupEntity groupp = new GroupEntity();
		
		groupp.setId( group.getId() );
		groupp.setCreateDate( DateUtil.parseDateToString( 
				group.getCreateDate(), DateUtil.PATTERN_yyyy_MM_dd_HHmmss ) );
		groupp.setCreatorId(group.getCreatorId());
		groupp.setName(group.getName());
		groupp.setDesp(group.getDesp());

		return groupp;
	}
	
	public final static GroupDetailEntity getGroupDetailEntity( Group group, GroupDetail groupdeatil ){
		if( group == null ){
			return null;
		}
		
		GroupDetailEntity groupdetailentity = new GroupDetailEntity();
		groupdetailentity.setId( groupdeatil.getId() );
		groupdetailentity.setCreateDate( DateUtil.parseDateToString( 
				group.getCreateDate(), DateUtil.PATTERN_yyyy_MM_dd_HHmmss ) );
		groupdetailentity.setNumber(String.valueOf(groupdeatil.getNumber()));
		groupdetailentity.setGroupid(groupdeatil.getGroupId());
		groupdetailentity.setName(group.getName());
		groupdetailentity.setDesp(group.getDesp());

		return groupdetailentity;
	}
	
	public final static GroupMemberEntity getGroupMemberEntity(GroupMember member){
		if(member == null){
			return null;
		}
		
		GroupMemberEntity memberEntity = new GroupMemberEntity();
		memberEntity.setCreateDate(DateUtil.parseDateToString( 
				member.getCreateDate(), DateUtil.PATTERN_yyyy_MM_dd_HHmmss ));
		memberEntity.setGroupId(member.getGroupId());
		memberEntity.setId(member.getId());
		memberEntity.setJoinTime(DateUtil.parseDateToString( 
				member.getJoinTime(), DateUtil.PATTERN_yyyy_MM_dd_HHmmss ));
		memberEntity.setMemberId(member.getMemberId());
		memberEntity.setNickname(member.getMemberId());
		
		return memberEntity;
	}
	
	public final static GroupFileEntity getGroupFileEntity(GroupFile groupFile){
		
		if(groupFile == null ){
			return null;
		}
		
		GroupFileEntity groupFileEntity = new GroupFileEntity();
		groupFileEntity.setCreateDate(DateUtil.parseDateToString( 
				groupFile.getCreateDate(), DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		groupFileEntity.setFolderId(groupFile.getFolderId());
		groupFileEntity.setGroupId(groupFile.getGroupId());
		groupFileEntity.setId(groupFile.getId());
		groupFileEntity.setMetaFolder(groupFile.getMetaFolder());
		groupFileEntity.setMetaId(groupFileEntity.getMetaId());
		groupFileEntity.setPath(groupFile.getPath());
		groupFileEntity.setSize(CommUtil.formatSizeDisp(groupFile.getSize()));
		groupFileEntity.setStatus(groupFile.getStatus());
		groupFileEntity.setType(groupFile.getType());
		groupFileEntity.setUserId(groupFile.getUserId());
		
		return groupFileEntity;
		
		
	}
	
	
	public final static List<GroupEntity> listGroupEntity(List<Group> groups){
		if(groups == null || groups.size() == 0){
			return null;
		}
		
		List<GroupEntity>  groupentitys = new ArrayList<GroupEntity>();
		for(int i = 0; i < groups.size(); ++i){
			groupentitys.add(getGroupEntity(groups.get(i)));
		}
		
		return groupentitys;
	}
	
	public final static List<GroupMemberEntity> listGroupMemberEntity(List<GroupMember> members){
		if(members == null || members.size() == 0){
			return null;
		}
		
		List<GroupMemberEntity> memberEntitys = new ArrayList<GroupMemberEntity>();
		for(int i = 0; i < members.size(); ++i){
			memberEntitys.add(getGroupMemberEntity(members.get(i)));
		}
		
		return memberEntitys;
		
	}
	
	public final static List<GroupFileEntity> listgroupFileEntity(List<GroupFile> groupFiles){
		if(groupFiles == null || groupFiles.size() == 0){
			return null;
		}
		
		List<GroupFileEntity> groupFileEntitys = new ArrayList<GroupFileEntity>();
		for(int i = 0; i < groupFiles.size() ; ++ i){
			groupFileEntitys.add(getGroupFileEntity(groupFiles.get(i)));
			
		}
		
		return groupFileEntitys;
	}


}
