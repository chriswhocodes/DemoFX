/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;
import com.chrisnewland.demofx.util.MaskingSystem;

import javafx.scene.image.Image;

public class MaskStack extends AbstractEffect
{
	private Image[] imageRing;
	private double[] ringAngle;
	private double[] angleInc;

	private static int thickness;

	public MaskStack(DemoConfig config)
	{
		super(config);

		init(16, ImageUtil.loadImageFromResources("tiger.jpeg"));
	}
	
	public MaskStack(DemoConfig config, Image image)
	{
		super(config);

		init(16, image);
	}

	private void init(int count, Image image)
	{
		itemCount = count;

		imageRing = new Image[count];

		ringAngle = new double[count];

		angleInc = new double[count];

		double angle = 0;
		
		int diameterOuter = (int) Math.min(image.getWidth(), image.getHeight());

		thickness = diameterOuter / count;

		for (int i = 0; i < itemCount; i++)
		{
			ringAngle[i] = i * angle;

			angleInc[i] = (i + 1.0);

			int diameterInner = diameterOuter - thickness;

			if (i == itemCount - 1)
			{
				diameterInner = 0;
			}

			Image mask = MaskingSystem.createMaskRing(diameterOuter, diameterInner);
			//Image mask = MaskingSystem.createMaskBorder(diameterOuter, diameterOuter, thickness);
			
			int offsetX = (int) (image.getWidth() - mask.getWidth()) / 2;
			int offsetY = (int) (image.getHeight() - mask.getHeight()) / 2;

			imageRing[i] = MaskingSystem.applyMask(mask, image, offsetX, offsetY);

			diameterOuter = diameterInner;

		}
	}

	@Override
	public void renderForeground()
	{
		for (int i = 0; i < itemCount; i++)
		{
			move(i);

			render(i);
		}
	}

	private final void move(int i)
	{
		ringAngle[i] += angleInc[i];
	}

	private final void render(int i)
	{
		Image image = imageRing[i];

		double imageWidth = image.getWidth();
		double imageHeight = image.getHeight();

		double x = thickness * precalc.sin(ringAngle[i]);
		double y = thickness * precalc.cos(ringAngle[i]);

		x += halfWidth - imageWidth / 2;
		y += halfHeight - imageHeight / 2;

		gc.drawImage(imageRing[i], x, y, imageWidth, imageHeight);
	}
}