package com.chrisnewland.demofx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

public class ScriptedDemoConfig
{
	public static List<IEffect> getEffects(GraphicsContext gc, DemoConfig config)
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

		Starfield starField1 = new Starfield(gc, config);
		starField1.setStartMillis(startIntro);
		starField1.setStopMillis(stopIntro);

		List<String> twStringsIntro = Arrays.asList(new String[] { "DemoFX Part II", "JavaFX Canvas Performance Demo",
				"Layers+Timelines+Fractals+Fonts+3D+Starfields, Oh My!" });

		TextWave textWaveIntro = new TextWave(gc, config);
		textWaveIntro.customInitialise(twStringsIntro, startIntro, stopIntro, false);

		List<String> twStringsDescIntro = Arrays.asList(new String[] { "Starfield and TextWave Effect" });

		TextWave twDescriptionIntro = new TextWave(gc, config);
		twDescriptionIntro.customInitialise(twStringsDescIntro, startIntro+3000, stopIntro, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		effects.add(starField1);

		effects.add(textWaveIntro);

		effects.add(twDescriptionIntro);

		// ================================================
		// Phase 2 - CheckerBoard and SpriteWave
		// ================================================

		twSpeed = 10;

		long startCheckerboard = stopIntro;
		long stopCheckerboard = startCheckerboard + 20_000;

		Checkerboard checkerBoard1 = new Checkerboard(gc, config);
		checkerBoard1.setStartMillis(startCheckerboard);
		checkerBoard1.setStopMillis(stopCheckerboard);

		SpriteWave spritewave1 = new SpriteWave(gc, config);
		spritewave1.customInitialise(
				Arrays.asList(new String[] { "Rendered strings diced into pixel grids and plotted as sprites" }), startCheckerboard,
				stopCheckerboard, false);

		List<String> twStringsDescSpriteWave = Arrays.asList(new String[] { "Checkerboard and SpriteWave Effect",
				"In case you thought JavaFX was just for user interfaces!" });

		TextWave twDescriptionSpriteWave = new TextWave(gc, config);
		twDescriptionSpriteWave.customInitialise(twStringsDescSpriteWave, startCheckerboard, stopCheckerboard, false,
				twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		effects.add(checkerBoard1);

		effects.add(spritewave1);

		effects.add(twDescriptionSpriteWave);

		// ================================================
		// Phase 3 - Sierpinski Triangles
		// ================================================

		long startSierpinski = stopCheckerboard;
		long stopSierpinski = startSierpinski + 20_000;

		Sierpinski sierpinski = new Sierpinski(gc, config);
		sierpinski.setStartMillis(startSierpinski);
		sierpinski.setStopMillis(stopSierpinski);

		List<String> twStringsDescSierpinski = Arrays.asList(new String[] { "Sierpinski Triangles Effect",
				"Uses the recursive method to build the render list and gc.fillPolygon() to plot",
				"Led to the discovery of a JavaFX filled polygon performance bug" });

		TextWave twDescriptionSierpinski = new TextWave(gc, config);
		twDescriptionSierpinski.customInitialise(twStringsDescSierpinski, startSierpinski, stopSierpinski, false,
				twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		effects.add(sierpinski);

		effects.add(twDescriptionSierpinski);

		// ================================================
		// Phase 4 - Mandelbrot Zoomer
		// ================================================

		long startMandelbrot = stopSierpinski;
		long stopMandelbrot = startMandelbrot + 28_000;

		Mandelbrot mandelbrot = new Mandelbrot(gc, config);
		mandelbrot.setStartMillis(startMandelbrot);
		mandelbrot.setStopMillis(stopMandelbrot);

		List<String> twDescMandelbrot = Arrays.asList(new String[] { "Mandelbrot Zoomer Effect", "Dives into points of interest",
				"Plots using gc.fillRect()", "If you love fractals, check out the work of Paul Nylander at http://bugman123.com" });

		TextWave twDescriptionMandelbrot = new TextWave(gc, config);
		twDescriptionMandelbrot.customInitialise(twDescMandelbrot, startMandelbrot, stopMandelbrot, false, twDescriptionYpos,
				twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

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

		Starfield starField2 = new Starfield(gc, config);
		starField2.customInitialise(3000, startStarfield3D, stopStarField3D);

		Sprite3D ring = new Sprite3D(gc, config);
		ring.customInitialise(Shape3D.RING, startRing, stopRing);

		TextWave twDescriptionRing = new TextWave(gc, config);
		twDescriptionRing.customInitialise(
				Arrays.asList(new String[] { "And now for a bunch of 3D objects spinning around in space!" }), startRing, stopRing,
				false, twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		Sprite3D tube = new Sprite3D(gc, config);
		tube.customInitialise(Shape3D.TUBE, startTube, stopTube);

		TextWave twDescriptionTube = new TextWave(gc, config);
		twDescriptionTube.customInitialise(
				Arrays.asList(new String[] { "Hey Chris, the 1990s called and they want their demoscene back!" }), startTube,
				stopTube, false, twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor, fontEffectDescription, twSpeed);

		Sprite3D cube = new Sprite3D(gc, config);
		cube.customInitialise(Shape3D.CUBE, startCube, stopCube);

		TextWave twDescriptionCube = new TextWave(gc, config);
		twDescriptionCube.customInitialise(Arrays.asList(new String[] { "Rotates, Translates, Depth-Sorts, and Plots" }),
				startCube, stopCube, false, twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor, fontEffectDescription,
				twSpeed);

		Sprite3D sphere = new Sprite3D(gc, config);
		sphere.customInitialise(Shape3D.SPHERE, startSphere, stopSphere);

		TextWave twDescriptionSphere = new TextWave(gc, config);
		twDescriptionSphere.customInitialise(
				Arrays.asList(new String[] { "This effect is called Sprite3D, real 3D calculations plotted with scaled sprites" }),
				startSphere, stopSphere, false, twDescriptionYpos, twDescriptionAmplitude, twDescriptionColor,
				fontEffectDescription, twSpeed);

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

		Grid grid = new Grid(gc, config);
		grid.setStartMillis(startOutro);
		grid.setStopMillis(stopGrid);

		ShapeEffect stars = new ShapeEffect(gc, config, 5);
		stars.setDoubleAngle(true);
		stars.setStartMillis(startStars);
		stars.setStopMillis(stopStars);

		Bounce bounce = new Bounce(gc, config);
		bounce.customInitialise(14);
		bounce.setStartMillis(startBounce);
		bounce.setStopMillis(stopBounce);

		Burst burst = new Burst(gc, config);
		burst.setStartMillis(startBurst);
		burst.setStopMillis(stopBurst);

		Credits credits = new Credits(gc, config);
		credits.setStartMillis(startOutro);
		credits.setStopMillis(stopOutro);

		effects.add(grid);
		effects.add(stars);
		effects.add(bounce);
		effects.add(burst);

		effects.add(credits);

		return effects;
	}
}