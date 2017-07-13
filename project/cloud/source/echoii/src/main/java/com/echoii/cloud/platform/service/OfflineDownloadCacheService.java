package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.TorrentFile;
import com.echoii.cloud.platform.model.TorrentTable;

public interface OfflineDownloadCacheService {
	
	public void createTorrentFile(TorrentFile torrentfile);
	
	public void updateTorrentFile(TorrentFile torrentfile);
	
	public void createTorrentTable(TorrentTable torrenttable);
	
	public TorrentTable getTorrentTableByMd5(String md5);
	
	public TorrentTable getTorrentTableByHash(String infoHash);
	
	public TorrentFile getTorrentFile(String userId ,String md5);
	
	public TorrentFile getTorrentFileByInfoHash(String userId, String infoHash);
	
	public List<TorrentFile> listTorrentFile(String userId);
}
