/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.util.PreCalc;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public abstract class AbstractEffect implements IEffect
{
	protected GraphicsContext gc;
	protected Group group;

	protected int itemCount;

	protected double width;
	protected double height;

	protected int intWidth;
	protected int intHeight;

	protected double halfWidth;
	protected double halfHeight;

	protected double canvasRotationAngle = 0;

	protected PreCalc precalc;
	protected DemoConfig config;

	private int colourCycleAngle = 0;
	private static ColourCycleCache COLOUR_CYCLE = null;

	protected long effectStartMillis = -1;
	protected long effectStopMillis = -1;

	protected boolean effectStarted = false;
	protected boolean effectFinished = false;

	protected boolean renderOffScreen = false;

	private Rotate rotate;

	public AbstractEffect(DemoConfig config)
	{
		this.config = config;
		this.gc = config.getOnScreenCanvasGC();
		this.group = config.getGroupNode();

		precalc = config.getPreCalc();

		this.itemCount = config.getCount();

		this.width = config.getWidth();
		this.height = config.getHeight();

		this.intWidth = (int) width;
		this.intHeight = (int) height;

		this.halfWidth = width / 2;
		this.halfHeight = height / 2;

		rotate = new Rotate();

		if (config.isOffScreen())
		{
			setupOffScreen(config.getOffScreenWidth(), config.getOffScreenHeight());
		}
		if (COLOUR_CYCLE == null)
		{
			COLOUR_CYCLE = new ColourCycleCache(precalc);
		}
	}

	private void setupOffScreen(double newWidth, double newHeight)
	{
		renderOffScreen = true;

		gc = config.getOffScreenCanvasGC();

		width = newWidth;
		height = newHeight;

		this.intWidth = (int) width;
		this.intHeight = (int) height;

		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
	}

	protected final void fillBackground(int red, int green, int blue)
	{
		fillBackground(Color.rgb(red, green, blue));
	}

	protected final void fillBackground(Color colour)
	{
		gc.setFill(colour);

		gc.fillRect(0, 0, width, height);
	}

	protected final Color getCycleColour()
	{
		// range [0, 360[
		colourCycleAngle++;

		if (colourCycleAngle >= 360)
		{
			colourCycleAngle -= 360;
		}
		return COLOUR_CYCLE.colors[colourCycleAngle];
	}

	static Color generateColour(PreCalc precalc, int colourCycleAngle)
	{
		double redFraction = 2 + precalc.sin(colourCycleAngle);
		double greenFraction = 2 + precalc.sin(360 - colourCycleAngle);
		double blueFraction = 2 + precalc.cos(colourCycleAngle);

		int red = (int) (redFraction * 32);
		int green = (int) (greenFraction * 32);
		int blue = (int) (blueFraction * 32);

		return Color.rgb(red, green, blue);
	}

	protected final Color getRandomColour(int start, int end)
	{
		int range = end - start;

		int red = start + (int) (range * precalc.getUnsignedRandom());
		int green = start + (int) (range * precalc.getUnsignedRandom());
		int blue = start + (int) (range * precalc.getUnsignedRandom());

		return Color.rgb(red, green, blue);
	}

	protected final Color getRandomColour()
	{

		int red = (int) (255.0 * precalc.getUnsignedRandom());
		int green = (int) (255.0 * precalc.getUnsignedRandom());
		int blue = (int) (255.0 * precalc.getUnsignedRandom());

		return Color.rgb(red, green, blue);
	}

	protected final Color getRandomGreen(int start, int end)
	{
		int range = end - start;

		int green = start + (int) (range * precalc.getUnsignedRandom());

		return Color.rgb(0, green, 0);
	}

	protected final void rotateCanvasAroundCentre(double rotation)
	{
		rotateCanvasAroundPoint(halfWidth, halfHeight, canvasRotationAngle);

		canvasRotationAngle += rotation;

		if (canvasRotationAngle >= 360)
		{
			canvasRotationAngle -= 360;
		}
	}

	protected final void rotateCanvasAroundOrigin(double rotation)
	{
		rotateCanvasAroundPoint(0, 0, rotation);

		canvasRotationAngle += rotation;

		if (canvasRotationAngle >= 360)
		{
			canvasRotationAngle -= 360;
		}
	}

	protected final void rotateCanvasAroundPoint(double x, double y, double rotation)
	{
		rotate.setAngle(rotation);
		rotate.setPivotX(x);
		rotate.setPivotY(y);

		gc.setTransform(rotate.getMxx(), rotate.getMyx(), rotate.getMxy(), rotate.getMyy(), rotate.getTx(), rotate.getTy());
	}

	@Override public void stop()
	{
	}

	@Override public void start()
	{
	}

	@Override public void setStartOffsetMillis(long start)
	{
		this.effectStartMillis = start;
	}

	@Override public void setStopOffsetMillis(long stop)
	{
		this.effectStopMillis = stop;
	}

	@Override public long getStartOffsetMillis()
	{
		return effectStartMillis;
	}

	@Override public long getStopOffsetMillis()
	{
		return effectStopMillis;
	}

	@Override public boolean isVisible(long elapsed)
	{
		if (effectFinished)
		{
			return false;
		}

		boolean showEffect = true;

		if (effectStartMillis != -1)
		{
			if (elapsed < effectStartMillis)
			{
				showEffect = false;
			}
			else if (!effectStarted)
			{
				effectStarted = true;
				System.out.println("started " + getClass().getSimpleName() + " at " + elapsed);
				start();
			}
		}
		else if (!effectStarted)
		{
			effectStarted = true;
			System.out.println("started " + getClass().getSimpleName() + " at " + elapsed);
			start();
		}

		if (effectStopMillis != -1)
		{
			if (elapsed > effectStopMillis)
			{
				showEffect = false;
				effectFinished = true;
				System.out.println("stopped " + getClass().getSimpleName() + " at " + elapsed);
				stop();
			}
		}

		return showEffect;
	}

	protected void writeImageToIntArray(Image image, int[] dest, int imageWidth, int imageHeight)
	{
		int pixel = 0;

		PixelReader reader = image.getPixelReader();

		for (int y = 0; y < imageHeight; y++)
		{
			for (int x = 0; x < imageWidth; x++)
			{
				dest[pixel++] = reader.getArgb(x, y);
			}
		}
	}

	final class ColourCycleCache
	{
		final Color[] colors = new Color[360];

		ColourCycleCache(PreCalc precalc)
		{
			for (int i = 0; i < 360; i++)
			{
				colors[i] = generateColour(precalc, i);
			}
		}
	}
}