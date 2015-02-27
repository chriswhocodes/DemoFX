/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.chrisnewland.demofx.effect.IEffect;

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

	@Override
	public void start(final Stage stage) throws Exception
	{
		DemoConfig config = DemoConfig.parseArgs(args);

		if (config == null)
		{
			StringBuilder builder = new StringBuilder();

			builder.append("DemoFXApplication [options]").append("\n");
			builder.append("-e <effect>").append("\t\t").append("stars | stars2 | triangles | squares").append("\n");
			builder.append("-s <sides>").append("\t\t").append("sides per polygon").append("\n");
			builder.append("-c <count>").append("\t\t").append("number of items on screen").append("\n");
			builder.append("-r <degrees>").append("\t\t").append("rotation per frame").append("\n");
			builder.append("-w <width>").append("\t\t").append("canvas width").append("\n");
			builder.append("-h <height>").append("\t\t").append("canvas height").append("\n");
			builder.append("-a <true|false>").append("\t\t").append("antialias canvas").append("\n");
			builder.append("-m <line|poly>").append("\t\t").append("canvas plot mode").append("\n");

			System.err.print(builder.toString());
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
			@SuppressWarnings("unchecked")
			Class<IEffect> effectClass = (Class<IEffect>) Class.forName("com.chrisnewland.demofx.effect." + config.getEffect());

			Constructor<IEffect> constructor = effectClass.getDeclaredConstructor(GraphicsContext.class, DemoConfig.class);

			effect = constructor.newInstance(new Object[] { gc, config });
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		BorderPane root = new BorderPane();

		Scene scene;

		int topHeight = 25;

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
		statsLabel.setAlignment(Pos.CENTER);
		statsLabel.prefWidthProperty().bind(root.widthProperty());

		root.setStyle(BLACK_BG_STYLE);

		HBox hbox = new HBox();
		hbox.setMinHeight(topHeight);
		hbox.getChildren().add(statsLabel);

		root.setTop(hbox);
		root.setCenter(canvas);

		stage.setTitle("@chriswhocodes JavaFX Canvas Demo");
		stage.setScene(scene);
		stage.show();

		animate(effect);
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