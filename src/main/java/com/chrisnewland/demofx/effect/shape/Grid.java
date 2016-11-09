/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public class Grid extends AbstractEffect
{
	private double scale = 1.0;
	private double scaledWidth;
	private double scaledHeight;
	private double maxCellWidth;
	private double maxCellHeight;

	private double scaleAngle = 0;

	private double maxDimension;
	private double offScreenMargin;
	private double gridLineThickness;

	public Grid(DemoConfig config)
	{
		super(config);

		maxDimension = Math.max(width, height);
		offScreenMargin = maxDimension / 4;

		maxCellWidth = 160;
		maxCellHeight = 160;
	}

	@Override
	public void renderForeground()
	{
		clearScreenForRotation();

		scaleEffect();

		rotateCanvasAroundCentre(3);

		plotGridLines();
	}

	private final void scaleEffect()
	{
		scaleAngle += 0.4;

		if (scaleAngle >= 360)
		{
			scaleAngle -= 360;
		}

		// scale the image with a cosine to get smooth turning points
		scale = Math.abs(precalc.cos(scaleAngle));
		scale = 0.1 + Math.max(scale, 0.01);

		scaledWidth = maxCellWidth * scale;
		scaledHeight = maxCellHeight * scale;
	}

	private final void clearScreenForRotation()
	{
		gc.setFill(Color.BLACK);

		gc.fillRect(-offScreenMargin, -offScreenMargin, width + offScreenMargin * 2, height + offScreenMargin * 2);
	}

	private final void plotGridLines()
	{
		gridLineThickness = scaledHeight / 8;

		gc.setFill(getCycleColour());

		int rows = 0;
		int cols = 0;

		for (double y = -offScreenMargin; y < maxDimension + offScreenMargin; y += scaledHeight)
		{
			rows++;
			gc.fillRect(-offScreenMargin, y, width + offScreenMargin * 2, gridLineThickness);
		}

		for (double x = -offScreenMargin; x < maxDimension + offScreenMargin; x += scaledWidth)
		{
			cols++;
			gc.fillRect(x, -offScreenMargin, gridLineThickness, height + offScreenMargin * 2);
		}

		itemCount = rows * cols;
	}
}