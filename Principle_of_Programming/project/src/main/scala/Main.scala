package pp201902.project

import pp201902.project.Env.Bundle._
import pp201902.project.Expression.Bundle._
import pp201902.project.Parser.Bundle._
import pp201902.project.Value.Bundle._
import pp201902.project.Interpreter.Bundle._
import pp201902.project.Repl.Bundle._

import pp201902.project.ReplIF.Bundle._

object Main extends App {
  implicitly[ReplIF].run
}