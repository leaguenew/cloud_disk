package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.echoii.cloud.platform.model.GroupMember;
import com.echoii.cloud.platform.service.GroupFileService;
import com.echoii.cloud.platform.service.GroupMemberService;
import com.echoii.cloud.platform.service.HibernateDao;

public class GroupMemberServiceImpl implements GroupMemberService{
	
	static Logger log = Logger.getLogger(GroupMemberServiceImpl.class);
	static HibernateDao dao;
	static volatile GroupMemberService SERVICE = null;
	
	public static GroupMemberService getInstance() {
		if (SERVICE == null) {
			synchronized (GroupMemberServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new GroupMemberServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	
	public GroupMemberServiceImpl() {
		log.debug("group member service init");
		dao = HibernateDao.getInstance();
	}
	
	@Override
	public void createGroupMember(GroupMember groupmember) {
		dao.insert(groupmember);
		
	}
	
	@Override
	public List<GroupMember> listGroupMemberByMemberId(String memberId,String order,int begin,int size) {
		// TODO Auto-generated method stub
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("memberId", memberId));
		
		List orders = new ArrayList();
		if (order.equals("create_date"))
			orders.add(Order.asc("createDate"));
		else
			orders = null;
		
		List<?> result = dao.list(GroupMember.class, criterions, orders, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<GroupMember>) result;
	}

	@Override
	public GroupMember getGroupMember(String groupId, String memberId) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupId));
		criterions.add(Restrictions.eq("memberId", memberId));

		List<?> result = dao.list(GroupMember.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (GroupMember) result.get(0);
	}

	@Override
	public GroupMember getGroupMemberByGroupid(String groupid) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupid));

		List<?> result = dao.list(GroupMember.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (GroupMember) result.get(0);
	}

	@Override
	public void deleteGroupMember(GroupMember groupmember) {
		dao.delete(groupmember);	
	}

	@Override
	public List<GroupMember> listGroupMemberByGroupId(String groupid, int begin, int size) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupid));

		List<?> result = dao.list(GroupMember.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<GroupMember> ) result;
	}

}
