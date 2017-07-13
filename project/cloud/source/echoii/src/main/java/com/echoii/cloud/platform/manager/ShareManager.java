package com.echoii.cloud.platform.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.ShareFileEntity;
import com.echoii.cloud.platform.entity.factory.FileEntityFactory;
import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.model.Group;
import com.echoii.cloud.platform.model.GroupFile;
import com.echoii.cloud.platform.model.GroupMember;
import com.echoii.cloud.platform.model.ShareLinkFile;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.GroupFileService;
import com.echoii.cloud.platform.service.GroupMemberService;
import com.echoii.cloud.platform.service.GroupService;
import com.echoii.cloud.platform.service.ShareService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.service.impl.GroupFileServiceImpl;
import com.echoii.cloud.platform.service.impl.GroupMemberServiceImpl;
import com.echoii.cloud.platform.service.impl.GroupServiceImpl;
import com.echoii.cloud.platform.service.impl.ShareServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.HashUtil;

public class ShareManager {
	private static Logger log = Logger.getLogger(ShareManager.class);
	private static volatile ShareManager MANAGER = null;
	private FileService fileService;
	private ShareService shareService;
	private GroupMemberService groupmemberService;
	private GroupFileService groupfileService;
	private UserService userService; 
	private GroupService groupService;
	private Config config = Config.getInstance();
	
	public static ShareManager getInstance() {
		if (MANAGER == null) {
			synchronized (ShareManager.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (MANAGER == null) {
					MANAGER = new ShareManager();
				}
			}
		}
		return MANAGER;
	}

	public ShareManager() {
		fileService = FileServiceImpl.getInstance();	
		shareService = ShareServiceImpl.getInstance();
		groupmemberService = GroupMemberServiceImpl.getInstance();
		groupfileService = GroupFileServiceImpl.getInstance();
		userService = UserServiceImpl.getInstance();
		groupService = GroupServiceImpl.getInstance();
	}
	
	
	public ShareFileEntity getShareFile(String id){
		ShareLinkFile slf = shareService.getShareFileById(id);
		if(slf == null){
			return null;
		}
		User user = userService.getUserById(slf.getUserId());
		ShareFileEntity sfe = FileEntityFactory.getShareFileEntity(slf);
	
		sfe.setEmail(user.getEmail());
		sfe.setUserName(user.getNickName());
		
		
		return sfe;
		
	}
	
	public void cancel_share(String id){
		ShareLinkFile slf = shareService.getShareFileById(id);
		if(slf != null){
			shareService.deleteShareFile(slf);
		}
	}
	
	public ShareFileEntity update_idcode(String file_id,String idcode){
		ShareLinkFile slf = shareService.getShareFileById(file_id);
		if(slf == null){
			return null;
		}
	
		slf.setValidCode(idcode);
		slf.setType("passwd");
		shareService.updateShareFile(slf);
		return FileEntityFactory.getShareFileEntity(slf);
		
	}
	
	//count size and number of files
	private  Node count(String userId, String[] ids){
		Node node = new Node();
		for(int i = 0 ; i < ids.length; ++i){
			File f = fileService.getFileById(ids[i]);
			
			if(f == null){
				System.out.println("System error!");
				return node;
			}
			if (f.getType().equals("folder")) {
				node = countFolder(userId, f.getId(), node);
			} else {
				node.size = node.size + f.getSize();
				node.number = node.number + 1;
			}
			
		}
		return node;
	}
	
	private Node countFolder(String userId, String folderId, Node node){
		List<File> files = fileService.listAllFile(userId, folderId, null, null, 0, 100);
		if(files == null){
			return node;
		}
	
		for (int i = 0; i < files.size(); ++i) {
			if (files.get(i).getType().equals("folder")) {
				System.out.println("This is folder!");
				countFolder(userId, files.get(i).getId(), node);
			} else {
				System.out.println("This is file!");
				node.size = node.size + files.get(i).getSize();
				node.number = node.number + 1;
			}
		}
		return node;
		
	}
	
