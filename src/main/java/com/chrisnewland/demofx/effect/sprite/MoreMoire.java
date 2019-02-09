/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MoreMoire extends AbstractEffect
{
	private Image image;
	private double rotationDir;
	private double rotationAngle = 0.0;

	private long lastEffectStart = 0;
	private long millisPerEffect = 0;

	private List<MoireParameters> paramList;
	private int currentParamIndex = 0;

	private Random random = new Random();

	private double zoom = 1;
	private double zoomAngle = 0;

	private double clipAngle;

	private int minDimension;

	private double clipWidthOuter;
	private double clipHeightOuter;

	private double clipWidthInner;
	private double clipHeightInner;

	static class MoireParameters
	{
		private Image image;
		private double rotationSpeed;

		public MoireParameters(Image image, double rotationSpeed)
		{
			this.image = image;
			this.rotationSpeed = rotationSpeed;
		}

		public Image getImage()
		{
			return image;
		}

		public double getRotationSpeed()
		{
			return rotationSpeed;
		}
	}

	enum Shape
	{
		CIRCLE, SQUARE
	}

	public MoreMoire(DemoConfig config)
	{
		super(config);

		init();
	}

	private void init()
	{
		int intWidth = (int) width;
		int intHeight = (int) height;

		minDimension = Math.min(intWidth, intHeight);

		clipWidthOuter = (minDimension / 2) * 0.94;
		clipHeightOuter = (minDimension / 2) * 0.94;

		clipWidthInner = (minDimension / 2) * 0.92;
		clipHeightInner = (minDimension / 2) * 0.92;

		paramList = new ArrayList<>();

		//1
		paramList.add(new MoireParameters(buildImagePolygon(4, 4), 0.32));
		paramList.add(new MoireParameters(buildImageCheckerboard(2, Shape.CIRCLE), 0.32));

		//2
		paramList.add(new MoireParameters(buildImagePolygon(3, 6), -0.16));
		paramList.add(new MoireParameters(buildImageCheckerboard(4, Shape.SQUARE), 0.124));

		//3
		paramList.add(new MoireParameters(buildImagePolygon(5, 4), 0.32));
		paramList.add(new MoireParameters(buildImageCheckerboard(2, Shape.CIRCLE), 0.16));

		//4
		paramList.add(new MoireParameters(buildImagePolygon(6, 6), -0.48));
		paramList.add(new MoireParameters(buildImageCheckerboard(5, Shape.SQUARE), 0.64));

		//5
		paramList.add(new MoireParameters(buildImagePolygon(7, 4), 0.32));
		paramList.add(new MoireParameters(buildImageCheckerboard(4, Shape.CIRCLE), 0.32));

		//6
		paramList.add(new MoireParameters(buildImagePolygon(8, 6), -0.32));
		paramList.add(new MoireParameters(buildImageCheckerboard(2, Shape.SQUARE), 0.16));

		//7
		paramList.add(new MoireParameters(buildImagePolygon(4, 4), 0.16));
		paramList.add(new MoireParameters(buildImageCheckerboard(6, Shape.CIRCLE), 0.24));

		//8
		paramList.add(new MoireParameters(buildImagePolygon(3, 6), -0.32));
		paramList.add(new MoireParameters(buildImageCheckerboard(4, Shape.SQUARE), 0.16));

		//9
		paramList.add(new MoireParameters(buildImagePolygon(5, 4), 0.48));
		paramList.add(new MoireParameters(buildImageCheckerboard(4, Shape.CIRCLE), 0.24));

		//10
		paramList.add(new MoireParameters(buildImagePolygon(3, 6), -0.8));
		paramList.add(new MoireParameters(buildImageCheckerboard(6, Shape.SQUARE), 0.16));

		//11
		paramList.add(new MoireParameters(buildImagePolygon(4, 6), 0.32));
		paramList.add(new MoireParameters(buildImageCheckerboard(5, Shape.CIRCLE), 0.24));

		//12
		paramList.add(new MoireParameters(buildImagePolygon(6, 4), -0.64));
		paramList.add(new MoireParameters(buildImageCheckerboard(4, Shape.SQUARE), 0.16));

		millisPerEffect = 2667 * 2;// 2 bars @ 90bpm

		System.out.println("millisPerEffect: " + millisPerEffect);

		lastEffectStart = System.currentTimeMillis();

		currentParamIndex = 0;

		MoireParameters params = paramList.get(currentParamIndex);

		this.image = params.getImage();
		this.rotationDir = params.getRotationSpeed();
	}

	private Image buildImageCheckerboard(double side, Shape shape)
	{
		fillBackground(Color.BLACK);

		int changeColourEvery = (int) (minDimension / side / 16);

		boolean offset = false;

		double gapX = side * 2;
		double gapY = side;

		int colourCount = 0;

		for (double y = 0; y < minDimension; y += gapY)
		{
			offset = !offset;

			for (double x = 0; x < minDimension; x += gapX)
			{
				double x2 = x + (offset ? 0 : side);

				if (colourCount++ == changeColourEvery)
				{
					gc.setFill(getCycleColour());
					colourCount = 0;
				}

				if (shape == Shape.SQUARE)
				{
					gc.fillRect(x2, y, side, side);
				}
				else
				{
					gc.fillOval(x2, y, side, side);
				}
			}
		}

		return ImageUtil.replaceColour(ImageUtil.createImageFromCanvas(gc.getCanvas(), minDimension, minDimension, true),
				Color.BLACK, Color.TRANSPARENT);
	}

	private Image buildImagePolygon(int pointCount, double thickness)
	{
		fillBackground(Color.BLACK);

		boolean even = true;

		double[] pointsX = new double[pointCount];
		double[] pointsY = new double[pointCount];

		for (double radius = width; radius > 0; radius -= thickness)
		{
			double angleDelta = 360.0 / (double) pointCount;

			gc.setFill(even ? Color.WHITE : Color.BLACK);

			double angle = angleDelta / 2;

			for (int i = 0; i < pointCount; i++)
			{
				pointsX[i] = (double) minDimension / 2.0 + radius * precalc.sin(angle);
				pointsY[i] = (double) minDimension / 2.0 + radius * precalc.cos(angle);
				angle += angleDelta;
			}

			gc.fillPolygon(pointsX, pointsY, pointCount);

			even = !even;
		}

		return ImageUtil.replaceColour(ImageUtil.createImageFromCanvas(gc.getCanvas(), minDimension, minDimension, true),
				Color.BLACK, Color.TRANSPARENT);
	}

	private void clip()
	{
		clipAngle += 0.72;

		if (clipAngle >= 360.0)
		{
			clipAngle -= 360.0;
		}

		double modX = (precalc.sin(clipAngle) + 0.8) * 48.0;
		double modY = (precalc.cos(clipAngle) + 0.8) * 48.0;

		gc.beginPath();
		gc.arc(width / 2.0, height / 2.0, clipWidthOuter - modX, clipHeightOuter - modY, 0, 360);
		gc.closePath();
		gc.clip();

		fillBackground(Color.WHITE);

		gc.beginPath();
		gc.arc(width / 2.0, height / 2.0, clipWidthInner - modX, clipHeightInner - modY, 0, 360);
		gc.closePath();
		gc.clip();
	}

	@Override public void renderForeground()
	{
		clip();

		fillBackground(Color.BLACK);

		long now = System.currentTimeMillis();

		if (now - lastEffectStart > millisPerEffect)
		{
			currentParamIndex++;

			if (currentParamIndex >= paramList.size())
			{
				currentParamIndex = 0;
			}

			lastEffectStart = now;

			MoireParameters params = paramList.get(currentParamIndex);

			this.image = params.getImage();
			this.rotationDir = params.getRotationSpeed();
			rotationAngle = 0;
			zoom = 1;
		}

		zoomAngle += 1.4;

		if (zoomAngle >= 360)
		{
			zoomAngle -= 360;
		}

		zoom = 2.0 + precalc.cos(zoomAngle);

		double scaledDimension = minDimension * zoom;

		double newX = width / 2 - scaledDimension / 2;
		double newY = height / 2 - scaledDimension / 2;

		gc.drawImage(image, newX, newY, scaledDimension, scaledDimension);

		rotateCanvasAroundCentre(rotationDir);

		rotationAngle += rotationDir;

		gc.drawImage(image, newX, newY, scaledDimension, scaledDimension);
	}
}