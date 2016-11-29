/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

public interface IEffect
{
	void renderForeground();

	void start();
	
	void stop();

	void setStartOffsetMillis(long start);

	void setStopOffsetMillis(long stop);

	long getStartOffsetMillis();

	long getStopOffsetMillis();

	boolean isVisible(long elapsed);
}