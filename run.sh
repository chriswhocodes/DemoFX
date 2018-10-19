#!/bin/sh

export CLASSPATH=build/libs/DemoFX.jar

# May need to set -Xmx1024m on low RAM  machines
"$JAVA_HOME/bin/java" -Xlog:gc* --module-path /home/javafxdeveloper/Projects/openjfx/rt/build/sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.media -jar $CLASSPATH $@
