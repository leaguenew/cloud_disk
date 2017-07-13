package com.echoii.cloud.platform.jersey;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.AuthManager;
import com.echoii.cloud.platform.manager.EmailManager;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.Status;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.entity.UserLoginEntity;
@Path("auth")
public class Auth extends JerseyBase {
	
	static Logger log = Logger.getLogger(Auth.class);
	AuthManager authManager = AuthManager.getInstance();
	EmailManager emailManager = EmailManager.getInstance();

	@GET
	@Path("reg")
	@Produces(MediaType.APPLICATION_JSON)
	public String reg(
			@QueryParam("email") @DefaultValue("no-mail") String email,
			@QueryParam("password") @DefaultValue("no-password") String passwordhash,
			@Context HttpServletRequest request) {	
		
		UserEntity reg = null; // return value

		// para error
		if (email.equals("no-mail") || passwordhash.equals("no-password")|| !CommUtil.isMail(email)) {		
			log.debug("[Auth_register]: para error, check the name or password.");	
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		log.debug("[Auth_register]: register begin: email is '"+email+"', passwordhash is '"+ passwordhash+"'.");

		reg = this.authManager.register(email, passwordhash);
		
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

	@GET
	@Path("pre_login")
	@Produces(MediaType.APPLICATION_JSON)
	public String pre_login(
			@QueryParam("email") @DefaultValue("no-mail") String email,
			@Context HttpServletRequest request) {

	
		JSONObject pre_login = new JSONObject();
		
		// para error
		if (email.equals("no-mail")) {		
			log.debug("[Auth_pre_login]: para error,no email.");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		log.debug("[Auth_pre_login]:pre_login begin,the email is '"+email+"'.");
		pre_login.put("pre_login_token", this.authManager.pre_login(email));

		// success
		log.debug("[Auth_pre_login]:pre_login sucess, return the token '"
				+ pre_login.getString("pre_login_token") + "'.");
		return JSONHelper.getDefaultResponse(Status.OK, pre_login).toString();
	}

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String login(
			@QueryParam("email") @DefaultValue("no-mail") String email,
			@QueryParam("password") @DefaultValue("no-password") String passwordhash,
			@QueryParam("idcode") @DefaultValue("no-idcode") String preLoginId,
			@Context HttpServletRequest request) {

		UserLoginEntity login = null;

		if (passwordhash.equals("no-password") 
				|| email.equals("no-mail") || !CommUtil.isMail(email)
				|| preLoginId.equals("no-idcode")) {
			log.debug("[Auth_login]:para error, no password or no username or no idcode");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		
		log.debug("[login]:login begin。");
		login = this.authManager.login(email, passwordhash, preLoginId, this.getIpAddr(request));
		
		if (login == null) {
			log.debug("[Auth_login]:name or password error.");
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		}
		
		//success
		log.debug("[Auth_login]:login success.,the token is '"
				+ login.getToken() + "' and the userid is '"
				+ login.getId() + "'.");
		
		return JSONHelper.getDefaultResponse(Status.OK, login).toString();
	}

	@GET
	@Path("check_login")
	@Produces(MediaType.APPLICATION_JSON)
	public String check_login(
			@QueryParam("user_id") @DefaultValue("no-id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("return_info") @DefaultValue("no-info") String returnInfo,
			@Context HttpServletRequest request) {
		log.debug("[Auth_check_login]:check_login begin.");
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
			log.debug("[Auth_check_login]:validation sucess.");
			if (returnInfo.equals("true")) {
				UserEntity ue = this.authManager.check_login(userId);

				if (ue == null) {
					log.debug("[Auth_check_login]:check_login system error!.");
					return JSONHelper.getDefaultResponse(Status.SYS_ERROR, null).toString();
				}
				return JSONHelper.getDefaultResponse(Status.OK, ue).toString();
			}
			return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
		}
		
		log.debug("[Auth_check_login]:validation error.");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
		
	}
	
	@GET
	@Path("log_out")
	@Produces(MediaType.APPLICATION_JSON)
	public String logout(
			@QueryParam("user_id") @DefaultValue("no-id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {
		log.debug("[Auth_log_out]:begin.Fuck you, Please come out!");
		if(!this.isValidateUser(userId, token, this.getIpAddr(request))){
			log.debug("[Auth_log_out]:validation failure.");
			return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, "false").toString();
		}

		log.debug("[Auth_log_out]:validation sucess.");
		String key = userId + ":" + this.getIpAddr(request);
		this.authManager.logout(token, key);
		return JSONHelper.getDefaultResponse(Status.OK, "true").toString();	
		
	}
	
	@GET
	@Path("forget_password")
	@Produces(MediaType.APPLICATION_JSON)
	public String forgetpassword(@QueryParam("email") @DefaultValue("no-email") String email,
			@Context HttpServletRequest request )
		throws IOException, ParserConfigurationException, SAXException{
		JSONObject result;
		String str = emailManager.sendValidEmail(email);
		if( str.equals(Status.NOT_FOUND) ){
			log.debug("[Auth forget password] : not found ");
			result = JSONHelper.getDefaultResponse(Status.NOT_FOUND, null);
			return result.toString();
		}else if( str.equals(Status.AUTH_ERROR)){
			log.debug("[Auth forget password] : auth error ");
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			return result.toString();
		}else{
			log.debug("[Auth forget password] : success");
			result = JSONHelper.getDefaultResponse(Status.OK, null);
			return result.toString();
		}
	}
	
	@GET
	@Path("reset_password")
	@Produces(MediaType.APPLICATION_JSON)
	public String resetpassword(@QueryParam("email") @DefaultValue("no-email") String email,
			@QueryParam("password") @DefaultValue("no-password") String password,
			@QueryParam("pw_valid_code") @DefaultValue("no-pw_valid_code") String pwValidCode,
			@Context HttpServletRequest request ){
		JSONObject result;
		if (email.equals("no-email") || password.equals("no-password")
				|| pwValidCode.equals("no-verify_code")) {
			log.debug("[reset password] : para error" + "email : " + email
					+ " password : " + password + " verify_code : "
					+ pwValidCode);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}
		
		String str = emailManager.ResetPassword(email, password, pwValidCode);
		if( str.equals(Status.AUTH_ERROR) ){
			log.debug("[reset password] : auth error");
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			return result.toString();
		}else if(str.equals(Status.NOT_FOUND)){
			log.debug("[reset password] : not found ");
			result = JSONHelper.getDefaultResponse(Status.NOT_FOUND, null);
			return result.toString();
		}else{
			log.debug("[reset password] : success ");
			result = JSONHelper.getDefaultResponse(Status.OK, null);
			return result.toString();
		}
	}
}
