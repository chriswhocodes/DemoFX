/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public class Diamonds extends AbstractEffect
{
	private List<Color> colours = new ArrayList<>();

	private static final double DIAMOND_WIDTH = 100;
	private static final double DIAMOND_HEIGHT = DIAMOND_WIDTH * 1.6;

	private static final int POINTS = 4;

	private static final int X_STEP = (int) (1.1 * DIAMOND_WIDTH);
	private static final int Y_STEP = (int) (1.1 * DIAMOND_HEIGHT * 0.5);

	private int maxDimension;

	private double[] initialPointsX;
	private double[] initialPointsY;

	private double[] pointsX;
	private double[] pointsY;

	public Diamonds(DemoConfig config)
	{
		super(config);

		maxDimension = (int) (Math.max(width, height) + DIAMOND_WIDTH);

		initialPointsX = new double[POINTS];
		initialPointsY = new double[POINTS];

		pointsX = new double[POINTS];
		pointsY = new double[POINTS];

		makeInitialShape();
	}

	@Override
	public void renderForeground()
	{
		rotateCanvasAroundCentre(1);

		int xShift = 0;

		int pos = 0;

		for (int y = -maxDimension; y <= maxDimension; y += Y_STEP)
		{
			for (int x = xShift - maxDimension; x <= maxDimension; x += X_STEP)
			{
				double r = DIAMOND_WIDTH;

				Color colour = null;

				if (pos < colours.size())
				{
					colour = colours.get(pos);

					colour = colour.deriveColor(0.0, 1.0, 1.0, 2 * r / DIAMOND_WIDTH);

					colours.set(pos, colour);

				}
				else
				{
					colour = getCycleColour();

					colours.add(colour);
				}

				pos++;

				plotShape(x, y, colour);
			}

			if (xShift == 0)
			{
				xShift = X_STEP / 2;
			}
			else
			{
				xShift = 0;
			}
		}
	}

	private void makeInitialShape()
	{

		initialPointsX[0] = DIAMOND_WIDTH / 2;
		initialPointsY[0] = 0;

		initialPointsX[1] = DIAMOND_WIDTH;
		initialPointsY[1] = DIAMOND_HEIGHT / 2;

		initialPointsX[2] = DIAMOND_WIDTH / 2;
		initialPointsY[2] = DIAMOND_HEIGHT;

		initialPointsX[3] = 0;
		initialPointsY[3] = DIAMOND_HEIGHT / 2;
	}

	private final void plotShape(double x, double y, Color colour)
	{
		int pos = 0;

		for (int i = 0; i < POINTS; i++)
		{
			pointsX[pos] = initialPointsX[pos] + x;
			pointsY[pos] = initialPointsY[pos] + y;

			pos++;
		}

		gc.setFill(colour);
		gc.fillPolygon(pointsX, pointsY, pos);
	}
}