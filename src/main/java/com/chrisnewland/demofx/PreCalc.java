package com.chrisnewland.demofx;

public class PreCalc
{
	private final double[] SINE;
	private final double[] COSINE;

	private final int WIDTH;
	private final int HEIGHT;
	private final int[][] DISTANCE;
	private final double[][] FADE_FACTOR;

	private final double[] RANDOM;
	private final int RANDOM_COUNT;

	private int randomPos = 0;

	private final double PRECALC_ACCURACY = 0.1;
	private final int INDEX_MULTIPLIER = (int) (1d / PRECALC_ACCURACY);
	private final int PRECALC_LENGTH = 360 * INDEX_MULTIPLIER;

	public PreCalc(int width, int height, int randoms)
	{
		WIDTH = width;
		HEIGHT = height;
		DISTANCE = new int[WIDTH][HEIGHT];
		FADE_FACTOR = new double[WIDTH][HEIGHT];

		int midX = WIDTH / 2;
		int midY = HEIGHT / 2;

		for (int x = 0; x < WIDTH; x++)
		{
			for (int y = 0; y < HEIGHT; y++)
			{
				int dx = Math.abs(x - midX);
				int dy = Math.abs(y - midY);

				int distance = (int) Math.sqrt((dx * dx) + (dy * dy));

				DISTANCE[x][y] = distance;

				double maxDimension = Math.max(WIDTH, HEIGHT);

				double normalisedDistance = distance / maxDimension; // 0..1

				double fadeFactor = 1 - (normalisedDistance * 1.1);

				FADE_FACTOR[x][y] = fadeFactor;
			}
		}

		RANDOM_COUNT = randoms;

		RANDOM = new double[RANDOM_COUNT];

		for (int i = 0; i < RANDOM_COUNT; i++)
		{
			RANDOM[i] = Math.random();
		}

		SINE = new double[PRECALC_LENGTH];
		COSINE = new double[PRECALC_LENGTH];

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

	public final double getRandom()
	{
		if (randomPos >= RANDOM_COUNT)
		{
			randomPos = 0;
		}

		return RANDOM[randomPos++];
	}

	public final double sin(double degrees)
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

	public final int getDistance(int x, int y)
	{
		return DISTANCE[x][y];
	}

	public final int getDistanceSafe(int x, int y)
	{
		x = Math.min(Math.max(0, x), WIDTH - 1);
		y = Math.min(Math.max(0, y), HEIGHT - 1);

		return DISTANCE[x][y];
	}

	public final double getCoordinateFade(int x, int y)
	{
		x = Math.min(Math.max(0, x), WIDTH - 1);
		y = Math.min(Math.max(0, y), HEIGHT - 1);

		return FADE_FACTOR[x][y];
	}
}