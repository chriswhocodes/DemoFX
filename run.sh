#!/bin/sh

export CLASSPATH=target/DemoFX.jar
CLASSPATH=$CLASSPATH:$JAVA_HOME/jre/lib/jfxrt.jar

java -cp $CLASSPATH com.chrisnewland.demofx.DemoFXApplication $@
