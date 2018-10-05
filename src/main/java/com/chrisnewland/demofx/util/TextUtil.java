/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chrisnewland.demofx.effect.text.TextLayers;

import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
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

	private static Map<Character, Image> charMap = new HashMap<>();

	private static final String LETTERS_PATH = "letters/";

	static
	{
		for (char c = 'A'; c <= 'Z'; c++)
		{
			charMap.put(Character.valueOf(c), ImageUtil.loadImageFromResources(LETTERS_PATH + c + ".png"));
		}

		for (char c = '0'; c <= '9'; c++)
		{
			charMap.put(Character.valueOf(c), ImageUtil.loadImageFromResources(LETTERS_PATH + c + ".png"));
		}

		charMap.put(Character.valueOf('!'), ImageUtil.loadImageFromResources(LETTERS_PATH + "_exclamation.png"));
		charMap.put(Character.valueOf('-'), ImageUtil.loadImageFromResources(LETTERS_PATH + "_hyphen.png"));
		charMap.put(Character.valueOf('?'), ImageUtil.loadImageFromResources(LETTERS_PATH + "_question.png"));
		charMap.put(Character.valueOf('/'), ImageUtil.loadImageFromResources(LETTERS_PATH + "_slash.png"));
		charMap.put(Character.valueOf('\''), ImageUtil.loadImageFromResources(LETTERS_PATH + "_apostrophe.png"));
		charMap.put(Character.valueOf('.'), ImageUtil.loadImageFromResources(LETTERS_PATH + "_fullstop.png"));
		charMap.put(Character.valueOf(','), ImageUtil.loadImageFromResources(LETTERS_PATH + "_comma.png"));
	}

	public static Image getSpriteCharacter(Character character)
	{
		return charMap.get(character);
	}

	public static double getKerningForChar(char current, char prev, double plotHeight)
	{
		return getKerningForChar(current, prev, plotHeight, false);
	}

	public static double getKerningForChar(char current, char prev, double plotHeight, boolean rotation)
	{
		double letterGap = plotHeight / 10;

		if (current != 0)
		{
			if (current == 'Y')
			{
				if (prev == 'R')
				{
					letterGap = plotHeight / 20;
				}
				else if (prev == 'K')
				{
					letterGap = 0;
				}
			}
			else if (current == 'T')
			{
				if (prev == 'A')
				{
					letterGap = 0;
				}
				else if (prev == 'O' || prev == 'L')
				{
					letterGap = plotHeight / 32;
				}
			}
			else if (current == 'O')
			{
				if (prev == 'L')
				{
					letterGap = plotHeight / 20;
				}
			}
			else if (current == 'A')
			{
				if (prev == 'W' || prev == 'V')
				{
					letterGap = -plotHeight / 10;
				}
			}
			else if (current == 'N' || current == 'M')
			{
				if (!rotation && (prev == 'N' || prev == 'I'))
				{
					letterGap = plotHeight / 6;
				}
			}
			else if (current == 'I')
			{
				if (prev == 'N')
				{
					letterGap = plotHeight / 6;
				}
			}
			else if (current == 'V')
			{
				if (prev == '0')
				{
					letterGap = plotHeight / 100;
				}
				else if (prev == 'A')
				{
					letterGap = -plotHeight / 10;
				}
			}
			else if (current == 'U')
			{
				if (prev == 'A')
				{
					letterGap = 0;
				}
			}
		}

		return letterGap;
	}

	public static BallGrid createBallGrid(String string, GraphicsContext gc)
	{
		return createBallGrid(string, gc, "Courier New", 64, 2);
	}

	public static BallGrid createBallGrid(String string, GraphicsContext gc, String fontName, double fontSize, int divisor)
	{
		List<BallGrid> grids = new ArrayList<>();

		Font font = new Font(fontName, fontSize);

		for (int i = 0; i < string.length(); i++)
		{
			String charAsString = Character.toString(string.charAt(i));

			Image stringImage = createImageFromString(font, charAsString);

			Image cropImage = cropImage(stringImage, 16, 16);

			BallGrid letterGrid = getGrid(cropImage, divisor);

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
		int argb = reader.getArgb(x, y);

		// int alpha = (argb & 0xff000000) >> 24;
		int red = (argb & 0x00ff0000) >> 16;
		int green = (argb & 0x0000ff00) >> 8;
		int blue = (argb & 0x000000ff) >> 0;

		int threshold = 32;

		boolean set = red >= threshold || green >= threshold || blue >= threshold;

		return set;
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
			Image image = createImageFromString(font, str);

			// for debugging
			// imgCopy = image;

			dimensions = new Point2D(measureWidth(image), measureHeight(image));

			stringDimensionCache.put(fontKey, dimensions);
		}

		return dimensions;
	}

	// for debugging
	// public static Image imgCopy;

	public static Image createSpriteImageText(String text, boolean transparent, double scale)
	{
		double width = Math.max(16, text.length() * 100 * scale);
		double height = 100 * scale;

		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		if (transparent)
		{
			gc.setFill(Color.TRANSPARENT);
		}
		else
		{
			gc.setFill(Color.BLACK);
		}

		gc.fillRect(0, 0, width, height);

		double canvasHeight = gc.getCanvas().getHeight();

		writeSpriteStringOnGC(gc, text, 0, canvasHeight, scale);

		Image image = ImageUtil.createImageFromCanvas(gc.getCanvas(), width, height, transparent);

		image = cropImage(image, 16, 16);

		return image;
	}

	public static void writeSpriteStringOnGC(GraphicsContext gc, String text, double left, double bottom, double fontScale)
	{
		char[] chars = text.toUpperCase().toCharArray();

		double x = left;

		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];

			if (c == ' ')
			{
				x += 40 * fontScale;
				continue;
			}

			Image charImage = getSpriteCharacter(c);

			if (charImage == null)
			{
				System.out.println("No sprite for char '" + c + "'");
				continue;
			}

			double charWidth = charImage.getWidth();
			double charHeight = charImage.getHeight();

			double plotWidth = charWidth * fontScale;
			double plotHeight = charHeight * fontScale;

			if (i > 0)
			{
				x += getKerningForChar(c, chars[i - 1], plotHeight);
			}

			double y = bottom - plotHeight;

			gc.drawImage(charImage, x, y, plotWidth, plotHeight);

			x += plotWidth;
		}
	}

	public static String getStringFromResource(String resourceName)
	{
		if (!resourceName.startsWith("/"))
		{
			resourceName = "/" + resourceName;
		}

		StringBuilder builder = new StringBuilder();

		InputStream inStream = TextLayers.class.getResourceAsStream(resourceName);

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream)))
		{
			String line = bufferedReader.readLine();

			while (line != null)
			{
				builder.append(line).append("\n");

				line = bufferedReader.readLine();
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

		return builder.toString();
	}

	public static Image createImageFromString(Font font, String str)
	{
		Canvas canvas = new Canvas(1280, 720);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		double canvasWidth = gc.getCanvas().getWidth();
		double canvasHeight = gc.getCanvas().getHeight();

		gc.setFill(Color.TRANSPARENT);
		gc.fillRect(0, 0, canvasWidth, canvasHeight);

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setTextBaseline(VPos.BOTTOM);

		gc.setFont(font);

		gc.setFill(Color.WHITE);
		gc.fillText(str, 0, canvasHeight);

		Image image = ImageUtil.createImageFromCanvas(gc.getCanvas(), canvasWidth, canvasHeight, true);

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

		// SpriteUtil.saveImage(image, new File("/tmp/output.png"));

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