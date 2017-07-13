package com.echoii.cloud.platform.jersey;

import java.util.List;

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

import com.echoii.cloud.platform.entity.OfflineFileEntity;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.OfflineDownloadManager;
import com.echoii.cloud.platform.util.Status;

@Path("offlinedownload")
public class OfflineDownload extends JerseyBase {
	
	private static Logger log = Logger.getLogger(OfflineDownload.class);
	OfflineDownloadManager offlinedownloadmanager = OfflineDownloadManager.getInstance();
	
	@GET
	@Path("task/add")
	@Produces(MediaType.APPLICATION_JSON)
	public String downloadFileForMobile (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {	
		if (userId.equals("no-userid") || fileId.equals("no-file_id") || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

//		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
//			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
//		}
		
		if(!offlinedownloadmanager.addTask(userId, fileId)){
			return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();
		}

		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
		
	}
	
	//get the task info
	@GET
	@Path("task/info")
	@Produces(MediaType.APPLICATION_XML)
	public String taskInfo (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("task_id") @DefaultValue("no-id") String taskId,
			@QueryParam("return_info") @DefaultValue("no") String renturnInfo,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {	
		
		if (userId.equals("no-userid") || taskId.equals("no-id") || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}
		
		if(renturnInfo.equals("yes")){
			return null;
		}else{
			OfflineFileEntity offentity = offlinedownloadmanager.info(userId, taskId);	
			if(offentity == null){
				return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();
			}
			
			return JSONHelper.getDefaultResponse(Status.OK, offentity).toString();
		}
		
		
	}

	@GET
	@Path("task/stop")
	@Produces(MediaType.APPLICATION_XML)
	public String stop (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("task_id") @DefaultValue("no-id") String taskId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {	
		
		if (userId.equals("no-userid") || taskId.equals("no-id") || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}
		
		if(offlinedownloadmanager.stop(userId, taskId)){
			return JSONHelper.getDefaultResponse(Status.OK, null).toString();
		}

		return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();
		
	}
	
	@GET
	@Path("task/start")
	@Produces(MediaType.APPLICATION_XML)
	public String start (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("task_id") @DefaultValue("no-id") String taskId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {
		if (userId.equals("no-userid") || taskId.equals("no-id") || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}
		
		if(offlinedownloadmanager.start(userId, taskId)){
			return JSONHelper.getDefaultResponse(Status.OK, null).toString();
		}
		return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();	
		
	}
	
	@GET
	@Path("task/delete")
	@Produces(MediaType.APPLICATION_XML)
	public String delete (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("task_id") @DefaultValue("no-id") String taskId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {	
		if (userId.equals("no-userid") || taskId.equals("no-id") || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}
		
		if(offlinedownloadmanager.remove(userId, taskId)){
			return JSONHelper.getDefaultResponse(Status.OK, null).toString();
		}
		return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();
		
	}
	
	//get all my task info
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_XML)
	public String all (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {	
		if (userId.equals("no-userid")  || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		log.debug("list begin");

//		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
//			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
//		}
		
		List<OfflineFileEntity> files = offlinedownloadmanager.list(userId);
		
		return JSONHelper.getDefaultResponse(Status.OK, files).toString();
		

		
	}
	
	@GET
	@Path("empty")
	@Produces(MediaType.APPLICATION_XML)
	public String empty (
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)  {	
		if (userId.equals("no-userid")  || token.equals("no-token")) {
			log.debug("[OfflineDownload_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

//		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
//			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
//		}
		
		if(!offlinedownloadmanager.empty(userId)){
			return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();
		}
		
		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
		
	}

}
