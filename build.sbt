name := "nashorn-poc"
version := "0.1"
scalaVersion := "2.12.6"

val scalaLibrary = "org.scala-lang" % "scala-library" % "2.12.6"
val scalaCompiler = "org.scala-lang" % "scala-compiler" % "2.12.6"
val groovy = "org.codehaus.groovy" % "groovy-all" % "2.4.15"

lazy val `nashorn-poc` = (project in file("."))
  .settings(libraryDependencies ++= Seq( scalaLibrary, scalaCompiler, groovy ))
