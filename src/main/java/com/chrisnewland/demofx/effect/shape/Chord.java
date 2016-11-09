/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public class Chord extends AbstractEffect
{
	private static final int CHORD_MIN = 3;
	private static final int CHORD_MAX = 12;
	private static final int SATELLITES = 8;

	private double angle = 0;

	private int chordCount = CHORD_MIN;

	private static final double ANGLE_DELTA = 2;
	
	private Color color;

	public Chord(DemoConfig config)
	{
		super(config);
		init(Color.WHITE);
	}
	
	public Chord(DemoConfig config, Color color)
	{
		super(config);
		init(color);
	}
	
	private void init(Color color)
	{
		this.color = color;
	}

	@Override
	public void renderForeground()
	{
		angle += ANGLE_DELTA;

		if (angle > 360)
		{
			angle -= 360;
			
			chordCount++;

			if (chordCount <= CHORD_MAX)
			{
				chordCount++;
			}
		}
		
		double radius = 80;
		
		double circleRadius = radius * Math.PI;

		double pointAngle = 360 / SATELLITES;

		for (double theta = 0; theta < 360; theta += pointAngle)
		{
			double sinTheta = precalc.sin(theta+angle/6);
			double cosTheta = precalc.cos(theta+angle/6);

			double originX = circleRadius * sinTheta;
			double originY = circleRadius * cosTheta;

			drawChord(chordCount, halfWidth + originX, halfHeight + originY, radius, 1.5, false);
		}

		drawChord(chordCount, halfWidth, halfHeight, radius * 2, 2, true);
	}

	private void drawChord(final int points, final double x, final double y, final double radius, final double thickness, boolean reverseAngle)
	{
		int pointAngle = 360 / points;

		double theta = reverseAngle? (360 - angle) : angle;

		int pos = 0;

		double pointsX[] = new double[points];
		double pointsY[] = new double[points];

		gc.setStroke(color);
		gc.setLineWidth(thickness);

		for (int i = 0; i < points; i++)
		{
			theta += pointAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			pointsX[pos] = radius * sinTheta;
			pointsY[pos] = radius * cosTheta;

			pos++;
		}

		for (int i = 0; i < points - 1; i++)
		{
			double x1 = x + pointsX[i];
			double y1 = y + pointsY[i];

			for (int j = i + 1; j < points; j++)
			{
				double x2 = x + pointsX[j];
				double y2 = y + pointsY[j];

				gc.strokeLine(x1, y1, x2, y2);
			}
		}
	}
}