/*
 * Copyright (c) 2015-2017 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.effectfactory;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.fade.FadeOutEffect;
import com.chrisnewland.demofx.effect.fake3d.SnowField;
import com.chrisnewland.demofx.effect.ray.RayTrace;
import com.chrisnewland.demofx.effect.real3d.TexturedCube;
import com.chrisnewland.demofx.effect.text.TextWaveSprite;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class ChristmasFXScriptFactory implements IEffectFactory
{
	private List<IEffect> effects = new ArrayList<>();

	@Override
	public List<IEffect> getEffects(DemoConfig config)
	{
		String audio = getClass().getResource("/silent.wav").toExternalForm();

		config.setAudioFilename(audio);

		Image snowflake = ImageUtil.loadImageFromResources("flake.png");

		// https://pixabay.com/en/wrapping-paper-santa-clauses-funny-235940/
		WritableImage paper = ImageUtil.loadWritableImageFromResources("paper.jpg");

		long length = 48_000;

		addEffect(0, length, new RayTrace(config));
		addEffect(0, length, new SnowField(config, 8000, snowflake));

		addEffect(0, length, new TexturedCube(config, paper, 240, 1, 1, 240, 480, 32, 32));

		addEffect(0, length, new TextWaveSprite(config, new String[]
		{
				"Merry Christmas from chriswhocodes and DemoFX", "Wishing you a bug-free new year", "May your build systems behave, your unit tests run green, and may father JITmas bring you all the speedups you asked for ..."
		}, config.getHeight() * 0.1, 0.75, 6));

		addEffect(length - 5_000, length+1000, new FadeOutEffect(config));

		return effects;
	}

	private void addEffect(long start, long duration, IEffect effect)
	{
		effect.setStartOffsetMillis(start);
		effect.setStopOffsetMillis(start + duration);
		effects.add(effect);

		System.out.println("Adding " + start + " to " + (start + duration) + " " + effect.getClass().getName());
	}
}