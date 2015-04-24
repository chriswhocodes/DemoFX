/*
 * Copyright (c) 2015 Chris Newland.
 * Licensed under https://github.com/chriswhocodes/demofx/blob/master/LICENSE-BSD
 */
package com.chrisnewland.demofx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.chrisnewland.demofx.effect.EffectFactory;
import com.chrisnewland.demofx.effect.IEffect;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.impl.PrismSettings;
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

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
            }
        });

    }

}
