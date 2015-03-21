package com.chrisnewland.demofx.effect.text;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TextUtil
{
	private static Map<String, Double> widthCache = new HashMap<>();

	public static String[] createBallGrid(String string, GraphicsContext gc)
	{
		String[] result = null;

		Font font = new Font("Times New Roman", 64);

		Image stringImage = createImageFromString(font, gc, string);

		Image cropImage = cropImage(stringImage);

		result = getGrid(cropImage, 4);

		return result;
	}

	private static String[] getGrid(Image image, int square)
	{
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		int rows = height / square;
		int cols = width / square;

		String[] result = new String[rows];

		for (int row = 0; row < rows; row++)
		{
			StringBuilder builder = new StringBuilder();

			for (int col = 0; col < cols; col++)
			{
				if (isBall(image, col, row, square))
				{
					builder.append("*");
				}
				else
				{
					builder.append(" ");
				}
			}

			result[row] = builder.toString();
			builder.setLength(0);
		}

		return result;
	}

	private static boolean isBall(Image image, int col, int row, int square)
	{
		boolean result = false;

		PixelReader reader = image.getPixelReader();

		outer: for (int x = col * square; x < (col + 1) * square; x++)
		{
			for (int y = row * square; y < (row + 1) * square; y++)
			{
				int red = (reader.getArgb(x, y) & 0x00ff0000) >> 16;

				if (red > 0)
				{
					result = true;
					break outer;
				}
			}
		}

		return result;
	}

	public static double getStringWidthPixels(Font font, GraphicsContext gc, String str)
	{
		double width = 0;

		if (widthCache.containsKey(str))
		{
			width = widthCache.get(str);
		}
		else if (str.trim().length() == 0)
		{
			widthCache.put(str, font.getSize() / 2);
		}
		else
		{
			Image image = createImageFromString(font, gc, str);

			width = measureWidth(image);

			widthCache.put(str, width);
		}

		return width;
	}

	private static Image createImageFromString(Font font, GraphicsContext gc, String str)
	{
		Font oldFont = gc.getFont();

		double canvasWidth = gc.getCanvas().getWidth();
		double canvasHeight = gc.getCanvas().getHeight();

		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);

		gc.setFont(font);

		gc.setFill(Color.WHITE);
		gc.fillText(str, 0, canvasHeight-4);

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

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		// measure columns from right and stop when we get a non-black pixel

		outer: for (int col = width - 1; col >= 0; col--)
		{
			for (int row = 0; row < height; row++)
			{
				int red = (reader.getArgb(col, row) & 0x00ff0000) >> 16;

				if (red > 0)
				{
					result = col+1;
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

		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		// measure columns from right and stop when we get a non-black pixel

		outer: for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				int red = (reader.getArgb(col, row) & 0x00ff0000) >> 16;

				if (red > 0)
				{
					result = row+1;
					break outer;
				}
			}
		}

		return height - result;
	}

	/*
	 * Assumes part to crop is in bottom left
	 */
	public static Image cropImage(Image image)
	{
		int width = (int) measureWidth(image);
		int height = (int) measureHeight(image);

		int x = 0;
		int y = (int) image.getHeight() - height;

		WritableImage cropped = new WritableImage(image.getPixelReader(), x, y, width, height);

		return cropped;
	}
}