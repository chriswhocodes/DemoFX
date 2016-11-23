/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import java.util.Random;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;

public class Glowboard extends AbstractEffect
{
	private int checkSize;
	private int halfCheck;

	private double glow[];
	private boolean brighter[];

	private int rows;
	private int cols;
	private int size;

	private double offsetX = 0;
	private double offsetY = 0;

	private static final double SPEED = 4;

	private static final double MAX_INTENSITY = 0.8;
	private static final double SLOW = 100;

	public enum Direction
	{
		N, NE, E, SE, S, SW, W, NW
	}

	private Direction dir;

	private Color base;

	public Glowboard(DemoConfig config)
	{
		super(config);

		init(16, Direction.NE, Color.rgb(0, 0, 200));
	}

	public Glowboard(DemoConfig config, int checkSize, Direction dir, Color colour)
	{
		super(config);
		
		init(checkSize, dir, colour);
	}
	
	private void init(int checkSize, Direction dir, Color colour)
	{
		this.checkSize = checkSize;

		this.dir = dir;

		this.base = colour;

		halfCheck = checkSize / 2;

		rows = (int) Math.ceil(height / checkSize);
		cols = 1 + (int) Math.ceil(width / checkSize);

		size = rows * cols;

		glow = new double[size];
		brighter = new boolean[size];

		Random random = new Random();

		for (int i = 0; i < size; i++)
		{
			glow[i] = precalc.getUnsignedRandom() * MAX_INTENSITY;
			brighter[i] = random.nextBoolean();
		}
	}

	@Override
	public void renderForeground()
	{
		plotBoard();

		move();
	}

	private final void plotBoard()
	{
		int col = 0;
		int row = 0;

		for (int i = 0; i < size; i++)
		{
			double xPos = offsetX + col * checkSize;

			col++;

			if (col == cols)
			{
				col = 0;
				row++;
			}

			double yPos = offsetY + row * checkSize;

			if (xPos < 0)
			{
				xPos += width;
			}
			else if (xPos > width)
			{
				xPos -= width;
			}

			if (yPos < 0)
			{
				yPos += height;
			}
			else if (yPos > height)
			{
				yPos -= height;
			}

			plot(xPos, yPos, i);
		}
	}

	private void move()
	{
		switch (dir)
		{
		case W:
		case NW:
		case SW:
			moveLeft();
			break;
		case E:
		case NE:
		case SE:
			moveRight();
			break;
		default:
			break;
		}

		switch (dir)
		{
		case N:
		case NW:
		case NE:
			moveUp();
			break;
		case S:
		case SW:
		case SE:
			moveDown();
			break;
		default:
			break;
		}
	}

	private void moveLeft()
	{
		offsetX -= SPEED;

		if (offsetX < -width)
		{
			offsetX += width;
		}
	}

	private void moveRight()
	{
		offsetX += SPEED;

		if (offsetX > width)
		{
			offsetX -= width;
		}
	}

	private void moveUp()
	{
		offsetY -= SPEED;

		if (offsetY < -height)
		{
			offsetY += height;
		}
	}

	private void moveDown()
	{
		offsetY += SPEED;

		if (offsetY > height)
		{
			offsetY -= height;
		}
	}

	private void plot(double xPos, double yPos, int index)
	{
		double intensity = getNextColour(index);

		glow[index] = intensity;

		int r = (int) (255 * intensity * base.getRed());
		int g = (int) (255 * intensity * base.getGreen());
		int b = (int) (255 * intensity * base.getBlue());

		Color colour = Color.rgb(r, g, b);

		gc.setFill(colour);

		gc.fillRect(xPos, yPos, halfCheck, halfCheck);
	}

	private double getNextColour(int index)
	{
		double intensity = glow[index];

		if (brighter[index])
		{
			intensity += precalc.getUnsignedRandom() / SLOW;

			if (intensity >= MAX_INTENSITY)
			{
				brighter[index] = false;
				intensity = MAX_INTENSITY;
			}
		}
		else
		{
			intensity -= precalc.getUnsignedRandom() / SLOW;

			if (intensity <= 0.0)
			{
				brighter[index] = true;
				intensity = 0.0;
			}
		}

		return intensity;
	}

}