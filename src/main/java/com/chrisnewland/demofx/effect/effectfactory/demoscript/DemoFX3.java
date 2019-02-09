/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.effectfactory.demoscript;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.IPixelSource;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.Fake3DShapeFactory;
import com.chrisnewland.demofx.effect.fake3d.Object3D;
import com.chrisnewland.demofx.effect.fake3d.Sheet;
import com.chrisnewland.demofx.effect.fake3d.Sheet.SheetMode;
import com.chrisnewland.demofx.effect.fake3d.Sprite3D;
import com.chrisnewland.demofx.effect.fake3d.Sprite3D.Shape3D;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.pixel.Bobs;
import com.chrisnewland.demofx.effect.pixel.Mask;
import com.chrisnewland.demofx.effect.pixel.Rain;
import com.chrisnewland.demofx.effect.pixel.Rainbow;
import com.chrisnewland.demofx.effect.pixel.Shift;
import com.chrisnewland.demofx.effect.pixel.Twister;
import com.chrisnewland.demofx.effect.real3d.TexturedCubeCanvasSink;
import com.chrisnewland.demofx.effect.real3d.TexturedSphere;
import com.chrisnewland.demofx.effect.real3d.TubeStack;
import com.chrisnewland.demofx.effect.shape.Chord;
import com.chrisnewland.demofx.effect.shape.Cogs;
import com.chrisnewland.demofx.effect.shape.Diamonds;
import com.chrisnewland.demofx.effect.shape.Glowboard;
import com.chrisnewland.demofx.effect.shape.Glowboard.Direction;
import com.chrisnewland.demofx.effect.shape.Honeycomb;
import com.chrisnewland.demofx.effect.shape.Mandala;
import com.chrisnewland.demofx.effect.spectral.Equaliser2D;
import com.chrisnewland.demofx.effect.spectral.Equaliser3D;
import com.chrisnewland.demofx.effect.spectral.VUMeter;
import com.chrisnewland.demofx.effect.sprite.Falling;
import com.chrisnewland.demofx.effect.sprite.Fireworks;
import com.chrisnewland.demofx.effect.sprite.MaskStack;
import com.chrisnewland.demofx.effect.sprite.Moire;
import com.chrisnewland.demofx.effect.sprite.Sea;
import com.chrisnewland.demofx.effect.sprite.Sea.SeaMode;
import com.chrisnewland.demofx.effect.text.CreditsSprite;
import com.chrisnewland.demofx.effect.text.TextBounce;
import com.chrisnewland.demofx.effect.text.TextLayers;
import com.chrisnewland.demofx.effect.text.TextRing;
import com.chrisnewland.demofx.effect.text.TextRing.RingData;
import com.chrisnewland.demofx.effect.text.TextWaveSprite;
import com.chrisnewland.demofx.effect.text.WordSearch;
import com.chrisnewland.demofx.effect.video.ChromaKey;
import com.chrisnewland.demofx.effect.video.InverseChromaKey;
import com.chrisnewland.demofx.effect.video.RawPlayer;
import com.chrisnewland.demofx.util.Cog;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/*
 * 120 bpm
 *
 * 1 bar = 2 seconds
 *
 * 4m24 264 seconds = 132 bars (2s) = 66 double bars (4s) = 33 quad bars (8s)
 *
 * 000	start
 * 016	first chip notes
 * 032	bass comes in
 * 064	1st chorus start (for 32s / 16 bars)
 * 096	2nd verse start - heavy
 * 160	2nd chorus start (for 32s / 16 bars)
 * 192	middle 8 start (for 32s / 16 bars)
 * 224	3rd chorus start (for 32s / 16 bars)
 * 256	outro (8s / 4 bars)
 * 264	end
 */
public class DemoFX3 implements IEffectFactory
{
	private DemoConfig config;
	private List<IEffect> effects = new ArrayList<>();
	private long time = 0;
	private static final long D = 4000;

