/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoConfig.PlotMode;
import com.chrisnewland.demofx.PreCalc;

public abstract class ShapeEffect extends AbstractEffect
{
	protected int[] shapePosX;
	protected int[] shapePosY;
	protected int[] shapeVectorX;
	protected int[] shapeVectorY;

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

	private boolean incRespawnAngle = true;

	private int respawnShapeCount = 0;

	private final int respawnShapeLimit = 5;

	private static final int OFFSCREEN = 16;

	private final int OFFSCREEN_LEFT;
	private final int OFFSCREEN_RIGHT;
	private final int OFFSCREEN_TOP;
	private final int OFFSCREEN_BOTTOM;

	protected PreCalc precalc;

	private final PlotMode mode;
	private final int rotateDegrees;

	public ShapeEffect(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);

		this.mode = config.getPlotMode();
		this.rotateDegrees = config.getRotation();

		OFFSCREEN_LEFT = -OFFSCREEN;
		OFFSCREEN_RIGHT = width + OFFSCREEN;
		OFFSCREEN_TOP = -OFFSCREEN;
		OFFSCREEN_BOTTOM = height + OFFSCREEN;
	}

	@Override
	protected void initialise()
	{
		precalc = new PreCalc(width, height, 1000);

		shapePosX = new int[itemCount];
		shapePosY = new int[itemCount];

		shapeVectorX = new int[itemCount];
		shapeVectorY = new int[itemCount];

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

		initialiseEffect();
	}

	@Override
	public final void render()
	{
		long renderStartNanos = System.nanoTime();

		fillBackground();

		for (int i = 0; i < itemCount; i++)
		{
			plotShape(i);

			rotateShape(i);

			moveShape(i);
		}

		long renderEndNanos = System.nanoTime();

		long renderNanos = renderEndNanos - renderStartNanos;

		updateFPS(renderNanos);
	}

	private final void plotShape(int i)
	{
		if (mode == PlotMode.PLOT_MODE_POLYGON)
		{
			drawShapePolygon(i);
		}
		else if (mode == PlotMode.PLOT_MODE_LINE)
		{
			drawShapeLine(i);
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

		gc.setFill(Color.rgb(bgColourRed, bgColourGreen, bgColourBlue));

		gc.fillRect(0, 0, width, height);
	}

	private final void respawn(int i)
	{
		if (i > respawnEllipse)
		{
			// respawn on ellipse
			shapePosX[i] = halfWidth + (int) (spawnDiameterX * respawnEllipseX[respawnAngle]);
			shapePosY[i] = halfHeight + (int) (spawnDiameterY * respawnEllipseY[respawnAngle]);

			if (incRespawnAngle)
			{
				respawnAngle++;
			}

			incRespawnAngle = !incRespawnAngle;

			if (respawnAngle == 360)
			{
				respawnAngle = 0;
			}

			if (respawnShapeCount++ == respawnShapeLimit)
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
		}
		else
		{
			// respawn at random location
			shapePosX[i] = (int) (precalc.getRandom() * width);
			shapePosY[i] = (int) (precalc.getRandom() * height);
		}
	}

	protected final void setShapeColour(int index, int x, int y)
	{
		int r = shapeColorRed[index];
		int g = shapeColorGreen[index];
		int b = shapeColorBlue[index];

		double fadeFactor = precalc.getCoordinateFade(x, y);

		r *= fadeFactor;
		g *= fadeFactor;
		b *= fadeFactor;

		r = Math.max(0, Math.min(r, 255));
		g = Math.max(0, Math.min(g, 255));
		b = Math.max(0, Math.min(b, 255));

		gc.setStroke(Color.rgb(r, g, b));
	}

	protected abstract void drawShapePolygon(int i);

	protected abstract void drawShapeLine(int i);

	protected abstract void initialiseEffect();

}