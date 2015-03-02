/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

public class DemoConfig
{
	public enum PlotMode
	{
		PLOT_MODE_LINE, PLOT_MODE_POLYGON, PLOT_MODE_FILL_POLYGON
	}

	private String effect = "stars";
	private int count = 300;
	private int rotation = 10;
	private int width = 800;
	private int height = 600;
	private boolean antialias = false;
	private PlotMode plotMode = PlotMode.PLOT_MODE_FILL_POLYGON;

	private DemoConfig()
	{
	}

	public static String getUsageError()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("DemoFXApplication [options]").append("\n");
		builder.append("-e <effect>").append("\t\t").append("triangles,squares,pentagons,hexagons,stars").append("\n");
		builder.append("-c <count>").append("\t\t").append("number of items on screen").append("\n");
		builder.append("-r <degrees>").append("\t\t").append("rotation per frame").append("\n");
		builder.append("-w <width>").append("\t\t").append("canvas width").append("\n");
		builder.append("-h <height>").append("\t\t").append("canvas height").append("\n");
		builder.append("-a <true|false>").append("\t\t").append("antialias canvas").append("\n");
		builder.append("-m <line|poly|fill>").append("\t").append("canvas plot mode").append("\n");

		return builder.toString();
	}

	public static DemoConfig parseArgs(String[] args)
	{
		DemoConfig config = new DemoConfig();

		boolean argError = false;

		int argc = args.length;

		for (int i = 0; i < argc; i += 2)
		{
			String arg = args[i];

			boolean lastArg = (i == argc - 1);

			if (arg.startsWith("-") && arg.length() == 2 && !lastArg)
			{
				String value = args[i + 1];

				try
				{
					switch (arg.substring(1))
					{
					case "e":
						config.effect = value;
						break;
					case "c":
						config.count = Integer.parseInt(value);
						break;
					case "w":
						config.width = Integer.parseInt(value);
						break;
					case "h":
						config.height = Integer.parseInt(value);
						break;
					case "r":
						config.rotation = Integer.parseInt(value);
						break;
					case "a":
						config.antialias = Boolean.parseBoolean(value);
						break;
					case "m":
						if ("line".equals(value.toLowerCase()))
						{
							config.plotMode = PlotMode.PLOT_MODE_LINE;
						}
						else if ("poly".equals(value.toLowerCase()))
						{
							config.plotMode = PlotMode.PLOT_MODE_POLYGON;
						}
						else if ("fill".equals(value.toLowerCase()))
						{
							config.plotMode = PlotMode.PLOT_MODE_FILL_POLYGON;
						}
						else
						{
							argError = true;
						}
						break;
					default:
						argError = true;
						break;
					}
				}
				catch (Exception e)
				{
					argError = true;
					break;
				}
			}
			else
			{
				argError = true;
			}
		}

		if (argError)
		{
			config = null;
		}

		return config;
	}

	public String getEffect()
	{
		return effect.substring(0, 1).toUpperCase() + effect.substring(1).toLowerCase();
	}

	public int getCount()
	{
		return count;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public boolean isAntialias()
	{
		return antialias;
	}

	public PlotMode getPlotMode()
	{
		return plotMode;
	}

	public int getRotation()
	{
		return rotation;
	}
}