	@Override public List<IEffect> getEffects(DemoConfig config)
	{
		this.config = config;

		String audio = getClass().getResource("/DemoFX3.mp3").toExternalForm();

		System.out.println(audio);

		config.setAudioFilename(audio);

		wordsearch();

		cogsAndGlowBoard();

		equaliser();

		shadeBobs();

		honeycombTubestack();

		rain();

		shift();

		skullMask();

		space();

		maskStack();

		rainbowSection();

		chromaKey();

		textLayerRainbow();

		creditsAndEnding();

		return effects;
	}

	private void equaliser()
	{
		long duration2D = 2 * D;
		long duration3D = 2 * D;
		long totalDuration = duration2D + duration3D;

		addEffect(time, totalDuration, new Glowboard(config, 16, Direction.N, Color.DARKORCHID));

		Image quaver = ImageUtil.loadImageFromResources("quaver2.png");

		addEffect(time, totalDuration, new Falling(config, new Image[] { quaver }, true));
		addEffect(time, totalDuration, new Equaliser2D(config));

		addEffect(time, totalDuration, new TextBounce(config, "Spectral Analyser"));

		time += duration2D;

		int side = 320;

		config.setOffScreen(side, side);

		addEffect(time, duration3D, new Equaliser2D(config));

		config.clearOffScreen();

		TexturedCubeCanvasSink animatedTexCube = new TexturedCubeCanvasSink(config, side);

		animatedTexCube.setCanvas(config.getOffScreenCanvasGC().getCanvas());

		addEffect(time, duration3D, animatedTexCube);

		time += duration3D;
	}

	private void space()
	{
		long spaceDuration = 12 * D;

		addEffect(time, spaceDuration, new StarfieldSprite(config));

		textRingSpace(); // 1

		equaliserBarsAndVUMeters(); // 2

		videoCubeAndMandalas(); // 2

		threeDimensionShapes(); // 7
	}

	private void textRingSpace()
	{
		long duration = D;

		TextRing.RingData[] ringData = new TextRing.RingData[] { new TextRing.RingData("DemoFX Part III", 100, 0.10, 1, 7.3, 8),
				new TextRing.RingData("JavaFX Demoscene Engine", 160, 0.12, 1.2, 4.0, 6),
				new TextRing.RingData("Coding by Chriswhocodes", 220, 0.13, 1.4, 3.8, 4),
				new TextRing.RingData("Music by David Newland", 300, 0.15, 1.6, 3.6, 2), };

		addEffect(time, duration, new TextRing(config, ringData));

		time += duration;
	}

	private void equaliserBarsAndVUMeters()
	{
		long duration = 2 * D;

		addEffect(time, duration, new Equaliser3D(config));
		addEffect(time, duration, new VUMeter(config, 4));

		addEffect(time, duration, new TextBounce(config, "3D EQ with VU"));

		time += duration;
	}

	private void threeDimensionShapes()
	{
		addEffect(time, 7 * D, new Chord(config, Color.DARKBLUE));

		Image imageBlue = ImageUtil.loadImageFromResources("glassyball.png");
		Image imageGreen = ImageUtil.tintImage(imageBlue, Color.GREEN.getHue());
		Image imageRed = ImageUtil.tintImage(imageBlue, Color.RED.getHue());
		Image imagePurple = ImageUtil.tintImage(imageBlue, Color.PURPLE.getHue());
		Image imageGold = ImageUtil.tintImage(imageBlue, Color.GOLD.getHue());

		Fake3DShapeFactory factory = new Fake3DShapeFactory(config.getPreCalc(), imageBlue);

		addEffect(time, D, new Sprite3D(config, "DemoFX III", 0));
		addEffect(time, D, new TextBounce(config, "sprite3d"));

		time += D;

		Sprite3D face = new Sprite3D(config, true);
		face.addObjectFromModel("face", imageBlue);
		addEffect(time, 2 * D, face);
		addEffect(time, 2 * D, new TextBounce(config, "3d models"));
		time += 2 * D;

		Sprite3D spring = new Sprite3D(config, true);
		spring.addObject(Shape3D.SPRING, imageGreen, 1.0);
		addEffect(time, D / 2, spring);
		time += D / 2;

		Sprite3D spike = new Sprite3D(config, true);
		spike.addObject(Shape3D.SPIKE, imageRed, 2);
		addEffect(time, D / 2, spike);
		time += D / 2;

		Sprite3D sphere = new Sprite3D(config, true);
		sphere.addObject(Shape3D.SPHERE, imagePurple, 1);
		addEffect(time, D / 2, sphere);
		time += D / 2;

		Sprite3D cube = new Sprite3D(config, true);
		cube.addObject(Shape3D.SOLIDCUBE, imageGold, 1);
		addEffect(time, D / 2, cube);
		time += D / 2;

		Sprite3D star = new Sprite3D(config, true);

		Object3D star1 = factory.makeStar(2).setPitchInc(1.0);
		Object3D star2 = factory.makeStar(1).setPitchInc(2.0).setImage(imageGreen);
		Object3D star3 = factory.makeStar(0.5).setPitchInc(3.0).setImage(imageRed);

		star.addObject(star1);
		star.addObject(star2);
		star.addObject(star3);

		addEffect(time, D, star);
		time += D;

		Sprite3D cubes = new Sprite3D(config, true);

		Object3D cube1 = factory.makeWireframeCube(16, 3).setPitchInc(1.0);
		Object3D cube2 = factory.makeWireframeCube(16, 2).setPitchInc(2.0).setImage(imageGreen);
		Object3D cube3 = factory.makeWireframeCube(16, 1).setPitchInc(3.0).setImage(imageRed);

		cubes.addObject(cube1);
		cubes.addObject(cube2);
		cubes.addObject(cube3);

		addEffect(time, D, cubes);
		time += D;
	}

