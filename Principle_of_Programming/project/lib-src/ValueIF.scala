package pp201902.project.ValueIF
import pp201902.project.ExpressionIF.Bundle._

object Bundle {
  sealed abstract class Val[Env]
  case class VInt[Env](n:Int) extends Val[Env]
  case class VBool[Env](b:Boolean) extends Val[Env]
  case class VNil[Env]() extends Val[Env]
  case class VCons[Env](hd:Val[Env], tl:Val[Env]) extends Val[Env]
  case class VDef[Env](params:List[Arg], body:Expr, env:Env) extends Val[Env]
  case class VRec[Env](fields:Env, env:Env) extends Val[Env]

  abstract class ValUtilIF[Env] {
    def toString(v: Val[Env]): String
    def toInt(v: Val[Env]) : Option[Int]
    def toBool(v: Val[Env]) : Option[Boolean]
    def toPair(v: Val[Env]) : Option[(Val[Env], Val[Env])]
    def isNil(v: Val[Env]) : Boolean
    def isFun(v: Val[Env]) : Boolean
    def isRec(v: Val[Env]) : Boolean
  }
}
