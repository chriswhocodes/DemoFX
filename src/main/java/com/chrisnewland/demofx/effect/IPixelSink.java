/*
 * Copyright (c) 2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import javafx.scene.image.PixelWriter;

public interface IPixelSink
{
	int getWidth();
	
	int getHeight();
	
	PixelWriter getPixelWriter();
	
	void redraw();
}
