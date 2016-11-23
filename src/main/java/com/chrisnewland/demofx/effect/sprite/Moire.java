/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.effect.ICanvasSink;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Moire extends AbstractEffect implements ICanvasSink
{
	private double[] ringRadiusX;
	private double[] ringRadiusY;
	private double[] ringAngle;
	private double[] angleInc;

	private double imgWidth;
	private double imgHeight;

	private double halfImgWidth;
	private double halfImgHeight;

	private Image[] imageRing;

	public Moire(DemoConfig config)
	{
		super(config);
		
		init(2, new Color[]{Color.WHITE});
	}
	
	public Moire(DemoConfig config, int count, Color[] colours)
	{
		super(config);
		
		init(count, colours);
	}	

	private void init(int count, Color[] colours)
	{
		itemCount = count;

		ringAngle = new double[itemCount];
		angleInc = new double[itemCount];
		ringRadiusX = new double[itemCount];
		ringRadiusY = new double[itemCount];
		imageRing = new Image[itemCount];

		double angle = 360 / itemCount;

		double maxRadiusX = width / 8;
		double maxRadiusY = height / 8;

		int rings = 30;
		
		for (int i = 0; i < itemCount; i++)
		{
			ringAngle[i] = i * angle;

			angleInc[i] = 1.5 + (1.5 * precalc.getUnsignedRandom());

			if (i % 2 == 0)
			{
				angleInc[i] = -angleInc[i];
			}

			ringRadiusX[i] = maxRadiusX + (maxRadiusX * precalc.getUnsignedRandom());
			ringRadiusY[i] = maxRadiusY + (maxRadiusY * precalc.getUnsignedRandom());

			imageRing[i] = ImageUtil.makeContentricRings(height*1.5, height*1.5, rings, colours[i % colours.length]);
		}

		imgWidth = imageRing[0].getWidth();
		imgHeight = imageRing[0].getHeight();

		halfImgWidth = imgWidth / 2;
		halfImgHeight = imgHeight / 2;
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
		double x = ringRadiusX[i] * precalc.sin(ringAngle[i]);
		double y = ringRadiusY[i] * precalc.cos(ringAngle[i]);

		x += halfWidth - halfImgWidth;
		y += halfHeight - halfImgHeight;

		gc.drawImage(imageRing[i], x, y);
	}

	@Override
	public void setCanvas(Canvas canvas)
	{
		// TODO Auto-generated method stub
		
	}
}