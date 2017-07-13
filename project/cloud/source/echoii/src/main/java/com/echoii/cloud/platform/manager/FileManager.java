package com.echoii.cloud.platform.manager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.echoii.cloud.platform.entity.FileEntity;
import com.echoii.cloud.platform.entity.factory.FileEntityFactory;
import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.service.FileService;
import com.echoii.cloud.platform.service.impl.FileServiceImpl;
import com.echoii.cloud.platform.util.DataUtil;
import com.echoii.cloud.platform.util.DateUtil;
import com.echoii.cloud.platform.util.HashUtil;
/**
 * @author leaguenew
 *
 */
public class FileManager {

	private static volatile FileManager MANAGER;
	private FileService fileService;

	static long size = 0;

	public static FileManager getInstance() {
		if (MANAGER == null) {
			synchronized (FileManager.class) {
				if (MANAGER == null) {
					MANAGER = new FileManager();
				}
			}
		}
		return MANAGER;
	}

	public FileManager() {
		fileService = FileServiceImpl.getInstance();
	}

	/* check if there is a duplicated file under parent folder */
	public FileEntity createFolder(String userId, String folderId, String name) {

		File file = new File();
		file.setId(HashUtil.strHash(HashUtil.getRandomId()));
		file.setCreateDate(new Date());
		file.setMetaId(HashUtil.strHash(name));
		file.setName(name);
		file.setFolderId(folderId);
		file.setSize(0);
		file.setStatus(File.STATUS_NORMAL);
		file.setType(File.TYPE_FOLDER);
		file.setIdx(0);
		file.setUserId(userId);
		// file.setIsCurrentVersion(null);
		file.setLmf_date(new Date());
		if (folderId.equals("root")) {
			file.setPath("/" + folderId + "/" + name);
		} else {
			file.setPath(fileService.getFileById(folderId).getPath() + "/" + name);
		}
		file.setMetaFolder(DateUtil.parseDateToString(file.getCreateDate(),
				DateUtil.PATTERN_yyyyMM));

		if (fileService.getFileByName(file) == null) {
			fileService.createFile(file);
			return FileEntityFactory.getFileEntity(file);
		} else {
			return FileEntityFactory.getFileEntity(null);
		}

	}

	public boolean delete(String userId, String fileIdList, String removeSource) {

		String[] fileidlist = fileIdList.split(",");
		File file;
		System.out.println("[file delete manager]:fileIdList = "+fileIdList);
		System.out.println("[file delete manager]:fileidlist.length = "+fileidlist.length);
		/* delete a single file or file list */
		for (int i = 0; i < fileidlist.length; i++) {
			file = fileService.getFileById(fileidlist[i]);
			// check whether the file is in the database
			if (file != null) {
				// check that whether fileId belongs to user according to
				// userId
				if (userId.equals(file.getUserId())) {
					// UserDetail userdetail =
					// userService.getUserDetailByUserId(userId);
					// if(userdetail != null){
					// userdetail.setSize(userdetail.getSize() -
					// file.getSize());
					fileService.deleteFile(fileidlist[i], removeSource);
					// userService.updateUserDetail(userdetail);
					file.setStatus(File.STATUS_DELETE);
					// }else{
					// System.out.println("[file manager delete]:userdetail not exit, can not update the size of userdetail!");
					// return null;
					// }
				} else {
					System.out
							.println("[file manager delete]:the file does not belong to the user");
					return false;
				}
			} else {
				System.out
						.println("[file manager delete]:the file (fileId) does not exist");
				return false;
			}
		}//for			
		return true;
	}//delete

