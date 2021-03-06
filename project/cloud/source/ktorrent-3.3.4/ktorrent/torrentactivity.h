/***************************************************************************
 *   Copyright (C) 2009 by Joris Guisson                                   *
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

#ifndef TORRENTACTIVITY_H
#define TORRENTACTIVITY_H

#include <QSplitter>
#include <KTabWidget>
#include <interfaces/torrentactivityinterface.h>

namespace kt
{
	class ScanListener;
	class GUI;
	class Core;
	class View;
	class ViewManager;
	class GroupView;
	class QueueManagerWidget;
	class TabBarWidget;
	class Group;
	
	
	/**
	 * Activity which manages torrents.
	 */
	class TorrentActivity : public TorrentActivityInterface
	{
		Q_OBJECT
	public:
		TorrentActivity(Core* core,GUI* gui,QWidget* parent);
		virtual ~TorrentActivity();
		
		/// Get the group view
		GroupView* getGroupView() {return group_view;}
		
		/// Set the tab properties of a view 
		void setTabProperties(View* v,const QString & name,const QString & icon,const QString & tooltip);
		
		virtual void loadState(KSharedConfigPtr cfg);
		virtual void saveState(KSharedConfigPtr cfg);
		virtual const bt::TorrentInterface* getCurrentTorrent() const;
		virtual bt::TorrentInterface* getCurrentTorrent();
		virtual void updateActions();
		virtual void addToolWidget(QWidget* widget,const QString & text,const QString & icon,const QString & tooltip);
		virtual void removeToolWidget(QWidget* widget);
		virtual View* getCurrentView();
		
		/// Update the activity
		void update();
		
		/// A data scan was started
		void dataScanStarted(ScanListener* listener);
		
		/// A data scan was closed
		void dataScanClosed(ScanListener* listener);
		
	public slots:
		/**
		* Open a view
		* @param g The group to show in the view
		* */
		void openNewView(kt::Group* g);
		
		/**
		* Open a view
		* @param group_name Name of group to show in view
		* @param starting_up Wether or not we are starting up (and thus are loading existing views)
		*/
		void openView(const QString & group_name,bool starting_up);
		
		/**
		* Remove a View
		* @param v The View to remove
		* */
		void removeView(View* v);
		
		/**
		* Called by the ViewManager when the current torrent has changed
		* @param tc The torrent 
		* */
		void currentTorrentChanged(bt::TorrentInterface* tc);
		
		/**
			Hide or show the group view
		*/
		void setGroupViewVisible(bool visible);
		
		
	private slots:
		void newView();
		void closeTab();
		void currentTabPageChanged(int idx);
		
	private:
		View* newView(kt::Group* g);
		
	private:
		Core* core;
		GUI* gui;
		KTabWidget* tabs;
		ViewManager* view_man; 
		GroupView* group_view;
		QueueManagerWidget* qm;
		QSplitter* hsplit;
		QSplitter* vsplit;
		TabBarWidget* tool_views;
	};
}

#endif // TORRENTACTIVITY_H
