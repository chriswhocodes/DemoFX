package com.chrisnewland.demofx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class TextUtil
{
	private static Map<String, Point2D> stringDimensionCache = new HashMap<>();

	public static BallGrid createBallGridList(String string, GraphicsContext gc)
	{
		List<BallGrid> grids = new ArrayList<>();

		Font font = new Font("Courier New", 64);

		for (int i = 0; i < string.length(); i++)
		{
			String charAsString = Character.toString(string.charAt(i));

			Image stringImage = createImageFromString(font, gc, charAsString);

			Image cropImage = cropImage(stringImage, 16, 16);

			BallGrid letterGrid = getGrid(cropImage, 2);

			grids.add(letterGrid);
		}

		BallGrid result = BallGrid.concatenate(grids);

		return result;
	}

	private static BallGrid getGrid(Image image, int square)
	{
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		int rows = height / square;
		int cols = width / square;

		BallGrid result = new BallGrid(cols, rows);

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				if (isBall(image, col, row, square))
				{
					result.setBall(col, row);
				}
			}
		}

		return result;
	}

	private static boolean isPixelSet(PixelReader reader, int x, int y)
	{
		int red = (reader.getArgb(x, y) & 0x00ff0000) >> 16;

		return red > 0;
	}

	private static boolean isBall(Image image, int col, int row, int square)
	{
		boolean result = false;

		PixelReader reader = image.getPixelReader();

		outer: for (int x = col * square; x < (col + 1) * square; x++)
		{
			for (int y = row * square; y < (row + 1) * square; y++)
			{
				if (isPixelSet(reader, x, y))
				{
					result = true;
					break outer;
				}
			}
		}

		return result;
	}

	public static Point2D getStringDimensions(Font font, GraphicsContext gc, String str)
	{
		Point2D dimensions = Point2D.ZERO;

		String fontKey = font.toString() + str;
		
		if (stringDimensionCache.containsKey(fontKey))
		{
			dimensions = stringDimensionCache.get(fontKey);
		}
		else if (str.trim().length() == 0)
		{
			dimensions = new Point2D(font.getSize() / 2, font.getSize());
			
			stringDimensionCache.put(fontKey, dimensions);
		}
		else
		{
			Image image = createImageFromString(font, gc, str);

			dimensions = new Point2D(measureWidth(image), measureHeight(image));

			stringDimensionCache.put(fontKey, dimensions);
		}

		return dimensions;
	}

	private static Image createImageFromString(Font font, GraphicsContext gc, String str)
	{
		Font oldFont = gc.getFont();

		double canvasWidth = gc.getCanvas().getWidth();
		double canvasHeight = gc.getCanvas().getHeight();

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);

		gc.setFont(font);

		gc.setFill(Color.WHITE);
		gc.fillText(str, 0, canvasHeight);

		WritableImage image = new WritableImage((int) canvasWidth, (int) canvasHeight);

		gc.getCanvas().snapshot(new SnapshotParameters(), image);

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);

		gc.setFont(oldFont);

		return image;
	}

	public static double measureWidth(Image image)
	{
		double result = 0;

		PixelReader reader = image.getPixelReader();

		int imgWidth = (int) image.getWidth();
		int imgHeight = (int) image.getHeight();

		// measure columns from right and stop when we get a non-black pixel

		outer: for (int col = imgWidth - 1; col >= 0; col--)
		{
			for (int row = 0; row < imgHeight; row++)
			{
				if (isPixelSet(reader, col, row))
				{
					result = col + 1;
					break outer;
				}
			}
		}

		return result;
	}

	public static double measureHeight(Image image)
	{
		double result = 0;

		PixelReader reader = image.getPixelReader();

		int imgWidth = (int) image.getWidth();
		int imgHeight = (int) image.getHeight();

		// measure rows from top and stop when we get a non-black pixel

		outer: for (int row = 0; row < imgHeight; row++)
		{
			result = row;

			for (int col = 0; col < imgWidth; col++)
			{
				if (isPixelSet(reader, col, row))
				{
					break outer;
				}
			}
		}

		return imgHeight - result;
	}

	/*
	 * Assumes part to crop is in bottom left
	 */
	public static Image cropImage(Image image, int minWidth, int minHeight)
	{
		int imgWidth = (int) Math.max(minWidth, measureWidth(image));
		int imgHeight = (int) Math.max(minHeight, measureHeight(image));

		int x = 0;
		int y = (int) image.getHeight() - imgHeight;

		WritableImage cropped = new WritableImage(image.getPixelReader(), x, y, imgWidth, imgHeight);

		return cropped;
	}
}