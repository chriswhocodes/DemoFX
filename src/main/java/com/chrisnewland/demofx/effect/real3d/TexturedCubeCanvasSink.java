/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.real3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.ICanvasSink;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;

public class TexturedCubeCanvasSink extends TexturedCube implements ICanvasSink
{
	private Canvas gcCanvas;

	private SnapshotParameters params;
	
	public TexturedCubeCanvasSink(DemoConfig config, double side)
	{
		super(config, side, 1, 1);
	}

	@Override
	public void setCanvas(Canvas canvas)
	{
		this.gcCanvas = canvas;
		
		params = new SnapshotParameters();
	}
	
	@Override
	public void renderForeground()
	{
		super.renderForeground();
		
		gcCanvas.snapshot(params, imageTexture);
	}
}