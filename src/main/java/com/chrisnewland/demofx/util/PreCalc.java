/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.util;

import java.util.Random;

import com.chrisnewland.demofx.DemoConfig;

public class PreCalc
{
	private final double[] SINE;
	private final double[] COSINE;

	private final int WIDTH;
	private final int HEIGHT;
	private final int HALF_WIDTH;
	private final int HALF_HEIGHT;

	private final int MAX_DIMENSION;

	private final int[][] DISTANCE;
	private final double[][] FADE_FACTOR;

	private static final int RANDOM_COUNT = 4096 + 1; // +1 to prevent multiple
														// of screen size
	private final double[] UNSIGNED_RANDOM;
	private final double[] SIGNED_RANDOM;
	private final int[] RANDOM_COLOUR;

	private int unsignedRandomPos = 0;
	private int signedRandomPos = 0;
	private int colourRandomPos = 0;

	private static final double PRECALC_ACCURACY = 0.1;
	private static final int INDEX_MULTIPLIER = (int) (1d / PRECALC_ACCURACY);
	private static final int PRECALC_LENGTH = 360 * INDEX_MULTIPLIER;

	private final boolean USE_LOOKUPS_FOR_SQRT;
	private final boolean USE_LOOKUPS_FOR_TRIG;
	private final boolean USE_LOOKUPS_FOR_RANDOM;

	public PreCalc(DemoConfig config)
	{
		USE_LOOKUPS_FOR_SQRT = config.isLookupSqrt();
		USE_LOOKUPS_FOR_TRIG = config.isLookupTrig();
		USE_LOOKUPS_FOR_RANDOM = config.isLookupRandom();

		WIDTH = config.getWidth();
		HEIGHT = config.getHeight();

		HALF_WIDTH = WIDTH / 2;
		HALF_HEIGHT = HEIGHT / 2;

		MAX_DIMENSION = Math.max(WIDTH, HEIGHT);

		DISTANCE = new int[WIDTH][HEIGHT];

		FADE_FACTOR = new double[WIDTH][HEIGHT];

		if (USE_LOOKUPS_FOR_SQRT)
		{
			buildDistanceTable();
			buildColourFadeTable();
		}

		if (USE_LOOKUPS_FOR_RANDOM)
		{
			UNSIGNED_RANDOM = new double[RANDOM_COUNT];
			SIGNED_RANDOM = new double[RANDOM_COUNT];
			RANDOM_COLOUR = new int[RANDOM_COUNT];
			buildRandomTables();
		}
		else
		{
			UNSIGNED_RANDOM = new double[0];
			SIGNED_RANDOM = new double[0];
			RANDOM_COLOUR = new int[0];
		}

		if (USE_LOOKUPS_FOR_TRIG)
		{
			SINE = new double[PRECALC_LENGTH];
			COSINE = new double[PRECALC_LENGTH];
			buildTrigTables();
		}
		else
		{
			SINE = new double[0];
			COSINE = new double[0];
		}
	}

	private final void buildDistanceTable()
	{
		for (int x = 0; x < WIDTH; x++)
		{
			for (int y = 0; y < HEIGHT; y++)
			{
				int distance = (int) Math.sqrt((x * x) + (y * y));

				DISTANCE[x][y] = distance;
			}
		}
	}

	private final void buildColourFadeTable()
	{
		int midX = WIDTH / 2;
		int midY = HEIGHT / 2;

		for (int x = 0; x < WIDTH; x++)
		{
			for (int y = 0; y < HEIGHT; y++)
			{
				double distance = getDistance(midX, midY, x, y);

				double normalisedDistance = distance / MAX_DIMENSION; // 0..1

				double fadeFactor = 1 - (normalisedDistance * 1.3);

				FADE_FACTOR[x][y] = fadeFactor;
			}
		}
	}

	private final void buildRandomTables()
	{
		Random random = new Random();

		for (int i = 0; i < RANDOM_COUNT; i++)
		{
			UNSIGNED_RANDOM[i] = Math.random();
			SIGNED_RANDOM[i] = (Math.random() - 0.5) * 2;
			RANDOM_COLOUR[i] = random.nextInt(255 + 1);
		}
	}

