package com.echoii.cloud.platform.manager;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.OfflineFileEntity;
import com.echoii.cloud.platform.entity.OfflineSubFileEntity;
import com.echoii.cloud.platform.jersey.Auth;
import com.echoii.cloud.platform.model.TorrentFile;
import com.echoii.cloud.platform.model.TorrentTable;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.OfflineDownloadCacheService;
import com.echoii.cloud.platform.service.OfflineDownloadService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.service.impl.OfflineDownloadCacheServiceImpl;
import com.echoii.cloud.platform.service.impl.OfflineDownloadServiceImpl;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.HashUtil;


public class OfflineDownloadManager {
	
	private static volatile OfflineDownloadManager MANAGER = null;	
	static Logger log = Logger.getLogger(OfflineDownloadManager.class);
	private OfflineDownloadCacheService offlinedownloadcacheservice;
	private OfflineDownloadService offlinedownloadservice;
	private FileService fileservice;
	private Config config = Config.getInstance();
	private String appserver;
	
	public static OfflineDownloadManager getInstance() {
		if (MANAGER == null) {
			synchronized (OfflineDownloadManager.class) {
				if (MANAGER == null) {
					MANAGER = new OfflineDownloadManager();
				}
			}
		}
		return MANAGER;
	}

	OfflineDownloadManager() {
		offlinedownloadcacheservice = OfflineDownloadCacheServiceImpl.getInstance();
		offlinedownloadservice = OfflineDownloadServiceImpl.getInstance();
		fileservice = FileServiceImpl.getInstance();
		appserver = config.getStringValue("echoii.app.server", "http://172.21.7.199:81/echoii/");

	}
	
	public boolean addTask(String userId, String fileId){
		
		//construct the url
		String url = appserver + "service/0/download/torrent?file_id="+fileId;
		log.debug(url);
		//check the url and get the hashvalue
		String torrentMd5 = CommUtil.getTorrentHash(url); 
		log.debug(torrentMd5);
		
		//the url is not correct, return
		if(torrentMd5 == null){
			return false;
		}
		log.debug(torrentMd5);
		
		TorrentTable torrenttable  = offlinedownloadcacheservice.getTorrentTableByMd5(torrentMd5);
		//has download , return success
		if(torrenttable != null){
			log.debug("torrenttable is not null");
			
			TorrentFile tfile = offlinedownloadcacheservice.getTorrentFile(userId, torrentMd5);
			
			if(tfile == null){
				log.debug("TorrentFile is not null");
				TorrentFile tfile1 = new TorrentFile();
				tfile1.setCreateDate(new Date());
				tfile1.setId(HashUtil.getRandomId());
				tfile1.setInfoHash(torrenttable.getInfoHash());
				tfile1.setStatus(TorrentFile.STATUS_NORMAL);
				tfile1.setTorrentMd5(torrentMd5);
				tfile1.setUserId(userId);
				offlinedownloadcacheservice.createTorrentFile(tfile1);
			}else if(tfile.getStatus().equals(TorrentFile.STATUS_DELETE)){
				tfile.setStatus(TorrentFile.STATUS_NORMAL);
				tfile.setCreateDate(new Date());
				offlinedownloadcacheservice.updateTorrentFile(tfile);
			}
			
			return true;	
		}

		String infoHash = offlinedownloadservice.submitTask(url);
		
		if(infoHash == null){
			log.debug("infoHash is null");
			return false;
		}
		
		
		//Persistent storage 
		TorrentFile torrentfile = new TorrentFile();
		torrentfile.setId(HashUtil.getRandomId());
		torrentfile.setCreateDate(new Date());
		torrentfile.setInfoHash(infoHash);
		torrentfile.setStatus(TorrentFile.STATUS_NORMAL);
		torrentfile.setTorrentMd5(torrentMd5);
		torrentfile.setUserId(userId);
		offlinedownloadcacheservice.createTorrentFile(torrentfile);
		
		//torrentTable persistent storage
		TorrentTable table = new TorrentTable();
		table.setCreateDate(new Date());
		table.setId(HashUtil.getRandomId());
		table.setInfoHash(infoHash);
		table.setStatus(TorrentTable.STATUS_NORMAL);
		table.setTorrentMd5(torrentMd5);
		offlinedownloadcacheservice.createTorrentTable(table);
		
		
		return true;
		
	}
	
