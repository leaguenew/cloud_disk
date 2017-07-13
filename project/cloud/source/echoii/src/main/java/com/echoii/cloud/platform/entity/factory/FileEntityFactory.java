package com.echoii.cloud.platform.entity.factory;

import java.util.ArrayList;
import java.util.List;

import com.echoii.cloud.platform.entity.HdcFileEntity;
import com.echoii.cloud.platform.entity.FileEntity;
import com.echoii.cloud.platform.entity.ShareFileEntity;
import com.echoii.cloud.platform.model.File;
import com.echoii.cloud.platform.model.Hdc;
import com.echoii.cloud.platform.model.HdcFile;
import com.echoii.cloud.platform.model.ShareLinkFile;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.DateUtil;

public class FileEntityFactory {
	
	public final static FileEntity getFileEntity(File file) {
		if (file == null) {
			return null;
		}
		FileEntity fe = new FileEntity();
		fe.setId(file.getId());
		fe.setCreateDate(DateUtil.parseDateToString(file.getCreateDate(),
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		fe.setUserId(file.getUserId());
		fe.setSize(CommUtil.formatSizeDisp(file.getSize()));
		fe.setFolderId(file.getFolderId());
		fe.setName(file.getName());
		fe.setStatus(file.getStatus());
		fe.setVersion(file.getVersion());
		fe.setType(file.getType());
		fe.setIsCurrentVersion(file.getIsCurrentVersion());
		fe.setMetaId(file.getMetaId());
		fe.setMetaFolder(file.getMetaFolder());
		fe.setLmf_date(DateUtil.parseDateToString(file.getLmf_date(),
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		fe.setPath(file.getPath());
		fe.setIdx(file.getIdx());
		return fe;
	}
	
	public final static FileEntity  getFileEntityLimited(File file){
		if(file == null){
			return null;
		}
		FileEntity fe = new FileEntity();
		fe.setId(file.getId());
		fe.setLmf_date(DateUtil.parseDateToString(file.getLmf_date(),
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		fe.setPath(file.getPath());
		fe.setSize(CommUtil.formatSizeDisp(file.getSize()));
		fe.setUserId(file.getUserId());
		fe.setType(file.getType());
		fe.setIdx(file.getIdx());
		return fe;
	}
	
	public final static ShareFileEntity getShareFileEntity(ShareLinkFile file) {
		if (file == null) {
			return null;
		}

		ShareFileEntity sfe = new ShareFileEntity();
		sfe.setId(file.getId());
		sfe.setCreateDate(DateUtil.parseDateToString(file.getCreateDate(),
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		sfe.setUserId(file.getUserId());
		sfe.setSize(CommUtil.formatSizeDisp(file.getSize()));
	
		sfe.setName(file.getName());
		sfe.setValidCode(file.getValidCode());
		
		sfe.setType(file.getType());
		
		sfe.setMetaId(file.getMetaId());
		sfe.setMetaFolder(file.getMetaFolder());
		
		return sfe;
	}
	
	public final static List<FileEntity> listFileEntity(List<File> files) {
		
		if (files == null || files.size() == 0) {
			return null;
		}

		List<FileEntity> fes = new ArrayList<FileEntity>();
		for(int i = 0; i < files.size(); ++i){
			fes.add(getFileEntity(files.get(i)));	
		}
		
		return fes;
	}
	
	public final static HdcFileEntity getDeviceFileEntity(HdcFile hdcfile){
		if (hdcfile == null) {
			return null;
		}
		HdcFileEntity hfe = new HdcFileEntity();
		hfe.setId(hdcfile.getId());
		hfe.setCreateDate(DateUtil.parseDateToString(hdcfile.getCreateDate(),
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		hfe.setSize(CommUtil.formatSizeDisp(hdcfile.getSize()));
		hfe.setFolderId(hdcfile.getFolderId());
		hfe.setName(hdcfile.getName());
		hfe.setStatus(hdcfile.getStatus());
		hfe.setVersion(hdcfile.getVersion());
		hfe.setType(hdcfile.getType());
		hfe.setIsCurrentVersion(hdcfile.getIsCurrentVersion());
		hfe.setMetaId(hdcfile.getMetaId());
		hfe.setMetaFolder(hdcfile.getMetaFolder());
		hfe.setLmf_date(DateUtil.parseDateToString(hdcfile.getLmf_date(),
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		hfe.setPath(hdcfile.getPath());
		hfe.setIdx(hdcfile.getIdx());
		return hfe;
	}
	
}
