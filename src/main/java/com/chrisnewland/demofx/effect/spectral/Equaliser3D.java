/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.spectral;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.ISpectrumDataProvider;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Equaliser3D extends AbstractSpectralEffect
{
	private int bandCount;

	private float[] bandMax;

	private float DECAY = 0.2f;

	private static final float DECIBEL_RANGE = 60.0f;

	private double angle = 0;

	private static final int CUBES = 32;

	private Bar[] bars;

	private class Bar
	{
		private Box box;
		private Rotate rotateX;
		private Rotate rotateY;
		private PhongMaterial material;

		public Bar(int width, int height, int depth, Color color)
		{
			box = new Box(width, height, depth);
			material = new PhongMaterial(color);
			box.setMaterial(material);

			rotateY = new Rotate(0, Rotate.Y_AXIS);
			rotateX = new Rotate(0, Rotate.X_AXIS);

			box.getTransforms().add(rotateY);
			box.getTransforms().add(rotateX);
		}

		public void setPosition(double x, double y)
		{
			box.setTranslateX(x);
			box.setTranslateY(y);
		}

		public void setHeight(double height)
		{
			box.setHeight(height);
		}
	}

	public Equaliser3D(DemoConfig config)
	{
		super(config);

		bars = new Bar[CUBES];
	}

	@Override
	public void start()
	{
		double usableWidth = width * 0.8;

		double startX = (width - usableWidth) / 2;

		for (int i = 0; i < bars.length; i++)
		{
			Bar bar = new Bar(20, 0, 20, Color.WHITE);

			double x = startX + ((i + 0.5) / CUBES * usableWidth);

			bar.setPosition(x, halfHeight);

			group.getChildren().add(bar.box);

			bars[i] = bar;
		}
	}

	@Override
	public void stop()
	{
		for (Bar bar : bars)
		{
			if (bar != null)
			{
				group.getChildren().remove(bar.box);
			}
		}
	}

	@Override
	public void setSpectrumDataProvider(ISpectrumDataProvider provider)
	{
		this.spectrumProvider = provider;

		// high end of frequency range rarely used
		// only plot low fraction of spectrum

		this.bandCount = (int) Math.floor(provider.getBandCount() * 0.16);

		bandMax = new float[bandCount];

		for (int i = 0; i < bandCount; i++)
		{
			bandMax[i] = -DECIBEL_RANGE;
		}
	}

	@Override
	public void renderForeground()
	{
		if (spectrumProvider != null)
		{
			float[] data = spectrumProvider.getData();

			int bandsPerCube = bandCount / CUBES;

			double sum = 0;
			int cubeCount = 0;
			int cubeNumber = 0;

			Color colour = getCycleColour();

			for (int i = 0; i < bandCount; i++)
			{
				float mag = data[i];

				if (mag > bandMax[i])
				{
					bandMax[i] = mag;
				}
				else
				{
					bandMax[i] -= DECAY;
				}

				sum += bandMax[i] + DECIBEL_RANGE;

				cubeCount++;

				if (cubeCount >= bandsPerCube)
				{
					double size = 24 + 12 * (sum / bandsPerCube);

					Bar bar = bars[cubeNumber];

					angle += 0.1;

					if (angle >= 360)
					{
						angle -= 360;
					}

					bar.rotateX.setAngle(i * 4 + angle);
					// bar.rotateY.setAngle(angle);

					bar.setHeight(size);

					bar.material.setDiffuseColor(colour);

					cubeCount = 0;
					cubeNumber++;

					if (cubeNumber == CUBES)
					{
						break;
					}

					sum = 0;
				}
			}
		}
	}
}