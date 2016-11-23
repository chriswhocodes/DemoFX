/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.util.PreCalc;

import javafx.scene.image.Image;

public class Object3D
{
	private PreCalc precalc;

	private double roll = 0;
	private double pitch = 0;
	private double yaw = 0;

	private double rollInc = 0;
	private double pitchInc = 0;
	private double yawInc = 0;

	private double xOffset = 0;
	private double yOffset = 0;
	private double zOffset = 0;

	private int transformAfterFrames = 0;
	private int frameCount = 0;

	private double zoom = 0;

	private double maxImageSize = 24.0;
	private Image image;

	private List<P3D> points = new ArrayList<>();

	public Image getImage()
	{
		return image;
	}

	public Object3D setImage(Image image)
	{
		this.image = image;
		return this;
	}

	public double getMaxImageSize()
	{
		return maxImageSize;
	}

	public Object3D setMaxImageSize(double maxImageSize)
	{
		this.maxImageSize = maxImageSize;
		return this;
	}

	public void addPoint(double x, double y, double z)
	{
		P3D point = new P3D(x, y, z, this);

		if (!points.contains(point))
		{
			points.add(point);
		}
	}

	public Object3D(PreCalc precalc)
	{
		this.precalc = precalc;
	}

	public Object3D setRoll(double roll)
	{
		this.roll = roll;
		return this;
	}

	public Object3D setPitch(double pitch)
	{
		this.pitch = pitch;
		return this;
	}

	public Object3D setYaw(double yaw)
	{
		this.yaw = yaw;
		return this;
	}

	public Object3D setRollInc(double rollInc)
	{
		this.rollInc = rollInc;
		return this;
	}

	public Object3D setPitchInc(double pitchInc)
	{
		this.pitchInc = pitchInc;
		return this;
	}

	public Object3D setYawInc(double yawInc)
	{
		this.yawInc = yawInc;
		return this;
	}

	public Object3D setxOffset(double xOffset)
	{
		this.xOffset = xOffset;
		return this;
	}

	public Object3D setyOffset(double yOffset)
	{
		this.yOffset = yOffset;
		return this;
	}

	public Object3D setzOffset(double zOffset)
	{
		this.zOffset = zOffset;
		return this;
	}

	public Object3D setZoom(double zoom)
	{
		this.zoom = zoom;
		return this;
	}

	public void setTransformAfterFrames(int transformAfterFrames)
	{
		this.transformAfterFrames = transformAfterFrames;
	}

	public int getTransformAfterFrames()
	{
		return transformAfterFrames;
	}

	public int getFrameCount()
	{
		return frameCount;
	}

	public double getRoll()
	{
		return roll;
	}

	public double getPitch()
	{
		return pitch;
	}

	public double getYaw()
	{
		return yaw;
	}

	public double getRollInc()
	{
		return rollInc;
	}

	public double getPitchInc()
	{
		return pitchInc;
	}

	public double getYawInc()
	{
		return yawInc;
	}

	public double getxOffset()
	{
		return xOffset;
	}

	public double getyOffset()
	{
		return yOffset;
	}

	public double getzOffset()
	{
		return zOffset;
	}

	public double getZoom()
	{
		return zoom;
	}

	public List<P3D> getPoints()
	{
		return points;
	}

	public List<P3D> transform()
	{
		List<P3D> result = new ArrayList<>(points.size());

		if (frameCount > transformAfterFrames)
		{
			roll += rollInc;
			pitch += pitchInc;
			yaw += yawInc;
		}

		frameCount++;

		for (int i = 0; i < points.size(); i++)
		{
			P3D point = points.get(i);

			if (frameCount > transformAfterFrames)
			{
				point = transform(point, roll, pitch, yaw, xOffset, yOffset, zOffset);
			}
			else
			{
				point = transform(point, 0, 0, 0, xOffset, yOffset, zOffset);
			}

			result.add(point);
		}

		return result;
	}

	private final P3D transform(P3D orig, double pitch, double yaw, double roll, double translateX, double translateY,
			double translateZ)
	{
		// rotate around Z axis (roll)
		double newX = orig.getX() * precalc.cos(roll) - orig.getY() * precalc.sin(roll);
		double newY = orig.getX() * precalc.sin(roll) + orig.getY() * precalc.cos(roll);
		double newZ = orig.getZ();

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

		return new P3D(newX3, newY3, newZ3, this);
	}
}