	private void videoCubeAndMandalas()
	{
		int side = 320;

		long duration = 2 * D;

		addEffect(time, duration, new Mandala(config));

		config.setOffScreen(side, side);

		addEffect(time, duration, new RawPlayer(config, "/video/bike.m4v", side, side));

		config.clearOffScreen();

		TexturedCubeCanvasSink animatedTexCube = new TexturedCubeCanvasSink(config, side);

		animatedTexCube.setCanvas(config.getOffScreenCanvasGC().getCanvas());

		addEffect(time, duration, animatedTexCube);

		addEffect(time, duration, new TextBounce(config, "Video 3D texture"));

		time += duration;
	}

	private void maskStack()
	{
		int glowSize = 16;

		addEffect(time, 2 * D, new Glowboard(config, glowSize, Direction.NW, Color.BLUE));
		addEffect(time, 2 * D, new MaskStack(config, ImageUtil.loadImageFromResources("tiger.jpeg")));
		addEffect(time, 2 * D, new TextBounce(config, "MaskStack"));
		time += 2 * D;

		addEffect(time, 2 * D, new Glowboard(config, glowSize, Direction.SW, Color.PURPLE));
		addEffect(time, 2 * D, new MaskStack(config, ImageUtil.loadImageFromResources("cham.jpeg")));
		addEffect(time, 2 * D, new TextBounce(config, "MaskStack"));
		time += 2 * D;
	}

	private void wordsearch()
	{
		addEffect(time, D, new WordSearch(config, "Presenting\n\n\nDemoFX III"));
		time += D;

		addEffect(time, D, new WordSearch(config, "A JavaFX\n\n\nDemoscene\n\n\nBenchmark"));
		time += D;

		addEffect(time, D, new WordSearch(config, "Coding\n\n\nchriswhocodes"));
		time += D;

		addEffect(time, D, new WordSearch(config, "Music\n\n\nDavid Newland"));
		time += D;
	}

	private void honeycombTubestack()
	{
		addEffect(time, 2 * D, new Honeycomb(config));
		addEffect(time, 2 * D, new TubeStack(config));
		addEffect(time, 2 * D, new TextBounce(config, "Tubestack"));

		time += 2 * D;
	}

