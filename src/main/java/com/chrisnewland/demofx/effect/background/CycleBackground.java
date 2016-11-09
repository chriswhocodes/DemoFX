/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.background;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class CycleBackground extends AbstractEffect
{
	public CycleBackground(DemoConfig config)
	{
		super(config);
	}

	@Override
	public void renderForeground()
	{
		fillBackground(getCycleColour());
	}
}