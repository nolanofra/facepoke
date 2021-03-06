import sbt._

object LibraryDependencies {

  val catsVersion = "3.3.11"
  val http4sVersion = "0.23.11"
  val circeVersion = "0.14.1"
  val logbackVersion = "1.2.11"
  val scalaTestVersion = "3.2.11"
  val typeSafeConfigVersion = "1.4.2"
  val catsEffectScalaTestVersion = "0.5.4"
  val munitCatsEffectVersion = "1.0.7"
  val testContainerVersion = "0.40.5"
  val mockserverClientVersion = "5.13.2"
  val sttpCientVersion = "3.5.2"

  val catsEffect = "org.typelevel" %% "cats-effect" % catsVersion
  val http4sServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
  val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion
  val http4sClient = "org.http4s" %% "http4s-blaze-client" % http4sVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeLiteral = "io.circe" %% "circe-literal" % circeVersion
  val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
  val logback = "ch.qos.logback" % "logback-classic" % logbackVersion % Runtime
  val typeSafeConfig = "com.typesafe" % "config" % typeSafeConfigVersion
  val testContainers = "com.dimafeng" %% "testcontainers-scala-scalatest" % testContainerVersion
  val mockServerClient = "org.mock-server" % "mockserver-client-java" % mockserverClientVersion
  val mockServer = "com.dimafeng" %% "testcontainers-scala-mockserver" % testContainerVersion
  val munitCatsEffect = "org.typelevel" %% "munit-cats-effect-3" % munitCatsEffectVersion
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  val sttpClient = "com.softwaremill.sttp.client3" %% "core" % sttpCientVersion
}