	public ShareFileEntity share(String userId, String[] ids, String idcode) throws InterruptedException, IOException {
		
		Node node = count(userId,ids);
		
		if(node.size > config.getLongValue("package.file.size", 200) * 1024 * 1024
		     || node.number > config.getIntValue("package.file.number", 20)){
			System.out.println("too large or too many files");
			return null;
		}
		

		if (ids.length == 1) { // single file or folder	
			File file = fileService.getFileById(ids[0]); //file exit?	
			if(file == null){
				return null;
			}	
			ShareLinkFile sfile = shareService.getShareFileById(HashUtil.strHash(ids[0]));
			
			if(sfile != null){
				return FileEntityFactory.getShareFileEntity(sfile); //has shared
			}
			
			if (file.getType().equals("folder")) { //folder
				return shareFolder(userId, ids[0], file.getName() + "等", node.size, idcode);
			}

			ShareLinkFile nsfile = new ShareLinkFile();
			nsfile.setId(HashUtil.strHash(file.getId()));// hash the id
			nsfile.setCreateDate(new Date());
			nsfile.setMetaFolder(file.getMetaFolder());
			nsfile.setMetaId(file.getMetaId());
			nsfile.setName(file.getName());
			nsfile.setSize(file.getSize());

			nsfile.setUserId(file.getUserId());
			if (idcode != null) {
				if (!idcode.trim().equals("")) {
					nsfile.setValidCode(idcode.trim());
					nsfile.setType("passwd");
				}
			}
			shareService.createShareFile(nsfile);
			ShareFileEntity sfe = FileEntityFactory.getShareFileEntity(nsfile);
			User user = userService.getUserById(file.getUserId());
			if (user != null) {
				sfe.setUserName(user.getName());
				sfe.setEmail(user.getEmail());
			}

			return sfe;	

		} 

		//mutil files or folders
		String id = new String();

		for (int i = 0; i < ids.length; ++i) {
			id = id + ids[i];
		}

		id = HashUtil.strHash(id);

		ShareLinkFile sfile = shareService.getShareFileById(id);
		if (sfile != null) {// has shared
			return FileEntityFactory.getShareFileEntity(sfile);
		} 

		String name = null;

		String path = config.getStringValue("upload.batch.directory",
				"/mnt/moosefs/echoii/data/packagedownload");

		for (int i = 0; i < ids.length; ++i) {
			File f = fileService.getFileById(ids[i]);
			if (i == 0) {
				name = f.getName().replace("." + f.getType(), "") + "等";
				path = path + "/" + name;
				java.io.File ftemp = new java.io.File(path);

				if (!ftemp.exists()) {
					ftemp.mkdirs();
				}
			}
			if (!f.getType().equals("folder")) {
				String shell = "cp "
						+ config.getStringValue("upload.directory","/mnt/moosefs/echoii/data") 
						+ "/" + f.getMetaFolder() + "/" + f.getMetaId() + " " + path;
				Runtime.getRuntime().exec(shell).waitFor();
				shell = "mv " + path + "/" + f.getMetaId() + " " + path + "/" + f.getName();
				Runtime.getRuntime().exec(shell).waitFor();
				
			} else {
				processForShareFolder(userId, ids[i], path + "/" + f.getName());
			}
		}
		
		String metaId = HashUtil.getRandomId();
		String[] commands = {
				"/bin/sh",
				"-c",
				"cd "
						+ config.getStringValue("upload.batch.directory",
								"/mnt/moosefs/echoii/data/packagedownload")
						+ ";" + "zip -r " + metaId + " " + name + ";"
						+ "rm -rf " + name };

		Runtime.getRuntime().exec(commands);

		ShareLinkFile nfile = new ShareLinkFile();
		nfile.setId(id);
		nfile.setCreateDate(new Date());
		nfile.setName(name + ".zip");
		nfile.setMetaFolder("packagedownload");
		nfile.setMetaId(metaId + ".zip");
		nfile.setUserId(userId);
		nfile.setSize(node.size);
		if (idcode != null) {
			if (!idcode.trim().equals("")) {
				nfile.setValidCode(idcode.trim());
				nfile.setType("passwd");
			}
		}
		shareService.createShareFile(nfile);

		ShareFileEntity sfe = FileEntityFactory.getShareFileEntity(nfile);
		User user = userService.getUserById(nfile.getUserId());

		if (user != null) {
			sfe.setUserName(user.getName());
			sfe.setEmail(user.getEmail());
		}

		return sfe;

	}
	
	private ShareFileEntity shareFolder(String userId, String fileId, String name,long size,String idcode)
			throws InterruptedException, IOException {

		String path = config.getStringValue("upload.batch.directory","/mnt/moosefs/echoii/data/packagedownload") + "/" + name;

		processForShareFolder(userId, fileId, path);

		// The sehll command
		String metaId = HashUtil.getRandomId();
		String[] commands = {
				"/bin/sh",
				"-c",
				"cd "   + config.getStringValue("upload.batch.directory",
								"/mnt/moosefs/echoii/data/batchdownload") + ";"
						+ "zip -r " + metaId + " " + name + ";" 
						+ "rm -rf "+ name };

		Runtime.getRuntime().exec(commands);

		ShareLinkFile sfile = new ShareLinkFile();
		sfile.setId(HashUtil.strHash(fileId));
		sfile.setCreateDate(new Date());
		sfile.setName(name + ".zip");
		sfile.setMetaFolder("packagedownload");
		sfile.setMetaId(metaId + ".zip");
		sfile.setUserId(userId);
		sfile.setSize(size);
		
		if(idcode != null ){
			if(!idcode.trim().equals("")){
				sfile.setValidCode(idcode.trim());
				sfile.setType("passwd");
			}
		}

		shareService.createShareFile(sfile);


		ShareFileEntity sfe = FileEntityFactory.getShareFileEntity(sfile);
		User user = userService.getUserById(sfile.getUserId());
		if(user != null){
			sfe.setUserName(user.getName());
			sfe.setEmail(user.getEmail());
		}
		
		return sfe;

	}

