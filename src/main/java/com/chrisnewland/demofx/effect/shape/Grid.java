/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Grid extends AbstractEffect
{
	private double scale = 1.0;
	private double scaledWidth;
	private double scaledHeight;
	private double maxCellWidth;
	private double maxCellHeight;

	private double angle = 0;
	private double scaleAngle = 0;

	private double maxDimension;
	private double offScreenMargin;
	private double gridLineThickness;

	public Grid(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		maxDimension = Math.max(width, height);
		offScreenMargin = maxDimension / 4;

		maxCellWidth = 160;
		maxCellHeight = 160;
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

		rotateCanvas();

		plotGridLines();
	}

	private final void scaleEffect()
	{
		scaleAngle += 0.4;

		if (scaleAngle >= 360)
		{
			scaleAngle -= 360;
		}

		// scale the image with a cosine
		// to get smooth turns from
		// zoom in to zoom out
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

	private final void rotateCanvas()
	{
		Rotate r = new Rotate(angle, halfWidth, halfHeight);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

		angle += 3;

		if (angle >= 360)
		{
			angle -= 360;
		}
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