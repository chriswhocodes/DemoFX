/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.real3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class TexturedSphere extends AbstractEffect
{
	private Sphere ball;

	private PhongMaterial material;

	protected double radius;

	protected WritableImage imageTexture;
	protected int textureWidth;
	protected int textureHeight;

	private Rotate rotateX;
	private Rotate rotateY;

	private double angleX;
	private double angleY;

	private double rotateAngleX;
	private double rotateAngleY;
	
	public TexturedSphere(DemoConfig config)
	{
		super(config);

		WritableImage earthImage = ImageUtil.loadWritableImageFromResources("/earth.jpg"); // https://www.evl.uic.edu/pape/data/Earth/

		init(earthImage, height / 3, 0, 1, halfWidth, halfHeight);
	}

	public TexturedSphere(DemoConfig config, WritableImage textureImage, double radius, double rotateAngleX, double rotateAngleY, double translateX, double translateY)
	{
		super(config);
		
		// http://answers.unity3d.com/questions/544279/recommended-sphere-texture-size.html
		
		init(textureImage, radius, rotateAngleX, rotateAngleY, translateX, translateY);
	}

	private void init(WritableImage image, double radius, double rotateAngleX, double rotateAngleY, double translateX, double translateY)
	{
		this.radius = radius;

		imageTexture = image;

		this.rotateAngleX = rotateAngleX;
		this.rotateAngleY = rotateAngleY;

		ball = new Sphere(radius);

		material = new PhongMaterial();
		material.setDiffuseMap(imageTexture);

		ball.setMaterial(material);

		rotateX = new Rotate(0, Rotate.X_AXIS);
		rotateY = new Rotate(0, Rotate.Y_AXIS);

		ball.getTransforms().add(rotateY);
		ball.getTransforms().add(rotateX);

		ball.setTranslateX(translateX);
		ball.setTranslateY(translateY);
	}
	
	@Override
	public void start()
	{
		group.getChildren().add(ball);
	}
	
	@Override
	public void stop()
	{
		group.getChildren().remove(ball);
	}

	@Override
	public void renderForeground()
	{
		angleX += rotateAngleX;

		if (rotateAngleX >= 360)
		{
			rotateAngleX -= 360;
		}

		angleY += rotateAngleY;

		if (rotateAngleY >= 360)
		{
			rotateAngleY -= 360;
		}

		rotateX.setAngle(angleX);
		rotateY.setAngle(angleY);

		// ball.setTranslateY(halfWidth + halfWidth / 4 * precalc.sin(angle));
	}
}