package pp201902.project.InterpreterIF

import pp201902.project.ExpressionIF.Bundle._
import pp201902.project.ValueIF.Bundle._
import pp201902.project.EnvIF.Bundle._

object Bundle {
  abstract class InterpreterIF[Env] {
    def interp(expr: Expr): Val[Env]
  }
}
