/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

public class TextLabel extends AbstractEffect
{
	private String[] lines;
	private double left;
	private double top;
	private double scale;

	public TextLabel(DemoConfig config)
	{
		super(config);

		String string = "This is the label text";

		init(string, 0, 0, 1);
	}

	public TextLabel(DemoConfig config, String text, double left, double top, double scale)
	{
		super(config);

		init(text, left, top, scale);
	}

	private void init(String text, double left, double top, double scale)
	{
		lines = text.split("\n");
		this.left = left;
		this.top = top;
		this.scale = scale;
	}

	@Override
	public void renderForeground()
	{
		plotText();
	}

	private final void plotText()
	{
		final double lineHeight = 80 * scale;

		double x = left;
		double y = top + lineHeight;

		for (String line : lines)
		{
			TextUtil.writeSpriteStringOnGC(gc, line, x, y, scale);
			y += lineHeight;
		}
	}
}