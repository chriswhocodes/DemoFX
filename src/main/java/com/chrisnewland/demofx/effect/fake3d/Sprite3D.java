/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.fake3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.ImageUtil;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Sprite3D extends AbstractEffect
{
	public enum Shape3D
	{
		RING, TUBE, SOLIDCUBE, WIREFRAMECUBE, SPHERE, SPRING, SPIKE, STAR
	}

	private List<P3D> renderList = new ArrayList<>();

	private List<Object3D> objects = new ArrayList<>();

	public Sprite3D(DemoConfig config)
	{
		super(config);

		Image image = ImageUtil.loadImageFromResources("glassyball.png");

		addObjectFromModel("face", image, -1, -1);
	}
	
	public Sprite3D(DemoConfig config, boolean blank)
	{
		super(config);
	}

	public Sprite3D(DemoConfig config, String text, int transformAfterFrames)
	{
		super(config);

		Image image = ImageUtil.loadImageFromResources("glassyball.png");

		Fake3DShapeFactory factory = new Fake3DShapeFactory(precalc, image);

		Object3D obj3D = factory.makeFromTextString(text, gc);
		obj3D.setTransformAfterFrames(transformAfterFrames);

		addObject(obj3D);
	}
	
	public Object3D addObject(Shape3D shape, Image image, double scale, long startMillis, long stopMillis)
	{
		this.effectStartMillis = startMillis;
		this.effectStopMillis = stopMillis;
		
		return addObject(shape, image, scale);
	}	

	public Object3D addObject(Shape3D shape, Image image, double scale)
	{
		Fake3DShapeFactory factory = new Fake3DShapeFactory(precalc, image);

		Object3D obj3D = null;

		switch (shape)
		{
		case SOLIDCUBE:
			obj3D = factory.makeSolidCube(12, 3.0 * scale);
			break;
		case WIREFRAMECUBE:
			obj3D = factory.makeWireframeCube(12, 3.0 * scale);
			obj3D.setMaxImageSize(obj3D.getMaxImageSize()*2.5);
			obj3D.setZoom(obj3D.getZoom()*2.5);
			obj3D.setzOffset(obj3D.getzOffset()*2);
			break;
		case RING:
			obj3D = factory.makeRing(48, 2.0 * scale);
			break;
		case SPHERE:
			obj3D = factory.makeSphere(5.0, 50 * scale);
			break;
		case TUBE:
			obj3D = factory.makeTube(48, 1.0, 16, 3.0 * scale);
			break;
		case SPRING:
			obj3D = factory.makeSpring(48, 1.0, 16, 3.0 * scale);
			break;
		case SPIKE:
			obj3D = factory.makeSpike(scale);
			break;
		case STAR:
			obj3D = factory.makeStar(scale);
			break;
		default:
			break;
		}

		addObject(obj3D);
		
		return obj3D;
	}

	public void addObjectFromModel(String filename, Image image, long startMillis, long stopMillis)
	{
		this.effectStartMillis = startMillis;
		this.effectStopMillis = stopMillis;

		addObjectFromModel(filename, image);
	}
	
	public void addObjectFromModel(String filename, Image image)
	{
		Fake3DShapeFactory factory = new Fake3DShapeFactory(precalc, image);

		Object3D obj3D = factory.makeFromFile("/models/" + filename + ".model");

		addObject(obj3D);
	}

	public void addObject(Object3D obj3D)
	{
		objects.add(obj3D);

		itemCount += obj3D.getPoints().size();
	}

	@Override
	public void renderForeground()
	{
		gc.setStroke(Color.WHITE);

		createRenderList();

		zSort();

		drawRenderList();
	}

	private final void createRenderList()
	{
		renderList.clear();

		final int size = objects.size();

		for (int i = 0; i < size; i++)
		{
			renderList.addAll(objects.get(i).transform());
		}
	}

	private final void drawRenderList()
	{
		final int size = renderList.size();

		for (int i = 0; i < size; i++)
		{
			P3D point = renderList.get(i);

			drawPoint(point);
		}
	}

	private final void zSort()
	{
		Collections.sort(renderList);
	}

	private final void drawPoint(P3D point)
	{
		final double zoom = point.getParent().getZoom();

		final double maxImageSize = point.getParent().getMaxImageSize();

		final Image image = point.getParent().getImage();

		final double pointZ = point.getZ();

		final double x = halfWidth + point.getX() / pointZ * zoom;
		final double y = halfHeight + point.getY() / pointZ * zoom;

		final double ballSize = maxImageSize / pointZ;

		gc.drawImage(image, x, y, ballSize, ballSize);
	}
}