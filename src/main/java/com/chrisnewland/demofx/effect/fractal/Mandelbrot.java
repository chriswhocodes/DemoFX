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
	private double xOffset;
	private double yOffset;
	private double resolution;
	private double resolutionStep;
	private double xc;
	private double yc;

	private final double pixelSize = 2;

	private boolean zoomIn = true;

	private String[] pointsOfInterest;
	private int poiIndex = 0;
	
	public Mandelbrot(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		pointsOfInterest = new String[] { "-.1,.9", ".27,0", "0,-0.8", "-0.5,0.6", "-1.4,0" };

		nextPointOfInterest();
	}

	public void pixel(double x, double y)
	{
		gc.fillRect(x, y, pixelSize, pixelSize);
	}

	@Override
	public void renderBackground()
	{
		fillBackground(Color.BLACK);
	}

	private final void zoom()
	{
		resolutionStep = resolution * 0.03;

		if (zoomIn)
		{
			resolution += resolutionStep;
		}
		else
		{
			resolution -= resolutionStep;
		}

		iterations = 16 + (int) (resolution * 0.004);

		if (resolution > 20000)
		{
			zoomIn = false;
		}
		else if (resolution < width / 4)
		{
			zoomIn = true;

			poiIndex++;

			if (poiIndex == pointsOfInterest.length)
			{
				poiIndex = 0;
			}

			nextPointOfInterest();
		}
	}

	private final void nextPointOfInterest()
	{
		String nextPoiString = pointsOfInterest[poiIndex];

		String[] parts = nextPoiString.split(",");

		xOffset = Double.parseDouble(parts[0]);
		yOffset = Double.parseDouble(parts[1]);

		resolution = width / 4;
	}

	private final void plot()
	{
		boolean unbounded;

		int i;
		double x = 0;
		double y = 0;
		double dx = 1 / resolution;
		double x1 = xOffset - 0.5 * width / resolution;
		double x2 = xOffset + 0.5 * width / resolution;
		double y2 = yOffset + height * dx / 2;

		double iterFraction = Math.max(Math.abs(x1), Math.abs(x2));

		for (double iy = 0; iy <= height; iy += pixelSize + 1)
		{
			yc = y2 - iy * dx;

			for (double ix = 0; ix <= width; ix += pixelSize + 1)
			{
				xc = x1 + ix * dx;

				x = 0;
				y = 0;

				unbounded = false;

				for (i = 0; (i < iterations); ++i)
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
						unbounded = true;
						break;
					}
				}

				if (unbounded)
				{
					iterFraction = (double) i / iterations;

					setColour(iterFraction);

					pixel(ix, iy);
				}
			}
		}
	}

	private final void setColour(double iterFraction)
	{
		int red = (int) Math.min(255 * 2 * iterFraction, 255);
		int green = (int) Math.max(255 * (2 * iterFraction - 1), 0);

		Color pixelColour = Color.rgb(red, green, green);

		gc.setFill(pixelColour);
	}

	@Override
	public void renderForeground()
	{
		zoom();

		plot();
	}
}
