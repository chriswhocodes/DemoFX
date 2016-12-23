/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.background;

import java.nio.charset.StandardCharsets;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BinaryBackground extends AbstractEffect
{
	private String chars;

	public BinaryBackground(DemoConfig config)
	{
		super(config);
		
		init();
	}

	private void init()
	{
		String str = "The end is the beginning is the end.";

		byte[] bytes = str.getBytes(StandardCharsets.UTF_8);

		StringBuilder builder = new StringBuilder();

		for (byte b : bytes)
		{
			String binary = Integer.toBinaryString(b);

			builder.append(binary);
		}

		chars = builder.toString();

	}

	@Override
	public void renderForeground()
	{
		Font font = Font.font("Courier New", FontWeight.MEDIUM, 14);

		gc.setFont(font);

		double x = 0;
		double y = 16;

		gc.setStroke(Color.rgb(32,64,32));

		while (y < height)
		{
			for (int i = 0; i < chars.length(); i++)
			{
				String charString = Character.toString(chars.charAt(i));

				gc.strokeText(charString, x, y);

				Point2D dimensions = TextUtil.getStringDimensions(font, gc, charString);

				x += dimensions.getX();

				if (x >= width)
				{
					x = 0;
					y += dimensions.getY();
				}
			}

		}
	}
}