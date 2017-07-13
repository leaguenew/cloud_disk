package com.echoii.cloud.platform.service;

import java.util.List;
import com.echoii.cloud.platform.model.File;

public interface FileService {

	public void createFile( File file );
	
	public void updateFile( File file );
	
	public File getFileById( String id );
	
	public List<File> listAllSubFileByFolderId(String FolderId);
	
	public List<File> listFileByIds(Object[] list,String status,int begin,int size);
	
	public void deleteFile( String fileId, String removeSource );
	
	public List<File> listAllFile( String userId, String folderId, String order, String orderColumn, int begin, int size );
	
	public List<File> listFileByTypes(String userId, String parentId,List<String> typeList, String order, String orderColumn, int begin,int size);
	
	public List<File> listFileByTypesExclude(String userId, String folderId,
			List<String> typeList, String order, String orderColumn, int begin,int size);
	
	public List<File> listTrushFile( String userId, String order, String orderColumn, int begin, int size );
	
	public boolean isExistFileName(String userId, String id, String parentId,String fileName, String name);
	
	public File getFileByName(File file);
	
	public File getFileByName(String userId, String folderId, String name);
	
	public List<File> listUpdateFile(String userId , long date, int begin ,int size, List<String> typeList);

	public void recoverFile( String string );

	public List<File> listAllFile(String userId, String order,
			String orderColumn, int begin, int size);

	
}
