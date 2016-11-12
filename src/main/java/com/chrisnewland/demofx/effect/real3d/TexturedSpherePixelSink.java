/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.real3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IPixelSink;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class TexturedSpherePixelSink extends TexturedSphere implements IPixelSink
{
	public TexturedSpherePixelSink(DemoConfig config, double radius, double rotateAngleX, double rotateAngleY, double offsetX, double offsetY )
	{
		super(config, new WritableImage((int)(4*radius), (int)(2*radius)), radius, rotateAngleX, rotateAngleY, offsetX, offsetY);
	}

	@Override
	public PixelWriter getPixelWriter()
	{
		return imageTexture.getPixelWriter();
	}

	@Override
	public void redraw()
	{
		super.renderForeground();
	}

	@Override
	public int getWidth()
	{
		return (int) (4 * radius);
	}

	@Override
	public int getHeight()
	{
		return (int) (2 * radius);
	}
}