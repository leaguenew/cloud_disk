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

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import com.echoii.cloud.platform.entity.HdcFileEntity;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.jersey.helper.JSONHelper;
import com.echoii.cloud.platform.manager.HdcManager;
import com.echoii.cloud.platform.model.HdcBinding;
import com.echoii.cloud.platform.model.HdcDetail;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DataUtil;
import com.echoii.cloud.platform.util.Status;

@Path("device")
public class Device extends JerseyBase{
	static Logger log = Logger.getLogger(Device.class);
	HdcManager hdcManager = HdcManager.getInstance();
	
	@GET
	@Path("reg")
	@Produces(MediaType.APPLICATION_JSON)
	public String reg(
			@QueryParam("device_id")  @DefaultValue("no-device_id") String deviceId,
			@Context HttpServletRequest request){
		
		log.debug("device reg begin");
		
		String pwd = null;
		JSONObject result = null;
		
		if( deviceId.equals("no-device_id")  ){
			log.debug("device reg para error ： device_id = "+deviceId);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}
		
		pwd = this.hdcManager.register(deviceId);
		
		//if pwd is not null , then allow to register
		if(pwd!=null){
			log.debug("register success");
			result = JSONHelper.getDefaultResponse(Status.OK, pwd);
			return result.toString();
		}else{
			log.debug("register fail , the device id has existed");
			result = JSONHelper.getDefaultResponse(Status.NAME_DUP, null);
			return result.toString();
		}
		
	}
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String login(
			@QueryParam("device_id") @DefaultValue("no-device_id") String deviceId,
			@QueryParam("token")  @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		JSONObject result;
		
		if( deviceId.equals("no-device_id") || token.equals("token") ){
			log.debug("[Device_login] : device login para error");
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}
		
		log.debug("[Device login] : begin");
		String str;
		str = this.hdcManager.login(deviceId,token);
		
		if(str==null){
			log.debug("[Device_login] : auth error");
			result = JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null);
			return result.toString();
		}else{
			log.debug("[Device_login] : login success");
			result = JSONHelper.getDefaultResponse(Status.OK, null);
			return result.toString();
		}
		
	}
	
	@GET
	@Path("sync/list_all_file")
	@Produces(MediaType.APPLICATION_JSON)
	public String listallfile(@QueryParam("device_id") @DefaultValue("no-device_id") String deviceId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request) {
		
		log.debug("list totally begin");

		List<HdcFileEntity> hfe;
		JSONObject result;
		
		Config config = Config.getInstance();
		
		// parameter error
		if (deviceId.equals("no-device_id") || token.equals("no-token")) {
			log.debug("[File list]: para error: deviceId = " + deviceId
					+ " token = " + token);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}


		hfe = this.hdcManager.listAll(deviceId, token);
		if(hfe!=null){
			result = JSONHelper.getDefaultResponse(Status.OK, hfe);
			log.debug("[Device File list]: device file list success!  deviceId = " + deviceId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}else{
			result = JSONHelper.getDefaultResponse(Status.NOT_FOUND, hfe);
			log.debug("[Devive File list]: device file list fail!  deviceId = " + deviceId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
		
	}
	
	@GET
	@Path("sync/list_file")
	@Produces(MediaType.APPLICATION_JSON)
	public String listfile(@QueryParam("device_id") @DefaultValue("no-device_id") String deviceId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@QueryParam("folder_id") @DefaultValue("root") String folderId,
			@Context HttpServletRequest request) {
		
		log.debug("list totally begin");

		List<HdcFileEntity> hfe;
		JSONObject result;
		
		Config config = Config.getInstance();
		
		// parameter error
		if (deviceId.equals("no-device_id") || token.equals("no-token")) {
			log.debug("[File list]: para error: deviceId = " + deviceId
					+ " token = " + token);
			result = JSONHelper.getDefaultResponse(Status.PARA_ERROR, null);
			return result.toString();
		}

		hfe = this.hdcManager.listAll(deviceId, token , folderId);
		if(hfe!=null){
			result = JSONHelper.getDefaultResponse(Status.OK, hfe);
			log.debug("[Device File list]: device file list success!  deviceId = " + deviceId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}else{
			result = JSONHelper.getDefaultResponse(Status.NOT_FOUND, hfe);
			log.debug("[Devive File list]: device file list fail!  deviceId = " + deviceId
					+ " token = " + token + " IP = "
					+ this.getIpAddr(request));
			return result.toString();
		}
	}
	
	@GET
	@Path("device_info")
	@Produces(MediaType.APPLICATION_JSON)
	public String device_info(
			@QueryParam("device_id") @DefaultValue("no-device_id") String deviceId,
			@QueryParam("user_id") @DefaultValue("no-user_id") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		HdcDetail hdcdetail = null;
		log.debug("[device_device_info]: device_device_info begin!");
		if(deviceId.equals("no-device_id")||userId.equals("no-user_id")||token.equals("no-token")){
			log.debug("[device_device_info]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		log.debug("[device_device_info]: begin verification!");
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
			
			hdcdetail = this.hdcManager.device_info(deviceId);
			
			if (hdcdetail != null) {
				log.debug("[device_device_info]: device_info ok!");
				return JSONHelper.getDefaultResponse(Status.OK, hdcdetail).toString();
			}
			log.debug("[device_device_info]: device error!");
			return JSONHelper.getDefaultResponse(Status.DEVICE_ERROR, null).toString();
		} 
		log.debug("[device_device_info]:verification error !");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
				
		
	}
	
	@GET
	@Path("binding/list_bound_device")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_bound_device(
			@QueryParam("status")@DefaultValue("no-status") String status,
			@QueryParam("begin")@DefaultValue("0") int  begin,
			@QueryParam("size") @DefaultValue("20") int size,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		List<HdcBinding> hdcbindings = null;
		log.debug("[device_device_info]: device_device_info begin!");
		if(!DataUtil.getDeviceStatus().contains(status)||userId.equals("no-userid")||token.equals("no-token")){
			log.debug("[device_device_info]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		log.debug("[device_device_info]: begin verification!");
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
			
			hdcbindings = this.hdcManager.list_bound_device(userId, status, begin, size);
		
			if (hdcbindings != null) {
				log.debug("[device_device_info]: device_info ok!");
				return JSONHelper.getDefaultResponse(Status.OK, hdcbindings).toString();
			} 
			
			log.debug("[device_device_info]: device error!");
			return JSONHelper.getDefaultResponse(Status.DEVICE_ERROR, null).toString();
		}
		log.debug("[device_device_info]:verification error !");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
				
	}
	
	@GET
	@Path("binding/list_bound_user")
	@Produces(MediaType.APPLICATION_JSON)
	public String list_bound_user(
			@QueryParam("status")@DefaultValue("no-status") String status,
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId,
			@QueryParam("begin")@DefaultValue("0") int  begin,
			@QueryParam("size") @DefaultValue("20") int size,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		List<UserEntity> users = null;
		
		log.debug("[device_device_user]: device_device_user begin!");
		if(!DataUtil.getDeviceStatus().contains(status)||userId.equals("no-userid")||token.equals("no-token")){
			log.debug("[device_device_user]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		log.debug("[device_device_user]: begin verification!");
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
			
			users = this.hdcManager.list_bound_user(deviceId, status, begin, size);
		
			if (users != null) {
				log.debug("[device_device_user]: device_user ok!");
				return JSONHelper.getDefaultResponse(Status.OK, users).toString();
			}
			log.debug("[device_device_user]: device error!");
			return JSONHelper.getDefaultResponse(Status.DEVICE_ERROR, null).toString();
			
		}
		log.debug("[device_device_user]:verification error !");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
					
	}
	
	@GET
	@Path("binding/bind")
	@Produces(MediaType.APPLICATION_JSON)
	public String bind(	
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		
		log.debug("[device_bind]: bind begin!");
		if(deviceId.equals("no-device-id")||userId.equals("no-userid")||token.equals("no-token")){
			log.debug("[device_bind]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		log.debug("[device_bind]: begin verification!");
		
		String status = null;
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
			
			status = this.hdcManager.bind(deviceId, userId);
			if ( status.equals(Status.OK) ) {
				log.debug("[device_bind]: device_user ok!");
				return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
			}else if( status.equals(Status.DEVICE_ERROR)){
				log.debug("[device_bind]: device error!");
				return JSONHelper.getDefaultResponse(Status.DEVICE_ERROR, null).toString();
			}else if( status.equals(Status.NOT_FOUND) ){
				log.debug("[device_bind]: user not found!");
				return JSONHelper.getDefaultResponse(Status.NOT_FOUND, null).toString();
			}else if( status.equals(Status.NAME_DUP) ){
				log.debug("[device_bind]: the device id has duplicated binding!");
				return JSONHelper.getDefaultResponse(Status.NAME_DUP, null).toString();
			}
	
		} 
		log.debug("[device_bind]:verification error !");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
				
	}
	
	@GET
	@Path("binding/cancel_binding")
	@Produces(MediaType.APPLICATION_JSON)
	public String cancel_bind(	
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		
		log.debug("[device_unbind]: unbind begin!");
		if(deviceId.equals("no-device-id")||userId.equals("no-userid")||token.equals("no-token")){
			log.debug("[device_bind]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		log.debug("[device_bind]: begin verification!");
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
		
			if (this.hdcManager.cancel_binding(userId, deviceId)) {
				log.debug("[device_unbind]: unbind ok!");
				return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
			} 
			log.debug("[device_unbind]: unbind error!");
			return JSONHelper.getDefaultResponse(Status.DEVICE_ERROR, null).toString();
			
		} 
		log.debug("[device_unbind]:verification error !");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
					
	}
	
	@GET
	@Path("binding/update_binding_status")
	@Produces(MediaType.APPLICATION_JSON)
	public String update_binding_status(	
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId,
			@QueryParam("device_token")@DefaultValue("no-token") String deviceToken,
			@QueryParam("new_status")@DefaultValue("no-status") String status,
			@QueryParam("user_id") @DefaultValue("no-userid") String userId,
			@QueryParam("token") @DefaultValue("no-token") String token,
			@Context HttpServletRequest request){
		
		
		log.debug("[device_update_binding_status]: update_binding_status begin!");
		if(status.equals("no-status")||deviceId.equals("no-device-id")
				||userId.equals("no-userid")
				||token.equals("no-token")){
			log.debug("[device_bind]: para error!");
			return JSONHelper.getDefaultResponse(Status.PARA_ERROR, null).toString();
		}
		log.debug("[device_update_binding_status]: begin verification!");
		
		if (this.isValidateUser(userId, token, this.getIpAddr(request))) {
		
			if (this.hdcManager.update_binding_status(deviceId, deviceToken, userId, status)) {
				log.debug("[device_update_binding_status]: update_binding_status ok!");
				return JSONHelper.getDefaultResponse(Status.OK, "true").toString();
			} 
			log.debug("[device_update_binding_status]: update_binding_status error!");
			return JSONHelper.getDefaultResponse(Status.DEVICE_ERROR, null).toString();
			
		} 
		log.debug("[device_update_binding_status]:verification error !");
		return JSONHelper.getDefaultResponse(Status.AUTH_ERROR, null).toString();
			
	}
	
	@GET
	@Path("sync/heartbeat")
	@Produces(MediaType.APPLICATION_JSON)
	public String heartbeat(	
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId,
			@QueryParam("device_token")@DefaultValue("no-token") String deviceToken){	
		log.debug("[device_active]: active begin!");
	
		return JSONHelper.getDefaultResponse(Status.OK, this.hdcManager.active(deviceId)).toString();
		
	}
	
	@GET
	@Path("auth/active")
	@Produces(MediaType.APPLICATION_JSON)
	public String active(	
			@QueryParam("device_id")@DefaultValue("no-device-id") String deviceId){	
		log.debug("[device_active]: active begin!");
	
		return JSONHelper.getDefaultResponse(Status.OK, this.hdcManager.active(deviceId)).toString();
		
	}

}
