/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.effect.shape;

import java.util.ArrayList;
import java.util.List;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.effect.AbstractEffect;
import com.chrisnewland.demofx.util.Cog;

import javafx.scene.paint.Color;

public class Cogs extends AbstractEffect
{
	private List<Cog> cogs;

	private double angle = 0;

	public Cogs(DemoConfig config)
	{
		super(config);
		
		init(Color.BLACK);
	}
	
	public Cogs(DemoConfig config, Color outline)
	{
		super(config);
		
		init(outline);
	}

	private void init(Color outline)
	{
		List<Cog> cogs = new ArrayList<>();

		int teeth = 9;

		double outer = 80;
		double inner = outer - (outer / 3);
		double closer = (outer - inner) * 0.8;

		double x0 = halfWidth - outer * 2 + closer;
		double x1 = halfWidth;
		double x2 = halfWidth + outer * 2 - closer;

		double y0 = halfHeight - outer * 2 + closer;
		double y1 = halfHeight;
		double y2 = halfHeight + outer * 2 - closer;
		
		cogs.add(new Cog(config, x0, y0, true, 0, outer, inner, teeth, outline));
		cogs.add(new Cog(config, x1, y0, false, teeth * 3, outer, inner, teeth, outline));
		cogs.add(new Cog(config, x2, y0, true, 0, outer, inner, teeth, outline));

		cogs.add(new Cog(config, x0, y1, false, teeth, outer, inner, teeth, outline));
		cogs.add(new Cog(config, x2, y1, false, teeth, outer, inner, teeth, outline));

		cogs.add(new Cog(config, x0, y2, true, 0, outer, inner, teeth, outline));
		cogs.add(new Cog(config, x1, y2, false, teeth * 3, outer, inner, teeth, outline));
		cogs.add(new Cog(config, x2, y2, true, 0, outer, inner, teeth, outline));
		
		init(cogs);		
	}
	
	public Cogs(DemoConfig config, List<Cog> cogs)
	{
		super(config);

		init(cogs);
	}
	
	private void init(List<Cog> cogs)
	{
		this.cogs = cogs;
	}

	@Override
	public void renderForeground()
	{
		angle += 1;

		if (angle > 360)
		{
			angle -= 360;
		}

		for (Cog cog : cogs)
		{
			cog.draw(gc, angle);
		}
	}
}