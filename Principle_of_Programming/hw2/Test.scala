package pp201902.hw2test
import pp201902.hw2.Main._
import pp201902.hw2.Data._

object Test extends App {
  def print_result(b:Boolean) : Unit =
    if (b) println("O") else println("X")

  def listIntToIList(xs: List[Int]): IList =
    xs match {
      case (h :: t) => ICons(h, listIntToIList(t))
      case Nil => INil()
    }
  // Problem 1
  {
    val a = listIntToIList(List(1, 2, 3, 4))
    val b = listIntToIList(List(2, 4, 6, 8))
    print_result(map(a)(_ * 2) == b)
  }

  // Problem 2
  print_result(calculator(EOp(OpSub(),EInt(62),EInt(500))) == -438)
  print_result(calculator(EOp(OpSub(),EInt(5),EOp(OpMul(),EInt(8),EInt(3)))) == -19)
  print_result(calculator(EOp(OpAdd(),EOp(OpAdd(),EInt(3),EInt(88)),EOp(OpAdd(),EOp(OpAdd(),EInt(22),EOp(OpMul(),EInt(2),EInt(6))),EInt(7)))) == 132)

  // Problem 3
  print_result(lexer("(42)".toList) == List(TkLPar(), TkInt(42), TkRPar()))
  print_result(lexer("62+500".toList) == List(TkInt(62), TkBop(OpAdd()), TkInt(500)))
  print_result(lexer("(81310-2422)*(13-421-4)".toList) == List(TkLPar(), TkInt(81310), TkBop(OpSub()), TkInt(2422), TkRPar(), TkBop(OpMul()), TkLPar(), TkInt(13), TkBop(OpSub()), TkInt(421), TkBop(OpSub()), TkInt(4), TkRPar()))

  // Problem 4
  println("parser(List(TkInt(4), TkBop(OpSub()), TkInt(8), TkBop(OpMul()), TkInt(9))) is equal to " + parser(List(TkInt(4), TkBop(OpSub()), TkInt(8), TkBop(OpMul()), TkInt(9))))
  println("parser(List(TkInt(8), TkBop(OpAdd()), TkInt(9), TkBop(OpMul()), TkLPar(), TkInt(8), TkBop(OpAdd()), TkInt(4), TkRPar(), TkBop(OpMul()), TkInt(3), TkBop(OpSub()), TkInt(9), TkBop(OpMul()), TkInt(7))) is equal to " + parser(List(TkInt(8), TkBop(OpAdd()), TkInt(9), TkBop(OpMul()), TkLPar(), TkInt(8), TkBop(OpAdd()), TkInt(4), TkRPar(), TkBop(OpMul()), TkInt(3), TkBop(OpSub()), TkInt(9), TkBop(OpMul()), TkInt(7))))
  println("parser(List(TkLPar(), TkInt(5), TkBop(OpMul()), TkInt(9))) is equal to " + parser(List(TkLPar(), TkInt(5), TkBop(OpMul()), TkInt(9))))

}
