package pp201902.project.ExpressionIF

object Bundle {

  sealed abstract class Arg
  case class AVname(x:String) extends Arg
  case class ANname(x:String) extends Arg

  sealed abstract class Bind
  case class BDef(f:String, params:List[Arg], e:Expr) extends Bind
  case class BVal(x:String, e:Expr) extends Bind
  case class BLval(x:String, e:Expr) extends Bind

  sealed abstract class Expr
  case class EInt(n:Int) extends Expr
  case class ETrue() extends Expr
  case class EFalse() extends Expr
  case class ENil() extends Expr
  case class EName(s:String) extends Expr
  case class EIf(econd:Expr, et:Expr, ef:Expr) extends Expr
  case class ECons(eh:Expr, et:Expr) extends Expr
  case class EFst(el:Expr) extends Expr
  case class ESnd(el:Expr) extends Expr
  case class EApp(ef:Expr, eargs:List[Expr]) extends Expr
  case class ELet(bs:List[Bind], eb:Expr) extends Expr
  case class EIsNil(e:Expr) extends Expr
  case class ERmk(bs:List[Bind]) extends Expr
  case class ERfd(rec:Expr, fd:String) extends Expr
  case class EPlus(e1:Expr, e2:Expr) extends Expr
  case class EMinus(e1:Expr, e2:Expr) extends Expr
  case class EMult(e1:Expr, e2:Expr) extends Expr
  case class EEq(e1:Expr, e2:Expr) extends Expr
  case class ELt(e1:Expr, e2:Expr) extends Expr
  case class EGt(e1:Expr, e2:Expr) extends Expr
  
  abstract class ExpUtilIF {
    def toString(e: Expr): String
  }
}
