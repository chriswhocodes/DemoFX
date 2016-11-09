/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import java.nio.ByteBuffer;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.effect.IPixelSink;
import com.chrisnewland.demofx.effect.IPixelSource;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rainbow extends AbstractEffect implements IPixelSource
{
	private byte[] pixelData;
	private byte[] precalculatedRGB;

	private int imageWidth;
	private int imageHeight;
	private int scanLine;

	private int pixelCount;

	private PixelWriter pixelWriter;
	private PixelFormat<ByteBuffer> pixelFormat;

	private int readOffset = 0;

	public Rainbow(DemoConfig config)
	{
		super(config);

		init();
	}

	private void init()
	{
		this.imageWidth = intWidth;
		this.imageHeight = intHeight;

		pixelWriter = gc.getPixelWriter();

		pixelFormat = PixelFormat.getByteBgraPreInstance();

		scanLine = imageWidth * 4;

		pixelCount = scanLine * imageHeight;

		precalculatedRGB = new byte[pixelCount];

		pixelData = new byte[pixelCount];

		precalculate();
	}

	private void precalculate()
	{
		double count = 0;

		Color rainbowColor = getCycleColour();

		for (int x = 0; x < scanLine; x += 4)
		{
			count += 360.0 / (double) imageWidth;

			if (count > 1)
			{
				rainbowColor = getCycleColour();
				count -= 1;
			}

			int r = (int) (rainbowColor.getRed() * 255);
			int g = (int) (rainbowColor.getGreen() * 255);
			int b = (int) (rainbowColor.getBlue() * 255);

			for (int y = 0; y < imageHeight; y++)
			{
				int writeIndex = x + (y * scanLine);

				precalculatedRGB[writeIndex + 0] = (byte) r;
				precalculatedRGB[writeIndex + 1] = (byte) g;
				precalculatedRGB[writeIndex + 2] = (byte) b;
				precalculatedRGB[writeIndex + 3] = (byte) 255;

			}
		}
	}

	@Override
	public void renderForeground()
	{
		render();

		readOffset += 64 / 2;

		if (readOffset >= precalculatedRGB.length)
		{
			readOffset = 0;
		}

		pixelWriter.setPixels(0, 0, imageWidth, imageHeight, pixelFormat, pixelData, 0, scanLine);
	}

	private void render()
	{
		int first = precalculatedRGB.length - readOffset;
		int second = precalculatedRGB.length - first;

		System.arraycopy(precalculatedRGB, readOffset, pixelData, 0, first);
		System.arraycopy(precalculatedRGB, 0, pixelData, first, second);
	}

	@Override
	public void setPixelSink(IPixelSink sink)
	{
		this.pixelWriter = sink.getPixelWriter();
	}
}