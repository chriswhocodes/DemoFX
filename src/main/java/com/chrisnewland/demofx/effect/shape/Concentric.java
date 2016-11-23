/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;

public class Concentric extends AbstractEffect
{
	private double angleClockwise = 0;
	private double angleAntiClockwise = 0;

	private double radii[];
	private double ringX[];
	private double ringY[];
	private int imageIndex[];
	private Image[] images;

	private boolean clockwise = true;

	private double space;

	private static final int RINGS = 10;
	private static final int PER_RING = 32;
	private static final double SPEED = 3.0;
	private static final double EACH_ANGLE = 360 / (double) PER_RING;
	private final double OFFSCREEN;

	public Concentric(DemoConfig config)
	{
		super(config);
		OFFSCREEN = Math.max(halfWidth, halfHeight) * 1.2;

		space = width / RINGS / 1.7;

		radii = new double[RINGS];
		radii[0] = 1;

		ringX = new double[RINGS];
		ringY = new double[RINGS];

		images = new Image[] {
				ImageUtil.loadImageFromResources("tomato.png"),
				ImageUtil.loadImageFromResources("/satsuma.png"),
				ImageUtil.loadImageFromResources("/pear.png"),
				ImageUtil.loadImageFromResources("/apple.png"),
				ImageUtil.loadImageFromResources("/orange.png"),
				ImageUtil.loadImageFromResources("/pineapple.png"),
				ImageUtil.loadImageFromResources("/banana.png"),
				ImageUtil.loadImageFromResources("/strawberry.png") };

		imageIndex = new int[RINGS];

		for (int i = 0; i < RINGS; i++)
		{
			ringX[i] = halfWidth;
			ringY[i] = halfHeight;
			imageIndex[i] = i % images.length;
		}

		itemCount = RINGS * PER_RING;
	}

	@Override
	public void renderForeground()
	{
		rotateRings();

		plotRings();
	}

	private final void rotateRings()
	{
		angleClockwise++;

		if (angleClockwise >= 360)
		{
			angleClockwise -= 360;
		}

		angleAntiClockwise--;

		if (angleAntiClockwise <= 0)
		{
			angleAntiClockwise += 360;
		}
	}

	private final void plotRings()
	{
		double lastRadius = 0;

		for (int i = 0; i < RINGS; i++)
		{
			if (radii[i] > 0 || lastRadius > space)
			{
				radii[i] += SPEED;

				if (radii[i] > OFFSCREEN)
				{
					respawnRing(i);
				}

				plotRing(i, clockwise ? angleClockwise : angleAntiClockwise);
			}

			clockwise = !clockwise;

			lastRadius = radii[i];
		}
	}

	private final void respawnRing(int ringIndex)
	{
		radii[ringIndex] = 1;
		ringX[ringIndex] = halfWidth;
		ringY[ringIndex] = halfHeight;
	}

	private void plotRing(int ringIndex, double angle)
	{
		double radius = radii[ringIndex];

		for (int i = 0; i < PER_RING; i++)
		{
			double sAngle = angle + EACH_ANGLE * i;

			double x = ringX[ringIndex] + radius * precalc.sin(sAngle);
			double y = ringY[ringIndex] + radius * precalc.cos(sAngle);

			gc.drawImage(images[imageIndex[ringIndex]], x, y);
		}
	}
}