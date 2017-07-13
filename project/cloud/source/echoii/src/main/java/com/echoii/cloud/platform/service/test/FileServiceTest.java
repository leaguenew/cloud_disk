package com.echoii.cloud.platform.service.test;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.factory.FileEntityFactory;
import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.util.HashUtil;

public class FileServiceTest {
	static Logger log = Logger.getLogger(FileServiceTest.class);
	static FileService fs = FileServiceImpl.getInstance();
	
	public static void main(String [] args){
		log.debug("file service test begin");
		for(int i = 0 ; i < 10; ++i){
			//createfile();
		}
	
		//System.out.println(fs.countDocuments("123"));
		
		File file = new File();
		file.setId("flv2");
		file.setMetaFolder("201311");
		file.setMetaId("2.flv");
		file.setUserId("zdh");
		file.setCreateDate(new Date());
		file.setType("flv");
		fs.createFile(file);
//		file.setCreateDate(new Date());
//		file.setId("12345");
//		file.setMetaId("111");
//		file.setType("bmp");
//		file.setName("tupian");
//		file.setPath("D:\\01\\111.bmp");
//		fs.update(file);
		//String id1 = "1380010647628";//"1379922102228";
		//String id2 = "1379925060998";
		//String id3 = "1379925927664";
		
		//String userId = "61f2fe361879166fb14e4b76ab670667";
//		//String userId = "520";
//		String parentId = "music";
//		
		//file = createFileTest();
//		//updateFileTest(file);
//		//findByIdTest(id1);
//		//findByIdTest(id2);
		//deleteTest("2ad90561fee5dbedad9386d433842c60");
//		
//		List<String> typeList = new ArrayList<String>();
//		typeList.add("doc");
//		typeList.add("ppt");
//		typeList.add("rmvb");
//		typeList.add("mp3");
//		typeList.add("java");
//		
		//listAllTest(userId,parentId,"asc","size",-1,2000);
		//listByTypesTest(userId,parentId, typeList,"asc", "size", -2, 0);
		//listTrushTest(userId, "desc", "lmf_date",-3, 0);
	}
	
	public static void createfile(){
		File file = new File();
		file.setUserId("123");
		file.setType("doc");
		file.setId(HashUtil.getRandomId());
		file.setSize(20);
		fs.createFile(file);
	}
	
	public static File createFileTest(){
		File file = new File();
		Date d = Calendar.getInstance().getTime();
		file.setId(Long.toString(d.getTime()));
		file.setCreateDate(new Date());
		file.setName("5cc");
		file.setStatus(File.STATUS_NORMAL);
		file.setType("cpp");
		file.setUserId("530");
		file.setFolderId("root");
		file.setSize(0);
		if(file.getFolderId() == "root"){
			file.setPath("/"+file.getFolderId()+"/"+file.getName());
		}else{
			file.setPath(fs.getFileById(file.getFolderId()).getPath()+"/"+file.getName());
		}
		
		if(fs.getFileByName(file)==null){
			fs.createFile(file);
			log.debug("create file success");
			return file;
		}else{
			log.debug("create file fail because of duplicated name");
			return null;
		}
	}
	
	public static void updateFileTest(File file){
		/*
		File file = createFileTest();
		file.setName("update file test");
		fs.update(file);
		*/
		if(file == null)
			return ;
		File file1 = fs.getFileById(file.getId());
		file1.setName("update file test");
		fs.updateFile(file1);
	}
	
	public static void findByIdTest(String id) {
		if(id==""||id.equals(""))
			return ;
		File file = fs.getFileById(id);
		System.out.println("Id : "+file.getId()+" CreateDate : "+file.getCreateDate()+" name : "+file.getName());
	}
	
	public static void deleteTest(String id){
		if(id==""||id.equals(""))
			return ;
		//fs.deleteFile(id);
	}
	
	public static void listAllTest(String userId,String parentId,
			String order,String orderColumn,int begin,int size){
		System.out.println("enter listAllTest");
		List<File> list = fs.listAllFile(userId, parentId, order, orderColumn, begin, size);
		if(list == null){
			System.out.println("list is null");
		}
		
		System.out.println("list size = "+list.size());
		Iterator<File> it = list.iterator();
		while(it.hasNext()){
			File fileIt = (File)it.next();
			System.out.println(fileIt.getId()+" "+fileIt.getUserId()+" "+
					fileIt.getFolderId()+" "+fileIt.getName()+" "+fileIt.getType()+" "+fileIt.getSize());
		}
	}
	
	public static void listByTypesTest(String userId, String parentId,
			List<String> typeList, String order, String orderColumn, int begin, int size) {
		System.out.println("enter listByTypesTest");
		List<File> list = fs.listFileByTypes(userId, parentId, typeList, order, orderColumn, begin, size);
		if(list == null){
			System.out.println("list is null");
		}
		
		System.out.println("list size = "+list.size());
		Iterator<File> it = list.iterator();
		while(it.hasNext()){			
			File fileIt = (File)it.next();
			System.out.println(fileIt.getId()+" "+fileIt.getUserId()+" "+fileIt.getFolderId()
					+" "+fileIt.getName()+" "+fileIt.getType()+" "+fileIt.getSize());
		}
	}
	
	public static void listTrushTest(String userId, String order, String orderColumn,
			int begin,int size) {
		
		System.out.println("enter listTrushTest");
		List<File> list = fs.listTrushFile(userId, order, orderColumn, begin, size);
		if(list == null){
			System.out.println("list is null");
		}
		
		System.out.println("list size = "+list.size());
		Iterator<File> it = list.iterator();
		while(it.hasNext()){
			File fileIt = (File)it.next();
			System.out.println(fileIt.getId()+" "+fileIt.getUserId()+" "+fileIt.getFolderId()
					+" "+fileIt.getName()+" "+fileIt.getType()+" "+fileIt.getSize()+"\t"+fileIt.getLmf_date());
		}
	}
}
