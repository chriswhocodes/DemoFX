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

public class Honeycomb extends AbstractEffect
{
	private List<Color> colours = new ArrayList<>();

	private static final double MAX_RADIUS = 50;
	private static final int POINTS = 6;
	private static final double POINT_ANGLE = 360.0 / POINTS;

	private static final int X_STEP = (int) (Math.sqrt(3.0) * MAX_RADIUS * 1.1);
	private static final int Y_STEP = (int) (1.5 * MAX_RADIUS * 1.1);

	private int maxDimension;

	private double[] initialPointsX;
	private double[] initialPointsY;
	
	private double[] pointsX;
	private double[] pointsY;

	public Honeycomb(DemoConfig config)
	{
		super(config);

		maxDimension = (int) (Math.max(width, height) + MAX_RADIUS);
		
		initialPointsX = new double[POINTS];
		initialPointsY = new double[POINTS];
		
		pointsX = new double[POINTS];
		pointsY = new double[POINTS];
		
		makeInitialHexgon();
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
				double r = MAX_RADIUS;

				Color colour = null;

				if (pos < colours.size())
				{
					colour = colours.get(pos);

					colour = colour.deriveColor(0.0, 1.0, 1.0, 2 * r / MAX_RADIUS);

					colours.set(pos, colour);

				}
				else
				{
					colour = getCycleColour();
					
					colours.add(colour);
				}

				pos++;

				plotHexagon(x, y, colour);
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

	private void makeInitialHexgon()
	{
		double theta = 0;

		int pos = 0;

		for (int i = 0; i < POINTS; i++)
		{
			theta += POINT_ANGLE;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			initialPointsX[pos] = MAX_RADIUS * sinTheta;
			initialPointsY[pos] = MAX_RADIUS * cosTheta;

			pos++;
		}
	}

	private final void plotHexagon(double x, double y, Color colour)
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