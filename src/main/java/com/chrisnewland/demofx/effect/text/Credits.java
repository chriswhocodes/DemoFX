/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Credits extends AbstractEffect
{
	private double yOffset;

	private List<String> stringList;

	private static final double INITIAL_FONT_SIZE = 24;
	private static final double OFFSCREEN = 100;
	private static final double INITIAL_LINE_SPACE = 10;

	private double speed = 3;

	private boolean loopStringList = false;

	private Color fontColour = Color.WHITE;

	private Font currentFont;
	private double currentSpacing = INITIAL_LINE_SPACE;

	public Credits(DemoConfig config)
	{
		super(config);

		currentFont = Font.font("Arial", INITIAL_FONT_SIZE);
		yOffset = height;

		stringList = new ArrayList<>();

		String commandH1 = "{family:Arial;size:96;spacing:40;}";
		String commandH2 = "{family:Arial;size:48;spacing:30;}";
		String commandBody = "{family:Apple Chancery;size:32;spacing:20;}";
		
		stringList.add(commandH2);
		stringList.add("You have been watching");
		
		stringList.add(commandH1);
		stringList.add("DemoFX Part II");
		
		stringList.add(commandH2);
		stringList.add("JavaFX Demoscene Benchmarking Harness");
		
		stringList.add(commandBody);
		stringList.add("A layered effect demoscene engine for testing");
		stringList.add("the performance of javafx.scene.canvas.Canvas");
		stringList.add("~~~");
		
		stringList.add(commandH2);
		stringList.add("Coding");

		stringList.add(commandBody);
		stringList.add("Chris Newland (@chriswhocodes)");
		stringList.add("~~~");

		stringList.add(commandH2);
		stringList.add("Music");
		
		stringList.add(commandBody);
		stringList.add("David Newland");
		stringList.add("~~~");

		stringList.add(commandH2);
		stringList.add("Full source code available at");
		
		stringList.add(commandBody);
		stringList.add("https://github.com/chriswhocodes/DemoFX");
		stringList.add("Licensed under Simplified BSD");
		stringList.add("~~~");
		
		stringList.add(commandH2);
		stringList.add("Performance tuned using");
		
		stringList.add(commandBody);
		stringList.add("JITWatch");
		stringList.add(" Log analyser / visualiser for Java HotSpot JIT compiler.");
		stringList.add("https://github.com/AdoptOpenJDK/jitwatch/");
		stringList.add("~~~");
		
		stringList.add(commandH2);
		stringList.add("Greetings");
		
		stringList.add(commandBody);

		try
		{
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/greetings.txt")));

			int pos = 1;
						
			StringBuilder builder = new StringBuilder();
			
			String line;

			while ((line = bufferedReader.readLine()) != null)
			{
				if (pos % 3 == 0)
				{
					builder.append(line);
					stringList.add(builder.toString());
					builder.setLength(0);
				}
				else
				{
					builder.append(line).append(", ");
				}

				pos++;
			}
			
			bufferedReader.close();
		}
		catch (IOException ioe)
		{
			stringList.add("Couldn't load greetings file!");
		}
		
		stringList.add(commandBody);
		stringList.add("~~~");
		stringList.add("Copyright (c) 2015 Chris Newland");		

		precalculateLineWidths();
	}

	public void customInitialise(List<String> stringList, long startMillis, long stopMillis, boolean loopStringList, Color colour,
			Font font)
	{
		this.stringList = stringList;
		this.effectStartMillis = startMillis;
		this.effectStopMillis = stopMillis;
		this.loopStringList = loopStringList;
		this.fontColour = colour;
		this.currentFont = font;

		precalculateLineWidths();
	}

	public void customInitialise(List<String> stringList, long startMillis, long stopMillis, boolean loopStringList)
	{
		customInitialise(stringList, startMillis, stopMillis, loopStringList, null, gc.getFont());
	}

	public void customInitialise(List<String> stringList, long startMillis, long stopMillis, boolean loopStringList, Color colour)
	{
		customInitialise(stringList, startMillis, stopMillis, loopStringList, colour, gc.getFont());
	}

	private final void precalculateLineWidths()
	{
		gc.setFont(currentFont);

		for (String str : stringList)
		{
			TextUtil.getStringDimensions(currentFont, gc, str);
		}
	}

	@Override
	public void renderForeground()
	{
		yOffset -= speed;

		double yPos = 0;

		yPos = yOffset;

		for (int index = 0; index < stringList.size(); index++)
		{
			String line = stringList.get(index);

			if (line.startsWith("{"))
			{
				handleCommand(line);
				continue;
			}

			double stringHeight = TextUtil.getStringDimensions(currentFont, gc, line).getY();

			yPos += stringHeight + currentSpacing;

			if (!effectFinished && isLineOnScreen(yPos))
			{
				plotText(line, yPos);
			}
		}

		if (yPos < 0)
		{
			if (loopStringList)
			{
				yOffset = height;
			}
			else
			{
				effectFinished = true;
			}
		}
	}

	private void handleCommand(String line)
	{
		String trimmed = line.substring(1, line.length() - 2);
		
		String[] commands = trimmed.split(";");
		
		String currentFontFamily = currentFont.getName();
		double currentFontSize = currentFont.getSize();
		
		for (String command : commands)
		{
			String[] parts = command.split(":");
			
			String key = parts[0];
			String value = parts[1];
			
			switch(key)
			{
			case "family":
				currentFontFamily = value;
				break;
			case "size":
				currentFontSize = Double.parseDouble(value);
				break;
			case "spacing":
				currentSpacing = Double.parseDouble(value);
				break;
			}
		}
		
		currentFont = Font.font(currentFontFamily, currentFontSize);
	}

	private final void plotText(String line, double yPos)
	{
		gc.setFont(currentFont);

		gc.setFill(fontColour);

		double strWidth = TextUtil.getStringDimensions(currentFont, gc, line).getX();

		double x = halfWidth - strWidth / 2;

		gc.fillText(line, x, yPos);
	}

	private final boolean isLineOnScreen(double yPos)
	{
		return yPos > -OFFSCREEN;
	}
}