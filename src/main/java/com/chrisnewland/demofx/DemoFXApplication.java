/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
		DemoConfig config = DemoConfig.parseArgs(args);

		if (config == null)
		{
			System.err.print(DemoConfig.getUsageError());
			System.exit(-1);
		}
                
                DemoFX demoFX = new DemoFX();

		Scene scene = new Scene(demoFX.runDemo(config), config.getWidth(), config.getHeight());
		stage.setTitle("DemoFX performance test platform by @chriswhocodes");
		stage.setScene(scene);
		stage.show();
	}

}