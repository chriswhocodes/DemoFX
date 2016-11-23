/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fade;

import com.chrisnewland.demofx.DemoConfig;

import javafx.scene.paint.Color;

public class FadeOutEffect extends AbstractFadeEffect
{
	public FadeOutEffect(DemoConfig config)
	{
		super(config, Color.BLACK, false, 5000);
	}

	public FadeOutEffect(DemoConfig config, Color colour, long duration)
	{
		super(config, colour, false, duration);
	}
}