	public boolean recover(String userId, String fileIdList) {
		String [] fileidlist = fileIdList.split(",");
		File file;
		System.out.println("[file recover manager]:fileIdList = "+fileIdList);
		System.out.println("[file recover manager]:fileidlist.length = "+fileidlist.length);
		for(int i=0;i<fileidlist.length;i++){
			file = fileService.getFileById(fileidlist[i]);
			/* check whether the file is in the database */
			if(file!=null){
				/* check that whether fileId belongs to user according to userId */
				if(userId.equals(file.getUserId())){
					fileService.recoverFile(fileidlist[i]);
				}else{
					System.out
					.println("[file manager recover]:the file does not belong to the user");
					return false;
				}
			}else{
				System.out
				.println("[file manager recover]:the file (fileId) does not exist");
				return false;
			}
		}
		return true;
	}
	
	
	public FileEntity rename(String userId, String fileId, String newName) {

		File file = fileService.getFileById(fileId);
		// check whether the file is in the database
		if (file != null) {
			// check that whether fileId belongs to user according to userId
			if (userId.equals(file.getUserId())) {
				String oldName = file.getName();
				file.setName(newName);
				fileService.updateFile(file);
				//check whether there is a duplicated name file 
				if(fileService.getFileByName(file)==null){
					file.setLmf_date(new Date());
					file.setPath(file.getPath().substring(0, file.getPath().lastIndexOf('/')+1) + file.getName() );
				
					fileService.updateFile(file);
					return FileEntityFactory.getFileEntity(file);
				}else{
					file.setName(oldName);
					fileService.updateFile(file);
					return null;
				}
				
			} else {
				System.out
						.println("[file manager rename]:the file does not belong to the user");
				return null;
			}
		} else {
			System.out
					.println("[file manager rename]:the file (fileId) does not exist");
			return null;
		}
	}

	
	public FileEntity move(String userId, String fileId, String folderId) {

		File file = fileService.getFileById(fileId);
		// check whether the file is in the database
		if (file != null) {
			// check that whether fileId belongs to user according to userId
			if (userId == file.getUserId()) {
				File newfile = new File();
				newfile = file;

				Date d = Calendar.getInstance().getTime();
				newfile.setId(Long.toString(d.getTime()));
				newfile.setFolderId(folderId);
				if (folderId.equals("root")) {
					newfile.setPath("/root/" + newfile.getName());
				} else {
					newfile.setPath(fileService.getFileById(folderId).getPath()
							+ "/" + newfile.getName());
				}

				fileService.createFile(newfile);

				fileService.deleteFile(fileId,"false");

				return FileEntityFactory.getFileEntity(newfile);
			} else {
				System.out
						.println("[file manager move]:the file does not belong to the user");
				return null;
			}
		} else {
			System.out
					.println("[file manager move]:the file (fileId) does not exist");
			return null;
		}
	}

	public void copy(String userId, String fileId, String folderId) {

		File file = fileService.getFileById(fileId);
		// check whether the file in the database
		if (file != null) {
			// check that whether fileId belongs to user according to userId
			if (userId == file.getUserId()) {

				File newfile = new File();
				newfile = file;

				newfile.setId(HashUtil.getRandomId());
				newfile.setFolderId(folderId);

				if (folderId.equals("root")) {
					newfile.setPath("/root/" + newfile.getName());
				} else {
					newfile.setPath(fileService.getFileById(folderId).getPath()
							+ "/" + newfile.getName());
				}

				fileService.createFile(newfile);

			} else {
				System.out
						.println("[file manager copy]:the file does not belong to the user");
				return;
			}
		} else {
			System.out
					.println("[file manager copy]:the file (fileId) does not exist");
		}

	}

	public List<FileEntity> listAll(String userId, String folderId,
			String order, String orderColumn, int begin, int size) {

		List<File> filelist = null;
		filelist = fileService.listAllFile(userId, folderId, order, orderColumn,
				begin, size);
		System.out.println("file list size = " + filelist.size());

		List<FileEntity> fileentitylist = new ArrayList<FileEntity>();

		Iterator<File> filelistIt = filelist.iterator();
		while (filelistIt.hasNext()) {
			File f = filelistIt.next();
			fileentitylist.add(FileEntityFactory.getFileEntity(f));
		}
		// System.out.println("file entity list size = "+fileentitylist.size());

		return fileentitylist;
	}
	
