/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */

package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.pixel.Pixels;
import com.chrisnewland.demofx.effect.shape.Burst;
import com.chrisnewland.demofx.effect.shape.Concentric;
import com.chrisnewland.demofx.effect.shape.Grid;
import com.chrisnewland.demofx.effect.shape.Rings;
import com.chrisnewland.demofx.effect.shape.Sierpinski;
import com.chrisnewland.demofx.effect.sprite.Bounce;
import com.chrisnewland.demofx.effect.sprite.Spin;
import com.chrisnewland.demofx.effect.sprite.Tiles;
import com.chrisnewland.demofx.effect.text.BallGrid;
import com.chrisnewland.demofx.effect.text.TextWave;
import com.chrisnewland.demofx.util.ShapeEffect;

public class EffectFactory
{
	public static IEffect getEffect(GraphicsContext gc, DemoConfig config)
	{
		switch (config.getEffect())
		{
		case "triangles":
			return new ShapeEffect(gc, config, 3);

		case "squares":
			return new ShapeEffect(gc, config, 4);

		case "pentagons":
			return new ShapeEffect(gc, config, 5);

		case "hexagons":
			return new ShapeEffect(gc, config, 6);

		case "stars":
			ShapeEffect stars = new ShapeEffect(gc, config, 5);
			stars.setDoubleAngle(true);
			return stars;

		case "rings":
			return new Rings(gc, config);

		case "tiles":
			return new Tiles(gc, config);

		case "spin":
			return new Spin(gc, config);

		case "bounce":
			return new Bounce(gc, config);

		case "burst":
			return new Burst(gc, config);

		case "concentric":
			return new Concentric(gc, config);

		case "sierpinski":
			return new Sierpinski(gc, config);

		case "pixels":
			return new Pixels(gc, config);

		case "textwave":
			return new TextWave(gc, config);
			
		case "balls":
			return new BallGrid(gc, config);
			
		case "grid":
			return new Grid(gc, config);

		default:
			throw new RuntimeException("No such effect: " + config.getEffect());
		}
	}
}
