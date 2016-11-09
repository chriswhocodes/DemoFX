package com.chrisnewland.demofx.effect.real3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.IPixelSink;

import javafx.scene.image.PixelWriter;

public class TexturedCubePixelSink extends TexturedCube implements IPixelSink
{
	public TexturedCubePixelSink(DemoConfig config, double side)
	{		
		super(config, side, 1, 1);
	}

	@Override
	public void redraw()
	{
		super.renderForeground();
	}

	@Override
	public PixelWriter getPixelWriter()
	{
		return imageTexture.getPixelWriter();
	}
	
	@Override
	public int getWidth()
	{
		return (int)side;
	}

	@Override
	public int getHeight()
	{
		return (int)side;
	}
}