package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import com.echoii.cloud.platform.model.Group;
import com.echoii.cloud.platform.model.GroupDetail;
import com.echoii.cloud.platform.service.GroupService;
import com.echoii.cloud.platform.service.HibernateDao;


public class GroupServiceImpl implements GroupService {
	
	static Logger log = Logger.getLogger(GroupServiceImpl.class);
	static HibernateDao dao;
	static volatile GroupService SERVICE = null;
	
	public static GroupService getInstance() {
		if (SERVICE == null) {
			synchronized (GroupServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new GroupServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	
	public GroupServiceImpl() {
		log.debug("group service init");
		dao = HibernateDao.getInstance();
	}

	@Override
	public void createGroup(Group group) {
		dao.insert(group);	
	}

	@Override
	public Group getGroupByName(String name) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("name", name));

		List<?> result = dao.list(Group.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (Group) result.get(0);
	}

	@Override
	public Group getGroupById(String id) {

		return (Group) dao.getById(Group.class, id);
	}

	@Override
	public void createGroupDetail(GroupDetail groupdetail) {
		dao.insert(groupdetail);	
		
	}

	@Override
	public List<Group> listGroupByUserId(String creatorid,String order,int begin,int size) {
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("creatorId", creatorid));
		
		List orders = new ArrayList();
		if (order.equals("create_date"))
			orders.add(Order.asc("createDate"));
		else if (order.equals("name"))
			orders.add(Order.asc("name"));
		else
			orders.add(Order.asc(order));

		List<?> result = dao.list(Group.class, criterions, orders, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<Group>) result;
	}

	
	@Override
	public List<Group> listAllGroup(String order,int begin,int size) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();
		
		List orders = new ArrayList();
		if (order.equals("create_date"))
			orders.add(Order.asc("createDate"));
		else if (order.equals("name"))
			orders.add(Order.asc("name"));
		else
			orders.add(Order.asc(order));
		
		List<?> result = dao.list(Group.class, criterions, orders, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<Group>) result;
	}

	@Override
	public List<Group> listGroupByIds(List<String> ids, String order, int begin,int size) {	
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.in("id", ids));
		
		List orders = new ArrayList();
		if (order.equals("create_date"))
			orders.add(Order.asc("createDate"));
		else if (order.equals("name"))
			orders.add(Order.asc("name"));
		else
			orders.add(Order.asc(order));

		List<?> result = dao.list(Group.class, criterions, orders, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<Group>) result;
		
	}

	@Override
	public GroupDetail getGroupDetailByGropId(String groupid) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("groupId", groupid));

		List<?> result = dao.list(GroupDetail.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (GroupDetail) result.get(0);
	}

	
	@Override
	public Group getGroup(String groupid, String userid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(Group group) {
		// TODO Auto-generated method stub
		dao.delete(group);
	}

	@Override
	public void deleteGroupDetial(GroupDetail groupdetail) {
		dao.delete(groupdetail);
		
	}



}
