/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.effectfactory;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.background.ColourBackground;
import com.chrisnewland.demofx.effect.background.CycleBackground;
import com.chrisnewland.demofx.effect.background.ImageBackground;
import com.chrisnewland.demofx.effect.fake3d.Sheet;
import com.chrisnewland.demofx.effect.fake3d.Sprite3D;
import com.chrisnewland.demofx.effect.fake3d.Starfield;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.fake3d.Tunnel;
import com.chrisnewland.demofx.effect.fractal.Mandelbrot;
import com.chrisnewland.demofx.effect.fractal.Sierpinski;
import com.chrisnewland.demofx.effect.pixel.Blur;
import com.chrisnewland.demofx.effect.pixel.Bobs;
import com.chrisnewland.demofx.effect.pixel.Mask;
import com.chrisnewland.demofx.effect.pixel.Rain;
import com.chrisnewland.demofx.effect.pixel.Rainbow;
import com.chrisnewland.demofx.effect.pixel.Shift;
import com.chrisnewland.demofx.effect.pixel.Twister;
import com.chrisnewland.demofx.effect.ray.RayTrace;
import com.chrisnewland.demofx.effect.real3d.CubeField;
import com.chrisnewland.demofx.effect.real3d.TexturedCube;
import com.chrisnewland.demofx.effect.real3d.TexturedSphere;
import com.chrisnewland.demofx.effect.real3d.TubeStack;
import com.chrisnewland.demofx.effect.shape.Burst;
import com.chrisnewland.demofx.effect.shape.Checkerboard;
import com.chrisnewland.demofx.effect.shape.Chord;
import com.chrisnewland.demofx.effect.shape.Cogs;
import com.chrisnewland.demofx.effect.shape.Concentric;
import com.chrisnewland.demofx.effect.shape.Diamonds;
import com.chrisnewland.demofx.effect.shape.Glowboard;
import com.chrisnewland.demofx.effect.shape.Grid;
import com.chrisnewland.demofx.effect.shape.Honeycomb;
import com.chrisnewland.demofx.effect.shape.Mandala;
import com.chrisnewland.demofx.effect.shape.Rings;
import com.chrisnewland.demofx.effect.spectral.Equaliser2D;
import com.chrisnewland.demofx.effect.spectral.Equaliser3D;
import com.chrisnewland.demofx.effect.spectral.Feedback;
import com.chrisnewland.demofx.effect.spectral.VUMeter;
import com.chrisnewland.demofx.effect.sprite.Bounce;
import com.chrisnewland.demofx.effect.sprite.Falling;
import com.chrisnewland.demofx.effect.sprite.Fireworks;
import com.chrisnewland.demofx.effect.sprite.MaskStack;
import com.chrisnewland.demofx.effect.sprite.Moire;
import com.chrisnewland.demofx.effect.sprite.Rotations;
import com.chrisnewland.demofx.effect.sprite.Sea;
import com.chrisnewland.demofx.effect.sprite.Spin;
import com.chrisnewland.demofx.effect.sprite.Tiles;
import com.chrisnewland.demofx.effect.text.Credits;
import com.chrisnewland.demofx.effect.text.CreditsSprite;
import com.chrisnewland.demofx.effect.text.SpriteWave;
import com.chrisnewland.demofx.effect.text.TextBounce;
import com.chrisnewland.demofx.effect.text.TextFlash;
import com.chrisnewland.demofx.effect.text.TextLabel;
import com.chrisnewland.demofx.effect.text.TextLayers;
import com.chrisnewland.demofx.effect.text.TextRing;
import com.chrisnewland.demofx.effect.text.TextWave;
import com.chrisnewland.demofx.effect.text.TextWaveSprite;
import com.chrisnewland.demofx.effect.text.WordSearch;
import com.chrisnewland.demofx.effect.video.ChromaKey;
import com.chrisnewland.demofx.effect.video.Hue;
import com.chrisnewland.demofx.effect.video.InverseChromaKey;
import com.chrisnewland.demofx.effect.video.MirrorX;
import com.chrisnewland.demofx.effect.video.MirrorY;
import com.chrisnewland.demofx.effect.video.PictureInPicture;
import com.chrisnewland.demofx.effect.video.QuadPlay;
import com.chrisnewland.demofx.effect.video.RawPlayer;
import com.chrisnewland.demofx.util.ShapeEffect;

public class SimpleEffectFactory implements IEffectFactory
{
	@Override
	public List<IEffect> getEffects(DemoConfig config)
	{
		List<IEffect> result = new ArrayList<>();

		String effectParam = config.getEffect();

		String[] parts = effectParam.split(",");

		for (String part : parts)
		{
			result.add(getEffect(part, config));
		}

		return result;
	}

