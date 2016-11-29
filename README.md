# DemoFX
Performance test platform for JavaFX.

Effects can be layered and scheduled on a timeline.

Videos of the demo effects:

Part I : https://www.youtube.com/watch?v=N1rihYA8c2M

Part II: https://www.youtube.com/watch?v=WZf0j4GUFYM

Part III: https://www.youtube.com/watch?v=9jztG_l8qrk

Used to discover the best techniques for optimising JavaFX performance on the Raspberry Pi and Desktop.

Compile in your IDE or with gradle:
```
./gradlew
```
```
Run with:
./run.sh [options]

-e <effects>               comma separated list of effects (See SimpleEffectFactory)
-c <count>                 number of items on screen
-w <width>                 canvas width
-h <height>                canvas height
-l [sqrt,trig,rand,none]   use lookup tables for Math.sqrt, Math.{sin|cos}, Math.Random
-m <line|poly|fill>        canvas plot mode
-s <true>                  use ScriptedDemoConfig
-f <true>                  fullscreen mode (no performance bar at top)

```
Examples:
```
# Default settings
./run.sh

# Triangle effect, 500 shapes
./run.sh -e colourbackground,triangles -c 500

# Square effect, set 640x480 canvas size
./run.sh -e colourbackground,squares -w 640 -h 480

# Star effect, plot mode line
./run.sh -e colourbackground,stars -m line

# Bounce effect
./run.sh -e rainbow,bounce

# Burst effect
./run.sh -e rainbow,burst

# Layered effects: grid,ballwave
./run.sh -e colourbackground,grid,ballwave

# DemoFX Part III scripted demo:
./run.sh -s true -f true -w 1280 -h 720
```
