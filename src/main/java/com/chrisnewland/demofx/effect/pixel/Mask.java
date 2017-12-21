/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.pixel;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;
import com.chrisnewland.demofx.util.MaskingSystem;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;

public class Mask extends AbstractEffect
{
	private PixelWriter pixelWriter;

	private int layerWidth;
	private int layerHeight;

	private int maskWidth;
	private int maskHeight;

	private int maskX = 100;
	private int maskY = 50;

	private int dX = 8;
	private int dY = 8;

	private int xPos = 0;
	private int yPos = 0;
	
	private int diameter = 192;

	private MaskingSystem maskingSystem;

	public Mask(DemoConfig config)
	{
		super(config);
		
		Image image0 = ImageUtil.loadImageFromResources("face.jpeg");
		Image image1 = ImageUtil.loadImageFromResources("skull.jpeg");

		layerWidth = (int) image0.getWidth();
		layerHeight = (int) image0.getHeight();

		int[] layer0 = ImageUtil.readImageToIntArray(image0);
		int[] layer1 = ImageUtil.readImageToIntArray(image1);
		
		maskWidth = diameter;
		maskHeight = diameter;
		
		int[] mask = createCircularMask(diameter);
		
		init(layer0, layer1, mask, layerWidth, layerHeight, maskWidth, maskHeight);
	}
	
	public Mask(DemoConfig config, int[] layer0, int[] layer1, int[] mask, int layerWidth, int layerHeight, int maskWidth, int maskHeight)
	{
		super(config);
		
		init(layer0, layer1, mask, layerWidth, layerHeight, maskWidth, maskHeight);
	}
	
	private void init(int[] layer0, int[] layer1, int[] mask, int layerWidth, int layerHeight, int maskWidth, int maskHeight)
	{
		xPos = (intWidth - layerWidth) / 2;
		yPos = (intHeight- layerHeight) / 2;
		
		maskingSystem = new MaskingSystem(layer0, layer1, mask, layerWidth, layerHeight, maskWidth, maskHeight);

		pixelWriter = gc.getPixelWriter();
	}
	
	private int[] createCircularMask(int diameter)
	{
		int pixelCountM = maskWidth * maskHeight;

		int[] result = new int[pixelCountM];

		Image maskImage = MaskingSystem.createMaskCircle(diameter);

		PixelReader reader = maskImage.getPixelReader();

		int writeIndex = 0;

		for (int y = 0; y < maskHeight; y++)
		{
			for (int x = 0; x < maskWidth; x++)
			{
				int argbPixel = reader.getArgb(x, y);

				int red = argbPixel & 0x00ff0000;

				int maskPixel = red > 0 ? 1 : 0;

				result[writeIndex++] = maskPixel;
			}
		}
		
		return result;
	}

	@Override
	public void renderForeground()
	{
		moveMask();
	
		maskingSystem.writeCombinedPixels(pixelWriter, xPos, yPos, maskX, maskY);
	}

	private void moveMask()
	{
		maskX += dX;

		if (maskX < 0 || maskX + maskWidth >= layerWidth)
		{
			dX = -dX;
			maskX += dX;
		}

		maskY += dY;

		if (maskY < 0 || maskY + maskHeight >= layerHeight)
		{
			dY = -dY;
			maskY += dY;
		}
	}
}