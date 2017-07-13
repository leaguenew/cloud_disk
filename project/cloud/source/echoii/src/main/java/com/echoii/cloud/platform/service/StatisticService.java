package com.echoii.cloud.platform.service;

public interface StatisticService {
	
	public Long countPictures(String userid);
	
	public Long countVideos(String userid);
	
	public Long countMusics(String userid);
	
	public Long countDocuments(String userid);
	
	public Long countOthers(String userid);
	
	public Long countAll(String userid);
	
	public Long sumAllSize(String userid, String propertity);

}
