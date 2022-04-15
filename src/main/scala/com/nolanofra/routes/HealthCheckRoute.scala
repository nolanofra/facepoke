package com.nolanofra.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io.{ ->, /, GET, Ok, Root, _ }

object HealthCheckRoute {

  def healthCheck: HttpRoutes[IO] =
    HttpRoutes.of[IO] { case GET -> Root / "health" => Ok() }
}
