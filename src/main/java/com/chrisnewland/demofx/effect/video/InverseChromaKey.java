/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.video;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IPixelSink;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class InverseChromaKey extends AbstractVideoEffect implements IPixelSink
{
	private WritableImage keyedImage;

	private WritableImage imageTexture;

	private PixelWriter keyedImagePixelWriter;
	
	private PixelReader pixelReader;
	
	private double renderX;
	private double renderY;

	public InverseChromaKey(DemoConfig config)
	{
		super(config);

		int mediaWidth = 480;
		int mediaHeight = 624;
		
		init("/video/dancing.mp4", mediaWidth, mediaHeight);
		
		renderX = (width - mediaWidth) / 2;
		renderY = (height - mediaHeight) / 2;
	}

	public InverseChromaKey(DemoConfig config, String mediaPath, int mediaWidth, int mediaHeight, double renderX, double renderY)
	{
		super(config);

		init(mediaPath, mediaWidth, mediaHeight);
		
		this.renderX = renderX;
		this.renderY = renderY;
	}

	private void init(String mediaPath, int mediaWidth, int mediaHeight)
	{
		VideoStream videoStream = new VideoStream(mediaPath, mediaWidth, mediaHeight);

		initialiseVideoStream(videoStream);

		keyedImage = new WritableImage(mediaWidth, mediaHeight);

		keyedImagePixelWriter = keyedImage.getPixelWriter();
		
		imageTexture = new WritableImage(mediaWidth, mediaHeight);
		
		pixelReader = imageTexture.getPixelReader();
	}

	@Override
	public void renderForeground()
	{		
		takeSnapshots();
		
		doVideoEffect();

		gc.drawImage(keyedImage, renderX, renderY);
	}

	private void doVideoEffect()
	{
		VideoStream videoStream = videoStreams.get(0);

		final int frameWidth = videoStream.getFrameWidth();

		final int frameHeight = videoStream.getFrameHeight();

		final int[] rawFrameData = videoStream.getRawFrameData();

		final int[] processedFrameData = videoStream.getProcessedFrameData();

		int writeIndex = 0;

		for (int y = 0; y < frameHeight; y++)
		{
			for (int x = 0; x < frameWidth; x++)
			{
				int pixel = rawFrameData[writeIndex];

				if (makeTransparent(pixel))
				{
					pixel = 0x00000000;
				}
				else
				{
					pixel = pixelReader.getArgb(x, y);
				}

				processedFrameData[writeIndex++] = pixel;
			}
		}

		videoStream.writeProcessedFrame(keyedImagePixelWriter);
	}

	// Override this for different keyed colours
	protected boolean makeTransparent(int pixel)
	{
		int red = (pixel & 0x00ff0000) >> 16;
		int green = (pixel & 0x0000ff00) >> 8;

		return (green > 55 && red < (green-10));
	}

	@Override
	public int getWidth()
	{
		return intWidth;
	}

	@Override
	public int getHeight()
	{
		return intHeight;
	}

	@Override
	public PixelWriter getPixelWriter()
	{
		return imageTexture.getPixelWriter();
	}

	@Override
	public void redraw()
	{
	}
}