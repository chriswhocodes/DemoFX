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

public class BallWave extends AbstractEffect
{
	private List<BallGrid> sentenceList;
	private int sentenceIndex = 0;
	private int lineWidth;

	private Image imageBall;
	private double imgSize = 14;

	private double xOffset = 0;
	private double yOffset = 0;

	private static final int SPEED = 8;

	public BallWave(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
		xOffset = width;
	}

	@Override
	protected void initialise()
	{
		String[] sentences = new String[] { "DemoFX by @chriswhocodes", "Hey Chris! The 1990s called and they want their demoscene back!" };

		sentenceList = new ArrayList<>();

		for (String sentence : sentences)
		{
			sentenceList.add(TextUtil.createBallGridList(sentence, gc));
		}

		// http://pixabay.com/en/orb-ball-globe-glossy-glow-red-150545/
		imageBall = new Image(getClass().getResourceAsStream("/glassyball.png"));
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

		yOffset = halfHeight - sentenceBallGrid.getHeight() * imgSize / 2;

		if (xOffset < -lineWidth * imgSize)
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
			double x = xOffset + col * imgSize;
		
			if (onScreen(x))
			{					
				for (int row = 0; row < sentenceBallGrid.getHeight(); row++)
				{
					if (sentenceBallGrid.isSet(col, row))
					{
						double y = yOffset + (row * imgSize) + (precalc.sin(Math.abs(x + imgSize)) * 30);

						gc.drawImage(imageBall, x, y, imgSize, imgSize);
					}
				}
			}
		}
	}

	private final boolean onScreen(double x)
	{
		return x > -imgSize && x < width;
	}
}