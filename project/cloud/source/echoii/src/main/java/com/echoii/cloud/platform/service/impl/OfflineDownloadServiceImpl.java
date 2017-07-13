package com.echoii.cloud.platform.service.impl;

import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import com.echoii.cloud.platform.entity.OfflineFileEntity;
import com.echoii.cloud.platform.entity.OfflineSubFileEntity;
import com.echoii.cloud.platform.service.KtorrentService;
import com.echoii.cloud.platform.service.OfflineDownloadService;

public class OfflineDownloadServiceImpl implements OfflineDownloadService {
	

	private static volatile OfflineDownloadService SERVICE = null;
	private KtorrentService ktservice = KtorrentService.getInstance();
	
	public static OfflineDownloadService getInstance() {
		if (SERVICE == null) {
			synchronized (OfflineDownloadServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new OfflineDownloadServiceImpl();
				}
			}
		}
		return SERVICE;
	}
	
	@Override
	public OfflineFileEntity getTaskById(String id) {
		return ktservice.getTask(id);
	}


	@Override
	public String submitTask( String url) {
		try {
			List<String> before = ktservice.listAllTask();
			ktservice.addTask(url);
			Thread.sleep(3000);
			List<String> after = ktservice.listAllTask();
			if(after.size() != before.size()){
				return after.get(after.size() - 1);
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<OfflineSubFileEntity> listSubTaskById(String id) {
		return ktservice.listSubTask(id);
	}

}
