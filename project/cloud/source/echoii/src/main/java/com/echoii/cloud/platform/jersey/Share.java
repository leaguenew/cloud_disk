package com.echoii.cloud.platform.jersey;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.echoii.cloud.platform.entity.ShareFileEntity;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.ShareManager;
import com.echoii.cloud.platform.util.Status;

@Path("share")
public class Share extends JerseyBase{
	static Logger log = Logger.getLogger(Share.class);
	ShareManager shareManager = ShareManager.getInstance();
	@GET
	@Path("share_files")
	@Produces(MediaType.APPLICATION_JSON)
	public String shareFiles(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id_list") @DefaultValue("no-fileid-list") String fileIdList,
			@QueryParam("idcode")  String idcode,
			@Context HttpServletRequest request) throws InterruptedException, IOException {

		log.debug("share begin");

		ShareFileEntity fe = null;

		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileIdList.equals("no-fileid-list")) {
			log.debug("[create folder]: para error : userId = " + userId
					+ " token = " + token);
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("sucess");
		String[] ids = fileIdList.split(",");

		fe = this.shareManager.share(userId, ids, idcode);

		if (fe != null) {
			return JSONHelper.getDefaultResponse(Status.OK, fe).toString();
		}
		return JSONHelper.getDefaultResponse(Status.FILE_ERROR, null).toString();
			
		
	}
	
	@GET
	@Path("file_info")
	@Produces(MediaType.APPLICATION_JSON)
	public String file_info(
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId){
		
		log.debug("get-share-file begin");

		ShareFileEntity fe = null;
		
		fe = shareManager.getShareFile(fileId);
		if(fe != null){
			return JSONHelper.getDefaultResponse(Status.OK, fe).toString();
		}
		return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
		
		
	}
	
	@GET
	@Path("cancel_share")
	@Produces(MediaType.APPLICATION_JSON)
	public String cancel(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@Context HttpServletRequest request){
		

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		shareManager.cancel_share(fileId);

		return JSONHelper.getDefaultResponse(Status.OK, null).toString();	
		
	}
	
	@GET
	@Path("update_idcode")
	@Produces(MediaType.APPLICATION_JSON)
	public String update_idcode(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@QueryParam("idcode") @DefaultValue("no-file_id") String idcode,
			@Context HttpServletRequest request){
		
		log.debug("update_idcode begin");
		ShareFileEntity fe = null;
		if(!this.isValidateUser(userId, token, this.getIpAddr(request))){
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		fe = shareManager.update_idcode(fileId, idcode);
		if (fe == null) {
			return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
		}

		return JSONHelper.getDefaultResponse(Status.OK, fe).toString();
		
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public String file_list(
			@QueryParam("user_id") String userId,
			@QueryParam("token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("begin") @DefaultValue("no-begin") String begin, 
			@QueryParam("size") @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("file_list begin");

		List<ShareFileEntity> sf = null;

		if(!this.isValidateUser(userId, token, this.getIpAddr(request))){
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		int begin1 = Integer.parseInt(begin);
		int size1 = Integer.parseInt(size);
		sf = shareManager.list_share_files(userId, order, begin1, size1);

		return JSONHelper.getDefaultResponse(Status.OK, sf).toString();
	}

	@GET
	@Path("share_group")
	@Produces(MediaType.APPLICATION_JSON)
	public String share_group(
			@QueryParam("group_id") @DefaultValue("no-id") String groupid,
			@QueryParam("file_id_list") @DefaultValue("no-list") String fileidlist,
			@QueryParam("group_folder_id")  String groupFolderId,
			@QueryParam("user_id") @DefaultValue("no-user-id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {
		log.debug("share_group begin");
		
		if(groupid.equals("no-id")||fileidlist.equals("no-list")
				||userId.equals("no-user-id")||token.equals("no-token")){
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		String[] list = fileidlist.split(",");
		if (shareManager.shareGroup(userId, groupid, list,groupFolderId)) {
			return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
		}
		return JSONHelper.getDefaultResponse(Status.FILE_ERROR, "false").toString();
	}

}
