/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Dots extends AbstractEffect
{
	class Point3D
	{
		private double x;
		private double y;
		private double z;

		public Point3D(double x, double y, double z)
		{
			super();
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public double getX()
		{
			return x;
		}

		public double getY()
		{
			return y;
		}

		public double getZ()
		{
			return z;
		}
	}

	public enum Shape3D
	{
		RING, TUBE, CUBE, SPHERE
	}

	private List<Point3D> renderList;
	private List<Point3D> points;

	private double roll = 0;
	private double pitch = 0;
	private double yaw = 0;

	private double rollInc = 0;
	private double pitchInc = 0;
	private double yawInc = 0;

	private double xOffset = 0;
	private double yOffset = 0;
	private double zOffset = 0;

	private double zoom = 0;

	private Image imageBall;

	private Comparator<Point3D> zComparator = new Comparator<Point3D>()
	{
		@Override
		public int compare(Point3D p1, Point3D p2)
		{
			return Double.compare(p2.z, p1.z);
		}
	};

	public Dots(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
		
		customInitialise(Shape3D.CUBE, -1, -1);
	}

	@Override
	protected void initialise()
	{
		imageBall = new Image(getClass().getResourceAsStream("/glassyball.png"));
	}

	public void customInitialise(Shape3D shape, long startMillis, long stopMillis)
	{
		this.effectStartMillis = startMillis;
		this.effectStopMillis = stopMillis;

		switch (shape)
		{
		case CUBE:
			points = makeCube(12, 3.0);
			break;
		case RING:
			points = makeRing(48, 2.0);
			break;
		case SPHERE:
			points = makeSphere(5.0, 50);
			break;
		case TUBE:
			points = makeTube(48, 1.0, 16, 3.0);
			break;
		default:
			break;

		}

		itemCount = points.size();
		renderList = new ArrayList<>(points.size());
	}

	private List<Point3D> makeCube(int balls, double side)
	{
		zoom = 250;
		rollInc = 1.0;
		pitchInc = 0.5;
		yawInc = 1.5;
		zOffset = 4;

		List<Point3D> result = new ArrayList<>();

		double gap = 1.0 / (balls / 2 - 1);

		double halfSide = side / 2.0;

		for (double x = -halfSide; x <= halfSide; x += gap)
		{
			for (double y = -halfSide; y <= halfSide; y += gap)
			{
				for (double z = -halfSide; z <= halfSide; z += gap)
				{
					result.add(new Point3D(x, y, z));
				}
			}
		}

		return result;
	}

	private List<Point3D> makeRing(double balls, double radius)
	{
		zoom = 100;
		rollInc = 1.0;
		pitchInc = 0.5;
		yawInc = 1.5;
		zOffset = 2.5;

		List<Point3D> result = new ArrayList<>();

		for (double a = 0; a < 360; a += 360.0 / balls)
		{

			double x = radius * precalc.sin(a);
			double y = radius * precalc.cos(a);

			result.add(new Point3D(x, y, 0));
		}

		return result;
	}

	private List<Point3D> makeTube(double ballsPerRing, double radius, int rows, double length)
	{
		zoom = 300;
		rollInc = 1.0;
		pitchInc = 0.5;
		yawInc = 1.5;
		zOffset = 4.5;

		List<Point3D> result = new ArrayList<>();

		double gap = rows == 1 ? 1 : (1.0 / (rows - 1));

		double halfSide = length / 2.0;

		for (double z = -halfSide; z <= halfSide; z += gap)
		{
			for (double a = 0; a < 360; a += 360.0 / ballsPerRing)
			{
				double x = radius * precalc.sin(a);
				double y = radius * precalc.cos(a);

				result.add(new Point3D(x, y, z));
			}
		}

		return result;
	}

	private List<Point3D> makeSphere(double maxRadius, double rings)
	{
		zoom = 100;
		rollInc = 0.5;
		pitchInc = 0.5;
		yawInc = 0.5;
		zOffset = 5.5;

		List<Point3D> result = new ArrayList<>();

		double zAngle = 0.0;

		double zInc = 180 / rings;

		for (double r = 0; r <= rings; r++)
		{
			double z = maxRadius * precalc.cos(zAngle);

			zAngle += zInc;

			double radius = maxRadius * precalc.sin((r / rings) * 180);

			double ballsInLayer = Math.floor(radius * 16.0);

			double angleGap = 360.0 / ballsInLayer;

			for (double a = 0; a < 360; a += angleGap)
			{
				double x = radius * precalc.sin(a);
				double y = radius * precalc.cos(a);

				result.add(new Point3D(x, y, z));
			}
		}

		return result;
	}

	@Override
	public void renderBackground()
	{
		fillBackground(Color.BLACK);
	}

	@Override
	public void renderForeground()
	{
		gc.setStroke(Color.WHITE);

		roll += rollInc;
		pitch += pitchInc;
		yaw += yawInc;

		renderList.clear();

		for (int i = 0; i < points.size(); i++)
		{
			Point3D point = points.get(i);

			renderList.add(transform(point, roll, pitch, yaw, xOffset, yOffset, zOffset));
		}

		zSort(renderList);

		for (int i = 0; i < renderList.size(); i++)
		{
			Point3D point = renderList.get(i);

			drawPoint(point);
		}
	}

	private final void zSort(List<Point3D> points)
	{
		Collections.sort(points, zComparator);
	}

	private final Point3D transform(Point3D orig, double pitch, double yaw, double roll, double translateX, double translateY,
			double translateZ)
	{
		// rotate around Z axis (roll)
		double newX = orig.x * precalc.cos(roll) - orig.y * precalc.sin(roll);
		double newY = orig.x * precalc.sin(roll) + orig.y * precalc.cos(roll);
		double newZ = orig.z;

		// rotate around X axis (pitch)
		double newY2 = newY * precalc.cos(pitch) - newZ * precalc.sin(pitch);
		double newZ2 = newY * precalc.sin(pitch) + newZ * precalc.cos(pitch);
		double newX2 = newX;

		// rotate around Y axis (yaw)
		double newZ3 = newZ2 * precalc.cos(yaw) - newX2 * precalc.sin(yaw);
		double newX3 = newZ2 * precalc.sin(yaw) + newX2 * precalc.cos(yaw);
		double newY3 = newY2;

		// translate
		newX3 += translateX;
		newY3 += translateY;
		newZ3 += translateZ;

		return new Point3D(newX3, newY3, newZ3);
	}

	private final void drawPoint(Point3D point)
	{
		double x = halfWidth + point.x / point.z * zoom;
		double y = halfHeight + point.y / point.z * zoom;

		double ballSize = 24 / point.z;

		gc.drawImage(imageBall, x, y, ballSize, ballSize);
	}
}