	/**
	 * @param userId
	 * @param order
	 * @param orderColumn
	 * @param begin
	 * @param size
	 * @return List<FileEntity>
	 */
	
	/*
	public List<FileEntity> listAll(String userId, 
			String order, String orderColumn, int begin, int size) {

		List<File> filelist = null;
		filelist = fileService.listAllFile(userId,  order, orderColumn,
				begin, size);
		System.out.println("file list size = " + filelist.size());

		List<FileEntity> fileentitylist = new ArrayList<FileEntity>();

		Iterator<File> filelistIt = filelist.iterator();
		while (filelistIt.hasNext()) {
			File f = filelistIt.next();
			fileentitylist.add(FileEntityFactory.getFileEntityLimited(f));
		}
		// System.out.println("file entity list size = "+fileentitylist.size());

		return fileentitylist;
	}
	*/

	public List<FileEntity> listImage(String userId, String order,
			String orderColumn, int begin, int size) {

		List<File> imagelist = null;
		List<String> typeList = new ArrayList<String>();
		
		typeList = DataUtil.getImageType();
		System.out.println("[list image] typelist size = " + typeList.size());
		
		imagelist = fileService.listFileByTypes(userId, "root", typeList, order,
				orderColumn, begin, size);

		System.out.println("image list size = " + imagelist.size());

		List<FileEntity> imageentitylist = new ArrayList<FileEntity>();

		Iterator<File> imagelistIt = imagelist.iterator();
		while (imagelistIt.hasNext()) {
			File image = imagelistIt.next();
			imageentitylist.add(FileEntityFactory.getFileEntity(image));
		}

		System.out
				.println("image entity list size = " + imageentitylist.size());

		return imageentitylist;
	}

	public List<FileEntity> listDoc(String userId, String order,
			String orderColumn, int begin, int size) {

		List<File> doclist = null;
		List<String> typeList = new ArrayList<String>();
		
		typeList = DataUtil.getDocType();
		System.out.println("[list doc] typelist size = "+typeList.size());
		
		doclist = fileService.listFileByTypes(userId, "root", typeList, order,
				orderColumn, begin, size);

		System.out.println("[list doc] doc list size = " + doclist.size());

		List<FileEntity> docentitylist = new ArrayList<FileEntity>();

		Iterator<File> doclistIt = doclist.iterator();
		while (doclistIt.hasNext()) {
			File doc = doclistIt.next();
			docentitylist.add(FileEntityFactory.getFileEntity(doc));
		}

		System.out.println("[list doc] doc entity list size = " + docentitylist.size());

		return docentitylist;
	}

	public List<FileEntity> listVideo(String userId, String order,
			String orderColumn, int begin, int size) {

		List<File> videolist = null;
		List<String> typeList = new ArrayList<String>();
		
		typeList = DataUtil.getVideoType();
		System.out.println("[list video] typelist size = "+typeList.size());
		
		videolist = fileService.listFileByTypes(userId, "root", typeList, order,
				orderColumn, begin, size);

		System.out.println("video list size = " + videolist.size());

		List<FileEntity> videoentitylist = new ArrayList<FileEntity>();

		Iterator<File> videolistIt = videolist.iterator();
		while (videolistIt.hasNext()) {
			File video = videolistIt.next();
			videoentitylist.add(FileEntityFactory.getFileEntity(video));
		}

		System.out
				.println("video entity list size = " + videoentitylist.size());

		return videoentitylist;
	}

	public List<FileEntity> listMusic(String userId, String order,
			String orderColumn, int begin, int size) {
		List<File> musiclist = null;
		List<String> typeList = new ArrayList<String>();
		
		typeList = DataUtil.getMusicType();
		System.out.println("[list music] typelist size = "+typeList.size());
		
		musiclist = fileService.listFileByTypes(userId, "root", typeList, order,
				orderColumn, begin, size);

		System.out.println("music list size = " + musiclist.size());

		List<FileEntity> musicentitylist = new ArrayList<FileEntity>();

		Iterator<File> musiclistIt = musiclist.iterator();
		while (musiclistIt.hasNext()) {
			File music = musiclistIt.next();
			musicentitylist.add(FileEntityFactory.getFileEntity(music));
		}

		System.out
				.println("music entity list size = " + musicentitylist.size());

		return musicentitylist;
	}

