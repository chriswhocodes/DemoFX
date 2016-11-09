/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.Arrays;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextWave extends AbstractEffect
{
	private double xOffset;
	private double lastCharX;

	private List<String> stringList;
	private Font font;

	private static final double INITIAL_FONT_SIZE = 200;
	private static final double OFFSCREEN = 100;

	private double speed = 12;
	private double amplitude = 120;
	private double waveYPos;

	private int stringIndex = 0;

	private boolean loopStringList = true;

	private Color fontColour = null;

	public TextWave(DemoConfig config)
	{
		super(config);

		font = Font.font("Krungthep", FontWeight.BOLD, INITIAL_FONT_SIZE);

		init(new String[] {
				"TextWave effect - plot letters on a sine wave with automatic kerning" }, true, halfHeight + 48, 120, Color.WHITE,
				font, 12);
	}

	public TextWave(DemoConfig config, String[] strings, boolean loopStringList, double yPos, double amplitude, Color colour,
			Font font, double speed)
	{
		super(config);

		init(strings, loopStringList, yPos, amplitude, colour, font, speed);
	}
	
	public TextWave(DemoConfig config, String[] strings, boolean loopStringList)
	{
		super(config);

		init(strings, loopStringList, halfHeight + 48, amplitude,  Color.WHITE, font, speed);
	}

	private void init(String[] strings, boolean loopStringList, double yPos, double amplitude, Color colour, Font font,
			double speed)
	{
		xOffset = width;

		this.stringList = Arrays.asList(strings);

		this.loopStringList = loopStringList;
		this.waveYPos = yPos;
		this.amplitude = amplitude;
		this.fontColour = colour;
		this.font = font;
		this.speed = speed;

		precalculateCharacterWidths();
	}

	// public void customInitialise(List<String> stringList, boolean
	// loopStringList)
	// {
	// customInitialise(stringList, loopStringList, waveYPos, amplitude, null,
	// gc.getFont(), speed);
	// }
	//
	//
	// public void customInitialise(List<String> stringList, boolean
	// loopStringList, double yPos, double amplitude, Color colour)
	// {
	// customInitialise(stringList, loopStringList, yPos, amplitude, colour,
	// gc.getFont(), speed);
	// }

	private String[] explodeString(String str)
	{
		final int length = str.length();

		String[] result = new String[length];

		for (int i = 0; i < length; i++)
		{
			result[i] = Character.toString(str.charAt(i));
		}

		return result;
	}

	private final void precalculateCharacterWidths()
	{
		gc.setFont(font);

		for (String str : stringList)
		{
			String[] charStrings = explodeString(str);

			for (int i = 0; i < charStrings.length; i++)
			{
				String character = charStrings[i];

				TextUtil.getStringDimensions(font, gc, character);
			}
		}
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
				if (loopStringList)
				{
					stringIndex = 0;
				}
				else
				{
					effectFinished = true;
				}
			}
		}

		if (!effectFinished)
		{
			plotText();
		}
	}

	private final void plotText()
	{
		gc.setFont(font);

		String currentString = stringList.get(stringIndex);

		String[] chars = explodeString(currentString);

		if (fontColour != null)
		{
			gc.setFill(fontColour);
		}
		else
		{
			gc.setFill(getCycleColour());
		}

		double y = 0;

		double charX = xOffset + OFFSCREEN;

		for (int i = 0; i < chars.length; i++)
		{
			String character = chars[i];

			double charWidth = TextUtil.getStringDimensions(font, gc, character).getX();

			if (isLetterOnScreen(charX))
			{
				y = waveYPos + precalc.sin(charX / 2 + OFFSCREEN) * amplitude;

				gc.fillText(character, charX, y);
			}

			charX += charWidth;
		}

		lastCharX = charX;
	}

	private final boolean isLetterOnScreen(double charX)
	{
		return (charX > -OFFSCREEN && charX < width);
	}
}