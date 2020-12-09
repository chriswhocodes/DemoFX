#!/bin/sh

# May need to set -Xmx1024m on low RAM  machines
java -XX:+PrintGCDetails -Xloggc:demofx.gc.log \
  -Djava.library.path=/opt/javafx-sdk-16/lib \
  -Dmonocle.platform.traceConfig=true \
  -Dmonocle.platform=EGL \
  -Dprism.verbose=true \
  -Djavafx.verbose=true \
  -p /opt/javafx-sdk-16/lib \
  --add-modules javafx.controls \
  -jar target/demofx-0.0.1-jar-with-dependencies.jar $@