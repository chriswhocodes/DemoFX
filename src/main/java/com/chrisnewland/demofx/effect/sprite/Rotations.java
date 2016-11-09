/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;

public class Rotations extends AbstractEffect
{
	private double[] spriteX;
	private double[] spriteY;
	private double[] angle;
	private double[] angleInc;

	private double diameter;
	private double radius;

	private Image image;

	public Rotations(DemoConfig config)
	{
		super(config);

		init(32);
	}

	public Rotations(DemoConfig config, double ballSize)
	{
		super(config);

		init(ballSize);
	}

	private void init(double diameter)
	{
		this.diameter = diameter;
		radius = diameter / 2;

		if (itemCount == -1)
		{
			itemCount = 1000;
		}

		spriteX = new double[itemCount];
		spriteY = new double[itemCount];
		angle = new double[itemCount];
		angleInc = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{

			spriteX[i] = width * precalc.getUnsignedRandom();
			spriteY[i] = height * precalc.getUnsignedRandom();

			double speed = 4;

			angle[i] = 360 * precalc.getUnsignedRandom();
			angleInc[i] = 2 + (speed * precalc.getUnsignedRandom());
		}

		image = ImageUtil.loadImageFromResources("sun.png");
	}

	@Override
	public void renderForeground()
	{
		for (int i = 0; i < itemCount; i++)
		{
			incAngle(i);

			plotSprite(i);
		}
	}

	private final void incAngle(int i)
	{
		angle[i] += angleInc[i];

		if (angle[i] >= 360)
		{
			angle[i] -= 360;
		}
	}

	private final void plotSprite(int i)
	{
		rotateCanvasAroundPoint(spriteX[i], spriteY[i], angle[i]);

		gc.drawImage(image, (int) spriteX[i] - radius, (int) spriteY[i] - radius, diameter, diameter);
	}
}