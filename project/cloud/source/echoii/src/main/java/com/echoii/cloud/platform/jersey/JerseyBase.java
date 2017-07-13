package com.echoii.cloud.platform.jersey;

import javax.servlet.http.HttpServletRequest;

import com.echoii.cloud.platform.service.CacheService;
import com.echoii.cloud.platform.service.impl.CacheServiceMapImpl;
import com.echoii.cloud.platform.service.impl.CacheServiceRedisImpl;
import com.echoii.cloud.platform.util.Config;

public class JerseyBase {
	public Config config = Config.getInstance();
	public CacheService cacheservice = null;

	private void cacheInit() {
		if (config.getStringValue("cache.impl.method", "map").equals("map")) {
			cacheservice = CacheServiceMapImpl.getInstance();
		} else {
			cacheservice = CacheServiceRedisImpl.getInstance();
		}
	}

	public boolean isValidateUser(String userId, String token, String ip) {
		if (cacheservice == null) {
			cacheInit();
		}
		String key = userId+":"+ip;
		if (config.getStringValue("validation.mode.debug", "false").equals("true")) {	
			return true;
		} 
		System.out.println("validation begin, not debug!");
		if (cacheservice.validate(token, key)) { //	
			return true;
		} 
		return false;
					
	}
	
	public final String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP"); //first
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");//second
		}
		if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();//last
		}
		return ip;
	}
}
