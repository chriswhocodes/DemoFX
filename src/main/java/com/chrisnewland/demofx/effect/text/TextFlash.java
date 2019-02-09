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

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TextFlash extends AbstractEffect
{
	private List<String> stringList;
	private Font font;

	private static final double INITIAL_FONT_SIZE = 80;

	private long showMillis = 200;

	private long time;

	private int stringIndex = 0;

	private boolean loopStringList = true;

	private double yPercent;
	private double textYPos;

	private Color fontColour = Color.WHITE;

	public TextFlash(DemoConfig config)
	{
		super(config);

		font = Font.font("Krungthep", FontWeight.BOLD, INITIAL_FONT_SIZE);

		init("The end is the beginning is the end".toUpperCase().split(" "), true, Color.WHITE, font, 50, 200);
	}
	
	public TextFlash(DemoConfig config, String string)
	{
		super(config);

		font = Font.font("Krungthep", FontWeight.BOLD, INITIAL_FONT_SIZE);

		init(string.toUpperCase().split(" "), true, Color.WHITE, font, 50, 200);
	}
	
	public TextFlash(DemoConfig config, String string, Font font, Color colour)
	{
		super(config);

		init(string.toUpperCase().split(" "), true, colour, font, 50, 200);
	}

	public TextFlash(DemoConfig config, String string, String split, double yPercent, double fontSize, long showMillis)
	{
		super(config);

		font = Font.font("Linux Biolinum Keyboard O", FontWeight.BOLD, fontSize);

		init(string.toUpperCase().split(split), true, Color.WHITE, font, yPercent, showMillis);
	}


	private void init(String[] strings, boolean loopStringList, Color colour, Font font, double yPercent, long showMillis)
	{
		stringList = new ArrayList<>();

		stringList.addAll(Arrays.asList(strings));

		this.loopStringList = loopStringList;

		this.fontColour = colour;
		this.font = font;
		this.yPercent = yPercent;
		this.showMillis = showMillis;

		time = System.currentTimeMillis();

		precalulateStringDimensions();
	}

	private final void precalulateStringDimensions()
	{
		gc.setFont(font);

		for (String str : stringList)
		{
			TextUtil.getStringDimensions(font, gc, str);
		}

		textYPos = yPercent * height / 100;
	}

	@Override
	public void renderForeground()
	{
		long now = System.currentTimeMillis();

		long elapsed = now - time; // 0 .. showMillis

		double opacityFactor = 1 - ((double) elapsed / (double) showMillis); // 1
																				// -
																				// (0
																				// ..
																				// 1)

		Color derivedColor = fontColour.deriveColor(0, 1.0, 1.0, opacityFactor);

		gc.setFill(derivedColor);

		if (!effectFinished)
		{
			plotText();
		}

		if (elapsed > showMillis)
		{
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

			time = now;
		}
	}

	private final void plotText()
	{
		gc.setFont(font);

		String str = stringList.get(stringIndex);

		Point2D dimensions = TextUtil.getStringDimensions(font, gc, str);

		double strWidth = dimensions.getX();
		double strHeight = dimensions.getY();

		double x = halfWidth - strWidth / 2;
		double y = textYPos + strHeight / 2;

		gc.fillText(str, x, y);
	}
}