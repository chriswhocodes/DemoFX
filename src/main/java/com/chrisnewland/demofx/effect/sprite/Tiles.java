/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Tiles extends AbstractEffect
{
	private Image image;

	private double imgWidth;
	private double imgHeight;
	private double scale = 1.0;
	private double scaledWidth;
	private double scaledHeight;
	private double xStart;
	private double yStart;

	private double angle = 0;
	private double scaleAngle = 0;

	public Tiles(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		image = new Image(getClass().getResourceAsStream("/javafx.png"));

		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
	}
	
	@Override
	public void renderForeground()
	{
		scaleImage();

		rotateOffset();

		plotTiles();
	}

	private final void scaleImage()
	{
		scaleAngle += 0.2;

		if (scaleAngle >= 360)
		{
			scaleAngle -= 360;
		}

		scale = Math.abs(precalc.cos(scaleAngle));

		scale = 0.1 + Math.max(scale, 0.01);

		scaledWidth = imgWidth * scale;
		scaledHeight = imgHeight * scale;
	}

	private final void rotateOffset()
	{
		xStart = halfWidth + 500 * precalc.sin(angle);
		yStart = halfHeight + 500 * precalc.cos(angle);

		angle += 1.5;

		if (angle >= 360)
		{
			angle -= 360;
		}
	}

	private final void plotTiles()
	{
		int count = 0;

		while (xStart > 0)
		{
			xStart -= scaledWidth;
		}

		while (yStart > 0)
		{
			yStart -= scaledHeight;
		}

		for (double y = yStart; y < height; y += scaledHeight)
		{
			for (double x = xStart; x < width; x += scaledWidth)
			{
				gc.drawImage(image, x, y, scaledWidth, scaledHeight);
				count++;
			}
		}

		itemCount = count;
	}
}
