/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Sprites extends AbstractEffect
{
	private PathData[] paths;

	private Image image;

	public Sprites(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		itemName = "Sprites";

		List<Point> points = new ArrayList<Point>();

		int radius = height / 4;

		anticlockwise(points, radius, 0, 0, 180, true, true);
		clockwise(points, radius, 0, -2 * radius, 360, true, true);
		anticlockwise(points, radius, 0, 0, 180, false, false);
		PathData p1 = new PathData(points, (int) (width * .25), halfHeight + radius, 32, 16);

		paths = new PathData[] { p1 };

		for (PathData pd : paths)
		{
			itemCount += pd.getFollowers();
		}

		image = new Image(getClass().getResourceAsStream("/javafx.png"));
	}

	private void rectangleClockwise(List<Point> points, int width, int height, int speed)
	{
		int x = 0;
		int y = 0;

		for (; x < width; x += speed)
		{
			points.add(new Point(x, y));
		}
		for (; y < height; y += speed)
		{
			points.add(new Point(x, y));
		}
		for (; x > 0; x -= speed)
		{
			points.add(new Point(x, y));
		}
		for (; y > 0; y -= speed)
		{
			points.add(new Point(x, y));
		}
	}

	private void rectangleAnticlockwise(List<Point> points, int width, int height, int speed)
	{
		int x = 0;
		int y = 0;

		for (y = 0; y < height; y += speed)
		{
			points.add(new Point(x, y));
		}
		for (; x < width; x += speed)
		{
			points.add(new Point(x, y));
		}
		for (; y > 0; y -= speed)
		{
			points.add(new Point(x, y));
		}
		for (; x > 0; x -= speed)
		{
			points.add(new Point(x, y));
		}
	}

	private void spiral(List<Point> points, int maxRadius, double radiusStep)
	{

		int angle = 0;
		double radius = 0;

		while (radius < maxRadius)
		{
			int x;
			int y;
			x = (int) (precalc.sin(angle) * radius);
			y = (int) (precalc.cos(angle) * radius);

			points.add(new Point(x, y));

			angle += 1;

			if (angle >= 360)
			{
				angle = 0;
			}

			radius += radiusStep;

		}
	}

	private void wave(List<Point> points, int amplitude, int period, int width, int speed)
	{
		int angle = 0;

		int angleStep = 360 / (period / speed);

		for (int x = 0; x < width; x += speed)
		{
			int y = (int) (precalc.sin(angle) * amplitude);

			points.add(new Point(x, y));

			angle += angleStep;

			if (angle >= 360)
			{
				angle = 0;
			}
		}
	}

	private void anticlockwise(List<Point> points, int radius, int ox, int oy, int degrees, boolean left, boolean up)
	{
		int angle = 0;

		for (int i = 0; i < degrees; i++)
		{
			int x;
			int y;

			if (left)
			{
				x = ox + (int) (precalc.sin(angle) * radius);
			}
			else
			{
				x = ox - (int) (precalc.sin(angle) * radius);
			}

			if (up)
			{
				y = oy + (int) (precalc.cos(angle) * radius);
			}
			else
			{
				y = oy - (int) (precalc.cos(angle) * radius);
			}

			points.add(new Point(x, y));

			angle += 1;

			if (angle >= 360)
			{
				angle = 0;
			}
		}
	}

	private void clockwise(List<Point> points, int radius, int ox, int oy, int degrees, boolean left, boolean up)
	{
		int angle = 0;

		for (int i = 0; i < degrees; i++)
		{
			int x;
			int y;

			if (left)
			{
				x = ox + (int) (1 - precalc.sin(angle) * radius);
			}
			else
			{
				x = ox - (int) (1 - precalc.sin(angle) * radius);
			}
			if (up)
			{
				y = oy + (int) (precalc.cos(angle) * radius);
			}
			else
			{
				y = oy - (int) (precalc.cos(angle) * radius);
			}

			points.add(new Point(x, y));

			angle += 1;

			if (angle >= 360)
			{
				angle = 0;
			}
		}
	}

	@Override
	public void render()
	{
		long renderStartNanos = System.nanoTime();

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);

		for (int p = 0; p < paths.length; p++)
		{
			PathData path = paths[p];

			// for (int i = 0; i <= path.showFollowers; i++)
			for (int i = path.getShowFollowers(); i > 0; i--)
			{
				int offset = path.getIndex() - (path.getFollowDelay() * i);

				if (offset < 0)
				{
					offset += path.getLength();
				}

				double bx = path.getOffsetX() + path.getX()[offset];
				double by = path.getOffsetY() + path.getY()[offset];

				double bScale = 3 - by / halfHeight;
				double size = image.getWidth() * bScale;

				gc.drawImage(image, bx, by, size, size);

			}

			path.incIndex();

		}

		long renderEndNanos = System.nanoTime();

		long renderNanos = renderEndNanos - renderStartNanos;

		updateFPS(renderNanos);
	}
}
