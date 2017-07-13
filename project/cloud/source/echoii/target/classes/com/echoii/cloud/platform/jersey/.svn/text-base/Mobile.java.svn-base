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
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.AuthManager;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.Status;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.entity.UserLoginEntity;
@Path("mobile")
public class Mobile extends JerseyBase {
	
	static Logger log = Logger.getLogger(Mobile.class);
	AuthManager authManager = AuthManager.getInstance();

	@GET
	@Path("auth/login")
	@Produces(MediaType.APPLICATION_JSON)
	public String mlogin(
			@QueryParam("email") @DefaultValue("no-mail") String email,
			@QueryParam("password") @DefaultValue("no-password") String password,
			@Context HttpServletRequest request) {

		UserLoginEntity mlogin = null;

		if (password.equals("no-password") 
				|| email.equals("no-mail") || !CommUtil.isMail(email)) {
			log.debug("[Auth_login]:para error, no password or no username or no idcode");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		log.debug("[login]:login begin。");
		mlogin = this.authManager.mlogin(email, password, this.getIpAddr(request));
		
		if (mlogin == null) {
			log.debug("[Auth_login]:name or password error.");
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}
		
		//success
		log.debug("[Auth_login]:login success.,the token is '"
				+ mlogin.getToken() + "' and the userid is '"
				+ mlogin.getId() + "'.");
		
		return JSONHelper.getDefaultResponse(Status.OK, mlogin).toString();
	}
	
	@GET
	@Path("auth/reg")
	@Produces(MediaType.APPLICATION_JSON)
	public String mreg(
			@QueryParam("email") @DefaultValue("no-mail") String email,
			@QueryParam("password") @DefaultValue("no-password") String password,
			@Context HttpServletRequest request) {	
		
		UserEntity reg = null; // return value

		// para error
		if (email.equals("no-mail") || password.equals("no-password")|| !CommUtil.isMail(email)) {		
			log.debug("[Auth_register]: para error, check the name or password.");	
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		log.debug("[Auth_register]: register begin: email is '"+email+"', password is '"+ password+"'.");

		reg = this.authManager.mregister(email, password);
		
		//duplicate email
		if (reg == null) {
			log.debug("[Auth_register]: email has exit in the dataset");
			return JSONHelper.getDefaultResponse(Status.NAME_DUP, null).toString();
		}
		
		// success
		log.debug("[Auth_register]: register complete.");
		System.out.println(this.getIpAddr(request));
		return JSONHelper.getDefaultResponse(Status.OK, reg).toString();
	}

}
