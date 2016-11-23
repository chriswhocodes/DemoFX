/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.spectral;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Equaliser2D extends AbstractSpectralEffect
{
	private Image bar = ImageUtil.loadImageFromResources("bar.png");
	private Image ball = ImageUtil.loadImageFromResources("glassyball.png");
	
	public Equaliser2D(DemoConfig config)
	{
		super(config);

		heightFactor = (float) (halfHeight / (DECIBEL_RANGE * 0.75));
	}

	@Override
	public void renderForeground()
	{
		if (renderOffScreen)
		{
			fillBackground(getCycleColour());
		}
		
		if (spectrumProvider != null)
		{
			float[] data = spectrumProvider.getData();

			Color cycleColour = getCycleColour();

			for (int i = 0; i < usableBandCount; i++)
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

				mag = convertY(mag);

				double x1 = i * bandWidth;

				gc.setFill(cycleColour);

				//gc.fillRect(x1, halfHeight - mag, barWidth, 2 * mag);
				gc.drawImage(bar, x1, halfHeight - mag, barWidth, 2 * mag);

				double y1 = halfHeight - convertY(bandMax[i])-barWidth;
				double y2 = halfHeight + convertY(bandMax[i]);
				
				gc.drawImage(ball, x1, y1, barWidth, barWidth);
				gc.drawImage(ball, x1, y2, barWidth, barWidth);
			}
		}
	}

	private float convertY(float magnitude)
	{
		float result = (magnitude + DECIBEL_RANGE) * heightFactor;

		if (result < 1)
		{
			result = 1;
		}

		return result;
	}
}