package com.chrisnewland.demofx.measurement.gc;

import java.util.ArrayList;
import java.util.List;

public class GCEventSeries
{
	private List<GCEvent> events = new ArrayList<>();
	
	private long maxDurationMinorGC = 0;
	private long maxDurationMajorGC = 0;
	
	public void add(GCEvent event)
	{
		events.add(event);
		
		if (event.getAction().toLowerCase().contains("major"))
		{
			maxDurationMajorGC = Math.max(maxDurationMajorGC, event.getDuration());
		}
		else
		{
			maxDurationMinorGC = Math.max(maxDurationMinorGC, event.getDuration());
		}		
	}

	public List<GCEvent> getEvents()
	{
		return events;
	}

	public long getMaxDurationMinorGC()
	{
		return maxDurationMinorGC;
	}

	public long getMaxDurationMajorGC()
	{
		return maxDurationMajorGC;
	}
}