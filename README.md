# DemoFX
Performance test platform for JavaFX.

Effects can be layered and scheduled on a timeline.

Videos of the demo effects:

Part I : https://www.youtube.com/watch?v=N1rihYA8c2M

Part II: https://www.youtube.com/watch?v=WZf0j4GUFYM

Used to discover the best techniques for optimising JavaFX performance on the Raspberry Pi, Desktop iOS and Android.

Compile in your IDE or with ant:
```
ant
```
```
Run with:
./run.sh [options]
DemoFXApplication [options]
-e <effects>               bounce
                           burst
                           checkerboard
                           concentric
                           credits
                           grid
                           hexagons
                           mandelbrot
                           pentagons
                           pixels
                           rings
                           sierpinski
                           spin
                           sprite3d
                           spritewave
                           squares
                           starfield
                           stars
                           textwave
                           tiles
                           triangles

-c <count>                 number of items on screen
-w <width>                 canvas width
-h <height>                canvas height
-l [sqrt,trig,rand,none]   use lookup tables for Math.sqrt, Math.{sin|cos}, Math.Random
-m <line|poly|fill>        canvas plot mode
-s <true>                  use ScriptedDemoConfig

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

# Layered effects: grid,ballwave
./run.sh -e grid,ballwave

# Scripted demo mode:
./run.sh -s true
```