	private void processForShareFolder(String userId, String folderId,String path) throws InterruptedException, IOException {

		java.io.File f = new java.io.File(path);

		if (!f.exists()) {
			f.mkdirs();
		}

		List<File> files = fileService.listAllFile(userId, folderId, null, null, 0, 100);

		for (int i = 0; i < files.size(); ++i) {
			
			if (files.get(i).getType().equals("folder")) { // folder,recursion
				log.debug("This is folder:" + files.get(i).getName());
				processForShareFolder(userId, files.get(i).getId(), path + "/" + files.get(i).getName());
			} else { // file
				String shell = "cp "
						+ config.getStringValue("upload.directory",
								"/mnt/moosefs/echoii/data") + "/"
						+ files.get(i).getMetaFolder() + "/"
						+ files.get(i).getMetaId() + " " + path;
				log.debug("This is shell:" + shell);
				Runtime.getRuntime().exec(shell).waitFor();

				// rename the file
				shell = "mv " + path + "/" + files.get(i).getMetaId() + " "
						+ path + "/" + files.get(i).getName();

				Runtime.getRuntime().exec(shell).waitFor();
			}
		}
	}

	public String packageDownload(String userId, String[] ids)
			throws InterruptedException, IOException {

		Node node = count(userId, ids);

		if (node.size > config.getLongValue("package.file.size", 200) * 1024 * 1024
				|| node.number > config.getIntValue("package.file.number", 20)) {
			System.out.println("two large or two many files");
			return null;
		}

		if (ids.length == 1) { // single file or folder
			File file = fileService.getFileById(ids[0]);
			if (file == null) {
				return null;
			}

			if (!file.getType().equals("folder")) { // file
				return config.getStringValue("upload.directory","/mnt/moosefs/echoii/data")
						+ "/" + file.getMetaFolder() + "/" + file.getMetaId();
			}
			return downladFolder(userId, ids[0], file.getName());

		}

		String name = null;
		String path = config.getStringValue("upload.temp.directory",
				"/data/echoii/tempdata/");
		for (int i = 0; i < ids.length; ++i) {
			File f = fileService.getFileById(ids[i]);
			if (i == 0) {
				name = f.getName().replace("." + f.getType(), "");
				path = path + "/" + name;

				java.io.File ftemp = new java.io.File(path);

				if (!ftemp.exists()) {
					ftemp.mkdirs();
				}
			}
			if (!f.getType().equals("folder")) {
				String shell = "cp "
						+ config.getStringValue("upload.directory","/mnt/moosefs/echoii/data") 
						+ "/" + f.getMetaFolder() + "/" + f.getMetaId() + " " + path;

				Runtime.getRuntime().exec(shell).waitFor();

				// rename the file
				shell = "mv " + path + "/" + f.getMetaId() + " " + path + "/" + f.getName();
				Runtime.getRuntime().exec(shell).waitFor();

			} else {
				processForPackageDownload(userId, ids[i],path + "/" + f.getName());
			}
		}

		String[] commands = {
				"/bin/sh",
				"-c",
				"cd "
						+ config.getStringValue("upload.temp.directory",
								"/data/echoii/tempdata/") + ";" + "zip -r "
						+ name + ".zip " + name + ";" + "rm -rf " + name };

		Runtime.getRuntime().exec(commands).waitFor();
		return config.getStringValue("upload.temp.directory",
				"/data/echoii/tempdata/") + "/" + name + ".zip";

	}

	private String downladFolder(String userId, String fileId, String name) throws InterruptedException, IOException {

		String path = config.getStringValue("upload.temp.directory",
				"/data/echoii/tempdata/") + "/" + name;

		processForShareFolder(userId, fileId, path);

		// The sehll command
		String[] commands = {
				"/bin/sh",
				"-c",
				"cd "
						+ config.getStringValue("upload.temp.directory",
								"/data/echoii/tempdata/") + ";" + "zip -r "
						+ name + ".zip " + name + ";" + "rm -rf " + name };

		Runtime.getRuntime().exec(commands).waitFor();

		return config.getStringValue("upload.temp.directory",
				"/data/echoii/tempdata/") + "/" + name + ".zip";

	}

