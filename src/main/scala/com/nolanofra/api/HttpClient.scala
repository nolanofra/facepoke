package com.nolanofra.api

import cats.effect.{ IO, Resource }
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.client.middleware.Logger

case class HttpClient(client: Client[IO])

object HttpClient {

  def apply: Resource[IO, HttpClient] =
    BlazeClientBuilder[IO].resource
      .map(client => Logger(logHeaders = true, logBody = true)(client))
      .map(client => HttpClient(client))
}
