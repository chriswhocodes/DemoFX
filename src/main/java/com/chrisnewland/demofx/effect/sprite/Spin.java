/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Spin extends AbstractEffect
{
	private Image image;

	private double imgWidth;
	private double imgHeight;
	private double scale = 1.0;
	private double scaledWidth;
	private double scaledHeight;

	private double angle = 0;
	private double scaleAngle = 0;

	public Spin(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		image = new Image(getClass().getResourceAsStream("/wave.png"));

		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
	}
	
	@Override
	public void renderForeground()
	{
		scaleImage();

		rotateCanvas();

		plotTiles();
	}

	private final void scaleImage()
	{
		scaleAngle += 0.4;

		if (scaleAngle >= 360)
		{
			scaleAngle -= 360;
		}

		// scale the image with a cosine to get smooth turns from zoom in to zoom out
		scale = Math.abs(precalc.cos(scaleAngle));

		scale = 0.1 + Math.max(scale, 0.01);

		scaledWidth = imgWidth * scale;
		scaledHeight = imgHeight * scale;
	}

	private final void rotateCanvas()
	{
		Rotate r = new Rotate(angle, halfWidth, halfHeight);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

		angle += 2;

		if (angle >= 360)
		{
			angle -= 360;
		}
	}

	private final void plotTiles()
	{
		int count = 0;

		final double maxDimension = Math.max(width, height);

		final double buffer = maxDimension / 4;

		for (double y = -buffer; y < maxDimension+buffer; y += scaledHeight)
		{
			for (double x = -buffer; x < maxDimension+buffer; x += scaledWidth)
			{
				gc.drawImage(image, x, y, scaledWidth, scaledHeight);
				count++;
			}
		}

		itemCount = count;
	}
}