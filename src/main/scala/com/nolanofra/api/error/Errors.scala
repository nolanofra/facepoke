package com.nolanofra.api.error

import scala.util.control.NoStackTrace

object Errors {
  sealed trait Errors extends NoStackTrace
  case class PokemonNotFound(description: String) extends Errors
  case class ServiceNotAvailable(description: String) extends Errors
  case class TooManyRequest(description: String) extends Errors
  case class InternalServerError(description: String) extends Errors
  case class PokeApiBadRequest(description: String) extends Errors
}
