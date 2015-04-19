/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Checkerboard extends AbstractEffect
{
	private double scale = 1.0;
	private double checkSize;
	private double maxCheckSize;

	private double scaleAngle = 0;

	private double maxDimension;
	private double offScreenMargin;

	public Checkerboard(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		maxDimension = Math.max(width, height);
		offScreenMargin = maxDimension / 4;

		maxCheckSize = 160;
	}

	@Override
	public void renderBackground()
	{
		clearScreenForRotation();
	}

	@Override
	public void renderForeground()
	{
		scaleEffect();

		rotateCanvas(3);

		plotCheckerboard();
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

		checkSize = maxCheckSize * scale;
	}

	private final void clearScreenForRotation()
	{
		gc.setFill(Color.BLACK);

		gc.fillRect(-offScreenMargin, -offScreenMargin, width + offScreenMargin * 2, height + offScreenMargin * 2);
	}

	private final void plotCheckerboard()
	{
		gc.setFill(Color.NAVY);

		boolean oddRow = true;

		for (double y = -offScreenMargin; y < maxDimension + offScreenMargin; y += checkSize)
		{
			double startX = -offScreenMargin;

			if (oddRow)
			{
				startX += checkSize;
			}

			oddRow = !oddRow;

			for (double x = startX; x < maxDimension + offScreenMargin; x += checkSize * 2)
			{
				gc.fillRect(x, y, checkSize, checkSize);
			}
		}
	}
}