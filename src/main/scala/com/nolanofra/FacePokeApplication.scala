package com.nolanofra

import cats.effect._
import com.nolanofra.configuration.Configuration
import com.nolanofra.routes.FacePokeHttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder

object FacePokeApplication extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(Configuration.serverPort, Configuration.serverHost)
      .withHttpApp(FacePokeHttpRoutes().facePokeRoutes) // alternative: apis
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
