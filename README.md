# DemoFX
Performance test platform for JavaFX.

See a video of the demo effects here: https://www.youtube.com/watch?v=N1rihYA8c2M

I'll be using this to discover the best techniques for optimising JavaFX performance on the Raspberry Pi and Desktop.

Compile in your IDE or with ant:
```
ant
```
```
Run with:
./run.sh [options]
-e <effects>               triangles
                           squares
                           pentagons
                           hexagons
                           stars
                           rings
                           sierpinski
                           tiles
                           spin
                           burst
                           bounce
                           concentric
                           pixels
                           textwave
                           ballwave
                           grid
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
./run.sh -e stars -m line

# Bounce effect
./run.sh -e bounce

# Burst effect
./run.sh -e burst

#Layered effects: grid,ballwave
./run -e grid,ballwave
```
