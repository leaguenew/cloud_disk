package com.echoii.cloud.platform.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import com.echoii.cloud.platform.model.TorrentFile;
import com.echoii.cloud.platform.model.TorrentTable;
import com.echoii.cloud.platform.service.HibernateDao;
import com.echoii.cloud.platform.service.OfflineDownloadCacheService;


public class OfflineDownloadCacheServiceImpl implements OfflineDownloadCacheService{
	
	private static volatile OfflineDownloadCacheService SERVICE = null;
	
	private HibernateDao dao = HibernateDao.getInstance();

	public static OfflineDownloadCacheService getInstance() {
		if (SERVICE == null) {
			synchronized (OfflineDownloadCacheServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new OfflineDownloadCacheServiceImpl();
				}
			}
		}
		return SERVICE;
	}

	OfflineDownloadCacheServiceImpl() {
		dao = HibernateDao.getInstance();
	}


	@Override
	public void createTorrentFile(TorrentFile torrentfile) {
		dao.insert(torrentfile);	
	}

	@Override
	public void createTorrentTable(TorrentTable torrenttable) {
		dao.insert(torrenttable);
		
	}

	@Override
	public TorrentTable getTorrentTableByMd5(String md5) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("torrentMd5", md5));
		List<?> result = dao.list(TorrentTable.class, criterions, null, 0, 1);
		if (result == null || result.size() < 1) {
			return null;
		}
		return (TorrentTable) result.get(0);
	}

	@Override
	public TorrentFile getTorrentFile(String userId, String md5) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("torrentMd5", md5));
		criterions.add(Restrictions.eq("userId", userId));
		
		List<?> result = dao.list(TorrentFile.class, criterions, null, 0, 1);
		
		if (result == null || result.size() < 1) {
			return null;
		}
		
		return (TorrentFile) result.get(0);
	}

	@Override
	public TorrentFile getTorrentFileByInfoHash(String userId, String infoHash) {
		List<Object> criterions = new ArrayList<Object>();

		criterions.add(Restrictions.eq("infoHash", infoHash));
		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.not(Restrictions.eq("status", TorrentFile.STATUS_DELETE)));
		
		List<?> result = dao.list(TorrentFile.class, criterions, null, 0, 1);
		
		if (result == null || result.size() < 1) {
			return null;
		}
		
		return (TorrentFile) result.get(0);
	}

	@Override
	public void updateTorrentFile(TorrentFile torrentfile) {
		dao.update(torrentfile);
		
	}

	@Override
	public TorrentTable getTorrentTableByHash(String infoHash) {
		List<SimpleExpression> criterions = new ArrayList<SimpleExpression>();

		criterions.add(Restrictions.eq("infoHash", infoHash));
		List<?> result = dao.list(TorrentTable.class, criterions, null, 0, 1);
		if (result == null || result.size() < 1) {
			return null;
		}
		return (TorrentTable) result.get(0);
	}

	@Override
	public List<TorrentFile> listTorrentFile(String userId) {
		List<Object> criterions = new ArrayList<Object>();

		criterions.add(Restrictions.eq("userId", userId));
		criterions.add(Restrictions.not(Restrictions.eq("status", TorrentFile.STATUS_DELETE)));
		
		List<TorrentFile> result = dao.list(TorrentFile.class, criterions, null, 0, 100);
		
		if (result == null || result.size() < 1) {
			return null;
		}
		
		return result;
	}

}
