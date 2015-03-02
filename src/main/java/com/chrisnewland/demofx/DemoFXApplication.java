/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.chrisnewland.demofx.effect.EffectFactory;
import com.chrisnewland.demofx.effect.IEffect;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.impl.PrismSettings;

public class DemoFXApplication extends Application
{
	private static String[] args;

	private static final long ONE_SECOND_NANOS = 1_000_000_000L;

	public static void main(String[] args)
	{
		DemoFXApplication.args = args;
		Application.launch(args);
	}

	private GraphicsContext gc;

	private Label statsLabel;
	private Label fxLabel;

	@Override
	public void start(final Stage stage) throws Exception
	{
		DemoConfig config = DemoConfig.parseArgs(args);

		if (config == null)
		{
			System.err.print(DemoConfig.getUsageError());
			System.exit(-1);
		}

		runDemo(stage, config);
	}

	private void runDemo(final Stage stage, final DemoConfig config)
	{
		IEffect effect = null;

		Canvas canvas = new Canvas(config.getWidth(), config.getHeight());

		gc = canvas.getGraphicsContext2D();
		
		try
		{		
			effect = EffectFactory.getEffect(gc, config);
		}
		catch (RuntimeException re)
		{
			System.err.println(re.getMessage());
			System.exit(-1);
		}

		BorderPane root = new BorderPane();

		Scene scene;

		int topHeight = 50;

		if (config.isAntialias())
		{
			scene = new Scene(root, config.getWidth(), config.getHeight() + topHeight);
		}
		else
		{
			scene = new Scene(root, config.getWidth(), config.getHeight() + topHeight, false, SceneAntialiasing.DISABLED);
		}

		final String BLACK_BG_STYLE = "-fx-background-color:black;";
		final String FONT_STYLE = "-fx-font-family:monospace; -fx-font-size:16px; -fx-text-fill:white;";

		statsLabel = new Label();
		statsLabel.setStyle(FONT_STYLE);
		statsLabel.setAlignment(Pos.BASELINE_LEFT);
		statsLabel.prefWidthProperty().bind(root.widthProperty());

		fxLabel = new Label();
		fxLabel.setStyle(FONT_STYLE);
		fxLabel.setAlignment(Pos.BASELINE_LEFT);
		fxLabel.prefWidthProperty().bind(root.widthProperty());
		fxLabel.setText(getFXLabelText());

		root.setStyle(BLACK_BG_STYLE);

		VBox vbox = new VBox();
		vbox.setMinHeight(topHeight);
		vbox.getChildren().add(statsLabel);
		vbox.getChildren().add(fxLabel);

		root.setTop(vbox);
		root.setCenter(canvas);

		stage.setTitle("DemoFX performance test platform by @chriswhocodes");
		stage.setScene(scene);
		stage.show();

		animate(effect);
	}

	private String getFXLabelText()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("Trying: ").append(listToString(PrismSettings.tryOrder));
		builder.append("  Using: ").append(getUsedPipeline());

		return builder.toString();
	}

	private String getUsedPipeline()
	{
		GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
		return pipeline.getClass().getName();
	}

	private String listToString(List<String> list)
	{
		StringBuilder builder = new StringBuilder();

		for (String str : list)
		{
			builder.append(str).append(",");
		}

		if (builder.length() > 0)
		{
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}

	private void animate(final IEffect effect)
	{
		AnimationTimer timer = new AnimationTimer()
		{
			private long lastNanos = 0;

			@Override
			public void handle(long nanos)
			{
				effect.render();

				if (nanos - lastNanos > ONE_SECOND_NANOS)
				{
					statsLabel.setText(effect.getStatistics());
					lastNanos = nanos;
				}
			}
		};

		timer.start();
	}
}