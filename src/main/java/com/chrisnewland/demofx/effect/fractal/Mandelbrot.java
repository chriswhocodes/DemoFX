/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fractal;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Mandelbrot extends AbstractEffect
{
	private int iterations;

	private double zoom;

	private double xCentre;
	private double yCentre;

	private double xOffset;
	private double yOffset;

	private double xOffsetCur;
	private double yOffsetCur;
	
	private double xOffsetNext;
	private double yOffsetNext;

	private static final double PIXEL_SIZE = 2;
	private static final double ITERATIONS_PER_ZOOM = 0.004;

	private static double MIN_ZOOM = 0;
	private static double MAX_ZOOM = 18_000;

	private boolean zoomIn = true;

	private String[] pointsOfInterest;
	private int poiIndex = 0;

	private Color cycleColour;

	public Mandelbrot(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);

		MIN_ZOOM = width;
	}

	@Override
	protected void initialise()
	{
		pointsOfInterest = new String[] {
				" 0.00,-0.80",
				" 0.35,-0.40",
				" 0.35, 0.40",
				" 0.00, 0.80",
				"-0.10, 0.90",
				"-0.50, 0.60",
				"-0.70, 0.30",
				"-1.40, 0.00",
				"-0.70,-0.30",
				"-0.50,-0.60",
				"-0.10,-0.90", };

		setPointOfInterest();
	}

	public void pixel(double x, double y)
	{
		gc.fillRect(x, y, PIXEL_SIZE, PIXEL_SIZE);
	}

	@Override
	public void renderBackground()
	{
		fillBackground(Color.BLACK);
	}
	
	private final void zoom()
	{
		double zoomStep = zoom * 0.03;

		if (zoomIn)
		{
			zoom += zoomStep;
		}
		else
		{
			zoom -= zoomStep;
			
			xOffset += (xOffsetNext - xOffsetCur) / 103;
			yOffset += (yOffsetNext - yOffsetCur) / 103;
		}

		iterations = 16 + (int) (zoom * ITERATIONS_PER_ZOOM);

		if (zoom > MAX_ZOOM)
		{
			zoomIn = false;
		}
		else if (zoom < MIN_ZOOM)
		{
			zoomIn = true;

			poiIndex++;

			setPointOfInterest();
		}
	}

	private final void setPointOfInterest()
	{
		if (poiIndex == pointsOfInterest.length)
		{
			poiIndex = 0;
		}

		String nextPoiString = pointsOfInterest[poiIndex];

		String[] parts = nextPoiString.split(",");

		xOffset = Double.parseDouble(parts[0].trim());
		yOffset = Double.parseDouble(parts[1].trim());
		
		xOffsetCur = xOffset;
		yOffsetCur = yOffset;

		int nextIndex = poiIndex + 1;

		if (nextIndex == pointsOfInterest.length)
		{
			nextIndex = 0;
		}
		
		nextPoiString = pointsOfInterest[nextIndex];
		
		parts = nextPoiString.split(",");

		xOffsetNext = Double.parseDouble(parts[0].trim());
		yOffsetNext = Double.parseDouble(parts[1].trim());

		zoom = MIN_ZOOM;
	}

	private final void plot()
	{
		double dx = 1 / zoom;

		double x1 = xOffset - halfWidth / zoom;
		double x2 = xOffset + halfWidth / zoom;

		double y2 = yOffset + height * dx / 2;

		double iterFraction = Math.max(Math.abs(x1), Math.abs(x2));

		for (double pixelY = 0; pixelY <= height; pixelY += PIXEL_SIZE + 1)
		{
			yCentre = y2 - pixelY * dx;

			for (double pixelX = 0; pixelX <= width; pixelX += PIXEL_SIZE + 1)
			{
				xCentre = x1 + pixelX * dx;

				testPixel(pixelX, pixelY, xCentre, yCentre, iterFraction);
			}
		}
	}

	private final void testPixel(double pixelX, double pixelY, double xc, double yc, double iterFraction)
	{
		double x = 0;
		double y = 0;

		int i;

		for (i = 0; i < iterations; ++i)
		{
			double xSquared = x * x;
			double ySquared = y * y;

			iterFraction = xSquared - ySquared;

			y = 2 * x * y;

			x = iterFraction;

			x += xc;
			y += yc;

			if (xSquared + ySquared >= 4.0)
			{
				iterFraction = (double) i / iterations;

				setColour(iterFraction);

				pixel(pixelX, pixelY);

				break;
			}
		}
	}

	private final void setColour(double iterFraction)
	{
		int red = (int) Math.min(255 * 2 * iterFraction, 255);
		int green = (int) Math.max(255 * 2 * (iterFraction - 1), 0);
		int blue = (red + green) / 2;

		Color pixelColour = Color.rgb(red, green, blue).interpolate(cycleColour, 0.7);

		gc.setFill(pixelColour);
	}

	@Override
	public void renderForeground()
	{
		cycleColour = getCycleColour().brighter().brighter();

		zoom();

		plot();
	}
}