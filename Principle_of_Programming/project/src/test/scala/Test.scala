package pp201902.project.Test

import pp201902.project.TestIF.Bundle._
import pp201902.project.InterpreterIF.Bundle._
import pp201902.project.ValueIF.Bundle._
import pp201902.project.ParserIF.Bundle._

object Bundle {
  implicit def testIF[Env]
  (implicit INTP: InterpreterIF[Env],
   PS: ParserIF,
   VU: ValUtilIF[Env])
  : TestIF = new TestIF {
    def check(code: String, answer: String): Boolean = {
      val result = VU.toString(INTP.interp(PS.parse(code)))
      result == answer
    }
  }
}
