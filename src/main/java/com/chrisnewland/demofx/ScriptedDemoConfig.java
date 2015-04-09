package com.chrisnewland.demofx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.shape.Grid;
import com.chrisnewland.demofx.effect.text.TextWave;

public class ScriptedDemoConfig
{
	public static List<IEffect> getEffects(GraphicsContext gc, DemoConfig config)
	{
		List<IEffect> effects = new ArrayList<>();

		long elapsed = 0;

		// ================================================

		TextWave tw1 = new TextWave(gc, config);
		tw1.customInitialise(
				Arrays.asList(new String[] { "DemoFX Part II by @chriswhocodes",
						"TextWave effect - plot letters on a sine wave with automatic kerning" }), elapsed, 30_000, false);

		// ================================================

		elapsed += 13_000;

		// ================================================

		Grid grid1 = new Grid(gc, config);
		grid1.setStartMillis(elapsed);

		// ================================================

		TextWave tw2 = new TextWave(gc, config);
		tw2.customInitialise(
				Arrays.asList(new String[] { "DemoFX Part II by @chriswhocodes",
						"TextWave effect - plot letters on a sine wave with automatic kerning" }), elapsed, 30_000, false);

		// ================================================

		elapsed += 13_000;

		// ================================================

		effects.add(grid1);
		effects.add(tw1);

		return effects;
	}
}
