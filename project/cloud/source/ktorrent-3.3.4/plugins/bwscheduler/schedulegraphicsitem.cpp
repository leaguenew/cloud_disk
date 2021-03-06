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
#include <QPen>
#include <QBrush>
#include <QRectF>
#include <QCursor>
#include <QFontMetricsF>
#include <QGraphicsTextItem>
#include <QGraphicsSceneMouseEvent>
#include <klocale.h>
#include <util/log.h>
#include <util/functions.h>
#include "schedulegraphicsitem.h"
#include "schedule.h"
#include "weekscene.h"
#include "bwschedulerpluginsettings.h"

using namespace bt;

namespace kt
{

	ScheduleGraphicsItem::ScheduleGraphicsItem(ScheduleItem* item,const QRectF & r,const QRectF & constraints,WeekScene* ws)
	: QGraphicsRectItem(r),item(item),constraints(constraints),ws(ws)
	{
		setAcceptHoverEvents(true);
		setPen(QPen(Qt::black));
		setZValue(3);
		setHandlesChildEvents(true);
		
		if (item->paused)
		{
			setBrush(QBrush(SchedulerPluginSettings::pausedColor()));
		}
		else
		{
			setBrush(QBrush(SchedulerPluginSettings::itemColor()));
		}
		setFlag(QGraphicsItem::ItemIsSelectable,true);
		setFlag(QGraphicsItem::ItemIsMovable,true);
		text_item = 0;
		ready_to_resize = false;
		resizing = false;
		resize_edge = NoEdge;
	}


	ScheduleGraphicsItem::~ScheduleGraphicsItem()
	{
	}
	
	void ScheduleGraphicsItem::update(const QRectF & r)
	{
		setRect(r);
		setPos(QPointF(0,0));
		QString text;
		if (item->paused)
		{
			setBrush(QBrush(SchedulerPluginSettings::pausedColor()));
			text = i18n("Paused");
		}
		else
		{
			setBrush(QBrush(SchedulerPluginSettings::itemColor()));
			text = i18n("%1 Down\n%2 Up",
						BytesPerSecToString(item->download_limit * 1024),
						BytesPerSecToString(item->upload_limit * 1024));
		}
		
		if (text_item == 0)
			text_item = scene()->addText(text);
		else
			text_item->setPlainText(text);
		
		QFontMetricsF fm(text_item->font());
		text_item->setPos(QPointF(r.x(),r.y()));
		text_item->setZValue(4);
		text_item->setTextWidth(r.width());
		text_item->setParentItem(this);
		setToolTip(text);
		
		if (text_item->boundingRect().height() > r.height())
		{
			// Text is to big for rect
			delete text_item;
			text_item = 0;
		}
	}

	QVariant ScheduleGraphicsItem::itemChange(GraphicsItemChange change, const QVariant &value)
	{
		if (change == ItemPositionChange && scene()) 
		{
			QPointF new_pos = value.toPointF();
			if (!constraints.contains(new_pos))
			{
				qreal x = constraints.x() - boundingRect().x();
				if (new_pos.x() < x)
					new_pos.setX(x);
				else if (new_pos.x() + rect().width() > x + constraints.width())
					new_pos.setX(x + constraints.width() - rect().width());
				
				qreal y = constraints.y() - boundingRect().y();
				if (new_pos.y() < y)
					new_pos.setY(y);
				else if (new_pos.y() + rect().height() > y + constraints.height())
					new_pos.setY(y + constraints.height() - rect().height());
				
				return new_pos;
			}
		}

		return QGraphicsItem::itemChange(change, value);
	}
	
