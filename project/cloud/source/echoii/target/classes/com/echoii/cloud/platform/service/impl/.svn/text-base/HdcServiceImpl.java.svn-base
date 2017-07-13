package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import com.echoii.cloud.platform.model.Hdc;
import com.echoii.cloud.platform.model.HdcBinding;
import com.echoii.cloud.platform.model.HdcDetail;
import com.echoii.cloud.platform.model.HdcFile;
import com.echoii.cloud.platform.service.HdcService;
import com.echoii.cloud.platform.service.HibernateDao;

public class HdcServiceImpl implements HdcService {
	
	static Logger log = Logger.getLogger(HdcServiceImpl.class);
	static HibernateDao dao;
	static volatile HdcService SERVICE = null;
	
	public static HdcService getInstance() {
		if (SERVICE == null) {
			synchronized (HdcServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new HdcServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	
	public HdcServiceImpl() {
		log.debug("hdc service init");
		dao = HibernateDao.getInstance();
	}

	@Override
	public void createHdcBingding(HdcBinding hdcbinding) {
		dao.insert(hdcbinding);
		
	}

	@Override
	public void createHdcDetail(HdcDetail hdcdetail) {
		dao.insert(hdcdetail);
		
	}

	@Override
	public void createHdc(Hdc hdcuser) {
		dao.insert(hdcuser);
		
	}

	@Override
	public void deleteHdcBingding(HdcBinding hdcbinding) {
		dao.delete(hdcbinding);
		
	}

	@Override
	public void deleteHdcDetail(HdcDetail hdcdetail) {
		dao.delete(hdcdetail);
		
	}

	@Override
	public void deleteHdc(Hdc hdcuser) {
		dao.delete(hdcuser);
		
	}

	@Override
	public Hdc getHdc(String device_id) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("deviceId", device_id));

		List<Hdc> result = dao.list(Hdc.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (Hdc) result.get(0);
		
	}

	@Override
	public HdcDetail getHdcDetail(String device_id) {
		
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("deviceId", device_id));

		List<HdcDetail> result = dao.list(HdcDetail.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (HdcDetail) result.get(0);
	}

	@Override
	public List<HdcBinding> listHdcBindingByUserId(String userid, String status,
			int begin, int size) {
		
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("userId", userid));
		
		criterions.add(Restrictions.eq("status", status));
		

		List<?> result = dao.list(HdcBinding.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<HdcBinding>) result;
		
	}

	@Override
	public HdcBinding getHdcBinding(String userid, String deviceid) {
		
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("userId", userid));
		
		criterions.add(Restrictions.eq("deviceId", deviceid));
		

		List<?> result = dao.list(HdcBinding.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (HdcBinding) result.get(0);
	}

	@Override
	public List<HdcBinding> listHdcBindingByDeviceId(String deviceid,
			String status, int begin, int size) {
		
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("deviceId", deviceid));
		criterions.add(Restrictions.eq("status", status));

		List<?> result = dao.list(HdcBinding.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<HdcBinding>) result;
	}

	@Override
	public Hdc getHdc(String deviceid, String token) {
		
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("token", token));
		
		criterions.add(Restrictions.eq("deviceId", deviceid));
		

		List<?> result = dao.list(Hdc.class, criterions, null, 0, 1);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (Hdc) result.get(0);
	}

	@Override
	public void updateHdcBinding(HdcBinding hdcbinding) {
		dao.update(hdcbinding);
		
	}

	@Override
	public List<HdcBinding> listAllHdcBindingByUserId(String userid, int begin,
			int size) {
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("userId", userid));
		

		List<?> result = dao.list(HdcBinding.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<HdcBinding>) result;
	}

	@Override
	public List<HdcBinding> listAllHdcBindingByDeviceId(String deviceid,
			int begin, int size) {
		List criterions = new ArrayList<>();

		criterions.add(Restrictions.eq("deviceId", deviceid));
		

		List<?> result = dao.list(HdcBinding.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<HdcBinding>) result;
	}
	
	public List<HdcFile> listAllfile(String deviceId, String token){
		log.debug("hdc service file list all");
		

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("deviceId", deviceId));
		//criterions.add(Restrictions.eq("folderId", folderId));
		criterions.add(Restrictions.eq("status", "normal"));

		
		String orderColumn = "lmf_date";
		
		
		orders.add(Order.asc("idx"));
		orders.add(Order.desc(orderColumn));
		
		
		List<HdcFile> list = dao.list(HdcFile.class, criterions, orders);

		if (list != null) {
			return list;
		} else{
			return Collections.emptyList();
			
		}
		
	}
	
	public List<HdcFile> listAllfile(String deviceId, String token,String folderId){
		log.debug("hdc service file list all");
		

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("deviceId", deviceId));
		criterions.add(Restrictions.eq("folderId", folderId));
		criterions.add(Restrictions.eq("status", "normal"));

		
		String orderColumn = "lmf_date";
		
		
		orders.add(Order.asc("idx"));
		orders.add(Order.desc(orderColumn));
		
		
		List<HdcFile> list = dao.list(HdcFile.class, criterions, orders);

		if (list != null) {
			return list;
		} else{
			return Collections.emptyList();
			
		}
		
	}

	@Override
	public HdcFile getHdcFile(String fileId) {
        log.debug("hdc service get file");
		

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("id", fileId));
		criterions.add(Restrictions.eq("status", "normal"));

		
		String orderColumn = "lmf_date";
		
		
		orders.add(Order.asc("idx"));
		orders.add(Order.desc(orderColumn));
		
		
		List<HdcFile> list = dao.list(HdcFile.class, criterions, orders,0,1);
		
		
		if(list == null || list.size() == 0){
			return null;
		}

		return list.get(0);
	}
}
