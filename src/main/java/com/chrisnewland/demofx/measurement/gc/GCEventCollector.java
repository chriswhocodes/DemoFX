package com.chrisnewland.demofx.measurement.gc;

import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import com.sun.management.GarbageCollectionNotificationInfo;

public class GCEventCollector implements NotificationListener
{
	private GCEventSeries series = new GCEventSeries();

	public GCEventCollector()
	{
		List<GarbageCollectorMXBean> gcBeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();

		for (GarbageCollectorMXBean gcBean : gcBeans)
		{
			NotificationEmitter emitter = (NotificationEmitter) gcBean;

			emitter.addNotificationListener(this, null, null);
		}
	}

	@Override
	public void handleNotification(Notification notification, Object handback)
	{
		if (GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION.equals(notification.getType()))
		{
			GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo
					.from((CompositeData) notification.getUserData());

			GCEvent event = new GCEvent(info);
			
//			System.out.println(event);
			
			series.add(event);
		}
	}
	
	public GCEventSeries getSeries()
	{
		return series;
	}
}