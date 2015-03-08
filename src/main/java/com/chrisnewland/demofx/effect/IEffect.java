package com.chrisnewland.demofx.effect;

public interface IEffect
{
	public void render();
	public void updateStatistics(long renderNanos);
	public String getStatistics();
}