package com.echoii.cloud.platform.jersey;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.FileEntity;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.FileManager;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.Status;

@Path("file")
public class File extends JerseyBase {
	static Logger log = Logger.getLogger(File.class);
	FileManager filemanager = FileManager.getInstance();

	@GET
	@Path("create_folder")
	@Produces(MediaType.APPLICATION_JSON)
	public String createFolder(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("folder_id") @DefaultValue("root") String folderId,
			@QueryParam("name") @DefaultValue("no-name") String name,
			@Context HttpServletRequest request) {
		log.debug("create folder begin");

		JSONObject result;

		FileEntity file = null;

		if (userId.equals("no-user_id") || token.equals("no-token")
				|| name.equals("no-name")) {
			log.debug("[create folder]: para error : userId = " + userId
					+ " token = " + token + " name = " + name + " folderId = "
					+ folderId);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			file = this.filemanager.createFolder(userId, folderId, name);
			if(file==null){
				result = JSONHelper.getDefaultResponse(Status.NAME_DUP, null);
				log.debug("[create folder]: name duplicated! : userId = " + userId
						+ " token = " + token + " name = " + name + " folderId = "
						+ folderId);
				return result.toString();
			}else{
				result = JSONHelper.getDefaultResponse(Status.OK, file);
				log.debug("[create folder]: success! : userId = " + userId
						+ " token = " + token + " name = " + name + " folderId = "
						+ folderId);
				return result.toString();
			}
			
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[create folder]: fail! : userId = " + userId
					+ " token = " + token + " name = " + name + " folderId = "
					+ folderId);
			return result.toString();
		}
	}

	@GET
	@Path("del")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@QueryParam("remove_source") @DefaultValue("false") String removeSource,
			@Context HttpServletRequest request) {
		log.debug("delete file begin");

		JSONObject result;
		FileEntity file = null;
		boolean flag;
		
		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileId.equals("no-file_id")) {
			log.debug("[delete]: para error : userId = " + userId + " token = "
					+ token + " fileId = " + fileId);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		//if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			flag = this.filemanager.delete(userId, fileId,removeSource);
			if(flag){
				result = JSONHelper.getDefaultResponse(Status.OK, file);
				log.debug("[delete]: success : userId = " + userId + " token = "
						+ token + " fileId = " + fileId);
				return result.toString();
			}else{
				result = JSONHelper.getDefaultResponse(Status.SYS_ERROR, file);
				log.debug("[delete]: fail : userId = " + userId + " token = "
						+ token + " fileId = " + fileId);
				return result.toString();
			}
//		} else {
//			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
//			log.debug("[delete]: fail : userId = " + userId + " token = "
//					+ token + " fileId = " + fileId);
//			return result.toString();
//		}

	}

	@GET
	@Path("del_list")
	@Produces(MediaType.APPLICATION_JSON)
	public String delete_list(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id_list") @DefaultValue("no-file_id_list") String fileIdList,
			@QueryParam("remove_source") @DefaultValue("false") String removeSource,
			@Context HttpServletRequest request) {
		log.debug("delete file begin");

		JSONObject result;
		FileEntity file = null;
		boolean flag ;
		
		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileIdList.equals("no-file_id_list")) {
			log.debug("[delete]: para error : userId = " + userId + " token = "
					+ token + " fileIdList = " + fileIdList);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			flag = this.filemanager.delete(userId, fileIdList,removeSource);
			if(flag){
				result = JSONHelper.getDefaultResponse(Status.OK, file);
				log.debug("[delete]: success : userId = " + userId + " token = "
						+ token + " fileIdList = " + fileIdList);
				return result.toString();
			}else{
				result = JSONHelper.getDefaultResponse(Status.SYS_ERROR, file);
				log.debug("[delete]: fail : userId = " + userId + " token = "
						+ token + " fileIdList = " + fileIdList);
				return result.toString();
			}
			
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[delete]: fail : userId = " + userId + " token = "
					+ token + " fileIdList = " + fileIdList);
			return result.toString();
		}

	}
	
	
	@GET
	@Path("recover")
	@Produces(MediaType.APPLICATION_JSON)
	public String recover(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id_list") @DefaultValue("no-file_id_list") String fileIdList,
			@Context HttpServletRequest request
			){
		log.debug("recover begin");
		
		JSONObject result;
		FileEntity file = null;
		boolean flag ;
		
		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileIdList.equals("no-file_id_list")) {
			log.debug("[recover]: para error : userId = " + userId + " token = "
					+ token + " fileIdList = " + fileIdList);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			flag = this.filemanager.recover(userId, fileIdList);
			if(flag){
				result = JSONHelper.getDefaultResponse(Status.OK, file);
				log.debug("[recover]: success : userId = " + userId + " token = "
						+ token + " fileIdList = " + fileIdList);
				return result.toString();
			}else{
				result = JSONHelper.getDefaultResponse(Status.SYS_ERROR, file);
				log.debug("[recover]: fail : userId = " + userId + " token = "
						+ token + " fileIdList = " + fileIdList);
				return result.toString();
			}
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[delete]: fail : userId = " + userId + " token = "
					+ token + " fileIdList = " + fileIdList);
			return result.toString();
		}
		
	}
	
	
	
	
	
	@GET
	@Path("rename")
	@Produces(MediaType.APPLICATION_JSON)
	public String rename(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@QueryParam("new_name") @DefaultValue("no-new_name") String newName,
			@Context HttpServletRequest request) {
		log.debug("rename file begin");

		JSONObject result;
		FileEntity file = null;
		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileId.equals("no-file_id") || newName.equals("no-new_name")) {
			log.debug("[rename]: para error : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " newName = " + newName);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			file = this.filemanager.rename(userId, fileId, newName);
			if(file==null){
				log.debug("[rename]: fail : userId = " + userId + " token = "
						+ token + " fileId = " + fileId + " newName = " + newName);
				result = JSONHelper.getDefaultResponse(Status.RENAME_ERROR, null);
				return result.toString();
			}else{
				result = JSONHelper.getDefaultResponse(Status.OK, file);
				log.debug("[rename]: success : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " newName = " + newName);
				return result.toString();
			}//if
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[rename]: fail : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " newName = " + newName);
			return result.toString();
		}//if
	}

	@GET
	@Path("move")
	@Produces(MediaType.APPLICATION_JSON)
	public String move(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@QueryParam("folder_id") @DefaultValue("no-folder_id") String folderId,
			@Context HttpServletRequest request) {
		log.debug("move file begin");

		// verify that whether the userId and the token is valid
		JSONObject result;
		FileEntity file = null;
		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileId.equals("no-file_id")
				|| folderId.equals("no-folder_id")) {
			log.debug("[move]: para error : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " folderId = " + folderId);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			file = this.filemanager.move(userId, fileId, folderId);
			result = JSONHelper.getDefaultResponse(Status.OK, file);
			log.debug("[move]: success : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " folderId = " + folderId);
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[move]: fail : userId = " + userId + " token = " + token
					+ " fileId = " + fileId + " folderId = " + folderId);
			return result.toString();
		}
	}

	@GET
	@Path("copy")
	@Produces(MediaType.APPLICATION_JSON)
	public String copy(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("file_id") @DefaultValue("no-file_id") String fileId,
			@QueryParam("folder_id") @DefaultValue("no-folder_id") String folderId,
			@Context HttpServletRequest request) {
		log.debug("copy file begin");

		JSONObject result;

		if (userId.equals("no-user_id") || token.equals("no-token")
				|| fileId.equals("no-file_id")
				|| folderId.equals("no-folder_id")) {
			log.debug("[copy]: para error : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " folderId = " + folderId);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			this.filemanager.copy(userId, fileId, folderId);
			result = JSONHelper.getDefaultResponse(Status.OK, null);
			log.debug("[copy]: success : userId = " + userId + " token = "
					+ token + " fileId = " + fileId + " folderId = " + folderId);
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[copy]: fail : userId = " + userId + " token = " + token
					+ " fileId = " + fileId + " folderId = " + folderId);
			return result.toString();
		}

	}

	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public String list(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("folder_id") @DefaultValue("root") String folderId,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin") String begin, 
			@QueryParam("size") @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("file list begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if
	
		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			log.debug("[File list]: para error: userId = " + userId
					+ " token = " + token);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request)) == true) {
			fe = this.filemanager.listAll(userId, folderId, order, orderColumn,
					beginInt, sizeInt);

			result = JSONHelper.getDefaultResponse(Status.OK, fe);
			log.debug("[File list]: file list success!  userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[File list]: file list fail!  userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}

	
	
	@GET
	@Path("list_image")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_image(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin") String begin,
			@QueryParam("size")  @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("list image begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if

		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			log.debug("[image list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		// authorize
		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listImage(userId, order, orderColumn, beginInt,
					sizeInt);
			result = JSONHelper.getDefaultResponse(Status.OK, fe);
			log.debug("[image list]: image list success: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[image list]: image list fail : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}

	@GET
	@Path("list_doc")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_doc(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin") String begin,
			@QueryParam("size")  @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("list doc begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if
		

		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			log.debug("[Doc list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listDoc(userId, order, orderColumn, beginInt,
					sizeInt);

			result = JSONHelper.getDefaultResponse(Status.OK, fe);
			log.debug("[Doc list]: doc list success: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[Doc list]: doc list fail : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}

	}

	@GET
	@Path("list_video")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_video(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin")  String begin,
			@QueryParam("size")  @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("list video begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt < 0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if

		
		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			log.debug("[Video list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listVideo(userId, order, orderColumn, beginInt,
					sizeInt);

			result = JSONHelper.getDefaultResponse(Status.OK, fe);
			log.debug("[Video list]: video list success : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[Video list]: video list fail : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}

	@GET
	@Path("list_music")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_music(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin") String begin,
			@QueryParam("size")  @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("list music begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if

		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			log.debug("[Music list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listMusic(userId, order, orderColumn, beginInt,
					sizeInt);

			log.debug("[Music list]: music list success : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			result = JSONHelper.getDefaultResponse(Status.OK, fe);

			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[Music list]: music list fail : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}

	@GET
	@Path("list_torrent")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_torrent(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin")  @DefaultValue("no-begin") String begin,
			@QueryParam("size")  @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("list torrent begin");

		List<FileEntity> fe;
		JSONObject result;

		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if
		
		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			log.debug("[Torrent list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listTorrent(userId, order, orderColumn,
					beginInt, sizeInt);

			result = JSONHelper.getDefaultResponse(Status.OK, fe);
			log.debug("[Torrent list]: torrent list success : userId = "
					+ userId + " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[Torrent list]: torrent list fail : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}

	@GET
	@Path("list_others")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_others(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_by") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin") String begin,
			@QueryParam("size")  @DefaultValue("no-size") String size,
			@Context HttpServletRequest request) {
		log.debug("list others begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if

		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			log.debug("[Others list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}

		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listOthers(userId, order, orderColumn, beginInt,
					sizeInt);
			log.debug("[Others list]: Others list success : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			result = JSONHelper.getDefaultResponse(Status.OK, fe);

			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			log.debug("[Others list]: Others list fail : userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}

	@GET
	@Path("list_trash")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_trash(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request,
			@QueryParam("order") @DefaultValue("desc") String order,
			@QueryParam("order_column") @DefaultValue("lmf_date") String orderColumn,
			@QueryParam("begin") @DefaultValue("no-begin") String begin, 
			@QueryParam("size") @DefaultValue("no-size") String size) {
		log.debug("list trash begin");

		List<FileEntity> fe;
		JSONObject result;
		
		Config config = Config.getInstance();
		int beginInt,sizeInt;
		if(begin.equals("no-begin")||begin == "no-begin"){
			beginInt = 0;
		}else{
			beginInt = Integer.parseInt(begin);
			if(beginInt<0){
				beginInt = 0;
			}
		}//if
		if(size.equals("no-size")|| size == "no-size"){
			sizeInt = config.getIntValue("api.default.result.size", 40);
		}else{
			sizeInt = Integer.parseInt(size);
		}//if

		
		// parameter error
		if (userId.equals("no-user_id") || token.equals("no-token")) {
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			log.debug("[Trash list]: para error: userId = " + userId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
		
		
		if (isValidateUser(userId, token, this.getIpAddr(request))) {
			fe = this.filemanager.listTrash(userId, order, orderColumn, beginInt,
					sizeInt);

			System.out.println("fe size = " + fe.size());
			result = JSONHelper.getDefaultResponse(Status.OK, fe);

			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);

			return result.toString();
		}
	}
	
	@GET
	@Path("latest")
	@Produces(MediaType.APPLICATION_JSON)
	public String syn(
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("date") @DefaultValue("no-date") String date,
			@QueryParam("begin") @DefaultValue("0") String begin,
			@QueryParam("size") @DefaultValue("no-date") String size,
			@QueryParam("type") @DefaultValue("all") String type,
			@Context HttpServletRequest request) {
		log.debug("syn file begin");

		JSONObject result;
		List<FileEntity> file = null;
		if (userId.equals("no-user_id") || token.equals("no-token")
				|| date.equals("no-date")) {
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}
		
		//long time = Long.parseLong(date);
		long date1 = Long.parseLong(date);
		int begin1 = Integer.parseInt(begin);
		int size1 = Integer.parseInt(size);
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request)) == true) {
	//	if(true){
			file = this.filemanager.syn(userId, date1,begin1,size1,type);
			result = JSONHelper.getDefaultResponse(Status.OK, file);
			
			return result.toString();
		} else {
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			return result.toString();
		}

	}
	

}
