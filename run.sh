#!/bin/sh

mvn clean compile exec:java -Dexec.args="$*"
