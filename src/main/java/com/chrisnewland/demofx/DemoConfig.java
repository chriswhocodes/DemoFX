package com.chrisnewland.demofx;

import com.chrisnewland.demofx.effect.Stars;

public class DemoConfig
{
	public enum PlotMode {PLOT_MODE_LINE, PLOT_MODE_POLYGON};

	private String effect = Stars.class.getName();
	private int count = 500;
	private int width = 640;
	private int height = 480;
	private boolean antialias = false;
	private PlotMode plotMode = PlotMode.PLOT_MODE_POLYGON;

	private DemoConfig()
	{
	}

	public static DemoConfig parseArgs(String[] args)
	{
		DemoConfig config = new DemoConfig();

		boolean argError = false;

		int argc = args.length;

		for (int i = 0; i < argc; i+=2)
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
					case "i":
						config.count = Integer.parseInt(value);
						break;
					case "w":
						config.width = Integer.parseInt(value);
						break;
					case "h":
						config.height = Integer.parseInt(value);
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
		return effect;
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
}