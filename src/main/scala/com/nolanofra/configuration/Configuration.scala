package com.nolanofra.configuration

import cats.effect.IO

case class PokemonApiConfigurations(baseUrl: String)

object Configuration {
  val serverHost = "0.0.0.0"
  val serverPort = 8080

  def loadPokemonEndpointBaseUrl: IO[PokemonApiConfigurations] =
    Some("https://pokeapi.co/api/v2/") match {
      //Properties.envOrNone("POKEMON_API") match {
      case Some(url) => IO(PokemonApiConfigurations(url))
      case _ => IO.raiseError(new NoSuchElementException("Env variable not found"))
    }
}
