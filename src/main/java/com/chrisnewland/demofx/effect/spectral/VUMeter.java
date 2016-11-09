/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.spectral;

import com.chrisnewland.demofx.DemoConfig;

import javafx.scene.paint.Color;

public class VUMeter extends AbstractSpectralEffect
{
	private static final int SEGMENTS = 10;

	private static final double DIAMETER = 200;
	private static final double GAP = DIAMETER / 6;

	private int meters;

	private float[] meterValue;

	private float[] lastValue;

	private double offsetX;

	private static final double angleSpread = 90 + 45;

	private static final double angleStart = 180 + angleSpread / 2;

	private static final double angleEnd = 180 - angleSpread / 2;

	private static final double angleDelta = angleSpread / SEGMENTS;

	public VUMeter(DemoConfig config)
	{
		super(config);

		init(5);
	}

	public VUMeter(DemoConfig config, int meters)
	{
		super(config);

		init(meters);
	}

	private void init(int meters)
	{
		this.meters = meters;

		meterValue = new float[meters];

		lastValue = new float[meters];

		double totalMeterWidth = meters * DIAMETER;

		if (meters > 1)
		{
			totalMeterWidth += (meters - 1) * GAP;
		}

		offsetX = (width - totalMeterWidth) / 2;
	}

	@Override
	public void renderForeground()
	{
		if (spectrumProvider != null)
		{
			float[] data = spectrumProvider.getData();

			quantise(data);
		}
		
		render();
	}

	private void quantise(float[] data)
	{
		usableBandCount = Math.min(meters * 4, data.length);

		final int readingsPerMeter = usableBandCount / meters;

		int pos = 0;

		int meter = 0;

		float meterSum = 0;

		for (int i = 0; i < usableBandCount && meter < meters; i++)
		{
			meterSum += DECIBEL_RANGE + data[i];

			if (pos == readingsPerMeter || i == usableBandCount - 1)
			{
				float nextValue = meterSum / (pos + 1);

				float previous = lastValue[meter];

				if (nextValue < previous)
				{
					nextValue = previous - DECAY;
				}

				meterValue[meter] = nextValue;

				lastValue[meter] = nextValue;

				meter++;

				pos = 0;

				meterSum = 0;
			}

			pos++;
		}
	}

	private void render()
	{
		double meterX = offsetX + DIAMETER / 2;

		for (int i = 0; i < meters; i++)
		{
			drawMeter(i, meterX, height - GAP, DIAMETER / 2);

			meterX += DIAMETER + GAP;
		}
	}

	private void drawMeter(int meterIndex, double x, double y, double radius)
	{
		float value = meterValue[meterIndex];

		double markStart = radius;
		double markEnd = radius * 0.8;

		gc.setStroke(Color.WHITE);
		gc.setLineWidth(4.0);

		for (double a = angleStart; a >= angleEnd; a -= angleDelta)
		{
			double x1 = x + markStart * precalc.sin(a);
			double y1 = y + markStart * precalc.cos(a);

			double x2 = x + markEnd * precalc.sin(a);
			double y2 = y + markEnd * precalc.cos(a);

			gc.strokeLine(x1, y1, x2, y2);
		}		
		
		double valueAngle = angleStart - (value / DECIBEL_RANGE) * angleSpread;

		double vX = x + markEnd * precalc.sin(valueAngle);
		double vY = y + markEnd * precalc.cos(valueAngle);

		gc.setLineWidth(4.0);
		gc.setStroke(Color.RED);

		gc.strokeLine(x, y, vX, vY);

		gc.setLineWidth(1.0);
		gc.strokeText("VU " + (meterIndex + 1), x - 16, y - radius / 4);
	}
}