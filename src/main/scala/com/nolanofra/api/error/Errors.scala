package com.nolanofra.api.error

object Errors {
  sealed trait Errors extends Exception
  case class PokemonNotFound(description: String) extends Errors
  case class ServiceNotAvailable(description: String) extends Errors
  case class TooManyRequest(description: String) extends Errors
  case class InternalServerError(description: String) extends Errors
  case class BadRequest(description: String) extends Errors
}
