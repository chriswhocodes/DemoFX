/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Fireworks extends AbstractEffect
{
	private double[][] px;
	private double[][] py;
	private double[] dx;
	private double[] dy;
	private boolean[] popped;

	private static final double GRAVITY = 0.125;
	private static final int FRAGS = 24;
	private static final int FRAG_HIST = 16;
	private static final int MAX_FRAME_COUNT = 150;
	private static final double POP_SPEED = 7;

	private double fragX[][][];
	private double fragY[][][];
	private double fragDX[][];
	private double fragDY[][];
	private int fragFrameCount[];
	private Image[] images;

	private Image imageStar = ImageUtil.loadImageFromResources("star.png");
	private Image imageStarBlue = ImageUtil.tintImage(imageStar, Color.BLUE.getHue());
	private Image imageStarRed = ImageUtil.tintImage(imageStar, Color.RED.getHue());
	private Image imageStarGreen = ImageUtil.tintImage(imageStar, Color.GREEN.getHue());
	private Image imageStarPurple = ImageUtil.tintImage(imageStar, Color.PURPLE.getHue());

	private Image[] imageBank;

	public Fireworks(DemoConfig config)
	{
		super(config);

		init(20);
	}

	public Fireworks(DemoConfig config, int count)
	{
		super(config);

		init(count);
	}

	private void init(int count)
	{
		itemCount = count;

		px = new double[itemCount][FRAG_HIST];
		py = new double[itemCount][FRAG_HIST];
		dx = new double[itemCount];
		dy = new double[itemCount];

		popped = new boolean[itemCount];
		fragX = new double[itemCount][FRAGS][FRAG_HIST];
		fragY = new double[itemCount][FRAGS][FRAG_HIST];
		fragDX = new double[itemCount][FRAGS];
		fragDY = new double[itemCount][FRAGS];
		fragFrameCount = new int[itemCount];

		images = new Image[itemCount];

		imageBank = new Image[] { imageStar, imageStarBlue, imageStarRed, imageStarGreen, imageStarPurple };

		for (int i = 0; i < itemCount; i++)
		{
			reset(i, true);
		}
	}

	private void reset(int i, boolean initialDelay)
	{
		double interval = (width - 100) / (itemCount + 1);

		px[i][0] = (i + 1) * interval;
		py[i][0] = height - 16;

		for (int k = FRAG_HIST - 1; k > 0; k--)
		{
			px[i][k] = px[i][0];
			py[i][k] = py[i][0];
		}

		dx[i] = 4 * precalc.getSignedRandom();
		dy[i] = -(8.5 + precalc.getUnsignedRandom() * 3);

		if (initialDelay)
		{
			py[i][0] += i * 10;
			dy[i] -= i * 0.15;
		}

		popped[i] = false;

		images[i] = imageBank[i % imageBank.length];
	}

	@Override
	public void renderForeground()
	{
		for (int i = 0; i < itemCount; i++)
		{
			gc.setGlobalAlpha(1);

			if (popped[i])
			{
				moveFrags(i);

				renderFrags(i);
			}
			else
			{
				moveUnpopped(i);

				renderUnpopped(i);
			}
		}
	}

	private final void moveUnpopped(int i)
	{
		px[i][0] += dx[i];
		py[i][0] += dy[i];

		for (int k = FRAG_HIST - 1; k > 0; k--)
		{
			px[i][k] = px[i][k - 1];
			py[i][k] = py[i][k - 1];
		}

		dy[i] += GRAVITY;

		if (dy[i] > 1)
		{
			pop(i);
		}
	}

	private final void pop(int i)
	{
		popped[i] = true;

		double angle = 360 / FRAGS;

		for (int j = 0; j < FRAGS; j++)
		{
			for (int k = 0; k < FRAG_HIST; k++)
			{
				fragX[i][j][k] = px[i][0];
				fragY[i][j][k] = py[i][0];
			}

			double modAngleSin = angle + precalc.getSignedRandom() / 2;
			double modAngleCos = angle + precalc.getSignedRandom() / 2;

			double modPopSpeedX = POP_SPEED * precalc.getSignedRandom() * 1.2;
			double modPopSpeedY = POP_SPEED * precalc.getSignedRandom() * 1.2;

			fragDX[i][j] = modPopSpeedX * precalc.sin(j * modAngleSin);
			fragDY[i][j] = modPopSpeedY * precalc.cos(j * modAngleCos);
		}

		fragFrameCount[i] = 0;
	}

	private final void moveFrags(int i)
	{
		fragFrameCount[i]++;

		if (fragFrameCount[i] > MAX_FRAME_COUNT)
		{
			reset(i, false);
			return;
		}

		for (int j = 0; j < FRAGS; j++)
		{
			fragX[i][j][0] += fragDX[i][j];
			fragY[i][j][0] += fragDY[i][j];

			fragDX[i][j] *= .96;
			fragDY[i][j] *= .96;

			fragDY[i][j] += GRAVITY;

			for (int k = FRAG_HIST - 1; k > 0; k--)
			{
				fragX[i][j][k] = fragX[i][j][k - 1];
				fragY[i][j][k] = fragY[i][j][k - 1];

				fragY[i][j][k] += GRAVITY * fragFrameCount[i] / 4;
			}
		}
	}

	private final void renderUnpopped(int i)
	{
		for (int j = 0; j < FRAG_HIST; j++)
		{
			gc.setGlobalAlpha(1 - ((double)j / (double)FRAG_HIST));

			gc.drawImage(images[i], px[i][j], py[i][j], 8, 12);
		}
	}

	private final void renderFrags(int i)
	{
		gc.setGlobalAlpha(1.0 - (double)fragFrameCount[i] * 1.2 / (double)MAX_FRAME_COUNT);

		for (int j = 0; j < FRAGS; j++)
		{
			for (int k = 0; k < FRAG_HIST; k++)
			{
				gc.drawImage(images[i], fragX[i][j][k], fragY[i][j][k], 8, 8);
			}
		}
	}
}