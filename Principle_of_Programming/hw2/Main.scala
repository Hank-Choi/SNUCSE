package pp201902.hw2
import pp201902.hw2.Data._

object Main {
  /*
   Implement given functions, which is currently blank. (???)
   */

  /*
   Exercise 1: IList Map
   Write a map function that applies the given function to all elements of the given IList.
   */
  def map(xs: IList)(f: Int => Int): IList = 
    xs match{
      case INil() => false
      case ICons(hd, tl) => ICons(f(hd), map(tl)(f))
    }
  

  /*
    Exercise 2: Exp calculator 
    Given an expression (Exp), calculate the result in Int value.
    EAdd, ESub, and EMul classes represents operators "+", "-", and "*",
    respectively.
    EError is an erroneous case, though, you don't have to consider such case in
    this problem.
    ex)
    calculator(EOp(OpAdd(), Eint(1), Eint(2))) = 3
    (*) Syntax of Exp (see Data.scala)
    Exp = n in Int
        | Op e1 e2
        | Error
    Op  = +
        | -
        | *
    
  */
  def calculator(e: Exp): Int = 
    e match{
      case EInt(i) => i
      case EOp(o, lhs, rhs) => 
        o match{
          case OpAdd() => calculator(lhs) + calculator(rhs)
          case OpSub() => calculator(lhs) - calculator(rhs)
          case OpMul() => calculator(lhs) * calculator(rhs)
        }
    }
  


   /*
    Exercise 3: Lexical Analysis - Lexer 
    Implement a function that converts a list of characters into a list of Tokens.
    There are 6 kinds of tokens, which are integers, "+", "-", "*", "(", and ")".
    
    ex) Lexer({'6', '2', '+', '5', '0', '0'}) => {TkInt(62), TkBop(OpAdd()), TkInt(500)}
        Lexer({'(', '6', '2', '+', '5', '0', '0', ')', '-', '4'}) => {TkLPar(), TkInt(62), TkBop(OpAdd()), TkInt(500), TkRPar(), TkBop(OpSub()), TkInt(4)}
    (Caution)
    1. Whitespaces separate tokens.
      ex)
      33 => TkInt(33)
      3 3 => TkInt(3) TkInt(3)
      
    2. Only '0' ~ '9', '+', '-', '*', '(', ')', and ' ' will be given as an input.
    3. Lexer does not check the grammar.
      ex)
      3 3 + 4 => TkInt(3) TkInt(3) TkBop(OpAdd()) TkInt(4)
      (((3 + 4) => TkLPar() TkLPar() TkLPar() TkInt(3) TkBop(OpAdd()) TkInt(4) TkRPar()
    4. There is no unary operator.
      ex)
      -5 => TkBop(OpSub()) TkInt(5)
    (*) Tokens (see Data.scala)
    Token = n in Int (n >= 0)
          | (
          | )
          | Op
    Op    = +
          | -
          | *
    (*) type "Char" consists characters such as '7', 'a', '(', ...
    For usage, see below "char_to_int".
    (*) "List" is an algebraic datatype which is defined as below.
      sealed abstract class List[A]
      case class Nil() extends List[A]
      case class Cons(hd: A, tl: List[A]) extends List[A]
    Scala provides a special syntax for List.
    - "Nil" means "Nil()", and
    - "hd :: tl" means "Cons(hd, tl)".
    For usage, see below "drop_two".
      def drop_two(l: List[Int]): List[Int] =
        l match {
          case hd1::tl1 =>
            tl1 match {
              case hd2::tl2 => tl2
              case Nil => Nil
            }
          case _ => Nil
        }
  */
  /* returns the corresponding integer.
  returns -1 if the input is not a digit. */
  def char_to_int(c: Char): Int =
    c match {
      case '0' => 0
      case '1' => 1
      case '2' => 2
      case '3' => 3
      case '4' => 4
      case '5' => 5
      case '6' => 6
      case '7' => 7
      case '8' => 8
      case '9' => 9
      case _ => -1
    }

  def lexer(l: List[Char]): List[Token] = {
    def int_digit(a:Int,b:Int,n:Int) = 
      if(a/n < 0) n
      else int_digit(a,b,10*n)

    l match{
      case Nil => Nil
      case hd::tl if (char_to_int(hd)==-1) => 
        hd match{
          case '(' => TkLPar()::lexer(tl)
          case ')' => TKRPar()::lexer(tl)
          case '+' => TkBop(OpAdd())::lexer(tl)
          case '-' => TkBop(OpSub())::lexer(tl)
          case '*' => TkBop(OpMul())::lexer(tl)
          case ' ' => lexer(tl)
        }
      case hd::tl => 
        val lex = lexer(tl)
        lex match{
          case TkInt(num)::subList => TkInt(hd*int_digit(num,10)+num)::subList
          case _ => TkInt(hd)::lex
        }
    }
  }


  /*
    Exercise 4: Syntax Analysis - Parser 
    Implement a function that converts a list of tokens to an expression (Exp).
    The result of calculating the converted Exp should be the same as the result
    of calculating the original expression in a list of characters.
    In this problem, integers, "+", "-", "*", "(", and ")" will be given as tokens.
    Note that the order of operations is as in common sense.
    ex)
    calculator(parser({TkInt(62), TkBop(OpSub()), TkInt(500), TkBop(OpAdd()), TkInt(4)})
      = 62 - 500 + 4 = -434
    calculator(parser({TkInt(62), TkBop(OpSub()), TkLPar(), TkInt(500), TkBop(OpAdd()), TkInt(4), TkRPar()})
      = 62 - (500 + 4) = -442
    calculator(parser({TkLPar(), TkInt(62), TkBop(OpSub()), TkInt(500), TkRPar(), TkBop(OpMul()), TkInt(4)})
      = (62 - 500) * 4 = -1752
    calculator(parser({TkInt(62), TkBop(OpSub()), TkInt(500), TkBop(OpMul()), TkInt(4)})
      = 62 - 500 * 4 = -1938
    
    (Caution)
    1. Parser must be able to handle gramatical errors of token lists.
      Note that the result of parsing an erroneous token list should be Error
      itself, rather than an expression containing Error.
      ex)
      3 3 + 4 => EError()
      (((3 + 4) => EError()
    (*) Tokens (see Data.scala)
    Token = n in Int (n >= 0)
          | (
          | )
          | Op
    Op    = +
          | -
          | *
    (*) Syntax of Exp (see Data.scala)
    Exp = n in Int
        | Op e1 e2
        | Error
    (*) "List" is an algebraic datatype which is defined as below.
      sealed abstract class List[A]
      case class Nil() extends List[A]
      case class Cons(hd: A, tl: List[A]) extends List[A]
    Scala provides a special syntax for List.
    - "Nil" means "Nil()", and
    - "hd :: tl" means "Cons(hd, tl)".
    For usage, see below "drop_two".
      def drop_two(l: List[Int]): List[Int] =
        l match {
          case hd1::tl1 =>
            tl1 match {
              case hd2::tl2 => tl2
              case Nil => Nil
            }
          case _ => Nil
        }
  */
  def parser(l: List[Token]): Exp = {
    def 

    l match{
      case Nil => Nil
      case hd::tl =>
        hd match{
          case TkInt
          case TkBop
          case TkLPar() //expression 튀어나오면 그냥 반환
          case TKRPar()
    //EOp(o, lhs, parser(child))
  }


}
