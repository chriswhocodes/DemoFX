
package com.chrisnewland.demofx;

import com.chrisnewland.demofx.effect.EffectFactory;
import com.chrisnewland.demofx.effect.IEffect;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.impl.PrismSettings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class DemoFX {

    private static final long ONE_SECOND_NANOS = 1_000_000_000L;

    private GraphicsContext gc;

    private Label statsLabel;
    private Label fxLabel;
    
        AnimationTimer timer ;
    
    public DemoFX(){
        
    }
    
    public void stopDemo(){
        if(timer!=null)timer.stop();
    }
    
    public Pane runDemo(DemoConfig config) {
        stopDemo();
        
        final int canvasHeight = (int) (config.getHeight());
        Canvas canvas = new Canvas(config.getWidth(), canvasHeight);

        gc = canvas.getGraphicsContext2D();

        IEffect effect = null;
        try {
            effect = EffectFactory.getEffect(gc, config);
        } catch (RuntimeException re) {
            System.err.println(re.getMessage());
            System.exit(-1);
        }

        final String FONT_STYLE = "-fx-font-family:monospace; -fx-font-size:16px; -fx-text-fill:white;";

        statsLabel = new Label();
        statsLabel.setStyle(FONT_STYLE);

        fxLabel = new Label();
        fxLabel.setStyle(FONT_STYLE);


        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color:black;");

        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color:rgba(173, 255, 47,0.5)");
        vbox.getChildren().add(statsLabel);
        statsLabel.setAlignment(Pos.CENTER_LEFT);
        VBox.setVgrow(statsLabel, Priority.ALWAYS);
        vbox.getChildren().add(fxLabel);
        fxLabel.setAlignment(Pos.CENTER_LEFT);
        fxLabel.setText(getFXLabelText(config));
        VBox.setVgrow(fxLabel, Priority.ALWAYS);

        root.getChildren().addAll(canvas,vbox);
        AnchorPane.setBottomAnchor(canvas, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(canvas, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(canvas, 0.0);
        AnchorPane.setTopAnchor(vbox, 0.0);
        AnchorPane.setTopAnchor(canvas, 0.0);

        animate(effect);
        return root;
    }
    
    
	private String getFXLabelText(DemoConfig config)
	{
		StringBuilder builder = new StringBuilder();

		builder.append("Order: ").append(getPrismTryOrder());
		builder.append(" | Pipeline: ").append(getUsedPipeline());
		builder.append(" | Lookups: ");

		StringBuilder lookupBuilder = new StringBuilder();

		boolean anyLookups = false;

		if (config.isLookupRandom())
		{
			anyLookups = true;
			lookupBuilder.append("rand").append(",");
		}

		if (config.isLookupSqrt())
		{
			anyLookups = true;
			lookupBuilder.append("sqrt").append(",");
		}

		if (config.isLookupTrig())
		{
			anyLookups = true;
			lookupBuilder.append("trig");
		}

		if (!anyLookups)
		{
			lookupBuilder.append("none");
		}
		else if (lookupBuilder.charAt(lookupBuilder.length() - 1) == ',')
		{
			lookupBuilder.deleteCharAt(lookupBuilder.length() - 1);
		}

		builder.append(lookupBuilder.toString());

		return builder.toString();
	}

	private String getUsedPipeline()
	{
		GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
		return pipeline.getClass().getName();
	}

	@SuppressWarnings("unchecked")
	private String getPrismTryOrder()
	{
		// Java 7 returns String[]
		// Java 8 returns List<String>
		Object result = PrismSettings.tryOrder;

		List<String> tryOrderList = new ArrayList<>();

		if (result instanceof String[])
		{
			tryOrderList.addAll(Arrays.asList((String[])result));
		}
		else if (result instanceof List)
		{
			tryOrderList.addAll((List<String>)result);
		}

		StringBuilder builder = new StringBuilder();

		for (String str : tryOrderList)
		{
			builder.append(str).append(",");
		}

		if (builder.length() > 0)
		{
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}


    private void animate(final IEffect effect) {
timer = new AnimationTimer() {
            private long lastNanos = 0;

            @Override
            public void handle(long startNanos) {
                effect.render();

                long renderNanos = System.nanoTime() - startNanos;

                effect.updateStatistics(renderNanos);

                if (startNanos - lastNanos > ONE_SECOND_NANOS) {
                    statsLabel.setText(effect.getStatistics());
                    lastNanos = startNanos;
                }
            }
        };
        timer.start();
    }
}
