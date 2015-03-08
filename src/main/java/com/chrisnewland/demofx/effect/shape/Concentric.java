/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

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

	public Concentric(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
		OFFSCREEN = Math.max(halfWidth, halfHeight) * 1.2;
	}

	@Override
	protected void initialise()
	{
		itemName = "Images";
		space = width / RINGS / 1.7;

		radii = new double[RINGS];
		radii[0] = 1;

		ringX = new double[RINGS];
		ringY = new double[RINGS];

		images = new Image[] { new Image(getClass().getResourceAsStream("/tomato.png")),
				new Image(getClass().getResourceAsStream("/satsuma.png")),
				new Image(getClass().getResourceAsStream("/pear.png")),
				new Image(getClass().getResourceAsStream("/apple.png")),
				new Image(getClass().getResourceAsStream("/orange.png")),
				new Image(getClass().getResourceAsStream("/pineapple.png")),
				new Image(getClass().getResourceAsStream("/banana.png")),
				new Image(getClass().getResourceAsStream("/strawberry.png")) };

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
	public void render()
	{
		fillBackground();

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
	
	private final void fillBackground()
	{
		double redFraction = 2 + precalc.sin(angleClockwise);
		double greenFraction = 2 + precalc.sin(angleAntiClockwise + 10);
		double blueFraction = 2 + precalc.cos(angleClockwise);

		int lightness = 16;
		
		int red = (int) (redFraction * lightness);
		int green = (int) (greenFraction * lightness);
		int blue = (int) (blueFraction * lightness);

		fillBackground(red, green, blue);
	}
}