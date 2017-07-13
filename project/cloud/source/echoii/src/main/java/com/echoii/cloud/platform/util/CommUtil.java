package com.echoii.cloud.platform.util;

import java.net.HttpURLConnection;
import java.net.URL;


public class CommUtil {
	
	public final static boolean isMail(String email) {
		if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
			return false;
		} else
			return true;
		
	}	
	
	
	public final static String getTorrentHash(String torrentUrl){
		try {
			URL u = new URL(torrentUrl);
			HttpURLConnection urlconn = (HttpURLConnection) u.openConnection();
			int state = urlconn.getResponseCode();
			
			if (state != 200 || !urlconn.getContentType().equals("application/x-bittorrent")) {
			
				return null;
			} 
			
			return HashUtil.strHash(StringUtil.StreamToString(urlconn.getInputStream()));
			
		} catch (Exception e) {
			return null;
		}	

	}
	
	public final static String formatSizeDisp( long size){
		if( size == 0 ){
			return "";
		}
		
		String suffix = "B";
		
		if( size < 1024 ){
			return size + suffix;
		}
		
		double result = size;
		
		result = result / 1024;
		suffix = "KB";
		
		if( result > 1024 ){
			result = result / 1024;
			suffix = "MB";
		}
		
		if( result > 1024 ){
			result = result / 1024;
			suffix = "GB";
		}
		
		//log.debug( "result" + result + ",size = " + size );
		return StringUtil.formatDouble("#.00", result ) + suffix;
	}
}
