package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.service.HibernateDao;
import com.echoii.cloud.platform.service.StatisticService;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DataUtil;

public class StatisticServiceImpl implements StatisticService{
	
	static Logger log = Logger.getLogger(StatisticServiceImpl.class);
	private HibernateDao dao;
	static volatile StatisticService SERVICE = null;
	static Config config = Config.getInstance();

	public StatisticServiceImpl() {
		log.debug("file service init");
		dao = HibernateDao.getInstance();
	}


	public static StatisticService getInstance() {
		if (SERVICE == null) {
			synchronized (StatisticServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new StatisticServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	
	@Override
	public Long countPictures(String userid) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.in("type", DataUtil.getImageType()));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		return dao.count(File.class, criterions);
	}

	@Override
	public Long countVideos(String userid) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.in("type", DataUtil.getVideoType()));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		return dao.count(File.class, criterions);

	}

	@Override
	public Long countMusics(String userid) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.in("type", DataUtil.getMusicType()));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		return dao.count(File.class, criterions);
	}

	@Override
	public Long countDocuments(String userid) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.in("type", DataUtil.getDocType()));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		return dao.count(File.class, criterions);
	}

	@Override
	public Long countAll(String userid) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.not(Restrictions.eq("type", "folder")));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		return dao.count(File.class, criterions);
	}

	@Override
	public Long sumAllSize(String userid, String propertity) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.not(Restrictions.eq("type", "folder")));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		return dao.sum(File.class, criterions, propertity);
	}

	@Override
	public Long countOthers(String userid) {
		List<Object> criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userid));
		criterions.add(Restrictions.not(Restrictions.in("type", DataUtil.getOthersType())));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		/*folder should not be list*/
		criterions.add(Restrictions.not(Restrictions.eq("type", "folder")));
		
		return dao.count(File.class, criterions);
	}

}
