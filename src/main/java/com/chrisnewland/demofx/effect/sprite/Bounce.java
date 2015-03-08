/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Bounce extends AbstractEffect
{
	private double[] bx;
	private double[] by;
	private double[] dx;
	private double[] dy;

	private int diameter;
	private int radius;

	private Image imageBall;

	private static final double MAX_SPEED = 6;

	public Bounce(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		itemName = "Balls";

		diameter = 24;
		radius = diameter / 2;

		if (itemCount == -1)
		{
			itemCount = 50;
		}

		bx = new double[itemCount];
		by = new double[itemCount];
		dx = new double[itemCount];
		dy = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			int angle = 0;

			if (i > 0)
			{
				angle = i * 360 / itemCount;
			}

			double sin = precalc.sin(angle);
			double cos = precalc.cos(angle);

			bx[i] = width / 2 + 100 * sin;
			by[i] = height / 2 + 100 * cos;

			double speed = 4;

			dx[i] = sin * speed + precalc.getSignedRandom();
			dy[i] = cos * speed + precalc.getSignedRandom();

			// http://pixabay.com/en/orb-ball-globe-glossy-glow-red-150545/
			imageBall = new Image(getClass().getResourceAsStream("/glassyball.png"));
		}
	}

	@Override
	public void render()
	{
		fillBackground(Color.rgb(0, 0, 20));

		for (int i = 0; i < itemCount; i++)
		{
			moveBall(i);

			checkCollisionWall(i);

			checkCollisionBall(i);

			drawBall(i);
		}
	}

	private final void moveBall(int i)
	{
		if (Math.abs(dx[i]) < MAX_SPEED)
		{
			dx[i] += precalc.getSignedRandom() / 2;
		}
		else
		{
			dx[i] *= 0.8;
		}

		if (Math.abs(dy[i]) < MAX_SPEED)
		{
			dy[i] += precalc.getSignedRandom() / 2;
		}
		else
		{
			dy[i] *= 0.8;
		}

		bx[i] += dx[i];
		by[i] += dy[i];
	}

	private final void drawBall(int i)
	{
		gc.drawImage(imageBall, (int) bx[i] - radius, (int) by[i] - radius);
	}

	private final void checkCollisionWall(int i)
	{
		if (bx[i] + radius >= width)
		{
			dx[i] = -dx[i];
			bx[i] = width - radius;
		}
		else if (bx[i] - radius < 0)
		{
			dx[i] = -dx[i];
			bx[i] = radius;
		}

		if (by[i] + radius >= height)
		{
			dy[i] = -dy[i];
			by[i] = height - radius;
		}
		else if (by[i] - radius < 0)
		{
			dy[i] = -dy[i];
			by[i] = radius;
		}
	}

	private void checkCollisionBall(int i)
	{
		for (int j = 0; j < itemCount; j++)
		{
			if (i == j)
			{
				continue;
			}

			double xd = bx[i] - bx[j];
			double yd = by[i] - by[j];

			double dist = Math.sqrt(xd * xd + yd * yd);
			// double dist = precalc.getDistance(bx[i], by[i], bx[j], by[j]);

			if (dist <= diameter)
			{
				double nextDXI = dx[j];
				double nextDYI = dy[j];
				double nextDXJ = dx[i];
				double nextDYJ = dy[i];

				dx[i] = nextDXI;
				dy[i] = nextDYI;

				dx[j] = nextDXJ;
				dy[j] = nextDYJ;

				bx[i] += dx[i] * 2;
				bx[j] += dx[j] * 2;

				by[i] += dy[i] * 2;
				by[j] += dy[j] * 2;

				break;
			}
		}
	}
}