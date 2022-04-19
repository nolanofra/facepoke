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
  catsEffect,
  http4sServer,
  http4sCirce,
  http4sClient,
  circeGeneric,
  circeLiteral,
  http4sDsl,
  logback,
  typeSafeConfig,
  scalaTest,
  munitCatsEffect
)

lazy val root = (project in file("."))
  .settings(projectSettings)
  .settings(parallelExecution in Test := false)
  .settings(test in assembly := {})
  .settings(assemblyJarName in assembly := projectName + ".jar")
  .settings(assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") => MergeStrategy.singleOrError
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  })
