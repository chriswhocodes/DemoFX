/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;

public class StarfieldSprite extends AbstractEffect
{
	private double[] starX;
	private double[] starY;
	private double[] starZ;

	private static final double SPEED = 0.01;
	private static final double MAX_DEPTH = 5;

	private boolean spin = true;

	private Image star = ImageUtil.loadImageFromResources("starshine.png");

	public StarfieldSprite(DemoConfig config)
	{
		super(config);

		if (itemCount == -1)
		{
			itemCount = 5000;
		}

		init();
	}

	public StarfieldSprite(DemoConfig config, int starCount, long startMillis, long stopMillis)
	{
		super(config);

		this.itemCount = starCount;
		this.effectStartMillis = startMillis;
		this.effectStopMillis = stopMillis;

		init();
	}

	private void init()
	{
		buildStars();
	}

	private void buildStars()
	{
		starX = new double[itemCount];
		starY = new double[itemCount];
		starZ = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			starX[i] = precalc.getSignedRandom() * halfWidth;
			starY[i] = precalc.getSignedRandom() * halfHeight;
			respawn(i);
		}
	}

	@Override
	public void renderForeground()
	{
		if (spin)
		{
			rotateCanvasAroundCentre(0.5);
		}

		for (int i = 0; i < itemCount; i++)
		{
			moveStar(i);

			plotStar(i);
		}
	}

	private final void moveStar(int i)
	{
		starZ[i] -= SPEED;
	}

	private final void respawn(int i)
	{
		starZ[i] = precalc.getUnsignedRandom() * MAX_DEPTH;
	}

	private double translateX(int i)
	{
		return starX[i] / starZ[i];
	}

	private double translateY(int i)
	{
		return starY[i] / starZ[i];
	}

	private final void plotStar(int i)
	{
		double x = halfWidth + translateX(i);
		double y = halfHeight + translateY(i);

		if (isOnScreen(x, y))
		{
			int size = (int) (8 / starZ[i]);
			gc.drawImage(star, x, y, size, size);
		}
		else
		{
			respawn(i);
		}
	}

	private final boolean isOnScreen(double x, double y)
	{
		if (spin)
		{
			double max = 1.4 * Math.max(width, height);

			return x > -max && y > -max && x < max && y < max;
		}
		else
		{
			return x > 0 && y > 0 && x < width && y < height;
		}
	}
}