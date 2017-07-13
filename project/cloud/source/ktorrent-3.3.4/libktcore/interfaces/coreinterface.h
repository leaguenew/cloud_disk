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
 *   51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.             *
 ***************************************************************************/
#ifndef KTCOREINTERFACE_H
#define KTCOREINTERFACE_H

#include <kurl.h>
#include <qobject.h>
#include <util/constants.h>
#include <ktcore_export.h>

namespace bt
{
	class TorrentInterface;
}

namespace kt
{
	///Stats struct
	struct CurrentStats
	{
		bt::Uint32 download_speed;
		bt::Uint32 upload_speed;
		bt::Uint64 bytes_downloaded;
		bt::Uint64 bytes_uploaded;
	};
	
	class QueueManager;
	class GroupManager;
	class DBus;
	

	/**
	 * @author Joris Guisson
	 * @brief Interface for plugins to communicate with the application's core
	 *
	 * This interface provides the plugin with the functionality to modify
	 * the applications core, the core is responsible for managing all
	 * TorrentControl objects.
	*/
	class KTCORE_EXPORT CoreInterface : public QObject
	{
		Q_OBJECT
	public:
		CoreInterface();
		virtual ~CoreInterface();

		/**
		 * Set whether or not we should keep seeding after
		 * a download has finished.
		 * @param ks Keep seeding yes or no
		 */
		virtual void setKeepSeeding(bool ks) = 0;
	
		/**
		 * Change the data dir. This involves copying
		 * all data from the old dir to the new.
		 * This can offcourse go horribly wrong, therefore
		 * if it doesn't succeed it returns false
		 * and leaves everything where it supposed to be.
		 * @param new_dir The new directory
		 */
		virtual bool changeDataDir(const QString & new_dir) = 0;

		/**
		 * Start all, takes into account the maximum number of downloads.
		 */
		virtual void startAll() = 0;

		/**
		 * Stop all torrents.
		 */
		virtual void stopAll() = 0;

		/**
		 * Start a torrent, takes into account the maximum number of downloads.
		 * @param tc The TorrentControl
	 	 */
		virtual void start(bt::TorrentInterface* tc) = 0;
		
		/**
		 * Start a list of torrents.
		 * @param todo The list of torrents
		 */
		virtual void start(QList<bt::TorrentInterface*> & todo) = 0;

		/**
		 * Stop a torrent, may start another download if it hasn't been started.
		 * @param tc The TorrentControl
		 * @param user true if user stopped the torrent, false otherwise
		 */
		virtual void stop(bt::TorrentInterface* tc) = 0;
		
		/**
		 * Stop a list of torrents.
		 * @param todo The list of torrents
		 */
		virtual void stop(QList<bt::TorrentInterface*> & todo) = 0;

		/// Get CurrentStats structure
		virtual CurrentStats getStats() = 0;

		/**
		 * Switch the port
		 * @param port The new port
		 * @return true if we can, false otherwise
		 */
		virtual bool changePort(bt::Uint16 port) = 0;

		///  Get the number of torrents running (including seeding torrents).
		virtual bt::Uint32 getNumTorrentsRunning() const = 0;

		///  Get the number of torrents not running.
		virtual bt::Uint32 getNumTorrentsNotRunning() const = 0;

		/**
		 * Load a torrent file. Pops up an error dialog
		 * if something goes wrong. Will ask the user for a save location, or use
		 * the default.
		 * @param url The torrent file
		 * @param group Group to add torrent to
		 */
		virtual void load(const KUrl& url,const QString & group) = 0;
		
		/**
		 * Load a torrent file. Pops up an error dialog
		 * if something goes wrong. Will ask the user for a save location, or use
		 * the default. This will not popup a file selection dialog for multi file torrents.
		 * @param url The torrent file
		 * @param group Group to add torrent to
		 */
		virtual void loadSilently(const KUrl& url,const QString & group) = 0;

		
		/**
		 * Load a torrent using a byte array
		 * @param data Data of the torrent
		 * @param url URL of the torrent
		 * @param group Group to use
		 * @param savedir Directory to save to
		 */
		virtual void load(const QByteArray & data,const KUrl& url,const QString & group,const QString & savedir) = 0;
		
		/**
		 * Load a torrent using a byte array silently
		 * @param data Data of the torrent
		 * @param url URL of the torrent
		 * @param group Group to use
		 * @param savedir Directory to save to
		 */
		virtual void loadSilently(const QByteArray & data,const KUrl& url,const QString & group,const QString & savedir) = 0;
		
		/**
		 * Remove a download.This will delete all temp
		 * data from this TorrentControl And delete the
		 * TorrentControl itself. It can also potentially
		 * start a new download (when one is waiting to be downloaded).
		 * @param tc The torrent
		 * @param data_to Whether or not to delete the file data to
		 */
		virtual void remove(bt::TorrentInterface* tc,bool data_to) = 0;
		
		/**
		 * Find the next free torX dir.
		 * @return Path to the dir (including the torX part)
		 */
		virtual QString findNewTorrentDir() const = 0;
		
		/**
		 * Load an existing torrent, which has already a properly set up torX dir.
		 * @param tor_dir The torX dir
		 */
		virtual void loadExistingTorrent(const QString & tor_dir) = 0;
		
		/**
		 * Sets global paused state for all torrents (QueueManager) and stopps all torrents.
		 * No torrents will be automatically started/stopped.
		 */
		virtual void setPausedState(bool pause) = 0;
		
		/// Gets the globla paused state
		virtual bool getPausedState() = 0;
		
		/// Get the QueueManager
		virtual kt::QueueManager* getQueueManager() = 0;
		
		/// Get the GroupManager
		virtual kt::GroupManager* getGroupManager() = 0;
		
		/// Get a pointer to the external interface object (for dbus and scripting)
		virtual DBus* getExternalInterface() = 0;
		
		/// Apply all settings
		virtual void applySettings() = 0;
		
	signals:
		/**
		 * Seeing that when load returns the loading process may not have finished yet,
		 * and some code expects this. We emit this signal to notify that code of it.
		 * @param url The url which has been loaded
		 * @param success Whether or not it succeeded
		 * @param canceled Whether or not it was canceled by the user
		 */
		void loadingFinished(const KUrl & url,bool success,bool canceled);
		
		/**
		 * A bt::TorrentInterface was added
		 * @param tc 
		 */
		void torrentAdded(bt::TorrentInterface* tc);

	
		/**
		 * A TorrentInterface was removed
		 * @param tc
		 */
		void torrentRemoved(bt::TorrentInterface* tc);
		
		/**
		 * A TorrentInterface has finished downloading.
		 * @param tc
		 */
		void finished(bt::TorrentInterface* tc);

	    	/**
		 * Torrent download is stopped by error
		 * @param tc TorrentInterface
		 * @param msg Error message
		 */
		void torrentStoppedByError(bt::TorrentInterface* tc, QString msg);

		/**
		 * Signal emmitted when the settings have been changed in the settings dialog.
		 * Plugins interested in this should update their internal states.
		 * */
		void settingsChanged();
	};

}

#endif