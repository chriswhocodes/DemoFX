/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.background;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

import javafx.scene.image.Image;

public class ImageBackground extends AbstractEffect
{
	private Image image;

	public ImageBackground(DemoConfig config)
	{
		super(config);

		init("/javafx.png");
	}

	public ImageBackground(DemoConfig config, String filename)
	{
		super(config);

		init(filename);
	}

	private void init(String imageName)
	{
		image = new Image(getClass().getResourceAsStream(imageName));

	}

	@Override
	public void renderForeground()
	{
		gc.drawImage(image, 0, 0, width, height);
	}
}
