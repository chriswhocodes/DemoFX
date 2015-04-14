package com.chrisnewland.demofx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.fake3d.Dots;
import com.chrisnewland.demofx.effect.fake3d.Dots.Shape3D;
import com.chrisnewland.demofx.effect.shape.Grid;
import com.chrisnewland.demofx.effect.text.TextWave;

public class ScriptedDemoConfig
{
	
	// stars
	// textwave + stars
	//		DemoFX Part II by @chriswhocodes
	//		Layered Effects
	//		Effect Timelines

	// textwave description on other effects
	
	// spritewave + checkerboard
	// sierpinski + grid
	// mandelbrot + stars
	// starfield (less stars)  - ring
	// starfield - tube
	// starfield - cube
	// starfield - sphere
	
	public static List<IEffect> getEffects(GraphicsContext gc, DemoConfig config)
	{
		List<IEffect> effects = new ArrayList<>();

		long elapsed = 0;

		// ================================================

		TextWave tw1 = new TextWave(gc, config);
		tw1.customInitialise(
				Arrays.asList(new String[] {
						"DemoFX Part II by @chriswhocodes",
						"TextWave effect - plot letters on a sine wave with automatic kerning" }), elapsed, 30_000, false);

		// ================================================

		elapsed += 13_000;

		// ================================================

		Grid grid1 = new Grid(gc, config);
		grid1.setStartMillis(elapsed);

		// ================================================

		TextWave tw2 = new TextWave(gc, config);
		tw2.customInitialise(
				Arrays.asList(new String[] {
						"DemoFX Part II by @chriswhocodes",
						"TextWave effect - plot letters on a sine wave with automatic kerning" }), elapsed, 30_000, false);

		// ================================================
		
		Dots dots1 = new Dots(gc, config);
		dots1.customInitialise(Shape3D.RING, 0, 3000);
		
		Dots dots2 = new Dots(gc, config);
		dots2.customInitialise(Shape3D.TUBE, 3000, 6000);
		
		Dots dots3 = new Dots(gc, config);
		dots3.customInitialise(Shape3D.CUBE, 6000, 9000);
		
		Dots dots4 = new Dots(gc, config);
		dots4.customInitialise(Shape3D.SPHERE, 9000, 12000);
		
		// ================================================

		elapsed += 13_000;

		// ================================================

//		effects.add(grid1);
//		effects.add(tw1);
		effects.add(dots1);
		effects.add(dots2);
		effects.add(dots3);
		effects.add(dots4);


		return effects;
	}
}
