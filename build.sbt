import LibraryDependencies.{ circeGeneric, http4sCirce, http4sDsl, http4sServer }

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
  http4sDsl
)

lazy val root = (project in file("."))
  .settings(projectSettings)
