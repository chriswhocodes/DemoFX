# DemoFX
Performance test platform for JavaFX.

I'll be using this to discover the best techniques for optimising JavaFX performance on the Raspberry Pi and Desktop.
```
Usage:
DemoFXApplication [options]
-e <effect>      stars | stars2 | triangles | squares
-c <count>       number of items on screen
-r <degrees>     rotation per frame
-w <width>       canvas width
-h <height>      canvas height
-a <true|false>  antialias canvas
-m <line|poly>   canvas plot mode
```
Examples:
```
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication -e squares
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication -e triangles -c 500
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication -e stars2 -r 20
```
