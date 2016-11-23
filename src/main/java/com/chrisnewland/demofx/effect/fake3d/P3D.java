/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

public class P3D implements Comparable<P3D>
{
	private double x;
	private double y;
	private double z;
	private Object3D parent;

	public P3D(double x, double y, double z, Object3D parent)
	{
		this.x = x;
		this.y = y;
		this.z = z;

		this.parent = parent;
	}

	public Object3D getParent()
	{
		return parent;
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

	@Override
	public int compareTo(P3D o)
	{
		return Double.compare(z, o.z);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null)
		{
			return false;
		}
		
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		P3D other = (P3D) obj;
		
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
		{
			return false;
		}
		
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
		{
			return false;
		}
		
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
		{
			return false;
		}
		
		return true;
	}
}