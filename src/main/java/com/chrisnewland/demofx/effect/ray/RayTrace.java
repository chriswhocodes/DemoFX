/*
 * Copyright (c) 2015-2017 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.ray;

import java.nio.ByteBuffer;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;

public class RayTrace extends AbstractEffect
{
	private int rayImageWidth;
	private int rayImageHeight;
	private RenderConfig renderConfig;
	private JFXRay raytracer;
	private PixelFormat<ByteBuffer> pixelFormat = PixelFormat.getByteRgbInstance();

	private PixelWriter pixelWriter;
	private WritableImage image;

	private String[] lines = new String[]
	{
			"*       * ***** ****    ****   *     *",
			"* *   * * *     *   *   *   *    * *  ",
			"*   *   * ****  *   *   *   *     *   ",
			"*       * *     *****   *****     *   ",
			"*       * *     *    *  *    *    *   ",
			"*       * ***** *     * *     *   *   ",
			"                                      ",
			"**** *     * *       *     *    ******",
			"*     *   *  * *   * *   *  *   *     ",
			"****   * *   *   *   *  ******  ***** ",
			"*       *    *       * *      *     * ",
			"*      * *   *       * *      *     * ",
			"*     *   *  *       * *      * ***** "
	};

	public RayTrace(DemoConfig config)
	{
		super(config);

		render();
	}

	private void render()
	{
		renderConfig = new RenderConfig();

		renderConfig.setLines(lines);

		rayImageWidth = (int) width;
		rayImageHeight = (int) height;

		renderConfig.setImageWidth(rayImageWidth);
		renderConfig.setImageHeight(rayImageHeight);

		int cores = Runtime.getRuntime().availableProcessors();

		renderConfig.setCamDirection(new Vector3f(0, -8, 0));
		renderConfig.setEvenColour(new Vector3f(3, 0, 0));
		renderConfig.setOddColour(new Vector3f(3, 3, 3));
		renderConfig.setRayOrigin(new Vector3f(8, 20, 12)); // zoom
		renderConfig.setSkyColour(new Vector3f(.4f, .4f, 1f));
		renderConfig.setSphereReflectivity(0.8f);
		renderConfig.setRays(2);
		renderConfig.setBrightness(200);

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
		gc.drawImage(image, 0, 8, width, height);
	}
}