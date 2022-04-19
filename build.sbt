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
  scalafmtOnCompile := true,
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

lazy val tests = project
  .in(file("tests"))
  .settings(name := "tests")
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(projectSettings)
  .settings(
    libraryDependencies ++= Seq(
      testContainers,
      mockServer,
      mockServerClient
    )
  )
  .settings(parallelExecution in IntegrationTest := false)
  .enablePlugins(NoPublishPlugin)
  .settings(fork in IntegrationTest := true)
  .enablePlugins(DockerPlugin)
  .settings(dockerfile in docker := dockerFile((assembly in core).value))
  .dependsOn(core)

lazy val core = (project in file("."))
  .enablePlugins(NoPublishPlugin)
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

def dockerFile(dependsOn: File) = {
  val artifactTargetPath = s"/app/${dependsOn.name}"

  new Dockerfile {
    from("openjdk:11-jre")
    add(dependsOn, artifactTargetPath)
    entryPoint("java", "-jar", artifactTargetPath)
    expose(80)
    label("org.containers.image.source", s"https://github.com/azanin/${projectName}")
  }
}

lazy val facepoke = (project in file("."))
  .enablePlugins(NoPublishPlugin)
  .aggregate(core, tests)

addCommandAlias("integrationTests", ";project tests;docker;it:test")
