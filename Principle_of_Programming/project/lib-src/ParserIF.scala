package pp201902.project.ParserIF

import pp201902.project.ExpressionIF.Bundle._

object Bundle {
  
  abstract class ParserIF {
    def parse(code: String): Expr
  }
}
