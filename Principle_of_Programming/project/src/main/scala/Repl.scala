package pp201902.project.Repl

import pp201902.project.ReplIF.Bundle._

import pp201902.project.Common.Bundle._
import pp201902.project.InterpreterIF.Bundle._
import pp201902.project.ValueIF.Bundle._
import pp201902.project.ParserIF.Bundle._

object Bundle {
  implicit def replIF[Env]
  (implicit INTP: InterpreterIF[Env],
   PS: ParserIF,
   VU: ValUtilIF[Env])
  : ReplIF = new ReplIF {
    def run: Unit = {
      println("Input your program [Enter for exit]:")
      val input = scala.io.StdIn.readLine()
      if (input == "") {
        println("Goodbye!")
      } else {
        try {
          println("Result : " + VU.toString(INTP.interp(PS.parse(input))))
        } catch {
          case e: ExceptionMsg =>
            println("Error: " + e.msg)
        }
        run
      }
    }
  }
}
