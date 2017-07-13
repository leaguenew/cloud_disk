package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.model.UserDetail;
import com.echoii.cloud.platform.service.HibernateDao;
import com.echoii.cloud.platform.service.UserService;

public class UserServiceImpl implements UserService {

	static Logger log = Logger.getLogger(UserServiceImpl.class);
	private HibernateDao dao;
	static volatile UserService SERVICE = null;


	
	public UserServiceImpl() {
		log.debug("user service init");
		dao = HibernateDao.getInstance();
	}

	
	public static UserService getInstance() {
		if (SERVICE == null) {
			synchronized (UserServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new UserServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	

	@Override
	public void createUser(User user) {
		dao.insert(user);
	}

	@Override
	public User getUserById(String id) {
		return (User) dao.getById(User.class, id);
	}

	@Override
	public User getUserByNameHash(String name_hash) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("nameHash", name_hash));

		List<?> result = dao.list(User.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}

		return (User) result.get(0);
	}

	@Override
	public User getUsertByName(String name) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("name", name));

		List<?> result = dao.list(User.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}

		return (User) result.get(0);
	}

	@Override
	public User getUserByMail(String email) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("email", email));

		List<?> result = dao.list(User.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}

		return (User) result.get(0);
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub
		dao.update(user);
	}

	@Override
	public List<User> listUserByIds(Object[] ids,int begin, int size) {
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.in("id", ids));
		
		
		List<?> result = dao.list(User.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<User>) result;
	}

	@Override
	public void createUserDetail(UserDetail userdetail) {
		dao.insert(userdetail);
		
	}

	@Override
	public void updateUserDetail(UserDetail userdetail) {
		dao.update(userdetail);
		
	}

	@Override
	public UserDetail getUserDetailByUserId(String userid) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("userid", userid));

		List<?> result = dao.list(UserDetail.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}

		return (UserDetail) result.get(0);
	}


}