	private void processForPackageDownload(String userId, String folderId, String path) throws InterruptedException, IOException {

		java.io.File f = new java.io.File(path);

		if (!f.exists()) {
			f.mkdirs();
		}

		List<File> files = fileService.listAllFile(userId, folderId, null, null, 0, 100);

		for (int i = 0; i < files.size(); ++i) {
			if (files.get(i).getType().equals("folder")) { // folder,recursion
				processForPackageDownload(userId, files.get(i).getId(), path + "/" + files.get(i).getName());
			} else { // file
			
				String shell = "cp " + config.getStringValue("upload.directory","/mnt/moosefs/echoii/data") 
						+ "/" + files.get(i).getMetaFolder() 
						+ "/" + files.get(i).getMetaId() + " " + path;
				Runtime.getRuntime().exec(shell).waitFor();
				shell = "mv " + path + "/" + files.get(i).getMetaId() 
						+ " " + path + "/" + files.get(i).getName();
				Runtime.getRuntime().exec(shell).waitFor();

			}
		}
	}
	
	public  List<ShareFileEntity> list_share_files(String userId,String order, int begin, int size){
		List<ShareLinkFile> slfile = shareService.listShareFileByUserId(userId, order, begin, size);
		
		if(slfile == null){
			return null;
		}
		
		List<ShareFileEntity> fileentitylist = new ArrayList<ShareFileEntity>();
		for(int i = 0 ; i < slfile.size(); ++i){
			ShareLinkFile f = slfile.get(i);
			fileentitylist.add(FileEntityFactory.getShareFileEntity(f));
		}

		return fileentitylist;
		
		
	}
	public boolean shareGroup(String userId, String groupId, String[] list, String groupFolderId) {
		int flag = 0;
		Group gp = groupService.getGroup(groupId, userId);
		if(gp != null){
			flag = 1;
		}
		GroupMember groupmember = groupmemberService.getGroupMember(groupId, userId);
	
		//is in this group?
		if(groupmember == null){
			return false;
		}
		
		if(groupFolderId == null){
			groupFolderId = "root";
		}

		for (int i = 0; i < list.length; ++i) {
			
			File file = fileService.getFileById(list[i]);
			
			GroupFile groupfile = new GroupFile();
			groupfile.setId(HashUtil.getRandomId());
			groupfile.setUserId(userId);
			groupfile.setFolderId(groupFolderId);
			groupfile.setGroupId(groupId);
			groupfile.setMetaFolder(file.getMetaFolder());
			groupfile.setMetaId(file.getMetaId());
			groupfile.setSize(file.getSize());
			groupfile.setType(file.getType());
			if(flag == 1){
				groupfile.setStatus(GroupFile.STATUS_PUBLISH); //admin
			}else{
				groupfile.setStatus(GroupFile.STATUS_PEND);//normal user
			}
			
			groupfile.setCreateDate(new Date());
			groupfileService.createGroupFile(groupfile);
			
			if(file.getType().equals("folder")){	
				shareFolderGroupService(userId, groupId, list[i], groupfile.getId(),flag);
			}
		}
		
		return true;
	}
	
	/*
	 *recursion for the folder or file 
	 **/
	
	private void shareFolderGroupService(String userId, String groupId, String folderId, String groupFolderId, int flag){
		
		List<File> files = fileService.listAllFile(userId, folderId, null, null, 0, 100);
		
		if(files == null ){
			return;
		}
		
		for(int i = 0 ; i < files.size(); ++i){
			
			GroupFile groupfile = new GroupFile();
			groupfile.setId(HashUtil.getRandomId());
			groupfile.setUserId(userId);
			groupfile.setFolderId(groupFolderId);
			groupfile.setGroupId(groupId);
			groupfile.setMetaFolder(files.get(i).getMetaFolder());
			groupfile.setMetaId(files.get(i).getMetaId());
			groupfile.setSize(files.get(i).getSize());
			groupfile.setType(files.get(i).getType());
			if(flag == 1){
				groupfile.setStatus(GroupFile.STATUS_PUBLISH); //admin
			}else{
				groupfile.setStatus(GroupFile.STATUS_PEND);//normal user
			}

			groupfile.setCreateDate(new Date());
			groupfileService.createGroupFile(groupfile);
			
			if(files.get(i).getType().equals("folder")){
				//recursion here
				shareFolderGroupService(userId,groupId,files.get(i).getId(),groupfile.getId(),flag);
			}
			
		}
	
	}
	
	
	private class Node{
		long size;
		int number;
	}
	
}
