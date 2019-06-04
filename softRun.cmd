@echo off
set CLASSPATH=build\libs\DemoFX.jar
rem May need to set -Xmx1024m on low RAM  machines
set CLASSPATH=%CLASSPATH%:%JAVA_HOME%\jre\lib\jfxrt.jar
%JAVA_HOME%\bin\java -Dprism.order=sw -cp %CLASSPATH% com.chrisnewland.demofx.DemoFXApplication %*
