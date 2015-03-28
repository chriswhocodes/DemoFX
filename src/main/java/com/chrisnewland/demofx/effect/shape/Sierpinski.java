/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Sierpinski extends AbstractEffect
{
	private final double SMALLEST;

	private final double[] pointsX = new double[3];
	private final double[] pointsY = new double[3];

	private double rootHeight;

	class Triangle
	{
		private final double topX;
		private final double topY;
		private final double height;

		public Triangle(double topX, double topY, double height)
		{
			this.topX = topX;
			this.topY = topY;
			this.height = height;
		}

		public final double getTopX()
		{
			return topX;
		}

		public final double getTopY()
		{
			return topY;
		}

		public final double getHeight()
		{
			return height;
		}
	}

	private List<Triangle> keep;

	public Sierpinski(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
		SMALLEST = height / 64;
	}

	@Override
	protected void initialise()
	{
		keep = new ArrayList<>();
		rootHeight = height;
	}

	@Override
	public void renderBackground()
	{
		fillBackground(getCycleColour());
	}
	
	@Override
	public void renderForeground()
	{
		calcTriangles();

		drawTriangles();
	}

	private final void calcTriangles()
	{
		keep.clear();

		double acceleration = rootHeight * 0.02;
		
		rootHeight += acceleration;

		if (rootHeight >= 2 * height)
		{
			rootHeight = height;
		}

		Triangle root = new Triangle(halfWidth, 0, rootHeight);

		shrink(root);

		itemCount = keep.size();
	}

	private void shrink(Triangle tri)
	{
		double topX = tri.getTopX();
		double topY = tri.getTopY();
		double h = tri.getHeight();

		if (topY >= height)
		{
			return;
		}

		if (h < SMALLEST)
		{
			keep.add(tri);
		}
		else
		{
			Triangle top = new Triangle(topX, topY, h / 2);
			Triangle left = new Triangle(topX - h / 4, topY + h / 2, h / 2);
			Triangle right = new Triangle(topX + h / 4, topY + h / 2, h / 2);

			shrink(top);
			shrink(left);
			shrink(right);
		}
	}

	private final void drawTriangles()
	{
		gc.setFill(Color.WHITE);
		
		int triangleCount = keep.size();

		for (int i = 0; i < triangleCount; i++)
		{
			Triangle tri = keep.get(i);			

			if (tri.getTopY() < height)
			{
				drawTriangle(tri);
			}
		}
	}

	private final void drawTriangle(Triangle tri)
	{
		double topX = tri.getTopX();
		double topY = tri.getTopY();
		double h = tri.getHeight();

		pointsX[0] = topX;
		pointsY[0] = topY;

		pointsX[1] = topX + h / 2;
		pointsY[1] = topY + h;

		pointsX[2] = topX - h / 2;
		pointsY[2] = topY + h;

		gc.fillPolygon(pointsX, pointsY, 3);		
	}
}