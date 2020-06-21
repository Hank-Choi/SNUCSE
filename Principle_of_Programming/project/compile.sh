#!/usr/bin/env bash

rm -rf classes
mkdir classes

scalac -classpath lib/*:classes/ -d classes/ src/main/scala/Env.scala
scalac -classpath lib/*:classes/ -d classes/ src/main/scala/Interpreter.scala
scalac -classpath lib/*:classes/ -d classes/ src/main/scala/Repl.scala
scalac -classpath lib/*:classes/ -d classes/ src/main/scala/Main.scala
