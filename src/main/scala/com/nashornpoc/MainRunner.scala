package com.nashornpoc

import java.io
import java.io.{File, FileReader}

import groovy.lang._
import groovy.util.GroovyScriptEngine
import javax.script.{Invocable, ScriptEngine, ScriptEngineManager}
import org.codehaus.groovy.control.CompilerConfiguration

import scala.reflect.runtime.universe
import scala.tools.reflect.ToolBox
import scala.util.Random

object MainRunner extends App {

  var i = 0
  var result: String = null
  val benchmarkIterations = 100

  println("\n\n###############################################")
  println(s"############### Benchmark - ${benchmarkIterations} ###############")
  println("###############################################")

  var startTime = System.nanoTime()


/*
    //Javascript parse and execute with ScriptEngine
    println("\n########## Javascript parse and execute with ScriptEngine #########")

    while (i < benchmarkIterations) {
      val engine: ScriptEngine = new ScriptEngineManager(MainRunner.getClass.getClassLoader).getEngineByName("nashorn")
      engine.eval(new FileReader("/home/ashish/workspace/nashorn-poc/src/main/scala/com/nashornpoc/LinePrinter.js"))
      engine.asInstanceOf[Invocable].invokeFunction("sayName", "Ashish Tomer", new Integer(2))
      i += 1
    }

    println(s"The time taken : ${ (System.nanoTime() - startTime) / 1000000} milliseconds")


    //Groovy parse and execute class method with GroovyClassLoader
    println("\n########## Groovy parse and execute with GroovyClassLoader #########\n")

    startTime = System.nanoTime()
    i = 0
    while(i < benchmarkIterations) {
      var groovyClassLoader = new GroovyClassLoader()
      var groovyScript = groovyClassLoader.parseClass(new File("/home/ashish/workspace/nashorn-poc/src/" +
        "main/scala/com/nashornpoc/SampleScript.groovy")) //Use File with GroovyClassLoader to avoid memory leaks
      var groovyObject = groovyScript.newInstance().asInstanceOf[GroovyObject]
      var outputGroovyClassLoader = groovyObject.invokeMethod("scriptSays", Array[Object]("Ashish Tomer", new Integer(2)))

      i += 1
    }
    println(s"The time taken : ${ (System.nanoTime() - startTime) / 1000000} milliseconds")


    val sharedData1 = new Binding() //CAUTION! Binding class is not thread safe
    sharedData1.setProperty("name", "Ashish Tomer")
    sharedData1.setProperty("num", new Integer(2))

    val sharedData2 = new Binding() //CAUTION! Binding class is not thread safe
    sharedData2.setProperty("name", "Ashish Tomer")
    sharedData2.setProperty("num", new Integer(2))


    //Groovy parse and execute script with GroovyShell - not thread-safe
    println("\n########## Groovy parse and execute with GroovyShell - not thread-safe #########\n")
    var groovyShell = new GroovyShell(sharedData1)
    var outputGroovyShell: AnyRef = null
    i = 0
    startTime = System.nanoTime()
    while(i < benchmarkIterations) {
      groovyShell = new GroovyShell(sharedData1)
      outputGroovyShell = groovyShell.evaluate(new File("/home/ashish/workspace/nashorn-poc/src/" +
        "main/scala/com/nashornpoc/SampleGroovyScript.groovy"))
      i += 1
    }
    println(s"The time taken: ${(System.nanoTime() - startTime) / 1000000} milliseconds")

    //Groovy parse and execute script with GroovyShell - thread-safe
    println("\n########## Groovy parse and execute with GroovyShell - thread-safe #########\n")
    val groovyShellWithoutBinding = new GroovyShell()
    var compiledGroovyScript: Script = null
    i = 0
    startTime = System.nanoTime()
    while(i < benchmarkIterations) {
      //Create the different instance of compile Script for each thread, bind its binding and execute the run()
      compiledGroovyScript = groovyShellWithoutBinding.parse(new File("/home/ashish/workspace/nashorn-poc/src/" +
        "main/scala/com/nashornpoc/SampleGroovyScript.groovy"))
      compiledGroovyScript.setBinding(sharedData2)
      compiledGroovyScript.run()
      i += 1
    }

    println(s"The time taken: ${(System.nanoTime() - startTime) / 1000000} milliseconds")

    //Groovy parse and execute class method with GroovyShell
    println("\n########## Groovy parse and execute class method with GroovyShell #########\n")

    startTime = System.nanoTime()

    val config = new CompilerConfiguration()
    config.setScriptBaseClass("SampleScript2")

    val shell = new GroovyShell(MainRunner.getClass.getClassLoader, new Binding(), config)
    val script = shell.parse("scriptSays()")
    script.run

    println(s"The time taken: ${(System.nanoTime() - startTime) / 1000000} milliseconds")


    //Groovy parse and execute class method with ScriptEngine
    println("########## Groovy parse and execute class method with ScriptEngineManager ##########")
    val groovyScriptEngine: ScriptEngine = new ScriptEngineManager().getEngineByName("groovy")
    var outputFromGroovyScriptEngine: AnyRef = null
    var invocable: Invocable = null
    var funArgs: Array[Object] = Array("Ashish Tomer", new Integer(2))

    startTime = System.nanoTime()
    i = 0

    while(i < benchmarkIterations) {
      groovyScriptEngine.eval(new FileReader("/home/ashish/workspace/nashorn-poc/src/" +
        "main/scala/com/nashornpoc/SampleGroovyScript2.groovy"))
      invocable = groovyScriptEngine.asInstanceOf[Invocable]
      outputFromGroovyScriptEngine = invocable.invokeFunction("scriptSays", "Ashish Tomer", new Integer(2))
      i += 1
    }

    println(s"\nThe time taken: ${System.nanoTime() - startTime}")

*/


