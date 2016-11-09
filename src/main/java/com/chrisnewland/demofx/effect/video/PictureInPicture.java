/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.video;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PictureInPicture extends AbstractVideoEffect
{
	private VideoStream streamMain;
	private VideoStream streamInPicture;

	private WritableImage inPictureImage;
	private PixelWriter inPictureImagePixelWriter;

	private int border = 4;
	private int inset = 8;

	private int inPicXPos;
	private int inPicYPos;
	private int inPicWidth;
	private int inPicHeight;

	private int inPicFrameXPos;
	private int inPicFrameYPos;
	private int inPicFrameWidth;
	private int inPicFrameHeight;

	public PictureInPicture(DemoConfig config)
	{
		super(config);

		init(new VideoStream("/video/bike.m4v", 320, 320), new VideoStream("/video/bike.m4v", 320, 320));
	}

	public PictureInPicture(DemoConfig config, VideoStream mainStream, VideoStream inPictureStream)
	{
		super(config);

		init(mainStream, inPictureStream);
	}

	private void init(VideoStream mainStream, VideoStream inPictureStream)
	{
		this.streamMain = mainStream;
		this.streamInPicture = inPictureStream;

		inPictureImage = new WritableImage(inPictureStream.getFrameWidth(), inPictureStream.getFrameHeight());
		inPictureImagePixelWriter = inPictureImage.getPixelWriter();

		inPicWidth = streamInPicture.getFrameWidth() / 4;
		inPicHeight = streamInPicture.getFrameHeight() / 4;

		inPicFrameXPos = streamMain.getFrameWidth() - inPicWidth - border * 2 - inset;
		inPicFrameYPos = streamMain.getFrameHeight() - inPicHeight - border * 2 - inset;

		inPicFrameWidth = inPicWidth + border * 2;
		inPicFrameHeight = inPicHeight + border * 2;

		inPicXPos = inPicFrameXPos + border;
		inPicYPos = inPicFrameYPos + border;

		List<VideoStream> streams = new ArrayList<>();

		streams.add(streamMain);
		streams.add(streamInPicture);

		customInitialise(streams);
	}

	public void customInitialise(List<VideoStream> streams)
	{
		initialiseVideoStreams(streams);
	}

	@Override
	public void renderForeground()
	{
		takeSnapshots();

		doVideoEffect();

		streamMain.writeRawFrame(pixelWriter);

		showPictureInPicture();
	}

	private void showPictureInPicture()
	{
		gc.setFill(Color.WHITE);

		gc.fillRect(inPicFrameXPos, inPicFrameYPos, inPicFrameWidth, inPicFrameHeight);

		gc.drawImage(inPictureImage, inPicXPos, inPicYPos, inPicWidth, inPicHeight);
	}

	private void doVideoEffect()
	{
		// write the in-picture frame to the WritableImage PixelWriter
		streamInPicture.writeRawFrame(inPictureImagePixelWriter);
	}
}