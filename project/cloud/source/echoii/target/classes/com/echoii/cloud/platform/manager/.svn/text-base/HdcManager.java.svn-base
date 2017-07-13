package com.echoii.cloud.platform.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.HdcFileEntity;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.entity.factory.FileEntityFactory;
import com.echoii.cloud.platform.entity.factory.UserEntityFactory;
import com.echoii.cloud.platform.model.Hdc;
import com.echoii.cloud.platform.model.HdcBinding;
import com.echoii.cloud.platform.model.HdcDetail;
import com.echoii.cloud.platform.model.HdcFile;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.HdcService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.service.impl.HdcServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;
import com.echoii.cloud.platform.util.HashUtil;
import com.echoii.cloud.platform.util.Status;

public class HdcManager {
	private static volatile HdcManager MANAGER = null;
	static Logger log = Logger.getLogger(HdcManager.class);
	private UserService userService;
//	private FileService fileService;
	private HdcService hdcService;
	
	FileService fs = FileServiceImpl.getInstance();
	
	public static HdcManager getInstance() {
		if (MANAGER == null) {
			synchronized (HdcManager.class) {
				if (MANAGER == null) {
					MANAGER = new HdcManager();
				}
			}
		}
		return MANAGER;
	}

	HdcManager() {
		userService = UserServiceImpl.getInstance();
	//	fileService = FileServiceImpl.getInstance();
		hdcService = HdcServiceImpl.getInstance();
	}
	
	public String active(String device_id){
		Hdc hdc = hdcService.getHdc(device_id);
		if(hdc != null){
			return hdc.getToken();
		}
		
		hdc = new Hdc();
		hdc.setCreateDate(new Date());
		hdc.setDeviceId(device_id);
		hdc.setId(HashUtil.getRandomId());
		hdc.setToken(HashUtil.getRandomId());
		hdc.setStatus(Hdc.STATUS_ACTIVE);
		hdcService.createHdc(hdc);
		return hdc.getToken();
		
	}

	/**
	 * HdcManager.java
	 * @param deviceId
	 * @param token
	 * @return
	 */
	public String register(String deviceId) {
		// TODO Auto-generated method stub
		Hdc hdc = null ;
		
		hdc = hdcService.getHdc(deviceId);
		
		if(hdc == null){
			hdc = new Hdc();
			hdc.setId(deviceId);
			hdc.setDeviceId(deviceId);
			hdc.setToken(HashUtil.getRandomId());
			hdcService.createHdc(hdc);;
			return hdc.getToken();
		}else{
			System.out.println("the device has existed id = "+deviceId);
			return null;
		}
	}	
	
	/**
	 * HdcManager.java
	 * @param deviceId
	 * @param token
	 */
	public String login(String deviceId, String token) {
		// TODO Auto-generated method stub
		if(deviceId.equals("")){
			log.debug("para error");
			return null;
		}
		Hdc hdc = hdcService.getHdc(deviceId);
		
		if(hdc==null){
			log.debug("the device does not exist");
			return null;
		}else{
			if( hdc.getToken().equals(token) ){
				log.debug("the token matches");
				return Status.OK;
			}else{
				log.debug("the token does not match");
				return null;
			}
		}
			
	}
	
	public HdcDetail device_info(String device_id){
		return hdcService.getHdcDetail(device_id);
	}
	
	public List<HdcBinding> list_bound_device(String userid, String status, int begin, int size){
		
		User user = userService.getUserById(userid);
		
		if(user == null){
			return null;
		}

		List<HdcBinding> devices = null;
		if (status.equals("all")) {
			devices = hdcService.listAllHdcBindingByUserId(userid, begin, size);
		} else {
			devices = hdcService.listHdcBindingByUserId(userid, status, begin,size);
		}

		return devices;
		
	}
	
	public boolean cancel_binding(String userid,String deviceid){
		HdcBinding hdcb = hdcService.getHdcBinding(userid, deviceid);
		
		if(hdcb == null){
			return false;
		}
		hdcService.deleteHdcBingding(hdcb);
		return true;
		
	}
	
	public List<UserEntity> list_bound_user(String device_id,String status,int begin,int size){
		
		Hdc hdc = hdcService.getHdc(device_id);
		//device exit

		if (hdc == null) {
			return null;
		}

		List<HdcBinding> devices = null;
		if (status.equals("all")) {
			devices = hdcService.listAllHdcBindingByDeviceId(device_id, begin,size);
		} else {
			devices = hdcService.listHdcBindingByDeviceId(device_id, status,begin, size);
		}
		if (devices == null) {
			return null;
		}
		// get userids for search
		Object[] ids = new Object[devices.size()];
		for (int i = 0; i < ids.length; ++i) {
			ids[i] = devices.get(i).getUserId();
		}
		// get users
		List<User> users = userService.listUserByIds(ids, begin, size);

		// factory the user to userentity
		List<UserEntity> ues = new ArrayList<UserEntity>();
		for (int i = 0; i < users.size(); ++i) {
			ues.add(UserEntityFactory.getUserEntity(users.get(i)));
		}
		return ues;
	}
	
