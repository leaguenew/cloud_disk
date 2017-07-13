package com.echoii.cloud.platform.jersey;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DateUtil;
import com.echoii.cloud.platform.util.HashUtil;

@Path("services")
public class Upload extends JerseyBase {

	private  Logger log = Logger.getLogger(Upload.class);
	private  Config config = Config.getInstance();
	private  FileService fileService = FileServiceImpl.getInstance();
	
	//private int threadNumber = 0;

	@POST
	@Path("upload")
	@Consumes({ MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON })
	@Produces(MediaType.TEXT_PLAIN)
	public void upload(
			@QueryParam("user_id") @DefaultValue("no-userid") String userid,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("folder_id") @DefaultValue("no-folder_id") String folder_id,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException {
		
		if (this.isValidateUser(userid, token, this.getIpAddr(request))){
			log.debug("[upload]:begin validate");
			response.setCharacterEncoding("UTF-8");

			String uploadFolderPath = config.getStringValue("upload.directory",
					"/mnt/moosefs/echoii/data");
			
			//int number = config.getIntValue("upload.files.number", 5);

			try {
				log.debug("upload begin!");
				if (ServletFileUpload.isMultipartContent(request)) {
					DiskFileItemFactory factory = new DiskFileItemFactory();

					// cache size in memory, 1M
					factory.setSizeThreshold(config.getIntValue(
							"upload.size.threshold", 1024 * 1024));

					// when greater than the cache size, the folder which store the
					// data
					factory.setRepository(new File(config.getStringValue(
							"upload.temp.directory", "/data/echoii/tempdata")));

					ServletFileUpload sfu = new ServletFileUpload(factory);

					// single file size here is 1G
					sfu.setFileSizeMax(config.getLongValue("upload.max.size",
							1024 * 1024 * 1024));

					// the max size when there are many files, here is 10G
					sfu.setSizeMax(10 * config.getLongValue("upload.max.size",
							1024 * 1024 * 1024));
					sfu.setHeaderEncoding("UTF-8");

					FileItemIterator fii = sfu.getItemIterator(request);// pare
																		// request
					if (!new File(uploadFolderPath).isDirectory()) {
						new File(uploadFolderPath).mkdirs();
					}
					
					while (fii.hasNext()) {
						
						FileItemStream fis = fii.next();// get a file stream
						if (!fis.isFormField() && fis.getName().length() > 0) {// filter the file
							
							String name = fis.getName().substring(
									fis.getName().lastIndexOf("\\")+1);
							
							String type = fis.getName().substring(
									fis.getName().lastIndexOf(".") + 1);// get the type
							
							String metaId = HashUtil.getRandomId();
							
							String path = uploadFolderPath
									+ "//"
									+ DateUtil.parseDateToString(new Date(),
											DateUtil.PATTERN_yyyyMM) + "//"
									+ metaId;
							BufferedInputStream in = new BufferedInputStream(
									fis.openStream());
							BufferedOutputStream out = new BufferedOutputStream(
									new FileOutputStream(new File(path)));
							long size = Streams.copy(in, out, true);
							com.echoii.cloud.platform.model.File file = new com.echoii.cloud.platform.model.File();
							file.setCreateDate(new Date());
							file.setId(HashUtil.getRandomId());
							file.setMetaFolder(DateUtil.parseDateToString(
									file.getCreateDate(), 
									DateUtil.PATTERN_yyyyMM));
							file.setMetaId(metaId);
							if(folder_id.equals("root") ){
							//	file.setPath( "/root/"+file.getFileName() );
							}else {
								if(fileService.getFileById(file.getFolderId())==null){
									log.error("file service find by id (parentId) failed");
									return ;
								}else{
								//	file.setPath( fileService.findById(pSet.getFolderId()).getPath() + "/" +pSet.getFileName() );
								}
								
							}
							file.setPath(path);
							//file.setName(fis.getName());
							file.setName(name);
							file.setType(type);
							file.setUserId(userid);
							file.setSize(size);
							file.setMetaFolder(uploadFolderPath);
							fileService.createFile(file);

							// multithreading
//							if (threadNumber < number) {
//								Thread copy = new Thread(new UploadThread(in, out));
//							}

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String msg = "<html><body>"
					+ "上传成功"
					+ "<input type=\"button\" value=\"返回继续上传\" onclick=\"window.history.back(-1);\"></body></html>";

			response.getWriter().write(msg);
		}else{
			String msg = "<html><body>"
					+ "认真错误，请重新登录"
					+ "<input type=\"button\" value=\"返回继续上传\" onclick=\"window.history.back(-1);\"></body></html>";
			response.getWriter().write(msg);
			}
		
	}

//	private class UploadThread implements Runnable {
//
//		private BufferedInputStream in;
//		private BufferedOutputStream out;
//
//		public UploadThread(BufferedInputStream in, BufferedOutputStream out) {
//			this.in = in;
//			this.out = out;
//		}
//
//		@Override
//		public void run() {
//			threadNumber = threadNumber + 1;
//			try {
//				Streams.copy(in, out, true);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			threadNumber = threadNumber - 1;
//		}
//
//	}


}
