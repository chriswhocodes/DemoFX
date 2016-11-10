/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.measurement;

public class Measurements
{
	private final int INITIAL_SIZE = 300 * 4; // 300s for demoFX3
	private final Runtime RUNTIME = Runtime.getRuntime();

	private Series heapSize = new Series(INITIAL_SIZE);
	private Series heapUsed = new Series(INITIAL_SIZE);
	private Series fps = new Series(INITIAL_SIZE);

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

		if (duration > 0L)
		{
			averageFPS = totalFrames / duration * 1000.0;
		}

		return averageFPS;
	}
}