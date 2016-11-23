/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;

public class TextWaveSprite extends AbstractEffect
{
	private double xOffset;
	private double lastCharX;

	private List<String> stringList;

	private static final double OFFSCREEN = 100;

	private double speed = 10;
	private double amplitude = 40;
	private double waveYPos;
	
	private double fontScale = 0.2;

	private int stringIndex = 0;

	private String currentString;
	private Character[] chars;

	public TextWaveSprite(DemoConfig config)
	{
		super(config);

		init(new String[] { "DemoFX III" }, halfHeight + 48, 1, 10);
	}

	public TextWaveSprite(DemoConfig config, String[] strings, double yPos, double fontScale, double speed)
	{
		super(config);

		init(strings, yPos, fontScale, speed);
	}

	private void init(String[] strings, double yPos, double fontScale, double speed)
	{
		waveYPos = yPos;
		this.fontScale = fontScale;
		this.speed = speed;

		xOffset = width;

		stringList = new ArrayList<>();

		stringList.addAll(Arrays.asList(strings));

		currentString = stringList.get(stringIndex);

		chars = explodeString(currentString.toUpperCase());
	}

	private Character[] explodeString(String str)
	{
		final int length = str.length();

		Character[] result = new Character[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = Character.valueOf(str.charAt(i));
		}

		return result;
	}

	@Override
	public void renderForeground()
	{
		xOffset -= speed;

		if (lastCharX < 0)
		{
			xOffset = width;

			stringIndex++;

			if (stringIndex == stringList.size())
			{
				effectFinished = true;
			}

			if (!effectFinished)
			{
				currentString = stringList.get(stringIndex);

				chars = explodeString(currentString.toUpperCase());
			}

		}

		if (!effectFinished)
		{
			plotText();
		}
	}

	private final void plotText()
	{
		double y = 0;

		double charX = xOffset + OFFSCREEN;

		for (int i = 0; i < chars.length; i++)
		{
			Character character = chars[i];

			if (character.charValue() == ' ')
			{
				charX += 40;
				continue;
			}

			Image charImage = TextUtil.getSpriteCharacter(character);

			double charWidth = charImage.getWidth();
			double charHeight = charImage.getHeight();

			double plotWidth = charWidth * fontScale;
			double plotHeight = charHeight * fontScale;

			if (i > 0)
			{
				charX += TextUtil.getKerningForChar(character.charValue(), chars[i - 1].charValue(), plotHeight);
			}

			if (isLetterOnScreen(charX))
			{
				y = waveYPos + precalc.sin(charX / 2 + OFFSCREEN) * amplitude;

				gc.drawImage(charImage, charX, y, plotWidth, plotHeight);
			}

			charX += plotWidth;

		}

		lastCharX = charX;
	}

	private final boolean isLetterOnScreen(double charX)
	{
		return (charX > -OFFSCREEN && charX < width);
	}
}