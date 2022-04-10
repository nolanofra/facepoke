import sbt._

object LibraryDependencies {

  val http4sVersion = "0.23.11"
  val circeVersion = "0.14.1"
  val logbackVersion = "1.2.11"
  val scalaTestVersion = "3.2.11"

  val http4sServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeLiteral = "io.circe" %% "circe-literal" % circeVersion
  val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
  val logback = "ch.qos.logback" % "logback-classic" % logbackVersion % Runtime
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test
}
