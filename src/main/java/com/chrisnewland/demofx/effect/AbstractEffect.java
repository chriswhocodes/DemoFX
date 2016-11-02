/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.util.PreCalc;

public abstract class AbstractEffect implements IEffect
{
	protected final GraphicsContext gc;

	protected int itemCount;

	protected final double width;
	protected final double height;

	protected final double halfWidth;
	protected final double halfHeight;

	protected double canvasRotationAngle = 0;

	protected long lastSecond;
	protected int frameCount = 0;
	protected int framesPerSecond = 0;

	private long lastRenderNanos = 0;
	private long averageRenderNanos = 0;
	private long count = 0;

	private long now;
	private StringBuilder builder = new StringBuilder();

	protected PreCalc precalc;
	protected DemoConfig config;

	private double colourCycleAngle = 0;

	protected long effectStartMillis = -1;
	protected long effectStopMillis = -1;
	protected boolean effectFinished = false;

	private static long scriptStartTimeMillis;

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
		this.config = config;

		precalc = new PreCalc(config);

		this.itemCount = config.getCount();

		this.width = config.getWidth();
		this.height = config.getHeight();

		this.halfWidth = width / 2;
		this.halfHeight = height / 2;

		initialise();
	}

	public static void setScriptStartTimeMillis(long millis)
	{
		scriptStartTimeMillis = millis;
	}

	protected abstract void initialise();

	@Override
	public void updateStatistics(long renderNanos)
	{
		frameCount++;
		lastRenderNanos = renderNanos;

		now = System.currentTimeMillis();

		if (now - lastSecond > 1000L)
		{
			framesPerSecond = frameCount;
			frameCount = 0;
			lastSecond = now;

			averageRenderNanos += (renderNanos - averageRenderNanos) / ++count;
            
                        System.out.println(config.getEffect() + " = " + framesPerSecond + " fps");
		}
	}

	@Override
	public String getStatistics()
	{
		builder.setLength(0);

		builder.append(width).append("x").append(height).append(" | ");

		builder.append(framesPerSecond).append(" fps | ");

		if (config.isUseScriptedDemoConfig())
		{
			long elapsedSeconds = (System.currentTimeMillis() - scriptStartTimeMillis) / 1000;
			builder.append("Demo mode: ").append(elapsedSeconds).append("s | ");
		}
		else
		{

			if (itemCount > -1)
			{
				builder.append(itemCount).append(" | ");
			}

			builder.append(config.getEffect()).append(" | ");
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

	@Override
	public void renderBackground()
	{
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

	protected final Color getCycleColour()
	{
		colourCycleAngle++;

		if (colourCycleAngle >= 360)
		{
			colourCycleAngle -= 360;
		}

		double redFraction = 2 + precalc.sin(colourCycleAngle);
		double greenFraction = 2 + precalc.sin(360 - colourCycleAngle);
		double blueFraction = 2 + precalc.cos(colourCycleAngle);

		int red = (int) (redFraction * 32);
		int green = (int) (greenFraction * 32);
		int blue = (int) (blueFraction * 32);

		return Color.rgb(red, green, blue);
	}

	protected final void rotateCanvas(double rotation)
	{
		Rotate r = new Rotate(canvasRotationAngle, halfWidth, halfHeight);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

		canvasRotationAngle += rotation;

		if (canvasRotationAngle >= 360)
		{
			canvasRotationAngle -= 360;
		}
	}

	@Override
	public void stop()
	{
	}

	@Override
	public void setStartMillis(long start)
	{
		this.effectStartMillis = start;
	}

	@Override
	public void setStopMillis(long stop)
	{
		this.effectStopMillis = stop;
	}

	@Override
	public long getStartMillis()
	{
		return effectStartMillis;
	}

	@Override
	public long getStopMillis()
	{
		return effectStopMillis;
	}

	public boolean isShowEffect(long elapsed)
	{
		boolean showEffect = !effectFinished;

		if (effectStartMillis >= 0)
		{
			if (elapsed < effectStartMillis)
			{
				showEffect = false;
			}
		}

		if (effectStopMillis >= 0)
		{
			if (elapsed > effectStopMillis)
			{
				showEffect = false;
			}
		}

		return showEffect;
	}
}