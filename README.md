# DemoFX
Performance test platform for JavaFX.

I'll be using this to discover the best techniques for optimising JavaFX performance on the Raspberry Pi and Desktop.

Compile in your IDE or with ant:
```
ant compile
```
```
Run with:
com.chrisnewland.demofx.DemoFXApplication [options]
-e <effect>           triangles,squares,pentagons,hexagons,stars
-c <count>            number of items on screen
-r <degrees>          rotation per frame
-w <width>            canvas width
-h <height>           canvas height
-a <true|false>       antialias canvas
-m <line|poly|fill>   canvas plot mode
```
Examples:
```
# Default settings
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication

# Triangle effect, 500 shapes
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication -e triangles -c 500

# Square effect, set 640x480 canvas size
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication -e squares -w 640 -h 480

# Star effect, plot mode line
java -cp target/classes/ com.chrisnewland.demofx.DemoFXApplication -e stars -m line
```
