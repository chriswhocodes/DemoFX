package org.demofx.port;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;

/**
 *
 * @author diego cirujano
 */
public class DemoFXPort extends Application {
    
    private final String[] shapes = new String[]{"Triangles","Squares","Pentagons","Hexagons","Stars","Rings", "Sierpinski"};
    private int shape = 0;
    
    private final int numberItems = 100;

    @Override
    public void start(final Stage stage) throws Exception {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        DemoConfig config = DemoConfig.parseArgs(new String[]{});

        if (config == null) {
            System.err.print(DemoConfig.getUsageError());
            System.exit(-1);
        }
        
        Scene scene;
        
        DemoFX demoFX = new DemoFX();

        scene = new Scene(demoFX.runDemo(getConfig(numberItems, (int)visualBounds.getWidth(), (int)visualBounds.getHeight(), shapes[shape])), visualBounds.getWidth(), visualBounds.getHeight());
                
        scene.setOnMouseClicked((MouseEvent event) -> {
            shape = shape==shapes.length-1? 0:shape+1;
            scene.setRoot(demoFX.runDemo(getConfig(numberItems, (int)visualBounds.getWidth(), (int)visualBounds.getHeight(), shapes[shape])));
        });

        stage.setScene(scene);
        stage.show();

    }
    
    private DemoConfig getConfig(int numberItems, int canvasWidth, int canvasHeight, String fx){
        return DemoConfig.parseArgs(new String[]{"-c", numberItems+"", "-e", fx, "-h", canvasHeight+"","-w", canvasWidth+""});
    }


}
