/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.PreCalc;

public abstract class AbstractEffect implements IEffect
{
	protected final GraphicsContext gc;

	protected int itemCount;

	protected final int width;
	protected final int height;

	protected final int halfWidth;
	protected final int halfHeight;

	protected long lastSecond;
	protected int frameCount = 0;
	protected int framesPerSecond = 0;
	protected String itemName = null;

	private long lastRenderNanos = 0;
	private long averageRenderNanos = 0;
	private long count = 0;

	private long now;
	private StringBuilder builder = new StringBuilder();

	protected PreCalc precalc;

	protected final double getRandomDouble(double min, double max)
	{
		Random random = new Random();
		return min + (max - min) * random.nextDouble();
	}

	protected final int getRandomIntInclusive(int min, int max)
	{
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}

	public AbstractEffect(GraphicsContext gc, DemoConfig config)
	{
		this.gc = gc;

		precalc = new PreCalc(config);

		this.itemCount = config.getCount();

		this.width = config.getWidth();
		this.height = config.getHeight();

		this.halfWidth = width / 2;
		this.halfHeight = height / 2;

		initialise();
	}

	protected abstract void initialise();

	@Override
	public void updateStatistics(long renderNanos)
	{
		frameCount++;
		lastRenderNanos = renderNanos;

		now = System.currentTimeMillis();

		if (now - lastSecond > 1000)
		{
			framesPerSecond = frameCount;
			frameCount = 0;
			lastSecond = now;

			averageRenderNanos += (renderNanos - averageRenderNanos) / ++count;
		}
	}

	@Override
	public String getStatistics()
	{
		builder.setLength(0);

		//builder.append(width).append("x").append(height).append(" | ");

		builder.append(framesPerSecond).append(" fps | ");

		if (itemCount > -1)
		{
			builder.append(itemCount).append(' ').append(itemName).append(" | ");
		}

		builder.append("render ");

		formatNanos(builder, lastRenderNanos);

		builder.append(" | avg render ");

		formatNanos(builder, averageRenderNanos);

		return builder.toString();
	}

	private void formatNanos(StringBuilder builder, long nanos)
	{
		if (nanos > 5_000_000)
		{
			builder.append(nanos / 1_000_000).append("ms");
		}
		else if (nanos > 5_000)
		{
			builder.append(nanos / 1_000).append("us");
		}
		else
		{
			builder.append(nanos).append("ns");
		}
	}

	protected final void fillBackground(int red, int green, int blue)
	{
		fillBackground(Color.rgb(red, green, blue));
	}

	protected final void fillBackground(Color colour)
	{
		gc.setFill(colour);

		gc.fillRect(0, 0, width, height);
	}
}