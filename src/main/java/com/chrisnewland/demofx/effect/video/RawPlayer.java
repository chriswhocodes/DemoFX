/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.video;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IPixelSink;
import com.chrisnewland.demofx.effect.IPixelSource;

public class RawPlayer extends AbstractVideoEffect implements IPixelSource
{
	public RawPlayer(DemoConfig config)
	{
		super(config);

		init("/video/bike.m4v", 320, 320);
	}
	
	public RawPlayer(DemoConfig config, String mediaPath, int mediaWidth, int mediaHeight)
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
		
		videoStreams.get(0).writeRawFrame(pixelWriter);
	}

	@Override
	public void setPixelSink(IPixelSink sink)
	{
		this.pixelWriter = sink.getPixelWriter();
		videoStreams.get(0).setPixelSink(sink);
	}
}