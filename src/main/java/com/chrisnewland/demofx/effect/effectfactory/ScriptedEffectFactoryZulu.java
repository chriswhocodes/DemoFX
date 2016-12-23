/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.effectfactory;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.fake3d.Sprite3D;
import com.chrisnewland.demofx.effect.fake3d.Starfield;
import com.chrisnewland.demofx.effect.text.TextWave;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScriptedEffectFactoryZulu implements IEffectFactory
{
	private static DemoConfig config;

	@Override
	public List<IEffect> getEffects(DemoConfig config)
	{
		ScriptedEffectFactoryZulu.config = config;

		List<IEffect> effects = new ArrayList<>();

		// get ready for screen capture
		long startDelay = 15000;

		long startIntro = startDelay;
		long stopIntro = startIntro + 38_000;

		Starfield starField1 = new Starfield(config, 10_000, startIntro, stopIntro);
		effects.add(starField1);

		buildText3D(effects, "Zulu + OpenJFX", startIntro, stopIntro);

		Font fontEffectDescription = Font.font("Courier New", FontWeight.BOLD, 128);
		double twDescriptionYpos = config.getHeight() - 64;
		double twDescriptionAmplitude = 50;
		double twSpeed = 10;

		Color twDescriptionColor = Color.WHITE;

		String[] twStringsDescIntro = new String[] {
				"Zulu JDK From Azul Systems",
				"TCK-certified OpenJDK builds packaged for Linux, OSX, Windows, and the Cloud",
				"Support options available for Java 8, 7, and 6",
				"www.zulu.org" };

		TextWave twDescriptionIntro = new TextWave(config, twStringsDescIntro, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);
		twDescriptionIntro.setStartOffsetMillis(startIntro + 3_000);
		twDescriptionIntro.setStopOffsetMillis(stopIntro);
		
		effects.add(twDescriptionIntro);

		return effects;
	}

	private static void buildText3D(List<IEffect> effects, String text, long start, long duration)
	{
		Sprite3D text3D = new Sprite3D(config, text, 30);
		text3D.setStartOffsetMillis(start);
		text3D.setStopOffsetMillis(start+duration);
		effects.add(text3D);
	}
}