/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fade;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public abstract class AbstractFadeEffect extends AbstractEffect
{
	private Color colour;
	private long duration;
	private long start;
	private boolean fadeIn = true;

	public AbstractFadeEffect(DemoConfig config, Color colour, boolean fadeIn, long duration)
	{
		super(config);
		init(colour, fadeIn, duration);
	}

	private void init(Color colour, boolean fadeIn, long duration)
	{
		this.colour = colour;
		this.fadeIn = fadeIn;
		this.duration = duration;
	}

	@Override
	public void renderForeground()
	{
		long now = System.currentTimeMillis();

		if (start == 0)
		{
			start = now;
		}

		long elapsed = now - start;

		if (elapsed > duration)
		{
			effectFinished = true;
		}

		double alpha = (double) elapsed / (double) duration;

		alpha = precalc.clampDouble(alpha, 0, 1);

		if (fadeIn)
		{
			alpha = 1 - alpha;
		}

		gc.setGlobalAlpha(alpha);

		fillBackground(colour);		
	}
}