/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.DemoConfig;

public class Stars extends ShapeEffect
{
	private int[] spikeCount;

	private double[] outerRadius;
	private double[] innerRadius;

	public Stars(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialiseEffect()
	{
		itemName = "stars";

		spikeCount = new int[itemCount];
		outerRadius = new double[itemCount];
		innerRadius = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			spikeCount[i] = getRandomIntInclusive(4, 6);

			outerRadius[i] = getRandomIntInclusive(8, 16);

			innerRadius[i] = outerRadius[i] / getRandomIntInclusive(2, 4);
		}
	}

	@Override
	protected final void drawShapeLine(int index)
	{
		int x = shapePosX[index];
		int y = shapePosY[index];

		int spikes = spikeCount[index];

		double outer = outerRadius[index];
		double inner = innerRadius[index];

		double firstX = 0;
		double firstY = 0;

		double lastX = 0;
		double lastY = 0;

		double spikeAngle = 360.0 / (spikes * 2);
		double theta = spikeAngle + shapeAngle[index];

		setShapeColour(index, x, y);

		for (int i = 0; i < spikes; i++)
		{
			theta += spikeAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			double newX = x + outer * sinTheta;
			double newY = y + outer * cosTheta;

			if (i > 0)
			{
					gc.strokeLine(lastX, lastY, newX, newY);
			}
			else
			{
				firstX = newX;
				firstY = newY;
			}

			theta += spikeAngle;

			sinTheta = precalc.sin(theta);
			cosTheta = precalc.cos(theta);

			double newX2 = x + inner * sinTheta;
			double newY2 = y + inner * cosTheta;

			gc.strokeLine(newX, newY, newX2, newY2);

			lastX = newX2;
			lastY = newY2;
		}

		gc.strokeLine(lastX, lastY, firstX, firstY);
	}

	@Override
	protected final void drawShapePolygon(int index)
	{
		int x = shapePosX[index];
		int y = shapePosY[index];

		int spikes = spikeCount[index];

		double outer = outerRadius[index];
		double inner = innerRadius[index];

		double[] pointsX = new double[spikes * 2];
		double[] pointsY = new double[spikes * 2];

		double spikeAngle = 360.0 / (spikes * 2);
		double theta = spikeAngle + shapeAngle[index];

		setShapeColour(index, x, y);

		int pos = 0;

		for (int i = 0; i < spikes; i++)
		{
			theta += spikeAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			pointsX[pos] = x + outer * sinTheta;
			pointsY[pos] = y + outer * cosTheta;

			pos++;

			theta += spikeAngle;

			sinTheta = precalc.sin(theta);
			cosTheta = precalc.cos(theta);

			pointsX[pos] = x + inner * sinTheta;
			pointsY[pos] = y + inner * cosTheta;

			pos++;
		}

		gc.strokePolygon(pointsX, pointsY, pos);
	}
}