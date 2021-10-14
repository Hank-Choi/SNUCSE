package pp201902.project.Env

import pp201902.project.EnvIF.Bundle._

import pp201902.project.Common.Bundle._
import pp201902.project.ValueIF.Bundle._

object Bundle {

  // implement here!
  implicit val envIF: EnvIF[Env] = new EnvIF[Env] {

    def emptyEnv(): Env = {
      new Env
    }

    def pushEmptyFrame(env: Env): Env = env.addFrame()

    def popFrame(env: Env): Env = env.pop()

    def addItem(env: Env, name: String, item: EnvItem[Env]): Env = env.add(name, item)

    def findItem(env: Env, name: String): (EnvItem[Env], Env) = env.find(name)

    def modifyEnv(env: Env, name: String, v: Val[Env]): Unit = env.modify(name,v)
  }


  class Env (constFrame:List[Frame]){
    def this() = this(Nil)
    var frames: List[Frame] = constFrame
    def add(name:String, item: EnvItem[Env]):Env = {
      frames match{
        case head :: next => head.add(name,item)
      }
      this
    }
    def merge(env: Env):Env = {
      new Env(this.frames ::: env.frames)
    }
    def addFrame():Env ={
      frames = new Frame() :: frames
      this
    }
    def find(name: String): (EnvItem[Env], Env) = {
      val frameIt=frames.iterator
      val scopeEnv=new Env(frames)
      while(frameIt.hasNext){
        val f = frameIt.next().find(name)
        if(f._1){
          return (f._2,scopeEnv)
        }
        scopeEnv.pop()
      }
      (frameIt.next().find(name)._2,scopeEnv)
    }
    def modify(name: String,v:Val[Env]) :Unit= {
      val frameIt=frames.iterator
      while(frameIt.hasNext){
        frameIt.next().modify(name,v)
      }
    }
    def pop() :Env = {
      frames match {
        case head::next => frames=next
      }
      this
    }
  }


  class Frame {
    var nameSet: List[(String,EnvItem[Env])] = Nil
    def add(name: String, item: EnvItem[Env]) = {
      nameSet = (name,item) :: nameSet
    }

    def find(name: String): (Boolean,EnvItem[Env]) = {
      val frameIt = nameSet.iterator
      while (frameIt.hasNext) {
        val frame = frameIt.next
        if (frame._1 == name) {
          return (true,frame._2)
        }
      }
      (false,new EVal[Env](VNil()))
    }
    def modify(name: String,v:Val[Env]) = {
      nameSet = nameSet.map(tuple=> {
        if(tuple._1 == name)
          (name,EVal[Env](v))
        else
          tuple
      })
    }
  }

}
