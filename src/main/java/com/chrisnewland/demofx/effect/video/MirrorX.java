/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.video;

import com.chrisnewland.demofx.DemoConfig;

public class MirrorX extends AbstractVideoEffect
{
	public MirrorX(DemoConfig config)
	{
		super(config);

		init("/video/bike.m4v", 320, 320);
	}

	public MirrorX(DemoConfig config, String mediaPath, int mediaWidth, int mediaHeight)
	{
		super(config);

		init(mediaPath, mediaWidth, mediaHeight);
	}

	private void init(String mediaPath, int mediaWidth, int mediaHeight)
	{
		VideoStream videoStream = new VideoStream(mediaPath, mediaWidth, mediaHeight);

		initialiseVideoStream(videoStream);
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
		VideoStream videoStream = videoStreams.get(0);

		final int frameWidth = videoStream.getFrameWidth();

		final int frameHeight = videoStream.getFrameHeight();

		final int[] rawFrameData = videoStream.getRawFrameData();

		final int[] processedFrameData = videoStream.getProcessedFrameData();

		int writeIndex = 0;

		final int halfImageWidth = frameWidth / 2;

		for (int y = 0; y < frameHeight; y++)
		{
			// block copy first half line
			System.arraycopy(rawFrameData, writeIndex, processedFrameData, writeIndex, halfImageWidth);

			writeIndex += halfImageWidth;

			int readIndex = writeIndex;

			for (int x = halfImageWidth; x < frameWidth; x++)
			{
				int pixel = rawFrameData[readIndex--];

				processedFrameData[writeIndex++] = pixel;
			}
		}
	}
}