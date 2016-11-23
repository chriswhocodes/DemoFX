/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;

public class Sea extends AbstractEffect
{
	private Image image;

	private double imgWidth;
	private double imgHeight;

	private double angle = 0.0;
	private static final double DELTA = 2.0;
	private static double AMPLITUDE_X = 24.0;
	private static double AMPLITUDE_Y = 32.0;

	private double xStart;
	private double xStop;
	private double xStep;

	private double yStart;
	private double yStop;
	private double yStep;

	public enum SeaMode
	{
		CUBES, HEARTS
	}

	public Sea(DemoConfig config)
	{
		super(config);

		init(SeaMode.CUBES);
	}

	public Sea(DemoConfig config, SeaMode mode)
	{
		super(config);

		init(mode);
	}

	private void init(SeaMode mode)
	{
		imgWidth = 64;
		imgHeight = imgWidth;
		
		xStart = -(imgWidth + AMPLITUDE_X);
		xStop = width + imgWidth + AMPLITUDE_X;
		xStep = imgWidth / 2;

		yStart = height / 3;
		yStop = height + imgHeight + AMPLITUDE_Y;
		yStep = imgHeight / 1.5;

		switch (mode)
		{
		case CUBES:
			image = ImageUtil.makeCubes(imgWidth, imgHeight);
			break;
			
		case HEARTS:
			image = ImageUtil.makeHearts(imgWidth, imgHeight);
			break;
		}
	}

	@Override
	public void renderForeground()
	{
		move();

		plotTiles();
	}

	private void move()
	{
		angle += DELTA;

		if (angle >= 360)
		{
			angle -= 360;
		}

		// image = SpriteUtil.recolourImage(image);
	}

	private final void plotTiles()
	{
		int count = 0;

		for (double y = yStart; y < yStop; y += yStep)
		{
			for (double x = xStart; x < xStop; x += xStep)
			{
				double aa = imgWidth + x + y + angle;

				double yPos = y + (AMPLITUDE_Y + precalc.sin(x / 5 + 720 - angle) * 8) * precalc.cos(aa);

				double xPos = x + AMPLITUDE_X * precalc.sin(aa);

				gc.drawImage(image, xPos, yPos);

				count++;
			}
		}

		itemCount = count;
	}
}