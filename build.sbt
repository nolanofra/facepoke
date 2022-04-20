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

lazy val commonSettings = Seq(
  organization := "nolanofra",
  scalaVersion := "2.13.8",
  scalafmtOnCompile := true
)

lazy val core = (project in file("core"))
  .settings(commonSettings)
  .settings(name := projectName)
  .settings(
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
  )
  .settings(parallelExecution in Test := false)
  .settings(test in assembly := {})
  .settings(assemblyJarName in assembly := projectName + ".jar")
  .enablePlugins(DockerPlugin)
  .settings(dockerfile in docker := {
    dockerFile(assembly.value)
  })
  .settings(assemblyMergeStrategy in assembly := {
    case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") => MergeStrategy.singleOrError
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  })

lazy val tests = project
  .in(file("tests"))
  .settings(name := "tests")
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      testContainers,
      mockServer,
      mockServerClient,
      sttpClient
    )
  )
  .settings(parallelExecution in IntegrationTest := false)
  .enablePlugins(NoPublishPlugin)
  .settings(fork in IntegrationTest := true)
  .enablePlugins(DockerPlugin)
  .settings(dockerfile in docker := dockerFile((assembly in core).value))
  .dependsOn(core)

def dockerFile(dependsOn: File) = {
  val artifactTargetPath = s"/app/${dependsOn.name}"

  new Dockerfile {
    from("openjdk:11-jre")
    add(dependsOn, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
    expose(5000)
    label("org.containers.image.source", s"https://github.com/nolanofra/${projectName}")
  }
}

lazy val facepoke = (project in file("."))
  .enablePlugins(NoPublishPlugin)
  .aggregate(core, tests)

addCommandAlias("integrationTests", ";project tests;docker;it:test")
