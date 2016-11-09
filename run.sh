#!/bin/sh

export CLASSPATH=target/DemoFX.jar
export CLASSPATH=$CLASSPATH:src/main/resources

# May need to set -Xmx1024m on low RAM  machines
$JAVA_HOME/bin/java -cp $CLASSPATH com.chrisnewland.demofx.DemoFXApplication $@
