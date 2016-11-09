package com.chrisnewland.demofx.effect.spectral;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.ISpectrumDataProvider;
import com.chrisnewland.demofx.effect.AbstractEffect;

public abstract class AbstractSpectralEffect extends AbstractEffect implements ISpectralEffect
{
	protected ISpectrumDataProvider spectrumProvider;

	protected double bandWidth;

	protected int usableBandCount;
	protected float[] bandMax;
	protected float DECAY = 0.4f;
	protected static final float DECIBEL_RANGE = 60.0f;
	protected float heightFactor = 0;
	protected double barWidth;
	protected double halfBar;
	
	@Override
	public void setSpectrumDataProvider(ISpectrumDataProvider provider)
	{
		this.spectrumProvider = provider;
		
		// high end of frequency range rarely used
		// only plot low fraction of spectrum

		this.usableBandCount = (int) Math.floor(provider.getBandCount() * 0.33);

		bandWidth = width / usableBandCount;

		barWidth = bandWidth - 2;
		halfBar = barWidth / 2;

		bandMax = new float[usableBandCount];

		for (int i = 0; i < usableBandCount; i++)
		{
			bandMax[i] = -DECIBEL_RANGE;
		}		
	}
	
	public AbstractSpectralEffect(DemoConfig config)
	{
		super(config);
	}
}