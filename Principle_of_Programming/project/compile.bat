rd /s /q classes
mkdir classes

call scalac -classpath "lib/*;classes/" -d classes/ src/main/scala/Env.scala
call scalac -classpath "lib/*;classes/" -d classes/ src/main/scala/Interpreter.scala
call scalac -classpath "lib/*;classes/" -d classes/ src/main/scala/Repl.scala
call scalac -classpath "lib/*;classes/" -d classes/ src/main/scala/Main.scala

pause
