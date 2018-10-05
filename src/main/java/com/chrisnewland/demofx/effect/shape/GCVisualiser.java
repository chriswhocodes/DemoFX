/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.measurement.gc.GCEvent;
import com.chrisnewland.demofx.measurement.gc.GCEventCollector;
import com.chrisnewland.demofx.measurement.gc.GCEventSeries;

import javafx.scene.paint.Color;

public class GCVisualiser extends AbstractEffect
{
	private double myWidth;
	private double myHeight;

	private GCEventCollector collector = new GCEventCollector();

	public GCVisualiser(DemoConfig config)
	{
		super(config);

		myWidth = width;
		myHeight = height / 4;
	}

	@Override
	public void renderForeground()
	{
		GCEventSeries series = collector.getSeries();

		List<GCEvent> events = series.getEvents();

		int count = events.size();

		double regionWidth = 16;

		double x = width - myWidth;
		double y = height - myHeight;

		gc.setStroke(Color.WHITE);
		gc.strokeRect(x, y, myWidth, myHeight);

		int max = (int) (myWidth / regionWidth);

		int start = 0;

		if (count > max)
		{
			start = count - max;
		}

		for (int i = start; i < count; i++)
		{
			GCEvent event = events.get(i);

			List<GCEvent.GCRegion> regions = event.getRegions();

			plotRegions(x, y, regionWidth, regions);

			x += regionWidth + 1;
		}
	}

	private void plotRegions(double x, double y, double regionWidth, List<GCEvent.GCRegion> regions)
	{
		int regionCount = regions.size();

		long totalRegionMax = 0;

		for (int j = 0; j < regionCount; j++)
		{
			totalRegionMax += regions.get(j).getAfterMax();
		}

		double regionHeightFactor = (double) myHeight / (double) totalRegionMax;

		double yStart = y;

		double halfRegionWidth = regionWidth / 2;

		for (int j = 0; j < regionCount; j++)
		{
			GCEvent.GCRegion region = regions.get(j);

			long beforeUsed = region.getBeforeUsed();
			long beforeCommitted = region.getBeforeCommitted();
			long beforeMax = region.getBeforeMax();

			long afterUsed = region.getAfterUsed();
			long afterCommitted = region.getAfterCommitted();
			long afterMax = region.getAfterMax();

			gc.setFill(Color.GREEN);
			gc.fillRect(x, yStart, halfRegionWidth, beforeMax * regionHeightFactor);
			gc.fillRect(x + halfRegionWidth, yStart, halfRegionWidth, afterMax * regionHeightFactor);

			gc.setFill(Color.YELLOW);
			gc.fillRect(x, yStart, halfRegionWidth, beforeCommitted * regionHeightFactor);
			gc.fillRect(x + halfRegionWidth, yStart, halfRegionWidth, afterCommitted * regionHeightFactor);

			gc.setFill(Color.RED);
			gc.fillRect(x, yStart, halfRegionWidth, beforeUsed * regionHeightFactor);
			gc.fillRect(x + halfRegionWidth, yStart, halfRegionWidth, afterUsed * regionHeightFactor);

			double maxHeight = Math.max(beforeMax,  afterMax);
			
			gc.setStroke(Color.WHITE);
			gc.strokeRect(x,  yStart, regionWidth, maxHeight);
			
			yStart += maxHeight * regionHeightFactor;
		}
	}
}