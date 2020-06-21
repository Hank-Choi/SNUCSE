package pp201902.project

import pp201902.project.Env.Bundle._
import pp201902.project.Expression.Bundle._
import pp201902.project.Parser.Bundle._
import pp201902.project.Value.Bundle._
import pp201902.project.Interpreter.Bundle._
import pp201902.project.Test.Bundle._

import pp201902.project.TestIF.Bundle._

import org.scalatest.FunSuite

class InterpreterTest extends FunSuite {
  test("basic - 1") {
    assert(implicitly[TestIF].check("(fst (cons 1 2))", "1"))
  }
  test("basic - 2") {
    assert(implicitly[TestIF].check("(if true 10 20)", "10"))
  }
  test("basic - 3") {
    assert(implicitly[TestIF].check("(let ((val p (cons 1 (cons true nil)))) (cons 0 p))", "(cons 0, (cons 1, (cons true, nil)))"))
  }
  test("basic - 4") {
    assert(implicitly[TestIF].check("(let ((def f (x (by-name y)) (+ x y))) (app f 2 3))", "5"))
  }
  test("basic - 5") {
    assert(implicitly[TestIF].check("(let ((def f (x) (if (= x 0) 0 (+ x (app f (- x 1)))))) (let ((val g f)) (app g 5)))", "15"))
  }
  test("basic - 6") {
    assert(implicitly[TestIF].check("(let ((val a 10) (val b (+ a 1))) (* b 3))", "33"))
  }
  test("basic - 7") {
    assert(implicitly[TestIF].check("(rfd (rmk (lazy-val abc (+ 3 4))) abc)", "7"))
  }
  test("basic - 8") {
    assert(implicitly[TestIF].check("(let ((def x () b) (lazy-val a (app x)) (val b 5)) a)", "5"))
  }
  test("basic - 9") {
    assert(implicitly[TestIF].check("(nil? nil)", "true"))
  }
  test("basic - 10") {
    assert(implicitly[TestIF].check("(nil? (cons true nil))", "false"))
  }
  test("basic - 11") {
    assert(implicitly[TestIF].check("(let ((val x 1)) (let ((def f (y) (+ x y))) (let ((val g f)) (let ((val x 2)) (app g 3)))))", "4"))
  }

}
