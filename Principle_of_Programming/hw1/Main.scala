package pp201902.hw1
import scala.annotation.tailrec

object Main {
  /*
   Implement given functions, which is currently blank. (???)
   */

  /*
   Exercise 1: Perfect, Deficient, Abundant number
   A) A pefect number is a natural number which is equal to the sum of its
   divisors excluding itself.
   Similarly, a number is a deficient number (abundant number), when the number
   is smaller (bigger) than the sum of its divisors excluding itself.
   For example, 6, 28, and 496 are perfect numbers; 1, 4, and 10 are deficient
   numbers; 12, 18 and 20 are abudant number.
   Implement a function that returns 0, -1, and 1, when a given natural number n is
   a perfect number, a deficiet number, and a abundant number respectively.
   For any input n s.t. 1 <= n <= 10^3, your program should terminate within 1 second.
   */
  def PDAcheckA(n: Int): Int = {
    def _PDAcheckA(idx: Int): Int = {
      if (n == idx) 0
      else if (n % idx == 0) idx + _PDAcheckA(idx + 1)
      else _PDAcheckA(idx + 1)
    }

    val sum = _PDAcheckA(1)
    if (sum == n) 0
    else if (sum < n) -1
    else 1
  }

  /*
   B) Implement A) using tail recursion.
   The waste of the stack space can be avoided by using tail recursion.
   For any input n s.t. 1 <= n <= 10^5, your program should terminate within 5 seconds.
   */
  def PDAcheckB(n: Int): Int = {
    @tailrec
    def _PDAcheckB(idx: Int, sum: Int): Int = {
      if (n == idx) sum
      else if (n % idx == 0) _PDAcheckB(idx + 1, sum + idx)
      else _PDAcheckB(idx + 1, sum)
    }

    val sum = _PDAcheckB(1, 0)
    if (sum == n) 0
    else if (sum < n) -1
    else 1
  }

  /*
   Exercise 2: Path count
   Given n, m, and three inaccessable points, compute the number of the North-East lattice paths
   from (0, 0) to (n, m).
   (See https://en.wikipedia.org/wiki/Lattice_path)
   */
  def count(n: Int, m: Int, x: (Int, Int), y: (Int, Int), z: (Int, Int)): BigInt = {
    if (n  < 0 || m < 0) 0
    else if (x == (0, 0) || y == (0, 0) || z == (0, 0)) 0
    else if (n == 0 && m == 0) 1
    else count(n - 1, m, (x._1 - 1, x._2), (y._1 - 1, y._2), (z._1 - 1, z._2)) +
         count(n, m - 1, (x._1, x._2 - 1), (y._1, y._2 - 1), (z._1, z._2 - 1))
  }

  /*
   Exercise 3: Newton's method
   Newton's method is root-finding algorithm which produces successively better
   approximations to the roots (or zeroes) of a real-valued function.
   (See https://en.wikipedia.org/wiki/Newton%27s_method)
   Given a function f, a derivative of f, and a start point, solve equation f(x) = 0
   using Newton's method starting from the given start point.
   For a solution x, |f(x)| should be smaller than 0.01.
   */
 def solver(f: Double => Double, diff_f: Double => Double, start: Double) = {
    def solverIter(guess: Double): Double = {
      if (isGoodEnough(guess)) guess
      else solverIter(improve(guess))
    }

    def isGoodEnough(guess: Double) = {
      val diff = f(guess)
      diff > -0.01 && diff < 0.01
    }

    def improve(guess: Double) =
      guess - f(guess) / diff_f(guess)

    solverIter(start)
  }
}
