/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.Random;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class WordSearch extends AbstractEffect
{
	private String[] lines;

	private static final int GRID_X = 30;
	private static final int GRID_Y = 30;
	
	private static final int MAX_TRIES = 100;
	private static final int INTERVAL = 200;


	private char[][] charGrid;
	private int[][] tryCount;
	private Color[][] colourGrid;

	private static final Random RANDOM = new Random();

	private static final double INITIAL_FONT_SIZE = 30;

	private Font font;

	private long lastChange = 0;

	public WordSearch(DemoConfig config)
	{
		super(config);

		init("DemoFX III\n\nA JavaFX demo by\n\n@chriswhocodes");
	}
	
	public WordSearch(DemoConfig config, String string)
	{
		super(config);

		init(string);
	}
	
	private void init(String string)
	{
		lines = string.toUpperCase().split("\n");
		
		if (lines.length > GRID_Y)
		{
			throw new RuntimeException("More lines than GRID_Y: " + lines.length + "/" + GRID_Y);
		}
		
		for (String line : lines)
		{
			if (line.length() > GRID_X)
			{
				throw new RuntimeException("Line longer than GRID_X: " + line);
			}
		}
		
		tryCount = new int[GRID_X][GRID_Y];
		charGrid = new char[GRID_X][GRID_Y];
		colourGrid = new Color[GRID_X][GRID_Y];

		font = Font.font("monospace", FontWeight.BOLD, INITIAL_FONT_SIZE);
	}

	@Override
	public void renderForeground()
	{
		long now = System.currentTimeMillis();

		if (now - lastChange > INTERVAL)
		{
			randomise();
			lastChange = now;
		}

		overlay();

		plotText();
	}

	private void randomise()
	{
		for (int y = 0; y < GRID_Y; y++)
		{
			for (int x = 0; x < GRID_X; x++)
			{
				charGrid[x][y] = getRandomPrintableChar();
				colourGrid[x][y] = getRandomGreen(16, 48);
			}
		}
	}

	private char getRandomPrintableChar()
	{
		char letter = (char) (32 + RANDOM.nextInt(65 + 26 - 32));

		return letter;
	}

	private void overlay()
	{
		int lineCount = lines.length;

		int startY = (GRID_Y - lineCount) / 2;

		int y = 0;

		for (String line : lines)
		{
			int lineLength = line.length();

			int startX = (GRID_X - lineLength) / 2;
			
			int overlayY = startY + y;

			for (int x = 0; x < lineLength; x++)
			{
				char c = line.charAt(x);

				if (tryCount[x][y] < MAX_TRIES)
				{
					char randomChar = getRandomPrintableChar();
					
					if (c == randomChar)
					{
						tryCount[x][y] = MAX_TRIES;
					}
					else
					{
						tryCount[x][y]++;
						c = randomChar;
					}
				}
				
				int overlayX = startX + x;

				charGrid[overlayX][overlayY] = c;
				colourGrid[overlayX][overlayY] = Color.WHITE;
			}

			y++;
		}
	}

	private final void plotText()
	{
		gc.setFont(font);

		double xStep = width / GRID_X;
		double yStep = height / GRID_Y;

		for (int y = 0; y < GRID_Y; y++)
		{
			for (int x = 0; x < GRID_X; x++)
			{
				double plotX = (xStep / 4) + x * xStep;
				double plotY = (yStep * 0.75) + y * yStep;

				gc.setFill(colourGrid[x][y]);
				
				char c = charGrid[x][y];
			
				gc.fillText(Character.toString(c), plotX, plotY);
			}
		}
	}
}