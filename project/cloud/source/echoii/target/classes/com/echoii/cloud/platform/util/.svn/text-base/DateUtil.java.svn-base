package com.echoii.cloud.platform.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static String PATTERN_yyyyMM = "yyyyMM";
	public static String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";
	public static String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static String PATTERN_yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";
	public static String PATTERN_yyyyMMdd = "yyyyMMdd";
	public static String PATTERN_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
	public static String PATTERN_HTTP = "EEE, dd MMM yyyy HH:mm:ss zzz";
	
	public static String getCurrentTimeString(){
		return parseDateToString( 
				Calendar.getInstance().getTime(), PATTERN_yyyyMMddHHmmssSSS );
	}
	
	public static String parseDateToString(Date date, String pattern) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat _df = new SimpleDateFormat(pattern);
		return _df.format(date);
	}
	
	public final static long getDateLong(){
		return (new Date()).getTime();
	}
	
	public static Date format(Date date ,String pattern){
		if (date == null) {
			return date;
		}
		SimpleDateFormat _df = new SimpleDateFormat(pattern);
		_df.format(date);
		return date;
	}
	
	public static double getCurrentTime_fortoken(){
		return Double.parseDouble(getCurrentTimeString().substring(0, 13));
	}
	
	public static String parseDateToHttp( Date date ){
		if (date == null) {
			return "";
		}
		SimpleDateFormat _df = new SimpleDateFormat( PATTERN_HTTP, Locale.US );
		return _df.format(date);
	}
	
	public static Date parseLongToDate(long time ){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getTime();
	}
	
	public static long getTimeDiff(Date begin_date , Date end_date){
		return  end_date.getTime() - begin_date.getTime() ;
	}
}
