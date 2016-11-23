/*
 * Copyright (c) 2015-2016 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class DemoFXApplication extends Application
{
	private static String[] args;

	public static void main(String[] args)
	{
		DemoFXApplication.args = args;
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception
	{
		DemoConfig config = DemoConfig.buildConfig(args);

		if (config == null)
		{
			System.err.print(DemoConfig.getUsageError());
			System.exit(-1);
		}

		DemoFX demoFX = new DemoFX(config);

		Scene scene = demoFX.getScene();

		stage.setTitle("DemoFX / JavaFX Demoscene Engine / @chriswhocodes");
		stage.setScene(scene);
		stage.show();

		stage.setOnCloseRequest((WindowEvent arg0) -> demoFX.stopDemo());

		// Double-click to interrupt the demo and show the result panel:
		scene.setOnMouseClicked((MouseEvent me) -> {
				if (me.getClickCount() == 2) {
				demoFX.stopDemo();
			}
        	});

		demoFX.runDemo();
	}
}