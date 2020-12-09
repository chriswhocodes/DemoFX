package com.chrisnewland.demofx.effect.video;

import java.nio.IntBuffer;

import com.chrisnewland.demofx.effect.IPixelSink;
import com.chrisnewland.demofx.effect.IPixelSource;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status;

public class VideoStream implements IPixelSource
{
	private MediaPlayer player;

	private int[] rawFrameData;
	private int[] processedFrameData;

	private int frameWidth;
	private int frameHeight;

	private VideoDataBuffer buf;

	private PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();

	public VideoStream(String mediaPath, int mediaWidth, int mediaHeight)
	{
		Media media = new Media(getClass().getResource(mediaPath).toExternalForm());

		player = new MediaPlayer(media);

		new MediaView(player);

		player.setAutoPlay(false);

		player.setCycleCount(1);

		frameWidth = mediaWidth;
		frameHeight = mediaHeight;

		int pixelCount = frameWidth * frameHeight;

		rawFrameData = new int[pixelCount];
		processedFrameData = new int[pixelCount];
	}

	public void start()
	{
		player.play();

	}

	public synchronized void stop()
	{
		if (player != null && player.getStatus() == Status.PLAYING)
		{
			player.stop();
			player.dispose();
		}
	}

	public void writeRawFrame(PixelWriter pixelWriter)
	{
		pixelWriter.setPixels(0, 0, frameWidth, frameHeight, pixelFormat, rawFrameData, 0, frameWidth);
	}

	public void writeProcessedFrame(PixelWriter pixelWriter)
	{
		pixelWriter.setPixels(0, 0, frameWidth, frameHeight, pixelFormat, processedFrameData, 0, frameWidth);
	}

	// http://stackoverflow.com/questions/4041840/function-to-convert-ycbcr-to-rgb
	public synchronized void snapshotVideo()
	{
		/*buf = player.impl_getLatestFrame();

		if (buf != null)
		{
			buf = buf.convertToFormat(VideoFormat.BGRA_PRE); // int format

			buf.getBufferForPlane(VideoDataBuffer.PACKED_FORMAT_PLANE).asIntBuffer().get(rawFrameData);
			
			buf.releaseFrame();
		}*/
	}

	public int[] getRawFrameData()
	{
		return rawFrameData;
	}

	public int[] getProcessedFrameData()
	{
		return processedFrameData;
	}

	public int getFrameWidth()
	{
		return frameWidth;
	}

	public int getFrameHeight()
	{
		return frameHeight;
	}

	@Override
	public void setPixelSink(IPixelSink sink)
	{
		frameWidth = sink.getWidth();
		frameHeight = sink.getHeight();
	}
}