	private IEffect getEffect(String name, DemoConfig config)
	{
		switch (name)
		{
		case "animtexsphere":
			// return new AnimatedTexturedSphere(config);
			throw new UnsupportedOperationException("AnimatedTexturedSphere can't be run solo, must be combined with an effect that generates the texture");
			
		case "animtexcube":
			// return new AnimatedTexturedCube(config);
			throw new UnsupportedOperationException("AnimatedTexturedCube can't be run solo, must be combined with an effect that generates the texture");

		case "blur":
			return new Blur(config);

		case "bobs":
			return new Bobs(config);

		case "bounce":
			return new Bounce(config);

		case "burst":
			return new Burst(config);

		case "checkerboard":
			return new Checkerboard(config);

		case "chord":
			return new Chord(config);

		case "chromakey":
			return new ChromaKey(config);

		case "cogs":
			return new Cogs(config);
			
		case "colourbackground":
			return new ColourBackground(config);

		case "concentric":
			return new Concentric(config);

		case "credits":
			return new Credits(config);
			
		case "creditssprite":
			return new CreditsSprite(config);

		case "cubefield":
			return new CubeField(config);
			
		case "cyclebackground":
			return new CycleBackground(config);

		case "diamonds":
			return new Diamonds(config);

		case "equaliser":
			return new Equaliser2D(config);

		case "equalisercubes":
			return new Equaliser3D(config);

		case "falling":
			return new Falling(config);

		case "feedback":
			return new Feedback(config);

		case "fireworks":
			return new Fireworks(config);

		case "flash":
			return new TextFlash(config);
			
		case "glowboard":
			return new Glowboard(config);

		case "grid":
			return new Grid(config);

		case "hexagons":
			return new ShapeEffect(config, 6);

		case "honeycomb":
			return new Honeycomb(config);

		case "hue":
			return new Hue(config);

		case "imagebackground":
			return new ImageBackground(config);
			
		case "inversechromakey":
			return new InverseChromaKey(config);
						
		case "mandala":
			return new Mandala(config);

		case "mandelbrot":
			return new Mandelbrot(config);
			
		case "mask":
			return new Mask(config);
			
		case "maskstack":
			return new MaskStack(config);

		case "mirrorx":
			return new MirrorX(config);

		case "mirrory":
			return new MirrorY(config);

		case "moire":
			return new Moire(config);

		case "pentagons":
			return new ShapeEffect(config, 5);

		case "picinpic":
			return new PictureInPicture(config);

		case "quadplay":
			return new QuadPlay(config);

		case "rain":
			return new Rain(config);
			
		case "rainbow":
			return new Rainbow(config);

		case "rawplayer":
			return new RawPlayer(config);
			
		case "raytrace":
			return new RayTrace(config);

		case "rings":
			return new Rings(config);
			
		case "rotations":
			return new Rotations(config);

		case "sea":
			return new Sea(config);

		case "sheet":
			return new Sheet(config);

		case "shift":
			return new Shift(config);

		case "sierpinski":
			return new Sierpinski(config);

		case "spin":
			return new Spin(config);

		case "spritewave":
			return new SpriteWave(config);

		case "sprite3d":
			return new Sprite3D(config);

		case "squares":
			return new ShapeEffect(config, 4);

		case "stars":
			ShapeEffect stars = new ShapeEffect(config, 5);
			stars.setDoubleAngle(true);
			return stars;

		case "starfield":
			return new Starfield(config);

		case "starfieldsprite":
			return new StarfieldSprite(config);
			
		case "textbounce":
			return new TextBounce(config);

		case "texcube":
			return new TexturedCube(config);
			
		case "texsphere":
			return new TexturedSphere(config);

		case "textwave":
			return new TextWave(config);

		case "textwavesprite":
			return new TextWaveSprite(config);

		case "textlabel":
			return new TextLabel(config);
			
		case "textlayers":
			return new TextLayers(config);
			
		case "textring":
			return new TextRing(config);

		case "tiles":
			return new Tiles(config);

		case "tunnel":
			return new Tunnel(config);

		case "triangles":
			return new ShapeEffect(config, 3);

		case "tubestack":
			return new TubeStack(config);
			
		case "twister":
			return new Twister(config);
			
		case "vumeter":
			return new VUMeter(config);

		case "wordsearch":
			return new WordSearch(config);

		default:
			throw new UnsupportedOperationException("No such effect: " + config.getEffect());
		}
	}
}
