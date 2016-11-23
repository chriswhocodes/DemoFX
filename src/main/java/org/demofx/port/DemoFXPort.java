package org.demofx.port;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author diego cirujano
 */
public class DemoFXPort extends Application
{

	private final String[] shapes = new String[] {
			"bounce",
			"burst",
			"checkerboard",
			"concentric",
			"grid",
			"hexagons",
			"mandelbrot",
			"pentagons",
			"pixels",
			"rings",
			"sierpinski",
			"spin",
			"sprite3d",
			"squares",
			"starfield",
			"stars",
			"tiles",
			"triangles",
			"textwave",
			"spritewave",
			"credits" };
	private int shape = 0;

	private final int numberItems = 100;

	private DemoConfig config;

	private DemoFX demoFX;

	@Override
	public void start(final Stage stage) throws Exception
	{
		Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

		int topHeight = 50;

		config = getConfig(numberItems, (int) visualBounds.getWidth(), (int) visualBounds.getHeight() - topHeight, shapes[shape]);

		if (config == null)
		{
			System.err.print(DemoConfig.getUsageError());
			System.exit(-1);
		}

		demoFX = new DemoFX(config);

		Scene scene = demoFX.getScene();

		scene.setOnMouseClicked((MouseEvent event) -> {
			demoFX.stopDemo();
			shape = shape == shapes.length - 1 ? 0 : shape + 1;

			config = getConfig(numberItems, (int) visualBounds.getWidth(), (int) visualBounds.getHeight(), shapes[shape]);

			demoFX = new DemoFX(config);
			demoFX.runDemo();

		});

		demoFX.runDemo();

		stage.setScene(scene);
		stage.show();

		stage.setOnCloseRequest((WindowEvent arg0) -> demoFX.stopDemo());
	}

	private static DemoConfig getConfig(int numberItems, int canvasWidth, int canvasHeight, String effect)
	{
		return DemoConfig.buildConfig(new String[] {
				"-c",
				numberItems + "",
				"-e",
				effect,
				"-h",
				canvasHeight + "",
				"-w",
				canvasWidth + "" });
	}
}