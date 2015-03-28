/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Burst extends AbstractEffect
{
	private double angleClockwise = 0;
	private double angleAntiClockwise = 0;

	private double radii[];
	private double ringX[];
	private double ringY[];

	private boolean clockwise = true;

	private static final int BURSTS = 16;
	private static final int BURST_SECTORS = 32;
	private static final double SPEED = 3.0;
	private static final double EACH_ANGLE = 360 / (double) BURST_SECTORS;
	private static final double FADE_STEPS = 100;

	private final double MAX_RADIUS;
	private final double FADE_RADIUS;

	private Image image;

	public Burst(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);

		MAX_RADIUS = width / 4;
		FADE_RADIUS = MAX_RADIUS - FADE_STEPS;
	}

	@Override
	protected void initialise()
	{
		radii = new double[BURSTS];
		ringX = new double[BURSTS];
		ringY = new double[BURSTS];

		for (int i = 0; i < BURSTS; i++)
		{
			respawnRing(i);

			// stagger initial radius
			radii[i] = i * -100;
		}

		image = new Image(getClass().getResourceAsStream("/star.png"));

		itemCount = BURSTS * BURST_SECTORS;
	}

	@Override
	public void renderBackground()
	{
		fillBackground(getCycleColour());
	}
	
	@Override
	public void renderForeground()
	{
		gc.setGlobalAlpha(1);

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
		for (int i = 0; i < BURSTS; i++)
		{
			radii[i] += SPEED;

			if (radii[i] > MAX_RADIUS)
			{
				respawnRing(i);
			}

			if (radii[i] > 0)
			{
				plotRing(i, clockwise ? angleClockwise : angleAntiClockwise);
			}

			clockwise = !clockwise;
		}
	}

	private final void respawnRing(int ringIndex)
	{
		radii[ringIndex] = 1;
		ringX[ringIndex] = precalc.getUnsignedRandom() * width;
		ringY[ringIndex] = precalc.getUnsignedRandom() * height;
	}

	private final void plotRing(int ringIndex, double angle)
	{
		double radius = radii[ringIndex];
		
		setRingFade(radius);

		for (int i = 0; i < BURST_SECTORS; i++)
		{
			double sAngle = angle + EACH_ANGLE * i;

			double x = ringX[ringIndex] + radius * precalc.sin(sAngle);
			double y = ringY[ringIndex] + radius * precalc.cos(sAngle);
			
			gc.drawImage(image, x, y);
		}
	}
	
	private final void setRingFade(double radius)
	{
		if (radius > FADE_RADIUS)
		{
			double alpha = (MAX_RADIUS - radius) / FADE_STEPS;
			gc.setGlobalAlpha(alpha);
		}
		else
		{
			gc.setGlobalAlpha(1);
		}
	}
}