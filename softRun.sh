#!/bin/sh

export CLASSPATH=build/libs/DemoFX.jar
CLASSPATH=$CLASSPATH:$JAVA_HOME/jre/lib/jfxrt.jar

java -Dprism.order=sw -Xlog:gc* --module-path /home/javafxdeveloper/Projects/openjfx/rt/build/sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.media -cp $CLASSPATH com.chrisnewland.demofx.DemoFXApplication $@
