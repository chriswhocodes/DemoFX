/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javafx.scene.layout.Pane;

public class DemoFXApplication extends Application {

    private static String[] args;

    public static void main(String[] args) {
        DemoFXApplication.args = args;
        Application.launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        DemoConfig config = DemoConfig.parseArgs(args);

        if (config == null) {
            System.err.print(DemoConfig.getUsageError());
            System.exit(-1);
        }

        int topHeight = 50;
        DemoFX demoFX = new DemoFX();
        Pane root = demoFX.runDemo(config);
        Scene scene = new Scene(root, config.getWidth(), config.getHeight() + topHeight);

        stage.setTitle("DemoFX performance test platform by @chriswhocodes");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent arg0) -> demoFX.stopDemo());

    }

}
