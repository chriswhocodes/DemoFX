/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.DemoConfig;

public class Squares extends BasicShape
{
	public Squares(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config, 4);
	}
}