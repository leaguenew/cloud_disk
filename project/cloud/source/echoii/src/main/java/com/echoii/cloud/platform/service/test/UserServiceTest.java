package com.echoii.cloud.platform.service.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.model.User;
 
import com.echoii.cloud.platform.model.UserDetail;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.HashUtil;

public class UserServiceTest {
	static Logger log = Logger.getLogger( UserServiceTest.class );
	
	static UserService us = UserServiceImpl.getInstance();
	
	static FileService fs  = FileServiceImpl.getInstance();
	private static Config config = Config.getInstance();
	
	public static void main(String[] argv) throws IOException, InterruptedException{
		log.debug("test begin");
		Date data = new Date();
		//System.out.println(data.getTime());
//		String path = java.net.URLDecoder.decode("我试试","GBK");
//		System.out.println(path);
//		long a = (long) 344.0;
//		long b = (long) 3433.1;
//		System.out.println(((float)a/(float)b));
//		UserDetail userdetail = new UserDetail();
//		userdetail.setId(HashUtil.getRandomId());
//		userdetail.setUserid("61f2fe361879166fb14e4b76ab670667");
//		us.createUserDetail(userdetail);
		//List<String> strList = new ArrayList();  
//		File file = new File("/home/zdh/echoii/data/201310");
//		file.mkdirs();
//		com.echoii.cloud.platform.model.File f1 = new com.echoii.cloud.platform.model.File();
//		f1.setId("f1");
//		f1.setType("folder");
//		f1.setUserId("zdh");
//		f1.setName("folder1");
//		fs.create(f1);
//		
//		com.echoii.cloud.platform.model.File f2 = new com.echoii.cloud.platform.model.File();
//		f2.setId("f2");
//		f2.setType("folder");
//		f2.setUserId("zdh");
//		f2.setName("folder2");
//		f2.setParentId("f1");
//		fs.create(f2);
//		  
//		com.echoii.cloud.platform.model.File f3 = new com.echoii.cloud.platform.model.File();
//		f3.setId("f3");
//		f3.setType("exe");
//		f3.setUserId("zdh");
//		f3.setName("jdk.exe");
//		f3.setParentId("f1");
//		f3.setMetaFolder("201310");
//		f3.setMetaId("jdk");
//		fs.create(f3);
//		
//		com.echoii.cloud.platform.model.File f4 = new com.echoii.cloud.platform.model.File();
//		f4.setId("f4");
//		f4.setType("pdf");
//		f4.setUserId("zdh");
//		f4.setName("jersey.pdf");
//		f4.setParentId("f2");
//		f4.setMetaFolder("201310");
//		f4.setMetaId("jersey");
//		fs.create(f4);
		
//		String path = "/home/zdh/echoii/data/batchdownlad/folder1";
//		processlocal("zdh","f1",path);
//		String shell = "zip -q -r /home/zdh/echoii/data/batchdownlad/folder1.zip "+path;
//		String[] commands = {
//				"/bin/sh", 
//				"-c", 
//				"cd /home/zdh/echoii/data/batchdownlad;zip -r folder1.zip folder1"
//		};
		//commands[0] = "/bin/sh";
		//commands[1] = "cd /home/zdh/echoii/data/batchdownlad";
		//commands[2] = "zip -r folder1.zip folder1";
		//shell = "cd /home/zdh/echoii/data/batchdownlad";
		// Runtime.getRuntime().exec(commands).waitFor();
		// Runtime.getRuntime().exec(shell).waitFor();
//		 shell = "mv folder1.zip "+path;
//		 Runtime.getRuntime().exec(shell).waitFor();
//        Process process; 
//        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c"," cd /home/zdh "},null,null).waitFor();
//        
//        process = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c"," cd /home/zdh ","ls"},null,null);  
//        InputStreamReader ir = new InputStreamReader(process  
//                .getInputStream());  
//        LineNumberReader input = new LineNumberReader(ir);  
//        String line;  
//        process.waitFor();  
//        while ((line = input.readLine()) != null){  
//            strList.add(line);  
//            System.out.println(line);
//        }  
        log.debug("end"); 
      //  return strList;  
		//getUserByNamehashTest();
	}
	
	public static void process(String userId, String folderid, String path) throws InterruptedException, IOException{
		//create folder
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		
		List<com.echoii.cloud.platform.model.File> files = fs.listAllFile(userId, folderid, null, null, 0, 100);
		for(int i = 0 ; i < files.size(); ++i){
			if(files.get(i).getType().equals("folder")){ //folder,recursion
				process(userId,files.get(i).getId(),path+"/"+files.get(i).getName());
			}else{ //not folder, is file
				//copy the file to folder
				String shell = "cp " + config.getStringValue(
						"upload.directory", "/mnt/moosefs/echoii/data")
						+ "/" + files.get(i).getMetaFolder() + "/" + files.get(i).getMetaId()+" "+path;
				
				 Runtime.getRuntime().exec(shell).waitFor();
				 
				 //rename the file
				 shell = "mv "+path+"/"+files.get(i).getMetaId()+" "+path+"/"+files.get(i).getName();
				 Runtime.getRuntime().exec(shell).waitFor();
				 
			}
		}
	}
	
	public static void processlocal(String userId, String folderid, String path) throws InterruptedException, IOException{
		//create folder
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
			System.out.println(f.getName());
		}
		List<com.echoii.cloud.platform.model.File> files = fs.listAllFile(userId, folderid, null, null, 0, 100);
		for(int i = 0 ; i < files.size(); ++i){
			System.out.println(files.get(i).getName());
			if(files.get(i).getType().equals("folder")){ //folder,recursion
				processlocal(userId,files.get(i).getId(),path+"/"+files.get(i).getName());
			}else{ //not folder, is file
				//copy the file to folder
				String shell = "cp /home/zdh/echoii/data/" + 
						 files.get(i).getMetaFolder() + "/" + files.get(i).getMetaId()+" "+path;
				System.out.println(shell);
				
				 Runtime.getRuntime().exec(shell).waitFor();
				 
				 //rename the file
				 shell = "mv "+path+"/"+files.get(i).getMetaId()+" "+path+"/"+files.get(i).getName();
				 System.out.println(shell);
				 Runtime.getRuntime().exec(shell).waitFor();
				 
			}
		}
	}
	
	private static void createUserTest(){
		User user = new User();
		//Date d = Calendar.getInstance().getTime();
		user.setId( HashUtil.getRandomId() );
		user.setName("hehehe");
		us.createUser(user);
	}
	
//	private static void getUserByNamehashTest(){
//		log.debug("getUserByNamehashTest begin");
//		User user = us.getByNameHash( "niuliguo");
//		log.debug( "user = " + user.getId() ); 		
//	}
}
	