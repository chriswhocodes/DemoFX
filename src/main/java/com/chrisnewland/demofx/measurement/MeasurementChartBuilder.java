/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx.measurement;

import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class MeasurementChartBuilder
{
	public static LineChart<Number, Number> buildChartHeap(Measurements measurements)
	{		
		final long MEGABYTE = 1024*1024;
		
		XYChart.Series<Number, Number> seriesHeapSize = convertMeasurementSeries(measurements.getHeapSize(), MEGABYTE);
		XYChart.Series<Number, Number> seriesHeapUsed = convertMeasurementSeries(measurements.getHeapUsed(), MEGABYTE);
		
		seriesHeapSize.setName("Heap");
		seriesHeapUsed.setName("Used");
		
		Axis<Number> xAxis = createAxis("Time (seconds)");
		Axis<Number> yAxis = createAxis("MB");
		
		LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
		
		chart.getData().add(seriesHeapSize);
		chart.getData().add(seriesHeapUsed);
		
		return chart;
	}
	
	public static LineChart<Number, Number> buildChartFPS(Measurements measurements)
	{		
		XYChart.Series<Number, Number> seriesFPS = convertMeasurementSeries(measurements.getFps(), 1);
		
		seriesFPS.setName("FPS");
		
		NumberAxis xAxis = createAxis("Time (seconds)");
		NumberAxis yAxis = createAxis("FPS");		
		
		LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

		chart.getData().add(seriesFPS);
		
		return chart;
	}
	
	private static NumberAxis createAxis(String label)
	{
		NumberAxis axis = new NumberAxis();
		axis.setLabel(label);
		axis.setStyle("-fx-text-fill: white; -fx-tick-label-fill: white"); 

		//http://docs.oracle.com/javafx/2/charts/css-styles.htm
		
		/*
		axis.setTickLabelFormatter(new StringConverter<Number>()
		{
			
			@Override
			public String toString(Number object)
			{
				return DecimalFormat.getIntegerInstance().format(object);
			}
			
			@Override
			public Number fromString(String string)
			{
				return null;
			}
		});
		*/
		
		return axis;
	}
	
	private static XYChart.Series<Number, Number> convertMeasurementSeries(com.chrisnewland.demofx.measurement.Series series, long valueDivisor)
	{
		XYChart.Series<Number, Number> result = new XYChart.Series<>();
		
		int size = series.size();
		
		for (int i = 0; i < size; i++)
		{
			long time = series.getTimes().get(i);
			long value = series.getValues().get(i);
			
			time /= 1000;
			value /= valueDivisor;
			
			result.getData().add(new XYChart.Data<Number, Number>(time, value));
		}
		
		return result;
	}

}