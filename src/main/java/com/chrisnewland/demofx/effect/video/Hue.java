/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.video;

import com.chrisnewland.demofx.DemoConfig;

public class Hue extends AbstractVideoEffect
{
	private int red;
	private int green;
	private int blue;

	private int frame = 0;
	private int mask = 7;

	public Hue(DemoConfig config)
	{
		super(config);

		init("/video/bike.m4v", 320, 320);
	}
	
	public Hue(DemoConfig config, String mediaPath, int mediaWidth, int mediaHeight)
	{
		super(config);

		init(mediaPath, mediaWidth, mediaHeight);
	}

	private void init(String mediaPath, int mediaWidth, int mediaHeight)
	{
		mask = 7;
		
		VideoStream videoStream = new VideoStream(mediaPath, mediaWidth, mediaHeight);
		
		initialiseVideoStream(videoStream);

		change();
	}

	@Override
	public void renderForeground()
	{
		takeSnapshots();
		
		doVideoEffect();
		
		videoStreams.get(0).writeProcessedFrame(pixelWriter);
	}

	private void doVideoEffect()
	{
		int writeIndex = 0;

		if (frame++ > 60)
		{
			change();
			frame = 0;
		}
		
		VideoStream videoStream = videoStreams.get(0);

		int hue = (0xff000000 | (red << 16) | (green << 8) | blue);

		final int frameWidth = videoStream.getFrameWidth();

		final int frameHeight = videoStream.getFrameHeight();

		final int[] rawFrameData = videoStream.getRawFrameData();

		final int[] processedFrameData = videoStream.getProcessedFrameData();

		for (int y = 0; y < frameHeight; y++)
		{
			for (int x = 0; x < frameWidth; x++)
			{
				int pixel = rawFrameData[writeIndex];

				pixel &= hue;

				processedFrameData[writeIndex++] = pixel;
			}
		}
	}

	private void change()
	{
		red = 255 * (mask & 1);
		green = 255 * (mask & 2);
		blue = 255 * (mask & 4);

		mask++;

		if (mask > 7)
		{
			mask = 1;
		}
	}
}