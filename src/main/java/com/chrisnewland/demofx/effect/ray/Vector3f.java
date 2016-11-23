/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.ray;

public class Vector3f
{
	private final float x, y, z; // Vector has three float attributes.

	private float dotThis = -1;

	// Constructor
	public Vector3f(float a, float b, float c)
	{
		x = a;
		y = b;
		z = c;
	}

	public Vector3f()
	{
		x = 0;
		y = 0;
		z = 0;
	}

	// Vector add
	public Vector3f add(Vector3f r)
	{
		return new Vector3f(x + r.x, y + r.y, z + r.z);
	}

	public Vector3f add(float a, float b, float c)
	{
		return new Vector3f(x + a, y + b, z + c);
	}

	public float getB(Vector3f p, Vector3f d)
	{
		return p.x * d.x + p.y * d.y + p.z * d.z;
	}
	
	public float getC(Vector3f p)
	{
		return p.x * p.x + p.y * p.y + p.z * p.z - 1;
	}

	// Vector scaling
	public Vector3f scale(float r)
	{
		return new Vector3f(x * r, y * r, z * r);
	}

	public Vector3f negate()
	{
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f halfVector(Vector3f n)
	{
		float dotProduct = (n.x * x * -2f) + (n.y * y * -2f) + (n.z * z * -2f);

		return add(n.scale(dotProduct));
	}

	public Vector3f invertScale(float r)
	{
		return new Vector3f(x / r, y / r, z / r);
	}

	// Vector dot product
	public float dot(Vector3f r)
	{
		return x * r.x + y * r.y + z * r.z;
	}

	// Vector dot product
	public float dotThis()
	{
		if (dotThis == -1)
		{
			dotThis = x * x + y * y + z * z;
		}

		return dotThis;
	}

	// Cross-product
	public Vector3f cross(Vector3f r)
	{
		return new Vector3f(y * r.z - z * r.y, z * r.x - x * r.z, x * r.y - y * r.x);
	}

	// Used later for normalizing the vector
	public Vector3f normalise()
	{
		float factor = 1f / (float) Math.sqrt(dotThis());

		return scale(factor);
	}

	@Override
	public String toString()
	{
		return x + "  " + y + "  " + z;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}
}