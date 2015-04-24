/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class Rings extends AbstractEffect
{
	class Ring
	{
		private Ring child;
		private double radius;
		private double thickness;
		private double angle;
		private double angleInc;
		private Color color;

		public Ring(Ring child, double radius, double thickness, double angleInc, Color color)
		{
			this.child = child;
			this.radius = radius;
			this.thickness = thickness;
			this.angleInc = angleInc;
			this.color = color;
		}

		public double getRadius()
		{
			return radius;
		}

		public double getThickness()
		{
			return thickness;
		}

		public double getAngle()
		{
			return angle;
		}

		public void incAngle()
		{
			angle += angleInc;

			if (angle >= 360)
			{
				angle -= 360;
			}

			if (child != null)
			{
				child.incAngle();
			}
		}

		public void plot(double x, double y)
		{
			drawRing(x, y, radius, thickness, color);

			if (child != null)
			{
				double childX = x + (radius - thickness - child.getRadius()) * precalc.sin(child.getAngle());
				double childY = y + (radius - thickness - child.getRadius()) * precalc.cos(child.getAngle());

				child.plot(childX, childY);
			}
		}
	}

	private Ring master;

	public Rings(GraphicsContext gc, DemoConfig config)
	{
		super(gc, config);
	}

	@Override
	protected void initialise()
	{
		Ring ring8 = new Ring(null, 40, 10, 10, Color.DODGERBLUE);

		Ring ring7 = new Ring(ring8, 70, 10, 9, Color.INDIGO);

		Ring ring6 = new Ring(ring7, 110, 10, 8, Color.ROYALBLUE);

		Ring ring5 = new Ring(ring6, 150, 10, 7, Color.AQUAMARINE);

		Ring ring4 = new Ring(ring5, 180, 10, 6, Color.CORNFLOWERBLUE);

		Ring ring3 = new Ring(ring4, 220, 15, 5, Color.DARKSLATEBLUE);

		Ring ring2 = new Ring(ring3, 260, 20, 4, Color.TEAL);

		master = new Ring(ring2, 300, 30, 3, Color.BLUEVIOLET);

		itemCount = 8;
	}

	
	@Override
	public void renderBackground()
	{
		fillBackground(Color.MIDNIGHTBLUE);
	}	
	
	@Override
	public void renderForeground()
	{
		master.incAngle();

		double rx = halfWidth + 150 * precalc.sin(master.getAngle());
		double ry = halfHeight + 80 * precalc.cos(master.getAngle());

		master.plot(rx, ry);
	}

	private final void drawRing(double x, double y, double radius, double thickness, Color color)
	{
		gc.setFill(color);
		gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);

		radius -= thickness;

		gc.setFill(Color.BLACK);
		gc.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}
}
