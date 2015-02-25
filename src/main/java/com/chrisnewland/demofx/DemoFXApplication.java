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

import com.chrisnewland.demofx.DemoConfig.PlotMode;
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
			builder.append("-e <effect>\t\tstars").append("\n");
			builder.append("-i <items>\t\tnumber of items on screen").append("\n");
			builder.append("-w <width>\t\tcanvas width").append("\n");
			builder.append("-h <height>\t\tcanvas height").append("\n");
			builder.append("-a <true|false>\t\tantialias canvas").append("\n");
			builder.append("-m <line|poly>\t\tcanvas plot mode").append("\n");

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
			Class<IEffect> effectClass = (Class<IEffect>) Class.forName(config.getEffect());

			Constructor<IEffect> constructor = effectClass.getDeclaredConstructor(GraphicsContext.class, int.class, int.class,
					int.class, PlotMode.class);

			effect = constructor.newInstance(new Object[] {
					gc,
					config.getCount(),
					config.getWidth(),
					config.getHeight(),
					config.getPlotMode() });

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
