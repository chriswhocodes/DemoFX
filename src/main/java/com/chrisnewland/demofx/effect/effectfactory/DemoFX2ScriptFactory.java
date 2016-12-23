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
import com.chrisnewland.demofx.effect.fake3d.Sprite3D.Shape3D;
import com.chrisnewland.demofx.effect.fake3d.Starfield;
import com.chrisnewland.demofx.effect.fractal.Mandelbrot;
import com.chrisnewland.demofx.effect.fractal.Sierpinski;
import com.chrisnewland.demofx.effect.shape.Burst;
import com.chrisnewland.demofx.effect.shape.Checkerboard;
import com.chrisnewland.demofx.effect.shape.Grid;
import com.chrisnewland.demofx.effect.sprite.Bounce;
import com.chrisnewland.demofx.effect.text.Credits;
import com.chrisnewland.demofx.effect.text.SpriteWave;
import com.chrisnewland.demofx.effect.text.TextWave;
import com.chrisnewland.demofx.util.ShapeEffect;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class DemoFX2ScriptFactory implements IEffectFactory
{
	@Override
	public List<IEffect> getEffects(DemoConfig config)
	{
		List<IEffect> effects = new ArrayList<>();

		Font fontEffectDescription = Font.font("Courier New", FontWeight.BOLD, 72);
		double twDescriptionYpos = config.getHeight() - 64;
		double twDescriptionAmplitude = 16;
		double twSpeed = 12;

		Color twDescriptionColor = Color.SPRINGGREEN;

		// ================================================
		// Phase 1 - Starfield and TextWave
		// ================================================

		// get ready for screen capture
		long startDelay = 0;

		long startIntro = startDelay;
		long stopIntro = startIntro + 21_000;

		Starfield starField1 = new Starfield(config);
		starField1.setStartOffsetMillis(startIntro);
		starField1.setStopOffsetMillis(stopIntro);

		TextWave textWaveIntro = new TextWave(config, new String[] {
				"DemoFX Part II",
				"JavaFX Canvas Performance Demo",
				"Layers+Timelines+Fractals+Fonts+3D+Starfields, Oh My!" }, false);

		textWaveIntro.setStartOffsetMillis(startIntro);
		textWaveIntro.setStopOffsetMillis(stopIntro);

		TextWave twDescriptionIntro = new TextWave(config, new String[] {
				"Starfield and TextWave Effect" }, false, twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor,
				fontEffectDescription, twSpeed);

		textWaveIntro.setStartOffsetMillis(startIntro + 3000);
		textWaveIntro.setStopOffsetMillis(stopIntro);

		effects.add(starField1);

		effects.add(textWaveIntro);

		effects.add(twDescriptionIntro);

		// ================================================
		// Phase 2 - CheckerBoard and SpriteWave
		// ================================================

		twSpeed = 10;

		long startCheckerboard = stopIntro;
		long stopCheckerboard = startCheckerboard + 20_000;

		Checkerboard checkerBoard1 = new Checkerboard(config);
		checkerBoard1.setStartOffsetMillis(startCheckerboard);
		checkerBoard1.setStopOffsetMillis(stopCheckerboard);

		SpriteWave spritewave1 = new SpriteWave(config, new String[] {
				"Rendered strings diced into pixel grids and plotted as sprites" }, false);
		spritewave1.setStartOffsetMillis(startCheckerboard);
		spritewave1.setStopOffsetMillis(stopCheckerboard);

		TextWave twDescriptionSpriteWave = new TextWave(config, new String[] {
				"Checkerboard and SpriteWave Effect",
				"In case you thought JavaFX was just for user interfaces!" }, false, twDescriptionYpos, twDescriptionAmplitude,
				twDescriptionColor, fontEffectDescription, twSpeed);

		twDescriptionSpriteWave.setStartOffsetMillis(startCheckerboard);
		twDescriptionSpriteWave.setStopOffsetMillis(stopCheckerboard);

		effects.add(checkerBoard1);

		effects.add(spritewave1);

		effects.add(twDescriptionSpriteWave);

		// ================================================
		// Phase 3 - Sierpinski Triangles
		// ================================================

		long startSierpinski = stopCheckerboard;
		long stopSierpinski = startSierpinski + 20_000;

		Sierpinski sierpinski = new Sierpinski(config);
		sierpinski.setStartOffsetMillis(startSierpinski);
		sierpinski.setStopOffsetMillis(stopSierpinski);

		TextWave twDescriptionSierpinski = new TextWave(config, new String[] {
				"Sierpinski Triangles Effect",
				"Uses the recursive method to build the render list and gc.fillPolygon() to plot",
				"Led to the discovery of a JavaFX filled polygon performance bug" }, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		twDescriptionSierpinski.setStartOffsetMillis(startSierpinski);
		twDescriptionSierpinski.setStopOffsetMillis(stopSierpinski);

		effects.add(sierpinski);

		effects.add(twDescriptionSierpinski);

		// ================================================
		// Phase 4 - Mandelbrot Zoomer
		// ================================================

		long startMandelbrot = stopSierpinski;
		long stopMandelbrot = startMandelbrot + 28_000;

		Mandelbrot mandelbrot = new Mandelbrot(config);
		mandelbrot.setStartOffsetMillis(startMandelbrot);
		mandelbrot.setStopOffsetMillis(stopMandelbrot);

		TextWave twDescriptionMandelbrot = new TextWave(config, new String[] {
				"Mandelbrot Zoomer Effect",
				"Dives into points of interest",
				"Plots using gc.fillRect()",
				"If you love fractals, check out the work of Paul Nylander at http://bugman123.com" }, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		twDescriptionMandelbrot.setStartOffsetMillis(startMandelbrot);
		twDescriptionMandelbrot.setStopOffsetMillis(stopMandelbrot);

		effects.add(mandelbrot);

		effects.add(twDescriptionMandelbrot);

		// ================================================
		// Phase 5 - Starfield and Dots 3D
		// ================================================

		long shapeDuration = 9_000;

		long startStarfield3D = stopMandelbrot;
		long stopStarField3D = startStarfield3D + 4 * shapeDuration;

		long startRing = startStarfield3D;
		long stopRing = startRing + shapeDuration;

		long startTube = stopRing;
		long stopTube = startTube + shapeDuration;

		long startCube = stopTube;
		long stopCube = startCube + shapeDuration;

		long startSphere = stopCube;
		long stopSphere = startSphere + shapeDuration;

		Starfield starField2 = new Starfield(config, 3000, startStarfield3D, stopStarField3D);

		Image image = new Image(DemoFX2ScriptFactory.class.getResourceAsStream("/glassyball.png"));

		Sprite3D ring = new Sprite3D(config);
		ring.addObject(Shape3D.RING, image, 1.0, startRing, stopRing);

		TextWave twDescriptionRing = new TextWave(config, new String[] {
				"And now for a bunch of 3D objects spinning around in space!" }, false, twDescriptionYpos, twDescriptionAmplitude,
				twDescriptionColor, fontEffectDescription, twSpeed);
		twDescriptionRing.setStartOffsetMillis(startRing);
		twDescriptionRing.setStopOffsetMillis(stopRing);

		Sprite3D tube = new Sprite3D(config);
		tube.addObject(Shape3D.TUBE, image, 1.0, startTube, stopTube);

		TextWave twDescriptionTube = new TextWave(config, new String[] {
				"Hey Chris, the 1990s called and they want their demoscene back!" }, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);
		twDescriptionTube.setStartOffsetMillis(startTube);
		twDescriptionTube.setStopOffsetMillis(stopTube);

		Sprite3D cube = new Sprite3D(config);
		cube.addObject(Shape3D.SOLIDCUBE, image, 1.0, startCube, stopCube);

		TextWave twDescriptionCube = new TextWave(config, new String[] {
				"Rotates, Translates, Depth-Sorts, and Plots" }, false, twDescriptionYpos, twDescriptionAmplitude,
				twDescriptionColor, fontEffectDescription, twSpeed);
		twDescriptionCube.setStartOffsetMillis(startCube);
		twDescriptionCube.setStopOffsetMillis(stopCube);

		Sprite3D sphere = new Sprite3D(config);
		sphere.addObject(Shape3D.SPHERE, image, 1.0, startSphere, stopSphere);

		TextWave twDescriptionSphere = new TextWave(config, new String[] {
				"This effect is called Sprite3D, real 3D calculations plotted with scaled sprites" }, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);
		twDescriptionSphere.setStartOffsetMillis(startSphere);
		twDescriptionSphere.setStopOffsetMillis(stopSphere);

		effects.add(starField2);

		effects.add(ring);
		effects.add(twDescriptionRing);

		effects.add(tube);
		effects.add(twDescriptionTube);

		effects.add(cube);
		effects.add(twDescriptionCube);

		effects.add(sphere);
		effects.add(twDescriptionSphere);

		// ================================================
		// Phase 6 - Credits
		// ================================================

		config.setItemCount(400);

		long startOutro = stopStarField3D;
		long stopOutro = 160_000 + startDelay;

		long creditEffectDuration = 8000;

		long startGrid = startOutro;
		long stopGrid = stopOutro;

		long startStars = startGrid + creditEffectDuration;
		long stopStars = startStars + creditEffectDuration;

		long startBounce = stopStars;
		long stopBounce = startBounce + creditEffectDuration;

		long startBurst = stopBounce;
		long stopBurst = startBurst + creditEffectDuration;

		Grid grid = new Grid(config);
		grid.setStartOffsetMillis(startOutro);
		grid.setStopOffsetMillis(stopGrid);

		ShapeEffect stars = new ShapeEffect(config, 5);
		stars.setDoubleAngle(true);
		stars.setStartOffsetMillis(startStars);
		stars.setStopOffsetMillis(stopStars);

		Bounce bounce = new Bounce(config, 14);
		bounce.setStartOffsetMillis(startBounce);
		bounce.setStopOffsetMillis(stopBounce);

		Burst burst = new Burst(config);
		burst.setStartOffsetMillis(startBurst);
		burst.setStopOffsetMillis(stopBurst);

		Credits credits = new Credits(config);
		credits.setStartOffsetMillis(startOutro);
		credits.setStopOffsetMillis(stopOutro);

		effects.add(grid);
		effects.add(stars);
		effects.add(bounce);
		effects.add(burst);

		effects.add(credits);

		return effects;
	}
}