	private void cogsAndGlowBoard()
	{
		int glowSize = 16;

		long origTime = time;

		addEffect(time, D, new Glowboard(config, glowSize, Direction.NW, Color.BLUE));
		addEffect(time, D, new Cogs(config, Color.BLUE));
		time += D;

		addEffect(time, D, new Glowboard(config, glowSize, Direction.NE, Color.GREEN));
		addEffect(time, D, new Cogs(config, makeCogsRing(config, Color.GREEN)));
		time += D;

		addEffect(time, D, new Glowboard(config, glowSize, Direction.SE, Color.RED));
		addEffect(time, D, new Cogs(config, makeCogsDiamond(config, Color.RED)));
		time += D;

		addEffect(time, D, new Glowboard(config, glowSize, Direction.SW, Color.PURPLE));
		addEffect(time, D, new Cogs(config, makeCogsHeart(config, Color.PURPLE)));
		time += D;

		addEffect(origTime, D * 4, new TextWaveSprite(config,
				new String[] { "Welcome to DemoFX part III", "A JavaFX Demoscene benchmark", "Here we go!" },
				config.getHeight() * 0.5, 1, 10));
	}

	private void shadeBobs()
	{
		long origTime = time;

		addEffect(time, D, new Bobs(config, 64, 32, Color.BLUE));
		addEffect(time, D, new TextBounce(config, "ShadeBobs 64x32"));
		time += D;

		addEffect(time, D, new Bobs(config, 32, 64, Color.RED));
		addEffect(time, D, new TextBounce(config, "ShadeBobs 32x64"));
		time += D;

		addEffect(time, D, new Bobs(config, 48, 48, Color.GREEN));
		addEffect(time, D, new TextBounce(config, "ShadeBobs 48x48"));
		time += D;

		addEffect(time, D, new Bobs(config, 16, 128, Color.PURPLE));
		addEffect(time, D, new TextBounce(config, "ShadeBobs 16x128"));
		time += D;

		addEffect(origTime, D * 4, new TextWaveSprite(config, new String[] { "Old School effects are easy with a PixelWriter" },
				config.getHeight() * 0.1, 1, 10));
	}

	private void creditsAndEnding()
	{
		addEffect(time, 10 * D, new StarfieldSprite(config));

		credits();

		joshAndPlanet();
	}

	private void credits()
	{
		long duration = 8 * D;

		addEffect(time, duration, new CreditsSprite(config, "greetings_demofx_3.txt", 0.22, 2.1));

		addEffect(time, duration, new Fireworks(config));

		time += duration;
	}

	private void chromaKey()
	{
		int mediaWidth = 480;
		int mediaHeight = 624;

		double renderX = (config.getWidth() - mediaWidth) / 2;
		double renderY = config.getHeight() - mediaHeight;

		addEffect(time, D * 4, new Honeycomb(config));
		addEffect(time, D * 2, new ChromaKey(config, "/video/dancing.mp4", mediaWidth, mediaHeight, renderX, renderY));
		addEffect(time, D * 2, new TextBounce(config, "chroma keying"));
		addEffect(time, D * 2,
				new TextWaveSprite(config, new String[] { "Real-time video green-screening" }, config.getHeight() * 0.1, 1, 10));
		time += D * 2;

		config.setOffScreen(mediaWidth, mediaHeight);
		Bobs bobs = new Bobs(config, 128, 16, Color.YELLOW);
		config.clearOffScreen();
		addEffect(time, D * 2, bobs);

		InverseChromaKey inverseChroma = new InverseChromaKey(config, "/video/dancing.mp4", mediaWidth, mediaHeight, renderX,
				renderY);
		((IPixelSource) bobs).setPixelSink(inverseChroma);
		addEffect(time, D * 2, inverseChroma);
		addEffect(time, D * 2, new TextBounce(config, "Inverse chroma"));
		addEffect(time, D * 2,
				new TextWaveSprite(config, new String[] { "Green-screening with subject replacement" }, config.getHeight() * 0.1, 1,
						10));
		time += D * 2;
	}

	private void joshAndPlanet()
	{
		long duration = D * 2;

		int mediaWidth = 608;
		int mediaHeight = 648;

		double renderX = (config.getWidth() - mediaWidth) / 2;
		double renderY = config.getHeight() - mediaHeight;

		addEffect(time, duration, new ChromaKey(config, "/video/planet.mp4", mediaWidth, mediaHeight, renderX, renderY));

		WritableImage texture = ImageUtil.loadWritableImageFromResources("/earth.jpg"); // https://www.evl.uic.edu/pape/data/Earth/

		TexturedSphere planetEarth = new TexturedSphere(config, texture, config.getHeight() / 6, 0, 1, 890, 210);
		addEffect(time, duration, planetEarth);

		addEffect(time, duration,
				new TextWaveSprite(config, new String[] { "JavaFX ... It's not rocket science!" }, config.getHeight() * 0.8, 1,
						10));

		time += duration;

		// long fadeDuration = 500;
		//
		// addEffect(time - fadeDuration, fadeDuration, new
		// FadeOutEffect(config, Color.BLACK, fadeDuration));

	}

