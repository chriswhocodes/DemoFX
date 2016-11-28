#!/bin/sh

export CLASSPATH=build/libs/DemoFX.jar
CLASSPATH=$CLASSPATH:$JAVA_HOME/jre/lib/jfxrt.jar

java -Dprism.order=sw -cp $CLASSPATH com.chrisnewland.demofx.DemoFXApplication $@
