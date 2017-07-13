package com.echoii.cloud.platform.service.impl;

import java.util.HashMap;
import java.util.Map;
import com.echoii.cloud.platform.service.CacheService;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DateUtil;

public class CacheServiceMapImpl implements CacheService {	
	private static Map<String, String> preLoginToken = null;
	private static Map<String, Token> loginTokenTerm = null;
	private static volatile CacheService SERVICE = null;
	private static Config config = Config.getInstance();

	
	public static CacheService getInstance() {
		if (SERVICE == null) {
			synchronized (CacheServiceMapImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new CacheServiceMapImpl();
				}
			}
		}
		return SERVICE;
	}

	public CacheServiceMapImpl() {
		preLoginToken = new HashMap<String, String>();
		loginTokenTerm = new HashMap<String, Token>();
	}

	@Override
	public void setIdcode(String key, String data, int sec) {
		preLoginToken.put(key, data);
	}

	@Override
	public String getIdcode(String key) {
		return preLoginToken.get(key);
	}

	@Override
	public void setToken(String key, String token) {
		Token token1 = new Token();
		token1.token = token;
		token1.time = DateUtil.getCurrentTime_fortoken()+ config.getIntValue("cache.prelogin.maintain.sec", 60); 
		loginTokenTerm.put(key, token1);
	}

	private void setTokenTime(String key) {
		Token token1 = new Token();
		token1.token = getToken(key);
		token1.time = DateUtil.getCurrentTime_fortoken()+ config.getIntValue("cache.prelogin.maintain.sec", 60); 
		loginTokenTerm.put(key, token1);
	}

	@Override
	public String getToken(String key) {
		return loginTokenTerm.get(key).token;
	}
	

	//check the time
	private boolean isTimeVaild(String key) {	
			double time = loginTokenTerm.get(key).time - DateUtil.getCurrentTime_fortoken();
			double checking_sec = config.getIntValue("cache.prelogin.checking.sec", 5);	
			
			if (time >= checking_sec) 
				return true;
			else{
				if (time < 0) 
					return false;
				else{
					setTokenTime(key);
					return true;
				}
			}	
	}

	@Override
	public boolean validate(String key, String token) {
		if (!loginTokenTerm.containsKey(key))
			return false;
		
		if (getToken(key).equals(token)) {
			return isTimeVaild(key);
		} else
			return false;
	}
	
	class Token {
		public String token;
		public double time;
	}

	@Override
	public boolean delete(String userid, String token) {
		// TODO Auto-generated method stub
		return false;
	}

}
