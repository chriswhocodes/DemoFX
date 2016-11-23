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

public class Bobs extends AbstractEffect implements IPixelSource
{
	private byte[] pixelData;

	private int imageWidth;
	private int imageHeight;
	private int scanLine;

	private int pixelCount;

	private PixelWriter pixelWriter;
	private PixelFormat<ByteBuffer> pixelFormat;

	private int initialBlue;
	private int initialGreen;
	private int initialRed;

	private int saturatedB;
	private int saturatedG;
	private int saturatedR;

	private int bobSize;

	class Bob
	{
		private int bx;
		private int by;

		private int dx;
		private int dy;

		private int bw;
		private int bh;

		private int colourB;
		private int colourG;
		private int colourR;

		public void move()
		{
			dx += 4 * precalc.getSignedRandom();
			dy += 4 * precalc.getSignedRandom();

			bx += dx;
			by += dy;

			if (bx > width || bx < 0)
			{
				bx = (int) halfWidth;
			}

			if (by > height || by < 0)
			{
				by = (int) halfHeight;
			}
		}

		private void render()
		{
			for (int y = by; y < by + bh; y++)
			{
				int rowStart = (y * imageWidth * 4);

				for (int x = bx; x < bx + bw; x++)
				{
					int writeIndex = rowStart + x * 4;

					writeIndex = Math.max(writeIndex, 0);
					writeIndex = Math.min(writeIndex, pixelCount - 4 - 1);

					pixelData[writeIndex + 0] = (byte) Math.min(saturatedB, pixelData[writeIndex + 0] + this.colourB);
					pixelData[writeIndex + 1] = (byte) Math.min(saturatedG, pixelData[writeIndex + 1] + this.colourG);
					pixelData[writeIndex + 2] = (byte) Math.min(saturatedR, pixelData[writeIndex + 2] + this.colourR);
				}
			}
		}
	}

	private Bob[] bobs;

	public Bobs(DemoConfig config)
	{
		super(config);

		init(32, 32, Color.BLUE);
	}

	public Bobs(DemoConfig config, int count, int bobSize, Color initialColour)
	{
		super(config);

		init(count, bobSize, initialColour);
	}

	private void init(int count, int bobSize, Color colour)
	{
		int red = (int) (colour.getRed() * 255);
		int green = (int) (colour.getGreen() * 255);
		int blue = (int) (colour.getBlue() * 255);

		int initialReduction = 16;

		this.initialBlue = (int) (blue / initialReduction);
		this.initialGreen = (int) (green / initialReduction);
		this.initialRed = (int) (red / initialReduction);

		this.saturatedB = (blue > 0 ? 255 : 0);
		this.saturatedG = (green > 0 ? 255 : 0);
		this.saturatedR = (red > 0 ? 255 : 0);

		this.bobSize = bobSize;
		this.itemCount = count;

		imageWidth = intWidth;
		imageHeight = intHeight;

		createBobs(itemCount);

		pixelWriter = gc.getPixelWriter();

		pixelFormat = PixelFormat.getByteBgraPreInstance();

		scanLine = imageWidth * 4;

		pixelCount = scanLine * imageHeight;

		pixelData = new byte[pixelCount];

		initialisePixelData();
	}

	private void initialisePixelData()
	{
		int pixel = 0;

		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{
				pixelData[pixel++] = (byte) 0x00; // blue
				pixelData[pixel++] = (byte) 0x00; // green
				pixelData[pixel++] = (byte) 0x00; // red
				pixelData[pixel++] = (byte) 0xFF; // alpha
			}
		}
	}

	private void createBobs(int count)
	{
		bobs = new Bob[count];

		for (int i = 0; i < count; i++)
		{
			Bob bob = new Bob();

			bobs[i] = bob;

			bob.bx = (int) (precalc.getUnsignedRandom() * intWidth);
			bob.by = (int) (precalc.getUnsignedRandom() * intHeight);

			bob.bw = bobSize;
			bob.bh = bobSize;

			bob.bx = Math.max(0, bob.bx - bob.bw);
			bob.by = Math.max(0, bob.by - bob.bh);

			bob.dx = 8;
			bob.dy = 8;

			bob.colourB = initialBlue;
			bob.colourG = initialGreen;
			bob.colourR = initialRed;
		}
	}

	@Override
	public void renderForeground()
	{
		for (Bob bob : bobs)
		{
			bob.move();

			bob.render();
		}

		pixelWriter.setPixels(0, 0, imageWidth, imageHeight, pixelFormat, pixelData, 0, scanLine);		
	}
	
	@Override
	public void setPixelSink(IPixelSink sink)
	{
		this.pixelWriter = sink.getPixelWriter();
	}
}