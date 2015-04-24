package org.demofx.port;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.WindowEvent;

/**
 *
 * @author diego cirujano
 */
public class DemoFXPort extends Application {
    
    private final String[] shapes = new String[]{"bounce"
                                                ,"burst"
                                                ,"checkerboard"
                                                ,"concentric"
                                                ,"grid"
                                                ,"hexagons"
                                                ,"mandelbrot"
                                                ,"pentagons"
                                                ,"pixels"
                                                ,"rings"
                                                ,"sierpinski"
                                                ,"spin"
                                                ,"sprite3d"
                                                ,"squares"
                                                ,"starfield"
                                                ,"stars"
                                                ,"tiles"
                                                ,"triangles"
                                                ,"textwave"
                                                ,"spritewave"
                                                ,"credits"};
    private int shape = 0;
    
    private final int numberItems = 100;

    @Override
    public void start(final Stage stage) throws Exception {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        DemoConfig config = getConfig(numberItems, (int)visualBounds.getWidth(), (int)visualBounds.getHeight()-50, shapes[shape]);

        if (config == null) {
            System.err.print(DemoConfig.getUsageError());
            System.exit(-1);
        }
        
        DemoFX demoFX = new DemoFX();

        Pane root = demoFX.runDemo(config);

        final Scene scene;

        int topHeight = 50;

        scene = new Scene(demoFX.runDemo(getConfig(numberItems, (int)visualBounds.getWidth(), (int)visualBounds.getHeight(), shapes[shape])), visualBounds.getWidth(), visualBounds.getHeight());
                
        scene.setOnMouseClicked((MouseEvent event) -> {
            demoFX.stopDemo();
            shape = shape==shapes.length-1? 0:shape+1;
            scene.setRoot(demoFX.runDemo(getConfig(numberItems, (int)visualBounds.getWidth(), (int)visualBounds.getHeight(), shapes[shape])));
        });

        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent arg0) -> demoFX.stopDemo());

    }
    
    private static DemoConfig getConfig(int numberItems, int canvasWidth, int canvasHeight, String effect){
        return DemoConfig.parseArgs(new String[]{"-c", numberItems+"", "-e", effect, "-h", canvasHeight+"","-w", canvasWidth+""});
    }


}
