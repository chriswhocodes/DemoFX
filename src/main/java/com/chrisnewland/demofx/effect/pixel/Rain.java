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
import javafx.scene.image.PixelWriter;

public class Rain extends AbstractEffect
{
	private PixelWriter pixelWriter;
	private PixelFormat<IntBuffer> pixelFormat;

	// 2 maps as update next while averaging from previous
	private float[] bumpMapA;
	private float[] bumpMapB;

	private boolean writeMapA;

	private int[] imageData;
	private int[] modifiedImageData;
	
	private int imageWidth;
	private int imageHeight;
	private int pixelCount;
	
	private static final int INITIAL_RADIUS = 3;
	private static final float DECAY = 0.05f;
	private static final int MAX_BUMP_HEIGHT = 1024;
	
	public Rain(DemoConfig config)
	{
		super(config);

		init("/cham.jpeg");
	}

	public Rain(DemoConfig config, String backgroundImage)
	{
		super(config);

		init(backgroundImage);
	}
	
	private void init(String filename)
	{
		Image image = ImageUtil.loadImageFromResources(filename);

		imageWidth = (int)image.getWidth();
		imageHeight = (int)image.getHeight();
		
		pixelCount = imageWidth * imageHeight;
		
		imageData = new int[pixelCount];
		
		writeImageToIntArray(image, imageData, imageWidth, imageHeight);
		
		pixelWriter = gc.getPixelWriter();

		pixelFormat = PixelFormat.getIntArgbInstance();

		bumpMapA = new float[pixelCount];
		bumpMapB = new float[pixelCount];
		
		writeMapA = true;

		modifiedImageData = new int[pixelCount];
	}
	
	@Override
	public void renderForeground()
	{
		addDrop((int) (width * precalc.getUnsignedRandom()), (int) (height * precalc.getUnsignedRandom()));

		calculateRipples();
		
		pixelWriter.setPixels((intWidth-imageWidth)/2, (intHeight-imageHeight)/2, imageWidth, imageHeight, pixelFormat, modifiedImageData, 0, imageWidth);
	}

	public void addDrop(int dropX, int dropY)
	{
		int dropRadius = (int)(INITIAL_RADIUS * precalc.getSignedRandom());
		
		float[] writeMap = writeMapA ? bumpMapA : bumpMapB;
		
		for (int y = dropY - dropRadius; y < dropY + dropRadius; y++)
		{
			for (int x = dropX - dropRadius; x < dropX + dropRadius; x++)
			{
				if (y >= 0 && y < imageHeight && x >= 0 && x < imageWidth)
				{
					writeMap[x + (y * imageWidth)] += MAX_BUMP_HEIGHT * precalc.getUnsignedRandom();
				}
			}
		}
	}
	
	public void calculateRipples()
	{
		int pixelIndex = 0;

		float[] readMap = writeMapA ? bumpMapB : bumpMapA;
		float[] writeMap = writeMapA ? bumpMapA : bumpMapB;
		
		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{				
				int indexAbove = (pixelIndex - imageWidth) > 0 ? (pixelIndex - imageWidth) : pixelIndex;
				int indexBelow = (pixelIndex + imageWidth) < pixelCount ? (pixelIndex + imageWidth) : pixelIndex;
				int indexLeft = pixelIndex > 0 ? (pixelIndex - 1) : 0;
				int indexRight = (pixelIndex + 1) < pixelCount ? (pixelIndex + 1) : pixelIndex;
				
				float bumpAbove = readMap[indexAbove];
				float bumpBelow = readMap[indexBelow];
				float bumpLeft = readMap[indexLeft];
				float bumpRight = readMap[indexRight];

				float bumpHeight = (bumpAbove + bumpBelow + bumpLeft + bumpRight) / 2;

				bumpHeight -= writeMap[pixelIndex];

				bumpHeight -= bumpHeight * DECAY;

				writeMap[pixelIndex] = bumpHeight;

				bumpHeight = (MAX_BUMP_HEIGHT - bumpHeight);

				int pixelX = (int) (x * bumpHeight / MAX_BUMP_HEIGHT);
				int pixelY = (int) (y * bumpHeight / MAX_BUMP_HEIGHT);

				if (pixelX >= imageWidth)
				{
					pixelX = imageWidth - 1;
				}
				else if (pixelX < 0)
				{
					pixelX = 0;
				}

				if (pixelY >= imageHeight)
				{
					pixelY = imageHeight - 1;
				}
				else if (pixelY < 0)
				{
					pixelY = 0;
				}

				modifiedImageData[pixelIndex] = imageData[pixelX + (pixelY * imageWidth)];
				pixelIndex++;
			}
		}
		
		writeMapA = !writeMapA;
	}
}