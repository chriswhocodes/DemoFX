/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.spectral;

import java.nio.IntBuffer;

import com.chrisnewland.demofx.DemoConfig;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;

public class Feedback extends AbstractSpectralEffect
{	
	private PixelWriter pixelWriter;
	private PixelFormat<IntBuffer> pixelFormat;

	private boolean visibleA;

	private int[] bufferA;
	private int[] bufferB;

	private int imageWidth;
	private int imageHeight;
	private int pixelCount;

	public Feedback(DemoConfig config)
	{
		super(config);

		init();
	}

	private void init()
	{
		pixelWriter = gc.getPixelWriter();

		pixelFormat = PixelFormat.getIntArgbInstance();

		imageWidth = intWidth;
		imageHeight = intHeight;

		pixelCount = imageWidth * imageHeight;

		bufferA = new int[pixelCount];
		bufferB = new int[pixelCount];

		visibleA = false;
	}

	@Override
	public void renderForeground()
	{
		if (spectrumProvider != null)
		{
			float[] data = spectrumProvider.getData();

			drawCircle(data);
			
			render();

			pixelWriter.setPixels(0, 0, imageWidth, imageHeight, pixelFormat, visibleA ? bufferA : bufferB, 0, imageWidth);
		}
	}

	private void drawOnBuffer(int[] buffer, int x1, int y1, int size)
	{
		x1 = Math.max(0, x1);
		y1 = Math.max(0, y1);

		int x2 = Math.min(x1 + size, intWidth);
		int y2 = Math.min(y1 + size, intHeight);

		int r = 100 + (int) (Math.random() * 155);
		int g = 100 + (int) (Math.random() * 155);
		int b = 100 + (int) (Math.random() * 155);

		for (int y = y1; y < y2; y++)
		{
			for (int x = x1; x < x2; x++)
			{
				int pixel = y * intWidth + x;

				buffer[pixel] = 0xff_00_00_00 | r << 16 | g << 8 | b;
			}
		}
	}

	private void drawCircle(float[] data)
	{
		int[] writeBuffer = visibleA ? bufferB : bufferA;

		int square = 4;
				
		for (int i = 0; i < 360; i+=3)
		{
			double radius = Math.random()*10 + data[10];
			
			int x = (int) (halfWidth + radius * precalc.sin(i));
			int y = (int) (halfHeight + radius * precalc.cos(i));

			drawOnBuffer(writeBuffer, x, y, square);
		}
	}

	private void render()
	{
		int pixelIndex = 0;

		int[] readBuffer = visibleA ? bufferB : bufferA;
		int[] writeBuffer = visibleA ? bufferA : bufferB;

		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{
				int modifiedPixelA = 0;
				int modifiedPixelB = 0;

				if (x < intWidth / 2)
				{
					int indexRight = (pixelIndex + 1) < pixelCount ? (pixelIndex + 1) : pixelIndex;
					int bumpRight = readBuffer[indexRight];

					modifiedPixelA = bumpRight;
				}
				else
				{
					int indexLeft = pixelIndex > 0 ? (pixelIndex - 1) : 0;
					int bumpLeft = readBuffer[indexLeft];

					modifiedPixelA = bumpLeft;
				}

				if (y < intHeight / 2)
				{
					int indexBelow = (pixelIndex + imageWidth) < pixelCount ? (pixelIndex + imageWidth) : pixelIndex;
					int bumpBelow = readBuffer[indexBelow];

					modifiedPixelB = bumpBelow;
				}
				else
				{
					int indexAbove = (pixelIndex - imageWidth) > 0 ? (pixelIndex - imageWidth) : pixelIndex;
					int bumpAbove = readBuffer[indexAbove];

					modifiedPixelB = bumpAbove;
				}

				writeBuffer[pixelIndex] = averagePixels(modifiedPixelA, modifiedPixelB);
				pixelIndex++;
			}
		}

		visibleA = !visibleA;
	}

	private int averagePixels(int pixelA, int pixelB)
	{
		int rA = (pixelA & 0x00_ff_00_00) >> 16;
		int gA = (pixelA & 0x00_00_ff_00) >> 8;
		int bA = (pixelA & 0x00_00_00_ff);

		int rB = (pixelB & 0x00_ff_00_00) >> 16;
		int gB = (pixelB & 0x00_00_ff_00) >> 8;
		int bB = (pixelB & 0x00_00_00_ff);

		int r = precalc.clampInt((rA + rB) / 2, 0, 255);
		int g = precalc.clampInt((gA + gB) / 2, 0, 255);
		int b = precalc.clampInt((bA + bB) / 2, 0, 255);
		
		return 0xff_00_00_00 | r << 16 | g << 8 | b;
	}
}