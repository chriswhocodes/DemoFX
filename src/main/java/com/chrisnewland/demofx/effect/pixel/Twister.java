/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import java.nio.ByteBuffer;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class Twister extends AbstractEffect
{
	private byte[] pixelData;

	private int columnWidth;

	private double speed = 0;
	private double angle = 0;
	private double twist = 1;

	private int imageWidth;
	private int imageHeight;
	private int scanLine;

	private int pixelCount;

	private WritableImage imageTexture;

	private PixelWriter pixelWriter;
	private PixelFormat<ByteBuffer> pixelFormat;

	public Twister(DemoConfig config)
	{
		super(config);

		init();
	}

	private void init()
	{
		this.imageWidth = intWidth / 2;
		this.imageHeight = intHeight;

		columnWidth = imageWidth / 4;

		imageTexture = new WritableImage(imageWidth, imageHeight);

		pixelWriter = imageTexture.getPixelWriter();

		pixelFormat = PixelFormat.getByteBgraPreInstance();

		scanLine = imageWidth * 4;

		pixelCount = scanLine * imageHeight;

		pixelData = new byte[pixelCount];
	}

	@Override
	public void renderForeground()
	{
		render(angle / 60);

		speed += 0.5;

		if (speed >= 360)
		{
			speed -= 360;
		}

		angle += 4 * precalc.sin(speed);

		if (angle >= 1080)
		{
			angle -= 1080;
		}

		twist = 16 + precalc.sin(angle) * 8;

		pixelWriter.setPixels(0, 0, imageWidth, imageHeight, pixelFormat, pixelData, 0, scanLine);

		gc.drawImage(imageTexture, 0, 0);
		gc.drawImage(imageTexture, imageWidth, 0);
	}

	private void render(double distort)
	{
		for (int y = 0; y < intHeight; y++)
		{
			double xOffset = precalc.sin(angle + (y - halfHeight)) * distort * 2;

			int writePos = y * scanLine;

			double start = angle + (double) y / twist;

			int x1 = (int) (xOffset + imageWidth / 2 + precalc.sin(start) * columnWidth);
			int x2 = (int) (xOffset + imageWidth / 2 + precalc.sin(start + 90) * columnWidth);
			int x3 = (int) (xOffset + imageWidth / 2 + precalc.sin(start + 180) * columnWidth);
			int x4 = (int) (xOffset + imageWidth / 2 + precalc.sin(start + 270) * columnWidth);

			makeAlpha(writePos);

			if (x1 < x2)
			{
				renderLine(writePos, x1, x2, (byte) 0xff, (byte) 0, (byte) 0);
			}

			if (x2 < x3)
			{
				renderLine(writePos, x2, x3, (byte) 0xff, (byte) 0x55, (byte) 0);
			}

			if (x3 < x4)
			{
				renderLine(writePos, x3, x4, (byte) 0xff, (byte) 0, (byte) 0);
			}

			if (x4 < x1)
			{
				renderLine(writePos, x4, x1, (byte) 0xff, (byte) 0x55, (byte) 0);
			}
		}
	}

	private void makeAlpha(int writePos)
	{
		for (int x = 0; x < imageWidth * 4; x += 4)
		{
			pixelData[writePos + x + 0] = (byte) 0;
			pixelData[writePos + x + 1] = (byte) 0;
			pixelData[writePos + x + 2] = (byte) 0;
			pixelData[writePos + x + 3] = (byte) 0;
		}
	}

	private void renderLine(int writePos, int start, int end, byte blue, byte green, byte red)
	{
		final int startWritePos = writePos + start * 4;

		final int lineWidth = end - start;

		for (int x = 1; x < lineWidth - 1; x++)
		{
			int base = startWritePos + x * 4;

			pixelData[base + 0] = blue;
			pixelData[base + 1] = green;
			pixelData[base + 2] = red;
			pixelData[base + 3] = (byte) 0xff;
		}

		pixelData[startWritePos + 0] = (byte) 0xff;
		pixelData[startWritePos + 1] = (byte) 0xff;
		pixelData[startWritePos + 2] = (byte) 0xff;
		pixelData[startWritePos + 3] = (byte) 0xff;

		pixelData[startWritePos + lineWidth*4 + 0] = (byte) 0xff;
		pixelData[startWritePos + lineWidth*4 + 1] = (byte) 0xff;
		pixelData[startWritePos + lineWidth*4 + 2] = (byte) 0xff;
		pixelData[startWritePos + lineWidth*4 + 3] = (byte) 0xff;
	}
}