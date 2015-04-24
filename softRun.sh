#!/bin/sh

export CLASSPATH=target/DemoFX.jar
CLASSPATH=$CLASSPATH:$JAVA_HOME/jre/lib/jfxrt.jar

java -Dprism.order=sw -cp $CLASSPATH com.chrisnewland.demofx.DemoFXApplication $@
