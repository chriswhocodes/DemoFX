/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.util;

import com.chrisnewland.demofx.DemoConfig;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cog
{
	private double x;
	private double y;
	private boolean clockwise;
	private double angleDelta;
	private double outerRadius;
	private double innerRadius;
	private int teeth;
	private PreCalc precalc;
	private Color outline;

	public Cog(DemoConfig config, double x, double y, boolean clockwise, double angleDelta, double outer, double inner, int teeth, Color outline)
	{
		precalc = new PreCalc(config);
		
		this.x = x;
		this.y = y;
		this.clockwise = clockwise;
		this.angleDelta = angleDelta;
		this.outerRadius = outer;
		this.innerRadius = inner;
		this.teeth = teeth;
		this.outline = outline;
	}

	public void draw(GraphicsContext gc, double angle)
	{
		double toothAngle = Math.floor(360.0 / teeth);

		int points = 4 * (360 / (int) toothAngle);

		double wide = 0.16;
		double narrow = (1 - (2 * wide)) / 2;

		double wideAngle = toothAngle * wide;
		double narrowAngle = toothAngle * narrow;

		double theta = angle;

		theta += angleDelta;

		if (!clockwise)
		{
			theta = 360 - theta;
		}

		int pos = 0;

		double pointsX[] = new double[points + 1];
		double pointsY[] = new double[points + 1];

		for (int i = 0; i < points; i++)
		{
			int mod = i % 4;

			double radius = 0;

			switch (mod)
			{
			case 0:
				theta += wideAngle;
				radius = innerRadius;
				break;

			case 1:
				theta += narrowAngle;
				radius = outerRadius;
				break;

			case 2:
				theta += wideAngle;
				radius = outerRadius;
				break;

			case 3:
				theta += narrowAngle;
				radius = innerRadius;
				break;
			}

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			pointsX[pos] = x + radius * sinTheta;
			pointsY[pos] = y + radius * cosTheta;

			pos++;
		}

		pointsX[pos] = pointsX[0];
		pointsY[pos] = pointsY[0];

		gc.setFill(Color.WHITE);
		gc.fillPolygon(pointsX, pointsY, pointsX.length);

		gc.setStroke(outline);
		gc.setLineWidth(4.0);
		gc.strokePolygon(pointsX, pointsY, pointsX.length);

		double coreDiameter = innerRadius / 2;
		gc.setFill(Color.BLACK);
		gc.fillOval(x - coreDiameter / 2, y - coreDiameter / 2, coreDiameter, coreDiameter);
	}

}
