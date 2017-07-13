/**
 * 
 */
package com.echoii.cloud.platform.service.test;

import java.util.List;

import com.echoii.cloud.platform.entity.HdcFileEntity;
import com.echoii.cloud.platform.manager.HdcManager;

/**
 * @author leaguenew
 *
 */
public class HdcServiceTest {
	private static HdcManager hdcManager = HdcManager.getInstance();
	//private static HdcService hdcService = HdcServiceImpl.getInstance();
	
	public static void main(String [] args){
		/*
		String deviceId1 = "homedatacenter1";
		String token1 = "123";
		
		hdcManager.register(deviceId1);
		token1 = "1234";
		hdcManager.register(deviceId1);
		hdcManager.login(deviceId1, token1);
		
		String deviceId2 = "homedatacenter2";
		String token2 = "12345";
		hdcManager.register(deviceId2);
		hdcManager.login(deviceId2, token2);
		
		hdcManager.login("hehe", "yes");
		
		String pwd = hdcManager.register("homedatacenter4");
		System.out.println(pwd);
		*/
		
		List<HdcFileEntity>  hdcfilelist = hdcManager.listAll("44-33-4c-22-3c-56", "c195e2e1f152de1984ca3089aa7340b6");
		System.out.println(hdcfilelist.size());
		
	}
			
}
