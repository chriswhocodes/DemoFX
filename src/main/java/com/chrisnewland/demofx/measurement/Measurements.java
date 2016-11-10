/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.measurement;

import com.chrisnewland.demofx.DemoAnimationTimer;

public class Measurements
{
	// 300s for demoFX3
	private final static int INITIAL_SIZE = 300 * DemoAnimationTimer.SAMPLE_PER_SECOND;
	private final Runtime RUNTIME = Runtime.getRuntime();

	private final Series heapSize = new Series(INITIAL_SIZE);
	private final Series heapUsed = new Series(INITIAL_SIZE);
	private final Series fps = new Series(INITIAL_SIZE);

	private boolean isMeasuring = true;

	public void measure(long now, int framesPerSecond)
	{
		if (isMeasuring)
		{
			long heap = RUNTIME.totalMemory();
			long free = RUNTIME.freeMemory();
			long used = heap - free;

			heapSize.add(now, heap);
			heapUsed.add(now, used);
			fps.add(now, framesPerSecond);
		}
	}

	public Series getHeapSize()
	{
		return heapSize;
	}

	public Series getHeapUsed()
	{
		return heapUsed;
	}

	public Series getFps()
	{
		return fps;
	}

	public long getTotalFrameCount()
	{
		isMeasuring = false;

		if (fps.size() < INITIAL_SIZE) {
			System.out.println("Warning: too small initial size: " + INITIAL_SIZE + ", needed: " + fps.size());
		}

		long totalFPS = 0;

		for (long currentFPS : fps.getValues())
		{
			totalFPS += currentFPS;
		}

		return totalFPS;
	}

	public long getDurationMillis()
	{
		return fps.getRangeTime().getMax();
	}

	public double getAverageFPS()
	{
		double totalFrames = getTotalFrameCount();

		double duration = getDurationMillis();

		double averageFPS = 0.0;
        
		final double frameDuration = DemoAnimationTimer.UPDATE_STATS_MILLIS;

		if (duration > 0L)
		{
			averageFPS = totalFrames / duration * frameDuration;
		}

		return averageFPS;
	}
}