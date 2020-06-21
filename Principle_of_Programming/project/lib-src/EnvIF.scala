package pp201902.project.EnvIF
import pp201902.project.ExpressionIF.Bundle._
import pp201902.project.ValueIF.Bundle._

object Bundle {
  sealed abstract class EnvItem[Env]
  case class EDef[Env](params: List[Arg], e:Expr) extends EnvItem[Env]
  case class EVal[Env](v:Val[Env]) extends EnvItem[Env]
  case class ELval[Env](e:Expr) extends EnvItem[Env]

  abstract class EnvIF[Env] {
    def emptyEnv(): Env
    def pushEmptyFrame(env: Env): Env
    def popFrame(env: Env): Env
    def addItem(env: Env, name: String, item: EnvItem[Env]) : Env
    def findItem(env: Env, name: String): (EnvItem[Env], Env)
    def modifyEnv(env:Env, name:String, v:Val[Env]) : Unit
  }
}

