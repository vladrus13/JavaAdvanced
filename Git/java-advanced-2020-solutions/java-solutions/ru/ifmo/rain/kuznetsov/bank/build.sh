#!/bin/bash

Lib="../../java-advanced-2020/lib/"
Artifacts="../../java-advanced-2020/artifacts/"

cd ../../../../../
javac -p "$Lib:$Artifacts" $(find . -name "*.java")