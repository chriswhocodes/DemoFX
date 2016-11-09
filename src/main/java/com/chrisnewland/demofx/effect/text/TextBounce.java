/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;

public class TextBounce extends AbstractEffect
{
	private double bx;
	private double by;
	private double dx;
	private double dy;

	private double imgWidth;
	private double imgHeight;

	private double halfImgWidth;
	private double halfImgHeight;

	private static final double GRAVITY = 2;

	private Image bounceImage;

	public TextBounce(DemoConfig config)
	{
		super(config);

		init("TextBounce ENGINE");
	}
	
	public TextBounce(DemoConfig config, String text)
	{
		super(config);
		
		init(text);
	}
	
	private void init(String text)
	{
		itemCount = 1;

		bx = width / 2;
		by = height / 2;

		dx = 12;
		dy = GRAVITY;

		bounceImage = TextUtil.createSpriteImageText(text, true, 0.5);
		
		imgWidth = bounceImage.getWidth();
		imgHeight = bounceImage.getHeight();

		halfImgWidth = imgWidth / 2;
		halfImgHeight = imgHeight / 2;
	}

	@Override
	public void renderForeground()
	{
		moveText();

		checkCollisionWall();

		drawText();
	}

	private final void moveText()
	{
		dy += GRAVITY;

		bx += dx;
		by += dy;
	}

	private final void drawText()
	{
		gc.drawImage(bounceImage, (int) bx - halfImgWidth, (int) by - halfImgHeight);
	}

	private final void checkCollisionWall()
	{
		if (bx + halfImgWidth >= width)
		{
			dx = -dx;
			bx = width - halfImgWidth;
		}
		else if (bx - halfImgWidth < 0)
		{
			dx = -dx;
			bx = halfImgWidth;
		}

		if (by + halfImgHeight >= height)
		{
			dy = -dy - GRAVITY;
			by = height - halfImgHeight;
		}
		else if (by - halfImgHeight < 0)
		{
			dy = -dy;
			by = halfImgHeight;
		}
	}
}