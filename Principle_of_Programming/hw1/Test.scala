import scala.util._
import pp201902.hw1.Main._

trait API {
  val PASS = "[[PASS]]"
  val FAIL = "[[FAIL]]"
  val DELIMITER = "\n"
}

class Executor(val probNum: Int) {

  def execProb1(testNum:Int) = {
    TryAll{
      testNum match{
        case 0 => PDAcheckA(6) == 0
        case 1 => PDAcheckA(4) == -1
        case 2 => PDAcheckA(12) == 1
        case 3 => PDAcheckA(28) == 0
        case 4 => PDAcheckA(270) == 1
        case 5 => PDAcheckA(496) == 0
        case 6 => PDAcheckA(571) == -1
        case 7 => PDAcheckA(836) == 1
        case 8 => PDAcheckA(935) == -1
        case 9 => PDAcheckA(945) == 1
        case 10 => PDAcheckA(999) == -1
      }
    }
  }

  def execProb2(testNum:Int) = {
    TryAll{
      testNum match{
        case 0 => PDAcheckB(6) == 0
        case 1 => PDAcheckB(496) == 0
        case 2 => PDAcheckB(8128) == 0
        case 3 => PDAcheckB(935) == -1
        case 4 => PDAcheckB(9949) == -1
        case 5 => PDAcheckB(75201) == -1
        case 6 => PDAcheckB(99971) == -1
        case 7 => PDAcheckB(945) == 1
        case 8 => PDAcheckB(2210) == 1
        case 9 => PDAcheckB(15015) == 1
        case 10 => PDAcheckB(95760) == 1
      }
    }
  }

  def execProb3(testNum:Int) = {
    TryAll{
      testNum match{
        case 0 => count(3, 3, (0, 3), (1, 2), (3, 0)) == 9
        case 1 => count(5, 6, (2, 1), (2, 3), (4, 2)) == 124
        case 2 => count(10, 10, (3, 4), (1, 8), (6, 7)) == 88641
        case 3 => count(10, 10, (2, 6), (8, 6), (6, 6)) == 76991
        case 4 => count(8, 7, (3, 2), (1, 5), (6, 7)) == 2669
        case 5 => count(50, 2, (3, 1), (20, 2), (30, 1)) == 408
      }
    }
  }

  def abs(x:Double) =
    if(x > 0) x else -x

  def execProb4(testNum:Int) = {
    TryAll{
      val f1 = (x: Double) => x * x - 2
      val f2 = (x: Double) => x * x * x - 6 * x * x + 11 * x - 6
      val f3 = (x: Double) => x * x * x + 2 * x * x + 3 * x + 6
      val f4 = (x: Double) => x * x - 10 * x + 50 - 41 / x

      def x1 = solver(f1, (x: Double) => 2 * x, 1)
      def x2 = solver(f2, (x: Double) => 3 * x * x - 12 * x + 11, 0)
      def x3 = solver(f2, (x: Double) => 3 * x * x - 12 * x + 11, 10)
      def x4 = solver(f3, (x: Double) => 3 * x * x + 4 * x + 3, 0)
      def x5 = solver(f3, (x: Double) => 4 * x * x + 4 * x + 3, 5)
      def x6 = solver(f4, (x: Double) => 2 * x - 10 + 41 / x / x, 10)

      testNum match{
        case 0 => f1(x1) > -0.01 && f1(x1) < 0.01 && abs(x1 - 1.414) < abs(x1 + 1.414)
        case 1 => f2(x2) > -0.01 && f2(x2) < 0.01 && abs(x2 - 1) < abs(x2 - 2)
        case 2 => f2(x3) > -0.01 && f2(x3) < 0.01 && abs(x3 - 3) < abs(x3 - 2)
        case 3 => f3(x4) > -0.01 && f3(x4) < 0.01 && abs(x4 + 2) < 1
        case 4 => f3(x5) > -0.01 && f3(x5) < 0.01 && abs(x5 + 2) < 1
        case 5 => f4(x6) > -0.01 && f4(x6) < 0.01 && abs(x6 - 1) < 1
      }
    }
  }

  def exec(testNum: Int): Try[Boolean] =
    probNum match {
      case 1 => execProb1(testNum)
      case 2 => execProb2(testNum)
      case 3 => execProb3(testNum)
      case 4 => execProb4(testNum)
      case _ => new Failure(new Exception("System Error!!! No probNum matched."))
    }
}

//Need to capture stackoverflow error.
//http://stackoverflow.com/questions/38053222/why-does-scala-try-not-catching-java-lang-stackoverflowerror
object TryAll {
  def apply[K](f: => K): Try[K] = {
    try {
      Success(f)
    }
    catch {
      case e: Throwable => Failure(e)
    }
  }
}

object test extends API {

  def main(args: Array[String]): Unit = {
    val probNum = args(0).toInt
    val testNum = args(1).toInt

    val tester = new Executor(probNum)
    tester.exec(testNum) match {
      case Success(result) => {
        if(result) println(s"${PASS}")
        else println(s"${FAIL}")
      }
      case Failure(e) => {
        println(s"${FAIL} with error ${e}")
      }
    }

  }
}
