/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.sprite;

import java.awt.Point;
import java.util.List;

public class PathData
{
	private int length;
	private int index = 0;
	private int[] x;
	private int[] y;
	private int offsetX;
	private int offsetY;
	private int followers;
	private int followDelay;
	private int showFollowers = 0;
	private boolean looped = false;

	/*
	if (path.isLooped())
	{
		path.showFollowers = path.followers;
	}
	else
	{
		path.showFollowers = path.index / path.followDelay;

		if (path.showFollowers > path.followers)
		{
			path.looped = true;
			path.showFollowers = path.followers;
		}
	}
	 */
	public PathData(List<Point> points, int offsetX, int offsetY, int followers, int followDelay)
	{
		length = points.size();

		x = new int[length];
		y = new int[length];

		this.offsetX = offsetX;
		this.offsetY = offsetY;

		this.followers = followers;
		this.showFollowers = followers;
		this.followDelay = followDelay;

		System.out.println("f: " + followers);

		int pos = 0;

		for (Point p : points)
		{
			x[pos] = p.x;
			y[pos] = p.y;
			pos++;
		}
	}

	public int getLength()
	{
		return length;
	}

	public int getIndex()
	{
		return index;
	}

	public void incIndex()
	{
		index++;

		if (index >= length)
		{
			index = 0;
		}
	}

	public int[] getX()
	{
		return x;
	}

	public int[] getY()
	{
		return y;
	}

	public int getOffsetX()
	{
		return offsetX;
	}

	public int getOffsetY()
	{
		return offsetY;
	}

	public int getFollowers()
	{
		return followers;
	}

	public int getFollowDelay()
	{
		return followDelay;
	}

	public int getShowFollowers()
	{
		return showFollowers;
	}

	public boolean isLooped()
	{
		return looped;
	}


}
