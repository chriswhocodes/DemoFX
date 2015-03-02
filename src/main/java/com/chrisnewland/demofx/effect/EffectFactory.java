package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.shape.ShapeEffect;

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

		default:
			throw new RuntimeException("No such effect: " + config.getEffect());
		}
	}
}
