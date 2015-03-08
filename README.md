# DemoFX
Performance test platform for JavaFX.

I'll be using this to discover the best techniques for optimising JavaFX performance on the Raspberry Pi and Desktop.

Compile in your IDE or with ant:
```
ant compile
```
```
Run with:
./run.sh [options]
-e <effect>                triangles,squares,pentagons,hexagons,stars,rings,tiles,spin,burst,concentric,bounce
-c <count>                 number of items on screen
-w <width>                 canvas width
-h <height>                canvas height
-l [sqrt,trig,rand,none]   use lookup tables for Math.sqrt, Math.{sin|cos}, Math.Random
-a <true|false>            antialias canvas
-m <line|poly|fill>        canvas plot mode
```
Examples:
```
# Default settings
./run.sh

# Triangle effect, 500 shapes
./run.sh -e triangles -c 500

# Square effect, set 640x480 canvas size
./run.sh -e squares -w 640 -h 480

# Star effect, plot mode line
./runs.sh -e stars -m line
```
