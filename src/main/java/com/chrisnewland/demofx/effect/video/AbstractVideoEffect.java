/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.video;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.image.PixelWriter;

public abstract class AbstractVideoEffect extends AbstractEffect
{
	protected List<VideoStream> videoStreams;

	protected PixelWriter pixelWriter;

	public AbstractVideoEffect(DemoConfig config)
	{
		super(config);
	}

	@Override
	public void start()
	{		
		for (VideoStream stream : videoStreams)
		{
			stream.start();
		}
	}
	
	@Override
	public void stop()
	{		
		for (VideoStream stream : videoStreams)
		{
			stream.stop();
		}
	}
	
	protected void initialiseVideoStream(VideoStream stream)
	{
		List<VideoStream> list = new ArrayList<VideoStream>();

		list.add(stream);

		initialiseVideoStreams(list);
	}

	protected void initialiseVideoStreams(List<VideoStream> streams)
	{
		itemCount = streams.size();

		this.videoStreams = streams;

		pixelWriter = gc.getPixelWriter();
	}
	
	public void takeSnapshots()
	{
		for (int i = 0; i < itemCount; i++)
		{
			videoStreams.get(i).snapshotVideo();
		}
	}
}