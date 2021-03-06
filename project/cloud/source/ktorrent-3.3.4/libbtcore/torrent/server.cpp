/***************************************************************************
 *   Copyright (C) 2005 by Joris Guisson                                   *
 *   joris.guisson@gmail.com                                               *
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
#include "server.h"
#include <QStringList>
#include <QHostAddress>
#include <qsocketnotifier.h>
#include <net/socket.h>
#include <mse/streamsocket.h>
#include <util/sha1hash.h>
#include <util/log.h>
#include <util/functions.h>
#include <net/portlist.h>
#include <mse/encryptedserverauthenticate.h>
#include <peer/peermanager.h>
#include <peer/serverauthenticate.h>
#include <peer/authenticationmonitor.h>
#include <peer/accessmanager.h>

#include "globals.h"
#include "torrent.h"




namespace bt
{

	

	Server::Server(Uint16 port) : sock(0),sn(0),port(0)
	{
		changePort(port);
		encryption = false;
		allow_unencrypted = true;
	}


	Server::~Server()
	{
		delete sn;
		delete sock;
	}

	bool Server::isOK() const
	{
		return sock && sock->ok();
	}

	void Server::changePort(Uint16 p)
	{
		if (p == port)
			return;

		if (sock && sock->ok())
			Globals::instance().getPortList().removePort(port,net::TCP);
		
		port = p;
		delete sock;
		sock = 0;
		delete sn; 
		sn = 0;
		
		QString iface = NetworkInterface();
		QString ip = NetworkInterfaceIPAddress(iface);
		QStringList possible;
		if (!ip.isEmpty())
			possible << ip;
		
		// If the first address doesn't work try AnyIPv6 and Any
		possible << QHostAddress(QHostAddress::AnyIPv6).toString() << QHostAddress(QHostAddress::Any).toString();
		
		foreach (const QString & addr,possible)
		{
			if (addr.contains(":")) // IPv6
				sock = new net::Socket(true,6);
			else
				sock = new net::Socket(true,4);
			
			if (sock->bind(addr,port,true))
			{
				Out(SYS_GEN|LOG_NOTICE) << "Bound to " << addr << ":" << port << endl;
				break;
			}
			
			delete sock;
			sock = 0;
		}
		
		if (sock)
		{
			sock->setNonBlocking();
			sn = new QSocketNotifier(sock->fd(),QSocketNotifier::Read,this);
			connect(sn,SIGNAL(activated(int)),this,SLOT(readyToAccept(int)));
			Globals::instance().getPortList().addNewPort(port,net::TCP,true);
		}
	}

	void Server::addPeerManager(PeerManager* pman)
	{
		peer_managers.append(pman);
	}
	
	void Server::removePeerManager(PeerManager* pman)
	{
		peer_managers.removeAll(pman);
	}

	void Server::readyToAccept(int )
	{
		if (!sock)
			return;

		net::Address addr;
		int fd = sock->accept(addr);
		if (fd > 0)
			newConnection(fd);
	}
	
	void Server::newConnection(int socket)
	{
		mse::StreamSocket* s = new mse::StreamSocket(socket,sock->isIPv4() ? 4 : 6);
		if (peer_managers.count() == 0)
		{
			s->close();
			delete s;
		}
		else
		{
			if (!AccessManager::instance().allowed(s->getRemoteAddress()))
			{
				Out(SYS_CON|LOG_DEBUG) << "A client with a blocked IP address ("<< s->getRemoteIPAddress() << ") tried to connect !" << endl;
				delete s;
				return;
			}
			
			ServerAuthenticate* auth = 0;
			
			if (encryption)
				auth = new mse::EncryptedServerAuthenticate(s,this);
			else
				auth = new ServerAuthenticate(s,this);
			
			AuthenticationMonitor::instance().add(auth);
		}
	}
	
	void Server::close()
	{
		delete sock;
		sock= 0;
		delete sn;
		sn = 0;
	}

	Uint16 Server::getPortInUse() const
	{
		return port;
	}

	PeerManager* Server::findPeerManager(const SHA1Hash & hash)
	{
		QList<PeerManager*>::iterator i = peer_managers.begin();
		while (i != peer_managers.end())
		{
			PeerManager* pm = *i;
			if (pm && pm->getTorrent().getInfoHash() == hash)
			{
				if (!pm->isStarted())
					return 0;
				else
					return pm;
			}
			i++;
		}
		return 0;
	}
	
	bool Server::findInfoHash(const SHA1Hash & skey,SHA1Hash & info_hash)
	{
		Uint8 buf[24];
		memcpy(buf,"req2",4);
		QList<PeerManager*>::iterator i = peer_managers.begin();
		while (i != peer_managers.end())
		{
			PeerManager* pm = *i;
			memcpy(buf+4,pm->getTorrent().getInfoHash().getData(),20);
			if (SHA1Hash::generate(buf,24) == skey)
			{
				info_hash = pm->getTorrent().getInfoHash();
				return true;
			}
			i++;
		}
		return false;
	}
	
	
	void Server::enableEncryption(bool allow_unencrypted)
	{
		encryption = true;
		this->allow_unencrypted = allow_unencrypted;
	}	
	
	void Server::disableEncryption()
	{
		encryption = false;
	}
}

#include "server.moc"
