/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.BallGrid;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;

public class SpriteWave extends AbstractEffect
{
	private List<BallGrid> sentenceList;
	private int sentenceIndex = 0;
	private int lineWidth;

	private Image imageBall;

	private double xOffset = 0;
	private double yOffset = 0;

	private static final double IMAGE_SIZE = 12;
	private static final double AMPLITUDE = 28;
	private static final double SPEED = 12;

	private boolean loopStringList = true;

	public SpriteWave(DemoConfig config)
	{
		super(config);

		init(new String[] {
				"SpriteWave effect - convert text into pixel grids and plot with sprites",
				"github.com/chriswhocodes/DemoFX" }, true);
	}
	
	public SpriteWave(DemoConfig config, String[] sentences, boolean loop)
	{
		super(config);

		init(sentences, true);
	}
	
	private void init(String[] sentences, boolean loopStringList)
	{
		xOffset = width;

		this.loopStringList = loopStringList;

		sentenceList = new ArrayList<>();

		for (String sentence : sentences)
		{
			sentenceList.add(TextUtil.createBallGrid(sentence, gc));
		}

		imageBall = new Image(getClass().getResourceAsStream("/acid.png"));
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
				if (loopStringList)
				{
					sentenceIndex = 0;
				}
				else
				{
					effectFinished = true;
				}
			}

			xOffset = width;
		}

		if (!effectFinished)
		{
			plotSentence(sentenceBallGrid);
		}
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