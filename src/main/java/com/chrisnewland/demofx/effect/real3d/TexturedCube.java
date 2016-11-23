/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.real3d;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class TexturedCube extends AbstractEffect
{
	private Box box;

	private PhongMaterial material;

	protected double side;

	private double angle = 0;

	protected WritableImage imageTexture;

	private Rotate rotateY;
	private Rotate rotateX;

	public TexturedCube(DemoConfig config)
	{
		super(config);
		
		WritableImage earthImage = ImageUtil.loadWritableImageFromResources("/earth.jpg"); // https://www.evl.uic.edu/pape/data/Earth/
		
		init(earthImage, 256, 1, 1);
	}
	
	public TexturedCube(DemoConfig config, double side, double rotateAngleX, double rotateAngleY)
	{
		super(config);
		
		WritableImage image = new WritableImage((int)side, (int)side);
		
		init(image, side, rotateAngleX, rotateAngleY);
	}
	
	private void init(WritableImage image, double side, double rotateAngleX, double rotateAngleY)
	{
		this.side = side;

		imageTexture = image;
			
		rotateX = new Rotate(angle, Rotate.X_AXIS);
		rotateY = new Rotate(angle, Rotate.Y_AXIS);
		
		material = new PhongMaterial(Color.WHITE);
		material.setDiffuseMap(imageTexture);
		
		box = new Box(side, side, side);

		box.setMaterial(material);
		
		box.getTransforms().add(rotateY);
		box.getTransforms().add(rotateX);

		box.setTranslateX(halfWidth);
		box.setTranslateY(halfHeight);
	}
	
	@Override
	public void start()
	{
		group.getChildren().add(box);	
	}
	
	@Override
	public void stop()
	{
		group.getChildren().remove(box);
	}

	@Override
	public void renderForeground()
	{
		angle++;

		if (angle >= 360)
		{
			angle -= 360;
		}

		rotateX.setAngle(angle);
		rotateY.setAngle(angle);

		box.setTranslateX(halfWidth + halfWidth / 4 * precalc.sin(angle));
		box.setTranslateZ(100 + halfWidth / 2 * precalc.cos(angle));
	}
}