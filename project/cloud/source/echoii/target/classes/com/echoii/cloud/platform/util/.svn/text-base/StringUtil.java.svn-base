package com.echoii.cloud.platform.util;

import java.io.IOException;
import java.io.InputStream;

public class StringUtil {
	
	public static String formatDouble(String format, double d ){
		//"#.00"
		java.text.DecimalFormat df = new java.text.DecimalFormat( format ); 
		return df.format(d);
	}

	public static int stringToInt( String str, int def ){
		
		//if the the string of str is not fit the format of Integer, return def;
		try{
			return Integer.parseInt(str);
		}catch(Exception e){
			return def;
		}
		
	}
	
	public static long stringToLong( String str, long def ){
		
		//if the the string of str is not fit the format of Long, return def;
		try{
			return Long.parseLong(str);
		}catch(Exception e){
			return def;
		}
	}
	
	public final static String StreamToString(InputStream stream){
		if(stream == null){
			return null;
		}
		StringBuffer out = new StringBuffer();
	     byte[] b = new byte[4096];
	     try {
			for (int n; (n = stream.read(b)) != -1;) {
			  out.append(new String(b, 0, n));
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     return out.toString();
	}
}