	public String bind(String deviceId, String userId){
		User user = userService.getUserById(userId);
		Hdc hdc = hdcService.getHdc(deviceId);
		HdcBinding hbinding =hdcService.getHdcBinding(userId, deviceId); 
		
		if(hdc==null){
			log.debug("the hdc which id point to does not exist");
			return Status.NOT_FOUND;
		}
		
		if(hbinding!=null){
			log.debug("the device id has duplicated banding");
			return Status.NAME_DUP;
		}
		
		//user exit and hdc exit, binding it
		if(user != null && hdc != null){
			HdcBinding hdcb = new HdcBinding();
			hdcb.setId(HashUtil.getRandomId());
			hdcb.setCreateDate(new Date());
			hdcb.setDeviceId(deviceId);
			hdcb.setUserId(userId);
			hdcb.setStatus(HdcBinding.STATUS_REQUEST);
			hdcService.createHdcBingding(hdcb);
			return Status.OK;
		}else{
			return Status.DEVICE_ERROR;
		}
	}
	
	public boolean unbind(String deviceId, String userId){
	
		HdcBinding hdcb = hdcService.getHdcBinding(userId, deviceId);
		
		if(hdcb == null){
			return false;
		}

		hdcService.deleteHdcBingding(hdcb);
		return true;
		
	}
	
	public boolean update_binding_status(String deviceid,String token,String userid, String status){
		Hdc hdc = hdcService.getHdc(deviceid, token);

		if (hdc == null) {
			return false;
		}
		HdcBinding hdcb = hdcService.getHdcBinding(userid, deviceid);
		if (hdcb == null) {
			return false;
		}
		hdcb.setStatus(status);
		hdcService.updateHdcBinding(hdcb);
		return true;
	}

	/**
	 * HdcManager.java
	 * @param deviceId
	 * @param token
	 * @return
	 */
	public List<HdcFileEntity> listAll(String deviceId, String token) {
		log.debug("hdc manager list all file begin");
		Hdc hdc = hdcService.getHdc(deviceId);
		
		if(hdc!=null && hdc.getToken().equals(token)){
			List<HdcFile> hdcfilelist = null;
			
			log.debug("deviceId = "+deviceId+" token = "+token);
			hdcfilelist = hdcService.listAllfile(deviceId,token);
			
			log.debug("hdc file list size = " + hdcfilelist.size());
			List<HdcFileEntity> hdcfileentitylist = new ArrayList<HdcFileEntity>();
			
			Iterator<HdcFile> hdcfilelistIt = hdcfilelist.iterator();
			while( hdcfilelistIt.hasNext() ){
				HdcFile hf = hdcfilelistIt.next();
				hdcfileentitylist.add( FileEntityFactory.getDeviceFileEntity(hf) ) ;
			}
			
			return hdcfileentitylist;
		}else{
			log.debug("error : the device can not be found!");
			return null;
		}
	}

	/**
	 * HdcManager.java
	 * @param deviceId
	 * @param token
	 * @param folderId
	 * @return
	 */
	public List<HdcFileEntity> listAll(String deviceId, String token,
			String folderId) {
		// TODO Auto-generated method stub
		log.debug("hdc manager list all file begin");
		Hdc hdc = hdcService.getHdc(deviceId);
		
		if(hdc!=null && hdc.getToken().equals(token)){
			List<HdcFile> hdcfilelist = null;
			
			log.debug("deviceId = "+deviceId+" token = "+token);
			hdcfilelist = hdcService.listAllfile(deviceId,token,folderId);
			
			log.debug("hdc file list size = " + hdcfilelist.size());
			List<HdcFileEntity> hdcfileentitylist = new ArrayList<HdcFileEntity>();
			
			Iterator<HdcFile> hdcfilelistIt = hdcfilelist.iterator();
			while( hdcfilelistIt.hasNext() ){
				HdcFile hf = hdcfilelistIt.next();
				hdcfileentitylist.add( FileEntityFactory.getDeviceFileEntity(hf) ) ;
			}
			
			return hdcfileentitylist;
		}else{
			log.debug("error : the device can not be found!");
			return null;
		}
	}
}
