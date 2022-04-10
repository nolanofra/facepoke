package com.nolanofra.routes

import cats.effect.IO
import org.http4s.server.Router

class FacePokeHttpRoutes private {

  private val healthCheckRoute = HealthCheckRoute.healthCheck[IO]

  val facePokeRoutes = Router(
    "/" -> healthCheckRoute
  ).orNotFound
}

object FacePokeHttpRoutes {
  def apply() = new FacePokeHttpRoutes
}
