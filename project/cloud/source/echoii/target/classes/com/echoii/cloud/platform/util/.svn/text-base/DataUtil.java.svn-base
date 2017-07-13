package com.echoii.cloud.platform.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataUtil {

	public final static Set<String> getGroupListMode() {
		Set<String> groupListMode = new HashSet<String>();
		if(groupListMode.isEmpty()){
			groupListMode.add("create");
			groupListMode.add("join");
			groupListMode.add("all");
			
			return groupListMode;
		}
		
		return groupListMode;
	}

	public final static Set<String> getGroupListOrder() {
		Set<String> groupListOrder = new HashSet<String>();
		groupListOrder = new HashSet<String>();
		groupListOrder.add("create_date");
		groupListOrder.add("name");
		return groupListOrder;
	}
	
	public final static Set<String> getGroupFilefMode(){
		Set<String>  groupFilefMode = new HashSet<String>();
		groupFilefMode.add("mine");
		groupFilefMode.add("publish");
		groupFilefMode.add("pending");
		groupFilefMode.add("deny");
		return groupFilefMode;
	}

	
	public final static Set<String> getDeviceStatus(){
		Set<String> deviceStatus = new HashSet<String>();
		deviceStatus.add("all");
		deviceStatus.add("approve");
		deviceStatus.add("refuse");
		deviceStatus.add("request");
		return deviceStatus;
	}
	
	public final static List<String> getImageType() {
		List<String> imageTypeList = new ArrayList<String>();
		imageTypeList.add("bmp");
		imageTypeList.add("pcx");
		imageTypeList.add("tiff");
		imageTypeList.add("gif");
		imageTypeList.add("jpeg");
		imageTypeList.add("jpg");
		imageTypeList.add("tga");
		imageTypeList.add("exif");
		imageTypeList.add("fpx");
		imageTypeList.add("svg");
		imageTypeList.add("psd");
		imageTypeList.add("cdr");
		imageTypeList.add("pcd");
		imageTypeList.add("eps");
		imageTypeList.add("dxf");
		imageTypeList.add("ufo");
		imageTypeList.add("png");
		imageTypeList.add("hdri");
		imageTypeList.add("ai");
		imageTypeList.add("raw");

		return imageTypeList;
	}
	
	public final static List<String> getDocType(){
		List<String> docTypeList =  new ArrayList<String>();
		docTypeList.add("txt");
		docTypeList.add("pdf");
		docTypeList.add("doc");
		docTypeList.add("docx");
		docTypeList.add("xls");
		docTypeList.add("xlsx");
		docTypeList.add("ppt");
		docTypeList.add("pptx");
		docTypeList.add("rtf");
		docTypeList.add("odt");
		docTypeList.add("docm");
		docTypeList.add("dotx");
		docTypeList.add("dotm");
		docTypeList.add("xps");
		docTypeList.add("mht");
		docTypeList.add("mhtml");
		docTypeList.add("htm");
		docTypeList.add("html");
		docTypeList.add("xml");
		docTypeList.add("odt");
		docTypeList.add("wtf");
		docTypeList.add("vsd");
		return docTypeList;
	}
	
	public  final static List<String> getVideoType(){
		List<String> videoTypeList = new ArrayList<String>();
//		videotypelist.add("avi");
//		videotypelist.add("mov");
//		videotypelist.add("asf");
//		videotypelist.add("wmv");
//		videotypelist.add("avi");
//		videotypelist.add("navi");
//		videotypelist.add("3gp");
//		videotypelist.add("ram");
//		videotypelist.add("mkv");
		videoTypeList.add("flv");
//		videotypelist.add("rmvb");
		videoTypeList.add("mp4");
//		videotypelist.add("mpeg");
//		videotypelist.add("mpg");
//		videotypelist.add("dat");
//		videotypelist.add("rm");

		return videoTypeList;
	}

	public final static List<String> getMusicType() {
		List<String> musicTypeList = new ArrayList<String>();
		musicTypeList.add("wav");
		musicTypeList.add("ape");
		musicTypeList.add("flac");
		musicTypeList.add("wma");
		musicTypeList.add("ogg");
		musicTypeList.add("mp3");
		musicTypeList.add("acc");
		return musicTypeList;
	}

	public  final static List<String> getOthersType() {
		List<String> othersTypeList = new ArrayList<String>();
		othersTypeList.addAll(getDocType());
		othersTypeList.addAll(getMusicType());
		othersTypeList.addAll(getImageType());
		othersTypeList.addAll(getVideoType());
		return othersTypeList;
	}


}
