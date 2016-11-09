/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class TextLayers extends AbstractEffect
{
	private Image[] layers;
	private double depth[];
	private double angle;

	public TextLayers(DemoConfig config)
	{
		super(config);

		List<String> layers = new ArrayList<>();

		layers.add(TextUtil.getStringFromResource("layer.java"));
		layers.add(TextUtil.getStringFromResource("layer.bytecode"));
		layers.add(TextUtil.getStringFromResource("layer.assembly"));

		String asm = layers.get(2);

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < asm.length(); i++)
		{
			builder.append(String.format("%02X", (int)asm.charAt(i)));

			if (i > 0)
			{
				if (i % 80 == 0)
				{
					builder.append("\n");
				}
				else if (i % 8 == 0)
				{
					builder.append(" ");
				}
			}
		}

		layers.add(builder.toString());

		init(layers);
	}

	public TextLayers(DemoConfig config, List<String> layers)
	{
		super(config);

		init(layers);
	}

	private void init(List<String> textLayers)
	{
		int count = textLayers.size();

		layers = new Image[count];
		depth = new double[count];

		int pos = 0;

		for (String text : textLayers)
		{
			Image image = TextUtil.createImageFromString(Font.font("monospace"), text);
			image = TextUtil.cropImage(image, 1, 1);

			depth[pos] = pos / count;
			layers[pos++] = image;
		}
	}

	@Override
	public void renderForeground()
	{
		angle += 0.36;

		if (angle > 360)
		{
			angle -= 360;
		}

		plotText();
	}

	private final void plotText()
	{
		for (int i = 0; i < layers.length; i++)
		{
			Image image = layers[i];

			double a = layers.length * Math.abs(precalc.sin(angle)); // 0..4

			double d = (a - (i + 1)) * 4; // distance between layers

			double plotWidth = Math.abs(1 + d / layers.length) * image.getWidth();
			double plotHeight = Math.abs(1 + d / layers.length) * image.getHeight();

			double alpha = 1 - Math.abs(d) / 3.33; // alpha fade

			gc.setGlobalAlpha(alpha);

			gc.drawImage(image, (width - plotWidth) / 2, (height - plotHeight) / 2, plotWidth, plotHeight);
		}
	}
}