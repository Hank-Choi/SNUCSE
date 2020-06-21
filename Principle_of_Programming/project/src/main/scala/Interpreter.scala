package pp201902.project.Interpreter

import pp201902.project.InterpreterIF.Bundle._
import pp201902.project.EnvIF.Bundle._
import pp201902.project.ExpressionIF.Bundle._
import pp201902.project.ValueIF.Bundle._
import pp201902.project.ParserIF.Bundle._
import pp201902.project.Common.Bundle._

import scala.annotation.tailrec

object Bundle {
  implicit def interpreterIF[Env](implicit ENV: EnvIF[Env]): InterpreterIF[Env] = new InterpreterIF[Env] {

    def interp(expr: Expr): Val[Env] = {
      var env: List[Env] = Nil

      @tailrec
      def _interp(stack: List[(Int, Expr)], args: List[Val[Env]], scope: Env): Val[Env] = {
        stack match {
          case Nil => args.last
          case head :: next => head match {
            case (count, expr) => expr match {
              case EInt(n: Int) => _interp(next, VInt[Env](n) :: args, scope)
              case ETrue() => _interp(next, VBool[Env](true) :: args, scope)
              case EFalse() => _interp(next, VBool[Env](false) :: args, scope)
              case ENil() => _interp(next, VNil[Env]() :: args, scope)
              case EName(s: String) => {
                if(count==0) {
                  val searchResult = ENV.findItem(scope, s)
                  searchResult._1 match {
                    case EVal(v) => _interp(next, v :: args, scope)
                    case EDef(params: List[Arg], e: Expr) => {
                      _interp(next, VDef(params, e, searchResult._2) :: args, scope)
                    }
                    case ELval(e) => _interp((0,e)::(1,expr)::next,args,scope)
                  }
                }
                else{
                  val argHead::nextArgs = args
                  ENV.modifyEnv(scope,s,argHead)
                  _interp(next,args,scope)
                }
              }
              case EIf(econd: Expr, et: Expr, ef: Expr) => {
                if (count == 0)
                  _interp((0, econd) :: (1, expr) :: next, args, scope)
                else {
                  args match {
                    case argHead :: nextArgs => argHead match {
                      case VBool(true) => _interp((0, et) :: next, nextArgs, scope)
                      case VBool(false) => _interp((0, ef) :: next, nextArgs, scope)
                      case VDef(params, body, env) => _interp((0,body)::(1,expr)::next,nextArgs,scope)
                    }
                  }
                }
              }
              case ECons(eh: Expr, et: Expr) => {
                if (count == 0)
                  _interp((0, et) :: (0, eh) :: (1, expr) :: next, args, scope)
                else {
                  val vh :: vt :: nextArgs = args
                  _interp(next, VCons(vh, vt) :: nextArgs, scope)
                }
              }
              case EFst(el: Expr) => {
                if (count == 0)
                  _interp((0, el) :: (1, expr) :: next, args, scope)
                else {
                  val vl :: nextArgs = args
                  vl match {
                    case VCons(hd, tl) => _interp(next, hd :: nextArgs, scope)
                    case VDef(params, body, env) => _interp((0,body)::(1,expr)::next,nextArgs,scope)
                  }
                }
              }
              case ESnd(el: Expr) => {
                if (count == 0)
                  _interp((0, el) :: (1, expr) :: next, args, scope)
                else {
                  val vl :: nextArgs = args
                  vl match {
                    case VCons(hd, tl) => _interp(next, tl :: nextArgs, scope)
                    case VDef(params, body, env) => _interp((0,body)::(1,expr)::next,nextArgs,scope)
                  }
                }
              }
              case EApp(ef: Expr, eargs: List[Expr]) => {
                if (count == 0) {
                  env = scope :: env
                  val eargsSet = eargs.map(earg => (0, earg))
                  _interp(eargsSet.reverse ::: (0, ef) :: (2 + eargs.length, expr) :: next, args, scope)
                }
                else if (count == 1) {
                  val envHead :: envNext = env
                  env = envNext
                  _interp(next, args, envHead)
                }
                else if (count == 2) {
                  val vf :: nextArgs = args
                  vf match {
                    case VDef(params, body, localScope: Env) => {
                      _interp((0, body) :: (1, expr) :: next, nextArgs, localScope)
                    }
                  }
                }
                else {
                  val vf :: arg1 :: nextArgs = args
                  vf match {
                    case VDef(params, body, localScope: Env) => {
                      val paramHead :: paramNext = params
                      paramHead match {
                        case AVname(x: String) => {
                          if (count == 2 + eargs.length)
                            ENV.pushEmptyFrame(localScope)
                          ENV.addItem(localScope, x, EVal[Env](arg1))
                          _interp((count - 1, expr) :: next, VDef(paramNext, body, localScope) :: nextArgs, scope)
                        }
                        case ANname(x:String) => {
                          if (count == 2 + eargs.length)
                            ENV.pushEmptyFrame(localScope)
                          ENV.addItem(localScope, x, EDef[Env](Nil,eargs(2+eargs.length-count)))
                          _interp((count - 1, expr) :: next, VDef(paramNext, body, localScope) :: nextArgs, scope)
                        }
                      }
                    }
                  }
                }
              }
              case ELet(bs: List[Bind], eb: Expr) => {
                if (count == 0) {
                  ENV.pushEmptyFrame(scope)
                  _interp((1, expr) :: next, args, scope)
                }
                else if (count == 1) {
                  bs match {
                    case Nil => _interp((0, eb) :: next, args, scope)
                    case bindHead :: bindNext => bindHead match {
                      case BDef(f: String, params, e) => {
                        ENV.addItem(scope, f, new EDef[Env](params, e))
                        _interp((1, ELet(bindNext, eb)) :: next, args, scope)
                      }
                      case BVal(x: String, e) => _interp((0, e) :: (2, ELet(bs, eb)) :: next, args, scope)
                      case BLval(x: String, e) => {
                        ENV.addItem(scope, x, new ELval[Env](e))
                        _interp((1,ELet(bindNext, eb))::next,args,scope)
                      }
                    }
                  }
                }
                else {
                  val argHead :: argNext = args
                  val bsHead :: bsNext = bs
                  bsHead match {
                    case BVal(x: String, e) => {
                      ENV.addItem(scope, x, new EVal[Env](argHead))
                      _interp((1, ELet(bsNext, eb)) :: next, argNext, scope)
                    }
                  }
                }
              }
              case EIsNil(e: Expr) => {
                if (count == 0)
                  _interp((0, e) :: (1, expr) :: next, args, scope)
                else {
                  val argHead :: argNext = args
                  argHead match {
                    case VNil() => _interp(next, VBool[Env](true) :: argNext, scope)
                    case VDef(params, body, env) => _interp((0,body)::(1,expr)::next,argNext,scope)
                    case _ => _interp(next, VBool[Env](false) :: argNext, scope)
                  }
                }
              }
              case ERmk(bs:List[Bind]) => {
                if (count == 0) {
                  env=scope::env
                  _interp((1, expr) :: next, args, ENV.pushEmptyFrame(ENV.emptyEnv()))
                }
                else if (count == 1) {
                  bs match {
                    case Nil => {
                      val envHead::envNext = env
                      env = envNext
                      _interp(next, VRec(scope, envHead) :: args, envHead)
                    }
                    case bindHead :: bindNext => bindHead match {
                      case BDef(f: String, params, e) => {
                        ENV.addItem(scope, f, new EDef[Env](params, e))
                        _interp((1, ERmk(bindNext)) :: next, args, scope)
                      }
                      case BVal(x: String, e) => _interp((0, e) :: (2, ERmk(bs)) :: next, args, scope)
                      case BLval(x: String, e) => {
                        ENV.addItem(scope, x, new ELval[Env](e))
                        _interp((1,ERmk(bindNext))::next,args,scope)
                      }
                    }
                  }
                }
                else {
                  val argHead :: argNext = args
                  val bsHead :: bsNext = bs
                  bsHead match {
                    case BVal(x: String, e) => {
                      ENV.addItem(scope, x, new EVal[Env](argHead))
                      _interp((1, ERmk(bsNext)) :: next, argNext, scope)
                    }
                  }
                }
              }
              case ERfd(rec:Expr, fd:String) => {
                if(count == 0){
                  _interp((0,rec)::(1,expr)::next,args,scope)
                }
                else if(count == 1){
                  val argsHead::argsNext = args
                  argsHead match {
                    case VRec(fields, env2) => {
                      env=scope::env
                      _interp((0,EName(fd))::(2,expr)::next,argsNext,fields)
                    }
                  }
                }
                else{
                  val envHead::envNext = env
                  env = envNext
                  _interp(next,args,envHead)
                }
              }
              case EPlus(e1: Expr, e2: Expr) => {
                if (count == 0)
                  _interp((0, e2) :: (0, e1) :: (1, expr) :: next, args, scope)
                else {
                  val arg1 :: arg2 :: argNext = args
                  arg1 match {
                    case VInt(n1) => {
                      arg2 match {
                        case VInt(n2) => {
                          _interp(next, VInt[Env](n1 + n2) :: argNext, scope)
                        }
                        case VDef(params, body, env) => _interp((0,body) :: (0,e1) :: (1, expr) :: next, argNext, scope)
                      }
                    }
                    case VDef(params1, body1, env1) => arg2 match {
                      case VInt(n2) =>  _interp ((0,body1) :: (1, expr) :: next, arg2 :: argNext, scope)
                      case VDef(params2, body2, env2) =>_interp((0,body2)::(0,body1)::(1,expr)::next,argNext,scope)
                    }
                  }
                }
              }
              case EMinus(e1: Expr, e2: Expr) => {
                if (count == 0)
                  _interp((0, e2) :: (0, e1) :: (1, expr) :: next, args, scope)
                else {
                  val arg1 :: arg2 :: argNext = args
                  arg1 match {
                    case VInt(n1) => {
                      arg2 match {
                        case VInt(n2) => {
                          _interp(next, VInt[Env](n1 - n2) :: argNext, scope)
                        }
                        case VDef(params, body, env) => _interp((0,body) :: (0,e1) :: (1, expr) :: next, argNext, scope)
                      }
                    }
                    case VDef(params1, body1, env1) => arg2 match {
                      case VInt(n2) =>  _interp ((0,body1) :: (1, expr) :: next, arg2 :: argNext, scope)
                      case VDef(params2, body2, env2) =>_interp((0,body2)::(0,body1)::(1,expr)::next,argNext,scope)
                    }
                  }
                }
              }
              case EMult(e1: Expr, e2: Expr) => {
                if (count == 0)
                  _interp((0, e2) :: (0, e1) :: (1, expr) :: next, args, scope)
                else {
                  val arg1 :: arg2 :: argNext = args
                  arg1 match {
                    case VInt(n1) => {
                      arg2 match {
                        case VInt(n2) => {
                          _interp(next, VInt[Env](n1 * n2) :: argNext, scope)
                        }
                        case VDef(params, body, env) => _interp((0, body) :: (0, e1) :: (1, expr) :: next, argNext, scope)
                      }
                    }
                    case VDef(params1, body1, env1) => arg2 match {
                      case VInt(n2) => _interp((0, body1) :: (1, expr) :: next, arg2 :: argNext, scope)
                      case VDef(params2, body2, env2) => _interp((0, body2) :: (0, body1) :: (1, expr) :: next, argNext, scope)
                    }
                  }
                }
              }
              case EEq(e1: Expr, e2: Expr) => {
                if (count == 0)
                  _interp((0, e2) :: (0, e1) :: (1, expr) :: next, args, scope)
                else {
                  val arg1 :: arg2 :: argNext = args
                  arg1 match {
                    case VInt(n1) => {
                      arg2 match {
                        case VInt(n2) => {
                          if (n1 == n2) _interp(next, VBool[Env](true) :: argNext, scope)
                          else _interp(next, VBool[Env](false) :: argNext, scope)
                        }
                        case VDef(params, body, env) => _interp((0, body) :: (0, e1) :: (1, expr) :: next, argNext, scope)
                      }
                    }
                    case VDef(params1, body1, env1) => arg2 match {
                      case VInt(n2) => _interp((0, body1) :: (1, expr) :: next, arg2 :: argNext, scope)
                      case VDef(params2, body2, env2) => _interp((0, body2) :: (0, body1) :: (1, expr) :: next, argNext, scope)
                    }
                  }
                }
              }
              case ELt(e1: Expr, e2: Expr) => {
                if (count == 0)
                  _interp((0, e2) :: (0, e1) :: (1, expr) :: next, args, scope)
                else {
                  val arg1 :: arg2 :: argNext = args
                  arg1 match {
                    case VInt(n1) => {
                      arg2 match {
                        case VInt(n2) => {
                          if (n1 < n2) _interp(next, VBool[Env](true) :: argNext, scope)
                          else _interp(next, VBool[Env](false) :: argNext, scope)
                        }
                        case VDef(params, body, env) => _interp((0, body) :: (0, e1) :: (1, expr) :: next, argNext, scope)
                      }
                    }
                    case VDef(params1, body1, env1) => arg2 match {
                      case VInt(n2) => _interp((0, body1) :: (1, expr) :: next, arg2 :: argNext, scope)
                      case VDef(params2, body2, env2) => _interp((0, body2) :: (0, body1) :: (1, expr) :: next, argNext, scope)
                    }
                  }
                }
              }
              case EGt(e1: Expr, e2: Expr) => {
                if (count == 0)
                  _interp((0, e2) :: (0, e1) :: (1, expr) :: next, args, scope)
                else {
                  val arg1 :: arg2 :: argNext = args
                  arg1 match {
                    case VInt(n1) => {
                      arg2 match {
                        case VInt(n2) => {
                          if (n1 > n2) _interp(next, VBool[Env](true) :: argNext, scope)
                          else _interp(next, VBool[Env](false) :: argNext, scope)
                        }
                        case VDef(params, body, env) => _interp((0, body) :: (0, e1) :: (1, expr) :: next, argNext, scope)
                      }
                    }
                    case VDef(params1, body1, env1) => arg2 match {
                      case VInt(n2) => _interp((0, body1) :: (1, expr) :: next, arg2 :: argNext, scope)
                      case VDef(params2, body2, env2) => _interp((0, body2) :: (0, body1) :: (1, expr) :: next, argNext, scope)
                    }
                  }
                }
              }
            }
          }
        }
      }
      _interp((0, expr) :: Nil, Nil, ENV.emptyEnv())
    }
  }
}
