/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.BallGrid;
import com.chrisnewland.demofx.util.TextUtil;

public class SpriteWave extends AbstractEffect
{
	private List<BallGrid> sentenceList;
	private int sentenceIndex = 0;
	private int lineWidth;

	private Image imageBall;

	private double xOffset = 0;
	private double yOffset = 0;

	private static final double IMAGE_SIZE = 20;
	private static final double AMPLITUDE = 20;
	private static final double SPEED = 12;

	public SpriteWave(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
		xOffset = width;
	}

	@Override
	protected void initialise()
	{
		String[] sentences = new String[] { "SpriteWave effect - convert text into pixel grids and plot with sprites",
				"github.com/chriswhocodes/DemoFX" };

		sentenceList = new ArrayList<>();

		for (String sentence : sentences)
		{
			sentenceList.add(TextUtil.createBallGridList(sentence, gc));
		}

		imageBall = new Image(getClass().getResourceAsStream("/acid.png"));
	}

	@Override
	public void renderBackground()
	{
		fillBackground(Color.BLACK);
	}

	@Override
	public void renderForeground()
	{
		BallGrid sentenceBallGrid = sentenceList.get(sentenceIndex);

		lineWidth = sentenceBallGrid.getWidth();

		xOffset -= SPEED;

		yOffset = halfHeight - sentenceBallGrid.getHeight() * IMAGE_SIZE / 2;

		if (xOffset < -lineWidth * IMAGE_SIZE)
		{
			sentenceIndex++;

			if (sentenceIndex == sentenceList.size())
			{
				sentenceIndex = 0;
			}

			xOffset = width;
		}

		plotSentence(sentenceBallGrid);
	}

	private final void plotSentence(BallGrid sentenceBallGrid)
	{
		for (int col = 0; col < sentenceBallGrid.getWidth(); col++)
		{
			double x = xOffset + col * IMAGE_SIZE;

			if (onScreen(x))
			{
				for (int row = 0; row < sentenceBallGrid.getHeight(); row++)
				{
					if (sentenceBallGrid.isSet(col, row))
					{
						double y = yOffset + (row * IMAGE_SIZE) + (precalc.sin(Math.abs(x + IMAGE_SIZE)) * AMPLITUDE);

						gc.drawImage(imageBall, x, y, IMAGE_SIZE, IMAGE_SIZE);
					}
				}
			}
		}
	}

	private final boolean onScreen(double x)
	{
		return x > -IMAGE_SIZE && x < width;
	}
}