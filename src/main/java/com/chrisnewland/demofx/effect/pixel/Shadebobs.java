/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import java.nio.ByteBuffer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Shadebobs extends AbstractEffect
{
	private WritableImage image;
	private PixelWriter pixelWriter;
	private byte[] imageData;
	private PixelFormat<ByteBuffer> pixelFormat;
	private boolean running = true;

	public Shadebobs(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		itemCount = 1;
		itemName = "Image";
		image = new WritableImage(width, height);
		pixelWriter = gc.getPixelWriter();

		imageData = new byte[width * height * 4];

		pixelFormat = PixelFormat.getByteBgraPreInstance();

		gc.drawImage(image, 0, 0);

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (running)
				{
					int pixel = 0;

					for (int y = 0; y < height; y++)
					{
						for (int x = 0; x < width; x++)
						{
							byte blue = (byte) (precalc.getUnsignedRandom() * 255);
							byte green = (byte) (precalc.getUnsignedRandom() * 255);
							byte red = (byte) (precalc.getUnsignedRandom() * 255);
							byte alpha = 0;

							imageData[pixel++] = (byte)255;
							imageData[pixel++] = green;
							imageData[pixel++] = red;
							imageData[pixel++] = alpha;
						}
					}

					try
					{
						Thread.sleep(5); // TODO - native leak without this?
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void render()
	{
		pixelWriter.setPixels(0, 0, width, height, pixelFormat, imageData, 0, width * 4);
		gc.drawImage(image, 0, 0);
	}

	@Override
	public void stop()
	{
		running = false;
	}
}