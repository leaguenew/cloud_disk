package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.ShareLinkFile;

public interface ShareService {
	
	public void createShareFile( ShareLinkFile file );
	
	public ShareLinkFile getShareFileById( String id );
	
	public void deleteShareFile(ShareLinkFile file);
	
	public void updateShareFile(ShareLinkFile file);
	
	public ShareLinkFile getShareFileByUserId( String userId );
	
	public List<ShareLinkFile> listShareFileByUserId(String userId,String order,int begin,int size);

}
