package com.echoii.cloud.platform.service.impl;

import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.service.EmailService;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.HibernateDao;
import com.echoii.cloud.platform.util.Config;;

public class FileServiceImpl implements FileService {

	static Logger log = Logger.getLogger(FileServiceImpl.class);
	private HibernateDao dao;
	static volatile FileService  SERVICE = null;
	static Config config = Config.getInstance();

	public FileServiceImpl() {
		log.debug("file service init");
		dao = HibernateDao.getInstance();
	}

	public FileServiceImpl(HibernateDao dao) {
		this.dao = dao;
	}

	public static FileService getInstance() {
		if (SERVICE == null) {
			synchronized (FileServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new FileServiceImpl();
				}
			}
		}
		return SERVICE;
	}

	public static FileService getService(HibernateDao dao) {
		synchronized (log) {
			if (SERVICE == null) {
				SERVICE = new FileServiceImpl(dao);
			}
		}
		return SERVICE;
	}

	@Override
	public void createFile(File file) {
		log.debug("file service create file");
		if (file == null)
			return;
		dao.insert(file);
	}

	@Override
	public void updateFile(File file) {
		log.debug("file service update file");
		if (file == null)
			return;
		dao.update(file);
	}

	@Override
	public File getFileById(String id) {
		log.debug("file service find by id");
		if (id == "" || id.equals(""))
			return null;
		log.debug("find by id :" + id);
		File file = new File();
		file = (File) dao.getById(File.class, id);
		return file;
		//return (File) dao.getById(File.class, id);
	}

	private List<File> listSubDirFile(String userId, String folderId){
		log.debug("file servie list all");
		if (userId == null || userId.equals("")) {
			return Collections.emptyList();
		}
		
		if (folderId == null || folderId.equals("")) {
			folderId = "root";
		}
		
		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("folderId", folderId));
		
		List<File> list = dao.list(File.class, criterions,null);

		if (list != null) {
			return list;
		} else
			return Collections.emptyList();
	}
	
	/* delete file Recursively */
	private void deleteFileRecursive(File file,String removeSource){
		List<File> childs = listSubDirFile(file.getUserId(),file.getId());
		
		if(childs==null){
			return ;
		}
		
		for(int i=0;i<childs.size();i++){
			File child = childs.get(i);
			if(child.getType().equals("folder")){
				if(removeSource.equals("false")){
					deleteFileRecursive(child,removeSource);
					child.setStatus(File.STATUS_SUB_DELETE);
					updateFile(child);
				}else if(removeSource.equals("true")){
					deleteFileRecursive(child,removeSource);
					child.setStatus(File.STATUS_PERMANENT_DELETE);
					updateFile(child);
				}
			}else{
				if(removeSource.equals("false")){
					child.setStatus(File.STATUS_SUB_DELETE);
					updateFile(child);
				}else if(removeSource.equals("true")){
					child.setStatus(File.STATUS_PERMANENT_DELETE);
					updateFile(child);
				}
			}
		}
	}
	
	@Override
	public void deleteFile(String fileId,String removeSource) {
		log.debug("file service delete");
		if (fileId == "" || fileId.equals(""))
			return;
		File file = getFileById(fileId);
		
		//dao.delete(file);
		//log.debug("[fileserviceimpl delete file directly]: id = "+file.getId()+" name = "+file.getName()+" status = "+file.getStatus()+" type = "+file.getType());
		/*
		 * removeSource = "false" : just modify the status from "normal" to "delete" or "sub_delete".
		 * removeSource = "true" : modify the status to "permanent_delete"
		 * */
		if(removeSource.equals("false")){
			if(file.getType().equals("folder")){
				deleteFileRecursive(file,removeSource);
				file.setStatus(File.STATUS_DELETE);
				updateFile(file);
			}else{
				file.setStatus(File.STATUS_DELETE);
				updateFile(file);
			}
		}else if(removeSource.equals("true")){
			if(file.getType().equals("folder")){
				deleteFileRecursive(file,removeSource);
				file.setStatus(File.STATUS_PERMANENT_DELETE);
				updateFile(file);
			}else{				
				file.setStatus(File.STATUS_PERMANENT_DELETE);
				updateFile(file);
			}
		}
	}

	private void recoverFileRecursive(File file) {
		List<File> childs = listSubDirFile(file.getUserId(),file.getId());
		if(childs==null){
			return;
		}
		
		for(int i=0;i<childs.size();i++){
			File child = childs.get(i);
			
			if(child.getType().equals("folder")){
				recoverFileRecursive(child);
				child.setStatus(File.STATUS_NORMAL);
				child.setLmf_date(new Date());
				updateFile(child);
			}else{
				child.setStatus(File.STATUS_NORMAL);
				child.setLmf_date(new Date());
				updateFile(child);
			}
		}
	}
	
	@Override
	public void recoverFile( String fileId ){
		log.debug("file service recover");
		if(fileId==""||fileId.equals("")){
			return ;
		}
		File file = getFileById(fileId);
		
		/* folder's name duplicated */
		if(file.getType().equals("folder")){
			file.setStatus(File.STATUS_NORMAL);
			file.setLmf_date(new Date());
			updateFile(file);
			String filename = file.getName();
			int filenamelength = filename.length();
			int i = 1;
			while(getFileByName(file)!=null){
				log.debug("duplicated file name : "+file.getName());
				file.setName(filename.substring(0, filenamelength)+"_recover_"+i);
				i++;
			}
			updateFile(file);
			recoverFileRecursive(file);
			
		}else{/* file's name duplicated */
			file.setStatus(File.STATUS_NORMAL);
			file.setLmf_date(new Date());
			updateFile(file);
			
			int i = 1;
			String filename = file.getName();
			//no filename extension
			if( file.getType().equals("binary") || file.getType() == "binary" || !filename.matches(".*\\..*")){
				int filenamelength = filename.length();
				
				while( getFileByName(file)!=null ){
					log.debug("duplicated file name : "+file.getName());
					file.setName(filename.substring(0,filenamelength)+"_recover_"+i);
					i++;
				}
			}else{//with filename extension			
				int filenamelength = filename.substring(0, filename.lastIndexOf(".")).length();
				
				while( getFileByName(file)!=null ){
					log.debug("duplicated file name : "+file.getName());
					file.setName(filename.substring(0,filenamelength)+"_recover_"+i+"."+file.getType());
					i++;
				}
			}
			updateFile(file);
		}//else
		
		
	}

	@Override
	public List<File> listAllFile(String userId, String folderId, String order,
			String orderColumn, int begin, int size) {

		log.debug("file servie list all");
		if (userId == null || userId.equals("")) {
			return Collections.emptyList();
		}
		
		if (folderId == null || folderId.equals("")) {
			folderId = "root";
		}
		
		if (begin < 0)
			begin = 0;

		// service.size.max=1000 in config.properties.
		if (size > config.getIntValue("service.size.max", 1000)) {
			size = config.getIntValue("service.size.max", 1000);
		}

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("folderId", folderId));
		criterions.add(Restrictions.eq("status", "normal"));

		
		if( orderColumn == null
				|| orderColumn.equals("")) {
			orderColumn = "lmf_date";
		}
		
		orders.add(Order.asc("idx"));
		if (order == null || ! order.equals("asc")) {
			orders.add(Order.desc(orderColumn));
		}else{
			orders.add(Order.asc(orderColumn));
		}
		
		List<File> list = dao.list(File.class, criterions, orders, begin, size);

		if (list != null) {
			return list;
		} else
			return Collections.emptyList();
	}

	@Override
	/*
	 * list all the file totally without recursive
	 * */
	public List<File> listAllFile(String userId,  String order,
			String orderColumn, int begin, int size) {

		log.debug("file servie list all");
		if (userId == null || userId.equals("")) {
			return Collections.emptyList();
		}
		
		
		
		if (begin < 0)
			begin = 0;

		// service.size.max=1000 in config.properties.
		//if (size > config.getIntValue("service.size.max", 1000)) {
		size = config.getIntValue("service.size.max", 1000);
		//}

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("userId", userId));
		//criterions.add(Restrictions.eq("folderId", folderId));
		criterions.add(Restrictions.eq("status", "normal"));

		
		if( orderColumn == null
				|| orderColumn.equals("")) {
			orderColumn = "lmf_date";
		}
		
		orders.add(Order.asc("idx"));
		if (order == null || ! order.equals("asc")) {
			orders.add(Order.desc(orderColumn));
		}else{
			orders.add(Order.asc(orderColumn));
		}
		
		List<File> list = dao.list(File.class, criterions, orders, begin, size);

		if (list != null) {
			return list;
		} else
			return Collections.emptyList();
	}

	
	@Override
	public List<File> listFileByTypes(String userId, String folderId,
			List<String> typeList, String order, String orderColumn, int begin,
			int size) {

		log.debug("file service list by types");
		if (userId == null || userId.equals(""))
			return Collections.emptyList();
		if (folderId.equals(""))
			folderId = "root";
		if (order == null || order.equals("") || orderColumn == null
				|| orderColumn.equals(null))
			return Collections.emptyList();

		if (begin < 0)
			begin = 0;

		// service.size.max=1000 in config.properties.
		if (size > config.getIntValue("service.size.max", 1000)) {
			size = config.getIntValue("service.size.max", 1000);
		}

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("status", "normal"));
		criterions.add(Restrictions.in("type", typeList));

		orders.add(Order.asc("idx"));
		if (order.equals("asc")) { // if order equals to asc
			orders.add(Order.asc(orderColumn));
		} else if (order.equals("desc")) {
			orders.add(Order.desc(orderColumn));
		}

		List<File> list = dao.list(File.class, criterions, orders, begin, size);

		if (list != null) {
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	
	@Override
	public List<File> listFileByTypesExclude(String userId, String folderId,
			List<String> typeList, String order, String orderColumn, int begin,
			int size) {

		log.debug("file service list by types exclude");
		if (userId == null || userId.equals(""))
			return Collections.emptyList();
		if (folderId.equals(""))
			folderId = "root";
		if (order == null || order.equals("") || orderColumn == null
				|| orderColumn.equals(null))
			return Collections.emptyList();

		if (begin < 0)
			begin = 0;

		// service.size.max=1000 in config.properties.
		if (size > config.getIntValue("service.size.max", 1000)) {
			size = config.getIntValue("service.size.max", 1000);
		}

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("userId", userId));
		//criterions.add(Restrictions.eq("folderId", folderId));
		criterions.add(Restrictions.eq("status", "normal"));
		criterions.add(Restrictions.not(Restrictions.in("type", typeList)));
		/*folder should not be list*/
		criterions.add(Restrictions.not(Restrictions.eq("type", "folder")));


		orders.add(Order.asc("idx"));
		if (order.equals("asc")) { // if order equals to asc
			orders.add(Order.asc(orderColumn));
		} else if (order.equals("desc")) {
			orders.add(Order.desc(orderColumn));
		}

		List<File> list = dao.list(File.class, criterions, orders, begin, size);

		if (list != null) {
			return list;
		} else {
			return Collections.emptyList();
		}
	}
	

	
	@Override
	public List<File> listTrushFile(String userId, String order,
			String orderColumn, int begin, int size) {

		log.debug("file service list trush");

		if (userId.equals("") || userId == null) {
			return Collections.emptyList();
		}
		if (order == null || order.equals("") || orderColumn == null
				|| orderColumn.equals(null))
			return Collections.emptyList();

		if (begin < 0)
			begin = 0;

		// service.size.max=1000 in config.properties.
		if (size > config.getIntValue("service.size.max", 1000)) {
			size = config.getIntValue("service.size.max", 1000);
		}

		List criterions = new ArrayList();
		List orders = new ArrayList();

		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("status", "delete"));// Status.STATUS.OK?
		
		orders.add(Order.asc("idx"));
		if (order.equals("asc")) {
			orders.add(Order.asc(orderColumn));
		} else if (order.equals("desc")) {
			orders.add(Order.desc(orderColumn));
		}
		
		
		List<File> list = dao.list(File.class, criterions, orders, begin, size);

		if (list != null) {
			return list;
		} else {
			return Collections.emptyList();
		}
	}

	/*Method1:check if there is a duplicated file under parent folder*/
	public boolean isExistFileName(String userId, String fileId,
			String folderId, String targetName, String fileType) {
		org.hibernate.Session session = dao.getSession();
		org.hibernate.Transaction tran = session.beginTransaction();
		List list;
		Criteria crit;
		crit = session.createCriteria(File.class);
		crit.add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("folderId", folderId))
				.add(Restrictions.eq("name", targetName))
				.add(Restrictions.eq("status", "normal"))
				.add(Restrictions.ne("id", fileId));
		list = crit.list();

		tran.commit();
		session.close();
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/*Method2:check if there is a duplicated file under parent folder*/
	public File getFileByName(File file) {
		
		if(file == null){
			log.debug("The file is null");
			return null;
		}
		
		org.hibernate.Session session = dao.getSession();
		org.hibernate.Transaction tran = session.beginTransaction();
		List list;
		Criteria crit ;
		crit = session.createCriteria(File.class);
		crit.add(Restrictions.eq("userId", file.getUserId()))
			.add(Restrictions.eq("folderId",file.getFolderId()))
			.add(Restrictions.eq("name",file.getName()))
			.add(Restrictions.eq("status", file.getStatus()))
			.add(Restrictions.ne("id", file.getId()));
		list = crit.list();
		
		tran.commit();
		session.close();
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return file;
		}
}
	
	@Override
	public List<File> listFileByIds(Object[] list, String status, int begin,
			int size) {

		List criterions = new ArrayList<>();

		criterions.add(Restrictions.in("id", list));
		criterions.add(Restrictions.eq("status", status));

		List<?> result = dao.list(File.class, criterions, null, begin, size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<File>) result;
	}

	@Override
	public List<File> listUpdateFile(String userId, long date, int begin , int size, List<String> typeList) {
		List criterions = new ArrayList<>();

	//	criterions.add(Restrictions.in("id", list));
		List orders = new ArrayList();
	
		orders.add(Order.asc("lmf_date"));
		
		criterions.add(Restrictions.eq("userId", userId));
		if(date != 0){
			Date date1 = new Date(date);
			criterions.add(Restrictions.gt("createDate", date1));
		}
		
		if(typeList != null){
			criterions.add(Restrictions.in("type", typeList));
		}
		
		//criterions.add(Restrictions.eq("folderId", "root"));
		criterions.add(Restrictions.not(Restrictions.eq("type", "folder")));

		List<?> result = dao.list(File.class, criterions, orders, begin,size);

		if (result == null || result.size() < 1) {
			return null;
		}
		return (List<File>) result;
	}

	@Override
	public File getFileByName(String userId, String folderId, String name) {

		List criterions = new ArrayList<>();
		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.eq("folderId",folderId));
		criterions.add(Restrictions.eq("name",name));
		criterions.add(Restrictions.eq("status", File.STATUS_NORMAL));
		List<?> result = dao.list(File.class, criterions, null, 0, 1);
		if (result == null || result.size() < 1) {
			return null;
		}

		return (File) result.get(0);
	}

	@Override
	public List<File> listAllSubFileByFolderId(String FolderId) {
		// TODO Auto-generated method stub
		return null;
	}


	
	

}
