/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class Sheet extends AbstractEffect
{
	private double[] pointX;
	private double[] pointY;
	private double[] pointZ;

	private int square;
	private double stretch;
	private double baseDepth;
	private double angleInc;

	private double waveDepth;

	private int rows;
	private int cols;

	private double angle = 0;

	private Color[] imageData;

	public enum SheetMode
	{
		FLAG, SHEET, SPRITE
	}

	private SheetMode mode = SheetMode.FLAG;

	public Sheet(DemoConfig config)
	{
		super(config);

		init();
	}

	public Sheet(DemoConfig config, SheetMode mode, String filename)
	{
		super(config);

		this.mode = mode;

		switch (mode)
		{
		case SPRITE:
		{
			Image loaded = ImageUtil.loadImageFromResources(filename);

			int imageWidth = (int) loaded.getWidth();
			int imageHeight = (int) loaded.getHeight();

			rows = imageWidth;
			cols = imageHeight;

			int pixelCount = imageWidth * imageHeight;

			PixelReader reader = loaded.getPixelReader();

			int pixel = 0;

			imageData = new Color[pixelCount];

			for (int y = 0; y < imageHeight; y++)
			{
				for (int x = 0; x < imageWidth; x++)
				{
					imageData[pixel++] = reader.getColor(x, y);
				}
			}

			square = 16;
			stretch = 1;
			baseDepth = 1.8;
			angleInc = 4;
			waveDepth = 8;
		}
			break;

		case FLAG:
		{
			square = 8;
			stretch = 0.5;
			baseDepth = 1.8;
			angleInc = 3;
			waveDepth = 0.6;
		}
			break;
		case SHEET:
		{
			square = 8;
			stretch = 0.5;
			baseDepth = 1.2;
			angleInc = 4;
			waveDepth = 4.6;
		}
			break;
		}

		init();
	}

	private void init()
	{
		if (mode != SheetMode.SPRITE)
		{
			rows = intHeight / square;
			cols = intWidth / square;
		}

		itemCount = rows * cols;

		buildSheet();
	}

	private void buildSheet()
	{
		pointX = new double[itemCount];
		pointY = new double[itemCount];
		pointZ = new double[itemCount];

		int pos = 0;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				pointX[pos] = -cols * square / 2 + col * square;
				pointY[pos] = -rows * square / 2 + row * square;
				pointZ[pos] = baseDepth;

				pos++;
			}
		}
	}

	@Override
	public void renderForeground()
	{
		gc.setStroke(Color.WHITE);

		angle += angleInc;

		if (angle >= 360)
		{
			angle -= 360;
		}

		switch (mode)
		{
		case SPRITE:
			renderSprite();
			break;
		case SHEET:
			renderSheet();
			break;
		case FLAG:
			renderFlag();
			break;
		}
	}

	private void renderSheet()
	{
		int pos = 0;

		int deformPos = (rows / 2 * cols) + cols / 2;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				fishEye(pos, deformPos);
				pos++;

			}
		}

		pos = 0;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				plotGridSheet(pos, col);
				pos++;
			}
		}
	}

	private void renderFlag()
	{
		int pos = 0;

		int deformPos = (rows / 2 * cols) + cols / 2;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				waveBoth(pos, deformPos);
				pos++;
			}
		}

		pos = 0;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				plotGridSheet(pos, col);
				pos++;
			}
		}
	}

	private void renderSprite()
	{
		int pos = 0;

		int deformPos = (rows / 2 * cols) + cols / 2;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				fishEye(pos, deformPos);
				pos++;
			}
		}

		pos = 0;

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				plotSpriteSheet(pos, col);
				pos++;
			}
		}
	}

	private final void fishEye(int pos, int deformPos)
	{
		double x1 = pointX[pos];
		double y1 = pointY[pos];

		double x2 = pointX[deformPos];
		double y2 = pointY[deformPos];

		double dx = Math.abs(x1 - x2);
		double dy = Math.abs(y1 - y2);

		double dist = Math.sqrt(dx * dx + dy * dy);

		dist = 1 - dist / halfWidth;

		pointZ[pos] = baseDepth + (dist * stretch * precalc.sin(angle));
	}

	private final void waveVertical(int pos, int deformPos)
	{
		double y = pointY[pos];

		double dist = waveDepth * precalc.sin(y + angle);

		pointZ[pos] = baseDepth + dist;
	}

	private final void waveHorizontal(int pos, int deformPos)
	{
		double x = pointX[pos];

		double dist = waveDepth * precalc.sin(x + angle);

		pointZ[pos] = baseDepth + dist;
	}

	private final void waveBoth(int pos, int deformPos)
	{
		double x = pointX[pos];
		double y = pointY[pos];

		double dist = waveDepth * precalc.sin(x + y + angle);

		pointZ[pos] = baseDepth + dist;
	}

	private double translateX(int i)
	{
		return pointX[i] / pointZ[i];
	}

	private double translateY(int i)
	{
		return pointY[i] / pointZ[i];
	}

	private final void plotGridSheet(int i, int col)
	{
		double x1 = halfWidth + translateX(i);
		double y1 = halfHeight + translateY(i);

		double depth = pointZ[i];

		depth -= stretch;

		depth /= baseDepth; // 0..1

		depth = 1 - depth;

		Color colour = Color.gray(precalc.clampDouble(depth, 0.3, 1));

		gc.setStroke(colour);

		if (col != cols - 1)
		{
			int right = i + 1;

			if (right < itemCount)
			{
				double x2 = halfWidth + translateX(right);
				double y2 = halfHeight + translateY(right);

				gc.strokeLine(x1, y1, x2, y2);
			}
		}

		int down = i + cols;

		if (down < itemCount)
		{
			double x3 = halfWidth + translateX(down);
			double y3 = halfHeight + translateY(down);

			gc.strokeLine(x1, y1, x3, y3);
		}
	}

	private final void plotSpriteSheet(int tl, int col)
	{
		Color colour = imageData[tl];

		if (Color.TRANSPARENT.equals(colour))
		{
			return;
		}

		if (col != cols - 1)
		{
			int tr = tl + 1;

			if (tr < itemCount)
			{
				int bl = tl + cols;

				if (bl < itemCount)
				{
					int br = tr + cols;

					double x1 = halfWidth + translateX(tl);
					double y1 = halfHeight + translateY(tl);

					double x2 = halfWidth + translateX(tr);
					double y2 = halfHeight + translateY(tr);

					double x3 = halfWidth + translateX(br);
					double y3 = halfHeight + translateY(br);

					double x4 = halfWidth + translateX(bl);
					double y4 = halfHeight + translateY(bl);

					double[] x = new double[] {
							(int) x1 - 1,
							(int) x2 + 1,
							(int) x3 + 1,
							(int) x4 - 1 };
					double[] y = new double[] {
							(int) y1 - 1,
							(int) y2 - 1,
							(int) y3 + 1,
							(int) y4 + 1 };

					gc.setFill(colour);

					gc.fillPolygon(x, y, 4);
				}
			}
		}
	}
}