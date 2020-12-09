#!/bin/sh

# May need to set -Xmx1024m on low RAM  machines
java -XX:+PrintGCDetails -Xloggc:demofx.gc.log \
  -Dmonocle.egl.lib=/opt/arm32fb-sdk/lib/libgluon_drm.so \
  -Djava.library.path=/opt/arm32fb-sdk/lib \
  -Dmonocle.platform.traceConfig=true \
  -Dmonocle.platform=EGL \
  -Dprism.verbose=true \
  -Djavafx.verbose=true \
  -p /opt/arm32fb-sdk/lib \
  --add-modules javafx.controls \
  -jar target/demofx-0.0.1-jar-with-dependencies.jar $@
