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

		int rows = 48;
		int cols = 64;

		layers.add(repeat("source code ", rows, cols));
		layers.add(repeat("bytecode ", rows, cols));
		layers.add(repeat("assembly ", rows, cols));
		layers.add(repeat("binary ", rows, cols));

		init(layers);
	}

	private String repeat(String text, int rows, int cols)
	{
		StringBuilder builder = new StringBuilder();

		boolean finished = false;

		int textLength = text.length();

		int currentRowWidth = 0;

		int currentRow = 0;

		while (!finished)
		{
			if (currentRowWidth + textLength < cols)
			{
				builder.append(text);
				currentRowWidth += textLength;
			}
			else
			{
				int spillover = (currentRowWidth + textLength) - cols;

				builder.append(text.substring(0, textLength - spillover)).append("\n");

				if (spillover > 0)
				{
					builder.append(text.substring(spillover - 1));
					currentRowWidth = textLength - spillover;

				}
				else
				{
					currentRowWidth = 0;
				}

				currentRow++;

				if (currentRow == rows)
				{
					finished = true;
				}
			}
		}

		System.out.println(builder.toString());

		return builder.toString();
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

			double d = (a - (i+1)) * 4; // distance between layers

			double plotWidth = Math.abs(1 + d / layers.length) * image.getWidth();
			double plotHeight = Math.abs(1 + d / layers.length) * image.getHeight();

			double alpha = 1 - Math.abs(d) / 3.33; // alpha fade

			gc.setGlobalAlpha(alpha);

			gc.drawImage(image, (width - plotWidth) / 2, (height - plotHeight) / 2, plotWidth, plotHeight);
		}
	}
}