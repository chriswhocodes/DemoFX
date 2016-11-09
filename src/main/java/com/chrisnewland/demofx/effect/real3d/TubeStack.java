/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.real3d;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class TubeStack extends AbstractEffect
{
	private static double yPosition = 64;
	private static double speed = 1;

	private class Tube
	{
		private double angle = 0;

		private Rotate rotateY;

		private double angleDelta;

		private Cylinder cylinder;

		public void incAngle()
		{
			angle += angleDelta;

			if (angle >= 360)
			{
				angle -= 360;
			}

			rotateY.setAngle(angle);

		}

		public Tube(String text)
		{
			this.angleDelta = speed;

			Image texture = TextUtil.createSpriteImageText(text, false, 0.6);

			texture = ImageUtil.createBorderedImage(texture, 32, 8);

			double textureWidth = texture.getWidth();

			double radius = textureWidth / (Math.PI * 2);

			PhongMaterial material = new PhongMaterial();
			material.setDiffuseMap(texture);

			cylinder = new Cylinder(radius, texture.getHeight());
			cylinder.setMaterial(material);

			rotateY = new Rotate(angle, Rotate.Y_AXIS);

			cylinder.getTransforms().add(rotateY);

			cylinder.setTranslateZ(radius);
			cylinder.setTranslateX(halfWidth);
			cylinder.setTranslateY(yPosition);

			yPosition += texture.getHeight() + 32;
			speed *= 1.5;
		}
	}

	private Tube[] stack;

	public TubeStack(DemoConfig config)
	{
		super(config);
		
		List<Tube> items = new ArrayList<>();

		items.add(new Tube("DemoFX Demoscene Engine Part III"));
		items.add(new Tube("JavaFX Benchmarking System"));
		items.add(new Tube("Coding by Chris Newland"));
		items.add(new Tube("Music by David Newland"));
		items.add(new Tube("TubeStack Effect"));
		items.add(new Tube("JavaFX Rocks"));

		stack = items.toArray(new Tube[items.size()]);
	}
	
	@Override
	public void start()
	{
		for (Tube unit : stack)
		{
			group.getChildren().add(unit.cylinder);
		}
	}

	@Override
	public void stop()
	{
		for (Tube unit : stack)
		{
			group.getChildren().remove(unit.cylinder);
		}
	}

	@Override
	public void renderForeground()
	{
		for (Tube unit : stack)
		{
			unit.incAngle();
		}
	}
}