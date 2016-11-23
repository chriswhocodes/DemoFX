/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.ray;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/*
 * Standing on the shoulders of giants.
 * This isn't my raytracer, I just converted it from C to Java 
 * with the help of this web page by Fabien Sanglard:
 * http://fabiensanglard.net/rayTracing_back_of_business_card/index.php
 * The original code is by Andrew Kensler
 */
public class JFXRay
{
	private byte[] imageData;

	private boolean[][] data;
	private int rows;
	private int cols;

	private Vector3f floorColourOdd;
	private Vector3f floorColourEven;
	private Vector3f skyColour;

	private float sphereReflectivity;

	private long renderStart = 0;
	private long renderTime = 0;

	private final Vector3f vector001 = new Vector3f(0, 0, 1);

	private int workerThreads;

	private void init(String[] lines)
	{
		cols = lines[0].length();
		rows = lines.length;

		data = new boolean[rows][cols];

		for (int r = 0; r < rows; r++)
		{
			for (int c = 0; c < cols; c++)
			{
				char ch = lines[r].charAt(c);

				data[rows - 1 - r][cols - 1 - c] = ch == '*';
			}
		}
	}

	// The intersection test for line [o,v].
	// Return 2 if a hit was found (and also return distance t and bouncing ray
	// n).
	// Return 0 if no hit was found but ray goes upward
	// Return 1 if no hit was found but ray goes downward

