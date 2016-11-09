/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;

public class Blur extends AbstractEffect
{
	private double radius = 1.0;
	private double direction = 0.3;

	private Image loaded;
	private GaussianBlur blur;

	public Blur(DemoConfig config)
	{
		super(config);

		init("/javafx.png");
	}

	public Blur(DemoConfig config, String filename)
	{
		super(config);

		init(filename);
	}

	private void init(String filename)
	{
		loaded = new Image(getClass().getResourceAsStream(filename));

		blur = new GaussianBlur(radius);

		gc.getCanvas().setEffect(blur);
	}

	@Override
	public void renderForeground()
	{
		radius += direction;

		if (radius > 50 || radius < 1)
		{
			direction = -direction;
			radius += direction;
		}

		gc.drawImage(loaded, -radius, -radius);

		blur.setRadius(radius);
	}
}