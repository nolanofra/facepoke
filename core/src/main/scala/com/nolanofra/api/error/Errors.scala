package com.nolanofra.api.error

import cats.effect.IO
import io.circe.Decoder
import org.http4s.{ Response, Status }
import org.http4s.circe._
import cats.implicits.catsSyntaxApplicativeErrorId

import scala.util.control.NoStackTrace

object Errors {
  sealed trait Errors extends NoStackTrace
  case class PokemonNotFound(description: String) extends Errors
  case class ServiceNotAvailable(description: String) extends Errors
  case class TooManyRequest(description: String) extends Errors
  case class InternalServerError(description: String) extends Errors
  case class PokeApiBadRequest(description: String) extends Errors

  def handleHttpErrors[A: Decoder](response: Response[IO]) =
    response.status match {
      case Status.Ok => response.asJsonDecode[A]
      case Status.NotFound => PokemonNotFound(response.status.reason).raiseError[IO, A]
      case Status.TooManyRequests => TooManyRequest(response.status.reason).raiseError[IO, A]
      case Status.BadRequest => PokeApiBadRequest(response.status.reason).raiseError[IO, A]
      case _ => ServiceNotAvailable(response.status.reason).raiseError[IO, A]
    }
}