	// Returns object[] 0 = int (m), 1 = float (t), 2 = Vector3f n
	private Object[] test(Vector3f o, Vector3f d, Vector3f n)
	{
		float t = 1e9f;

		int mode = 0;

		float p2 = -o.getZ() / d.getZ();

		if (p2 >= .01)
		{
			t = p2;
			n = vector001;
			mode = 1;
		}

		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < cols; col++)
			{
				// For this row and column is there a sphere?
				if (data[row][col])
				{
					// There is a sphere but does the ray hit it ?

					Vector3f p = o.add(-col, 0, -row - 4);

					float b =  p.getB(d, p);
					float c =  p.getC(p);

					float q = b * b - c;

					// Does the ray hit the sphere ?
					if (q > 0)
					{
						float s = -b - (float) Math.sqrt(q);

						if (s < t && s > .01)
						{
							// So far this is the minimum distance, save
							// it. And also compute the bouncing ray
							// vector into 'n'
							t = s;
							n = (p.add(d.scale(t))).normalise();
							mode = 2;
						}
					}
				}
			}
		}
		return new Object[] { mode, t, n };
	}

	// sample the world and return the pixel color for
	// a ray passing by point o (Origin) and d (Direction)
	private Vector3f sample(Vector3f origin, Vector3f direction)
	{
		Vector3f n = new Vector3f();

		// Search for an intersection ray Vs World.
		Object[] result = test(origin, direction, n);

		int m = (int) result[0];
		float t = (float) result[1];
		n = (Vector3f) result[2];

		if (m == 0)
		{
			// No sphere found and the ray goes upward: Generate a sky color
			return skyColour.scale((float) Math.pow(1 - direction.getZ(), 4));
		}

		// A sphere was maybe hit.

		// h = intersection coordinate
		Vector3f h = origin.add(direction.scale(t));

		// 'l' = direction to light (with random delta for soft-shadows).
		Vector3f l = new Vector3f(9, 9, 16);

		l = l.add(h.negate());

		l = l.normalise();

		// r = The half-vector
		Vector3f r = direction.halfVector(n);

		// Calculated the lambertian factor
		float b = l.dot(n);

		// Calculate illumination factor (lambertian coefficient > 0 or in
		// shadow)?
		if (b < 0)
		{
			b = 0;
		}
		else
		{
			result = test(h, l, n);

			int res = (int) result[0];
			t = (float) result[1];
			n = (Vector3f) result[2];

			if (res > 0)
			{
				b = 0;
			}
		}

		if (m == 1)
		{
			return generateFloorColour(h, b);
		}
		else
		{
			// m == 2 A sphere was hit.
			// Cast an ray bouncing from the sphere surface.
			return bounceRay(r, b, h, l);
		}
	}

	private Vector3f bounceRay(Vector3f r, float b, Vector3f h, Vector3f l)
	{
		float p = 0;

		if (b > 0)
		{
			// Calculate the color 'p' with diffuse and specular component
			p = (float) Math.pow(l.dot(r), 64);
		}

		// Attenuate color since it is bouncing
		return new Vector3f(p, p, p).add(sample(h, r).scale(sphereReflectivity));
	}

	private Vector3f generateFloorColour(Vector3f h, float b)
	{
		// No sphere was hit and the ray was going downward:
		h = h.invertScale(4);

		// Generate a floor color
		int ceil = (int) (Math.ceil(h.getX()) + Math.ceil(h.getY()));

		if ((ceil & 1) == 1)
		{
			return floorColourOdd.scale(b / 4 + .1f);
		}
		else
		{
			return floorColourEven.scale(b / 4 + .1f);
		}
	}

	public byte[] getImageData()
	{
		renderTime = System.currentTimeMillis() - renderStart;
		return imageData;
	}

	public JFXRay(int workerThreads)
	{
		this.workerThreads = workerThreads;
	}

	public void render(final RenderConfig config)
	{
		renderStart = System.currentTimeMillis();

		this.floorColourOdd = config.getOddColour();
		this.floorColourEven = config.getEvenColour();
		this.skyColour = config.getSkyColour();
		this.sphereReflectivity = config.getSphereReflectivity();

		if (data == null)
		{
			init(config.getLines());
			imageData = new byte[config.getImageWidth() * config.getImageHeight() * 3];
		}

		// Camera direction
		final Vector3f g = config.getCamDirection().normalise();

		// Camera up vector...Seem Z is pointing up :/ WTF !
		final Vector3f a = vector001.cross(g).normalise().scale(.003f);

		// The right vector, obtained via traditional cross-product
		final Vector3f b = g.cross(a).normalise().scale(.003f);

		// WTF ? See https://news.ycombinator.com/item?id=6425965 for more.
		final Vector3f c = a.add(b).scale(-256).add(g);

		final int linesPerThread = config.getImageHeight() / workerThreads;

		// System.out.println("LinesPerThread: " + linesPerThread);

		final Vector3f defaultPixelColour = new Vector3f(16, 16, 16);
		
		// A little bit of delta up/down and left/right
		final Vector3f t = a.add(b);

		for (int i = 0; i < workerThreads; i++)
		{
			final int startingLine = config.getImageHeight() - 1 - (i * linesPerThread);
			final int pixelBufferOffset = i * linesPerThread;

			Thread thread = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					int pixel = config.getImageWidth() * pixelBufferOffset * 3;

					// For each line
					for (int y = startingLine; y > startingLine - linesPerThread; y--)
					{
						// For each pixel in a line
						for (int x = config.getImageWidth() - 1; x >= 0; x--)
						{
							// Reuse the vector class to store not XYZ but an
							// RGB
							// pixel color
							// Default pixel color is almost pitch black
							Vector3f p = defaultPixelColour;

							// The delta to apply to the origin of the view
							// (For Depth of View blur).

							// Set the camera focal point and
							// Cast the ray
							// Accumulate the color returned in the p
							// variable
							// Ray Direction with random deltas for
							// stochastic sampling

							Vector3f dirA = a.scale(x);
							Vector3f dirB = b.scale(y);
							Vector3f dirC = dirA.add(dirB).add(c);

							Vector3f dir = t.negate().add(dirC.scale(16)).normalise();

							// Ray Origin +p for color accumulation
							p = sample(config.getRayOrigin().add(t), dir).scale(config.getBrightness()).add(p);

							imageData[pixel++] = (byte) p.getX();
							imageData[pixel++] = (byte) p.getY();
							imageData[pixel++] = (byte) p.getZ();
						}
					}
				}
			});
			
			thread.start();
			
			try
			{
				thread.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}

		renderTime = System.currentTimeMillis() - renderStart;
	}

	public long getRenderTime()
	{
		return renderTime;
	}
}
