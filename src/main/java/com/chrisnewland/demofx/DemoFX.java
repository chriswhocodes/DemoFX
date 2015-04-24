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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class DemoFX {

    private static String[] args;
    private static final long ONE_SECOND_NANOS = 1_000_000_000L;

    private GraphicsContext gc;

    private Label statsLabel;
    private Label fxLabel;

    private AnimationTimer timer;

    private List<IEffect> effects;

    public DemoFX() {

    }

    public void stopDemo() {
        for (IEffect effect : effects) {
            effect.stop();
        }
        if (timer != null) {
            timer.stop();
        }
    }

    public Pane runDemo(final DemoConfig config) {
        Canvas canvas = new Canvas(config.getWidth(), config.getHeight());

        gc = canvas.getGraphicsContext2D();

        try {
            if (config.isUseScriptedDemoConfig()) {
                effects = ScriptedDemoConfig.getEffects(gc, config);
            } else {
                effects = EffectFactory.getEffects(gc, config);
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
            System.err.println(re.getMessage());
            System.err.print(DemoConfig.getUsageError());
            System.exit(-1);
        }

        BorderPane root = new BorderPane();

        int topHeight = 50;

        final String BLACK_BG_STYLE = "-fx-background-color:black;";
        final String FONT_STYLE = "-fx-font-family:monospace; -fx-font-size:16px; -fx-text-fill:white;";

        statsLabel = new Label();
        statsLabel.setStyle(FONT_STYLE);
        statsLabel.setAlignment(Pos.BASELINE_LEFT);
        statsLabel.prefWidthProperty().bind(root.widthProperty());

        fxLabel = new Label();
        fxLabel.setStyle(FONT_STYLE);
        fxLabel.setAlignment(Pos.BASELINE_LEFT);
        fxLabel.prefWidthProperty().bind(root.widthProperty());
        fxLabel.setText(getFXLabelText(config));

        root.setStyle(BLACK_BG_STYLE);

        VBox vbox = new VBox();
        vbox.setMinHeight(topHeight);
        vbox.getChildren().add(statsLabel);
        vbox.getChildren().add(fxLabel);

        root.setTop(vbox);
        root.setCenter(canvas);

        timer = new DemoAnimationTimer(gc, statsLabel, effects);
        timer.start();
        return root;
    }

    private String getFXLabelText(DemoConfig config) {
        StringBuilder builder = new StringBuilder();

        builder.append("Order: ").append(getPrismTryOrder());
        builder.append(" | Pipeline: ").append(getUsedPipeline());
        builder.append(" | Lookups: ");

        StringBuilder lookupBuilder = new StringBuilder();

        boolean anyLookups = false;

        if (config.isLookupRandom()) {
            anyLookups = true;
            lookupBuilder.append("rand").append(",");
        }

        if (config.isLookupSqrt()) {
            anyLookups = true;
            lookupBuilder.append("sqrt").append(",");
        }

        if (config.isLookupTrig()) {
            anyLookups = true;
            lookupBuilder.append("trig");
        }

        if (!anyLookups) {
            lookupBuilder.append("none");
        } else if (lookupBuilder.charAt(lookupBuilder.length() - 1) == ',') {
            lookupBuilder.deleteCharAt(lookupBuilder.length() - 1);
        }

        builder.append(lookupBuilder.toString());

        return builder.toString();
    }

    private String getUsedPipeline() {
        GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
        return pipeline.getClass().getName();
    }

    @SuppressWarnings("unchecked")
    private String getPrismTryOrder() {
        // Java 7 returns String[]
        // Java 8 returns List<String>
        Object result = PrismSettings.tryOrder;

        List<String> tryOrderList = new ArrayList<>();

        if (result instanceof String[]) {
            tryOrderList.addAll(Arrays.asList((String[]) result));
        } else if (result instanceof List) {
            tryOrderList.addAll((List<String>) result);
        }

        StringBuilder builder = new StringBuilder();

        for (String str : tryOrderList) {
            builder.append(str).append(",");
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

}
