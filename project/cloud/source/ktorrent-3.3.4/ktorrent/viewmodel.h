/***************************************************************************
 *   Copyright (C) 2007 by Joris Guisson and Ivan Vasic                    *
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
#ifndef KTVIEWMODEL_H
#define KTVIEWMODEL_H

#include <QList>
#include <QAbstractTableModel>
#include <interfaces/torrentinterface.h>

namespace kt
{
	class View;
	class ViewDelegate;
	class Core;
	class Group;
	
	/**
	 * @author Joris Guisson
	 * 
	 * Model for the main torrent view
	*/
	class ViewModel : public QAbstractTableModel
	{
		Q_OBJECT
	public:
		ViewModel(Core* core,View* parent);
		virtual ~ViewModel();
		
		/**
		 * Set the Group to filter
		 * @param g The group
		 */
		void setGroup(Group* g);
		
		/**
		 * Update the model, checks if data has changed.
		 $ @param delegate The ViewDelegate, so we don't hide extended items
		 * @param force_resort Force a resort
		 * @return true if the model got resorted
		 */
		bool update(ViewDelegate* delegate,bool force_resort = false);
		
		/// Is a column a default column for an upload view
		bool defaultColumnForUpload(int column);
		
		/// Is a column a default column for a download view
		bool defaultColumnForDownload(int column);
		
		virtual int rowCount(const QModelIndex & parent = QModelIndex()) const;
		virtual int columnCount(const QModelIndex & parent = QModelIndex()) const;
		virtual QVariant headerData(int section, Qt::Orientation orientation,int role) const;
		virtual QVariant data(const QModelIndex & index, int role) const;
		virtual bool removeRows(int row,int count,const QModelIndex & parent);
		virtual bool insertRows(int row,int count,const QModelIndex & parent);
		virtual Qt::ItemFlags flags(const QModelIndex & index) const;
		virtual QStringList mimeTypes() const;
		virtual QMimeData* mimeData(const QModelIndexList &indexes) const;
		virtual bool setData(const QModelIndex & index,const QVariant & value,int role);
		virtual QModelIndex index(int row,int column,const QModelIndex & parent = QModelIndex()) const;
		virtual bool dropMimeData(const QMimeData *data,Qt::DropAction action,int row,int column,const QModelIndex &parent);
		virtual Qt::DropActions supportedDropActions() const;
		/**
		 * Emit the data changed signal
		 * @param row The row 
		 * @param col The column
		 */
		void emitDataChanged(int row,int col);
		
		/**
		 * Get a list of torrents from an index list.
		 * @param idx The index list
		 * @param tlist The torrent list to fill
		 */
		void torrentsFromIndexList(const QModelIndexList & idx,QList<bt::TorrentInterface*> & tlist);
		
		/**
		 * Get a torrent from a model index.
		 * @param index The model index
		 * @return The torrent if the index is valid and in the proper range, 0 otherwise
		 */
		const bt::TorrentInterface* torrentFromIndex(const QModelIndex & index) const;
		
		/**
		 * Get a torrent from a model index.
		 * @param index The model index
		 * @return The torrent if the index is valid and in the proper range, 0 otherwise
		 */
		bt::TorrentInterface* torrentFromIndex(const QModelIndex & index);
		
		/**
		 * Get a torrent from a row.
		 * @param index The row index
		 * @return The torrent if the index is valid and in the proper range, 0 otherwise
		 */
		bt::TorrentInterface* torrentFromRow(int index);
		
		/**
		 * Get all torrents
		 * @param tlist The list of torrents to fill
		 */
		void allTorrents(QList<bt::TorrentInterface*> & tlist) const;
		
	public slots:
		void addTorrent(bt::TorrentInterface* ti);
		void removeTorrent(bt::TorrentInterface* ti);
		void sort(int col, Qt::SortOrder order);
		
	signals:
		void sorted();
		
	public:
		enum Column
		{
			NAME = 0,
			STATUS,
			BYTES_DOWNLOADED,
			TOTAL_BYTES_TO_DOWNLOAD,
			BYTES_UPLOADED,
			DOWNLOAD_RATE,
			UPLOAD_RATE,
			ETA,
			SEEDERS,
			LEECHERS,
			PERCENTAGE,
			SHARE_RATIO,
			DOWNLOAD_TIME,
			SEED_TIME,
			DOWNLOAD_LOCATION	
		};
		
		struct Item
		{
			bt::TorrentInterface* tc;
			// cached values to avoid unneeded updates
			bt::TorrentStatus status;
			bt::Uint64 bytes_downloaded; 
			bt::Uint64 bytes_uploaded;
			bt::Uint64 total_bytes_to_download;
			bt::Uint32 download_rate;
			bt::Uint32 upload_rate;
			bt::Uint32 seeders_total;
			bt::Uint32 seeders_connected_to;
			bt::Uint32 leechers_total;
			bt::Uint32 leechers_connected_to;
			double percentage;
			float share_ratio;
			bt::Uint32 runtime_dl;
			bt::Uint32 runtime_ul;
			int eta;
			bool hidden;
			
			Item(bt::TorrentInterface* tc);

			bool update(int col,bool & modified);
			QVariant data(int col) const;
			QVariant color(int col) const;
			bool lessThan(int col,const Item* other) const;
			bool member(Group* group) const;
		};
			
	private:
		Core* core;
		View* view;
		QList<Item*> torrents;
		int sort_column;
		Qt::SortOrder sort_order;
		Group* group;
		int num_visible;
	};

}

#endif
