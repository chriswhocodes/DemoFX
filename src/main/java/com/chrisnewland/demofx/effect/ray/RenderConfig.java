/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.ray;

public final class RenderConfig
{
	private int imageWidth;
	private int imageHeight;
	private int rays;
	private String[] lines;
	private int threads=1;
	private Vector3f rayOrigin;
	private Vector3f camDirection;
	private Vector3f oddColour;
	private Vector3f evenColour;
	private Vector3f skyColour;
	private float sphereReflectivity;
	private float brightness;

	public final int getImageWidth()
	{
		return imageWidth;
	}

	public final void setImageWidth(int imageWidth)
	{
		this.imageWidth = imageWidth;
	}

	public final int getImageHeight()
	{
		return imageHeight;
	}

	public final void setImageHeight(int imageHeight)
	{
		this.imageHeight = imageHeight;
	}

	public final int getRays()
	{
		return rays;
	}

	public final void setRays(int rays)
	{
		this.rays = rays;
	}

	public final String[] getLines()
	{
		return lines;
	}

	public final void setLines(String[] lines)
	{
		this.lines = lines;
	}

	public final int getThreads()
	{
		return threads;
	}

	public final void setThreads(int threads)
	{
		this.threads = threads;
	}

	public final Vector3f getRayOrigin()
	{
		return rayOrigin;
	}

	public final void setRayOrigin(Vector3f rayOrigin)
	{
		this.rayOrigin = rayOrigin;
	}

	public final Vector3f getCamDirection()
	{
		return camDirection;
	}

	public final void setCamDirection(Vector3f camDirection)
	{
		this.camDirection = camDirection;
	}

	public final Vector3f getOddColour()
	{
		return oddColour;
	}

	public final void setOddColour(Vector3f oddColour)
	{
		this.oddColour = oddColour;
	}

	public final Vector3f getEvenColour()
	{
		return evenColour;
	}

	public final void setEvenColour(Vector3f evenColour)
	{
		this.evenColour = evenColour;
	}

	public final Vector3f getSkyColour()
	{
		return skyColour;
	}

	public final void setSkyColour(Vector3f skyColour)
	{
		this.skyColour = skyColour;
	}

	public final float getSphereReflectivity()
	{
		return sphereReflectivity;
	}

	public final void setSphereReflectivity(float sphereReflectivity)
	{
		this.sphereReflectivity = sphereReflectivity;
	}

	public final float getBrightness()
	{
		return brightness;
	}

	public final void setBrightness(float brightness)
	{
		this.brightness = brightness;
	}
}
