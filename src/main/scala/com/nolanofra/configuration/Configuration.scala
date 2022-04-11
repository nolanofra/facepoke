package com.nolanofra.configuration

import cats.effect.IO

import java.util.NoSuchElementException
import scala.util.Properties

case class PokemonApi(baseUrl: String)

object Configuration {
  val serverHost = "0.0.0.0"
  val serverPort = 8080

  def loadPokemonEndpointBaseUrl(): IO[PokemonApi] =
    Properties.envOrNone("POKEMON_API") match {
      case Some(url) => IO(PokemonApi(url))
      case _ => IO.raiseError(new NoSuchElementException("Env variable not found"))
    }
}
