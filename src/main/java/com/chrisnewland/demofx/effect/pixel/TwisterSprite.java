/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import java.nio.ByteBuffer;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class TwisterSprite extends AbstractEffect
{
	private byte[] pixelData;

	private int columnWidth;

	private double speed = 0;
	private double angle = 0;
	private double twist = 1;

	private int imageWidth;
	private int imageHeight;
	private int pixelWriterScanLine;
	private int textureScanLine;

	private int pixelCount;

	private WritableImage imageTexture;

	private PixelWriter pixelWriter;
	private PixelFormat<ByteBuffer> pixelFormat;

	private byte[] side0BGRA;
	private byte[] side1BGRA;
	private byte[] side2BGRA;
	private byte[] side3BGRA;

	public TwisterSprite(DemoConfig config)
	{
		super(config);
		
		int column = 100;
		
		int scaled = (int)Math.ceil((double)column * Math.sqrt(2.0));
		
		int scanLine =  scaled * 4;
		
		byte[] sideA = new byte[scanLine * intHeight];
		byte[] sideB = new byte[scanLine * intHeight];
		byte[] sideC = new byte[scanLine * intHeight];
		byte[] sideD = new byte[scanLine * intHeight];

		for (int y = 0; y < intHeight; y++)
		{
			int yBase = y * scanLine;

			for (int x = 0; x < scanLine; x += 4)
			{
				int pos = yBase + x;

				sideA[pos + 0] = (byte) precalc.getRandomColour255();
				sideA[pos + 1] = (byte) 0;
				sideA[pos + 2] = (byte) 0;
				sideA[pos + 3] = (byte) 0xff;

				sideB[pos + 0] = (byte) 0;
				sideB[pos + 1] = (byte) precalc.getRandomColour255();
				sideB[pos + 2] = (byte) 0;
				sideB[pos + 3] = (byte) 0xff;

				sideC[pos + 0] = (byte) 0;
				sideC[pos + 1] = (byte) 0;
				sideC[pos + 2] = (byte) precalc.getRandomColour255();
				sideC[pos + 3] = (byte) 0xff;

				sideD[pos + 0] = (byte) precalc.getRandomColour255();
				sideD[pos + 1] = (byte) precalc.getRandomColour255();
				sideD[pos + 2] = (byte) 0;
				sideD[pos + 3] = (byte) 0xff;
			}
		}
		
		init(column, scanLine, sideA, sideB, sideC, sideD);
	}
	
	public TwisterSprite(DemoConfig config, Image a, Image b, Image c, Image d)
	{
		super(config);
		
		byte[] bytesA = ImageUtil.readImageToByteArrayBGRA(a);
		byte[] bytesB = ImageUtil.readImageToByteArrayBGRA(b);
		byte[] bytesC = ImageUtil.readImageToByteArrayBGRA(c);
		byte[] bytesD = ImageUtil.readImageToByteArrayBGRA(d);
				
		int scanLine = (int)a.getWidth() * 4;
		
		int column = (int)((double)scanLine /(4.0 * Math.sqrt(2.0)));
		
		init(column, scanLine, bytesA, bytesB, bytesC, bytesD);
	}

	private void init(int sideWidth, int scanLine, byte[] sideA, byte[] sideB, byte[] sideC, byte[] sideD)
	{		
		this.columnWidth = sideWidth;

		this.imageWidth = columnWidth * 3;
		this.imageHeight = intHeight;

		imageTexture = new WritableImage(imageWidth, imageHeight);

		pixelWriter = imageTexture.getPixelWriter();

		pixelFormat = PixelFormat.getByteBgraPreInstance();

		pixelWriterScanLine = imageWidth * 4;

		pixelCount = pixelWriterScanLine * imageHeight;

		pixelData = new byte[pixelCount];
		
		textureScanLine = scanLine;
		
		side0BGRA = sideA;
		side1BGRA = sideB;
		side2BGRA = sideC;
		side3BGRA = sideD;
	}
	
	@Override
	public void renderForeground()
	{
		render(angle / 60);

		speed += 0.5;

		if (speed >= 360)
		{
			speed -= 360;
		}

		angle += 4 * precalc.sin(speed);

		if (angle >= 1080)
		{
			angle -= 1080;
		}

		twist = 16 + precalc.sin(angle) * 8;

		pixelWriter.setPixels(0, 0, imageWidth, imageHeight, pixelFormat, pixelData, 0, pixelWriterScanLine);

		gc.drawImage(imageTexture, 0, 0);
		gc.drawImage(imageTexture, width-imageWidth, 0);
	}

	private void render(double distort)
	{
		for (int y = 0; y < imageHeight; y++)
		{
			double xOffset = precalc.sin(angle + (y - halfHeight)) * distort * 2;

			int writePos = y * pixelWriterScanLine;

			double start = angle + (double) y / twist;

			int x1 = (int) (xOffset + imageWidth / 2 + precalc.sin(start) * columnWidth);
			int x2 = (int) (xOffset + imageWidth / 2 + precalc.sin(start + 90) * columnWidth);
			int x3 = (int) (xOffset + imageWidth / 2 + precalc.sin(start + 180) * columnWidth);
			int x4 = (int) (xOffset + imageWidth / 2 + precalc.sin(start + 270) * columnWidth);

			makeAlpha(writePos);

			if (x1 < x2)
			{
				renderLine(y, x1, x2, side0BGRA);
			}

			if (x2 < x3)
			{
				renderLine(y, x2, x3, side1BGRA);
			}

			if (x3 < x4)
			{
				renderLine(y, x3, x4, side2BGRA);
			}

			if (x4 < x1)
			{
				renderLine(y, x4, x1, side3BGRA);
			}
		}
	}

	private void makeAlpha(int writePos)
	{
		for (int x = 0; x < pixelWriterScanLine; x += 4)
		{
			pixelData[writePos + x + 0] = (byte) 0;
			pixelData[writePos + x + 1] = (byte) 0;
			pixelData[writePos + x + 2] = (byte) 0;
			pixelData[writePos + x + 3] = (byte) 0;
		}
	}

	private void renderLine(int y, int start, int end, byte[] lineDataBGRA)
	{
		final int textureStartPos = y * textureScanLine;

		final int pixelWriterStartPos = y * pixelWriterScanLine + start * 4;

		final int lineWidth = end - start;

		int readPos = 0;

		for (int x = 1; x < lineWidth - 1; x++)
		{
			int base = pixelWriterStartPos + x * 4;
			int baseData = textureStartPos + Math.min(readPos, textureScanLine - 4);
			
			pixelData[base + 0] = lineDataBGRA[baseData + 0];
			pixelData[base + 1] = lineDataBGRA[baseData + 1];
			pixelData[base + 2] = lineDataBGRA[baseData + 2];
			pixelData[base + 3] = lineDataBGRA[baseData + 3];

			readPos += 4;
		}

		pixelData[pixelWriterStartPos + 0] = (byte) 0xff;
		pixelData[pixelWriterStartPos + 1] = (byte) 0xff;
		pixelData[pixelWriterStartPos + 2] = (byte) 0xff;
		pixelData[pixelWriterStartPos + 3] = (byte) 0xff;

		pixelData[pixelWriterStartPos + lineWidth * 4 + 0] = (byte) 0xff;
		pixelData[pixelWriterStartPos + lineWidth * 4 + 1] = (byte) 0xff;
		pixelData[pixelWriterStartPos + lineWidth * 4 + 2] = (byte) 0xff;
		pixelData[pixelWriterStartPos + lineWidth * 4 + 3] = (byte) 0xff;
	}
}