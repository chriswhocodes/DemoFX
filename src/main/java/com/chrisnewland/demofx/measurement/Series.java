/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.measurement;

import java.util.ArrayList;

public class Series
{
	private ArrayList<Long> times;
	private ArrayList<Long> values;
	private Range rangeTime;
	private Range rangeValue;

	public Series(int estimatedSize)
	{
		times = new ArrayList<>(estimatedSize);
		values = new ArrayList<>(estimatedSize);
		rangeTime = new Range();
		rangeValue = new Range();
	}
	
	public void add(long time, long value)
	{
		times.add(time);
		values.add(value);
		
		rangeTime.update(time);
		rangeValue.update(value);
	}

	public int size()
	{
		return times.size();
	}
	
	public ArrayList<Long> getTimes()
	{
		return times;
	}

	public ArrayList<Long> getValues()
	{
		return values;
	}

	public Range getRangeTime()
	{
		return rangeTime;
	}

	public Range getRangeValue()
	{
		return rangeValue;
	}
}