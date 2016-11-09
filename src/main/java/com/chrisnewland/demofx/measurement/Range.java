/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.measurement;

public class Range
{
	private long min;
	private long max;
	private boolean first;

	public void update(long value)
	{
		if (first)
		{
			min = value;
			max = value;
			first = false;
		}
		else
		{
			min = Math.min(min, value);
			max = Math.max(max, value);
		}
	}

	public long getMin()
	{
		return min;
	}

	public long getMax()
	{
		return max;
	}
}