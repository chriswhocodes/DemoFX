package com.chrisnewland.demofx.effect.text;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TextUtil
{
	private static Map<String, Double> widthCache = new HashMap<>();

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
			Font oldFont = gc.getFont();

			double canvasWidth = gc.getCanvas().getWidth();
			double canvasHeight = gc.getCanvas().getHeight();

			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, canvasWidth, canvasHeight);

			gc.setFont(font);

			gc.setFill(Color.WHITE);
			gc.fillText(str, 0, canvasHeight);

			WritableImage image = new WritableImage((int) canvasWidth, (int) canvasHeight);

			gc.getCanvas().snapshot(new SnapshotParameters(), image);

			width = measureWidth(image);

			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, canvasWidth, canvasHeight);

			gc.setFont(oldFont);

			widthCache.put(str, width);
		}

		return width;
	}

	public static double measureWidth(WritableImage image)
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
					result = col;
					break outer;
				}
			}
		}

		return result;
	}
}