/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.util.HashMap;
import java.util.Map;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.image.Image;

public class CreditsSprite extends AbstractEffect
{
	private double yOffset;

	private String[] stringList;
	private Map<String, Image> imageMap = new HashMap<>();

	private static final double OFFSCREEN = 100;

	private double scale = 0.5;
	private double speed = 3;

	public CreditsSprite(DemoConfig config)
	{
		super(config);

		yOffset = height;

		stringList = new String[] {
				"You have been watching",
				"DemoFX Part III",
				"JavaFX Demoscene Benchmarking Harness" };
	}

	public CreditsSprite(DemoConfig config, String filename, double scale, double speed)
	{
		super(config);

		this.stringList = TextUtil.getStringFromResource(filename).split("\n");
		
		init(scale, speed);
	}
	
	private void init(double scale, double speed)
	{
		this.scale = scale;
		this.speed = speed;
		
		createImages();
	}

	private void createImages()
	{
		for (int index = 0; index < stringList.length; index++)
		{
			String line = stringList[index];
			
			Image lineImage = TextUtil.createSpriteImageText(line, true, scale);
			
			imageMap.put(line, lineImage);
		}
	}
	
	@Override
	public void renderForeground()
	{	
		yOffset -= speed;

		double yPos = yOffset + height;
		
		for (int index = 0; index < stringList.length; index++)
		{
			String line = stringList[index];
			
			double lastHeight = plotText(line, yPos);
					
			yPos += lastHeight * 1.2;
		}
	}

	private final double plotText(String line, double yPos)
	{
		Image lineImage = imageMap.get(line);

		if (isLineOnScreen(yPos))
		{
			double x = (width - lineImage.getWidth()) / 2;

			double alpha = (halfHeight - Math.abs(halfHeight - yPos)) / halfHeight;
			
			gc.setGlobalAlpha(alpha);

			gc.drawImage(lineImage, x, yPos);
		}
		
		return lineImage.getHeight();		
	}

	private final boolean isLineOnScreen(double yPos)
	{
		return (yPos > -OFFSCREEN) && (yPos < height + OFFSCREEN);
	}
}