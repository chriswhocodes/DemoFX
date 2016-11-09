/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.util;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class ImageUtil
{
	public static Image loadImageFromResources(String filename)
	{
		if (!filename.startsWith("/"))
		{
			filename = "/" + filename;
		}

		return new Image(ImageUtil.class.getResourceAsStream(filename));
	}

	public static WritableImage loadWritableImageFromResources(String filename)
	{
		Image image = loadImageFromResources(filename);

		int imageWidth = (int) image.getWidth();
		int imageheight = (int) image.getHeight();

		WritableImage writableImage = new WritableImage(image.getPixelReader(), 0, 0, imageWidth, imageheight);

		return writableImage;
	}

	public static void saveImage(Image image, File file)
	{
		RenderedImage ri = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);

		try
		{
			ImageIO.write(ri, "PNG", file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static WritableImage createImageFromCanvas(Canvas canvas, double width, double height, boolean transparent)
	{
		WritableImage image = new WritableImage((int) width, (int) height);

		SnapshotParameters params = new SnapshotParameters();

		if (transparent)
		{
			params.setFill(Color.TRANSPARENT);
		}

		canvas.snapshot(params, image);

		return image;
	}

	public static int[] readImageToArray(Image image, int imageWidth, int imageHeight)
	{
		int[] dest = new int[imageWidth * imageHeight];

		PixelReader reader = image.getPixelReader();

		int pixel = 0;

		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{
				dest[pixel++] = reader.getArgb(x, y);
			}
		}

		return dest;
	}

	public static Image createBorderedImage(Image image, int borderX, int borderY)
	{
		double width = image.getWidth() + 2 * borderX;
		double height = image.getHeight() + 2 * borderY;

		Canvas canvas = new Canvas(width, height);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);

		gc.drawImage(image, borderX, borderY);

		Image snap = ImageUtil.createImageFromCanvas(gc.getCanvas(), width, height, false);

		return snap;
	}

	public static Image replaceColour(Image image, Color colorOld, Color colorNew)
	{
		PixelReader reader = image.getPixelReader();

		int imgWidth = (int) image.getWidth();
		int imgHeight = (int) image.getHeight();

		WritableImage result = new WritableImage(imgWidth, imgHeight);

		PixelWriter pixelWriter = result.getPixelWriter();

		byte[] imageData = new byte[imgWidth * imgHeight * 4];

		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();

		int pixel = 0;

		byte oldR = (byte) (colorOld.getRed() * 255);
		byte oldG = (byte) (colorOld.getGreen() * 255);
		byte oldB = (byte) (colorOld.getBlue() * 255);
		byte oldA = (byte) (colorOld.getOpacity() * 255);

		byte newR = (byte) (colorNew.getRed() * 255);
		byte newG = (byte) (colorNew.getGreen() * 255);
		byte newB = (byte) (colorNew.getBlue() * 255);
		byte newA = (byte) (colorNew.getOpacity() * 255);

		for (int y = 0; y < imgHeight; y++)
		{
			for (int x = 0; x < imgWidth; x++)
			{
				int color = reader.getArgb(x, y);

				byte alpha = (byte) ((color >> 24) & 255);
				byte red = (byte) ((color >> 16) & 255);
				byte green = (byte) ((color >> 8) & 255);
				byte blue = (byte) (color & 255);

				if (oldR == red && oldG == green && oldB == blue && oldA == alpha)
				{
					red = newR;
					green = newG;
					blue = newB;
					alpha = newA;
				}

				imageData[pixel++] = blue;
				imageData[pixel++] = green;
				imageData[pixel++] = red;
				imageData[pixel++] = alpha;
			}
		}

		pixelWriter.setPixels(0, 0, imgWidth, imgHeight, pixelFormat, imageData, 0, imgWidth * 4);

		return result;
	}

	public static Image tintImage(Image image, double hue)
	{
		int imgWidth = (int) image.getWidth();
		int imgHeight = (int) image.getHeight();

		PixelReader reader = image.getPixelReader();

		WritableImage result = new WritableImage(imgWidth, imgHeight);

		PixelWriter pixelWriter = result.getPixelWriter();

		byte[] imageData = new byte[imgWidth * imgHeight * 4];

		PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteBgraPreInstance();

		int pixel = 0;

		for (int y = 0; y < imgHeight; y++)
		{
			for (int x = 0; x < imgWidth; x++)
			{
				Color color = reader.getColor(x, y);

				Color newColour = Color.hsb(hue, color.getSaturation(), color.getBrightness());

				byte alpha = (byte) (color.getOpacity() * 255);
				byte red = (byte) (newColour.getRed() * 255);
				byte green = (byte) (newColour.getGreen() * 255);
				byte blue = (byte) (newColour.getBlue() * 255);

				imageData[pixel++] = blue;
				imageData[pixel++] = green;
				imageData[pixel++] = red;
				imageData[pixel++] = alpha;
			}
		}

		pixelWriter.setPixels(0, 0, imgWidth, imgHeight, pixelFormat, imageData, 0, imgWidth * 4);

		return result;
	}

	public static Image makeContentricRings(double imgWidth, double imgHeight, int rings, Color color)
	{
		double diameterX = imgWidth;
		double diameterY = imgHeight;

		Color colourOff = Color.BLACK;

		Canvas canvas = new Canvas(imgWidth, imgHeight);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		for (int i = 0; i < rings; i++)
		{
			if (i % 2 == 0)
			{
				gc.setFill(color);
			}
			else
			{
				gc.setFill(colourOff);
			}

			gc.fillOval((imgWidth / 2) - (diameterX / 2), (imgHeight / 2) - (diameterY / 2), diameterX, diameterY);

			diameterX -= imgWidth / rings;
			diameterY -= imgHeight / rings;
		}

		Image snap = ImageUtil.createImageFromCanvas(gc.getCanvas(), imgWidth, imgHeight, true);

		snap = replaceColour(snap, colourOff, Color.TRANSPARENT);

		return snap;
	}

	public static Image makeCubes(double imgWidth, double imgHeight)
	{
		Canvas canvas = new Canvas(imgWidth * 8, imgWidth * 8);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		double gap = 8;

		double x0 = gap;
		double x1 = imgWidth / 2;
		double x2 = imgWidth - gap;

		double y0 = gap;
		double y1 = gap + (imgHeight - 2 * gap) * (1.0 / 4.0);
		double y2 = gap + (imgHeight - 2 * gap) * (2.0 / 4.0);
		double y3 = gap + (imgHeight - 2 * gap) * (3.0 / 4.0);
		double y4 = (imgHeight - gap) * (4.0 / 4.0);

		gc.setLineCap(StrokeLineCap.ROUND);

		gc.setFill(Color.rgb(200, 200, 200));
		gc.fillPolygon(new double[] {
				x0,
				x1,
				x2,
				x1 },
				new double[] {
						y1,
						y0,
						y1,
						y2 },
				4);

		gc.setFill(Color.rgb(100, 100, 100));
		gc.fillPolygon(new double[] {
				x0,
				x1,
				x1,
				x0 },
				new double[] {
						y1,
						y2,
						y4,
						y3 },
				4);

		gc.setFill(Color.rgb(150, 150, 150));
		gc.fillPolygon(new double[] {
				x1,
				x2,
				x2,
				x1 },
				new double[] {
						y2,
						y1,
						y3,
						y4 },
				4);

		gc.setStroke(Color.BLACK);
		gc.setLineWidth(4);
		gc.setLineJoin(StrokeLineJoin.ROUND);
		gc.strokePolygon(new double[] {
				x0,
				x1,
				x2,
				x1 },
				new double[] {
						y1,
						y0,
						y1,
						y2 },
				4);
		gc.strokePolygon(new double[] {
				x0,
				x1,
				x1,
				x0 },
				new double[] {
						y1,
						y2,
						y4,
						y3 },
				4);
		gc.strokePolygon(new double[] {
				x1,
				x2,
				x2,
				x1 },
				new double[] {
						y2,
						y1,
						y3,
						y4 },
				4);

		return ImageUtil.createImageFromCanvas(gc.getCanvas(), imgWidth, imgHeight, true);
	}

	public static Image makeHearts(double imgWidth, double imgHeight)
	{
		Canvas canvas = new Canvas(imgWidth * 8, imgWidth * 8);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		double heartSize = imgWidth / Math.sqrt(2) * 1.14;

		double halfWidth = heartSize / 2;
		double diameterX = Math.sqrt(halfWidth * halfWidth + halfWidth * halfWidth);
		double diameterY = diameterX;

		double offset = (imgWidth - heartSize) / 2;

		double x0 = offset;
		double x1 = x0 + heartSize / 2;
		double x2 = x0 + heartSize;

		double y0 = offset;
		double y1 = y0 + heartSize / 2;
		double y2 = y0 + heartSize;

		gc.setFill(Color.BLACK);
		gc.fillPolygon(new double[] {
				x0,
				x1,
				x2,
				x1 },
				new double[] {
						y1,
						y0,
						y1,
						y2 },
				4);
		gc.fillOval(offset + heartSize * .25 - diameterX / 2, offset + heartSize / 4 - diameterY / 2, diameterX, diameterY);
		gc.fillOval(offset + heartSize * .75 - diameterX / 2, offset + heartSize / 4 - diameterY / 2, diameterX, diameterY);

		heartSize = imgWidth / Math.sqrt(2) * 0.9;

		halfWidth = heartSize / 2;
		diameterX = Math.sqrt(halfWidth * halfWidth + halfWidth * halfWidth);
		diameterY = diameterX;

		offset = (imgWidth - heartSize) / 2;

		x0 = offset;
		x1 = x0 + heartSize / 2;
		x2 = x0 + heartSize;

		y0 = offset;
		y1 = y0 + heartSize / 2;
		y2 = y0 + heartSize;

		gc.setFill(Color.RED);
		gc.fillPolygon(new double[] {
				x0,
				x1,
				x2,
				x1 },
				new double[] {
						y1,
						y0,
						y1,
						y2 },
				4);
		gc.fillOval(offset + heartSize * .25 - diameterX / 2, offset + heartSize / 4 - diameterY / 2, diameterX, diameterY);
		gc.fillOval(offset + heartSize * .75 - diameterX / 2, offset + heartSize / 4 - diameterY / 2, diameterX, diameterY);

		return ImageUtil.createImageFromCanvas(gc.getCanvas(), imgWidth, imgHeight, true);
	}
}