/*
 * Copyright (c) 2015-2017 Chris Newland.
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
	
	private double xAmplitude;
	private double zAmplitude;
	
	private double translateX;
	
	public TexturedCube(DemoConfig config)
	{
		super(config);
		
		zAmplitude = 100 + halfWidth / 2;
		xAmplitude = halfWidth / 4;
		
		WritableImage earthImage = ImageUtil.loadWritableImageFromResources("/earth.jpg"); // https://www.evl.uic.edu/pape/data/Earth/
		
		init(earthImage, 256, 1, 1, halfWidth, halfHeight);
	}
	
	public TexturedCube(DemoConfig config, WritableImage textureImage, double side, double rotateAngleX, double rotateAngleY, double translateX, double translateY, double xAmplitude, double zAmplitude)
	{
		super(config);
				
		this.xAmplitude = xAmplitude;
		this.zAmplitude = zAmplitude;
		
		init(textureImage, side, rotateAngleX, rotateAngleY, translateX, translateY);
	}
	
	public TexturedCube(DemoConfig config, double side, double rotateAngleX, double rotateAngleY)
	{
		super(config);
		
		zAmplitude = 100 + halfWidth / 2;
		
		WritableImage image = new WritableImage((int)side, (int)side);
		
		init(image, side, rotateAngleX, rotateAngleY, halfWidth, halfHeight);
	}
	
	private void init(WritableImage image, double side, double rotateAngleX, double rotateAngleY, double translateX, double translateY)
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

		this.translateX = translateX;
		
		box.setTranslateX(translateX);
		box.setTranslateY(translateY);
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

		box.setTranslateX(translateX + xAmplitude * precalc.sin(angle));
		box.setTranslateZ(zAmplitude * precalc.cos(angle));
	}
}