/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

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

	public TextWave(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);

		waveYPos = halfHeight + 48;
	}

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

	@Override
	protected void initialise()
	{
		// TODO Apple font, replace later with generic
		font = Font.font("Krungthep", FontWeight.BOLD, INITIAL_FONT_SIZE);
		xOffset = width;

		stringList = new ArrayList<>();

		stringList.add("TextWave effect - plot letters on a sine wave with automatic kerning");

		precalculateCharacterWidths();
	}

	public void customInitialise(List<String> stringList, long startMillis, long stopMillis, boolean loopStringList, double yPos, double amplitude, Color colour,
			Font font, double speed)
	{
		this.stringList = stringList;
		this.effectStartMillis = startMillis;
		this.effectStopMillis = stopMillis;
		this.loopStringList = loopStringList;
		this.waveYPos = yPos;
		this.amplitude = amplitude;
		this.fontColour = colour;
		this.font = font;
		this.speed = speed;

		precalculateCharacterWidths();
	}

	public void customInitialise(List<String> stringList, long startMillis, long stopMillis, boolean loopStringList)
	{
		customInitialise(stringList, startMillis, stopMillis, loopStringList, waveYPos, amplitude, null, gc.getFont(), speed);
	}

	
	public void customInitialise(List<String> stringList, long startMillis, long stopMillis, boolean loopStringList, double yPos, double amplitude, Color colour)
	{
		customInitialise(stringList, startMillis, stopMillis, loopStringList, yPos, amplitude, colour, gc.getFont(), speed);
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
	public void renderBackground()
	{
		fillBackground(Color.NAVY);
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