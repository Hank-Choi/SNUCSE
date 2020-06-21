package pp201902.hw2.Data

sealed abstract class IList
case class INil() extends IList
case class ICons(hd: Int, tl: IList) extends IList

sealed abstract class Bop
case class OpAdd() extends Bop
case class OpSub() extends Bop
case class OpMul() extends Bop

sealed abstract class Token
case class TkBop(o: Bop) extends Token
case class TkInt(n: Int) extends Token
case class TkLPar() extends Token
case class TkRPar() extends Token

sealed abstract class Exp
case class EInt(i: Int) extends Exp
case class EOp(o: Bop, lhs: Exp, rhs: Exp) extends Exp
case class EError() extends Exp
