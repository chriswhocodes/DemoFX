package com.chrisnewland.demofx.effect;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig.PlotMode;
import com.chrisnewland.demofx.PreCalc;

public class Stars extends AbstractEffect
{
	private int[] starPosX;
	private int[] starPosY;
	private int[] starVectorX;
	private int[] starVectorY;

	private int[] starColorRed;
	private int[] starColorGreen;
	private int[] starColorBlue;

	private double[] starAngle;

	private int[] spikeCount;

	private double[] outerDiameter;
	private double[] innerDiameter;

	private double[] respawnEllipseX;
	private double[] respawnEllipseY;

	private int respawnAngle = 0;

	private int bgColourRed;
	private int bgColourGreen;
	private int bgColourBlue;

	private int bgDirRed = 1;
	private int bgDirGreen = 1;
	private int bbgDirBlue = 1;

	private int bgColourCounter = 0;

	private int spawnDiameterX = 50;
	private int spawnDiameterXDir = 1;

	private int spawnDiameterY = 100;
	private int spawnDiameterYDir = 1;

	private int respawnEllipse;

	private boolean incRespawnAngle = true;

	private int respawnShapeCount = 0;

	private final int respawnShapeLimit = 5;

	private static final int OFFSCREEN = 16;

	private final int OFFSCREEN_LEFT;
	private final int OFFSCREEN_RIGHT;
	private final int OFFSCREEN_TOP;
	private final int OFFSCREEN_BOTTOM;

	private PreCalc precalc;

	private final PlotMode mode;

	public Stars(GraphicsContext gc, int count, int width, int height, PlotMode mode)
	{
		super(gc, count, width, height);

		this.mode = mode;

		OFFSCREEN_LEFT = -OFFSCREEN;
		OFFSCREEN_RIGHT = width + OFFSCREEN;
		OFFSCREEN_TOP = -OFFSCREEN;
		OFFSCREEN_BOTTOM = height + OFFSCREEN;
	}

	@Override
	protected void initialise()
	{
		precalc = new PreCalc(width, height, 1000);

		itemName = "stars";

		starPosX = new int[itemCount];
		starPosY = new int[itemCount];

		starVectorX = new int[itemCount];
		starVectorY = new int[itemCount];

		starColorRed = new int[itemCount];
		starColorGreen = new int[itemCount];
		starColorBlue = new int[itemCount];

		starAngle = new double[itemCount];
		spikeCount = new int[itemCount];
		outerDiameter = new double[itemCount];
		innerDiameter = new double[itemCount];

		for (int i = 0; i < itemCount; i++)
		{
			int x = getRandomIntInclusive(0, width);
			int y = getRandomIntInclusive(0, height);

			int dx = getRandomIntInclusive(2, 12);
			int dy = getRandomIntInclusive(2, 12);

			if (getRandomDouble(0, 1) > 0.5)
			{
				dx = -dx;
			}

			if (getRandomDouble(0, 1) > 0.5)
			{
				dy = -dy;
			}

			starPosX[i] = x;
			starPosY[i] = y;

			starVectorX[i] = dx;
			starVectorY[i] = dy;

			starColorRed[i] = getRandomIntInclusive(32, 255);
			starColorGreen[i] = getRandomIntInclusive(32, 255);
			starColorBlue[i] = getRandomIntInclusive(32, 255);

			starAngle[i] = getRandomIntInclusive(0, 20);

			spikeCount[i] = getRandomIntInclusive(4, 6);

			outerDiameter[i] = getRandomIntInclusive(8, 16);

			innerDiameter[i] = outerDiameter[i] / getRandomIntInclusive(2, 4);
		}

		respawnEllipse = itemCount / 3;

		respawnEllipseX = new double[360];
		respawnEllipseY = new double[360];

		for (int degree = 0; degree < 360; degree++)
		{
			respawnEllipseX[degree] = Math.sin(Math.toRadians(degree));
			respawnEllipseY[degree] = Math.cos(Math.toRadians(degree));
		}

		bgColourRed = 0;
		bgColourGreen = 32;
		bgColourBlue = 16;
	}

	@Override
	public final void render()
	{
		long renderStartNanos = System.nanoTime();

		fillBackground();

		for (int i = 0; i < itemCount; i++)
		{
			plotStar(i);

			rotateStar(i);

			moveStar(i);
		}

		long renderEndNanos = System.nanoTime();

		long renderNanos = renderEndNanos - renderStartNanos;

		updateFPS(renderNanos);
	}

	private final void plotStar(int i)
	{
		if (mode == PlotMode.PLOT_MODE_POLYGON)
		{
			drawStarPolygon(i);
		}
		else if (mode == PlotMode.PLOT_MODE_LINE)
		{
			drawStarLine(i);
		}
	}

	private final void rotateStar(int i)
	{
		starAngle[i] += 3;

		if (starAngle[i] > 360)
		{
			starAngle[i] -= 360;
		}
	}

