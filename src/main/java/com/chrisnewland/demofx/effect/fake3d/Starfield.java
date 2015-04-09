/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

/*
 * procedure 3Dto2D (x, y, z, pan, centre, position)

 x = x + position.x
 y = y + position.y
 z = z + position.z

 new.x = x*cos(pan.x) - z*sin(pan.x)
 new.z = x*sin(pan.x) + z*cos(pan.x)
 new.y = y*cos(pan.y) - new.z*sin(pan.y)

 z = new.y*cos(pan.y) - new.z*sin(pan.y)
 x = new.x*cos(pan.z) - new.y*sin(pan.z)
 y = new.x*sin(pan.z) + new.y*cos(pan.z)

 if z > 0 then

 screen.x = x / z * zoom + centre.x
 screen.y = y / z * zoom + centre.y

 end if
 */

public class Starfield extends AbstractEffect
{
	private double[] starX;
	private double[] starY;
	private double[] starZ;

	private static final double SPEED = 0.05;
	private static final double MAX_DEPTH = 5;

	public Starfield(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		if (itemCount == -1)
		{
			itemCount = 5000;
		}

		starX = new double[itemCount];
		starY = new double[itemCount];
		starZ = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			starX[i] = precalc.getSignedRandom() * halfWidth;
			starY[i] = precalc.getSignedRandom() * halfHeight;
			starZ[i] = precalc.getUnsignedRandom() * MAX_DEPTH;
		}
	}

	@Override
	public void renderBackground()
	{
		fillBackground(Color.BLACK);
	}

	@Override
	public void renderForeground()
	{
		gc.setStroke(Color.WHITE);

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

		if (onScreen(x, y))
		{
			gc.strokeLine(x, y, x + 1, y + 1);
		}
		else
		{
			respawn(i);
		}
	}

	private final boolean onScreen(double x, double y)
	{
		return x >= 0 && y >= 0 && x < width && y < height;
	}
}