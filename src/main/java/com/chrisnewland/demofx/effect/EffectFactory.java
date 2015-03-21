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
import com.chrisnewland.demofx.effect.shape.Rings;
import com.chrisnewland.demofx.effect.shape.ShapeEffect;
import com.chrisnewland.demofx.effect.shape.Sierpinski;
import com.chrisnewland.demofx.effect.sprite.Bounce;
import com.chrisnewland.demofx.effect.sprite.Spin;
import com.chrisnewland.demofx.effect.sprite.Tiles;
import com.chrisnewland.demofx.effect.text.BallGrid;
import com.chrisnewland.demofx.effect.text.Text;

public class EffectFactory
{
	public static IEffect getEffect(GraphicsContext gc, DemoConfig config)
	{
		switch (config.getEffect())
		{
		case "Triangles":
			return new ShapeEffect(gc, config, 3);

		case "Squares":
			return new ShapeEffect(gc, config, 4);

		case "Pentagons":
			return new ShapeEffect(gc, config, 5);

		case "Hexagons":
			return new ShapeEffect(gc, config, 6);

		case "Stars":
			ShapeEffect stars = new ShapeEffect(gc, config, 5);
			stars.setDoubleAngle(true);
			return stars;

		case "Rings":
			return new Rings(gc, config);

		case "Tiles":
			return new Tiles(gc, config);

		case "Spin":
			return new Spin(gc, config);

		case "Bounce":
			return new Bounce(gc, config);

		case "Burst":
			return new Burst(gc, config);

		case "Concentric":
			return new Concentric(gc, config);

		case "Sierpinski":
			return new Sierpinski(gc, config);

		case "Pixels":
			return new Pixels(gc, config);

		case "Text":
			return new Text(gc, config);
			
		case "Balls":
			return new BallGrid(gc, config);

		default:
			throw new RuntimeException("No such effect: " + config.getEffect());
		}
	}
}
