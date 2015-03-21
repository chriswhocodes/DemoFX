/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class BallGrid extends AbstractEffect
{
	public BallGrid(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
	}

	@Override
	public void render()
	{
		String[] strings = TextUtil.createBallGrid("@chriswhocodes", gc);
		
		for (String str : strings)
		{
			System.out.println(str);
		}
		
		System.exit(-1);
	}
}