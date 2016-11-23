/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.util.BallGrid;
import com.chrisnewland.demofx.util.PreCalc;
import com.chrisnewland.demofx.util.TextUtil;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fake3DShapeFactory
{
	private PreCalc precalc;
	private Image image;

	public Fake3DShapeFactory(PreCalc precalc, Image image)
	{
		this.precalc = precalc;
		this.image = image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public Object3D makeSolidCube(int balls, double side)
	{
		double zoom = 250;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 4;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		double gap = 1.0 / (balls / 2 - 1);

		double halfSide = side / 2.0;

		for (double x = -halfSide; x <= halfSide; x += gap)
		{
			for (double y = -halfSide; y <= halfSide; y += gap)
			{
				for (double z = -halfSide; z <= halfSide; z += gap)
				{
					obj3D.addPoint(x, y, z);
				}
			}
		}

		return obj3D;
	}

	public Object3D makeWireframeCube(int balls, double side)
	{
		double zoom = 250;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 4;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		double gap = 1.0 / ((double)balls / 2.0 - 1.0);

		double halfSide = side / 2.0;

		for (double p = -halfSide; p <= halfSide + gap / 2; p += gap)
		{
			obj3D.addPoint(p, -halfSide, -halfSide);
			obj3D.addPoint(p, -halfSide, halfSide);
			obj3D.addPoint(p, halfSide, -halfSide);
			obj3D.addPoint(p, halfSide, halfSide);

			obj3D.addPoint(-halfSide, p, -halfSide);
			obj3D.addPoint(-halfSide, p, halfSide);
			obj3D.addPoint(halfSide, p, -halfSide);
			obj3D.addPoint(halfSide, p, halfSide);

			obj3D.addPoint(-halfSide, -halfSide, p);
			obj3D.addPoint(-halfSide, halfSide, p);
			obj3D.addPoint(halfSide, -halfSide, p);
			obj3D.addPoint(halfSide, halfSide, p);
		}

		return obj3D;
	}

	public Object3D makeFromFile(String name)
	{
		// http:www.hongkiat.com/blog/60-excellent-free-3d-model-websites/

		double zoom = 450;
		double rollInc = 1;
		double pitchInc = 1;
		double yawInc = 0;
		double zOffset = 2.5;
		double maxImageSize = 16;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setMaxImageSize(maxImageSize);
		obj3D.setImage(image);

		List<String> lines = readFileToLines(name);

		for (String line : lines)
		{
			String[] parts = line.split(" ");

			if (parts.length == 3)
			{
				try
				{
					double x = Double.parseDouble(parts[0]);
					double y = -Double.parseDouble(parts[1]);
					double z = -Double.parseDouble(parts[2]);

					obj3D.addPoint(x, y, z);
				}
				catch (NumberFormatException nfe)
				{
					nfe.printStackTrace();
				}
			}
		}

		return obj3D;
	}

	public Object3D makeFromTextString(String str, GraphicsContext gc)
	{
		BallGrid grid = TextUtil.createBallGrid(str, gc, "Arial", 100, 5);

		double rollInc = 1.2;
		double pitchInc = 1.2;
		double yawInc = 0;

		double zoom = 600;
		double zOffset = 0.75;
		double maxImageSize = 7;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setZoom(zoom);
		obj3D.setzOffset(zOffset);
		obj3D.setMaxImageSize(maxImageSize);
		obj3D.setImage(image);

		int gridWidth = grid.getWidth();
		int gridHeight = grid.getHeight();

		for (int x = 0; x < gridWidth; x++)
		{
			for (int y = 0; y < gridHeight; y++)
			{
				if (grid.isSet(x, y))
				{
					double xPos = ((double) x / (double) gridWidth); // 0..1
					double yPos = ((double) y / (double) gridHeight); // 0..1

					xPos -= 0.5; // -0.5 .. +0.5
					yPos -= 0.5; // -0.5 .. +0.5

					yPos /= 4;

					obj3D.addPoint(xPos, yPos, -0.030);
					obj3D.addPoint(xPos, yPos, -0.015);
					obj3D.addPoint(xPos, yPos, 0);
				}
			}
		}

		return obj3D;
	}

	public List<String> readFileToLines(String path)
	{
		List<String> obj3D = new ArrayList<>();

		InputStream is = Fake3DShapeFactory.class.getResourceAsStream(path);

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = null;

		try
		{
			while ((line = reader.readLine()) != null)
			{
				obj3D.add(line);
			}

			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return obj3D;
	}

	public Object3D makeRing(double balls, double radius)
	{
		double zoom = 100;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 2.5;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		for (double a = 0; a < 360; a += 360.0 / balls)
		{
			double x = radius * precalc.sin(a);
			double y = radius * precalc.cos(a);

			obj3D.addPoint(x, y, 0);
		}

		return obj3D;
	}

	public Object3D makeTube(double ballsPerRing, double radius, int rows, double length)
	{
		double zoom = 300;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 4.5;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		double gap = (rows == 1) ? 1 : (1.0 / (rows - 1));

		double halfSide = length / 2.0;

		for (double z = -halfSide; z <= halfSide; z += gap)
		{
			for (double a = 0; a < 360; a += 360.0 / ballsPerRing)
			{
				double x = radius * precalc.sin(a);
				double y = radius * precalc.cos(a);

				obj3D.addPoint(x, y, z);
			}
		}

		return obj3D;
	}

	public Object3D makeSpring(double ballsPerRing, double radius, int rows, double length)
	{
		double zoom = 300;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 4.5;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		double gap = (rows == 1) ? 1 : (1.0 / (rows - 1));

		double halfSide = length;
		;

		double angle = 0;

		for (double z = -halfSide; z <= halfSide; z += gap / 4)
		{
			angle += 10;

			double x = radius * precalc.sin(angle);
			double y = radius * precalc.cos(angle);

			obj3D.addPoint(x, y, z);

		}

		return obj3D;
	}

	public Object3D makeSpike(double radius)
	{
		double zoom = 300;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 4;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		double gap = radius / 16;

		double halfSide = radius;

		for (double x = -halfSide; x <= halfSide; x += gap)
		{
			obj3D.addPoint(x, x, 0);
			obj3D.addPoint(x, -x, 0);

			obj3D.addPoint(0, x, 0);
			obj3D.addPoint(x, 0, 0);

			obj3D.addPoint(0, 0, x);
			obj3D.addPoint(-x, 0, x);
			obj3D.addPoint(x, 0, x);
		}

		return obj3D;
	}

	public Object3D makeStar(double radius)
	{
		double zoom = 300;
		double rollInc = 1.0;
		double pitchInc = 0.5;
		double yawInc = 1.5;
		double zOffset = 4;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

		double points = 5;

		double angle = 360 / points;

		int steps = 10;

		double theta = 0;

		double outer = radius;
		double inner = radius * 0.5;

		double lastX = 0;
		double lastY = 0;

		for (int i = 0; i < 1+points * 2; i++)
		{
			theta += angle / 2;

			double r = (i % 2 == 0) ? outer : inner;

			double newX = r * precalc.sin(theta);
			double newY = r * precalc.cos(theta);

			if (i > 0)
			{
				addLine(obj3D, lastX, lastY, newX, newY, steps);
			}

			lastX = newX;
			lastY = newY;
		}

		return obj3D;
	}

	private void addLine(Object3D obj3D, double x1, double y1, double x2, double y2, int steps)
	{
		double px = x1;
		double py = y1;

		double dx = (x2 - x1) / steps;
		double dy = (y2 - y1) / steps;

		for (int p = 0; p < steps; p++)
		{
			obj3D.addPoint(px, py, 0);

			px += dx;
			py += dy;
		}
	}

	public Object3D makeSphere(double maxRadius, double rings)
	{
		double zoom = 100;
		double rollInc = 0.5;
		double pitchInc = 0.5;
		double yawInc = 0.5;
		double zOffset = 5.5;

		Object3D obj3D = new Object3D(precalc);

		obj3D.setZoom(zoom);
		obj3D.setRollInc(rollInc);
		obj3D.setPitchInc(pitchInc);
		obj3D.setYawInc(yawInc);
		obj3D.setzOffset(zOffset);
		obj3D.setImage(image);

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

				obj3D.addPoint(x, y, z);
			}
		}

		return obj3D;
	}
}