	private void textLayerRainbow()
	{
		long duration = D * 2;

		addEffect(time, duration, new Rainbow(config));
		addEffect(time, duration, new TextLayers(config));
		addEffect(time, duration, new TextBounce(config, "Code Dive"));

		time += duration;
	}

	private void rain()
	{
		addEffect(time, 2 * D, new Rain(config, "reena.jpg"));
		addEffect(time, 2 * D, new TextBounce(config, "Raindrops"));

		time += 2 * D;
	}

	private void shift()
	{
		addEffect(time, 2 * D, new Shift(config, 380, "swans.jpeg"));
		addEffect(time, 2 * D, new TextBounce(config, "Reflection"));

		time += 2 * D;
	}

	private void skullMask()
	{
		long duration = 2 * D;

		addEffect(time, duration, new Diamonds(config));
		addEffect(time, duration, new Mask(config));
		addEffect(time, duration, new TextBounce(config, "Irregular mask"));

		time += duration;
	}

	private void rainbowSection()
	{
		addEffect(time, 8 * D, new Rainbow(config));

		moire();
		sheet();
		twister();
		sea();
	}

	private void twister()
	{
		long duration = 2 * D;
		addEffect(time, duration, new Twister(config));
		addEffect(time, duration, new TextBounce(config, "Twisters"));
		time += duration;
	}

	private void moire()
	{
		long duration = 2 * D;

		addEffect(time, duration, new Moire(config));
		addEffect(time, duration, new TextBounce(config, "MoreMoire"));

		time += duration;
	}

	private void sheet()
	{
		long duration = D;

		addEffect(time, duration, new Sheet(config, SheetMode.FLAG, null));
		addEffect(time, duration, new TextBounce(config, "Flag wave"));
		time += duration;

		addEffect(time, duration, new Sheet(config, SheetMode.SHEET, null));
		addEffect(time, duration, new TextBounce(config, "Sheet warp"));
		time += duration;

		addEffect(time, duration, new Sheet(config, SheetMode.SPRITE, "duke_sm.png"));
		addEffect(time, duration, new TextBounce(config, "Sprite warp"));
		time += duration;
	}

	private void sea()
	{
		long duration = D;

		addEffect(time, duration, new Sea(config, SeaMode.CUBES));
		addEffect(time, duration, new TextWaveSprite(config, new String[] { "Sea of Cubes" }, config.getHeight() * 0.1, 1, 10));

		time += duration;
	}

	private void addEffect(long start, long duration, IEffect effect)
	{
		effect.setStartOffsetMillis(start);
		effect.setStopOffsetMillis(start + duration);
		effects.add(effect);

		System.out.println("Adding " + start + " to " + (start + duration) + " " + effect.getClass().getName());
	}

	private List<Cog> makeCogsRing(DemoConfig config, Color outline)
	{
		double width = config.getWidth();
		double halfWidth = width / 2;

		double height = config.getHeight();
		double halfHeight = height / 2;

		List<Cog> cogs = new ArrayList<>();

		int teeth = 9;

		double outer = 55;
		double inner = outer - (outer / 3);

		double r = 185;
		boolean clockwise = true;

		double[] angleDelta = new double[] { -2, -3, 17, 12, -2, -3, 17, 12, -2, -3, 17, 12, -2 };

		final int angleInc = 30;

		int pos = 0;

		int show = 360 / angleInc;

		for (int i = 0; i < show; i++)
		{
			double a = i * angleInc;

			double x = halfWidth + r * config.getPreCalc().sin(a);
			double y = halfHeight + r * config.getPreCalc().cos(a);

			cogs.add(new Cog(config, x, y, clockwise, angleDelta[pos++], outer, inner, teeth, outline));

			clockwise = !clockwise;
		}

		cogs.add(new Cog(config, halfWidth, halfHeight, true, 0, outer * 2, inner * 2, 12, outline));

		return cogs;
	}

