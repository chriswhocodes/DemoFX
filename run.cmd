@echo off
set CLASSPATH=build\libs\DemoFX.jar
rem May need to set -Xmx1024m on low RAM  machines
rem -XX:+PrintTenuringDistribution 
%JAVA_HOME%\bin\java -XX:+PrintGCDetails -Xloggc:demofx.gc.log -jar %CLASSPATH% %*
