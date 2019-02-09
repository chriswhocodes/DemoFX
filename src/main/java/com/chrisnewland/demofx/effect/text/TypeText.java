/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;
import com.chrisnewland.demofx.util.TextUtil;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.*;

public class TypeText extends AbstractEffect
{
	private List<String> stringList;

	private Font font;

	private static final double INITIAL_FONT_SIZE = 20;

	private long showMillis;

	private long time;

	private int stringIndex = 0;

	private boolean loopStringList = true;

	private double textYPos;

	private long keystrokeIntervalMillis = 200;

	private Image key = new Image(getClass().getResourceAsStream("/key.png"));

	private Map<Character, Image> keyImages = new HashMap<>();

	public TypeText(DemoConfig config)
	{
		super(config);

		font = Font.font("monospace", FontWeight.NORMAL, INITIAL_FONT_SIZE);

		init("The end is the beginning is the end".toUpperCase().split(" "), true, font, 50);
	}

	public TypeText(DemoConfig config, String string, String split, boolean loopStringList, double yPercent, double fontSize)
	{
		super(config);

		font = Font.font("monospace", FontWeight.BOLD, fontSize);

		init(string.toUpperCase().split(split), loopStringList, font, yPercent);
	}

	private void init(String[] strings, boolean loopStringList, Font font, double yPercent)
	{
		stringList = new ArrayList<>();

		stringList.addAll(Arrays.asList(strings));

		this.loopStringList = loopStringList;

		this.font = font;

		this.textYPos = yPercent * height / 100;

		for (String str : strings)
		{
			for (int i = 0; i < str.length(); i++)
			{
				char ch = str.charAt(i);

				if (!keyImages.containsKey(ch) && ch != ' ')
				{
					keyImages.put(ch, makeKey(ch));
				}
			}
		}
	}

	@Override public void start()
	{
		time = System.currentTimeMillis();
	}

	private Image makeKey(char ch)
	{
		Canvas canvas = new Canvas(key.getWidth(), key.getHeight());

		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setFill(Color.TRANSPARENT);

		gc.fillRect(0, 0, key.getWidth(), key.getHeight());

		gc.drawImage(key, 0, 0);

		gc.setFont(font);

		gc.setTextAlign(TextAlignment.LEFT);

		gc.setTextBaseline(VPos.BOTTOM);

		Point2D charDimensions = TextUtil.getStringDimensions(font, gc, Character.toString(ch));

		gc.setFill(Color.rgb(64, 64, 64));

		double charX = (key.getWidth() - charDimensions.getX()) / 2 - 1;

		double charY = key.getHeight() - (key.getHeight() - charDimensions.getY()) / 2;

		gc.fillText(Character.toString(ch), charX, charY);

		return ImageUtil.createImageFromCanvas(gc.getCanvas(), key.getWidth(), key.getHeight(), true);
	}

	@Override public void renderForeground()
	{
		long now = System.currentTimeMillis();

		long elapsed = now - time; // 0 .. showMillis

		if (!effectFinished)
		{
			plotText(elapsed);
		}

		if (elapsed > showMillis)
		{
			stringIndex++;

			if (stringIndex == stringList.size())
			{
				if (loopStringList)
				{
					stringIndex = 0;
				}
				else
				{
					effectFinished = true;
				}
			}

			time = now;
		}
	}

	private final void plotText(long elapsed)
	{
		String str = stringList.get(stringIndex);

		int length = str.length();

		showMillis = length * keystrokeIntervalMillis + 2000;

		double gap = 4;

		double x = halfWidth - length * (key.getWidth() + gap) / 2;

		int keysToPlot = (int) Math.min(length, elapsed / keystrokeIntervalMillis);

		for (int i = 0; i < keysToPlot; i++)
		{
			char ch = str.charAt(i);

			Image keyImage = keyImages.get(ch);

			if (keyImage != null)
			{
				gc.drawImage(keyImage, x, textYPos);
			}

			x += key.getWidth() + gap;
		}
	}
}