	private List<Cog> makeCogsDiamond(DemoConfig config, Color outline)
	{
		List<Cog> cogs = new ArrayList<>();

		int teeth = 9;

		double width = config.getWidth();
		double halfWidth = width / 2;

		double height = config.getHeight();
		double halfHeight = height / 2;

		double outer = height / 10;
		double inner = outer - (outer / 3);

		boolean clockwise = true;

		double gap = outer * 2.50;

		cogs.add(new Cog(config, halfWidth, halfHeight - gap, clockwise, 0, outer * 2, inner * 2, teeth, outline));
		cogs.add(new Cog(config, halfWidth + gap, halfHeight, !clockwise, 0, outer * 2, inner * 2, teeth, outline));
		cogs.add(new Cog(config, halfWidth, halfHeight + gap, clockwise, 20, outer * 2, inner * 2, teeth, outline));
		cogs.add(new Cog(config, halfWidth - gap, halfHeight, !clockwise, 20, outer * 2, inner * 2, teeth, outline));

		return cogs;
	}

	private List<Cog> makeCogsHeart(DemoConfig config, Color outline)
	{
		List<Cog> cogs = new ArrayList<>();

		int teeth = 9;

		double width = config.getWidth();
		double halfWidth = width / 2;

		double height = config.getHeight();

		double outer = height / 14;
		double inner = outer - (outer / 3);

		double yOffset = 192;

		boolean clockwise = true;

		class CogDef
		{
			private double x;
			private double y;
			private double rotation;
			private boolean clockwise;

			public CogDef(double x, double y, double rotation, boolean clockwise)
			{
				this.x = x;
				this.y = y;
				this.rotation = rotation;
				this.clockwise = clockwise;
			}
		}

		double gap = outer * 2 - inner / 4;

		CogDef[] defs = new CogDef[] { new CogDef(halfWidth + gap * 0.0, height - (yOffset + gap * 3.0), -4, false),
				new CogDef(halfWidth - gap * 0.8, height - (yOffset + gap * 3.6), 14, true),
				new CogDef(halfWidth + gap * 0.8, height - (yOffset + gap * 3.6), 0, true),
				new CogDef(halfWidth - gap * 1.2, height - (yOffset + gap * 4.5), 10, false),
				new CogDef(halfWidth + gap * 1.2, height - (yOffset + gap * 4.5), 10, false),
				new CogDef(halfWidth - gap * 2.2, height - (yOffset + gap * 4.5), 22, true),
				new CogDef(halfWidth + gap * 2.2, height - (yOffset + gap * 4.5), 22, true),
				new CogDef(halfWidth - gap * 2.6, height - (yOffset + gap * 3.6), -4, false),
				new CogDef(halfWidth + gap * 2.6, height - (yOffset + gap * 3.6), 22, false),
				new CogDef(halfWidth - gap * 2.2, height - (yOffset + gap * 2.7), 0, true),
				new CogDef(halfWidth + gap * 2.2, height - (yOffset + gap * 2.7), 4, true),
				new CogDef(halfWidth - gap * 1.8, height - (yOffset + gap * 1.8), 0, false),
				new CogDef(halfWidth + gap * 1.8, height - (yOffset + gap * 1.8), 22, false),
				new CogDef(halfWidth - gap * 1.4, height - (yOffset + gap * 0.9), 0, true),
				new CogDef(halfWidth + gap * 1.4, height - (yOffset + gap * 0.9), 10, true),
				new CogDef(halfWidth - gap * 0.8, height - (yOffset + gap * 0.1), 10, false),
				new CogDef(halfWidth + gap * 0.8, height - (yOffset + gap * 0.1), 14, false),
				new CogDef(halfWidth + gap * 0.0, height - (yOffset - gap * 0.5), 22, true) };

		for (CogDef def : defs)
		{
			cogs.add(new Cog(config, def.x, def.y, def.clockwise, def.rotation + 7, outer, inner, teeth, outline));

			clockwise = !clockwise;
		}

		return cogs;
	}
}
