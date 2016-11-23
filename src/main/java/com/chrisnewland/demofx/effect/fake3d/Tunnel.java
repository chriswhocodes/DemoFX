/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public class Tunnel extends AbstractEffect
{
	private class Hoop
	{
		private static final int MAX_DISTANCE = 200;

		private double x;
		private double y;
		private double z;

		public Hoop(double z)
		{
			respawn();

			this.z = z;
		}

		public void respawn()
		{
			x = precalc.getSignedRandom() * halfWidth;
			y = 0;//precalc.cos(angle) * 64;
			z = MAX_DISTANCE;
		}

		public double translateX()
		{
			return x / z;
		}

		public double translateY()
		{
			return y / z;
		}

		public void incZ(double amount)
		{
			z -= 0.3;

			if (z < 1)
			{
				respawn();
			}
		}

		public double getDiameter()
		{
			return (MAX_DISTANCE - z) * 2;
		}
	}

	private Hoop[] hoops;

	private double angle = 0;

	public Tunnel(DemoConfig config)
	{
		super(config);

		if (itemCount == -1)
		{
			itemCount = 1;
		}

		init();
	}

	public Tunnel(DemoConfig config, int starCount, long startMillis, long stopMillis)
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
		hoops = new Hoop[itemCount];

		double minZ = 100;

		for (int i = 0; i < itemCount; i++)
		{
			hoops[i] = new Hoop(minZ - i * minZ / itemCount);
		}
	}

	@Override
	public void renderForeground()
	{
		gc.setStroke(Color.WHITE);

		angle += 1;

		if (angle >= 360)
		{
			angle -= 360;
		}

		for (int i = 0; i < itemCount; i++)
		{
			Hoop hoop = hoops[i];

			moveStar(hoop);

			plotStar(hoop);
		}
	}

	private final void moveStar(Hoop hoop)
	{
		hoop.incZ(1.0);
	}

	private final void plotStar(Hoop hoop)
	{
		double x = halfWidth + hoop.translateX();
		double y = halfHeight + hoop.translateY();

		double diameter = hoop.getDiameter();

		gc.strokeOval(x - diameter / 2, y - diameter / 2, diameter, diameter);

		gc.strokeOval(halfWidth - 4, halfHeight - 4, 8, 8);
	}
}