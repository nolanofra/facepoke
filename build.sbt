import LibraryDependencies._

inThisBuild(
  List(
    organization := "nolanofra",
    developers := List(
      Developer("nolanofra", "Francesco Nolano", "nolanofra@gmail.com", url("https://github.com/nolanofra"))
    ),
    licenses += ("GPL", url("https://www.gnu.org/licenses/gpl-3.0.html")),
    pomIncludeRepository := { _ => false }
  )
)

val projectName = "facepoke"

lazy val projectSettings = Seq(
  name := projectName,
  organization := "com.nolanofra",
  scalaVersion := "2.13.8",
  scalafmtOnCompile := true
)

libraryDependencies ++= Seq(
  http4sServer,
  http4sCirce,
  circeGeneric,
  circeLiteral,
  http4sDsl,
  logback,
  scalaTest
)

lazy val root = (project in file("."))
  .settings(projectSettings)
