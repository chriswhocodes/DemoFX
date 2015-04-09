/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

/*
 * procedure 3Dto2D (x, y, z, pan, centre, position)

 x = x + position.x
 y = y + position.y
 z = z + position.z

 new.x = x*cos(pan.x) - z*sin(pan.x)
 new.z = x*sin(pan.x) + z*cos(pan.x)
 new.y = y*cos(pan.y) - new.z*sin(pan.y)

 z = new.y*cos(pan.y) - new.z*sin(pan.y)
 x = new.x*cos(pan.z) - new.y*sin(pan.z)
 y = new.x*sin(pan.z) + new.y*cos(pan.z)

 if z > 0 then

 screen.x = x / z * zoom + centre.x
 screen.y = y / z * zoom + centre.y

 end if
 */

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

	private List<Point3D> points;

	public Dots(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		points = new ArrayList<>();

		points.add(new Point3D(-1, -1, 1));
		points.add(new Point3D(-1, 1, 1));
		points.add(new Point3D(1, -1, 1));
		points.add(new Point3D(1, 1, 1));
		
		points.add(new Point3D(-1, -1, 2));
		points.add(new Point3D(-1, 1, 2));
		points.add(new Point3D(1, -1, 2));
		points.add(new Point3D(1, 1, 2));
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

		for (int i = 0; i < points.size(); i++)
		{
			Point3D point = points.get(i);

			drawPoint(point);
		}
	}

	private final void drawPoint(Point3D point)
	{
		double x = halfWidth + point.x*halfWidth/2 / point.z;
		double y = halfHeight + point.y*halfHeight/2 / point.z;

		gc.strokeOval(x, y, 8, 8);
	}

}