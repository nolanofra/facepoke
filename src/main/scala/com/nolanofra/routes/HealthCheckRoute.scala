package com.nolanofra.routes

import cats.Monad
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object HealthCheckRoute {

  def healthCheck[F[_]](implicit F: Sync[F]): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] { case GET -> Root / "health" => Ok() }
  }
}
