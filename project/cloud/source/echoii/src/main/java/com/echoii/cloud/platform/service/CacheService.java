package com.echoii.cloud.platform.service;

public interface CacheService {

	public void setIdcode(String key, String data, int sec);

	public String getIdcode(String key);

	public String getToken(String key);

	public void setToken(String key, String token);

	public boolean validate(String key, String token);

	public boolean delete(String key, String token);
	
	


}
