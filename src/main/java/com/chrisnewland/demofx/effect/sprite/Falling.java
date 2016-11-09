/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Falling extends AbstractEffect
{
	private double[] bx;
	private double[] by;
	private double[] dx;
	private double[] dy;
	private Image[] sprites;

	private Image[] spriteChoices;

	private static final double ACCELERATION = 1.02;

	private boolean directionDown = true;

	public Falling(DemoConfig config)
	{
		super(config);

		// By Wilson Bentley - http://snowflakebentley.com/snowflakes.htm,
		// Public Domain,
		// https://commons.wikimedia.org/w/index.php?curid=1282792
		// sprite = ImageUtil.loadImageFromResources("star.png"));

		// Image snowflake =
		// ImageUtil.loadImageFromResources("BentleySnowFlakeResized.png"));
		//
		// spriteChoices = new Image[] {
		// snowflake };
		//
		// directionDown = true;

		Image balloon = ImageUtil.loadImageFromResources("balloon2.png");

		spriteChoices = new Image[] {
				balloon,
				ImageUtil.tintImage(balloon, Color.PURPLE.getHue() - 20),
				ImageUtil.tintImage(balloon, Color.PURPLE.getHue() - 40) };

		directionDown = false;

		init();
	}

	public Falling(DemoConfig config, Image[] spriteChoices, boolean directionDown)
	{
		super(config);

		this.spriteChoices = spriteChoices;

		this.directionDown = directionDown;

		init();
	}

	private void init()
	{
		if (itemCount == -1)
		{
			itemCount = 1024;
		}

		bx = new double[itemCount];
		by = new double[itemCount];
		dx = new double[itemCount];
		dy = new double[itemCount];
		sprites = new Image[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			respawn(i);
		}
	}

	private void respawn(int i)
	{
		bx[i] = width * precalc.getUnsignedRandom();

		dx[i] = 0;

		sprites[i] = spriteChoices[i % spriteChoices.length];

		if (directionDown)
		{
			by[i] = -height * precalc.getUnsignedRandom();
			dy[i] = precalc.getUnsignedRandom() / 2;
		}
		else
		{
			by[i] = height + height * precalc.getUnsignedRandom();
			dy[i] = -precalc.getUnsignedRandom() / 2;
		}
	}

	@Override
	public void renderForeground()
	{
		for (int i = 0; i < itemCount; i++)
		{
			moveSprite(i);

			drawSprite(i);
		}
	}

	private final void moveSprite(int i)
	{
		dx[i] += precalc.getSignedRandom() / 8;
		dy[i] *= ACCELERATION;

		bx[i] += dx[i];
		by[i] += dy[i];

		if (directionDown)
		{
			if (by[i] > height)
			{
				respawn(i);
			}
		}
		else
		{
			if (by[i] < -100)
			{
				respawn(i);
			}
		}
	}

	private final void drawSprite(int i)
	{
		gc.drawImage(sprites[i], bx[i], by[i]);
	}
}