/***************************************************************************
 *   Copyright (C) 2008 by Joris Guisson and Ivan Vasic                    *
 *   joris.guisson@gmail.com                                               *
 *   ivasic@gmail.com                                                      *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.          *
 ***************************************************************************/
#include <QTimer>
#include <QXmlStreamWriter>
#include <kapplication.h>
#include <settings.h>
#include <dht/dhtbase.h>
#include <net/socketmonitor.h>
#include <torrent/globals.h>
#include <torrent/server.h>
#include <torrent/choker.h>
#include <torrent/queuemanager.h>
#include <peer/peermanager.h>
#include <util/log.h>
#include <tracker/udptrackersocket.h>
#include <interfaces/coreinterface.h>
#include <interfaces/torrentfileinterface.h>
#include <interfaces/torrentinterface.h>
#include <interfaces/functions.h>
#include <util/sha1hash.h>
#include <util/functions.h>
#include "actionhandler.h"
#include "httpclienthandler.h"
#include "httpresponseheader.h"
#include "httpserver.h"

using namespace bt;

namespace kt 
{

	ActionHandler::ActionHandler(CoreInterface* core,HttpServer* server) 
		: WebContentGenerator(server,"/action", LOGIN_REQUIRED),core(core)
	{
	}
	
	
	ActionHandler::~ActionHandler()
	{
	}
	
	
	void ActionHandler::get(HttpClientHandler* hdlr, const QHttpRequestHeader& hdr)
	{
		KUrl url;
		url.setEncodedPathAndQuery(hdr.path());
		bool ret = false;
		const QMap<QString, QString> & params = url.queryItems();
		for (QMap<QString, QString>::const_iterator it = params.begin(); it != params.end();++it) 
		{
			ret = doCommand(it.key(),it.value());
			if (!ret)
				break;
		}
		
		HttpResponseHeader rhdr(200);
		server->setDefaultResponseHeaders(rhdr,"text/xml",true);
		
		QByteArray output_data;
		QXmlStreamWriter out(&output_data);
		out.setAutoFormatting(true);
		out.writeStartDocument();
		out.writeStartElement("result");
		QMap<QString, QString>::const_iterator it = params.begin();
		if(it.key() == "load_torrent" && ret){
		  QList<TorrentInterface*>::iterator i = core->getQueueManager()->begin();
		  bt::TorrentInterface* ti;
		  while(i != core->getQueueManager()->end()){
			ti = *i;
			i++;
		  }
		  const bt::TorrentStats & s = ti->getStats();
		  writeElement(out,"name",ti->getDisplayName());
		  writeElement(out,"info_hash",ti->getInfoHash().toString());
		  writeElement(out,"status",s.statusToString());
		  writeElement(out,"bytes_downloaded",BytesToString(s.bytes_downloaded));
		  writeElement(out,"bytes_uploaded",BytesToString(s.bytes_uploaded));
		  writeElement(out,"total_bytes",BytesToString(s.total_bytes));
		  writeElement(out,"total_bytes_to_download",BytesToString(s.total_bytes_to_download));
		  writeElement(out,"download_rate",BytesPerSecToString(s.download_rate));
		  writeElement(out,"upload_rate",BytesPerSecToString(s.upload_rate));
		  writeElement(out,"num_peers",QString::number(s.num_peers));
		  writeElement(out,"seeders",QString::number(s.seeders_connected_to));
		  writeElement(out,"seeders_total",QString::number(s.seeders_total));
		  writeElement(out,"leechers",QString::number(s.leechers_connected_to));
		  writeElement(out,"leechers_total",QString::number(s.leechers_total));
		  writeElement(out,"running",s.running ? "1" : "0");
		  writeElement(out,"percentage",QString::number(Percentage(s),'f',2));
		  writeElement(out,"num_files",QString::number(ti->getNumFiles()));
		}else{
		    out.writeCharacters(ret ? "OK" : "Failed");
		}
		
		//out.writeCharacters(ret ? "OK" : "Failed");
		out.writeEndElement();
		out.writeEndDocument();
		hdlr->send(rhdr,output_data);
	}
	
	void ActionHandler::writeElement(QXmlStreamWriter & out,const QString & name,const QString & value)
	{
		out.writeStartElement(name);
		out.writeCharacters(value);
		out.writeEndElement();
	}
	
