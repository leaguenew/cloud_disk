package com.echoii.cloud.platform.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.echoii.cloud.platform.model.Hdc;
import com.echoii.cloud.platform.model.UserDetail;
import com.echoii.cloud.platform.service.impl.GroupFileServiceImpl;
import com.echoii.cloud.platform.util.Config;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HibernateDao {
	private static Logger log = Logger.getLogger(HibernateDao.class);
	private Config config = Config.getInstance();
	private SessionFactory sessionFactory;
	private volatile static HibernateDao DAO = null;
	
	@SuppressWarnings("deprecation")
	private HibernateDao() {
		log.debug("Hibernate Dao init begin");
		String hiberPath = config.getStringValue("ext.config.hibernate.path", "/data/echoii/config/hibernate.cfg.xml");
		File extConfig = new File( hiberPath );
		if( extConfig.canRead() ){
			log.debug("use external file:" + extConfig.getAbsolutePath() );
			sessionFactory = new Configuration().configure(extConfig).buildSessionFactory();
		}else{
		    log.debug("use internal config file");
		    sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		
//		sessionFactory = new Configuration().configure().buildSessionFactory();
		
		log.debug("Hibernate Dao init end!");
	}
	
	public static HibernateDao getInstance() {
		if (DAO == null) {
			synchronized (HibernateDao.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (DAO == null) {
					DAO = new HibernateDao();
				}
			}
		}
		return DAO;
	}

	public Session getSession() {
		//this.sessionFactory.
		return this.sessionFactory.openSession();
	}

	public void update(Object obj) {
		log.debug("upate obj");
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		if(obj instanceof UserDetail){
			log.debug("user detail update");
		}
		if(obj instanceof File){
			log.debug("file update");
		}
		session.merge(obj);
		// session.update(obj);
		tran.commit();
		session.flush();
		session.close();
		//sessionFactory.close();
	}

	public void insert(Object obj) {
		log.debug("insert new obj");
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		if(obj instanceof UserDetail){
			log.debug("user detail insert");
		}
		if(obj instanceof com.echoii.cloud.platform.model.File){
			log.debug("file insert");
		}
		if(obj instanceof Hdc){
			log.debug("hdc insert");
		}
		session.save(obj);
		tran.commit();
		session.flush();
		session.close();
		//sessionFactory.close();
		
	}
	
	public void batchInsert( List list ){
		log.debug("batch insert new obj");
		if( list == null || list.isEmpty() ){
			log.debug("list is null");
			return;
		}else{
			log.debug( "size = " + list.size() );
		}
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		
		Iterator iter = list.iterator();
		while( iter.hasNext() ){
			session.save( iter.next() );
		}
		
		tran.commit();
		session.flush();
		session.close();
		log.debug("batch insert end");
	}

	public void delete(Object obj) {
		log.debug("delete obj");
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		session.delete(obj);
		tran.commit();
		session.flush();
		session.close();
		//sessionFactory.close();
	}

	
	public Object getById( Class target, String id) {
		List criterions = new ArrayList();
		criterions.add(Restrictions.eq("id", id));
		List list = this.list(target, criterions);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	public Object getByDeviceId( Class target, String id) {
		List criterions = new ArrayList();
		criterions.add(Restrictions.eq("deviceId", id));
		List list = this.list(target, criterions);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public List list( Class target, List criterions, List orders ,int first, int size ) {
		Session session = this.getSession();
		session.clear();
		
		Criteria crit = this.createCriteria(session, target, criterions);
		crit.setFirstResult(first);

		if (size > 0) {
			crit.setMaxResults(size);
		}

		if (orders != null) {
			for (int i = 0; i < orders.size(); i++) {
				crit.addOrder((Order) orders.get(i));
			}
		}

		Transaction tran = session.beginTransaction();
		List list = crit.list();
		tran.commit();
		session.flush();
		session.close();
		//sessionFactory.close();
		return list;
	}
	
	public long count( Class target, List criterions) {
		Session session = this.getSession();
		session.clear();
		
		Criteria crit = this.createCriteria(session, target, criterions);
		crit.setProjection(Projections.rowCount());
       
		Object result = crit.uniqueResult();
		session.flush();
		session.close();
		if(result != null){
			 return  (Long) result;
		}else{
			return 0;
		}
		

	}
	
	public long sum( Class target, List criterions , String propertity) {
		Session session = this.getSession();
		session.clear();
		
		Criteria crit = this.createCriteria(session, target, criterions);
		crit.setProjection(Projections.sum(propertity));
		
		Object result = crit.uniqueResult();
		session.flush();
		session.close();
		if(result != null){
		    return  (Long) result;
		}else{
			return 0;
		}

	}

	public List list( Class target, List criterions, List orders) {
		Session session = this.getSession();
		session.clear();
		Criteria crit = this.createCriteria(session, target, criterions);
		if (orders != null) {
			for (int i = 0; i < orders.size(); i++) {
				crit.addOrder((Order) orders.get(i));
			}
		}
		Transaction tran = session.beginTransaction();
		List list = crit.list();
		tran.commit();
		session.flush();
		session.close();
		//sessionFactory.close();
		return list;
	}


	public List list(Class target, List criterions) {
		Session session = this.getSession();
		session.clear();
		Transaction tran = session.beginTransaction();
		List list = this.createCriteria(session, target, criterions)
				.list();
		tran.commit();
		session.flush();
		session.close();
		//sessionFactory.close();
		return list;

	}

	private Criteria createCriteria(Session session, Class target,
			List criterions) {
		Criteria criteria = session.createCriteria(target);

		if (criterions != null && !criterions.isEmpty()) {
			Iterator iter = criterions.iterator();
			while (iter.hasNext()) {
				criteria.add((Criterion) iter.next());
			}
		}

		return criteria;
	}

}
