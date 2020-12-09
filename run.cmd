@echo off
mvn clean compile exec:java -Dexec.args="%*"
