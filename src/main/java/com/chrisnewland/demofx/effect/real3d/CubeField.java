/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.real3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class CubeField extends AbstractEffect
{
	private Box[][] boxes;

	private double side = 20;

	private double angle = 90;
	private double space = side * 1.5;

	private int dimensionX;
	private int dimensionY;
	private int maxDimension;

	public CubeField(DemoConfig config)
	{
		super(config);

		dimensionX = (int) (intWidth / (2 * space));

		dimensionY = (int) (intHeight / (2 * space));

		maxDimension = Math.max(dimensionX, dimensionY);

		boxes = new Box[maxDimension][maxDimension];
	}

	@Override
	public void start()
	{
		for (int y = 0; y < maxDimension; y++)
		{
			for (int x = 0; x < maxDimension; x++)
			{
				Box box = new Box(side, side, side);

				box.setMaterial(new PhongMaterial(Color.WHITE));

				box.getTransforms().add(new Rotate(0, Rotate.X_AXIS));
				box.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));

				group.getChildren().add(box);

				boxes[x][y] = box;
			}
		}
	}

	@Override
	public void stop()
	{
		for (int y = 0; y < maxDimension; y++)
		{
			for (int x = 0; x < maxDimension; x++)
			{
				Box box = boxes[x][y];
				group.getChildren().remove(box);
			}
		}
	}

	@Override
	public void renderForeground()
	{
		angle += 0.2;

		if (angle >= 360)
		{
			angle -= 360;
		}

		double depthFactor = -60;

		Color cycleColour = getCycleColour();

		for (int y = 0; y < maxDimension; y++)
		{
			for (int x = 0; x < maxDimension; x++)
			{
				Box box = boxes[x][y];

				((PhongMaterial) box.getMaterial()).setDiffuseColor(cycleColour);

				double zx = Math.abs((maxDimension / 2) - x);
				double zy = Math.abs((maxDimension / 2) - y);
				double z = zx + zy;

				box.setTranslateX(halfWidth + (x - maxDimension / 2) * space);
				box.setTranslateY(halfHeight + (y - maxDimension / 2) * space);
				box.setTranslateZ(z * depthFactor);

				((Rotate) box.getTransforms().get(0)).setAngle((angle + z) * 10);
				((Rotate) box.getTransforms().get(1)).setAngle((angle + z) * 10);
			}
		}
	}
}