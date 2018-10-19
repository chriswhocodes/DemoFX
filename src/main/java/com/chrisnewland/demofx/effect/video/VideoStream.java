package com.chrisnewland.demofx.effect.video;

import java.nio.IntBuffer;
import java.util.Optional;

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

import java.lang.reflect.Method;

public class VideoStream implements IPixelSource
{
	private MediaPlayer player;

	private int[] rawFrameData;
	private int[] processedFrameData;

	private int frameWidth;
	private int frameHeight;

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

		Optional<VideoDataBuffer> buf = getLastestFrame();
		
		if (buf.isPresent())
		{
			VideoDataBuffer newBuffer = buf.get().convertToFormat(VideoFormat.BGRA_PRE); // int format

			newBuffer.getBufferForPlane(VideoDataBuffer.PACKED_FORMAT_PLANE).asIntBuffer().get(rawFrameData);
			
			newBuffer.releaseFrame();
		}
	}

	private Optional<VideoDataBuffer> getLastestFrame() 
	{
		Optional<VideoDataBuffer> videoDataBuffer = Optional.empty();
		try
		{
			Method method = player.getClass().getMethod("getLatestFrame");
			method.setAccessible(true);
			Object tmp = method.invoke(player);
			videoDataBuffer = Optional.of((VideoDataBuffer)tmp);

			// buf = player.getLatestFrame();// java modularization(java jigsaw).
		} catch (Exception e) {}
		return videoDataBuffer;
	}

	public int[] getRawFrameData() {
		return rawFrameData;
	}

	public int[] getProcessedFrameData() {
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
