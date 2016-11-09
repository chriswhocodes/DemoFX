/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.ray;

import java.nio.ByteBuffer;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.TextUtil;

public class RayTrace extends AbstractEffect
{
	private int rayImageWidth;
	private int rayImageHeight;
	private RenderConfig renderConfig;
	private JFXRay raytracer;
	private PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();

	private PixelWriter pixelWriter;
	private WritableImage image;

	public RayTrace(DemoConfig config)
	{
		super(config);

		renderConfig = new RenderConfig();

		String[] lines = new String[] {
				"****                        **** *     *",
				"*   *  ***    *   *    ***  *     *   * ",
				"*   * *   *  * * * *  *   * ****   * *  ",
				"*   * ****  *   *   * *   * *       *   ",
				"*   * *     *       * *   * *      * *  ",
				"****   ***  *       *  ***  *     *   * " };

		renderConfig.setLines(lines);

		rayImageWidth = (int) width;/// 2;
		rayImageHeight = (int) height;/// 2;

		renderConfig.setImageWidth(rayImageWidth);
		renderConfig.setImageHeight(rayImageHeight);

		int cores = Runtime.getRuntime().availableProcessors();

		renderConfig.setCamDirection(new Vector3f(-2, -12, 0));
		renderConfig.setEvenColour(new Vector3f(3, 1, 1));
		renderConfig.setOddColour(new Vector3f(3, 3, 3));
		renderConfig.setRayOrigin(new Vector3f(8, 20, 12)); // zoom
		renderConfig.setSkyColour(new Vector3f(.4f, .4f, 1f));
		renderConfig.setSphereReflectivity(0.5f);
		renderConfig.setRays(1);
		renderConfig.setBrightness(150);

		image = new WritableImage(rayImageWidth, rayImageHeight);

		pixelWriter = image.getPixelWriter();

		raytracer = new JFXRay(cores - 1);

		raytracer.render(renderConfig);

		pixelWriter.setPixels(0, 0, rayImageWidth, rayImageHeight, pixelFormat, raytracer.getImageData(), 0,
				renderConfig.getImageWidth() * 3);
	}

	@Override
	public void renderForeground()
	{
		// zoom += 0.1;
		// renderConfig.setRayOrigin(new Vector3f(16,18, zoom)); // zoom
		//// renderConfig.setCamDirection(new Vector3f(-2, -zoom, 0));

		gc.drawImage(image, 0, 0, width, height);
	}
}