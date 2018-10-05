package com.chrisnewland.demofx.measurement.gc;

import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sun.management.GarbageCollectionNotificationInfo;

public class GCEvent
{
	private String action;
	private String name;
	private long duration;

	private List<GCRegion> regions;

	public String getAction()
	{
		return action;
	}

	public String getName()
	{
		return name;
	}

	public long getDuration()
	{
		return duration;
	}

	public List<GCRegion> getRegions()
	{
		return regions;
	}

	public static class GCRegion
	{
		private String name;
		
		private long beforeUsed;
		private long beforeCommitted;
		private long beforeMax;
		
		private long afterUsed;
		private long afterCommitted;
		private long afterMax;
		
		public GCRegion(String name, MemoryUsage before, MemoryUsage after)
		{
			this.name = name;
			
			this.beforeUsed = before.getUsed();
			this.beforeCommitted =  before.getCommitted();
			this.beforeMax = before.getMax();
			
			this.afterUsed = after.getUsed();
			this.afterCommitted =  after.getCommitted();
			this.afterMax = after.getMax();
		}

		public String getName()
		{
			return name;
		}

		public long getBeforeUsed()
		{
			return beforeUsed;
		}

		public long getBeforeCommitted()
		{
			return beforeCommitted;
		}

		public long getBeforeMax()
		{
			return beforeMax;
		}

		public long getAfterUsed()
		{
			return afterUsed;
		}

		public long getAfterCommitted()
		{
			return afterCommitted;
		}

		public long getAfterMax()
		{
			return afterMax;
		}

		@Override
		public String toString()
		{
			return "GCRegion [name=" + name + ", beforeUsed=" + beforeUsed + ", beforeCommitted=" + beforeCommitted + ", beforeMax="
					+ beforeMax + ", afterUsed=" + afterUsed + ", afterCommitted=" + afterCommitted + ", afterMax=" + afterMax
					+ "]";
		}
	}
	
	private static Set<String> ignoreRegions = new HashSet<>();
	
	static
	{
		ignoreRegions.add("Code Cache");
		ignoreRegions.add("Metaspace");
		ignoreRegions.add("Compressed Class Space");
		ignoreRegions.add("PS Survivor Space");
	}
	
	public GCEvent(GarbageCollectionNotificationInfo info)
	{
		this.action = info.getGcAction();
		this.name = info.getGcName();
		this.duration = info.getGcInfo().getDuration();
	
		regions = new ArrayList<>();
		
		Map<String, MemoryUsage> memBefore = info.getGcInfo().getMemoryUsageBeforeGc();
		Map<String, MemoryUsage> memAfter = info.getGcInfo().getMemoryUsageAfterGc();

		for (Entry<String, MemoryUsage> entry : memAfter.entrySet())
		{
			String regionName = entry.getKey();
			
			if (ignoreRegions.contains(regionName))
			{
				continue;
			}

			MemoryUsage before = memBefore.get(regionName);
			
			MemoryUsage after = entry.getValue();
			
			GCRegion region = new GCRegion(regionName, before, after);
			
			regions.add(region);					
		}
	}

	@Override
	public String toString()
	{
		return "GCEvent [action=" + action + ", name=" + name + ", duration=" + duration + ", regions=" + regions + "]";
	}
}
