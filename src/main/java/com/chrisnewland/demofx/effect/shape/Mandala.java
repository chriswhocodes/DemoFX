/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Mandala extends AbstractEffect
{
	class MandalaShape
	{
		private static final double MAX_RADIUS = 64;

		private double angle;
		private double angleDelta;
		private double radius;
		private double originX;
		private double originY;
		private Color colour;

		public MandalaShape()
		{
			respawn();
		}

		private void respawn()
		{
			angle = 0;
			radius = -MAX_RADIUS * precalc.getUnsignedRandom();
			angleDelta = 2;
			originX = width * precalc.getUnsignedRandom();
			originY = height * precalc.getSignedRandom();
			colour = getRandomColour(0, 255);
		}

		public void draw(GraphicsContext gc)
		{
			radius++;

			if (radius < 0)
			{
				return;
			}

			angle += angleDelta;

			if (angle > 360)
			{
				angle -= 360;
			}

			gc.setLineWidth(2);

			gc.setStroke(colour);

			gc.setGlobalAlpha(1 - radius / MAX_RADIUS);

			for (double theta = 0; theta < 360; theta += 12)
			{
				double x = originX + radius * precalc.sin(angle + theta);
				double y = originY + radius * precalc.cos(angle + theta);

				gc.strokeOval(x, y, radius, radius);
			}

			if (radius > MAX_RADIUS)
			{
				respawn();
			}
		}
	}

	private MandalaShape[] shapes = new MandalaShape[64];

	public Mandala(DemoConfig config)
	{
		super(config);

		for (int i = 0; i < shapes.length; i++)
		{
			shapes[i] = new MandalaShape();
		}
	}

	@Override
	public void renderForeground()
	{
		for (int i = 0; i < shapes.length; i++)
		{
			shapes[i].draw(gc);
		}
	}
}