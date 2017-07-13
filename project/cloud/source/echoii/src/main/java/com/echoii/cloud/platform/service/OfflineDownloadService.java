package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.entity.OfflineFileEntity;
import com.echoii.cloud.platform.entity.OfflineSubFileEntity;

public interface OfflineDownloadService {
	
	public OfflineFileEntity getTaskById( String id);
	
	public List<OfflineSubFileEntity> listSubTaskById(String id);
	
	public String submitTask(String url);
	
}
