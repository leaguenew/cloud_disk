package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.Hdc;
import com.echoii.cloud.platform.model.HdcBinding;
import com.echoii.cloud.platform.model.HdcDetail;
import com.echoii.cloud.platform.model.HdcFile;


public interface HdcService {
	
	public void createHdcBingding(HdcBinding hdcbinding);
	
	public void updateHdcBinding(HdcBinding hdcbinding);
	
	public void createHdcDetail(HdcDetail hdcdetail);
	
	public void createHdc(Hdc hdcuser);
	
	public void deleteHdcBingding(HdcBinding hdcbinding);
	
	public void deleteHdcDetail(HdcDetail hdcdetail);
	
	public void deleteHdc(Hdc hdcuser);
	
	public Hdc getHdc(String device_id);
	
	public HdcBinding getHdcBinding(String userid, String deviceid);
	
	public HdcDetail getHdcDetail(String device_id);
	
	public List<HdcBinding> listHdcBindingByUserId(String userid, String status,int begin, int size);
	
	public List<HdcBinding> listAllHdcBindingByUserId(String userid,int begin, int size);
	
	public List<HdcBinding> listHdcBindingByDeviceId(String deviceid, String status,int begin, int size);
	
	public List<HdcBinding> listAllHdcBindingByDeviceId(String deviceid, int begin, int size);
	
	public Hdc getHdc(String deviceid,String token);
	
	public HdcFile getHdcFile(String fileId);

	public List<HdcFile> listAllfile(String deviceId, String token);

	/**
	 * HdcService.java
	 * @param deviceId
	 * @param token
	 * @param folderId
	 * @return
	 */
	public List<HdcFile> listAllfile(String deviceId, String token,String folderId);

	
	

}
