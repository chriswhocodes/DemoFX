/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;

public class TextRing extends AbstractEffect
{
	public static class RingData
	{
		private String text;
		private double radius;
		private double fontScale;
		private double angle;
		private double speed;
		private double kern;
		private double spaceAngle;

		public RingData(String text, double radius, double fontScale, double speed, double kern, double spaceAngle)
		{
			this.text = text.toUpperCase();
			this.radius = radius;
			this.fontScale = fontScale * 3.25;
			this.speed = speed * 1.5;
			this.kern = kern;
			this.spaceAngle = spaceAngle * 1.6;
		}

		public void rotate()
		{
			angle += speed;

			if (angle >= 360)
			{
				angle -= 360;
			}
		}
	}

	private RingData[] ringData;

	public TextRing(DemoConfig config)
	{
		super(config);

		init(new RingData[] {
		new RingData("Your Text Here", 300, 0.15, 1.6, 3.6, 2)});
	}

	public TextRing(DemoConfig config, RingData[] ringData)
	{
		super(config);

		init(ringData);
	}

	private void init(RingData[] ringData)
	{
		this.ringData = ringData;
	}

	@Override
	public void renderForeground()
	{
		int count = ringData.length;

		for (int i = 0; i < count; i++)
		{
			plotText(i);
		}
	}

	private final void plotText(int index)
	{
		RingData rd = ringData[index];

		rd.rotate();

		double charAngle = 180.0 + rd.angle;

		if (index % 2 == 0)
		{
			charAngle = 360 - charAngle;
		}

		int length = rd.text.length();

		double inc = 0;

		Character lastCharacter = null;

		for (int i = 0; i < length; i++)
		{
			Character character = rd.text.charAt(i);

			if (character.charValue() == ' ')
			{
				charAngle -= rd.spaceAngle;
				continue;
			}

			Image charImage = TextUtil.getSpriteCharacter(character);

			double plotWidth = charImage.getWidth() * rd.fontScale;
			double plotHeight = charImage.getHeight() * rd.fontScale;

			if (i > 0)
			{
				inc = rd.kern * TextUtil.getKerningForChar(character.charValue(), lastCharacter.charValue(), plotHeight, true);

				inc = Math.abs(inc);

				charAngle -= inc;
			}

			double x = halfWidth + rd.radius * precalc.sin(charAngle);

			double y = halfHeight + rd.radius * precalc.cos(charAngle);

			rotateCanvasAroundPoint(x + plotWidth / 2, y + plotHeight / 2, 180 - charAngle);

			gc.drawImage(charImage, x, y, plotWidth, plotHeight);

			lastCharacter = character;
		}
	}
}