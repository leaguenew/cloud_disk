package com.echoii.cloud.platform.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.UserDetailEntity;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.UserManager;
import com.echoii.cloud.platform.util.Status;

@Path("user")
public class User extends JerseyBase{
	static Logger log = Logger.getLogger(User.class);
	UserManager userManager = UserManager.getInstance();
	
	@GET
	@Path("userdetail")
	@Produces(MediaType.APPLICATION_JSON)
	public String userdetail(
			@QueryParam("user_id") @DefaultValue("no-id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {
		log.debug("check_login begin.");
		UserDetailEntity ude = null;
		if (userId.equals("no-id") || token.equals("no-token")) {
			log.debug("para error.");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null)
					.toString();
		}

		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}

		log.debug("validation sucess.");
		ude = this.userManager.userdetail(userId);
		if (ude != null) {
			return JSONHelper.getDefaultResponse(Status.OK, ude).toString();
		}
		return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();

	}
	
	@GET
	@Path("update_userdetail")
	@Produces(MediaType.APPLICATION_JSON)
	public String update_user(
			@QueryParam("user_id") @DefaultValue("no-id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("name")  String name,
			@QueryParam("idcard")  String idCard,
			@QueryParam("nickname")  String nickName,
			@QueryParam("gender")  String gender,
			@QueryParam("birthday")  String birthday,
			@QueryParam("QQ")  String QQ,
			@QueryParam("tel")  String tel,
			@QueryParam("job")  String job,
			@QueryParam("introduction")  String introduction,
			@Context HttpServletRequest request) {
		log.debug("update_userdetail begin.");
		UserDetailEntity ude = null;
		if(userId.equals("no-id")||token.equals("no-token")){
			log.debug("para error.");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		if (!this.isValidateUser(userId, token, this.getIpAddr(request))) {
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null)
					.toString();
		}

		log.debug("validation sucess.");
		ude = this.userManager.update_userdetail(userId, idCard, name,
				nickName, gender, birthday, QQ, tel, job, introduction);
		if (ude != null) {
			return JSONHelper.getDefaultResponse(Status.OK, ude).toString();
		}
		return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
			
		
	}
	
	@GET
	@Path("update_password")
	@Produces(MediaType.APPLICATION_JSON)
	public String update_password(
			@QueryParam("user_id") @DefaultValue("no-id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("old_password") @DefaultValue("no-old_password") String oldPassword,
			@QueryParam("password") @DefaultValue("no-password") String password,
			@Context HttpServletRequest request) {
		log.debug("update_userdetail begin.");
		UserEntity ue = null;
		if(userId.equals("no-id")||token.equals("no-token")
		    ||oldPassword.equals("no-old_password")||password.equals("no-password")
		    ||oldPassword.equals("")||password.equals("")){
			log.debug("para error.");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
			log.debug("validation sucess.");
			ue = this.userManager.update_password(userId, oldPassword, password);
			if(ue != null){
				return JSONHelper.getDefaultResponse(Status.OK, ue).toString();
			}else{
				return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
			}
			
		} else {
			log.debug("[Auth_log_out]:validation sucess.");
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, "false").toString();
		}
	}

}