  println("\n########## Groovy parse and execute script with GroovyShell #########")

  lazy val groovyCodeSource =
    """
      |def scriptSays(String name, int num) {
      |    "Hello $name, from Groovy. randomNumber " * num
      |}
    """.stripMargin


  i = 0
  startTime = System.nanoTime()

  val shell = new GroovyShell()
  var compiledScript: Script = null

  while(i < benchmarkIterations) {
    compiledScript = shell.parse(groovyCodeSource.replace("randomNumber", Random.nextString(10)))
    result = compiledScript.invokeMethod("scriptSays", Array[Object]("Ashish Tomer", new Integer(2))).toString

//    print(s"G$i - ")
//    println(result)

    i += 1
  }
  println(s"The time taken: ${(System.nanoTime() - startTime) / 1000000} ms")



  println("\n########## Javascript parse and execute with ScriptEngine #########")

  lazy val jsCodeSource =
  """
      |var sayName = function(name, num) {
      |    var result = ""
      |    for(var i = 0; i < num; i ++) {
      |        result += "Hello " + name + ", from Javascript. randomString ";
      |    }
      |    return result
      |}
    """.stripMargin

  i = 0
  startTime = System.nanoTime()

  while (i < benchmarkIterations) {
    val engine: ScriptEngine = new ScriptEngineManager(MainRunner.getClass.getClassLoader).getEngineByName("nashorn")
    engine.eval(jsCodeSource.replace("randomString", Random.nextString(10)))
    result = engine.asInstanceOf[Invocable].invokeFunction("sayName", "Ashish Tomer", new Integer(2)).toString

//    print(s"J$i - ")
//    println(result)

    i += 1
  }

  println(s"The time taken : ${ (System.nanoTime() - startTime) / 1000000} milliseconds")


  println("\n########## Scala parse and execute method with Scala reflection #########")

  lazy val scalaCodeSource =
    """
      |new Function2[String, Int, String] {
      |
      |    override def apply(name: String, num: Int): String = {
      |
      |        var counter = num
      |        var result = ""
      |
      |        while(counter > 0) {
      |            result += s"Hello $name, from Scala. "
      |            counter -= 1
      |        }
      |
      |        result
      |      }
      |}
    """.stripMargin


  var runtimeMirror: universe.Mirror = null
  var toolBox: ToolBox[universe.type] = null

  i = 0
  startTime = System.nanoTime()

  while (i < benchmarkIterations) {
    runtimeMirror = scala.reflect.runtime.universe.runtimeMirror(getClass.getClassLoader)
    toolBox = runtimeMirror.mkToolBox()
    result = toolBox.eval(toolBox.parse(scalaCodeSource))
        .asInstanceOf[Function2[String, Int, String]].apply("Ashish Tomer", 2)

//    print(s"S$i - ")
//    println(result)

    i += 1
  }

  println(s"The time taken: ${(System.nanoTime() - startTime) / 1000000} ms")

  println("\n\n")
}
