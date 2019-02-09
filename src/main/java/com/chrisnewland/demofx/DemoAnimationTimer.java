/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import java.util.List;

import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.measurement.Measurements;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class DemoAnimationTimer extends AnimationTimer
{
	public static final int SAMPLE_PER_SECOND = 4;
	public static final long UPDATE_STATS_MILLIS = 1000L / SAMPLE_PER_SECOND;

	private long startTime = 0;

	private GraphicsContext onScreenGC;

	private final Measurements measurements = new Measurements();

	private Label statsLabel;

	private long lastStatsUpdate;
	private int frameCount = 0;

	private long lastRenderNanos = 0;
	private long averageRenderNanos = 0;

	private long count = 0;
	private StringBuilder builder = new StringBuilder();

	private static long scriptStartTimeMillis;

	private DemoFX parent;
	private DemoConfig config;

	private IEffect[] effectArray;
	private final int effectCount;

	public DemoAnimationTimer(DemoFX parent, DemoConfig config, Label statsLabel, List<IEffect> effects)
	{
		super();
		
		this.parent = parent;
		this.config = config;
		this.onScreenGC = config.getOnScreenCanvasGC();
		this.statsLabel = statsLabel;
		this.effectArray = effects.toArray(new IEffect[effects.size()]);

		effectCount = effectArray.length;
	}

	@Override
	public void start()
	{
		super.start();
		
		startTime = System.currentTimeMillis();
		scriptStartTimeMillis = startTime;

		System.out.println("DemoAnimationTimer start " + startTime);

		int runForSeconds = config.getRunForSeconds();

		if (runForSeconds != -1)
		{
			for (IEffect effect : effectArray)
			{
				effect.setStartOffsetMillis(0);

				effect.setStopOffsetMillis(1000 * runForSeconds);
			}

			System.out.println("Running for " + runForSeconds + " seconds");
		}

	}

	@Override
	public void handle(long renderStartNanos)
	{
		long now = System.currentTimeMillis();

		long elapsed = now - startTime;

		boolean effectsUsed = false;

		blackBackground();

		for (int i = 0; i < effectCount; i++)
		{
			IEffect currentEffect = effectArray[i];

			if (currentEffect.isVisible(elapsed))
			{
				plotEffect(currentEffect);

				effectsUsed = true;
			}
		}

		long renderEndNanos = System.nanoTime();

		updateStatistics(now, renderEndNanos - renderStartNanos);

		if (!effectsUsed)
		{
			stop();
		}
	}

	private void blackBackground()
	{
		onScreenGC.setFill(Color.BLACK);
		onScreenGC.fillRect(0, 0, config.getWidth(), config.getHeight());
	}

	@Override
	public void stop()
	{
		parent.timerCompleted(measurements);
		super.stop();
	}

	private final void plotEffect(IEffect effect)
	{
		onScreenGC.save();

		effect.renderForeground();

		onScreenGC.restore();
	}

	private void updateStatistics(long now, long renderNanos)
	{
		frameCount++;

		if (now - lastStatsUpdate > UPDATE_STATS_MILLIS)
		{
			int framesPerSecond = frameCount * SAMPLE_PER_SECOND;

			frameCount = 0;

			lastStatsUpdate = now;

			lastRenderNanos = renderNanos;

			if (averageRenderNanos == 0)
			{
				averageRenderNanos = renderNanos;
			}
			else
			{
				averageRenderNanos += (renderNanos - averageRenderNanos) / ++count;
			}

			if (!config.isFullScreen())
			{
				statsLabel.setText(getStatsString(now, framesPerSecond));
			}

			measurements.measure(now - startTime, framesPerSecond);

			// System.out.println(config.getEffect() + " = " + framesPerSecond +
			// " fps");
		}
	}

	private String getStatsString(long now, int framesPerSecond)
	{
		builder.setLength(0);

		builder.append(config.getWidth()).append("x").append(config.getHeight()).append(" | ");

		builder.append(framesPerSecond).append(" fps | ");

		if (config.getDemoScriptName() != null)
		{
			long elapsedSeconds = (now - scriptStartTimeMillis) / 1000L;
			builder.append("Demo mode: ").append(elapsedSeconds).append("s | ");
		}
		else
		{
			if (config.getCount() > -1)
			{
				builder.append(config.getCount()).append(" | ");
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
		if (nanos > 5_000_000L)
		{
			builder.append(nanos / 1_000_000L).append("ms");
		}
		else if (nanos > 5_000L)
		{
			builder.append(nanos / 1_000L).append("us");
		}
		else
		{
			builder.append(nanos).append("ns");
		}
	}
}