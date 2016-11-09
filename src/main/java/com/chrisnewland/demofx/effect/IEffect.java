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

	void setStartMillis(long start);

	void setStopMillis(long stop);

	long getStartMillis();

	long getStopMillis();

	boolean isVisible(long elapsed);
}