	bool ActionHandler::doCommand(const QString & cmd,const QString & arg)
	{
		if (cmd == "dht")
			return dht(arg);
		else if (cmd ==" encryption")
			return encryption(arg);
		else if (cmd=="global_connection")
		{
			Settings::setMaxTotalConnections(arg.toInt());
			PeerManager::setMaxTotalConnections(Settings::maxTotalConnections());
			return true;
		}
		else if(cmd=="load_torrent" && arg.length() > 0)
		{
			core->loadSilently(QUrl::fromPercentEncoding(arg.toLocal8Bit()),QString());
			return true;
		}
		else if(cmd=="maximum_downloads")
		{
			int max = arg.toInt();
			core->getQueueManager()->setMaxDownloads(max);
			Settings::setMaxDownloads(max);
			return true;
		}
		else if(cmd=="maximum_seeds")
		{
			core->getQueueManager()->setMaxSeeds(arg.toInt());
			Settings::setMaxSeeds(arg.toInt());	
			return true;
		}
		else if(cmd=="maximum_connection_per_torrent")
		{
			PeerManager::setMaxConnections(arg.toInt());
			Settings::setMaxConnections(arg.toInt());
			return true;
		}
		else if(cmd=="maximum_upload_rate")
		{
			Settings::setMaxUploadRate(arg.toInt());
			net::SocketMonitor::setUploadCap( Settings::maxUploadRate() * 1024);
			return true;
		}
		else if(cmd=="maximum_download_rate")
		{
			Settings::setMaxDownloadRate(arg.toInt());
			net::SocketMonitor::setDownloadCap(Settings::maxDownloadRate()*1024);
			return true;
		}
		else if(cmd=="maximum_share_ratio")
		{
			Settings::setMaxRatio(arg.toInt());
			return true;
		}
		else if(cmd=="number_of_upload_slots")
		{
			Settings::setNumUploadSlots(arg.toInt());
			Choker::setNumUploadSlots(Settings::numUploadSlots());
			return true;
		}
		else if(cmd=="port")
		{
			Settings::setPort(arg.toInt());
			core->changePort(Settings::port());
		}
		else if(cmd=="port_udp_tracker")
		{
			Settings::setUdpTrackerPort(arg.toInt());
			UDPTrackerSocket::setPort(Settings::udpTrackerPort());
			return true;
		}
		else if (cmd=="remove")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			/*for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{   
				if(arg.toInt()==k)
				{
				    
					core->remove((*i), false);
					return true;
				}
			}*/
			while(i != core->getQueueManager()->end()){
			    bt::TorrentInterface* ti = *i;
				if(arg == ti->getInfoHash().toString())
				{	    
					core->remove((*i), false);
					return true;
				}
				i++;
			}
			
		}
		else if(cmd=="stopall" && !arg.isEmpty())
		{
			core->stopAll();
			return true;
		}
		else if(cmd=="startall" && !arg.isEmpty())
		{
			core->startAll();
			return true;
		}
		else if(cmd=="stop")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			/*for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{
				if(arg.toInt()==k)
				{
					core->stop(*i);
					return true;
				}
			}*/
			while(i != core->getQueueManager()->end()){
			    bt::TorrentInterface* ti = *i;
				if(arg == ti->getInfoHash().toString())
				{	    
					core->stop(*i);
					return true;
				}
				i++;
			}
		}
		else if(cmd=="start")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			/*for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{
				if(arg.toInt()==k)
				{
					core->start(*i);
					return true;
				}
			}*/
			while(i != core->getQueueManager()->end()){
			    bt::TorrentInterface* ti = *i;
				if(arg == ti->getInfoHash().toString())
				{	    
					core->start(*i);
					return true;
				}
				i++;
			}
		}
		else if (cmd.startsWith("file_"))
		{
			return file(cmd,arg);
		}
		else if (cmd.startsWith("shutdown"))
		{
			QTimer::singleShot(500,qApp,SLOT(quit()));
			return true;
		}
		
		return false;
	}
	
	void ActionHandler::post(HttpClientHandler* hdlr, const QHttpRequestHeader& hdr, const QByteArray& data)
	{
		//Q_UNUSED(data);
		get(hdlr,hdr);
	}
	
	bool ActionHandler::dht(const QString & arg)
	{
#ifdef ENABLE_DHT_SUPPORT
		if (arg =="start")
		{
			Settings::setDhtSupport(true);
		}
		else
		{
			Settings::setDhtSupport(false);
		}
					
		dht::DHTBase & ht = Globals::instance().getDHT();
		if (Settings::dhtSupport() && !ht.isRunning())
		{
			ht.start(kt::DataDir() + "dht_table",kt::DataDir() + "dht_key",Settings::dhtPort());
			return true;
		}
		else if (!Settings::dhtSupport() && ht.isRunning())
		{
			ht.stop();
			return true;
		}
		else if (Settings::dhtSupport() && ht.getPort() != Settings::dhtPort())
		{
			ht.stop();
			ht.start(kt::DataDir() + "dht_table",kt::DataDir() + "dht_key",Settings::dhtPort());
			return true;
		}
#endif
		return false;
	}
	
	bool ActionHandler::encryption(const QString & arg)
	{
		if (arg =="start")
		{
			Settings::setUseEncryption(true);
		}
		else
		{
			Settings::setUseEncryption(false);
		}

		if (Settings::useEncryption())
		{
			Globals::instance().getServer().enableEncryption(Settings::allowUnencryptedConnections());
		}
		else
		{
			Globals::instance().getServer().disableEncryption();
		}
		return true;
	}
	
	bool ActionHandler::file(const QString & cmd,const QString & arg)
	{
		QString torrent_num;
		QString file_num;
				//parse argument into torrent number and file number
		int separator_loc=arg.indexOf('-');
		QString parse = arg;
				
		torrent_num.append(parse.left(separator_loc));
		file_num.append(parse.right(parse.length()-(separator_loc+1)));

		if(cmd=="file_lp")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{
				if(torrent_num.toInt()==k)
				{
					TorrentFileInterface & file = (*i)->getTorrentFile(file_num.toInt());
					file.setPriority(LAST_PRIORITY);
					return true;
				}
			}
		}
		else if(cmd=="file_np")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{
				if(torrent_num.toInt()==k)
				{
					TorrentFileInterface & file = (*i)->getTorrentFile(file_num.toInt());
					file.setPriority(NORMAL_PRIORITY);
					return true;
				}
			}
		}
		else if(cmd=="file_hp")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{
				if(torrent_num.toInt()==k)
				{
					TorrentFileInterface & file = (*i)->getTorrentFile(file_num.toInt());
					file.setPriority(FIRST_PRIORITY);
					return true;
				}
			}
		}
		else if(cmd=="file_stop")
		{
			QList<TorrentInterface*>::iterator i= core->getQueueManager()->begin();
			for(int k=0; i != core->getQueueManager()->end(); i++, k++)
			{
				if(torrent_num.toInt()==k)
				{
					TorrentFileInterface & file = (*i)->getTorrentFile(file_num.toInt());
					file.setPriority(ONLY_SEED_PRIORITY);
					return true;
				}
			}
		}
		
		return false;
	}

}
