/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;

public class Spin extends AbstractEffect
{
	private Image image;

	private double imgWidth;
	private double imgHeight;
	private double scale = 1.0;
	private double scaledWidth;
	private double scaledHeight;
	private double spinAngle;

	private double scaleAngle = 0;

	public Spin(DemoConfig config)
	{
		super(config);
		
		init("wave.png", 0.4);
	}

	private void init(String imageName, double spinAngle)
	{
		image = ImageUtil.loadImageFromResources(imageName);

		imgWidth = image.getWidth();
		imgHeight = image.getHeight();
		
		this.spinAngle = spinAngle;
	}
	
	@Override
	public void renderForeground()
	{
		scaleImage();

		rotateCanvasAroundCentre(2);

		plotTiles();
	}

	private final void scaleImage()
	{
		scaleAngle += spinAngle;

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