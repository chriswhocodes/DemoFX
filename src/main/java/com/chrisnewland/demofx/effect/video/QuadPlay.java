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

public class QuadPlay extends AbstractVideoEffect
{
	private VideoStream streamTL;
	private VideoStream streamTR;
	private VideoStream streamBL;
	private VideoStream streamBR;

	private WritableImage imageTL;
	private WritableImage imageTR;
	private WritableImage imageBL;
	private WritableImage imageBR;

	private PixelWriter writerTL;
	private PixelWriter writerTR;
	private PixelWriter writerBL;
	private PixelWriter writerBR;

	public QuadPlay(DemoConfig config)
	{
		super(config);

		VideoStream streamTL = new VideoStream("/video/bike.m4v", 320, 320);
		VideoStream streamTR = new VideoStream("/video/bike.m4v", 320, 320);
		VideoStream streamBL = new VideoStream("/video/bike.m4v", 320, 320);
		VideoStream streamBR = new VideoStream("/video/bike.m4v", 320, 320);

		init(streamTL, streamTR, streamBL, streamBR);
	}

	public QuadPlay(DemoConfig config, VideoStream tl, VideoStream tr, VideoStream bl, VideoStream br)
	{
		super(config);

		init(tl, tr, bl, br);
	}

	private void init(VideoStream tl, VideoStream tr, VideoStream bl, VideoStream br)
	{
		this.streamTL = tl;
		this.streamTR = tr;
		this.streamBL = bl;
		this.streamBR = br;

		imageTL = new WritableImage(640, 360);
		imageTR = new WritableImage(640, 360);
		imageBL = new WritableImage(640, 360);
		imageBR = new WritableImage(640, 360);

		writerTL = imageTL.getPixelWriter();
		writerTR = imageTR.getPixelWriter();
		writerBL = imageBL.getPixelWriter();
		writerBR = imageBR.getPixelWriter();

		List<VideoStream> streams = new ArrayList<>();

		streams.add(streamTL);
		streams.add(streamTR);
		streams.add(streamBL);
		streams.add(streamBR);

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

		gc.drawImage(imageTL, 0, 0, halfWidth, halfHeight);
		gc.drawImage(imageTR, halfWidth, 0, halfWidth, halfHeight);
		gc.drawImage(imageBL, 0, halfHeight, halfWidth, halfHeight);
		gc.drawImage(imageBR, halfWidth, halfHeight, halfWidth, halfHeight);

		drawBorders(8);
	}

	private void drawBorders(int border)
	{
		gc.setFill(Color.WHITE);

		gc.fillRect(0, 0, width, border);
		gc.fillRect(0, height - border, width, border);
		gc.fillRect(0, 0, border, height);
		gc.fillRect(width - border, 0, border, height);
		gc.fillRect(0, halfHeight - border / 2, width, border);
		gc.fillRect(halfWidth - border / 2, 0, border, height);
	}

	private void doVideoEffect()
	{
		streamTL.writeRawFrame(writerTL);
		streamTR.writeRawFrame(writerTR);
		streamBL.writeRawFrame(writerBL);
		streamBR.writeRawFrame(writerBR);
	}
}