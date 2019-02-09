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
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

import javafx.scene.shape.DrawMode;

public class Mesh extends AbstractEffect
{
	private MeshView pyramid;

	private PhongMaterial material;

	protected double side;

	private double angle = 0;

	protected WritableImage imageTexture;

	private Rotate rotateY;
	private Rotate rotateX;

//	private double xAmplitude;
//	private double zAmplitude;
//
//	private double translateX;

	public Mesh(DemoConfig config)
	{
		super(config);

		WritableImage earthImage = ImageUtil.loadWritableImageFromResources(
				"/earth.jpg"); // https://www.evl.uic.edu/pape/data/Earth/

		init(earthImage, 256, 1, 1, halfWidth, halfHeight);
	}

	public Mesh(DemoConfig config, WritableImage textureImage, double side, double rotateAngleX, double rotateAngleY,
			double translateX, double translateY, double xAmplitude, double zAmplitude)
	{
		super(config);

//		this.xAmplitude = xAmplitude;
//		this.zAmplitude = zAmplitude;

		init(textureImage, side, rotateAngleX, rotateAngleY, translateX, translateY);
	}

	public Mesh(DemoConfig config, double side, double rotateAngleX, double rotateAngleY)
	{
		super(config);

		//zAmplitude = 100 + halfWidth / 2;

		WritableImage image = new WritableImage((int) side, (int) side);

		init(image, side, rotateAngleX, rotateAngleY, halfWidth, halfHeight);
	}

	private void init(WritableImage image, double side, double rotateAngleX, double rotateAngleY, double translateX,
			double translateY)
	{
		this.side = side;

		imageTexture = image;

		rotateX = new Rotate(angle, Rotate.X_AXIS);
		rotateY = new Rotate(angle, Rotate.Y_AXIS);

		material = new PhongMaterial(Color.WHITE);
		material.setDiffuseMap(imageTexture);

		float h = 150;                    // Height
		float s = 300;                    // Side

		TriangleMesh pyramidMesh = new TriangleMesh();

		pyramidMesh.getPoints().addAll(0, 0, 0,            // Point 0 - Top
				0, h, -s / 2,         // Point 1 - Front
				-s / 2, h, 0,            // Point 2 - Left
				s / 2, h, 0,            // Point 3 - Back
				0, h, s / 2           // Point 4 - Right
		);

		pyramidMesh.getFaces().addAll(0, 0, 2, 0, 1, 0,          // Front left face
				0, 0, 1, 0, 3, 0,          // Front right face
				0, 0, 3, 0, 4, 0,          // Back right face
				0, 0, 4, 0, 2, 0,          // Back left face
				4, 0, 1, 0, 2, 0,          // Bottom rear face
				4, 0, 3, 0, 1, 0           // Bottom front face
		);

		pyramid = new MeshView(pyramidMesh);

//		pyramid.setDrawMode(DrawMode.FILL);
		pyramid.setDrawMode(DrawMode.LINE);

//		pyramid.setMaterial(material);
		pyramid.setTranslateX(200);
		pyramid.setTranslateY(100);
		pyramid.setTranslateZ(50);

//		zAmplitude = 100 + halfWidth / 2;
//		xAmplitude = halfWidth / 4;

		//		pyramid.getTransforms().add(rotateY);
		//		pyramid.getTransforms().add(rotateX);

		//		this.translateX = translateX;
		//
		//		pyramid.setTranslateX(translateX);
		//		pyramid.setTranslateY(translateY);
	}

	@Override public void start()
	{
		group.getChildren().add(pyramid);
		System.out.println("Start");
	}

	@Override public void stop()
	{
		group.getChildren().remove(pyramid);
		System.out.println("Stop");
	}

	@Override public void renderForeground()
	{
		angle++;

		if (angle >= 360)
		{
			angle -= 360;
		}

		rotateX.setAngle(angle);
		rotateY.setAngle(angle);

		//		pyramid.setTranslateX(translateX + xAmplitude * precalc.sin(angle));
		//		pyramid.setTranslateZ(zAmplitude * precalc.cos(angle));
	}
}