	/*
	 * 0 <= x < 1
	 */
	public final double getUnsignedRandom()
	{
		if (USE_LOOKUPS_FOR_RANDOM)
		{
			if (unsignedRandomPos >= RANDOM_COUNT)
			{
				unsignedRandomPos = 0;
			}

			return UNSIGNED_RANDOM[unsignedRandomPos++];
		}
		else
		{
			return Math.random();
		}
	}

	/*
	 * 0 <= x <= 255
	 */
	public final int getRandomColour()
	{
		if (USE_LOOKUPS_FOR_RANDOM)
		{
			if (colourRandomPos >= RANDOM_COUNT)
			{
				colourRandomPos = 0;
			}

			return RANDOM_COLOUR[colourRandomPos++];
		}
		else
		{
			return (int) (Math.random() * 255);
		}
	}

	/*
	 * -1 < x < 1
	 */
	public final double getSignedRandom()
	{
		if (USE_LOOKUPS_FOR_RANDOM)
		{
			if (signedRandomPos >= RANDOM_COUNT)
			{
				signedRandomPos = 0;
			}

			return SIGNED_RANDOM[signedRandomPos++];
		}
		else
		{
			return (Math.random() - 0.5) * 2;
		}
	}

	// =================================================
	// Trigonometry methods
	// =================================================

	private final void buildTrigTables()
	{
		double theta = 0;
		double inc = Math.toRadians(PRECALC_ACCURACY);

		for (int i = 0; i < PRECALC_LENGTH; i++)
		{
			double s = Math.sin(theta);
			double c = Math.cos(theta);

			SINE[i] = s;
			COSINE[i] = c;

			theta += inc;
		}
	}

	public final double sin(double degrees)
	{
		if (USE_LOOKUPS_FOR_TRIG)
		{
			return lookupSin(degrees);
		}
		else
		{
			return Math.sin(Math.toRadians(degrees));
		}
	}

	private final double lookupSin(double degrees)
	{
		if (degrees >= 360)
		{
			do
			{
				degrees -= 360;
			} while (degrees >= 360);
		}

		int index = (int) (degrees * INDEX_MULTIPLIER);

		return SINE[index];
	}

	public final double cos(double degrees)
	{
		if (USE_LOOKUPS_FOR_TRIG)
		{
			return lookupCos(degrees);
		}
		else
		{
			return Math.cos(Math.toRadians(degrees));
		}
	}

	private final double lookupCos(double degrees)
	{
		if (degrees >= 360)
		{
			do
			{
				degrees -= 360;
			} while (degrees >= 360);
		}

		int index = (int) (degrees * INDEX_MULTIPLIER);

		return COSINE[index];
	}

	// =================================================
	// Calculate 2D distance
	// =================================================

	public final double getDistance(double x1, double y1, double x2, double y2)
	{
		if (USE_LOOKUPS_FOR_SQRT)
		{
			return lookupDistance(x1, y1, x2, y2);
		}
		else
		{
			return calculateDistance(x1, y1, x2, y2);
		}
	}

	private final double lookupDistance(double x1, double y1, double x2, double y2)
	{
		int dx = (int) Math.abs(x1 - x2);
		int dy = (int) Math.abs(y1 - y2);

		dx = Math.min(dx, WIDTH - 1);
		dy = Math.min(dy, HEIGHT - 1);

		return DISTANCE[dx][dy];
	}

	private final double calculateDistance(double x1, double y1, double x2, double y2)
	{
		double dx = x1 - x2;
		double dy = y1 - y2;

		return Math.sqrt(dx * dx + dy * dy);
	}

	// =================================================
	// Coordinate based fade
	// =================================================

	public final double getCoordinateFade(int x, int y)
	{
		if (USE_LOOKUPS_FOR_SQRT)
		{
			return lookupFade(x, y);
		}
		else
		{
			return calculateFade(x, y);
		}
	}

	private final double lookupFade(int x, int y)
	{
		x = Math.min(Math.max(0, x), WIDTH - 1);
		y = Math.min(Math.max(0, y), HEIGHT - 1);

		return FADE_FACTOR[x][y];
	}

	private final double calculateFade(int x, int y)
	{
		double dx = x - HALF_WIDTH;
		double dy = y - HALF_HEIGHT;

		double distance = Math.sqrt(dx * dx + dy * dy);

		double normalisedDistance = distance / MAX_DIMENSION; // 0..1

		return 1 - (normalisedDistance * 1.3);
	}
}