	private final void moveStar(int i)
	{
		starPosX[i] += starVectorX[i];
		starPosY[i] += starVectorY[i];

		if (starPosX[i] < OFFSCREEN_LEFT || starPosX[i] > OFFSCREEN_RIGHT || starPosY[i] < OFFSCREEN_TOP || starPosY[i] > OFFSCREEN_BOTTOM)
		{
			respawn(i);
		}
	}

	private final void fillBackground()
	{
		if (bgColourCounter++ > 5)
		{
			int max = 32;

			bgColourRed += bgDirRed;
			bgColourGreen += bgDirGreen;
			bgColourBlue += bbgDirBlue;

			if (bgColourRed > max || bgColourRed == 0)
			{
				bgDirRed = -bgDirRed;
			}

			if (bgColourGreen > max || bgColourGreen == 0)
			{
				bgDirGreen = -bgDirGreen;
			}

			if (bgColourBlue > max || bgColourBlue == 0)
			{
				bbgDirBlue = -bbgDirBlue;
			}

			bgColourCounter = 0;
		}

		gc.setFill(Color.rgb(bgColourRed, bgColourGreen, bgColourBlue));

		gc.fillRect(0, 0, width, height);
	}

	private final void respawn(int i)
	{
		if (i > respawnEllipse)
		{
			// respawn on ellipse
			starPosX[i] = halfWidth + (int) (spawnDiameterX * respawnEllipseX[respawnAngle]);
			starPosY[i] = halfHeight + (int) (spawnDiameterY * respawnEllipseY[respawnAngle]);

			if (incRespawnAngle)
			{
				respawnAngle++;
			}

			incRespawnAngle = !incRespawnAngle;

			if (respawnAngle == 360)
			{
				respawnAngle = 0;
			}

			if (respawnShapeCount++ == respawnShapeLimit)
			{
				spawnDiameterX += spawnDiameterXDir;
				spawnDiameterY += spawnDiameterYDir;

				respawnShapeCount = 0;

				if (spawnDiameterX < 50 || spawnDiameterX > halfWidth)
				{
					spawnDiameterXDir = -spawnDiameterXDir;
				}

				if (spawnDiameterY < 50 || spawnDiameterY > halfHeight)
				{
					spawnDiameterYDir = -spawnDiameterYDir;
				}
			}
		}
		else
		{
			// respawn at random location
			starPosX[i] = (int) (precalc.getRandom() * width);
			starPosY[i] = (int) (precalc.getRandom() * height);
		}
	}

	private final void drawStarLine(int index)
	{
		int x = starPosX[index];
		int y = starPosY[index];

		int spikes = spikeCount[index];

		double outer = outerDiameter[index];
		double inner = innerDiameter[index];

		double firstX = 0;
		double firstY = 0;

		double lastX = 0;
		double lastY = 0;

		double spikeAngle = 360.0 / (spikes * 2);
		double theta = spikeAngle + starAngle[index];

		setStarColour(index, x, y);

		for (int i = 0; i < spikes; i++)
		{
			theta += spikeAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			double newX = (int) (x + outer * sinTheta);
			double newY = (int) (y + outer * cosTheta);

			if (i > 0)
			{
					gc.strokeLine(lastX, lastY, newX, newY);
			}
			else
			{
				firstX = newX;
				firstY = newY;
			}

			theta += spikeAngle;

			sinTheta = precalc.sin(theta);
			cosTheta = precalc.cos(theta);

			double newX2 = (int) (x + inner * sinTheta);
			double newY2 = (int) (y + inner * cosTheta);

			gc.strokeLine(newX, newY, newX2, newY2);

			lastX = newX2;
			lastY = newY2;
		}

		gc.strokeLine(lastX, lastY, firstX, firstY);
	}

	private final void drawStarPolygon(int index)
	{
		int x = starPosX[index];
		int y = starPosY[index];

		int spikes = spikeCount[index];

		double outer = outerDiameter[index];
		double inner = innerDiameter[index];

		double[] pointsX = new double[spikes * 2];
		double[] pointsY = new double[spikes * 2];

		double spikeAngle = 360.0 / (spikes * 2);
		double theta = spikeAngle + starAngle[index];

		setStarColour(index, x, y);

		int pos = 0;

		for (int i = 0; i < spikes; i++)
		{
			theta += spikeAngle;

			double sinTheta = precalc.sin(theta);
			double cosTheta = precalc.cos(theta);

			pointsX[pos] = x + outer * sinTheta;
			pointsY[pos++] = y + outer * cosTheta;

			theta += spikeAngle;

			sinTheta = precalc.sin(theta);
			cosTheta = precalc.cos(theta);

			pointsX[pos] = x + inner * sinTheta;
			pointsY[pos] = y + inner * cosTheta;

			pos++;
		}

		gc.strokePolygon(pointsX, pointsY, pos);
	}

	private final void setStarColour(int index, int x, int y)
	{
		int r = starColorRed[index];
		int g = starColorGreen[index];
		int b = starColorBlue[index];

		double fadeFactor = precalc.getCoordinateFade(x, y);

		r *= fadeFactor;
		g *= fadeFactor;
		b *= fadeFactor;

		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);

		gc.setStroke(Color.rgb(r, g, b));
	}
}