	public List<FileEntity> listTorrent(String userId, String order,
			String orderColumn, int begin, int size) {
		List<File> torrentlist = null;
		List<String> typeList = new ArrayList<String>();
		
		typeList.add("torrent");
		
		torrentlist = fileService.listFileByTypes(userId, "root", typeList, order,
				orderColumn, begin, size);

		System.out.println("torrent list size = " + torrentlist.size());

		List<FileEntity> torrententitylist = new ArrayList<FileEntity>();

		Iterator<File> torrentlistIt = torrentlist.iterator();
		while (torrentlistIt.hasNext()) {
			File torrent = torrentlistIt.next();
			torrententitylist.add(FileEntityFactory.getFileEntity(torrent));
		}

		System.out.println("torrent entity list size = "
				+ torrententitylist.size());

		return torrententitylist;
	}

	public List<FileEntity> listOthers(String userId, String order,
			String orderColumn, int begin, int size) {
		List<File> otherslist = null;
		List<String> typeList = new ArrayList<String>();
		
		typeList = DataUtil.getOthersType();
		System.out.println("[list other] typelist size = "+typeList.size());
		
		
		otherslist = fileService.listFileByTypesExclude(userId, "root", typeList, order,
				orderColumn, begin, size);

		System.out.println("others list size = " + otherslist.size());

		List<FileEntity> othersentitylist = new ArrayList<FileEntity>();

		Iterator<File> otherslistIt = otherslist.iterator();
		while (otherslistIt.hasNext()) {
			File others = otherslistIt.next();
			othersentitylist.add(FileEntityFactory.getFileEntity(others));
		}

		System.out.println("others entity list size = "
				+ othersentitylist.size());

		return othersentitylist;
	}

	public List<FileEntity> listTrash(String userId, String order,
			String orderColumn, int begin, int size) {
		List<File> trashlist = null;

		trashlist = fileService.listTrushFile(userId, order, orderColumn, begin,
				size);

		System.out.println("trash list size = " + trashlist.size());

		List<FileEntity> trashentitylist = new ArrayList<FileEntity>();

		Iterator<File> trashlistIt = trashlist.iterator();
		while (trashlistIt.hasNext()) {
			File trash = trashlistIt.next();
			trashentitylist.add(FileEntityFactory.getFileEntity(trash));
		}

		System.out
				.println("trash entity list size = " + trashentitylist.size());

		return trashentitylist;
	}


	public List<FileEntity> syn(String userId, long date,int begin, int size ,String type) {
		List<File> file;
		if(type.equals("image")){
			file = fileService.listUpdateFile(userId, date, begin, size,DataUtil.getImageType());
		}else if(type.equals("music")){
			file = fileService.listUpdateFile(userId, date, begin, size,DataUtil.getMusicType());
		}else if(type.equals("video")){
			file = fileService.listUpdateFile(userId, date, begin, size,DataUtil.getVideoType());
		}else if(type.equals("doc")){
			file = fileService.listUpdateFile(userId, date, begin, size, DataUtil.getDocType());
		}else{
			file = fileService.listUpdateFile(userId, date, begin, size, null);
		}

		//List<File> file = fileService.listUpdateFile(userId, date, begin, size);
		// check whether the file is in the database
		if (file != null) {
			// check that whether fileId belongs to user according to userId
			List<FileEntity> trashentitylist = new ArrayList<FileEntity>();

			Iterator<File> trashlistIt = file.iterator();
			while (trashlistIt.hasNext()) {
				File trash = trashlistIt.next();
				trashentitylist.add(FileEntityFactory.getFileEntity(trash));
			}
			return trashentitylist;
		} else {
			System.out
					.println("[file manager syn]:the file (fileId) does not exist");
			return null;
		}
	}


}
