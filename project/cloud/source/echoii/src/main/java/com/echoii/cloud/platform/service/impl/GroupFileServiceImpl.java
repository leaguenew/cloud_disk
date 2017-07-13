package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.echoii.cloud.platform.model.GroupFile;
import com.echoii.cloud.platform.model.GroupFileRequest;
import com.echoii.cloud.platform.service.GroupFileService;
import com.echoii.cloud.platform.service.HibernateDao;

public class GroupFileServiceImpl implements GroupFileService{
	
	static Logger log = Logger.getLogger(GroupFileServiceImpl.class);
	private HibernateDao dao;
	static volatile GroupFileService SERVICE = null;
	
	public static GroupFileService getInstance() {
		if (SERVICE == null) {
			synchronized (GroupFileServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new GroupFileServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	
	public GroupFileServiceImpl() {
		log.debug("group file service init");
		dao = HibernateDao.getInstance();
	}

	@Override
	public void createGroupFile(GroupFile groupfile) {
		dao.insert(groupfile);
		
	}
	

	@Override
	public void createGroupFileRequest(GroupFileRequest groupfilerequest) {
		dao.insert(groupfilerequest);
		
	}
	
	@Override
	public List<GroupFile> listByUserId(String groupId, String userId,String status, int begin, int size) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupId));
		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("status", status));


		List<?> result = dao.list(GroupFile.class, criterions, null, begin, size);

		if (result == null) {
			return Collections.emptyList();
		}
		return (List<GroupFile>) result;
	}
	@Override
	public void deleteGroupFile(GroupFile groupfile) {
		dao.delete(groupfile);
		
	}

	@Override
	public GroupFile getGroupFile(String groupId, String userId, String fileId) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupId));	
		criterions.add(Restrictions.eq("id", fileId));	
		criterions.add(Restrictions.eq("userId", userId));	
		criterions.add(Restrictions.eq("status", GroupFile.STATUS_PUBLISH));

		List<?> result = dao.list(GroupFile.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (GroupFile) result.get(0);
	}
	
	@Override
	public GroupFile getGroupFile(String groupId, String fileId) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupId));
		criterions.add(Restrictions.eq("id", fileId));
		criterions.add(Restrictions.eq("status", GroupFile.STATUS_PUBLISH));

		List<?> result = dao.list(GroupFile.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (GroupFile) result.get(0);
	}

	@Override
	public List<GroupFile> listByGroupId(String groupId, String status, int begin, int size) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupId));
		criterions.add(Restrictions.eq("status", status));

		List<?> result = dao.list(GroupFile.class, criterions, null, begin, size);

		if (result == null ) {
			return Collections.emptyList();
		}
		return (List<GroupFile> ) result;
	}

	@Override
	public List<GroupFile> listByFolderId(String userId, String folderId, int begin, int size) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("folderId", folderId));
		criterions.add(Restrictions.eq("status", GroupFile.STATUS_PUBLISH));

		List<?> result = dao.list(GroupFile.class, criterions, null, begin, size);

		if (result == null ) {
			return Collections.emptyList();
		}
		return (List<GroupFile> ) result;
	}


}
