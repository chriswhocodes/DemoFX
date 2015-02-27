/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.DemoConfig;

public abstract class BasicShape extends ShapeEffect
{
	private double[] radius;
	private final int points;
	private boolean doubleAngle = false;

	public BasicShape(GraphicsContext gc, DemoConfig config, int points)
	{
		super(gc, config);
		this.points = points;
	}

	@Override
	protected void initialiseEffect()
	{
		itemName = "shapes";

		radius = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			radius[i] = getRandomIntInclusive(8, 16);
		}
	}

	public void setDoubleAngle(boolean doubleAngle)
	{
		this.doubleAngle = doubleAngle;
	}

	@Override
	protected final void drawShapeLine(int index)
	{
		int x = shapePosX[index];
		int y = shapePosY[index];

		double outer = radius[index];

		double firstX = 0;
		double firstY = 0;

		double lastX = 0;
		double lastY = 0;

		double pointAngle = 360.0 / points;

		if (doubleAngle)
		{
			pointAngle *=2;
		}

		double theta = pointAngle + shapeAngle[index];

		setShapeColour(index, x, y);

		for (int i = 0; i < points; i++)
		{
			theta += pointAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			double newX = (x + outer * sinTheta);
			double newY = (y + outer * cosTheta);

			if (i > 0)
			{
				gc.strokeLine(lastX, lastY, newX, newY);
			}
			else
			{
				firstX = newX;
				firstY = newY;
			}

			lastX = newX;
			lastY = newY;
		}

		gc.strokeLine(lastX, lastY, firstX, firstY);
	}

	@Override
	protected final void drawShapePolygon(int index)
	{
		int x = shapePosX[index];
		int y = shapePosY[index];

		double outer = radius[index];

		double[] pointsX = new double[points];
		double[] pointsY = new double[points];

		double pointAngle = 360.0 / points;

		if (doubleAngle)
		{
			pointAngle *=2;
		}

		double theta = pointAngle + shapeAngle[index];

		setShapeColour(index, x, y);

		int pos = 0;

		for (int i = 0; i < points; i++)
		{
			theta += pointAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			pointsX[pos] = x + outer * sinTheta;
			pointsY[pos] = y + outer * cosTheta;

			pos++;
		}

		gc.strokePolygon(pointsX, pointsY, pos);
	}
}