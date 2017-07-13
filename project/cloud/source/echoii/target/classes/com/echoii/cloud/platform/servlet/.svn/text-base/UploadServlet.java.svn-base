package com.echoii.cloud.platform.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import com.echoii.cloud.platform.manager.UserManager;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DateUtil;
import com.echoii.cloud.platform.util.HashUtil;
import com.echoii.cloud.platform.util.ParaSet;


public class UploadServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(UploadServlet.class);
	private static Config config;
	private DiskFileItemFactory dfif;//DiskFileItemFactory package?
	private ServletFileUpload sfu;
	
	private static FileService fileService = null;
	private static UserManager usermanager = null; 


		
	public UploadServlet(){
		//log.debug("begin");
		super();
	}
	
	public void init(){
		log.debug("Upload servlet init");
		
		if(fileService==null){
			log.debug("file service is null");
		  fileService = FileServiceImpl.getInstance(); 
		}
		
		if(usermanager==null){
			log.debug("user service is null");
			usermanager = UserManager.getInstance();	
		}

		
		config = Config.getInstance();
			
		dfif = new DiskFileItemFactory();
		
		dfif.setSizeThreshold(config.getIntValue("upload.size.threshold", 1024*1024));
		dfif.setRepository(new File(config.getStringValue(
				"upload.temp.directory", "/data/echoii/tempdata/")));
		
		sfu = new ServletFileUpload(dfif);
		sfu.setSizeMax(config.getLongValue("upload.max.size", 1024*1024*1024));
		sfu.setHeaderEncoding("utf8");

		log.debug("Upload servlet init quit");
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.debug("upload begin");
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		//check upload file directory
		String uploadfolderpath = config.getStringValue("upload.directory", "/mnt/moosefs/echoii/data/");
		log.debug("upload directory = "+uploadfolderpath);	
		try{
			File uploadfolder = new File( uploadfolderpath );
			if(!uploadfolder.exists()&&!uploadfolder.isDirectory()){
				log.debug("folder = "+uploadfolderpath+" does not exist , then create it!");
				boolean flag = uploadfolder.mkdirs();
				if(flag){
					log.debug("create upload folder success");
				}else{
					log.debug("create upload folder fail");
				}
			}
			if(!uploadfolder.canWrite()){
				log.error("file system error, path:" + config.getStringValue("upload.directory","/mnt/moosefs/echoii/data/") );
			}
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		
		//check upload temp file directory
		String uploadtempfolderpath = config.getStringValue("upload.temp.directory", "/data/echoii/tempdata/");
		log.debug("upload temp directory = "+uploadtempfolderpath);	
		try{
			File uploadtempfolder = new File( uploadtempfolderpath );
			if(!uploadtempfolder.exists()&&!uploadtempfolder.isDirectory()){
				log.debug("folder = "+uploadfolderpath+" does not exist , then create it!");
				boolean flag = uploadtempfolder.mkdirs();
				if(flag){
					log.debug("create upload temp folder success");
				}else{
					log.debug("create upload temp folder fail");
				}
			}
			if(!uploadtempfolder.canWrite()){
				log.error("file system error, path:" + config.getStringValue("upload.directory","/data/echoii/tempdata/") );
			}
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		
		long sizeLimit = config.getLongValue("upload.max.size", 536870912);
		
		log.debug("request.getContentLength() = "+request.getContentLength()+" sizeLimit = "+sizeLimit);
		
		if(request.getContentLength() > sizeLimit ){
			log.debug("file size larger than limit");
			return ;
		}
		
		List<FileItem> fileList = null;
		try{
			fileList = sfu.parseRequest(request);
		}catch(FileUploadException e){
			if( e instanceof SizeLimitExceededException){
				log.error("out of max size size limited");
			}
			log.error(e);
			return ;
		}
		
		if(fileList == null || fileList.size() ==0 ){
			log.error("no file selected");
		}
		
		log.debug("fileList size  = "+fileList.size());
		
		Iterator<FileItem> fileItr = fileList.iterator();
		FileItem fileItem = null;
		/*set the form field parameter*/
		ParaSet pSet = this.getParaSet(fileItr);
		User user = null ;
		
		if (pSet.getUserId() == null || pSet.getUserId().equals("")
				|| pSet.getToken() == null || pSet.getToken().equals("")) {
			log.debug("null parameter!");
			log.debug(pSet.toString());
			return;
		}
		
		if (pSet.getFolderId() == null || pSet.getFolderId().equals("")) {
			pSet.setFolderId("root");
		}
		
		fileItr = fileList.iterator();
		while(fileItr.hasNext()){
			fileItem = (FileItem)fileItr.next();
			log.debug("fileItem.getName() = " + fileItem.getName());
			
			if( fileItem == null || fileItem.isFormField() ){	
				log.debug("fileItr continue!");
				continue;
			}
			
			String path = fileItem.getName();
			log.debug("path = "+path);
			
			/*set size*/
			pSet.setSize((int)fileItem.getSize());
			log.debug("size is "+pSet.getSize());
			
			if(path.equals("")||pSet.getSize()==0){
				log.error("no file selected");
				return ;
			}
			
			String fileName = null;
			fileName = path.substring(path.lastIndexOf("/") + 1);
			fileName = fileName.trim();
			fileName = fileName.replaceAll("[\\s]", "_"); //change space to "_"
			log.debug("fileName = "+fileName);
			
			/*set filename*/
			pSet.setFileName(fileName);
			
			/*set id*/
			pSet.setId(HashUtil.strHash(HashUtil.getRandomId()));
			
			/*set file type*/
			String type = null;
			type = fileName.substring(fileName.lastIndexOf(".") + 1);
			log.debug("file type = "+ type );
			if(type.equals(fileName)||type == fileName){
				pSet.setFileType(com.echoii.cloud.platform.model.File.TYPE_BINARY);
			}else{
				type = type.toLowerCase();
				pSet.setFileType(type);
			}
			
			log.debug(pSet.toString());
			
			this.fileUpload(fileItem, out, pSet, user);
			
		}//while
		
	}//post
	
	private ParaSet getParaSet(Iterator<FileItem> fileItr){
		ParaSet pSet = new ParaSet();
		while (fileItr.hasNext()) {
			
			FileItem fileItem = (FileItem) fileItr.next();
			if (fileItem.isFormField()) {
				String fieldName ,fieldValue;
				fieldName = fileItem.getFieldName();//get the name of the form field
				fieldValue = fileItem.getString();//get the value of the form field
				
				log.debug("fieldName = "+fieldName);
				//if(fieldName == "user_id"){ does not work
				if(fieldName.equals("user_id")){
					log.debug("fieldValue user_id = "+fieldValue);
					/*set user id*/
					pSet.setUserId(fieldValue);
				}else if(fieldName.equals("folder_id")){
					log.debug("fieldValue folder_id = "+fieldValue);
					/*set parent id*/
					pSet.setFolderId(fieldValue);
				}else if(fieldName.equals("token")){
					log.debug("fieldValue token = "+fieldValue);
					/*set token*/
					pSet.setToken(fieldValue);
				}else{
					log.debug("unknown para = "+fieldValue);
				}
			}//if
		} // while		
		return pSet;
	}
	
	private void fileUpload(FileItem fileItem, PrintWriter out,ParaSet pSet, User user) {
		log.debug("file upload");
		long time = System.currentTimeMillis();
		
		com.echoii.cloud.platform.model.File file = new com.echoii.cloud.platform.model.File();
		
		file.setName(pSet.getFileName());
		file.setSize(pSet.getSize());
		file.setType(pSet.getFileType());
		file.setVersion(0);
		file.setUserId(pSet.getUserId());
		file.setFolderId(pSet.getFolderId());
		file.setStatus(com.echoii.cloud.platform.model.File.STATUS_NORMAL);
		file.setCreateDate(new Date());
		file.setMetaFolder(DateUtil.parseDateToString(
				file.getCreateDate(), 
				DateUtil.PATTERN_yyyyMM));
		file.setLmf_date(new Date());
		file.setId(pSet.getId());	
		
		if(pSet.getFolderId().equals("root") || pSet.getFolderId() == "root"){
			file.setPath( "/root/"+pSet.getFileName() );
		}else {
			if(fileService.getFileById(file.getFolderId())==null){
				log.error("file service find by id (parentId) failed");
				return ;
			}else{
				file.setPath( fileService.getFileById(pSet.getFolderId()).getPath() + "/" +pSet.getFileName() );
			}
			
		}
			
		int i = 1;
		String filename = file.getName();
		//no filename extension
		if( file.getType().equals("binary") || file.getType() == "binary" ){
			int filenamelength = filename.length();
			
			while(fileService.getFileByName(file)!=null){
				log.debug("duplicated file name : "+file.getName());
				file.setName(filename.substring(0,filenamelength)+"_"+i);
				i++;
			}
			file.setPath(file.getPath().substring(0, file.getPath().lastIndexOf('/')+1) + file.getName() );
		}else{//with filename extension			
			int filenamelength = filename.substring(0, filename.lastIndexOf(".")).length();
			
			while(fileService.getFileByName(file)!=null){
				log.debug("duplicated file name : "+file.getName());
				file.setName(filename.substring(0,filenamelength)+"_"+i+"."+file.getType());
				i++;
			}
			file.setPath(file.getPath().substring(0, file.getPath().lastIndexOf('/')+1) + file.getName() );
		}
		
		if(pSet.getFileType() != null){
			if(pSet.getFileType().equals("flv")){
				file.setMetaId( HashUtil.getRandomId()+".flv");
			}else if(pSet.getFileType().equals("mp4")){
				file.setMetaId( HashUtil.getRandomId()+".mp4");
			}else{
				file.setMetaId( HashUtil.getRandomId());
			}
		}else{
			file.setMetaId( HashUtil.getRandomId());
		}
		
		
		
		//check file system
		String uploadfolderpath = config.getStringValue("upload.directory","/mnt/moosefs/echoii/data/")+file.getMetaFolder()+"/";
		log.debug("upload directory = "+uploadfolderpath);
		
		try{
			File uploadfolder = new File( uploadfolderpath );
			if(!uploadfolder.exists()||!uploadfolder.isDirectory()){
				log.debug("folder = "+uploadfolderpath+" does not exist , then create it!");
				boolean flag = uploadfolder.mkdirs();
				if(flag){
					log.debug("create upload folder success");
				}else{
					log.debug("create upload folder fail");
				}
			}
			if(!uploadfolder.canWrite()){
				log.error("file system error, path:" + config.getStringValue("upload.directory","/mnt/moosefs/echoii/data/") );
			}
		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		
		java.io.File uploadFile;
		
		String uploadfilename = uploadfolderpath + file.getMetaId();
		log.debug("upload file name = "+uploadfilename);
		
		try {
			log.debug("begin to write file, time used: "+ (System.currentTimeMillis() - time));
			uploadFile = new File(uploadfilename);
			fileItem.write(uploadFile);
			
			log.debug("save to db, time used: "+ (System.currentTimeMillis() - time));
			fileService.createFile(file);
			
			usermanager.updateStatistics(file.getUserId());

			
			uploadFile = null;
			
			log.debug("upload finish! time used: "+ (System.currentTimeMillis() - time));

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
	
	
}
