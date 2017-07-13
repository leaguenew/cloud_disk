package com.echoii.cloud.platform.jersey;

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
import com.echoii.cloud.platform.entity.factory.GroupEntityFactory;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.GroupManager;
import com.echoii.cloud.platform.model.GroupDetail;
import com.echoii.cloud.platform.model.GroupFile;
import com.echoii.cloud.platform.model.GroupMember;
import com.echoii.cloud.platform.util.DataUtil;
import com.echoii.cloud.platform.util.Status;

@Path("group")
public class Group extends JerseyBase {

	static Logger log = Logger.getLogger(Group.class);
	GroupManager groupManager = GroupManager.getInstance();

	@GET
	@Path("create")
	@Produces(MediaType.APPLICATION_JSON)
	public String create(
			@QueryParam("name") @DefaultValue("no-name") String name,
			@QueryParam("desp") @DefaultValue("no-desp") String desp,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		com.echoii.cloud.platform.model.Group create = null;
		log.debug("[Group_create]: create a group begin!");

		if (name.equals("no-name") || userId.equals("no-userid")
				|| token.equals("no-token")) {
			log.debug("[Group_create]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		create = this.groupManager.create(userId, name, desp);
		if (create != null) {
			log.debug("[Group_create]: create ok!");
			return JSONHelper.getDefaultResponse(Status.OK, GroupEntityFactory.getGroupEntity(create)).toString();
		}
		log.debug("[Group_create]: name dup!");
		return JSONHelper.getDefaultResponse(Status.NAME_DUP, null).toString();

	}

	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("mode") @DefaultValue("create") String mode,
			@QueryParam("order") @DefaultValue("name") String order,
			@QueryParam("begin") @DefaultValue("0") int begin,
			@QueryParam("size") @DefaultValue("20") int size,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		List<com.echoii.cloud.platform.model.Group> list = null;
		log.debug("[Group_list]: list groups begin!");
		if (userId.equals("no-userid") || token.equals("no-token")
				|| !DataUtil.getGroupListMode().contains(mode)
				|| !DataUtil.getGroupListOrder().contains(order)) {
			log.debug("[Group_list]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		list = this.groupManager.list(userId, mode, order, begin, size);
		
		if(list == null){
			return JSONHelper.getDefaultResponse(Status.OTHER_ERROR, null).toString();
		}

		
		return JSONHelper.getDefaultResponse(Status.OK, GroupEntityFactory.listGroupEntity(list)).toString();
		

	}

	@GET
	@Path("info")
	@Produces(MediaType.APPLICATION_JSON)
	public String info(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("return_detail") @DefaultValue("false") String returnInfo,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {
		if (userId.equals("no-userid") || token.equals("no-token")
				|| groupId.equals("no-id")) {
			log.debug("[Group_info]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		com.echoii.cloud.platform.model.Group g = this.groupManager.info(groupId);
		if(g == null){
			return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
		}
		
		if (returnInfo.equals("true")) {
			GroupDetail gt = this.groupManager.infoDetail(groupId);

			if (gt == null) {
				log.debug("[Group_info]: no group!");
				return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
			}

			log.debug("[Group_info]: return groupdetail!");
			return JSONHelper.getDefaultResponse(Status.OK,GroupEntityFactory.getGroupDetailEntity(g,gt)).toString();
	

		} else if (returnInfo.equals("false")) {
			
			return JSONHelper.getDefaultResponse(Status.OK,GroupEntityFactory.getGroupEntity(g)).toString();

		} else{
			return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
		}
			

	}

	@GET
	@Path("join")
	@Produces(MediaType.APPLICATION_JSON)
	public String join(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		log.debug("[Group_join]:join begin");

		if (userId.equals("no-userid") || token.equals("no-token")
				|| groupId.equals("no-id")) {
			log.debug("[Group_join]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("[Group_join]:verification success !");

		if (!this.groupManager.join(groupId, userId)) {
			log.debug("[Group_join]: join failure. check the groupid!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, "false").toString();

		}
		log.debug("[Group_join]: join success!");
		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();

	}

	@GET
	@Path("quit")
	@Produces(MediaType.APPLICATION_JSON)
	public String quit(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		log.debug("[Group_quit]:quit begin");

		if (userId.equals("no-userid") || token.equals("no-token")
				|| groupId.equals("no-id")) {
			log.debug("[Group_quit]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("[Group_quit]:verification success !");

		if (!this.groupManager.quit(groupId, userId)) {
			log.debug("[Group_quit]: quit failure. check the groupid!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, "false").toString();

		}
		log.debug("[Group_quit]: quit success!");
		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();

	}

	@GET
	@Path("list_member")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_member(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("begin") @DefaultValue("0") int begin,
			@QueryParam("size") @DefaultValue("20") int size,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		log.debug("[Group_list_member]:list_member begin");

		if (userId.equals("no-userid") || token.equals("no-token")
				|| groupId.equals("no-id")) {
			log.debug("[Group_list_member]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("[Group_list_member]:verification success !");

		List<GroupMember> members = this.groupManager.listMembers(groupId, begin, size);

		if (members == null) {
			log.debug("[Group_list_member]: list_member failure. check the groupid!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();

		}
		log.debug("[Group_list_member]: list_member success!");
		return JSONHelper.getDefaultResponse(Status.OK, members).toString();

	}

	@GET
	@Path("file/copy")
	@Produces(MediaType.APPLICATION_JSON)
	public String copy(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("group_file_id_list") @DefaultValue("no-group-id-list") String groupFileIdList,
			@QueryParam("folder_id") @DefaultValue("root") String folderId,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		log.debug("[Group_file/copy]:file/copy begin");

		if (userId.equals("no-userid") || token.equals("no-token")
				|| groupId.equals("no-id") || groupFileIdList.equals("no-group-id-list")) {
			log.debug("[Group_file/copy]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		String[] list = groupFileIdList.split(",");
		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("[Group_file/copy]:verification success !");

		if (!this.groupManager.copy(groupId, list, userId, folderId)) {
			log.debug("[Group_file/copy]:copy error !");
			return JSONHelper.getDefaultResponse(Status.SYS_ERROR, "false").toString();

		}
		log.debug("[Group_file/copy]:copy success !");
		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();

	}


	@GET
	@Path("file/list")
	@Produces(MediaType.APPLICATION_JSON)
	public String listfile(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("mode") @DefaultValue("no-mode") String mode,
			@QueryParam("begin") @DefaultValue("0") int begin,
			@QueryParam("size") @DefaultValue("20") int size,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		log.debug("[Group_file/list]:file/list begin");

		if (userId.equals("no-userid") || token.equals("no-token")
				|| groupId.equals("no-id") || mode.equals("no-mode")
				|| !DataUtil.getGroupFilefMode().contains(mode)) {
			log.debug("[Group_file/list]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("[Group_file/list]:verification success !");
		List<GroupFile> files = this.groupManager.listFiles(groupId, userId, mode, begin, size);
		if (files.isEmpty()) {
			log.debug("[Group_file/list]:system error !");
			return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();

		}
		log.debug("[Group_file/list]:list files success !");
		return JSONHelper.getDefaultResponse(Status.OK, GroupEntityFactory.listgroupFileEntity(files)).toString();

	}

	@GET
	@Path("file/delete")
	@Produces(MediaType.APPLICATION_JSON)
	public String deletefile(
			@QueryParam("group_id") @DefaultValue("no-id") String groupId,
			@QueryParam("group_file_id") @DefaultValue("no-file-id") String groupFileId,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {

		log.debug("[Group_file/delete]:file/delete begin");

		if (userId.equals("no-userid") || token.equals("no-token")) {
			log.debug("[Group_file/delete]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("[Group_file/delete]:verification success !");
		if (!this.groupManager.deleteGroupFile(groupId, userId, groupFileId)) {
			log.debug("[Group_file/delete]:delete success!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, "false").toString();

		}
		log.debug("[Group_file/delete]:delete success!");
		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();

	}

}