	public OfflineFileEntity info(String userId, String infoHash){	
	
		TorrentFile tfile = offlinedownloadcacheservice.getTorrentFileByInfoHash(userId, infoHash);
		if(tfile == null){
			return null; //the file does not belong the user;
		}
		
		
		//search the torrentTable, if has completed, the return the result;
		TorrentTable torrenttable = offlinedownloadcacheservice.getTorrentTableByHash(infoHash);
		
		if(torrenttable.getStatus().equals(TorrentTable.STATUS_COMPLETE)){
			com.echoii.cloud.platform.model.File file = fileservice.getFileById(torrenttable.getFileId());
			OfflineFileEntity entity = new OfflineFileEntity();
			entity.setInfoHash(infoHash);
			entity.setName(file.getName());
			entity.setSize(CommUtil.formatSizeDisp(file.getSize()));
			entity.setPercents("100");
			entity.setStatus(OfflineFileEntity.STATUS_COMPLETE);
			return entity;
		}
		
		return offlinedownloadservice.getTaskById(infoHash);
	}
	
	public boolean stop(String userId, String infoHash){
		
		TorrentFile tfile = offlinedownloadcacheservice.getTorrentFileByInfoHash(userId, infoHash);
		
		if(tfile == null ){
			return false; //the file does not belong the user;
		}
		
		tfile.setStatus(TorrentFile.STATUS_STOP);
		offlinedownloadcacheservice.updateTorrentFile(tfile);
		
		
		return true;
	}
	
     public boolean start(String userId, String infoHash){
		
		TorrentFile tfile = offlinedownloadcacheservice.getTorrentFileByInfoHash(userId, infoHash);
		
		if(tfile == null){
			return false; //the file does not belong the user;
		}
		
		tfile.setStatus(TorrentFile.STATUS_NORMAL);
		offlinedownloadcacheservice.updateTorrentFile(tfile);
		
		return true;
	}
     
     public boolean remove(String userId, String infoHash){
 		
		TorrentFile tfile = offlinedownloadcacheservice.getTorrentFileByInfoHash(userId, infoHash);
		
		if(tfile == null){
			return false; //the file does not belong the user;
		}
		
		tfile.setStatus(TorrentFile.STATUS_DELETE);
		offlinedownloadcacheservice.updateTorrentFile(tfile);
			
		return true;
	}
     
     public List<OfflineFileEntity> list(String userId){
    	 List<TorrentFile> torrentfiles = offlinedownloadcacheservice.listTorrentFile(userId);
    	 if(torrentfiles == null || torrentfiles.size() == 0){
    		 log.debug("no data");
    		 return null;
    	 }
    	 List<OfflineFileEntity> offfile = new ArrayList<OfflineFileEntity>();
    	 for(int i = 0 ; i < torrentfiles.size(); ++i){
    		 OfflineFileEntity file = this.info(userId,torrentfiles.get(i).getInfoHash());
    		 if(file != null){
    			 offfile.add(file);
    		 }
    	 }
    	 
    	 return offfile;
    	 
     }
     
     public List<OfflineSubFileEntity> info_detail(String userId, String infoHash){

 		TorrentFile tfile = offlinedownloadcacheservice.getTorrentFileByInfoHash(userId, infoHash);
 		if(tfile == null){
 			return null; //the file does not belong the user;
 		}
 		
 		
 		//search the torrentTable, if has completed, the return the result;
 		TorrentTable torrenttable = offlinedownloadcacheservice.getTorrentTableByHash(infoHash);
 		
 		if(torrenttable.getStatus().equals(TorrentTable.STATUS_COMPLETE)){
 			List<com.echoii.cloud.platform.model.File> files = fileservice.listAllSubFileByFolderId(torrenttable.getFileId());
 			if(files == null){
 				return null;
 			}
 			List<OfflineSubFileEntity> subfiles = new ArrayList<OfflineSubFileEntity>();
 			for(int i = 0; i < files.size(); ++i){
 				OfflineSubFileEntity entity = new OfflineSubFileEntity();
 				entity.setName(files.get(i).getPath());
 				entity.setSize(CommUtil.formatSizeDisp(files.get(i).getSize()));
 				entity.setParentInfoId(infoHash);
 				subfiles.add(entity);
 			}
 			
 			return subfiles;
 			
 		}
 		
 		return offlinedownloadservice.listSubTaskById(infoHash);
    	 
     }
     
     public boolean empty(String userId){
    	 List<TorrentFile> torrentfiles = offlinedownloadcacheservice.listTorrentFile(userId);
    	 if(torrentfiles == null || torrentfiles.size() == 0){
    		 return false;
    	 }
    	 
    	 for(int i = 0 ; i < torrentfiles.size(); ++i){
    		 TorrentFile file = torrentfiles.get(i);
    		 file.setStatus(TorrentFile.STATUS_DELETE);
    		 offlinedownloadcacheservice.updateTorrentFile(file);
    	 }
    	 
    	 return true;
     }


}
