/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import java.nio.IntBuffer;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public class Shift extends AbstractEffect
{
	private PixelWriter pixelWriter;
	private PixelFormat<IntBuffer> pixelFormat;

	private int[] sourceData;
	private int[] destBuffer0;
	private int[] destBuffer1;

	private int factor = 8;
	private int amplitude = 8;
	
	public enum ShiftMode {HORIZONTAL,VERTICAL,BOTH,REFLECTION}
	
	private ShiftMode mode;
	private int startRow = 0;

	private double angle;

	private int imageWidth;
	private int imageHeight;

	private int pixelWriterStartX;
	private int pixelWriterStartY;

	public Shift(DemoConfig config)
	{
		super(config);

		init("/cham.jpeg");
				
		mode = ShiftMode.REFLECTION;
		startRow = (int)(imageHeight * 0.5);
	}
	
	public Shift(DemoConfig config, ShiftMode shiftMode, String filename)
	{
		super(config);
		
		this.mode = shiftMode;

		init(filename);
	}
	
	public Shift(DemoConfig config, int startRow, String filename)
	{
		super(config);
		
		this.startRow = startRow;
		mode = ShiftMode.REFLECTION;

		init(filename);
	}

	private void init(String filename)
	{
		Image loaded = ImageUtil.loadImageFromResources(filename);

		loaded = ImageUtil.createBorderedImage(loaded, amplitude, amplitude);

		imageWidth = (int) loaded.getWidth();
		imageHeight = (int) loaded.getHeight();

		pixelWriterStartX = (intWidth - imageWidth) / 2;
		pixelWriterStartY = (intHeight - imageHeight) / 2;

		pixelWriter = gc.getPixelWriter();

		pixelFormat = PixelFormat.getIntArgbInstance();

		int pixelCount = imageWidth * imageHeight;

		destBuffer0 = new int[pixelCount];
		destBuffer1 = new int[pixelCount];

		PixelReader reader = loaded.getPixelReader();

		int pixel = 0;

		sourceData = new int[pixelCount];

		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{
				sourceData[pixel++] = reader.getArgb(x, y);
			}
		}
	}

	@Override
	public void renderForeground()
	{
		angle += 5;

		if (angle > 360)
		{
			angle = 0;
		}
		
		switch(mode)
		{
		case HORIZONTAL:
			horizontal(sourceData, destBuffer0);
			pixelWriter.setPixels(pixelWriterStartX, pixelWriterStartY, imageWidth, imageHeight, pixelFormat, destBuffer0, 0,
					imageWidth);
			break;
		case VERTICAL:
			vertical(sourceData, destBuffer0);
			pixelWriter.setPixels(pixelWriterStartX, pixelWriterStartY, imageWidth, imageHeight, pixelFormat, destBuffer0, 0,
					imageWidth);
			break;
		case BOTH:
			horizontal(sourceData, destBuffer0);
			vertical(destBuffer0, destBuffer1);
			pixelWriter.setPixels(pixelWriterStartX, pixelWriterStartY, imageWidth, imageHeight, pixelFormat, destBuffer1, 0,
					imageWidth);
			break;
		case REFLECTION:
			reflection(sourceData, destBuffer0);
			pixelWriter.setPixels(pixelWriterStartX, pixelWriterStartY, imageWidth, imageHeight, pixelFormat, destBuffer0, 0,
					imageWidth);
			break;
		}
	}

	private void horizontal(int[] sourcePixels, int[] destPixels)
	{
		int writeIndex = 0;
		
		for (int y = 0; y < imageHeight; y++)
		{
			int rowStart = y * imageWidth;

			for (int x = 0; x < imageWidth; x++)
			{
				int shift = x + (int) (amplitude * precalc.sin(y * factor + angle));

				int origPixel = 0;

				if (shift >= 0 && shift < imageWidth)
				{
					origPixel = sourcePixels[rowStart + shift];
				}

				destPixels[writeIndex++] = origPixel;
			}
		}
	}

	private void reflection(int[] sourcePixels, int[] destPixels)
	{
		int writeIndex = 0;
		
		if (startRow > 0)
		{
			int unprocessed = startRow*imageWidth;
			System.arraycopy(sourcePixels, 0, destPixels, 0, unprocessed);
			
			writeIndex = unprocessed;
		}

		int rowsToReflect = imageHeight - startRow;
		
		// read upwards, reflection must be < half height
		for (int y = 0; y < rowsToReflect; y++)
		{
			int rowStart = startRow * imageWidth - y * imageWidth;
			
			if (rowStart < 0)
			{
				return;
			}

			for (int x = 0; x < imageWidth; x++)
			{
				int shift = x + (int) (amplitude * precalc.sin(y * factor + angle));

				int origPixel = 0;

				if (shift >= 0 && shift < imageWidth)
				{
					origPixel = sourcePixels[rowStart + shift];
				}

				destPixels[writeIndex++] = origPixel;
			}
		}
	}

	
	private void vertical(int[] sourcePixels, int[] destPixels)
	{
		int writeIndex = 0;

		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{
				int shift = y + (int) (amplitude * precalc.sin(x * factor + angle));

				int origPixel = 0;

				if (shift >= 0 && shift < imageHeight)
				{
					origPixel = sourcePixels[imageWidth * shift + x];
				}

				destPixels[writeIndex++] = origPixel;
			}
		}
	}
}