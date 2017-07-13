package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.echoii.cloud.platform.model.ShareLinkFile;
import com.echoii.cloud.platform.service.HibernateDao;
import com.echoii.cloud.platform.service.ShareService;
import com.echoii.cloud.platform.util.Config;

public class ShareServiceImpl implements ShareService {
	static Logger log = Logger.getLogger(ShareServiceImpl.class);
	private HibernateDao dao;
	static volatile ShareService SERVICE = null;
	static Config config = Config.getInstance();

	public ShareServiceImpl() {
		log.debug("share service init");
		dao = HibernateDao.getInstance();
	}

	public ShareServiceImpl(HibernateDao dao) {
		this.dao = dao;
	}

	public static ShareService getInstance() {
		if (SERVICE == null) {
			synchronized (ShareServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new ShareServiceImpl();
				}
			}
		}
		return SERVICE;
	}

	
	@Override
	public void createShareFile(ShareLinkFile file) {
		dao.insert(file);
		
	}

	@Override
	public ShareLinkFile getShareFileById(String id) {
		return (ShareLinkFile) dao.getById(ShareLinkFile.class, id);
	}

	@Override
	public ShareLinkFile getShareFileByUserId(String userId) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userId));
		List<?> result = dao.list(ShareLinkFile.class, criterions, null, 0, 1);
		if (result == null || result.size() < 1) {
			return null;
		}

		return (ShareLinkFile) result.get(0);
	}

	@Override
	public List<ShareLinkFile> listShareFileByUserId(String userId,
			String order, int begin, int size) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userId));
		List<Order> orders = new ArrayList<Order>();
		if(order.equals("type") || order.equals("name")
				||order.equals("size")){
			orders.add(Order.asc(order));
		}else{
			orders =  null;
		}
		
		List<?> result = dao.list(ShareLinkFile.class, criterions, orders, begin, size);
		if (result == null || result.size() < 1) {
			return null;
		}

		return (List<ShareLinkFile>) result;
	}

	@Override
	public void deleteShareFile(ShareLinkFile file) {
		dao.delete(file);
		
	}

	@Override
	public void updateShareFile(ShareLinkFile file) {
		dao.update(file);
		
	}

}
