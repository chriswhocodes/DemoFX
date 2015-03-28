/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoConfig.PlotMode;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class ShapeEffect extends AbstractEffect
{
	protected double[] shapePosX;
	protected double[] shapePosY;
	protected double[] shapeVectorX;
	protected double[] shapeVectorY;

	private double[] radius;
	private final int points;
	private boolean doubleAngle = false;

	protected int[] shapeColorRed;
	protected int[] shapeColorGreen;
	protected int[] shapeColorBlue;

	protected double[] shapeAngle;

	private double[] respawnEllipseX;
	private double[] respawnEllipseY;

	private int respawnAngle = 0;

	private int bgColourRed;
	private int bgColourGreen;
	private int bgColourBlue;

	private int bgDirRed = 1;
	private int bgDirGreen = 1;
	private int bbgDirBlue = 1;

	private int bgColourCounter = 0;

	private int spawnDiameterX = 50;
	private int spawnDiameterXDir = 1;

	private int spawnDiameterY = 100;
	private int spawnDiameterYDir = 1;

	private int respawnEllipse;

	private int respawnShapeCount = 0;

	private final int respawnShapeLimit = 5;

	private static final int OFFSCREEN = 16;

	private final int OFFSCREEN_LEFT;
	private final int OFFSCREEN_RIGHT;
	private final int OFFSCREEN_TOP;
	private final int OFFSCREEN_BOTTOM;

	private final PlotMode mode;
	private final int rotateDegrees;

	public ShapeEffect(GraphicsContext gc, DemoConfig config, int points)
	{
		super(gc, config);

		this.points = points;

		this.mode = config.getPlotMode();
		this.rotateDegrees = 10;

		OFFSCREEN_LEFT = -OFFSCREEN;
		OFFSCREEN_RIGHT = width + OFFSCREEN;
		OFFSCREEN_TOP = -OFFSCREEN;
		OFFSCREEN_BOTTOM = height + OFFSCREEN;
	}

	@Override
	protected void initialise()
	{

		if (itemCount == -1)
		{
			itemCount = 300;
		}

		shapePosX = new double[itemCount];
		shapePosY = new double[itemCount];

		shapeVectorX = new double[itemCount];
		shapeVectorY = new double[itemCount];

		shapeColorRed = new int[itemCount];
		shapeColorGreen = new int[itemCount];
		shapeColorBlue = new int[itemCount];

		shapeAngle = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			int x = getRandomIntInclusive(0, width);
			int y = getRandomIntInclusive(0, height);

			int dx = getRandomIntInclusive(2, 12);
			int dy = getRandomIntInclusive(2, 12);

			if (getRandomDouble(0, 1) > 0.5)
			{
				dx = -dx;
			}

			if (getRandomDouble(0, 1) > 0.5)
			{
				dy = -dy;
			}

			shapePosX[i] = x;
			shapePosY[i] = y;

			shapeVectorX[i] = dx;
			shapeVectorY[i] = dy;

			shapeColorRed[i] = getRandomIntInclusive(32, 255);
			shapeColorGreen[i] = getRandomIntInclusive(32, 255);
			shapeColorBlue[i] = getRandomIntInclusive(32, 255);

			shapeAngle[i] = getRandomIntInclusive(0, 20);
		}

		respawnEllipse = itemCount / 3;

		respawnEllipseX = new double[360];
		respawnEllipseY = new double[360];

		for (int degree = 0; degree < 360; degree++)
		{
			respawnEllipseX[degree] = Math.sin(Math.toRadians(degree));
			respawnEllipseY[degree] = Math.cos(Math.toRadians(degree));
		}

		bgColourRed = 0;
		bgColourGreen = 32;
		bgColourBlue = 16;

		initialiseShapeRadii();
	}
	
	@Override
	public void renderBackground()
	{
		fillBackground();
	}

	@Override
	public final void renderForeground()
	{
		for (int i = 0; i < itemCount; i++)
		{
			plotShape(i);

			rotateShape(i);

			moveShape(i);

		}

		adjustRespawnPoint();
	}

	private final void plotShape(int i)
	{
		if (mode == PlotMode.PLOT_MODE_LINE)
		{
			drawShapeLine(i);
		}
		else if (mode == PlotMode.PLOT_MODE_POLYGON)
		{
			drawShapePolygon(i, false);
		}
		else if (mode == PlotMode.PLOT_MODE_FILL_POLYGON)
		{
			drawShapePolygon(i, true);
		}
	}

	private final void rotateShape(int i)
	{
		shapeAngle[i] += rotateDegrees;

		if (shapeAngle[i] > 360)
		{
			shapeAngle[i] -= 360;
		}
	}

	private final void moveShape(int i)
	{
		shapePosX[i] += shapeVectorX[i];
		shapePosY[i] += shapeVectorY[i];

		if (shapePosX[i] < OFFSCREEN_LEFT || shapePosX[i] > OFFSCREEN_RIGHT || shapePosY[i] < OFFSCREEN_TOP
				|| shapePosY[i] > OFFSCREEN_BOTTOM)
		{
			respawn(i);
		}
	}

	private final void fillBackground()
	{
		if (bgColourCounter++ > 5)
		{
			int max = 32;

			bgColourRed += bgDirRed;
			bgColourGreen += bgDirGreen;
			bgColourBlue += bbgDirBlue;

			if (bgColourRed > max || bgColourRed == 0)
			{
				bgDirRed = -bgDirRed;
			}

			if (bgColourGreen > max || bgColourGreen == 0)
			{
				bgDirGreen = -bgDirGreen;
			}

			if (bgColourBlue > max || bgColourBlue == 0)
			{
				bbgDirBlue = -bbgDirBlue;
			}

			bgColourCounter = 0;
		}

		fillBackground(bgColourRed, bgColourGreen, bgColourBlue);
	}

	private final void respawn(int i)
	{
		if (i > respawnEllipse)
		{
			respawnOnEllipse(i);
		}
		else
		{
			respawnRandomLocation(i);
		}
	}

	private final void respawnOnEllipse(int i)
	{
		shapePosX[i] = halfWidth + spawnDiameterX * respawnEllipseX[respawnAngle];
		shapePosY[i] = halfHeight + spawnDiameterY * respawnEllipseY[respawnAngle];
	}

	private final void respawnRandomLocation(int i)
	{
		shapePosX[i] = precalc.getUnsignedRandom() * width;
		shapePosY[i] = precalc.getUnsignedRandom() * height;
	}

	private final void adjustRespawnPoint()
	{
		rotateRespawnPoint();

		if (respawnShapeCount++ == respawnShapeLimit)
		{
			modifyEllipseShape();
		}
	}

	private final void rotateRespawnPoint()
	{
		respawnAngle += 3;

		if (respawnAngle == 360)
		{
			respawnAngle = 0;
		}
	}

	private final void modifyEllipseShape()
	{
		spawnDiameterX += spawnDiameterXDir;
		spawnDiameterY += spawnDiameterYDir;

		respawnShapeCount = 0;

		if (spawnDiameterX < 50 || spawnDiameterX > halfWidth)
		{
			spawnDiameterXDir = -spawnDiameterXDir;
		}

		if (spawnDiameterY < 50 || spawnDiameterY > halfHeight)
		{
			spawnDiameterYDir = -spawnDiameterYDir;
		}
	}

	private final Color getShapeColour(int index, double x, double y)
	{
		int r = shapeColorRed[index];
		int g = shapeColorGreen[index];
		int b = shapeColorBlue[index];

		double fadeFactor = precalc.getCoordinateFade((int) x, (int) y);

		r *= fadeFactor;
		g *= fadeFactor;
		b *= fadeFactor;

		r = Math.max(0, Math.min(r, 255));
		g = Math.max(0, Math.min(g, 255));
		b = Math.max(0, Math.min(b, 255));

		return Color.rgb(r, g, b);
	}

	private final void initialiseShapeRadii()
	{
		radius = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			radius[i] = getRandomIntInclusive(8, 16);
		}
	}

	public final void setDoubleAngle(boolean doubleAngle)
	{
		this.doubleAngle = doubleAngle;
	}

	private final void drawShapeLine(int index)
	{
		double x = shapePosX[index];
		double y = shapePosY[index];

		double outer = radius[index];

		double firstX = 0;
		double firstY = 0;

		double lastX = 0;
		double lastY = 0;

		double pointAngle = 360.0 / points;

		if (doubleAngle)
		{
			pointAngle *= 2;
		}

		double theta = pointAngle + shapeAngle[index];

		gc.setStroke(getShapeColour(index, x, y));

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

	private final void drawShapePolygon(int index, boolean filled)
	{
		double x = shapePosX[index];
		double y = shapePosY[index];

		double outer = radius[index];

		double[] pointsX = new double[points];
		double[] pointsY = new double[points];

		double pointAngle = 360.0 / points;

		if (doubleAngle)
		{
			pointAngle *= 2;
		}

		double theta = pointAngle + shapeAngle[index];

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

		if (filled)
		{
			gc.setFill(getShapeColour(index, x, y));
			gc.fillPolygon(pointsX, pointsY, pos);
		}
		else
		{
			gc.setStroke(getShapeColour(index, x, y));
			gc.strokePolygon(pointsX, pointsY, pos);
		}
	}
}