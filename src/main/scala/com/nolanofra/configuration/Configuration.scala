package com.nolanofra.configuration

import cats.effect.IO
import com.typesafe.config.ConfigFactory

case class PokeApiEndpointConfiguration(baseUrl: String)

object Configuration {
  val serverHost = "0.0.0.0"
  val serverPort = 5000

  def loadPokemonEndpointBaseUrl: IO[PokeApiEndpointConfiguration] =
    IO.apply(ConfigFactory.load)
      .map(config => PokeApiEndpointConfiguration(config.getString("pokeAPI.baseUrl")))
}
