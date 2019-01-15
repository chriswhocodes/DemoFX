/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import java.nio.IntBuffer;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DoomFire extends AbstractEffect
{
	private int fireWidth;
	private int fireHeight;

	private int[] palleteRefs;
	private int[] pixelData;

	private PixelWriter pixelWriter;
	private PixelFormat<IntBuffer> pixelFormat;

	private int[] pallete;

	private WritableImage imageFire;

	private Image imageLogo;

	public DoomFire(DemoConfig config)
	{
		super(config);

		init("/doomfx.png");
	}

	public DoomFire(DemoConfig config, String backgroundImage)
	{
		super(config);

		init(backgroundImage);
	}

	private void init(String filename)
	{
		imageLogo = ImageUtil.loadImageFromResources(filename);

		fireWidth = (int) width;
		fireHeight = Math.min(224, (int) height);

		imageFire = new WritableImage(fireWidth, fireHeight);

		pixelWriter = imageFire.getPixelWriter();

		pixelFormat = PixelFormat.getIntArgbPreInstance();

		int pixelCount = fireWidth * fireHeight;

		palleteRefs = new int[pixelCount];

		pixelData = new int[pixelCount];

		createPallete();

		intialisePalleteRefs();
	}

	private void createPallete()
	{
		int[] rawPalleteRGB = new int[] { 0x00, 0x00, 0x00, 0x1F, 0x07, 0x07, 0x2F, 0x0F, 0x07, 0x47, 0x0F, 0x07, 0x57, 0x17, 0x07,
				0x67, 0x1F, 0x07, 0x77, 0x1F, 0x07, 0x8F, 0x27, 0x07, 0x9F, 0x2F, 0x07, 0xAF, 0x3F, 0x07, 0xBF, 0x47, 0x07, 0xC7,
				0x47, 0x07, 0xDF, 0x4F, 0x07, 0xDF, 0x57, 0x07, 0xDF, 0x57, 0x07, 0xD7, 0x5F, 0x07, 0xD7, 0x5F, 0x07, 0xD7, 0x67,
				0x0F, 0xCF, 0x6F, 0x0F, 0xCF, 0x77, 0x0F, 0xCF, 0x7F, 0x0F, 0xCF, 0x87, 0x17, 0xC7, 0x87, 0x17, 0xC7, 0x8F, 0x17,
				0xC7, 0x97, 0x1F, 0xBF, 0x9F, 0x1F, 0xBF, 0x9F, 0x1F, 0xBF, 0xA7, 0x27, 0xBF, 0xA7, 0x27, 0xBF, 0xAF, 0x2F, 0xB7,
				0xAF, 0x2F, 0xB7, 0xB7, 0x2F, 0xB7, 0xB7, 0x37, 0xCF, 0xCF, 0x6F, 0xDF, 0xDF, 0x9F, 0xEF, 0xEF, 0xC7, 0xFF, 0xFF,
				0xFF };

		int palleteSize = rawPalleteRGB.length / 3;

		pallete = new int[palleteSize];

		for (int i = 0; i < palleteSize; i++)
		{
			int alpha = (i == 0) ? 0 : 255;

			int red = rawPalleteRGB[3 * i + 0];

			int green = rawPalleteRGB[3 * i + 1];

			int blue = rawPalleteRGB[3 * i + 2];

			int argb = (alpha << 24) + (red << 16) + (green << 8) + blue;

			pallete[i] = argb;
		}
	}

	private void intialisePalleteRefs()
	{
		int writeIndex = 0;

		for (int y = 0; y < fireHeight; y++)
		{
			for (int x = 0; x < fireWidth; x++)
			{
				if (y == fireHeight - 1)
				{
					palleteRefs[writeIndex++] = pallete.length - 1;
				}
				else
				{
					palleteRefs[writeIndex++] = 0;
				}
			}
		}
	}

	@Override public void renderForeground()
	{
		if (imageLogo != null)
		{
			gc.drawImage(imageLogo, (width - imageLogo.getWidth()) / 2, 16);
		}

		render();

		writeFireImage();

		gc.drawImage(imageFire, 0, height - fireHeight);
	}

	private void render()
	{
		for (int x = 0; x < fireWidth; x++)
		{
			for (int y = 1; y < fireHeight; y++)
			{
				spreadFire(y * fireWidth + x);
			}
		}
	}

	private void spreadFire(int src)
	{
		int rand = (int) Math.round(Math.random() * 3.0) & 3;

		int dst = src - rand + 1;

		palleteRefs[Math.max(0, dst - fireWidth)] = Math.max(0, palleteRefs[src] - (rand & 1));
	}

	private void writeFireImage()
	{
		int pos = 0;

		for (int y = 0; y < fireHeight; y++)
		{
			for (int x = 0; x < fireWidth; x++)
			{
				pixelData[pos] = pallete[palleteRefs[pos]];

				pos++;
			}
		}

		pixelWriter.setPixels(0, 0, fireWidth, fireHeight, pixelFormat, pixelData, 0, fireWidth);
	}
}