/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import java.util.List;

import com.chrisnewland.demofx.effect.IEffect;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

public class DemoAnimationTimer extends AnimationTimer
{
	private long lastNanos = 0;
	
	private static final long ONE_SECOND_NANOS = 1_000_000_000L;
	
	private final long startTime = System.currentTimeMillis();

	private GraphicsContext gc;
	
	private Label statsLabel;
	
	private List<IEffect> effects;
	
	public DemoAnimationTimer(GraphicsContext gc, Label statsLabel, List<IEffect> effects)
	{
		super();
		
		this.gc = gc;
		this.statsLabel = statsLabel;
		this.effects = effects;
	}
	
	@Override
	public void handle(long startNanos)
	{
		IEffect currentEffect = null;

		boolean firstLayer = true;
		
		for (int i = 0; i < effects.size(); i++)
		{
			currentEffect = effects.get(i);
			
			long elapsed = System.currentTimeMillis() - startTime;
			
			if (currentEffect.isShowEffect(elapsed))
			{
				plotEffect(currentEffect, firstLayer);
				firstLayer = false;
			}
		}

		long renderNanos = System.nanoTime() - startNanos;

		currentEffect.updateStatistics(renderNanos);

		if (startNanos - lastNanos > ONE_SECOND_NANOS)
		{
			statsLabel.setText(currentEffect.getStatistics());
			lastNanos = startNanos;
		}
	}
	
	private final void plotEffect(IEffect effect, boolean firstLayer)
	{
		gc.save();
		
		if (firstLayer)
		{
			effect.renderBackground();
		}

		effect.renderForeground();
		
		gc.restore();
	}
}
