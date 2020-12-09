package com.chrisnewland.demofx.effect.video;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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

	private MethodHandle mhGetLatestFrame = null;

	private VideoDataBuffer buf;

	private PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();

	public VideoStream(String mediaPath, int mediaWidth, int mediaHeight)
	{
		tryMethodHandle();

		Media media = new Media(getClass().getResource(mediaPath)
										  .toExternalForm());

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

	private void tryMethodHandle()
	{
		try
		{
			MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

			MethodType methodType = MethodType.methodType(VideoDataBuffer.class);

			mhGetLatestFrame = publicLookup.findVirtual(MediaPlayer.class, "impl_getLatestFrame", methodType);

			System.out.println("Obtained MethodHandle to MediaPlayer.impl_getLatestFrame");
		}
		catch (ReflectiveOperationException roe)
		{
			System.out.println("MediaPlayer.impl_getLatestFrame is not available, there will be no video playback");
		}
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
		if (mhGetLatestFrame == null)
		{
			return;
		}

		try
		{
			buf = (VideoDataBuffer) mhGetLatestFrame.invoke(player);

			if (buf != null)
			{
				buf = buf.convertToFormat(VideoFormat.BGRA_PRE); // int format

				buf.getBufferForPlane(VideoDataBuffer.PACKED_FORMAT_PLANE)
				   .asIntBuffer()
				   .get(rawFrameData);

				buf.releaseFrame();
			}
		}
		catch (Throwable t)
		{
			System.out.println("Could not grab frame: " + t.getMessage());
		}
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