	void ScheduleGraphicsItem::mouseMoveEvent(QGraphicsSceneMouseEvent* event)
	{
		if (!resizing)
		{
			QGraphicsItem::mouseMoveEvent(event);
			ws->setShowGuidanceLines(true);
			QPointF sp = pos() + rect().topLeft();
			ws->updateGuidanceLines(sp.y(),sp.y() + rect().height());
			return;
		}
		
		qreal y = event->scenePos().y();
		// Use cursor y pos to determine size of new rect
		QRectF cur = rect();
		if (resize_edge == TopEdge)
		{
			if (y >= cur.y() + cur.height()) // rect becomes flipped
			{
				qreal yn = cur.y() + cur.height();
				if (yn < constraints.y())
					yn = constraints.y();
				
				qreal h = y - yn;
				cur.setY(yn);
				cur.setHeight(h);
				resize_edge = BottomEdge;
			}
			else
			{
				qreal yn = y < constraints.y() ? constraints.y() : y;
				qreal h = cur.height() + (cur.y() - yn);
				cur.setY(yn);
				cur.setHeight(h);
			}
		}
		else
		{
			if (y < cur.y()) // rect becomes flipped
			{
				qreal yn = y;
				if (yn < constraints.y())
					yn = constraints.y();
				
				qreal h = cur.y() - yn;
				cur.setY(yn);
				cur.setHeight(h);
				resize_edge = TopEdge;
			}
			else
			{
				cur.setHeight(y - cur.y());
				if (cur.y() + cur.height() >= constraints.y() + constraints.height())
					cur.setHeight(constraints.y() + constraints.height() - cur.y());
			}
		}
		
		setRect(cur);
		if (text_item)
			text_item->setPos(cur.x(),cur.y());
		
		ws->updateGuidanceLines(cur.y(),cur.y() + cur.height());
	}
	
	void ScheduleGraphicsItem::mousePressEvent(QGraphicsSceneMouseEvent* event)
	{
		if (!ready_to_resize || !(event->button() & Qt::LeftButton))
		{
			QGraphicsRectItem::mousePressEvent(event);
			// keep track of original position before the item is dragged
			original_pos = pos();
		}
		else
		{
			resizing = true;
			ws->setShowGuidanceLines(true);
			ws->updateGuidanceLines(rect().y(),rect().y() + rect().height());
		}
	}
	
	void ScheduleGraphicsItem::mouseReleaseEvent(QGraphicsSceneMouseEvent* event)
	{
		if (resizing)
		{
			resizing = false;
			ws->setShowGuidanceLines(false);
			ws->itemResized(item,rect());
		}
		else
		{
			QGraphicsRectItem::mouseReleaseEvent(event);
			
			if (event->button() & Qt::LeftButton)
			{
				if (original_pos != pos())
				{
					QPointF sp = pos() + rect().topLeft();
					ws->itemMoved(item,sp);
				}
			}
			ws->setShowGuidanceLines(false);
		}
	}
	
	void ScheduleGraphicsItem::hoverEnterEvent(QGraphicsSceneHoverEvent* event)
	{
		setCursor(Qt::SizeVerCursor);
		ready_to_resize = true;
		resize_edge = nearEdge(event->scenePos());
	}
	
	void ScheduleGraphicsItem::hoverLeaveEvent(QGraphicsSceneHoverEvent* event)
	{
		setCursor(Qt::ArrowCursor);
		ready_to_resize = false;
	}
	
	void ScheduleGraphicsItem::hoverMoveEvent(QGraphicsSceneHoverEvent* event)
	{
		resize_edge = nearEdge(event->scenePos());
		ready_to_resize = resize_edge != NoEdge;
		if (ready_to_resize)
			setCursor(Qt::SizeVerCursor);
		else
			setCursor(Qt::ArrowCursor);
	}
	
	ScheduleGraphicsItem::Edge ScheduleGraphicsItem::nearEdge(QPointF p)
	{
		qreal y = rect().y();
		qreal ye = y + rect().height();
		if (qAbs(p.y() - y) < 3)
			return TopEdge;
		else if (qAbs(p.y() - ye) < 3)
			return BottomEdge;
		else
			return NoEdge;
	}	

	
}
