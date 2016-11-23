/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.background;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public class ColourBackground extends AbstractEffect
{
	private Color colour;
	
	public ColourBackground(DemoConfig config)
	{
		super(config);
		init(Color.BLACK);
	}
	
	public ColourBackground(DemoConfig config, Color colour)
	{
		super(config);
		init(colour);
	}
	
	private void init(Color colour)
	{
		this.colour = colour;
	}

	@Override
	public void renderForeground()
	{
		fillBackground(colour);
	}
}