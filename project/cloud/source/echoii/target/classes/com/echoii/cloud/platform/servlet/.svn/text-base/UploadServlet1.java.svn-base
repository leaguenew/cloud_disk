package com.echoii.cloud.platform.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import com.echoii.cloud.platform.manager.UserManager;
import com.echoii.cloud.platform.service.CacheService;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.impl.CacheServiceRedisImpl;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DateUtil;
import com.echoii.cloud.platform.util.HashUtil;


public class UploadServlet1 extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UploadServlet1.class);
	private static Config config = Config.getInstance();;
	private static DiskFileItemFactory factory = null;//DiskFileItemFactory package?
	private static ServletFileUpload sfu = null;
	private FileService fileService = FileServiceImpl.getInstance(); ;
	private UserManager usermanager = UserManager.getInstance();
	private CacheService cacheservice =  CacheServiceRedisImpl.getInstance();

	
	public void init(){
		log.debug("Upload servlet init");
		factory = new DiskFileItemFactory();

		// cache size in memory, 1M
		factory.setSizeThreshold(config.getIntValue("upload.size.threshold", 1024 * 1024));

		// when greater than the cache size, the folder which store the data
		String tempDirectory = config.getStringValue("upload.temp.directory", "/data/echoii/tempdata");
		exitFolder(tempDirectory);
		factory.setRepository(new File(tempDirectory));

		sfu = new ServletFileUpload(factory);

		// single file size here is 1G
		sfu.setFileSizeMax(config.getLongValue("upload.max.size",  1024 * 1024 * 1024));

		// the max size when there are many files, here is 10G
		sfu.setSizeMax(10 * config.getLongValue("upload.max.size", 1024 * 1024 * 1024));
		sfu.setHeaderEncoding("UTF-8");
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.debug("upload begin");
		
		if (factory == null || sfu == null) {
			init();
		}
		
		try {
			if (ServletFileUpload.isMultipartContent(request)) {
				String uploadFolderPath = config.getStringValue("upload.directory", "/mnt/moosefs/echoii/data/")
						+ DateUtil.parseDateToString(new Date(),DateUtil.PATTERN_yyyyMM) ;
				
				exitFolder(uploadFolderPath);

				List<FileItem> items = sfu.parseRequest(request);	
				if(items.size() == 0 || items == null ){
					return;
				}
				Iterator<FileItem> iter = items.iterator();
				
				UserUploadFieldInfo uploadinfo = getUserUploadFieldInfo(iter);
				String userId = uploadinfo.user_id,folderId = uploadinfo.folder_id,token = uploadinfo.token;
				//check the para
				if(folderId == null || token == null || userId == null){
					log.debug("info not complete, check the userid, token or folderid");
					return;
				}
				
				//check the user
				if(!checkUser(token,userId,getIpAddr(request))){
					log.debug("validation failure, please login");
					return;
				}
				
				iter = items.iterator();
				com.echoii.cloud.platform.model.File file = new com.echoii.cloud.platform.model.File();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();  
					if (!item.isFormField() && item.getName().length() > 0) {
						String name = item.getName().substring(item.getName().lastIndexOf("/") + 1).trim().replaceAll("[\\s]", "_");
						String type = name.substring(name.lastIndexOf(".") + 1);	
						
						//same folder may meet the same file,
						//recursion get the new name
						name = getName(userId, folderId, name); 						
						
						String metaId = HashUtil.getRandomId();
						
						//for the flv or mp4, the metaid contain the suffix
						if(type.toLowerCase().equals("flv")){
							metaId = metaId + ".flv";
						}else if(type.toLowerCase().equals("mp4")){
							metaId = metaId + ".mp4";
						}else{
							//do nothing
						}
						
						String path = uploadFolderPath + "//"+ metaId;
						BufferedInputStream in = new BufferedInputStream(item.getInputStream());
						BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(path)));
						
						long size = Streams.copy(in, out, true);

						file.setCreateDate(new Date());
						file.setId(HashUtil.getRandomId());
						file.setMetaFolder(DateUtil.parseDateToString(file.getCreateDate(), DateUtil.PATTERN_yyyyMM));
						file.setMetaId(metaId);
						file.setName(name);
						if(type.equals(name)){
							file.setType(com.echoii.cloud.platform.model.File.TYPE_BINARY);
						}else{
							file.setType(type.toLowerCase());
						}	
						file.setSize(size);
						file.setStatus(com.echoii.cloud.platform.model.File.STATUS_NORMAL);
						file.setVersion(0);
						file.setLmf_date(new Date());		
					}
				}
				
				if (folderId.equals("root")) {
					file.setPath("/root/" + file.getName());
				} else if (fileService.getFileById(folderId) == null) {
					return;
				} else {
					file.setPath(fileService.getFileById(folderId).getPath() + "/" + file.getName());
				}
				
				file.setFolderId(folderId);
				file.setUserId(userId);
				
				fileService.createFile(file);
				usermanager.updateStatistics(file.getUserId());
				
				log.debug("upload complete!");
		
			}
		} catch (Exception e) {
			if(e instanceof SizeLimitExceededException){
				log.debug("size limted!");
				return;
			}else{
				e.printStackTrace();
			}		
		}
		
	}
	
	
	//recursion get the new name
	private String getName(String userId, String folderId, String name){
		com.echoii.cloud.platform.model.File oldfile = fileService.getFileByName(userId, folderId, name);
		
		if(oldfile == null){
			return name;
		}
		
		if(oldfile.getType().equals(com.echoii.cloud.platform.model.File.TYPE_BINARY)){ //binary file
			String newName = oldfile.getName() + "_" + 1;
			return getName(userId, folderId, newName);
		}else{
			String newName = oldfile.getName().substring(0,oldfile.getName().lastIndexOf("."))
					+ "_" + 1 +"."
					+ oldfile.getName().substring(oldfile.getName().lastIndexOf(".") + 1);
			return getName(userId, folderId, newName);
		}
		
	}
	
	private void exitFolder(String path){
		File folder = new File(path);
		if(!folder.isDirectory() && !folder.exists()){
			folder.mkdirs();
			log.debug(path+ " does not exit! create it!");
			return;
		}
	}
	
	private class UserUploadFieldInfo{
		String user_id;
		String token;
		String folder_id;
		
	}
	
	private UserUploadFieldInfo getUserUploadFieldInfo(Iterator<FileItem> iter){
		UserUploadFieldInfo uploadinfo = new UserUploadFieldInfo();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();  
			if (item.isFormField()) {	
				if (item.getFieldName().equals("user_id")) {
					log.debug("fieldValue user_id = " + item.getString());
					uploadinfo.user_id = item.getString();
				} else if (item.getFieldName().equals("folder_id")) {
					log.debug("fieldValue folder_id = " + item.getString());
					uploadinfo.folder_id  = item.getString();
				} else if (item.getFieldName().equals("token")) {
					log.debug("fieldValue token = " + item.getString());
					uploadinfo.token = item.getString();		
				} else {
					log.debug("unknown para = " + item.getString());
				}
			} 
		}
		return uploadinfo;
	}
	
	private boolean checkUser(String token,String userId, String ip){
		String key = userId+":"+ip;
	
		return cacheservice.validate(token, key);

	}
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP"); //first
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");//second
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();//last
		}
		return ip;
	}
	
}
