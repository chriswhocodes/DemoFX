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

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

public class TextWave extends AbstractEffect
{
	private double xOffset;
	private double lastCharX;

	private List<String> stringList;
	private Font font;

	private static final double FONT_SIZE = 144;
	private static final int SPEED = 8;
	private static final int AMPLITUDE = 50;
	private static final int OFFSCREEN = 100;

	private int stringIndex = 0;

	public TextWave(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
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
		font = Font.font(Font.getDefault().getFamily(), FONT_SIZE);
		gc.setFont(font);
		xOffset = width;

		stringList = new ArrayList<>();

		stringList.add("JavaFX Text Effect by @chriswhocodes");
		stringList.add("Letters plotted individually to allow per-character sine plotting.");
		stringList.add("TextUtil calculates and caches letter width for correct kerning");
	}

	@Override
	public void renderBackground()
	{
		fillBackground(Color.BLACK);
	}
	
	@Override
	public void renderForeground()
	{
		xOffset -= SPEED;

		if (lastCharX < 0)
		{
			xOffset = width;
			
			stringIndex++;

			if (stringIndex == stringList.size())
			{
				stringIndex = 0;
			}
		}

		plotText();
	}

	private final void plotText()
	{
		String currentString = stringList.get(stringIndex);

		String[] chars = explodeString(currentString);

		gc.setFill(Color.WHITE);

		double y = 0;

		double charX = xOffset + OFFSCREEN;

		for (int i = 0; i < chars.length; i++)
		{
			String character = chars[i];

			double charWidth = TextUtil.getStringWidthPixels(font, gc, character);

			if (isLetterOnScreen(charX))
			{
				y = halfHeight + precalc.sin(charX + OFFSCREEN) * AMPLITUDE;

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