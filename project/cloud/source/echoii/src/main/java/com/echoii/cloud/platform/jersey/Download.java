package com.echoii.cloud.platform.jersey;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.echoii.cloud.platform.manager.DownloadManager;
import com.echoii.cloud.platform.manager.ShareManager;
import com.echoii.cloud.platform.model.Hdc;
import com.echoii.cloud.platform.model.HdcFile;
import com.echoii.cloud.platform.model.ShareLinkFile;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.HdcService;
import com.echoii.cloud.platform.service.ShareService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.service.impl.HdcServiceImpl;
import com.echoii.cloud.platform.service.impl.ShareServiceImpl;
import com.echoii.cloud.platform.util.Config;

@Path("download")
public class Download extends JerseyBase {
	private static Logger log = Logger.getLogger(Download.class);
	private FileService fileService = FileServiceImpl.getInstance();
	private ShareService shareService = ShareServiceImpl.getInstance();
	private HdcService hdcService = HdcServiceImpl.getInstance();
	ShareManager sharemanager = ShareManager.getInstance();
	DownloadManager downloadmanager = DownloadManager.getInstance();
	private static Config config = Config.getInstance();
	

	@GET
	@Path("mobile")
	@Produces(MediaType.TEXT_PLAIN)
	public void downloadFileForMobile(
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("file_id") @DefaultValue("no-fileid") String fileId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException, ServletException {	
		
		com.echoii.cloud.platform.model.File fl = fileService.getFileById(fileId);

		if (fl == null || !fl.getUserId().equals(userId)) {
			log.debug("[download]:no file!");
			return;
		}

		File file = new File(config.getStringValue("upload.directory","/mnt/moosefs/echoii/data")
				+ "/"+ fl.getMetaFolder()
				+ "/" + fl.getMetaId());

		if (!file.exists()) {
			log.debug("[download]:file not exit!");
			return;
		}

		downloadmanager.download(file, fl.getName(), response, request);
	}
	
	@GET
	@Path("torrent")
	@Produces(MediaType.TEXT_PLAIN)
	public void downloadFileForMobile(
			@QueryParam("file_id") @DefaultValue("no-fileid") String fileId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException, ServletException {	
		

		com.echoii.cloud.platform.model.File fl = fileService.getFileById(fileId);

		if (fl == null ) {
			log.debug("[download]:no file!");
			return;
		}

		File file = new File(config.getStringValue("upload.directory","/mnt/moosefs/echoii/data")
				+ "/"+ fl.getMetaFolder()
				+ "/" + fl.getMetaId());

		if (!file.exists()) {
			log.debug("[download]:file not exit!");
			return;
		}

		downloadmanager.download(file, fl.getName(), response, request);
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public void downloadFile(
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-fileid") String fileId,
			@QueryParam("callback") @DefaultValue("") String callBack,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException, ServletException {	
		
		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			log.debug("[download]:validation failure!");
			response.setStatus(404);
			response.sendError(404);
			return;
		}

		com.echoii.cloud.platform.model.File fl = fileService.getFileById(fileId);

		if (fl == null) {
			log.debug("[download]:no file!");
			downloadmanager.errorCallBack(callBack,response);
			return;
		}

		log.debug("[download]:download begin,file name is "+fl.getName());
		try {
			File file = new File(config.getStringValue("upload.directory","/mnt/moosefs/echoii/data")
					+ "/"+ fl.getMetaFolder()
					+ "/" + fl.getMetaId());

			if (!file.exists()) {
				log.debug("[download]:file not exit!");
				downloadmanager.errorCallBack(callBack,response);
				return;
			}
			
			downloadmanager.download(file, fl.getName(), response, request);

		} catch (IOException e) {
			// e.printStackTrace();
		}
	}
	
	@GET
	@Path("package_download")
	@Produces(MediaType.TEXT_PLAIN)
	public void downloadFiles(
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id_list") @DefaultValue("no-fileid_list") String fileIds,
			@QueryParam("callback") @DefaultValue("") String callBack,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException, InterruptedException, ServletException {	
		
		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			log.debug("[download]:validation failure!");
			response.setStatus(404);
			response.sendError(404);
			return;
		}

		String[] ids = fileIds.split(",");
		log.debug("length:" + ids.length);

		String path = sharemanager.packageDownload(userId, ids);

		if (path == null) {
			log.debug("[package_download]:no file!");
			downloadmanager.errorCallBack(callBack,response);
			return;
		}

		try {
			// read the file
			File file = new File(path);

			if (!file.exists()) {
				downloadmanager.errorCallBack(callBack,response);
				return;
			}
			
			String filename = java.net.URLDecoder.decode(path.substring(path.lastIndexOf("/") + 1), "utf-8");
			
			downloadmanager.download(file, filename, response, request);

			if (path.contains("mnt")) {
				return;
			} else {
				Runtime.getRuntime().exec("rm " + path).waitFor();
			}

		} catch (IOException e) {
			// e.printStackTrace();
		}

	}
	
	@GET
	@Path("public")
	@Produces(MediaType.TEXT_PLAIN)
	public void downloadPublic(
			@QueryParam("file_id") @DefaultValue("no-fileid") String fileId,
			@QueryParam("idcode") @DefaultValue("no-idcode")String idcode,
			@QueryParam("callback") @DefaultValue("") String callBack,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException, ServletException {			
			
			ShareLinkFile fl = shareService.getShareFileById(fileId);

		if (fl == null) {
			log.debug("no file!");
			downloadmanager.errorCallBack(callBack,response);
			return;
		}

		if (!idcode.equals("no-idcode")) {
			if (!idcode.equals(fl.getValidCode())) {
				downloadmanager.errorCallBack(callBack,response);
				response.sendError(404);
				response.setStatus(404);
				return;
			}
		}

		try {
			// read the file
			File file = new File(config.getStringValue("upload.directory","/mnt/moosefs/echoii/data")
					+ "/"+ fl.getMetaFolder()
					+ "/" + fl.getMetaId());
			if (!file.exists()) {
				log.debug("[public_download]:no file!!");
				downloadmanager.errorCallBack(callBack,response);
				return;
			}
			String filename = java.net.URLDecoder.decode(fl.getName(), "utf-8");
			
			downloadmanager.download(file, filename, response, request);
			

		} catch (IOException e) {
			// e.printStackTrace();
		}

	}
	
	@GET
	@Path("hdc")
	@Produces(MediaType.TEXT_PLAIN)
	public void downloadHdcFile(
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId,
			@QueryParam("device_token")@DefaultValue("no-token") String deviceToken,
			@QueryParam("file_id")@DefaultValue("no-file") String fileId,
			@QueryParam("callback") @DefaultValue("") String callBack,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response) throws IOException, ServletException {	
		
		Hdc hdc = hdcService.getHdc(deviceId, deviceToken);
		
		if(hdc == null){
			log.debug("no hdc!");
			downloadmanager.errorCallBack(callBack,response);
			return;
		}
		
		HdcFile hdcFile = hdcService.getHdcFile(fileId);
			

		try {
			// read the file
			File file = new File(config.getStringValue("upload.directory",
					"/mnt/moosefs/echoii/data")
					+ "/"+ hdcFile.getMetaFolder()
					+ "/" + hdcFile.getMetaId());
			if (!file.exists()) {
				log.debug("[public_download]:no file!!");
				downloadmanager.errorCallBack(callBack,response);
				return;
			}
			String filename = java.net.URLDecoder.decode(hdcFile.getName(), "utf-8");
			
			downloadmanager.download(file, filename, response, request);
			

		} catch (IOException e) {
			// e.printStackTrace();
		}

	}
	
}
