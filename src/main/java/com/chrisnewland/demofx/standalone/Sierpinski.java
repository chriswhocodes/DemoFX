/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.standalone;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Sierpinski extends Application
{
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

	private final double width = 800;
	private final double height = 600;
	private final double smallest = 8;
	
	private GraphicsContext gc;
	private List<Triangle> renderList;

	private final double[] pointsX = new double[3];
	private final double[] pointsY = new double[3];

	private double rootHeight;


	private final void calcTriangles()
	{
		renderList.clear();

		double acceleration = rootHeight * 0.02;

		rootHeight += acceleration;

		if (rootHeight >= 2 * height)
		{
			rootHeight = height;
		}

		Triangle root = new Triangle(width / 2, 0, rootHeight);

		shrink(root);
	}

	private void shrink(Triangle tri)
	{
		double topX = tri.getTopX();
		double topY = tri.getTopY();
		double triangleHeight = tri.getHeight();

		if (topY >= height)
		{
			return;
		}

		if (triangleHeight < smallest)
		{
			renderList.add(tri);
		}
		else
		{
			Triangle top = new Triangle(topX, topY, triangleHeight / 2);
			Triangle left = new Triangle(topX - triangleHeight / 4, topY + triangleHeight / 2, triangleHeight / 2);
			Triangle right = new Triangle(topX + triangleHeight / 4, topY + triangleHeight / 2, triangleHeight / 2);

			shrink(top);
			shrink(left);
			shrink(right);
		}
	}
	
	private final void clearBackground()
	{
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);
	}

	private final void drawTriangles()
	{
		gc.setFill(Color.WHITE);

		int triangleCount = renderList.size();

		for (int i = 0; i < triangleCount; i++)
		{
			Triangle tri = renderList.get(i);

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

	public static void main(String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception
	{
		BorderPane root = new BorderPane();

		Scene scene;

		scene = new Scene(root, width, height);
		
		renderList = new ArrayList<>();
		rootHeight = height;

		Canvas canvas = new Canvas(width, height);

		gc = canvas.getGraphicsContext2D();

		root.setCenter(canvas);

		stage.setTitle("GraphicsContext.fillPolygon performance test");
		stage.setScene(scene);
		stage.show();

		AnimationTimer timer = new AnimationTimer()
		{
			private long nextSecond = 0;
			private int framesPerSecond = 0;
			
			@Override
			public void handle(long startNanos)
			{
				calcTriangles();

				clearBackground();

				drawTriangles();
				
				framesPerSecond++;
				
				if (startNanos > nextSecond)
				{
					System.out.println("fps: " + framesPerSecond);
					framesPerSecond = 0;
					nextSecond = startNanos + 1_000_000_000L;
				}
			}
		};

		timer.start();
	}
}