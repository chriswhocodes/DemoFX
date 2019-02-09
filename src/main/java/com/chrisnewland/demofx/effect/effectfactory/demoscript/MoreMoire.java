/*
 * Copyright (c) 2017 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.effectfactory.demoscript;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fade.FadeOutEffect;
import com.chrisnewland.demofx.effect.fake3d.Starfield;
import com.chrisnewland.demofx.effect.shape.SineLines;
import com.chrisnewland.demofx.effect.text.TypeText;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MoreMoire implements IEffectFactory
{
	private List<IEffect> effects = new ArrayList<>();

	@Override public List<IEffect> getEffects(DemoConfig config)
	{
		String audio = getClass().getResource("/moire.mp3").toExternalForm();

		config.setAudioFilename(audio);

		long length = 132_000;

		long bar = 2667;

		String text = "Presenting|A JavaFX demoscene production|More MoreMoire|built with the DemoFX Engine|Coding by chriswhocodes|Music by David Newland|Inspired by Numberphile's video|Freaky Dot Patterns|by Tadashi Tokieda|Source code at|github.com/chriswhocodes/DemoFX";

		addEffect(0, length, new Starfield(config, 20_000, 0.01, Color.DEEPSKYBLUE));

		addEffect(2 * bar, length - 2 * bar, new SineLines(config));

		addEffect(4 * bar, length - 4 * bar, new com.chrisnewland.demofx.effect.sprite.MoreMoire(config));

		addEffect(6 * bar, length - 6 * bar, new TypeText(config, text, "\\|", false, 47.5, 20));

		addEffect(length - 5_000, length + 1000, new FadeOutEffect(config));

		return effects;
	}

	private void addEffect(long start, long duration, IEffect effect)
	{
		effect.setStartOffsetMillis(start);
		effect.setStopOffsetMillis(start + duration);
		effects.add(effect);
	}
}